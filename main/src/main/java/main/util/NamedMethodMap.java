package main.util;

import main.analyzer.backward.MethodWrapper;
import main.slicer.backward.MethodCallSiteInfo;
import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NamedMethodMap {

    private static Map<String, MethodWrapper> nameVsMethodMap = null;

    private static boolean isCallerCalleeBuilt = false;

    public static void build(List<String> classNames) {

        if (nameVsMethodMap == null) {

            nameVsMethodMap = new HashMap<>();

            for (String className : classNames) {
                SootClass sClass = Scene.v().getSootClass(className);

                fillMethodMapForClass(sClass);
            }
        }
    }

    public static void clearCallerCalleeGraph() {
        nameVsMethodMap = null;
        isCallerCalleeBuilt = false;
    }

    public static void addCriteriaClass(String className) {

        SootClass sClass = Scene.v().getSootClass(className);
        if (sClass.isPhantomClass()) {
            return;
        }

        fillMethodMapForClass(sClass);
    }

    public static void addCriteriaClasses(List<String> classNames) {
        for (String clazz : classNames) {
            addCriteriaClass(clazz);
        }
    }

    public static MethodWrapper getMethod(String methodName) {

        if (nameVsMethodMap == null) {
            throw new RuntimeException("Name vs Method Map is not built ...");
        } else {
            return nameVsMethodMap.get(methodName);
        }
    }

    private static void fillMethodMapForClass(SootClass sClass) {
        for (SootMethod m : sClass.getMethods()) {
            if (nameVsMethodMap.get(m.toString()) == null) {
                MethodWrapper methodWrapper = new MethodWrapper(m);
                nameVsMethodMap.put(m.toString(), methodWrapper);
            }
        }
    }

    public static void buildCallerCalleeRelation(List<String> classNames) {

        if (isCallerCalleeBuilt) {
            return;
        }

        isCallerCalleeBuilt = true;

        Map<String, List<SootClass>> classHierarchy = Utils.getClassHierarchyAnalysis(classNames);

        for (String className : classNames) {
            SootClass sClass = Scene.v().getSootClass(className);

            Iterator methodIt = sClass.getMethods().iterator();
            while (methodIt.hasNext()) {
                SootMethod m = (SootMethod) methodIt.next();

                if (m.isConcrete()) {
                    Body b;
                    try {
                        b = m.retrieveActiveBody();
                    } catch (RuntimeException e) {
                        System.err.println(e);
                        continue;
                    }

                    MethodWrapper caller = NamedMethodMap.getMethod(m.toString());
                    UnitGraph graph = new ExceptionalUnitGraph(b);

                    Iterator gIt = graph.iterator();

                    while (gIt.hasNext()) {
                        Unit u = (Unit) gIt.next();

                        String uStr = u.toString();

                        if (uStr.contains("}") || uStr.contains("{") || uStr.contains(";")) {
                            continue;
                        }

                        if (uStr.contains("staticinvoke ") || uStr.contains("void <init>")) {
                            String invokedMethod = uStr.substring(uStr.indexOf('<'), uStr.lastIndexOf('>') + 1);
                            MethodWrapper callee = NamedMethodMap.getMethod(invokedMethod);

                            if (callee != null && caller != callee) {
                                callee.setTopLevel(false);

                                MethodCallSiteInfo callSiteInfo = new MethodCallSiteInfo(caller, callee,
                                        u.getJavaSourceStartLineNumber(), u.getJavaSourceStartColumnNumber());

                                caller.getCalleeList().add(callSiteInfo);
                                callee.getCallerList().add(caller);

                            }
                        } else if ((uStr.contains("virtualinvoke ") ||
                                uStr.contains("interfaceinvoke ") ||
                                uStr.contains("specialinvoke ") ||
                                uStr.contains("dynamicinvoke ")) && uStr.contains(".<")) {
                            String invokedMethod = uStr.substring(uStr.indexOf('<'), uStr.lastIndexOf('>') + 1);
                            String reference = uStr.substring(uStr.indexOf("invoke ") + 7, uStr.indexOf(".<"));

                            String refType = null;

                            for (ValueBox useBox : u.getUseBoxes()) {
                                if (useBox.getValue().toString().equals(reference)) {
                                    refType = useBox.getValue().getType().toString();
                                    break;
                                }
                            }

                            String[] splits = invokedMethod.split(": ");
                            String methodSignature = splits[1].substring(0, splits[1].lastIndexOf('>'));
                            List<SootClass> subClasses = classHierarchy.get(refType);

                            if (subClasses != null && !subClasses.isEmpty()) {

                                for (SootClass subClass : subClasses) {
                                    SootMethod subClassMethod = subClass.getMethodUnsafe(methodSignature);

                                    if (subClassMethod != null) {
                                        MethodWrapper callee = NamedMethodMap.getMethod(subClassMethod.toString());

                                        if (callee != null && caller != callee) {
                                            callee.setTopLevel(false);

                                            MethodCallSiteInfo callSiteInfo = new MethodCallSiteInfo(caller, callee,
                                                    u.getJavaSourceStartLineNumber(), u.getJavaSourceStartColumnNumber());
                                            caller.getCalleeList().add(callSiteInfo);
                                            callee.getCallerList().add(caller);
                                        }
                                    }
                                }
                            } else {
                                MethodWrapper callee = NamedMethodMap.getMethod(invokedMethod);

                                if (callee != null && caller != callee) {
                                    callee.setTopLevel(false);

                                    MethodCallSiteInfo callSiteInfo = new MethodCallSiteInfo(caller, callee,
                                            u.getJavaSourceStartLineNumber(), u.getJavaSourceStartColumnNumber());
                                    caller.getCalleeList().add(callSiteInfo);
                                    callee.getCallerList().add(caller);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

}

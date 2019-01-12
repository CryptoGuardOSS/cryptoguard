package main.rule.base;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.AssignInvokeUnitContainer;
import main.analyzer.backward.InvokeUnitContainer;
import main.analyzer.backward.UnitContainer;
import main.util.Utils;
import soot.ByteType;
import soot.IntegerType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.RValueBox;

import java.util.*;

/**
 * Created by krishnokoli on 11/26/17.
 */
public abstract class PredictableSourceRuleChecker extends BaseRuleChecker {

    public static final List<String> PREDICTABLE_SOURCES = new ArrayList<>();

    static {
        PREDICTABLE_SOURCES.add("<java.lang.System: long nanoTime()>");
        PREDICTABLE_SOURCES.add("<java.lang.System: long currentTimeMillis()>");
        PREDICTABLE_SOURCES.add("<java.util.Date: java.util.Date <init>");
    }

    // Todo: Add a field to keep track of all the predictable sources ...

    private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
    private Map<UnitContainer, List<String>> othersSourceMap = new HashMap<>();

    @Override
    public void analyzeSlice(Analysis analysis) {

        if (analysis.getAnalysisResult().isEmpty()) {
            return;
        }

        Set<String> usedFields = new HashSet<>();
        Set<String> usedConstants = new HashSet<>();

        for (int index = 0; index < analysis.getAnalysisResult().size(); index++) {
            UnitContainer e = analysis.getAnalysisResult().get(index);

            if (e instanceof AssignInvokeUnitContainer) {
                Set<String> fields = ((AssignInvokeUnitContainer) e).getProperties();
                usedFields.addAll(fields);

                if (e.getUnit().toString().contains("interfaceinvoke ")) {
                    for (ValueBox usebox : e.getUnit().getUseBoxes()) {
                        if (usebox.getValue() instanceof Constant) {
                            usedConstants.add(usebox.getValue().toString());
                        }
                    }
                }
            }
        }

        for (int index = 0; index < analysis.getAnalysisResult().size(); index++) {

            UnitContainer e = analysis.getAnalysisResult().get(index);

            Map<UnitContainer, String> outSet = new HashMap<>();


            if (e instanceof AssignInvokeUnitContainer) {
                List<UnitContainer> resFromInside = ((AssignInvokeUnitContainer) e).getAnalysisResult();
                checkPredictableSource(resFromInside, e, outSet);

            } else {
                for (String predictableSource : PREDICTABLE_SOURCES) {
                    if (e.getUnit().toString().contains(predictableSource)) {
                        outSet.put(e, e.toString());
                        break;
                    }
                }
            }

            if (e instanceof AssignInvokeUnitContainer) {
                List<UnitContainer> result = ((AssignInvokeUnitContainer) e).getAnalysisResult();

                if (result != null) {
                    for (UnitContainer unit : result) {
                        checkHeuristics(unit, outSet);
                    }
                }

            } else {
                checkHeuristics(e, outSet);
            }

            if (outSet.isEmpty()) {
                continue;
            }

            InvokeUnitContainer invokeResult = new InvokeUnitContainer();

            if (Utils.isArgumentOfInvoke(analysis, index, new ArrayList<UnitContainer>(), usedFields, invokeResult)) {

                Map<UnitContainer, String> newOutset = new HashMap<>();

                if (e.getUnit().toString().contains("specialinvoke") &&
                        (invokeResult.getDefinedFields().isEmpty() || invokeResult.getUnit() != null)) {
                    for (UnitContainer unitContainer : outSet.keySet()) {
                        putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                    }
                } else {

                    if (e.getUnit() instanceof JInvokeStmt && e.getUnit().toString().contains("interfaceinvoke")) {

                        boolean found = false;

                        for (String constant : usedConstants) {
                            if (((JInvokeStmt) e.getUnit()).getInvokeExpr().getArg(0).toString().contains(constant)) {
                                putIntoMap(predictableSourcMap, e, outSet.get(e));
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            putIntoMap(othersSourceMap, e, outSet.get(e));
                        }

                    } else {
                        for (UnitContainer unitContainer : outSet.keySet()) {
                            putIntoMap(othersSourceMap, unitContainer, outSet.get(unitContainer));
                        }
                    }
                }

                List<UnitContainer> resFromInside = invokeResult.getAnalysisResult();

                if (!resFromInside.isEmpty()) {
                    checkPredictableSource(resFromInside, newOutset);

                } else {
                    for (String predictableSource : PREDICTABLE_SOURCES) {
                        if (e.getUnit().toString().contains(predictableSource)) {
                            newOutset.put(e, e.toString());
                            break;
                        }
                    }
                }

                for (UnitContainer unit : invokeResult.getAnalysisResult()) {
                    checkHeuristics(unit, newOutset);
                }

                for (UnitContainer unitContainer : newOutset.keySet()) {
                    putIntoMap(predictableSourcMap, unitContainer, newOutset.get(unitContainer));
                }

            } else {

                for (UnitContainer unitContainer : outSet.keySet()) {
                    putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                }
            }
        }
    }

    private void checkPredictableSource(List<UnitContainer> result, UnitContainer e, Map<UnitContainer, String> outSet) {
        for (UnitContainer key : result) {
            for (String predictableSource : PREDICTABLE_SOURCES) {
                if (key.getUnit().toString().contains(predictableSource)) {
                    outSet.put(e, e.toString());
                }
            }
        }
    }

    private void checkPredictableSource(List<UnitContainer> result, Map<UnitContainer, String> outSet) {
        for (UnitContainer key : result) {
            for (String predictableSource : PREDICTABLE_SOURCES) {
                if (key.getUnit().toString().contains(predictableSource)) {
                    outSet.put(key, key.toString());
                }
            }
        }
    }

    private void checkHeuristics(UnitContainer e, Map<UnitContainer, String> outSet) {

        for (ValueBox usebox : e.getUnit().getUseBoxes()) {
            if (usebox.getValue() instanceof Constant) {

                if (usebox.getValue().toString().equals("null") ||
                        usebox.getValue().toString().equals("\"\"")) {
                    putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                    continue;
                }

                if (usebox.getValue().getType() instanceof IntegerType) {

                    List<ValueBox> defBoxes = e.getUnit().getDefBoxes();

                    if (defBoxes != null && !defBoxes.isEmpty()) {
                        if (usebox instanceof RValueBox && defBoxes.get(0).getValue().getType() instanceof ByteType) {
                            outSet.put(e, usebox.getValue().toString());
                        } else {
                            putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                        }
                    }
                } else if (e.getUnit() instanceof JAssignStmt) {
                    if (((AssignStmt) e.getUnit()).containsInvokeExpr()) {
                        InvokeExpr invokeExpr = ((AssignStmt) e.getUnit()).getInvokeExpr();
                        List<Value> args = invokeExpr.getArgs();
                        for (Value arg : args) {
                            if (arg.equivTo(usebox.getValue())) {
                                putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                                break;
                            }
                        }
                    } else {
                        outSet.put(e, usebox.getValue().toString());
                    }

                } else if (e.getUnit().toString().contains(" newarray ")) {
                    putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                } else {
                    outSet.put(e, usebox.getValue().toString());
                }
            }
        }
    }

    public void printAnalysisOutput(Map<String, String> configFiles) {

        String rule = getRuleId();
        String ruleDesc = RULE_VS_DESCRIPTION.get(rule);

        List<String> predictableSources = new ArrayList<>();
        List<UnitContainer> predictableSourceInst = new ArrayList<>();
        List<UnitContainer> othersSourceInst = new ArrayList<>();
        List<String> others = new ArrayList<>();

        for (List<String> values : predictableSourcMap.values()) {
            predictableSources.addAll(values);
        }
        predictableSourceInst.addAll(predictableSourcMap.keySet());

        for (List<String> values : othersSourceMap.values()) {
            others.addAll(values);
        }
        othersSourceInst.addAll(othersSourceMap.keySet());

        if (!othersSourceMap.isEmpty()) {
            System.out.println("=======================================");
            String output = getPrintableMsg(othersSourceMap, rule + "a", ruleDesc);
            System.out.println(output);
            System.out.println("=======================================");
        }

        if (!predictableSourcMap.isEmpty()) {
            System.out.println("=======================================");
            String output = getPrintableMsg(predictableSourcMap, rule, ruleDesc);
            System.out.println(output);
            System.out.println("=======================================");
        }
    }

    private String getPrintableMsg(Map<UnitContainer, List<String>> predictableSourcMap, String rule, String ruleDesc) {
        String output = "***Violated Rule " +
                rule + ": " +
                ruleDesc;

        for (UnitContainer unit : predictableSourcMap.keySet()) {

            output += "\n***Found: " + predictableSourcMap.get(unit);
            if (unit.getUnit().getJavaSourceStartLineNumber() >= 0) {
                output += " in Line " + unit.getUnit().getJavaSourceStartLineNumber();
            }

            output += " in Method: " + unit.getMethod();
        }

        return output;
    }

    abstract public String getRuleId();
}

package main.rule;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.AssignInvokeUnitContainer;
import main.analyzer.backward.InvokeUnitContainer;
import main.analyzer.backward.UnitContainer;
import main.rule.base.BaseRuleChecker;

import main.rule.engine.Criteria;
import main.util.Utils;
import soot.IntType;

import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.RValueBox;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by krishnokoli on 10/22/17.
 */
public class PBEInterationCountFinder extends BaseRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();


    private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
    private Map<UnitContainer, List<String>> othersSourceMap = new HashMap<>();

    static {

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("javax.crypto.spec.PBEParameterSpec");
        criteria2.setMethodName("void <init>(byte[],int)");
        criteria2.setParam(1);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("javax.crypto.spec.PBEParameterSpec");
        criteria3.setMethodName("void <init>(byte[],int,java.security.spec.AlgorithmParameterSpec)");
        criteria3.setParam(1);
        CRITERIA_LIST.add(criteria3);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("javax.crypto.spec.PBEKeySpec");
        criteria4.setMethodName("void <init>(char[],byte[],int,int)");
        criteria4.setParam(2);
        CRITERIA_LIST.add(criteria4);

        Criteria criteria5 = new Criteria();
        criteria5.setClassName("javax.crypto.spec.PBEKeySpec");
        criteria5.setMethodName("void <init>(char[],byte[],int)");
        criteria5.setParam(2);
        CRITERIA_LIST.add(criteria5);

    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    @Override
    public void analyzeSlice(Analysis analysis) {
        if (analysis.getAnalysisResult().isEmpty()) {
            return;
        }

        HashMap<String, List<String>> callerVsUsedConstants = new HashMap<>();

        for (int index = 0; index < analysis.getAnalysisResult().size(); index++) {
            UnitContainer e = analysis.getAnalysisResult().get(index);

            if (!(e instanceof AssignInvokeUnitContainer) && e.getUnit() instanceof JAssignStmt) {

                List<String> usedConstants = callerVsUsedConstants.get(e.getMethod());

                if (usedConstants == null) {
                    usedConstants = new ArrayList<>();
                    callerVsUsedConstants.put(e.getMethod(), usedConstants);
                }

                if (e.getUnit().toString().contains("interfaceinvoke ")) {
                    for (ValueBox usebox : e.getUnit().getUseBoxes()) {
                        if (usebox.getValue() instanceof Constant) {
                            usedConstants.add(usebox.getValue().toString());
                        }
                    }
                }
            }

            Map<UnitContainer, String> outSet = new HashMap<>();

            if (e instanceof AssignInvokeUnitContainer) {
                List<UnitContainer> result = ((AssignInvokeUnitContainer) e).getAnalysisResult();
                if (result != null) {
                    for (UnitContainer unit : result) {
                        checkHeuristics(unit, outSet);
                    }
                }
            } else if (e instanceof InvokeUnitContainer) {
                List<UnitContainer> result = ((InvokeUnitContainer) e).getAnalysisResult();
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

            UnitContainer invokeResult = Utils.isArgumentOfInvoke(analysis, index, new ArrayList<UnitContainer>());

            if (invokeResult != null && invokeResult instanceof InvokeUnitContainer) {

                if ((((InvokeUnitContainer) invokeResult).getDefinedFields().isEmpty() || !((InvokeUnitContainer) invokeResult).getArgs().isEmpty())
                        && invokeResult.getUnit().toString().contains("specialinvoke")) {

                    for (UnitContainer unitContainer : outSet.keySet()) {
                        putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                    }
                }
            } else if (invokeResult != null && invokeResult.getUnit() instanceof JInvokeStmt) {
                if (invokeResult.getUnit().toString().contains("specialinvoke")) {

                    for (UnitContainer unitContainer : outSet.keySet()) {
                        putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                    }
                } else {
                    for (UnitContainer unitContainer : outSet.keySet()) {
                        if (unitContainer.getUnit() instanceof JInvokeStmt && unitContainer.getUnit().toString().contains("interfaceinvoke")) {

                            boolean found = false;

                            for (String constant : callerVsUsedConstants.get(e.getMethod())) {
                                if (((JInvokeStmt) unitContainer.getUnit()).getInvokeExpr().getArg(0).toString().contains(constant)) {
                                    putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                putIntoMap(othersSourceMap, unitContainer, outSet.get(unitContainer));
                            }

                        } else {
                            putIntoMap(othersSourceMap, unitContainer, outSet.get(unitContainer));
                        }
                    }
                }
            } else {
                for (UnitContainer unitContainer : outSet.keySet()) {
                    putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                }
            }
        }
    }

    private void checkHeuristics(UnitContainer e, Map<UnitContainer, String> outSet) {

        for (ValueBox usebox : e.getUnit().getUseBoxes()) {

            if (usebox.getValue() instanceof Constant &&
                    !Utils.isArgOfByteArrayCreation(usebox, e.getUnit()) &&
                    !e.getUnit().toString().contains("[" + usebox.getValue() + "]")) {

                if (usebox.getValue().getType() instanceof IntType &&
                        Integer.valueOf(usebox.getValue().toString()) >= 0 &&
                        Integer.valueOf(usebox.getValue().toString()) < 1000) {
                    if (e.getUnit() instanceof JAssignStmt && ((AssignStmt) e.getUnit()).containsInvokeExpr()) {
                        InvokeExpr invokeExpr = ((AssignStmt) e.getUnit()).getInvokeExpr();
                        List<Value> args = invokeExpr.getArgs();
                        for (Value arg : args) {
                            if (arg.equivTo(usebox.getValue())) {
                                putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                                break;
                            }
                        }
                    } else {
                        if (e.getUnit().toString().contains(" = " + usebox.getValue())) {
                            outSet.put(e, usebox.getValue().toString());
                        } else {
                            putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                        }
                    }

                } else {
                    putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                }

            } else {
                putIntoMap(othersSourceMap, e, usebox.getValue().toString());
            }
        }
    }

    public void printAnalysisOutput(Map<String, String> configFiles) {

        String rule = "8";
        String ruleDesc = RULE_VS_DESCRIPTION.get(rule);

        List<String> predictableSources = new ArrayList<>();
        List<UnitContainer> predictableSourceInst = new ArrayList<>();
        List<String> others = new ArrayList<>();

        for (List<String> values : predictableSourcMap.values()) {
            predictableSources.addAll(values);
        }
        predictableSourceInst.addAll(predictableSourcMap.keySet());

        for (List<String> values : othersSourceMap.values()) {
            others.addAll(values);
        }

        if (!predictableSources.isEmpty()) {
            System.out.println("=======================================");
            String output = getPrintableMsg(predictableSources, rule, ruleDesc);
            System.out.println(output);
            System.out.println(predictableSourceInst);
            System.out.println("=======================================");
        }

        if (!others.isEmpty()) {
            System.out.println("=======================================");
            String output = getOthersToPrint(configFiles, others, rule, ruleDesc);
            System.out.println(output);
            System.out.println("=======================================");
        }
    }

    private String getPrintableMsg(Collection<String> constants, String rule, String ruleDesc) {
        return "***Violated Rule " +
                rule + ": " +
                ruleDesc +
                " ***Constants: " +
                constants;
    }

    private String getOthersToPrint(Map<String, String> xmlFileStr, Collection<String> others, String rule, String ruleDesc) {

        StringBuilder output = new StringBuilder(getPrintableMsg(others, rule + "a", ruleDesc));

        for (String config : others) {
            for (String configFile : xmlFileStr.keySet()) {

                String val = config.replace("\"", "");
                Pattern p = Pattern.compile("[^a-zA-Z.]");
                boolean hasSpecialChar = p.matcher(val).find();

                if (!hasSpecialChar) {
                    val = ">" + val + "<";

                    String[] lines = xmlFileStr.get(configFile).split("\n");

                    for (int index = 0; index < lines.length; index++) {
                        if (lines[index].contains(val)) {

                            if (index + 1 < lines.length) {
                                output.append(" ***Config: ")
                                        .append(config)
                                        .append(" in line: ")
                                        .append(lines[index].trim())
                                        .append(" with value: ")
                                        .append(lines[index + 1].trim())
                                        .append(" in file: ")
                                        .append(configFile);
                            } else {
                                output.append(" ***Config: ")
                                        .append(config)
                                        .append(" in line: ")
                                        .append(lines[index].trim())
                                        .append(" in file: ")
                                        .append(configFile);
                            }
                        }
                    }

                }
            }
        }

        return output.toString();
    }
}

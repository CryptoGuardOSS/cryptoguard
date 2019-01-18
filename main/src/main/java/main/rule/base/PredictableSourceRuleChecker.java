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
import java.util.regex.Pattern;

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

                if ((invokeResult.getDefinedFields().isEmpty() || !invokeResult.getArgs().isEmpty())
                        && invokeResult.getUnit().toString().contains("specialinvoke")) {

                    for (UnitContainer unitContainer : outSet.keySet()) {
                        putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                    }
                } else {

                    for (UnitContainer unitContainer : outSet.keySet()) {
                        if (unitContainer.getUnit() instanceof JInvokeStmt && unitContainer.getUnit().toString().contains("interfaceinvoke")) {

                            boolean found = false;

                            for (String constant : usedConstants) {
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

                if (e.getUnit().toString().contains("[" + usebox.getValue() + "]")) {
                    putIntoMap(othersSourceMap, e, usebox.getValue().toString());
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
            System.out.println(othersSourceInst);
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

    abstract public String getRuleId();
}

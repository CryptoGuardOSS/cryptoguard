package main.rule;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.AssignInvokeUnitContainer;
import main.analyzer.backward.InvokeUnitContainer;
import main.analyzer.backward.UnitContainer;
import main.rule.base.BaseRuleChecker;

import main.rule.engine.Criteria;
import main.util.Utils;

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
 * Created by krishnokoli on 11/15/17.
 */
public class ExportGradeKeyInitializationFinder extends BaseRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("java.security.KeyPairGenerator");
        criteria2.setMethodName("void initialize(int)");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("java.security.KeyPairGenerator");
        criteria3.setMethodName("void initialize(int,java.security.SecureRandom)");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("java.security.KeyPairGenerator");
        criteria4.setMethodName("void initialize(java.security.spec.AlgorithmParameterSpec)");
        criteria4.setParam(0);
        CRITERIA_LIST.add(criteria4);

        Criteria criteria5 = new Criteria();
        criteria5.setClassName("java.security.KeyPairGenerator");
        criteria5.setMethodName("void initialize(java.security.spec.AlgorithmParameterSpec,java.security.SecureRandom)");
        criteria5.setParam(0);
        CRITERIA_LIST.add(criteria5);

    }

    private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
    private Map<UnitContainer, List<String>> othersSourceMap = new HashMap<>();

    private int minSize;

    private ArrayList<String> initializationCallsites;

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    @Override
    public void analyzeSlice(Analysis analysis) {
        if (analysis.getAnalysisResult().isEmpty()) {
            return;
        }

        String[] splits = analysis.getMethodChain().split("--->");
        String keyInitializationSite = splits[splits.length - 2];

        if (!initializationCallsites.toString().contains(keyInitializationSite)) {
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
                if (e.getUnit() instanceof AssignStmt && usebox.getValue().getType() instanceof IntegerType) {

                    int value = Integer.valueOf(usebox.getValue().toString());

                    if (usebox instanceof RValueBox && value != 0 && value % 2 == 0 && value < minSize) {

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
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public void setInitializationCallsites(ArrayList<String> initializationCallsites) {
        this.initializationCallsites = initializationCallsites;
    }

    @Override
    public void printAnalysisOutput(Map<String, String> xmlFileStr) {
        String rule = "5";
        String ruleDesc = RULE_VS_DESCRIPTION.get(rule);

        List<String> predictableSources = new ArrayList<>();
        List<UnitContainer> predictableSourceInsts = new ArrayList<>();

        for (List<String> values : predictableSourcMap.values()) {
            predictableSources.addAll(values);
        }

        predictableSourceInsts.addAll(predictableSourcMap.keySet());

        if (!predictableSources.isEmpty()) {
            System.out.println("=======================================");
            String output = getPrintableMsg(predictableSources, rule, ruleDesc);
            System.out.println(output);
            System.out.println(predictableSourceInsts);
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
}

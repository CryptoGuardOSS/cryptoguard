package main.rule;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.UnitContainer;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.rule.base.BaseRuleChecker;
import main.rule.base.MajorHeuristics;
import main.rule.engine.Criteria;
import soot.IntegerType;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.internal.RValueBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by krishnokoli on 11/15/17.
 *
 * @author krishnokoli
 * @since V01.00.00
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
    private final String rule = "5";
    private final String ruleDesc = RULE_VS_DESCRIPTION.get(rule);

    private ArrayList<String> initializationCallsites;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    /**
     * {@inheritDoc}
     */
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

        for (int index = 0; index < analysis.getAnalysisResult().size(); index++) {
            UnitContainer e = analysis.getAnalysisResult().get(index);

            for (ValueBox usebox : e.getUnit().getUseBoxes()) {
                if (usebox.getValue() instanceof Constant) {
                    if (e.getUnit() instanceof AssignStmt && usebox.getValue().getType() instanceof IntegerType) {

                        int value = Integer.valueOf(usebox.getValue().toString());

                        if (usebox instanceof RValueBox && value != 0 && value % 2 == 0 && value < minSize) {

                            List<UnitContainer> outSet = new ArrayList<>();
                            outSet.add(e);

                            if (MajorHeuristics.isArgumentOfInvoke(analysis, index, outSet)) {
                                putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                            } else if (MajorHeuristics.isArgumentOfByteArrayCreation(analysis, index, outSet)) {
                                putIntoMap(othersSourceMap, e, e.getUnit().toString());
                            } else {
                                putIntoMap(predictableSourcMap, e, usebox.getValue().toString());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * <p>Setter for the field <code>minSize</code>.</p>
     *
     * @param minSize a int.
     */
    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    /**
     * <p>Setter for the field <code>initializationCallsites</code>.</p>
     *
     * @param initializationCallsites a {@link java.util.ArrayList} object.
     */
    public void setInitializationCallsites(ArrayList<String> initializationCallsites) {
        this.initializationCallsites = initializationCallsites;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printAnalysisOutput(Map<String, String> xmlFileStr) {

        List<String> predictableSources = new ArrayList<>();
        List<UnitContainer> predictableSourceInsts = new ArrayList<>();

        for (List<String> values : predictableSourcMap.values()) {
            predictableSources.addAll(values);
        }

        predictableSourceInsts.addAll(predictableSourcMap.keySet());

        if (!predictableSources.isEmpty()) {
            System.out.println("=======================================");
            String output = getPrintableMsg(predictableSourcMap, rule, ruleDesc);
            System.out.println(output);
            System.out.println("=======================================");
        }
    }

    @Override
    public ArrayList<AnalysisIssue> createAnalysisOutput(Map<String, String> xmlFileStr, List<String> sourcePaths) {
        ArrayList<AnalysisIssue> outList = new ArrayList<>();

        for (UnitContainer unit : predictableSourcMap.keySet()) {
            String sootString = predictableSourcMap.get(unit).size() <= 0
                    ? ""
                    : "Found: " + predictableSourcMap.get(unit).get(0);
            outList.add(new AnalysisIssue(unit, Integer.parseInt(rule), sootString, sourcePaths));
        }

        return outList;
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
}

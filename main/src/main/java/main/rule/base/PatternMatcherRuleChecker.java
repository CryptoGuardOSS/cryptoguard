package main.rule.base;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.UnitContainer;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import soot.ValueBox;
import soot.jimple.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Abstract PatternMatcherRuleChecker class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public abstract class PatternMatcherRuleChecker extends BaseRuleChecker {

    //Todo: Add a field to keep track of all the found patterns ...

    private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
    private Map<UnitContainer, List<String>> othersSourceMap = new HashMap<>();
    private final String rule = getRuleId();
    private final String ruleDesc = RULE_VS_DESCRIPTION.get(rule);


    /**
     * {@inheritDoc}
     */
    @Override
    public void analyzeSlice(Analysis analysis) {
        if (analysis.getAnalysisResult().isEmpty()) {
            return;
        }

        for (UnitContainer e : analysis.getAnalysisResult()) {
            for (ValueBox usebox : e.getUnit().getUseBoxes()) {
                if (usebox.getValue() instanceof Constant) {
                    boolean found = false;

                    for (String regex : getPatternsToMatch()) {
                        if (usebox.getValue().toString().matches(regex)) {
                            putIntoMap(predictableSourcMap, e, usebox.getValue().toString());
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<AnalysisIssue> createAnalysisOutput(Map<String, String> xmlFileStr) {
        ArrayList<AnalysisIssue> outList = new ArrayList<>();

        for (UnitContainer unit : predictableSourcMap.keySet()) {
            String sootString = predictableSourcMap.get(unit).size() <= 0
                    ? ""
                    : "Found: " + predictableSourcMap.get(unit).get(0);
            outList.add(new AnalysisIssue(unit, Integer.parseInt(rule), sootString));
        }

        return outList;
    }

    /**
     * {@inheritDoc}
     */
    public void printAnalysisOutput(Map<String, String> configFiles) {

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
            String output = getPrintableMsg(predictableSourcMap);
            System.out.println(output);

            System.out.println("=======================================");
        }
    }

    private String getPrintableMsg(Map<UnitContainer, List<String>> predictableSourcMap) {
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

    /**
     * <p>getPatternsToMatch.</p>
     *
     * @return a {@link java.util.List} object.
     */
    abstract public List<String> getPatternsToMatch();

    /**
     * <p>getRuleId.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    abstract public String getRuleId();
}

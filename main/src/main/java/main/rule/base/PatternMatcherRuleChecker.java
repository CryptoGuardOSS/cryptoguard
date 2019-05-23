package main.rule.base;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.AssignInvokeUnitContainer;
import main.analyzer.backward.InvokeUnitContainer;
import main.analyzer.backward.UnitContainer;
import soot.ValueBox;
import soot.jimple.Constant;

import java.util.*;
import java.util.regex.Pattern;

public abstract class PatternMatcherRuleChecker extends BaseRuleChecker {

    //Todo: Add a field to keep track of all the found patterns ...

    private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
    private Map<UnitContainer, List<String>> othersSourceMap = new HashMap<>();

    @Override
    public void analyzeSlice(Analysis analysis) {
        if (analysis.getAnalysisResult().isEmpty()) {
            return;
        }

        for (UnitContainer e : analysis.getAnalysisResult()) {

            if (e instanceof AssignInvokeUnitContainer) {
                List<UnitContainer> resFromInside = ((AssignInvokeUnitContainer) e).getAnalysisResult();

                for (UnitContainer unit : resFromInside) {
                    checkForMatch(unit);
                }
            }

            if (e instanceof InvokeUnitContainer) {
                List<UnitContainer> resFromInside = ((InvokeUnitContainer) e).getAnalysisResult();

                for (UnitContainer unit : resFromInside) {
                    checkForMatch(unit);
                }
            }

            checkForMatchInternal(e);
        }
    }

    private void checkForMatch(UnitContainer e) {

        if (e instanceof AssignInvokeUnitContainer) {
            List<UnitContainer> resFromInside = ((AssignInvokeUnitContainer) e).getAnalysisResult();

            for (UnitContainer unit : resFromInside) {
                checkForMatch(unit);
            }
        }

        if (e instanceof InvokeUnitContainer) {
            List<UnitContainer> resFromInside = ((InvokeUnitContainer) e).getAnalysisResult();

            for (UnitContainer unit : resFromInside) {
                checkForMatch(unit);
            }
        }

        checkForMatchInternal(e);
    }

    private void checkForMatchInternal(UnitContainer e) {
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

    public void printAnalysisOutput(Map<String, String> configFiles) {

        String rule = getRuleId();
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

    abstract public List<String> getPatternsToMatch();

    abstract public String getRuleId();
}

package main.frontEnd.MessagingSystem.routing.outputStructures;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.AnalysisLocation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.EngineType;
import main.rule.engine.RuleList;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * The class containing the implementation of the legacy output.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V01.00.01
 */
public class Legacy implements OutputStructure {

    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output. Currently mimics the output as best seen.
     */
    public String getOutput(EnvironmentInformation source, ArrayList<AnalysisIssue> brokenRules) {
        StringBuilder output = new StringBuilder();

        //reopening the console stream
        source.openConsoleStream();

        output.append("Analyzing " + source.getSourceType() + ": ");

        for (int sourceKtr = 0; sourceKtr < source.getSource().size(); sourceKtr++) {
            output.append(source.getSource().get(sourceKtr));

            if (sourceKtr != source.getSource().size() - 1)
                output.append(",");
        }
        output.append("\n");

        //Only printing console output if it is set and there is output captured
        if (source.getInternalErrors() != null && source.getInternalErrors().split("\n").length >= 1) {
            output.append("=======================================\n");
            output.append("Internal Warnings: \n" + source.getInternalErrors() + "\n");
            output.append("=======================================\n");
        }

        Map<Integer, List<AnalysisIssue>> groupedRules = new HashMap<>();
        if (brokenRules != null)
            for (AnalysisIssue issue : brokenRules) {
                List<AnalysisIssue> tempList;
                if (groupedRules.containsKey(issue.getRuleId())) {
                    tempList = new ArrayList<>(groupedRules.get(issue.getRuleId()));
                    tempList.add(issue);
                } else {
                    tempList = Collections.singletonList(issue);
                }
                groupedRules.put(issue.getRuleId(), tempList);
            }

        //region Changing the order of the rules
        Set<Integer> ruleOrdering = new HashSet<>();
        if (true) {
            Integer[] paperBasedOrdering = new Integer[]{3, 14, 6, 4, 12, 7, 11, 13, 9, 1, 10, 8, 5, 2};
            for (Integer rule : paperBasedOrdering)
                if (groupedRules.containsKey(rule))
                    ruleOrdering.add(rule);
        } else
            ruleOrdering = groupedRules.keySet();

        //endregion

        //region Broken Rule Cycle
        for (Integer ruleNumber : ruleOrdering) {

            output.append("=======================================\n");
            output.append("***Violated Rule " + RuleList.getRuleByRuleNumber(ruleNumber).getRuleId() + ": " + RuleList.getRuleByRuleNumber(ruleNumber).getDesc() + "\n");

            for (AnalysisIssue issue : groupedRules.get(ruleNumber)) {
                StringBuilder outputMessage = new StringBuilder();

                if (StringUtils.isNotBlank(issue.getClassName())) {
                    outputMessage.append("***");
                    if (!issue.getInfo().equals("UNKNOWN"))
                        outputMessage.append(issue.getInfo());
                    else
                        outputMessage.append(issue.getRule().getDesc());
                } else {
                    outputMessage.append("***Found: ");
                    outputMessage.append("[\"" + issue.getInfo() + "\"] ");
                }

                //region Location Setting
                String lines = null;
                if (issue.getLocations().size() > 0) {

                    List<AnalysisLocation> issueLocations = new ArrayList<>();
                    for (AnalysisLocation loc : issue.getLocations())
                        if (loc.getMethodNumber() == issue.getMethods().size() - 1)
                            issueLocations.add(loc);

                    if (!issueLocations.isEmpty() && !issueLocations.toString().contains("-1"))
                        lines = ":" + issueLocations.toString().replace("[", "").replace("]", "");

                }

                outputMessage.append(" in ").append(issue.getClassName());

                if (source.getSourceType().equals(EngineType.DIR) || source.getSourceType().equals(EngineType.JAVAFILES))
                    outputMessage.append(".java");
                else if (source.getSourceType().equals(EngineType.CLASSFILES))
                    outputMessage.append(".class");

                outputMessage.append("::").append(issue.getMethods().pop());

                if (lines != null)
                    outputMessage.append(lines);

                outputMessage.append(".");
                //endregion

                //endregion
                output.append(outputMessage).append("\n");
            }

            output.append("=======================================\n");
        }
        //endregion

        //region Timing Section
        if (source.isShowTimes()) {
            output.append("=======================================\n");
            output.append("Analysis Timing (ms): ").append(source.getAnalyisisTime()).append(".\n");
            output.append("=======================================\n");
        }
        //endregion

        return StringUtils.stripToNull(output.toString());
    }
}
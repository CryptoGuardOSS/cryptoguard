package main.frontEnd.MessagingSystem.routing.outputStructures;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.AnalysisLocation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.RuleList;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * The class containing the implementation of the legacy output.
 * <p>STATUS: IC</p>
 *
 * @author franceme
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

        //Only printing console output if it is set and there is output captured
        if (source.getInternalErrors() != null && source.getInternalErrors().toString().split("\n").length > 1) {
            output.append("Internal Warnings: " + source.getInternalErrors().toString() + "\n");
        }

        output.append("Analyzing " + source.getSourceType() + ": ");

        for (int sourceKtr = 0; sourceKtr < source.getSource().size(); sourceKtr++) {
            output.append(source.getSource().get(sourceKtr));

            if (sourceKtr != source.getSource().size() - 1)
                output.append(",");
        }
        output.append("\n");

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

        //region Broken Rule Cycle
        for (Integer ruleNumber : groupedRules.keySet()) {
            output.append("=======================================\n");
            output.append("***Violated Rule " + RuleList.getRuleByRuleNumber(ruleNumber).getRuleId() + ": " + RuleList.getRuleByRuleNumber(ruleNumber).getDesc() + "\n");

            for (AnalysisIssue issue : groupedRules.get(ruleNumber)) {
                StringBuilder outputMessage = new StringBuilder();

                //region for no general cause
                if (StringUtils.isBlank(issue.getIssueCause())) {

                    if (StringUtils.isNotBlank(issue.getClassName())) {
                        outputMessage.append("***");
                        if (!issue.getIssueInformation().equals("UNKNOWN")) {
                            outputMessage.append(issue.getIssueInformation());
                            outputMessage.append(" ");
                        } else {
                            outputMessage.append(issue.getRule().getDesc());
                            outputMessage.append(" in ");
                        }
                        outputMessage.append(issue.getClassName());
                    } else if (issue.getMethods().size() > 0) {
                        outputMessage.append("***Found: ");
                        outputMessage.append("[\"" + issue.getIssueInformation() + "\"] ");

                        if (issue.getLocations().size() > 0) {

                            List<AnalysisLocation> issueLocations = new ArrayList<>();
                            for (AnalysisLocation loc : issue.getLocations())
                                if (loc.getMethodNumber() == issue.getMethods().size() - 1)
                                    issueLocations.add(loc);

                            if (!issueLocations.isEmpty()) {
                                outputMessage.append("in Line " + issueLocations.toString().replace("[", "").replace("]", "") + " ");
                            }
                        }
                        outputMessage.append("in Method: " + issue.getMethods().pop());

                    } else {
                        outputMessage.append("***Cause: ");
                        outputMessage.append(issue.getIssueCause());
                    }
                }
                //endregion
                //region only general cause
                else {
                    output.append("***Cause: " + issue.getIssueCause());
                }
                //endregion
                output.append(outputMessage + "\n");
            }

            output.append("=======================================\n");
        }
        //endregion

        return output.toString();
    }
}

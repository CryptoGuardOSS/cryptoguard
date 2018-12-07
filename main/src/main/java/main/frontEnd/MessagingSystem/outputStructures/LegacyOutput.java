package main.frontEnd.MessagingSystem.outputStructures;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.AnalysisLocation;
import main.frontEnd.MessagingSystem.EnvironmentInformation;
import main.frontEnd.MessagingSystem.OutputStructure;
import main.rule.engine.EngineType;
import main.rule.engine.RuleList;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The class containing the implementation of the legacy output.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @since V01.00.01
 */
public class LegacyOutput implements OutputStructure {
    public final Listing typeOfStructure = Listing.LegacyOutput;


    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * The overridden method for the Legacy output. Currently mimics the output as best seen.
     */
    public String getOutput(EnvironmentInformation source, EngineType type, ArrayList<AnalysisIssue> brokenRules, PrintStream internalWarnings) {
        StringBuilder output = new StringBuilder();


        //Only printing console output if it is set and there is output captured
        if (internalWarnings != null && internalWarnings.toString().split("\n").length > 1) {
            output.append("Internal Warnings: " + internalWarnings.toString() + "\n");
        }
        output.append("Analyzing " + type + ": " + source.getSource() + "\n");

        Map<Integer, List<AnalysisIssue>> groupedRules = brokenRules.stream().collect(Collectors.groupingBy(AnalysisIssue::getRuleId));

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
                        outputMessage.append(issue.getIssueInformation());
                        outputMessage.append(issue.getClassName());
                    } else if (issue.getMethods().size() > 0) {
                        outputMessage.append("***Found: ");
                        outputMessage.append("[\"" + issue.getIssueInformation() + "\"] ");

                        if (issue.getLocations().size() > 0) {
                            Predicate<AnalysisLocation> matchByCurrentMethod = loc -> loc.getMethodNumber() == issue.getMethods().size() - 1;
                            List<AnalysisLocation> issueLocations = issue.getLocations().stream().filter(matchByCurrentMethod).collect(Collectors.toList());

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

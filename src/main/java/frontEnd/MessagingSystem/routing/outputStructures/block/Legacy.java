package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import lombok.extern.log4j.Log4j2;
import rule.engine.RuleList;

import java.util.*;

/**
 * The class containing the implementation of the legacy output.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.01
 */
@Log4j2
public class Legacy extends Structure {

    //region Attributes

    /**
     * {@inheritDoc}
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public Legacy(EnvironmentInformation info) {
        super(info);
    }
    //endregion

    //region Constructor
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public String handleOutput() throws ExceptionHandler {
        StringBuilder output = new StringBuilder();

        //reopening the console stream
        log.debug("Writing the Header");
        output.append(frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.marshallingHeader(super.getType(), super.getSource().getSource()));

        //Only printing console output if it is set and there is output captured
        log.debug("Writing the Soot Errors");
        output.append(frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.marshallingSootErrors(super.getSource().getSootErrors()));

        Map<Integer, List<AnalysisIssue>> groupedRules = new HashMap<>();
        log.trace("Grouping all of the rules according by number.");
        if (super.getCollection() != null)
            for (AnalysisIssue issue : super.getCollection()) {
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
        log.trace("Ordering all of the rules based on the legacy output.");
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
            log.trace("Working through the rule group " + ruleNumber);
            output.append("=======================================\n");
            output.append("***Violated Rule " + RuleList.getRuleByRuleNumber(ruleNumber).getRuleId() + ": " + RuleList.getRuleByRuleNumber(ruleNumber).getDesc() + "\n");

            for (AnalysisIssue issue : groupedRules.get(ruleNumber)) {
                log.debug("Working through the broken rule " + issue.getInfo());
                output.append(frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.marshalling(issue, super.getType()));
            }

            output.append("=======================================\n");
        }
        //endregion

        //region Heuristics
        if (super.getSource().getDisplayHeuristics()) {
            log.trace("Writing the heuristics");
            output.append(frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.marshalling(super.getSource()));
        }
        //endregion

        //region Timing Section
        if (super.getSource().isShowTimes()) {
            log.trace("Writing the time measurements.");
            output.append(frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.marshalling(super.getSource().getAnalyisisTime()));
        }
        //endregion

        return output.toString();
    }
    //endregion
}

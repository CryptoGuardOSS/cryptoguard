package main.frontEnd.MessagingSystem.routing.outputStructures.block;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.RuleList;

import java.util.*;

import static main.frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.*;

/**
 * The class containing the implementation of the legacy output.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V01.00.01
 */
public class Legacy extends Structure {

    //region Attributes

    /**
     * {@inheritDoc}
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
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
        output.append(marshallingHeader(super.getType(), super.getSource().getSource()));

        //Only printing console output if it is set and there is output captured
        output.append(marshallingSootErrors(super.getSource().getSootErrors()));

        Map<Integer, List<AnalysisIssue>> groupedRules = new HashMap<>();
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
                output.append(marshalling(issue, super.getType()));
            }

            output.append("=======================================\n");
        }
        //endregion

        //region Timing Section
        if (super.getSource().isShowTimes()) {
            output.append(marshallingShowTimes(super.getSource().getAnalyisisTime()));
        }
        //endregion

        return output.toString();
    }
    //endregion
}

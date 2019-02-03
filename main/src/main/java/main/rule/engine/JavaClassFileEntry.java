package main.rule.engine;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author RigorityJTeam
 * Created on 2019-01-17.
 * @since 01.01.11
 *
 * <p>The method in the Engine handling Java Class File(s) Scanning.</p>
 */
public class JavaClassFileEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public ArrayList<AnalysisIssue> NonStreamScan(EnvironmentInformation generalInfo) {
        ArrayList<AnalysisIssue> issues = generalInfo.getPrintOut() ? null : new ArrayList<AnalysisIssue>();


        //region Core Handling
        try {
            generalInfo.startAnalysis();
            for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                ArrayList<AnalysisIssue> tempIssues = ruleChecker.checkRule(generalInfo.getSourceType(), generalInfo.getSource(), generalInfo.getDependencies(),
                        generalInfo.getPrintOut(), generalInfo.getSourcePaths());


                if (!generalInfo.getPrintOut())
                    issues.addAll(tempIssues);
            }
            generalInfo.stopAnalysis();


        } catch (IOException e) {

        }
        //endregion


        return issues;
    }

    /**
     * {@inheritDoc}
     */
    /*public Stream<AnalysisIssue> StreamScan(EnvironmentInformation generalInfo) {
        return new ArrayList<AnalysisIssue>().stream();
    }*/
}

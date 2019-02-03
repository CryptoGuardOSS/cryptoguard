package main.rule.engine;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @since 01.01.06
 *
 * <p>The method in the Engine handling Jar Scanning</p>
 */
public class JarEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public ArrayList<AnalysisIssue> NonStreamScan(EnvironmentInformation generalInfo) {

        ArrayList<AnalysisIssue> issues = generalInfo.getPrintOut() ? null : new ArrayList<AnalysisIssue>();
        try {
            generalInfo.startAnalysis();
            for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                ArrayList<AnalysisIssue> tempIssues = ruleChecker.checkRule(EngineType.JAR, generalInfo.getSource(), generalInfo.getDependencies(),
                        generalInfo.getPrintOut(), generalInfo.getSourcePaths());


                if (!generalInfo.getPrintOut())
                    issues.addAll(tempIssues);
            }
            generalInfo.stopAnalysis();
        } catch (IOException e) {

        }
        return issues;
    }
}

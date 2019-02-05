package main.rule.engine;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>ApkEntry class.</p>
 *
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @version $Id: $Id
 * @since 01.01.06
 *
 * <p>The method in the Engine handling Apk Scanning</p>
 */
public class ApkEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public ArrayList<AnalysisIssue> NonStreamScan(EnvironmentInformation generalInfo) {
        ArrayList<AnalysisIssue> issues = generalInfo.getPrintOut() ? null : new ArrayList<AnalysisIssue>();

        try {

            generalInfo.stopAnalysis();
            for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                ArrayList<AnalysisIssue> tempIssues = ruleChecker.checkRule(EngineType.APK, generalInfo.getSource(), null, generalInfo.getPrintOut(), generalInfo.getSourcePaths());

                if (!generalInfo.getPrintOut())
                    issues.addAll(tempIssues);
            }
            generalInfo.stopAnalysis();
        } catch (IOException e) {

        }
        return issues;
    }
}

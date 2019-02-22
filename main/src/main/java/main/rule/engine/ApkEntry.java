package main.rule.engine;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter;

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
    public ArrayList<AnalysisIssue> NonStreamScan(EnvironmentInformation generalInfo) throws ExceptionHandler {
        ArrayList<AnalysisIssue> issues = generalInfo.getPrintOut() ? null : new ArrayList<AnalysisIssue>();

        generalInfo.startAnalysis();
        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
            ArrayList<AnalysisIssue> tempIssues = ruleChecker.checkRule(EngineType.APK, generalInfo.getSource(), null, generalInfo.getPrintOut(), generalInfo.getSourcePaths(), null);

            if (!generalInfo.getPrintOut())
                issues.addAll(tempIssues);
        }
        generalInfo.stopAnalysis();

        return issues;
    }

    /**
     * {@inheritDoc}
     */
    public void StreamScan(EnvironmentInformation generalInfo, baseStreamWriter streamWriter) throws ExceptionHandler {
        generalInfo.startAnalysis();
        //region Core

        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList)
            ruleChecker.checkRule(EngineType.APK, generalInfo.getSource(), null, generalInfo.getPrintOut(), generalInfo.getSourcePaths(), streamWriter);


        //endregion
        generalInfo.stopAnalysis();
    }
}

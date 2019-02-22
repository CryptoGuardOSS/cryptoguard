package main.rule.engine;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>JavaClassFileEntry class.</p>
 *
 * @author RigorityJTeam
 * Created on 2019-01-17.
 * @version $Id: $Id
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

        generalInfo.startAnalysis();
        //region Core Handling
        try {
            for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                ArrayList<AnalysisIssue> tempIssues = ruleChecker.checkRule(generalInfo.getSourceType(), generalInfo.getSource(), generalInfo.getDependencies(),
                        generalInfo.getPrintOut(), generalInfo.getSourcePaths(), null);


                if (!generalInfo.getPrintOut())
                    issues.addAll(tempIssues);
            }


        } catch (IOException e) {
            e.printStackTrace();
            //TODO - Handle This

        }
        //endregion


        generalInfo.stopAnalysis();
        return issues;
    }

    /** {@inheritDoc} */
    public void StreamScan(EnvironmentInformation generalInfo, baseStreamWriter streamWriter) {
        generalInfo.startAnalysis();
        //region Core
        try {
            for (RuleChecker ruleChecker : CommonRules.ruleCheckerList)
                ruleChecker.checkRule(generalInfo.getSourceType(), generalInfo.getSource(), generalInfo.getDependencies(),
                        generalInfo.getPrintOut(), generalInfo.getSourcePaths(), streamWriter);

        } catch (IOException e) {
            e.printStackTrace();
            //TODO - Handle This
        }
        //endregion
        generalInfo.stopAnalysis();
    }

}

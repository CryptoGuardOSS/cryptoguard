package main.rule.engine;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @since 01.01.06
 *
 * <p>The method in the Engine handling Apk Scanning</p>
 */
public class ApkEntry {

    //TODO - Implement this
    public static ArrayList<AnalysisIssue> NonStreamScan(EnvironmentInformation generalInfo) {
        ArrayList<AnalysisIssue> issues = null;

        try {

            for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                ruleChecker.checkRule(EngineType.APK, Arrays.asList(generalInfo.getSource()), null);
            }
        } catch (IOException e) {

        }
        return issues;
    }
}

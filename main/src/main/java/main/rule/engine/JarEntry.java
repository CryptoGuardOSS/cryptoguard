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
 * <p>The method in the Engine handling Jar Scanning</p>
 */
public class JarEntry {

    public static ArrayList<AnalysisIssue> NonStreamScan(EnvironmentInformation generalInfo) {
        ArrayList<AnalysisIssue> issues = null;
        try {
            for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                ruleChecker.checkRule(EngineType.JAR, Arrays.asList(generalInfo.getSource()), generalInfo.getDependencies());
            }
        } catch (IOException e) {

        }
        return issues;
    }
}

package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.MessageRepresentation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.ApkEntry;
import main.rule.engine.JarEntry;
import main.rule.engine.SourceEntry;

import java.util.ArrayList;

/**
 * @author RigorityJTeam
 * Created on 12/5/18.
 * @since 01.00.06
 *
 * <p>The main entry point of the program when this program
 * is used via command-line and not as a library</p>
 */
public class EntryPoint {

    public static void main(String[] args) {

        //Fail Fast on the input validation
        EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(args);
        if (generalInfo == null)
            System.exit(0);


        ArrayList<AnalysisIssue> issues = null;
        switch (generalInfo.getSourceType()) {
            case APK:
                issues = ApkEntry.NonStreamScan(generalInfo);
                break;
            case JAR:
                issues = JarEntry.NonStreamScan(generalInfo);
                break;
            case SOURCE:
                issues = SourceEntry.NonStreamScan(generalInfo);
                break;
        }

        System.out.println(MessageRepresentation.getMessage(generalInfo, issues));
    }
}

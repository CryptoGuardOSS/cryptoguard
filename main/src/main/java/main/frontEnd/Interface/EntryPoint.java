package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.MessageRepresentation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.*;

import java.util.ArrayList;
import java.util.Arrays;

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
        try {
            EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(Arrays.asList(args));
            if (generalInfo == null)
                System.exit(0);


            ArrayList<AnalysisIssue> issues = null;
            EntryHandler handler = null;
            switch (generalInfo.getSourceType()) {
                case APK:
                    issues = ApkEntry.NonStreamScan(generalInfo);
                    break;
                case JAR:
                    issues = JarEntry.NonStreamScan(generalInfo);
                    break;
                case DIR:
                    issues = SourceEntry.NonStreamScan(generalInfo);
                    break;
                case JAVAFILES:
                    handler = new JavaFileEntry();
                    break;
                case CLASSFILES:
                    handler = new JavaClassFileEntry();
                    break;
            }
            if (handler != null)
                issues = handler.NonStreamScan(generalInfo);

            System.out.println(MessageRepresentation.getMessage(generalInfo, issues));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

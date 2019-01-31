package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.MessageRepresentation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.*;
import main.util.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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

        String outputMessage;
        String fileName = getCurrentTimeStamp();

        //Fail Fast on the input validation
        try {
            EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(Arrays.asList(args));
            if (generalInfo == null)
                System.exit(0);

            generalInfo.setPrintOut(false);
            ArrayList<AnalysisIssue> issues = null;
            EntryHandler handler = null;
            switch (generalInfo.getSourceType()) {
                case APK:
                    handler = new ApkEntry();
                    break;
                case JAR:
                    handler = new JarEntry();
                    break;
                case DIR:
                    handler = new SourceEntry();
                    break;
                case JAVAFILES:
                    handler = new JavaFileEntry();
                    break;
                case CLASSFILES:
                    handler = new JavaClassFileEntry();
                    break;
            }
            issues = handler.NonStreamScan(generalInfo);

            fileName = Utils.osPathJoin(System.getProperty("user.dir"),
                    generalInfo.getPackageName() + "_" + fileName + generalInfo.getMessagingType().getOutputFileExt());

            outputMessage = MessageRepresentation.getMessage(generalInfo, issues);
            //System.out.println(generalInfo.getInternalErrors());

        } catch (Exception e) {
            e.printStackTrace();
            fileName = Utils.osPathJoin(System.getProperty("user.dir"),
                    "ERROR_" + fileName + ".txt");
            outputMessage = e.getLocalizedMessage();
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(outputMessage);
            out.flush();
            out.close();

        } catch (Exception e) {
            System.out.println("File " + fileName + " cannot be written to.");
        }

    }

    private static String getCurrentTimeStamp() {

        StringBuilder date = new StringBuilder();
        Date currentDate = new Date();
        date.append(currentDate.getYear());
        date.append(currentDate.getMonth());
        date.append(currentDate.getDay());
        date.append("-");
        date.append(currentDate.getHours());
        date.append(currentDate.getMinutes());
        date.append(currentDate.getSeconds());

        return date.toString();

    }
}

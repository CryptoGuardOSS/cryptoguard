package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.MessageRepresentation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.*;
import main.util.Utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>EntryPoint class.</p>
 *
 * @author RigorityJTeam
 * Created on 12/5/18.
 * @version $Id: $Id
 * @since 01.00.06
 *
 * <p>The main entry point of the program when this program
 * is used via command-line and not as a library</p>
 */
public class EntryPoint {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {

        String outputMessage;
        String filePath;

        //Fail Fast on the input validation
        try {
            EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(Arrays.asList(args));
            if (generalInfo == null)
                System.exit(0);

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

            outputMessage = MessageRepresentation.getMessage(generalInfo, issues);
            filePath = generalInfo.getFileOut();

        } catch (Exception e) {
            e.printStackTrace();
            filePath = Utils.osPathJoin(System.getProperty("user.dir"), "ERROR.txt");
            outputMessage = e.getLocalizedMessage();
        }

        try {
            Files.write(Paths.get(filePath), outputMessage.getBytes());
        } catch (Exception e) {
            System.out.println("File " + filePath + " cannot be written to.");
        }

    }


}

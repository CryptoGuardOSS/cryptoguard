package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.MessageRepresentation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.streamWriters.Listing;
import main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter;
import main.rule.engine.*;

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

        EnvironmentInformation generalInfo = null;


        //Fail Fast on the input validation
        try {
            generalInfo = ArgumentsCheck.paramaterCheck(Arrays.asList(args));
        } catch (ExceptionHandler e) {
            e.printStackTrace();
            System.exit(0);
        }


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

        if (!generalInfo.getStreaming()) {
            String outputMessage;
            String filePath;

            ArrayList<AnalysisIssue> issues = null;

            issues = handler.NonStreamScan(generalInfo);

            outputMessage = MessageRepresentation.getMessage(generalInfo, issues);
            filePath = generalInfo.getFileOut();


            try {
                Files.write(Paths.get(filePath), outputMessage.getBytes());
            } catch (Exception e) {
                System.out.println("File " + filePath + " cannot be written to.");
            }
        } else {

            baseStreamWriter writer = Listing.retrieveWriterByType(generalInfo);

            handler.StreamScan(generalInfo, writer);

            writer.close(generalInfo);
        }

    }


}

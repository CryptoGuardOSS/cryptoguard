package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.*;

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

        try {
            //Fail Fast on the input validation
            EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(Arrays.asList(args));

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
            generalInfo.startScanning();

            handler.Scan(generalInfo);

            generalInfo.stopScanning();

        } catch (ExceptionHandler e) {
            System.err.print(e.toString());
            System.exit(e.getErrorCode().getId());
        }

    }
}
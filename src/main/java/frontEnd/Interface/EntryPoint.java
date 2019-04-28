package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import rule.engine.*;

import java.util.ArrayList;

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
@Log4j2
public class EntryPoint {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {

        //Removing all of the empty string values, i.e. " "
        ArrayList<String> strippedArgs = new ArrayList<>();
        for (String arg : args)
            if (StringUtils.isNotEmpty(arg))
                strippedArgs.add(arg);

        log.info("Removed the empty arguments.");

        try {
            //Fail Fast on the input validation
            EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(strippedArgs);

            EntryHandler handler = null;
            switch (generalInfo.getSourceType()) {
                case APK:
                    log.debug("Chosen APK Scanning");
                    handler = new ApkEntry();
                    break;
                case JAR:
                    log.debug("Chosen JAR Scanning");
                    handler = new JarEntry();
                    break;
                case DIR:
                    log.debug("Chosen DIR Scanning");
                    handler = new SourceEntry();
                    break;
                case JAVAFILES:
                    log.debug("Chosen JAVAFILES Scanning");
                    log.warn("Scanning Java Files is limited to Java 1.7 and lower, otherwise there may be issues.");
                    handler = new JavaFileEntry();
                    break;
                case CLASSFILES:
                    log.debug("Chosen CLASSFILES Scanning");
                    log.warn("This is still experimental, this has not stabilized yet.");
                    handler = new JavaClassFileEntry();
                    break;
            }
            log.trace("Initializing the scanning process");
            generalInfo.startScanning();

            log.info("Starting the scanning process");
            handler.Scan(generalInfo);
            log.info("Stopped the scanning process");

            generalInfo.stopScanning();

        } catch (ExceptionHandler e) {
            log.debug(e.getErrorCode().getMessage() + ": " + e.getLongDesciption());
            log.fatal(e.getLongDesciption());

            if (e.getErrorCode().getId().equals(0))
                System.out.print(e.getLongDesciption());
            else
                System.err.print(e.toString());

            System.exit(e.getErrorCode().getId());
        }

    }
}
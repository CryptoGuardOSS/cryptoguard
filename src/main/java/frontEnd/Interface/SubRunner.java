package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import lombok.extern.log4j.Log4j2;
import rule.engine.*;

@Log4j2
public class SubRunner {

    public static String run(EnvironmentInformation info) throws ExceptionHandler {

        EntryHandler handler = null;
        switch (info.getSourceType()) {
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
                log.warn("This is still experimental, this has not stabilized yet.");
                log.warn("Scanning Java Files is limited to Java 1.7 and lower, otherwise there may be issues.");
                handler = new JavaFileEntry();
                break;
            case CLASSFILES:
                log.debug("Chosen CLASSFILES Scanning");
                handler = new JavaClassFileEntry();
                break;
        }
        log.trace("Initializing the scanning process");
        info.startScanning();

        log.info("Starting the scanning process");
        handler.Scan(info);
        log.info("Stopped the scanning process");

        info.stopScanning();
        log.info("Writing the output to the file: " + info.getFileOut());

        return info.getFileOut();
    }

}

/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.logging.log4j.Logger;
import rule.engine.*;

/**
 * SubRunner class.
 *
 * @author maister
 * @version 03.10.00
 */
public class SubRunner {

  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(SubRunner.class);

  /**
   * run.
   *
   * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   * @return a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
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
        log.debug("Chosen JAVA FILE Scanning");
        log.warn("This is still experimental, this has not stabilized yet.");
        log.warn(
            "Scanning Java Files is limited to Java 1.7 and lower, otherwise there may be issues.");
        handler = new JavaFileEntry();
        break;
      case CLASSFILES:
        log.debug("Chosen Java CLASS FILE Scanning");
        handler = new JavaClassFileEntry();
        break;
    }
    log.debug("Initializing the scanning process");
    info.startScanning();

    log.info("Starting the scanning process");
    handler.Scan(info);
    log.info("Stopped the scanning process");

    info.stopScanning();
    log.info("Writing the output to the file: " + info.getFileOut());

    return info.getFileOut();
  }
}

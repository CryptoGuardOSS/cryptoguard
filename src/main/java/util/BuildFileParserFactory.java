package util;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import lombok.extern.log4j.Log4j2;

import java.io.File;

/**
 * <p>BuildFileParserFactory class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
@Log4j2
public class BuildFileParserFactory {

    /**
     * <p>getBuildfileParser.</p>
     *
     * @param projectRoot a {@link java.lang.String} object.
     * @return a {@link util.BuildFileParser} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static BuildFileParser getBuildfileParser(String projectRoot) throws ExceptionHandler {

        File pomFile = new File(projectRoot + "/" + "pom.xml");

        if (pomFile.exists()) {
            return new MvnPomFileParser(projectRoot + "/" + "pom.xml");
        }

        File gradleFile = new File(projectRoot + "/" + "settings.gradle");

        if (gradleFile.exists()) {
            return new GradleBuildFileParser(projectRoot + "/" + "settings.gradle");
        }

        log.fatal("Only Maven and Gradle Projects are supported");
        throw new ExceptionHandler("Only Maven and Gradle Projects are supported", ExceptionId.ARG_VALID);

    }
}

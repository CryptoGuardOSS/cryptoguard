package frontEnd.Interface.parameterChecks;

import frontEnd.Interface.Version;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import rule.engine.EngineType;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.Utils.retrieveFullyQualifiedName;

/**
 * @author franceme
 * Created on 2020-11-16.
 * @since 04.05.02
 *
 * <p>Core</p>
 * <p>The utility class containing the specific Parameter Check Routing</p>
 */

public class Core {
    private static final Logger log =
            org.apache.logging.log4j.LogManager.getLogger(Core.class);

    //region Methods
    /**
     * paramaterCheck.
     *
     * @param sourceFiles a {@link java.util.List} object.
     * @param dependencies a {@link java.util.List} object.
     * @param eType a {@link rule.engine.EngineType} object.
     * @param oType a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @param fileOutPath a {@link java.lang.String} object.
     * @param mainFile a {@link java.lang.String} object.
     * @param extraArguments a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static EnvironmentInformation paramaterCheck(
            List<String> sourceFiles,
            List<String> dependencies,
            EngineType eType,
            Listing oType,
            String fileOutPath,
            String mainFile,
            List<String> extraArguments)
            throws ExceptionHandler {
        EnvironmentInformation info =
                paramaterCheck(
                        sourceFiles,
                        dependencies,
                        eType,
                        oType,
                        fileOutPath,
                        true,
                        StringUtils.trimToNull(mainFile),
                        false,
                        null,
                        null,
                        extraArguments);

        //Setting base arguments, some might turn into defaults
        info.setShowTimes(true);
        info.setStreaming(true);
        info.setDisplayHeuristics(true);
        Utils.initDepth(1);

        info.setRawCommand(
                Utils.makeArg(argsIdentifier.SOURCE, info.getSource())
                        + Utils.makeArg(argsIdentifier.DEPENDENCY, info.getDependencies())
                        + Utils.makeArg(argsIdentifier.FORMAT, eType)
                        + Utils.makeArg(argsIdentifier.FORMATOUT, oType)
                        + Utils.makeArg(argsIdentifier.OUT, info.getFileOut())
                        + Utils.makeArg(argsIdentifier.NEW, true)
                        + (StringUtils.isNotEmpty(mainFile)
                        ? Utils.makeArg(argsIdentifier.MAIN, info.getMain())
                        : "")
                        + Utils.makeArg(argsIdentifier.TIMESTAMP, false)
                        + Utils.makeArg(argsIdentifier.TIMEMEASURE, true)
                        + Utils.makeArg(argsIdentifier.STREAM, true)
                        + Utils.makeArg(argsIdentifier.HEURISTICS, true)
                        + Utils.makeArg(argsIdentifier.DEPTH, 1)
                        + Utils.join(" ", extraArguments));

        return info;
    }

    /**
     * paramaterCheck.
     *
     * @param sourceFiles a {@link java.util.List} object.
     * @param dependencies a {@link java.util.List} object.
     * @param eType a {@link rule.engine.EngineType} object.
     * @param oType a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @param fileOutPath a {@link java.lang.String} object.
     * @param overWriteFileOut a {@link java.lang.Boolean} object.
     * @param mainFile a {@link java.lang.String} object.
     * @param timeStamp a {@link java.lang.Boolean} object.
     * @param java a {@link java.lang.String} object.
     * @param android a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @param extraArguments a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static EnvironmentInformation paramaterCheck(
            List<String> sourceFiles,
            List<String> dependencies,
            EngineType eType,
            Listing oType,
            String fileOutPath,
            Boolean overWriteFileOut,
            String mainFile,
            Boolean timeStamp,
            String java,
            String android,
            List<String> extraArguments)
            throws ExceptionHandler {

        //region verifying current running version
        Version currentVersion = Version.getRunningVersion();
        if (StringUtils.isBlank(java) && !currentVersion.supported()) {
            log.fatal("JRE Version: " + currentVersion + " is not compatible");
            throw new ExceptionHandler(
                    "JRE Version: "
                            + currentVersion
                            + " is not compatible, please use JRE Version: "
                            + Version.EIGHT,
                    ExceptionId.GEN_VALID);
        }
        //endregion

        //region verifying filePaths
        //region Setting the source files
        log.trace("Retrieving the source files.");
        ArrayList<String> vSources =
                (sourceFiles.size() == 1 && sourceFiles.get(0).equals("xargs"))
                        ? Utils.retrievingThroughXArgs(eType, false, true)
                        : Utils.retrieveFilePathTypes(new ArrayList<>(sourceFiles), eType, true, false, true);

        log.info("Scanning " + retrieveFullyQualifiedName(vSources).size() + " source file(s).");
        log.debug("Using the source file(s): " + retrieveFullyQualifiedName(vSources).toString());
        //endregion

        //region Setting the dependency path
        log.trace("Retrieving the dependency files.");
        List<String> vDeps =
                Utils.retrieveFilePathTypes(new ArrayList<>(dependencies), false, false, false);
        if (vDeps.size() > 0)
            log.debug("Using the dependency file(s) :" + retrieveFullyQualifiedName(vDeps).toString());
        //endregion
        //endregion

        //region Retrieving the package path
        log.trace("Retrieving the package path, may/may not be able to be replaced.");
        List<String> basePath = new ArrayList<>();
        File sourceFile;
        String pkg = "";
        switch (eType) {
            case APK:
            case JAR:
                sourceFile = new File(sourceFiles.get(0));
                basePath.add(sourceFile.getName());
                pkg = sourceFile.getName();
                break;
            case DIR:
                sourceFile = new File(sourceFiles.get(0));
                try {
                    basePath.add(sourceFile.getCanonicalPath() + ":dir");
                } catch (IOException e) {
                }
                pkg = sourceFile.getName();
                break;
            case JAVAFILES:
            case CLASSFILES:
                for (String file : sourceFiles) {
                    try {
                        sourceFile = new File(file);
                        basePath.add(sourceFile.getCanonicalPath());

                        if (pkg == null) {
                            pkg = sourceFile.getCanonicalPath();
                        }

                    } catch (IOException e) {
                    }
                }
                break;
        }
        log.debug("Package path: " + pkg);
        //endregion

        EnvironmentInformation info =
                new EnvironmentInformation(vSources, eType, oType, vDeps, basePath, pkg);
        info.setJavaHome(java);
        info.setAndroidHome(android);

        //Verifying the right argument is set based on the Enginetype
        info.verifyBaseSettings();

        if (StringUtils.isNotEmpty(mainFile)) {
            log.info("Attempting to validate the main method as " + mainFile);

            if (!info.getSource().contains(mainFile)) {
                log.fatal("The main class path is not included within the source file.");
                throw new ExceptionHandler(
                        "The main class path is not included within the source file.", ExceptionId.ARG_VALID);
            }

            log.info("Using the main method from class " + mainFile);
            info.setMain(mainFile);
        }

        //region Setting the file out
        if (fileOutPath == null) {
            fileOutPath =
                    Utils.getDefaultFileOut(
                            info.getPackageName(), info.getMessagingType().getOutputFileExt());
        } else {
            String ogFileOutPath = fileOutPath;
            fileOutPath =
                    Utils.retrieveFilePath(
                            fileOutPath, oType.getOutputFileExt(), overWriteFileOut, true, false);
            if (fileOutPath == null) {
                log.warn("Output file: " + ogFileOutPath + " is not available.");
                fileOutPath =
                        Utils.getDefaultFileOut(
                                info.getPackageName(), info.getMessagingType().getOutputFileExt());
                log.warn("Defaulting the output to file: " + fileOutPath);
            }
        }
        info.setFileOut(fileOutPath);
        //endregion

        //region Specific Parameter Checking
        if (extraArguments != null && extraArguments.size() > 1)
            oType.retrieveSpecificArgHandler().inputValidation(info, extraArguments);
        //endregion

        return info;
    }
    //endregion
}
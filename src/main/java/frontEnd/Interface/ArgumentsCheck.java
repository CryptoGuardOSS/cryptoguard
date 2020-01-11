package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.Interface.outputRouting.parcelHandling;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import rule.engine.EngineType;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.Utils.retrieveFullyQualifiedName;

/**
 * <p>ArgumentsCheck class.</p>
 *
 * @author CryptoguardTeam
 * Created on 12/13/18.
 * @version 03.07.01
 * @since 01.01.02
 *
 * <p>The main check for the Arguments</p>
 */
@Log4j2
public class ArgumentsCheck {

    /**
     * The fail fast parameter Check method
     * <p>This method will attempt to create the Environment Information and provide help if the usage doesn't match</p>
     *
     * @param args {@link java.lang.String} - the raw arguments passed into the console
     * @return {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} - when not null, the general Information is created for usage within any output structure.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static EnvironmentInformation paramaterCheck(List<String> args) throws ExceptionHandler {

        //region CLI Section

        Options cmdLineArgs = setOptions();
        CommandLine cmd = null;

        //region Printing Version
        if (args.contains(argsIdentifier.HELP.getArg())) {
            log.trace("Retrieving the help as requested.");
            throw new ExceptionHandler(parcelHandling.retrieveHelpFromOptions(cmdLineArgs, null), ExceptionId.HELP);
        }

        if (args.contains(argsIdentifier.VERSION.getArg())) {
            log.trace("Retrieving the version as requested.");
            throw new ExceptionHandler(parcelHandling.retrieveHeaderInfo(), ExceptionId.VERSION);
        }
        //endregion

        log.trace("Starting the parsing of arguments.");
        try {
            cmd = new DefaultParser().parse(cmdLineArgs, args.toArray(new String[0]), true);
        } catch (ParseException e) {
            log.debug("Issue with parsing the arguments: " + e.getMessage());
            StringBuilder arg = null;

            if (e.getMessage().startsWith("Missing required option: "))
                arg = new StringBuilder(argsIdentifier.lookup(e.getMessage().replace("Missing required option: ", "")).getArg());
            else if (e.getMessage().startsWith("Missing required options: ")) {
                String[] argIds = e.getMessage().replace("Missing required options: ", "").replace(" ", "").split(",");
                arg = new StringBuilder("Issue with the following argument(s) ");

                for (String argId : argIds)
                    arg.append(argsIdentifier.lookup(argId)).append(", ");

                arg = new StringBuilder(arg.substring(0, arg.length() - ", ".length()));

            }

            throw new ExceptionHandler(parcelHandling.retrieveHelpFromOptions(cmdLineArgs, arg.toString()), ExceptionId.ARG_VALID);
        }

        //endregion

        //region Cleaning retrieved values from args
        log.trace("Cleaning the extra output specific arguments.");
        ArrayList<String> upgradedArgs = new ArrayList<>(args);
        for (argsIdentifier arg : argsIdentifier.values()) {
            if (cmd.hasOption(arg.getId())) {
                upgradedArgs.remove("-" + arg.getId());
                upgradedArgs.remove(cmd.getOptionValue(arg.getId()));
            }
        }
        args = upgradedArgs;
        log.debug("Output specific arguments: " + args.toString());
        //endregion

        EngineType type = EngineType.getFromFlag(cmd.getOptionValue(argsIdentifier.FORMAT.getId()));
        log.debug("Chose the enginetype: " + type.getName());

        //region Validating the Resources available based on the EngineType

        //Needed regardless
        String javaHome = null;
        String androidHome = null;

        switch (type) {
            //Only APK path needs an android specified path
            case APK:
                if (cmd.hasOption(argsIdentifier.ANDROID.getArg()))
                    androidHome = Utils.retrieveFilePath(cmd.getOptionValue(argsIdentifier.ANDROID.getId()), null, false, false);
            default:
                if (cmd.hasOption(argsIdentifier.JAVA.getArg()))
                    javaHome = Utils.retrieveFilePath(cmd.getOptionValue(argsIdentifier.JAVA.getId()), null, false, false);
                break;
        }
        //endregion

        Boolean usingInputIn = cmd.getOptionValue(argsIdentifier.SOURCE.getId()).endsWith(".in");
        log.debug("Enhanced Input in file: " + usingInputIn);

        //region Logging Verbosity Check
        if (cmd.hasOption(argsIdentifier.VERYVERBOSE.getArg())) {
            Configurator.setRootLevel(Level.TRACE);
            log.info("Displaying debug level logs");
        } else if (cmd.hasOption(argsIdentifier.VERBOSE.getArg())) {
            Configurator.setRootLevel(Level.DEBUG);
            log.info("Displaying debug level logs");
        } else if (cmd.hasOption(argsIdentifier.NOLOGS.getArg())) {
            Configurator.setRootLevel(Level.OFF);
        } else {
            Configurator.setRootLevel(Level.INFO);
            log.info("Displaying info level logs");
        }
        //endregion

        //inputFiles

        //region Setting the source files
        List<String> source = Arrays.asList(cmd.getOptionValues(argsIdentifier.SOURCE.getId()));

        String setMainClass = null;
        if (cmd.hasOption(argsIdentifier.MAIN.getId())) {
            setMainClass = StringUtils.trimToNull(cmd.getOptionValue(argsIdentifier.MAIN.getId()));
            if (setMainClass == null)
                throw new ExceptionHandler("Please Enter a valid main class path.", ExceptionId.ARG_VALID);
        }
        //endregion

        //region Setting the dependency path
        List<String> dependencies = cmd.hasOption(argsIdentifier.DEPENDENCY.getId()) ? Arrays.asList(cmd.getOptionValues(argsIdentifier.DEPENDENCY.getId())) : new ArrayList<>();
        //endregion

        Listing messaging = Listing.retrieveListingType(cmd.getOptionValue(argsIdentifier.FORMATOUT.getId()));
        log.info("Using the output: " + messaging.getType());

        //region - TODO - Implement an option to specify the base package
        /*
        if (cmd.hasOption(argsIdentifier.BASEPACKAGE.getId())) {
            String basePackage = cmd.getOptionValue(argsIdentifier.BASEPACKAGE.getId());
            log.debug("Going to set the Base Package : " + basePackage);

            info.setBasePackage(Utils.verifyDir(basePackage));
            log.info("Specifying the base package as " + basePackage);
        }
        */
        //endregion

        //region Setting the file out
        log.trace("Determining the file out.");
        String fileOutPath = null;
        if (cmd.hasOption(argsIdentifier.OUT.getId()))
            fileOutPath = cmd.getOptionValue(argsIdentifier.OUT.getId());
        //endregion

        EnvironmentInformation info = paramaterCheck(source, dependencies, type,
                messaging, fileOutPath, cmd.hasOption(argsIdentifier.NEW.getId()),
                setMainClass, cmd.hasOption(argsIdentifier.TIMESTAMP.getId()),
                javaHome, androidHome);

        if (!messaging.getTypeOfMessagingInput().inputValidation(info, args.toArray(new String[0]))) {
            log.error("Issue Validating Output Specific Arguments.");
            //TODO - Add better output message for this case
            throw new ExceptionHandler(messaging.getInputHelp(), ExceptionId.FORMAT_VALID);
        }

        //region Logging Information
        info.setPrettyPrint(cmd.hasOption(argsIdentifier.PRETTY.getId()));
        log.debug("Pretty flag: " + cmd.hasOption(argsIdentifier.PRETTY.getId()));

        info.setShowTimes(cmd.hasOption(argsIdentifier.TIMEMEASURE.getId()));
        log.debug("Time measure flag: " + cmd.hasOption(argsIdentifier.TIMEMEASURE.getId()));

        info.setStreaming(cmd.hasOption(argsIdentifier.STREAM.getId()));
        log.debug("Stream flag: " + cmd.hasOption(argsIdentifier.STREAM.getId()));

        info.setDisplayHeuristics(cmd.hasOption(argsIdentifier.HEURISTICS.getId()));
        log.debug("Heuristics flag: " + cmd.hasOption(argsIdentifier.HEURISTICS.getId()));

        Utils.initDepth(Integer.parseInt(cmd.getOptionValue(argsIdentifier.DEPTH.getId(), String.valueOf(1))));
        log.debug("Scanning using a depth of " + Utils.DEPTH);

        boolean noExitJVM = cmd.hasOption(argsIdentifier.NOEXIT.getId());
        log.debug("Exiting the JVM: " + noExitJVM);
        if (noExitJVM)
            info.setKillJVM(false);
        //endregion

        //Setting the raw command within info
        info.setRawCommand(Utils.join(" ", args));

        return info;

    }

    /**
     * <p>paramaterCheck.</p>
     *
     * @param sourceFiles  a {@link java.util.List} object.
     * @param dependencies a {@link java.util.List} object.
     * @param eType        a {@link rule.engine.EngineType} object.
     * @param oType        a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @param fileOutPath  a {@link java.lang.String} object.
     * @param mainFile     a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static EnvironmentInformation paramaterCheck(List<String> sourceFiles, List<String> dependencies, EngineType eType, Listing oType, String fileOutPath, String mainFile) throws ExceptionHandler {
        EnvironmentInformation info = paramaterCheck(sourceFiles, dependencies, eType, oType, fileOutPath, true, StringUtils.trimToNull(mainFile), false, null, null);

        //Setting base arguments, some might turn into defaults
        info.setShowTimes(true);
        info.setStreaming(true);
        info.setDisplayHeuristics(true);
        Utils.initDepth(1);

        info.setRawCommand(
                Utils.makeArg(argsIdentifier.SOURCE, info.getSource()) +
                        Utils.makeArg(argsIdentifier.DEPENDENCY, info.getDependencies()) +
                        Utils.makeArg(argsIdentifier.FORMAT, eType) +
                        Utils.makeArg(argsIdentifier.FORMATOUT, oType) +
                        Utils.makeArg(argsIdentifier.OUT, info.getFileOut()) +
                        Utils.makeArg(argsIdentifier.NEW, true) +
                        (StringUtils.isNotEmpty(mainFile) ? Utils.makeArg(argsIdentifier.MAIN, info.getMain()) : "") +
                        Utils.makeArg(argsIdentifier.TIMESTAMP, false) +
                        Utils.makeArg(argsIdentifier.TIMEMEASURE, true) +
                        Utils.makeArg(argsIdentifier.STREAM, true) +
                        Utils.makeArg(argsIdentifier.HEURISTICS, true) +
                        Utils.makeArg(argsIdentifier.DEPTH, 1)
        );

        return info;
    }

    /**
     * <p>paramaterCheck.</p>
     *
     * @param sourceFiles      a {@link java.util.List} object.
     * @param dependencies     a {@link java.util.List} object.
     * @param eType            a {@link rule.engine.EngineType} object.
     * @param oType            a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @param fileOutPath      a {@link java.lang.String} object.
     * @param overWriteFileOut a {@link java.lang.Boolean} object.
     * @param mainFile         a {@link java.lang.String} object.
     * @param timeStamp        a {@link java.lang.Boolean} object.
     * @param java             a {@link java.lang.String} object.
     * @param android          a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static EnvironmentInformation paramaterCheck(List<String> sourceFiles, List<String> dependencies,
                                                        EngineType eType, Listing oType, String fileOutPath,
                                                        Boolean overWriteFileOut,
                                                        String mainFile, Boolean timeStamp,
                                                        String java, String android) throws ExceptionHandler {

        //region verifying current running version
        Version currentVersion = Version.getRunningVersion();
        if (!currentVersion.supported())
            throw new ExceptionHandler("JRE Version: " + currentVersion + " is not compatible, please use JRE Version: " + Utils.supportedVersion, ExceptionId.GEN_VALID);
        //endregion

        //region verifying filePaths
        //region Setting the source files
        log.trace("Retrieving the source files.");
        ArrayList<String> vSources = Utils.retrieveFilePathTypes(new ArrayList<>(sourceFiles), eType, true, false);
        log.info("Using the source file(s): " + retrieveFullyQualifiedName(vSources).toString());
        //endregion

        //region Setting the dependency path
        log.trace("Retrieving the dependency files.");
        List<String> vDeps = Utils.retrieveFilePathTypes(new ArrayList<>(dependencies), false, false);
        log.debug("Using the source file(s) :" + retrieveFullyQualifiedName(vDeps).toString());
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

        EnvironmentInformation info = new EnvironmentInformation(vSources, eType, oType, vDeps, basePath, pkg);
        info.setJavaHome(java);
        info.setAndroidHome(android);

        //Verifying the right argument is set based on the Enginetype
        info.verifyBaseSettings();

        if (StringUtils.isNotEmpty(mainFile)) {
            log.info("Attempting to validate the main method as " + mainFile);

            if (!info.getSource().contains(mainFile))
                throw new ExceptionHandler("The main class path is not included within the source file.", ExceptionId.ARG_VALID);

            log.info("Using the main method from class " + mainFile);
            info.setMain(mainFile);
        }

        //region Setting the file out
        if (fileOutPath == null) {
            fileOutPath = Utils.osPathJoin(System.getProperty("user.dir"),
                    info.getPackageName() + info.getMessagingType().getOutputFileExt());

            String[] tempSplit = fileOutPath.split("\\.\\w+$");
            fileOutPath = tempSplit[0] + "_" + Utils.getCurrentTimeStamp() + info.getMessagingType().getOutputFileExt();
        } else {
            fileOutPath = Utils.retrieveFilePath(fileOutPath, oType.getOutputFileExt(), overWriteFileOut, true);
        }
        info.setFileOut(fileOutPath);
        //endregion

        return info;
    }

    private static Options setOptions() {
        Options cmdLineArgs = new Options();

        Option format = Option.builder(argsIdentifier.FORMAT.getId()).required().hasArg().argName(argsIdentifier.FORMAT.getArgName()).desc(argsIdentifier.FORMAT.getDesc()).build();
        format.setType(String.class);
        format.setOptionalArg(false);
        cmdLineArgs.addOption(format);

        Option sources = Option.builder(argsIdentifier.SOURCE.getId()).required().hasArgs().argName(argsIdentifier.SOURCE.getArgName()).desc(argsIdentifier.SOURCE.getDesc()).build();
        sources.setType(String.class);
        sources.setValueSeparator(' ');
        sources.setOptionalArg(false);
        cmdLineArgs.addOption(sources);

        Option dependency = Option.builder(argsIdentifier.DEPENDENCY.getId()).hasArg().argName(argsIdentifier.DEPENDENCY.getArgName()).desc(argsIdentifier.DEPENDENCY.getDesc()).build();
        dependency.setType(String.class);
        dependency.setOptionalArg(false);
        cmdLineArgs.addOption(dependency);

        Option mainFile = Option.builder(argsIdentifier.MAIN.getId()).hasArg().argName(argsIdentifier.MAIN.getArgName()).desc(argsIdentifier.MAIN.getDesc()).build();
        mainFile.setType(String.class);
        mainFile.setOptionalArg(true);
        cmdLineArgs.addOption(mainFile);

        Option javaPath = Option.builder(argsIdentifier.JAVA.getId()).hasArg().argName(argsIdentifier.JAVA.getArgName()).desc(argsIdentifier.JAVA.getDesc()).build();
        javaPath.setType(File.class);
        javaPath.setOptionalArg(true);
        cmdLineArgs.addOption(javaPath);

        Option androidPath = Option.builder(argsIdentifier.ANDROID.getId()).hasArg().argName(argsIdentifier.ANDROID.getArgName()).desc(argsIdentifier.ANDROID.getDesc()).build();
        androidPath.setType(File.class);
        androidPath.setOptionalArg(true);
        cmdLineArgs.addOption(androidPath);

        //region - TODO - Implement an option to specify the base package
        /*
        Option baseProject = Option.builder(argsIdentifier.BASEPACKAGE.getId()).hasArg().argName("package").desc(argsIdentifier.BASEPACKAGE.getDesc()).build();
        baseProject.setType(String.class);
        baseProject.setOptionalArg(true);
        cmdLineArgs.addOption(baseProject);
        */
        //endregion

        Option depth = Option.builder(argsIdentifier.DEPTH.getId()).hasArg().argName(argsIdentifier.DEPTH.getArgName()).desc(argsIdentifier.DEPTH.getDesc()).build();
        depth.setType(String.class);
        depth.setOptionalArg(true);
        cmdLineArgs.addOption(depth);

        Option output = Option.builder(argsIdentifier.OUT.getId()).hasArg().argName(argsIdentifier.OUT.getArgName()).desc(argsIdentifier.OUT.getDesc()).build();
        output.setType(String.class);
        output.setOptionalArg(true);
        cmdLineArgs.addOption(output);

        Option timing = new Option(argsIdentifier.TIMEMEASURE.getId(), false, argsIdentifier.TIMEMEASURE.getDesc());
        timing.setOptionalArg(true);
        cmdLineArgs.addOption(timing);

        Option formatOut = Option.builder(argsIdentifier.FORMATOUT.getId()).hasArg().argName(argsIdentifier.FORMATOUT.getArgName()).desc(argsIdentifier.FORMATOUT.getDesc()).build();
        formatOut.setOptionalArg(false);
        cmdLineArgs.addOption(formatOut);

        Option prettyPrint = new Option(argsIdentifier.PRETTY.getId(), false, argsIdentifier.PRETTY.getDesc());
        prettyPrint.setOptionalArg(true);
        cmdLineArgs.addOption(prettyPrint);

        Option noExit = new Option(argsIdentifier.NOEXIT.getId(), false, argsIdentifier.NOEXIT.getDesc());
        prettyPrint.setOptionalArg(true);
        cmdLineArgs.addOption(noExit);

        Option help = new Option(argsIdentifier.HELP.getId(), false, argsIdentifier.HELP.getDesc());
        help.setOptionalArg(true);
        cmdLineArgs.addOption(help);

        Option version = new Option(argsIdentifier.VERSION.getId(), false, argsIdentifier.VERSION.getDesc());
        version.setOptionalArg(true);
        cmdLineArgs.addOption(version);

        Option displayHeuristcs = new Option(argsIdentifier.HEURISTICS.getId(), false, argsIdentifier.HEURISTICS.getDesc());
        displayHeuristcs.setOptionalArg(true);
        cmdLineArgs.addOption(displayHeuristcs);

        Option timeStamp = new Option(argsIdentifier.TIMESTAMP.getId(), false, argsIdentifier.TIMESTAMP.getDesc());
        timeStamp.setOptionalArg(true);
        cmdLineArgs.addOption(timeStamp);

        Option stream = new Option(argsIdentifier.STREAM.getId(), false, argsIdentifier.STREAM.getDesc());
        stream.setOptionalArg(true);
        cmdLineArgs.addOption(stream);

        Option nologs = new Option(argsIdentifier.NOLOGS.getId(), false, argsIdentifier.NOLOGS.getDesc());
        stream.setOptionalArg(true);
        cmdLineArgs.addOption(nologs);

        Option verbose = new Option(argsIdentifier.VERBOSE.getId(), false, argsIdentifier.VERBOSE.getDesc());
        stream.setOptionalArg(true);
        cmdLineArgs.addOption(verbose);

        Option vverbose = new Option(argsIdentifier.VERYVERBOSE.getId(), false, argsIdentifier.VERYVERBOSE.getDesc());
        stream.setOptionalArg(true);
        cmdLineArgs.addOption(vverbose);

        log.trace("Set the command line options to be used for parsing.");
        return cmdLineArgs;
    }

}

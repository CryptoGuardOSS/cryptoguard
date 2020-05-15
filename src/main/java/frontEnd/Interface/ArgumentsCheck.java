/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import static util.Utils.retrieveFullyQualifiedName;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.Interface.outputRouting.parcelHandling;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import rule.engine.EngineType;
import util.Utils;

/**
 * ArgumentsCheck class.
 *
 * @author CryptoguardTeam Created on 12/13/18.
 * @version 03.07.01
 * @since 01.01.02
 *     <p>The main check for the Arguments
 */
public class ArgumentsCheck {

  private static final Logger log =
      org.apache.logging.log4j.LogManager.getLogger(ArgumentsCheck.class);

  /**
   * The fail fast parameter Check method
   *
   * <p>This method will attempt to create the Environment Information and provide help if the usage
   * doesn't match
   *
   * @param args {@link java.lang.String} - the raw arguments passed into the console
   * @return {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} - when not null, the
   *     general Information is created for usage within any output structure.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static EnvironmentInformation paramaterCheck(List<String> args) throws ExceptionHandler {

    //region CLI Section
    List<String> originalArguments = new ArrayList<String>(args);
    Options cmdLineArgs = setOptions();
    CommandLine cmd = null;

    //region Printing Version
    if (args.contains(argsIdentifier.HELP.getArg())) {
      throw new ExceptionHandler(
          parcelHandling.retrieveHelpFromOptions(cmdLineArgs, null), ExceptionId.HELP);
    }

    if (args.contains(argsIdentifier.VERSION.getArg())) {
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
        arg =
            new StringBuilder(
                argsIdentifier
                    .lookup(e.getMessage().replace("Missing required option: ", ""))
                    .getArg());
      else if (e.getMessage().startsWith("Missing required options: ")) {
        String[] argIds =
            e.getMessage().replace("Missing required options: ", "").replace(" ", "").split(",");
        arg = new StringBuilder("Issue with the following argument(s) ");

        for (String argId : argIds) arg.append(argsIdentifier.lookup(argId)).append(", ");

        arg = new StringBuilder(arg.substring(0, arg.length() - ", ".length()));
      }

      log.fatal(parcelHandling.retrieveHelpFromOptions(cmdLineArgs, arg.toString()));
      throw new ExceptionHandler(
          parcelHandling.retrieveHelpFromOptions(cmdLineArgs, arg.toString()),
          ExceptionId.ARG_VALID);
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
          androidHome =
              Utils.retrieveFilePath(
                  cmd.getOptionValue(argsIdentifier.ANDROID.getId()), null, false, false);
      default:
        if (cmd.hasOption(argsIdentifier.JAVA.getArg()))
          javaHome =
              Utils.retrieveFilePath(
                  cmd.getOptionValue(argsIdentifier.JAVA.getId()), null, false, false);
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
      if (setMainClass == null) {
        log.fatal("Please Enter a valid main class path.");
        throw new ExceptionHandler("Please Enter a valid main class path.", ExceptionId.ARG_VALID);
      }
    }
    //endregion

    //region Setting the dependency path
    List<String> dependencies =
        cmd.hasOption(argsIdentifier.DEPENDENCY.getId())
            ? Arrays.asList(cmd.getOptionValues(argsIdentifier.DEPENDENCY.getId()))
            : new ArrayList<>();
    //endregion

    Listing messaging =
        Listing.retrieveListingType(cmd.getOptionValue(argsIdentifier.FORMATOUT.getId()));
    log.info("Using the output: " + messaging.getType());

    //region Setting the file out
    log.trace("Determining the file out.");
    String fileOutPath = null;
    if (cmd.hasOption(argsIdentifier.OUT.getId()))
      fileOutPath = cmd.getOptionValue(argsIdentifier.OUT.getId());
    //endregion

    EnvironmentInformation info =
        paramaterCheck(
            source,
            dependencies,
            type,
            messaging,
            fileOutPath,
            cmd.hasOption(argsIdentifier.NEW.getId()),
            setMainClass,
            cmd.hasOption(argsIdentifier.TIMESTAMP.getId()),
            javaHome,
            androidHome,
            args);

    //region Logging Information
    info.setPrettyPrint(cmd.hasOption(argsIdentifier.PRETTY.getId()));
    log.debug("Pretty flag: " + cmd.hasOption(argsIdentifier.PRETTY.getId()));

    info.setShowTimes(cmd.hasOption(argsIdentifier.TIMEMEASURE.getId()));
    log.debug("Time measure flag: " + cmd.hasOption(argsIdentifier.TIMEMEASURE.getId()));

    info.setStreaming(cmd.hasOption(argsIdentifier.STREAM.getId()));
    log.debug("Stream flag: " + cmd.hasOption(argsIdentifier.STREAM.getId()));

    info.setDisplayHeuristics(cmd.hasOption(argsIdentifier.HEURISTICS.getId()));
    log.debug("Heuristics flag: " + cmd.hasOption(argsIdentifier.HEURISTICS.getId()));

    Utils.initDepth(
        Integer.parseInt(cmd.getOptionValue(argsIdentifier.DEPTH.getId(), String.valueOf(1))));
    log.debug("Scanning using a depth of " + Utils.DEPTH);

    boolean noExitJVM = cmd.hasOption(argsIdentifier.NOEXIT.getId());
    log.debug("Exiting the JVM: " + noExitJVM);
    if (noExitJVM) info.setKillJVM(false);
    //endregion

    //Setting the raw command within info
    info.setRawCommand(Utils.join(" ", originalArguments));

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
    if (!currentVersion.supported()) {
      log.fatal("JRE Version: " + currentVersion + " is not compatible");
      throw new ExceptionHandler(
          "JRE Version: "
              + currentVersion
              + " is not compatible, please use JRE Version: "
              + Utils.supportedVersion,
          ExceptionId.GEN_VALID);
    }
    //endregion

    //region verifying filePaths
    //region Setting the source files
    log.trace("Retrieving the source files.");
    ArrayList<String> vSources =
        Utils.retrieveFilePathTypes(new ArrayList<>(sourceFiles), eType, true, false);
    log.info("Using the source file(s): " + retrieveFullyQualifiedName(vSources).toString());
    //endregion

    //region Setting the dependency path
    log.trace("Retrieving the dependency files.");
    List<String> vDeps = Utils.retrieveFilePathTypes(new ArrayList<>(dependencies), false, false);
    if (vDeps.size() > 0)
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
          Utils.retrieveFilePath(fileOutPath, oType.getOutputFileExt(), overWriteFileOut, true);
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

  private static Options setOptions() {
    Options cmdLineArgs = new Options();

    //region General Options
    Option format =
        Option.builder(argsIdentifier.FORMAT.getId())
            .required()
            .hasArg()
            .argName(argsIdentifier.FORMAT.getArgName())
            .desc(argsIdentifier.FORMAT.getDesc())
            .build();
    format.setType(String.class);
    format.setOptionalArg(argsIdentifier.FORMAT.getRequired());
    cmdLineArgs.addOption(format);

    Option sources =
        Option.builder(argsIdentifier.SOURCE.getId())
            .required()
            .hasArgs()
            .argName(argsIdentifier.SOURCE.getArgName())
            .desc(argsIdentifier.SOURCE.getDesc())
            .build();
    sources.setType(String.class);
    sources.setValueSeparator(' ');
    sources.setOptionalArg(argsIdentifier.SOURCE.getRequired());
    cmdLineArgs.addOption(sources);

    Option dependency =
        Option.builder(argsIdentifier.DEPENDENCY.getId())
            .hasArg()
            .argName(argsIdentifier.DEPENDENCY.getArgName())
            .desc(argsIdentifier.DEPENDENCY.getDesc())
            .build();
    dependency.setType(String.class);
    dependency.setOptionalArg(argsIdentifier.DEPENDENCY.getRequired());
    cmdLineArgs.addOption(dependency);

    Option mainFile =
        Option.builder(argsIdentifier.MAIN.getId())
            .hasArg()
            .argName(argsIdentifier.MAIN.getArgName())
            .desc(argsIdentifier.MAIN.getDesc())
            .build();
    mainFile.setType(String.class);
    mainFile.setOptionalArg(argsIdentifier.MAIN.getRequired());
    cmdLineArgs.addOption(mainFile);

    Option javaPath =
        Option.builder(argsIdentifier.JAVA.getId())
            .hasArg()
            .argName(argsIdentifier.JAVA.getArgName())
            .desc(argsIdentifier.JAVA.getDesc())
            .build();
    javaPath.setType(File.class);
    javaPath.setOptionalArg(argsIdentifier.JAVA.getRequired());
    cmdLineArgs.addOption(javaPath);

    Option androidPath =
        Option.builder(argsIdentifier.ANDROID.getId())
            .hasArg()
            .argName(argsIdentifier.ANDROID.getArgName())
            .desc(argsIdentifier.ANDROID.getDesc())
            .build();
    androidPath.setType(File.class);
    androidPath.setOptionalArg(argsIdentifier.ANDROID.getRequired());
    cmdLineArgs.addOption(androidPath);

    Option depth =
        Option.builder(argsIdentifier.DEPTH.getId())
            .hasArg()
            .argName(argsIdentifier.DEPTH.getArgName())
            .desc(argsIdentifier.DEPTH.getDesc())
            .build();
    depth.setType(String.class);
    depth.setOptionalArg(argsIdentifier.DEPTH.getRequired());
    cmdLineArgs.addOption(depth);

    Option output =
        Option.builder(argsIdentifier.OUT.getId())
            .hasArg()
            .argName(argsIdentifier.OUT.getArgName())
            .desc(argsIdentifier.OUT.getDesc())
            .build();
    output.setType(String.class);
    output.setOptionalArg(argsIdentifier.OUT.getRequired());
    cmdLineArgs.addOption(output);

    Option timing =
        new Option(argsIdentifier.TIMEMEASURE.getId(), false, argsIdentifier.TIMEMEASURE.getDesc());
    timing.setOptionalArg(argsIdentifier.TIMEMEASURE.getRequired());
    cmdLineArgs.addOption(timing);

    Option formatOut =
        Option.builder(argsIdentifier.FORMATOUT.getId())
            .hasArg()
            .argName(argsIdentifier.FORMATOUT.getArgName())
            .desc(argsIdentifier.FORMATOUT.getDesc())
            .build();
    formatOut.setOptionalArg(argsIdentifier.FORMATOUT.getRequired());
    cmdLineArgs.addOption(formatOut);

    Option prettyPrint =
        new Option(argsIdentifier.PRETTY.getId(), false, argsIdentifier.PRETTY.getDesc());
    prettyPrint.setOptionalArg(argsIdentifier.PRETTY.getRequired());
    cmdLineArgs.addOption(prettyPrint);

    Option noExit =
        new Option(argsIdentifier.NOEXIT.getId(), false, argsIdentifier.NOEXIT.getDesc());
    noExit.setOptionalArg(argsIdentifier.NOEXIT.getRequired());
    cmdLineArgs.addOption(noExit);

    Option help = new Option(argsIdentifier.HELP.getId(), false, argsIdentifier.HELP.getDesc());
    help.setOptionalArg(argsIdentifier.HELP.getRequired());
    cmdLineArgs.addOption(help);

    Option version =
        new Option(argsIdentifier.VERSION.getId(), false, argsIdentifier.VERSION.getDesc());
    version.setOptionalArg(argsIdentifier.VERSION.getRequired());
    cmdLineArgs.addOption(version);

    Option displayHeuristcs =
        new Option(argsIdentifier.HEURISTICS.getId(), false, argsIdentifier.HEURISTICS.getDesc());
    displayHeuristcs.setOptionalArg(argsIdentifier.HEURISTICS.getRequired());
    cmdLineArgs.addOption(displayHeuristcs);

    Option timeStamp =
        new Option(argsIdentifier.TIMESTAMP.getId(), false, argsIdentifier.TIMESTAMP.getDesc());
    timeStamp.setOptionalArg(argsIdentifier.TIMESTAMP.getRequired());
    cmdLineArgs.addOption(timeStamp);

    Option stream =
        new Option(argsIdentifier.STREAM.getId(), false, argsIdentifier.STREAM.getDesc());
    stream.setOptionalArg(argsIdentifier.STREAM.getRequired());
    cmdLineArgs.addOption(stream);

    Option nologs =
        new Option(argsIdentifier.NOLOGS.getId(), false, argsIdentifier.NOLOGS.getDesc());
    nologs.setOptionalArg(argsIdentifier.NOLOGS.getRequired());
    cmdLineArgs.addOption(nologs);

    Option verbose =
        new Option(argsIdentifier.VERBOSE.getId(), false, argsIdentifier.VERBOSE.getDesc());
    verbose.setOptionalArg(argsIdentifier.VERBOSE.getRequired());
    cmdLineArgs.addOption(verbose);

    Option vverbose =
        new Option(argsIdentifier.VERYVERBOSE.getId(), false, argsIdentifier.VERYVERBOSE.getDesc());
    vverbose.setOptionalArg(argsIdentifier.VERYVERBOSE.getRequired());
    cmdLineArgs.addOption(vverbose);

    Option newFile = new Option(argsIdentifier.NEW.getId(), false, argsIdentifier.NEW.getDesc());
    newFile.setOptionalArg(argsIdentifier.NEW.getRequired());
    cmdLineArgs.addOption(newFile);
    //endregion

    log.trace("Set the command line options to be used for parsing.");
    return cmdLineArgs;
  }
}

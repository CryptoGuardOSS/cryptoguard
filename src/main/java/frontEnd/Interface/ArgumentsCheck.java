package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.Interface.outputRouting.parcelHandling;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;
import rule.engine.EngineType;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>ArgumentsCheck class.</p>
 *
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @version $Id: $Id
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
        List<String> preservedArguments = args;

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
            String arg = null;

            if (e.getMessage().startsWith("Missing required option: "))
                arg = argsIdentifier.lookup(e.getMessage().replace("Missing required option: ", "")).getArg();
            else if (e.getMessage().startsWith("Missing required options: ")) {
                String[] argIds = e.getMessage().replace("Missing required options: ", "").replace(" ", "").split(",");
                arg = "Issue with the following argument(s) ";

                for (String argId : argIds)
                    arg += argsIdentifier.lookup(argId) + ", ";

                arg = arg.substring(0, arg.length() - ", ".length());

            }

            throw new ExceptionHandler(parcelHandling.retrieveHelpFromOptions(cmdLineArgs, arg), ExceptionId.ARG_VALID);
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

        Boolean verify = !cmd.hasOption(argsIdentifier.SKIPINPUTVALIDATION.getId());
        log.debug("Verification flag: " + verify);

        //region Setting the source files
        log.trace("Retrieving the source files.");
        List<String> source = verify ? Utils.retrieveFilesByType(
                Arrays.asList(
                        cmd.getOptionValues(argsIdentifier.SOURCE.getId())), type)
                : Arrays.asList(
                cmd.getOptionValues(argsIdentifier.SOURCE.getId()));
        log.info("Using the source file(s): " + source.toString());
        //endregion

        //region Setting the dependency path
        List<String> dependencies = null;
        if (cmd.hasOption(argsIdentifier.DEPENDENCY.getId())) {
            log.trace("Retrieving the dependency files.");
            dependencies = verify ? Utils.retrieveDirs(
                    Arrays.asList(
                            cmd.getOptionValues(argsIdentifier.DEPENDENCY.getId())))
                    : Arrays.asList(
                    cmd.getOptionValues(argsIdentifier.DEPENDENCY.getId()))
            ;
            log.info("Using the dependency file(s): " + source.toString());
        }
        //endregion

        Listing messaging = Listing.retrieveListingType(cmd.getOptionValue(argsIdentifier.FORMATOUT.getId()));
        log.info("Using the output: " + messaging.getType());

        //region Retrieving the package path
        log.trace("Retrieving the package path, may/may not be able to be replaced.");
        List<String> basePath = new ArrayList<String>();
        File sourceFile;
        String pkg = "";
        switch (type) {
            case APK:
            case JAR:
                sourceFile = new File(source.get(0));
                basePath.add(sourceFile.getName());
                pkg = sourceFile.getName();
                break;
            case DIR:
                sourceFile = new File(source.get(0));
                try {
                    basePath.add(sourceFile.getCanonicalPath() + ":dir");
                } catch (IOException e) {
                }
                pkg = sourceFile.getName();
                break;
            case JAVAFILES:
            case CLASSFILES:
                for (String file : source) {
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

        EnvironmentInformation info = new EnvironmentInformation(source, type, messaging, dependencies, basePath, pkg);

        //region Setting the file out
        log.trace("Determining the file out.");
        String fileOutPath = "";
        if (cmd.hasOption(argsIdentifier.OUT.getId()))
            if (verify)
                fileOutPath = Utils.verifyFileOut(cmd.getOptionValue(argsIdentifier.OUT.getId()), messaging, cmd.hasOption(argsIdentifier.NEW.getId()));
            else
                fileOutPath = cmd.getOptionValue(argsIdentifier.OUT.getId());
        else
            fileOutPath = Utils.osPathJoin(System.getProperty("user.dir"),
                    info.getPackageName() /*+ "_" + fileName*/ + info.getMessagingType().getOutputFileExt());

        if (cmd.hasOption(argsIdentifier.TIMESTAMP.getId())) {
            String[] tempSplit = fileOutPath.split("\\.\\w+$");
            fileOutPath = tempSplit[0] + "_" + Utils.getCurrentTimeStamp() + info.getMessagingType().getOutputFileExt();
        }
        log.debug("File out: " + fileOutPath);
        info.setFileOut(fileOutPath);


        //endregion

        if (!messaging.getTypeOfMessagingInput().inputValidation(info, args.toArray(new String[0]))) {
            log.error("Issue Validating Output Specific Arguments.");
            //TODO - Add better output message for this case
            throw new ExceptionHandler(messaging.getInputHelp(), ExceptionId.FORMAT_VALID);
        }

        info.setPrettyPrint(cmd.hasOption(argsIdentifier.PRETTY.getId()));
        log.debug("Pretty flag: " + argsIdentifier.PRETTY.getId());

        info.setShowTimes(cmd.hasOption(argsIdentifier.TIMEMEASURE.getId()));
        log.debug("Time measure flag: " + argsIdentifier.TIMEMEASURE.getId());

        info.setStreaming(cmd.hasOption(argsIdentifier.STREAM.getId()));
        log.debug("Stream flag: " + argsIdentifier.STREAM.getId());

        //Setting the raw command within info
        info.setRawCommand(Utils.join(" ", preservedArguments));

        return info;

    }

    private static Options setOptions() {
        Options cmdLineArgs = new Options();

        Option format = Option.builder(argsIdentifier.FORMAT.getId()).required().hasArg().argName("format").desc(argsIdentifier.FORMAT.getDesc()).build();
        format.setType(String.class);
        format.setOptionalArg(false);
        cmdLineArgs.addOption(format);

        Option sources = Option.builder(argsIdentifier.SOURCE.getId()).required().hasArgs().argName("file(s)/dir").desc(argsIdentifier.SOURCE.getDesc()).build();
        sources.setType(String.class);
        sources.setValueSeparator(' ');
        sources.setOptionalArg(false);
        cmdLineArgs.addOption(sources);

        Option dependency = Option.builder(argsIdentifier.DEPENDENCY.getId()).hasArg().argName("dir").desc(argsIdentifier.DEPENDENCY.getDesc()).build();
        dependency.setType(String.class);
        dependency.setOptionalArg(false);
        cmdLineArgs.addOption(dependency);

        Option output = Option.builder(argsIdentifier.OUT.getId()).hasArg().argName("file").desc(argsIdentifier.OUT.getDesc()).build();
        output.setType(String.class);
        output.setOptionalArg(true);
        cmdLineArgs.addOption(output);

        Option timing = new Option(argsIdentifier.TIMEMEASURE.getId(), false, argsIdentifier.TIMEMEASURE.getDesc());
        timing.setOptionalArg(true);
        cmdLineArgs.addOption(timing);

        Option formatOut = Option.builder(argsIdentifier.FORMATOUT.getId()).hasArg().argName("formatType").desc(argsIdentifier.FORMATOUT.getDesc()).build();
        formatOut.setOptionalArg(false);
        cmdLineArgs.addOption(formatOut);

        Option prettyPrint = new Option(argsIdentifier.PRETTY.getId(), false, argsIdentifier.PRETTY.getDesc());
        prettyPrint.setOptionalArg(true);
        cmdLineArgs.addOption(prettyPrint);

        Option help = new Option(argsIdentifier.HELP.getId(), false, argsIdentifier.HELP.getDesc());
        help.setOptionalArg(true);
        cmdLineArgs.addOption(help);

        Option version = new Option(argsIdentifier.VERSION.getId(), false, argsIdentifier.VERSION.getDesc());
        version.setOptionalArg(true);
        cmdLineArgs.addOption(version);

        Option skipInput = new Option(argsIdentifier.SKIPINPUTVALIDATION.getId(), false, argsIdentifier.SKIPINPUTVALIDATION.getDesc());
        skipInput.setOptionalArg(true);
        cmdLineArgs.addOption(skipInput);

        Option timeStamp = new Option(argsIdentifier.TIMESTAMP.getId(), false, argsIdentifier.TIMESTAMP.getDesc());
        skipInput.setOptionalArg(true);
        cmdLineArgs.addOption(timeStamp);

        Option stream = new Option(argsIdentifier.STREAM.getId(), false, argsIdentifier.STREAM.getDesc());
        stream.setOptionalArg(true);
        cmdLineArgs.addOption(stream);

        log.trace("Set the command line options to be used for parsing.");
        return cmdLineArgs;
    }

}

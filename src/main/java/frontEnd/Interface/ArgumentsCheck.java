package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import org.apache.commons.cli.*;
import rule.engine.EngineType;
import util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
public class ArgumentsCheck {

    private static final String PropertiesFile = "gradle.properties";

    /**
     * The fail fast parameter Check method
     * <p>This method will attempt to create the Environment Information and provide help if the usage doesn't match</p>
     *
     * @param args {@link java.lang.String} - the raw arguments passed into the console
     * @return {@link EnvironmentInformation} - when not null, the general Information is created for usage within any output structure.
     * @throws ExceptionHandler if any.
     */
    public static EnvironmentInformation paramaterCheck(List<String> args) throws ExceptionHandler {

        //region CLI Section

        Options cmdLineArgs = setOptions();
        CommandLine cmd = null;

        try {
            cmd = new DefaultParser().parse(cmdLineArgs, args.toArray(new String[0]), true);
        } catch (ParseException e) {
            String arg = null;

            if (e.getMessage().startsWith("Missing required option: "))
                arg = argsIdentifier.lookup(e.getMessage().replace("Missing required option: ", "")).getArg();
            else if (e.getMessage().startsWith("Missing required options: ")) {
                String[] argIds = e.getMessage().replace("Missing required options: ", "").replace(" ", "").split(",");
                arg = "";

                for (String argId : argIds)
                    arg += argsIdentifier.lookup(argId) + ", ";

                arg = arg.substring(0, arg.length() - ", ".length());

            }

            failFast(arg, cmdLineArgs, true);
        }

        //endregion

        //region Cleaning retrieved values from args
        ArrayList<String> upgradedArgs = new ArrayList<>(args);
        for (argsIdentifier arg : argsIdentifier.values()) {
            if (cmd.hasOption(arg.getId())) {
                upgradedArgs.remove("-" + arg.getId());
                upgradedArgs.remove(cmd.getOptionValue(arg.getId()));
            }
        }
        args = upgradedArgs;
        //endregion

        //region Printing Help or Version
        if (cmd.hasOption(argsIdentifier.HELP.getId()))
            failFast(null, cmdLineArgs, false);
        else if (cmd.hasOption(argsIdentifier.VERSION.getId())) {
            String version = "UNKNOWN";
            try {
                Properties Properties = new Properties();
                Properties.load(new FileInputStream(PropertiesFile));
                version = Properties.getProperty("version");
                {
                }
            } catch (IOException e) {
                throw new ExceptionHandler("V:" + version, ExceptionId.HELP);
            }
        }
        //endregion

        EngineType type = EngineType.getFromFlag(cmd.getOptionValue(argsIdentifier.FORMAT.getId()));

        Boolean verify = !cmd.hasOption(argsIdentifier.SKIPINPUTVALIDATION.getId());

        //region Setting the source files
        List<String> source = verify ? Utils.retrieveFilesByType(
                Arrays.asList(
                        cmd.getOptionValues(argsIdentifier.SOURCE.getId())), type)
                : Arrays.asList(
                cmd.getOptionValues(argsIdentifier.SOURCE.getId()));
        //endregion

        //region Setting the dependency path

        List<String> dependencies = null;
        if (cmd.hasOption(argsIdentifier.DEPENDENCY.getId()))
            dependencies = verify ? Utils.retrieveDirs(
                    Arrays.asList(
                            cmd.getOptionValues(argsIdentifier.DEPENDENCY.getId())))
                    : Arrays.asList(
                    cmd.getOptionValues(argsIdentifier.DEPENDENCY.getId()))
                    ;
        //endregion

        Listing messaging = Listing.retrieveListingType(cmd.getOptionValue(argsIdentifier.FORMATOUT.getId()));

        //region Retrieving the package path
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
        //endregion

        EnvironmentInformation info = new EnvironmentInformation(source, type, messaging, dependencies, basePath, pkg);

        //region Setting the file out
        String fileOutPath = "";
        if (cmd.hasOption(argsIdentifier.OUT.getId()))
            if (verify)
                fileOutPath = Utils.verifyFileOut(cmd.getOptionValue(argsIdentifier.OUT.getId()), messaging);
            else
                fileOutPath = cmd.getOptionValue(argsIdentifier.OUT.getId());
        else
            fileOutPath = Utils.osPathJoin(System.getProperty("user.dir"),
                    info.getPackageName() /*+ "_" + fileName*/ + info.getMessagingType().getOutputFileExt());

        if (cmd.hasOption(argsIdentifier.TIMESTAMP.getId())) {
            String[] tempSplit = fileOutPath.split("\\.\\w+$");
            fileOutPath = tempSplit[0] + "_" + Utils.getCurrentTimeStamp() + info.getMessagingType().getOutputFileExt();
        }

        info.setFileOut(fileOutPath);


        //endregion

        if (!messaging.getTypeOfMessagingInput().inputValidation(info, args.toArray(new String[0]))) {
            throw new ExceptionHandler(messaging.getInputHelp(), ExceptionId.FORMAT_VALID);
        }

        info.setPrettyPrint(cmd.hasOption(argsIdentifier.PRETTY.getId()));
        info.setShowTimes(cmd.hasOption(argsIdentifier.TIMEMEASURE.getId()));
        info.setStreaming(cmd.hasOption(argsIdentifier.STREAM.getId()));

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

        return cmdLineArgs;
    }

    /**
     * A universal method to return failure/help message
     *
     * @param args - a {@link org.apache.commons.cli.Options} object.
     */
    private static void failFast(String argument, Options args, Boolean broken) throws ExceptionHandler {

        HelpFormatter helper = new HelpFormatter();
        helper.setOptionComparator(null);

        String projectName = "Project";

        try {
            Properties Properties = new Properties();
            Properties.load(new FileInputStream(PropertiesFile));
            projectName = Properties.getProperty("projectName") + ": " + Properties.getProperty("versionNumber");
        } catch (IOException e) {
        }

        StringWriter message = new StringWriter();
        PrintWriter redirect = new PrintWriter(message);

        if (argument != null)
            redirect.write("Issue with argument: " + argument + ".\n");

        helper.printHelp(redirect, 100, projectName, null, args, 0, 0, null);

        if (!broken) {
            redirect.write(Listing.getInputFullHelp());
        }

        if (broken)
            throw new ExceptionHandler(message.toString(), ExceptionId.GEN_VALID);
        else
            throw new ExceptionHandler(message.toString(), ExceptionId.HELP);
    }

}

package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.argsIdentifier;
import org.apache.commons.cli.*;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * <p>ScarfXML class.</p>
 *
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @version $Id: $Id
 * @since 01.01.07
 *
 * <p>The Scarf Input check implementation.</p>
 */
public class ScarfXML implements InputStructure {

    /**
     * {@inheritDoc}
     * The overridden method for the ScarfXML output.
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @param args an array of {@link java.lang.String} objects.
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean inputValidation(EnvironmentInformation info, String[] args) {

        CommandLine cmd = null;
        Options cmdLineArgs = setOptions();
        try {
            cmd = new DefaultParser().parse(cmdLineArgs, args);
        } catch (ParseException e) {
            System.out.println("====================================================");

            if (e.getMessage().startsWith("Missing required option: "))
                System.out.println("Please enter a valid " +
                        argsIdentifier.lookup(e.getMessage().replace("Missing required option: ", "")));
            else
                System.out.println("Please enter valid information.");

            System.out.println("====================================================");

            System.out.println(this.helpInfo());
            System.exit(0);
        }

        //region Retrieving and setting the values
        info.setAssessmentFramework(cmd.getOptionValue(ScarfXMLId.AssessmentFramework.getId(), "UNKNOWN"));
        info.setAssessmentFrameworkVersion(cmd.getOptionValue(ScarfXMLId.AssessmentFrameworkVersion.getId(), "UNKNOWN"));
        info.setBuildRootDir(cmd.getOptionValue(ScarfXMLId.BuildRootDir.getId(), "UNKNOWN"));
        info.setPackageRootDir(cmd.getOptionValue(ScarfXMLId.PackageRootDir.getId(), "UNKNOWN"));
        info.setParserName(cmd.getOptionValue(ScarfXMLId.ParserName.getId(), "UNKNOWN"));
        info.setParserVersion(cmd.getOptionValue(ScarfXMLId.ParserVersion.getId(), "UNKNOWN"));
        info.setUUID(cmd.getOptionValue(ScarfXMLId.UUID.getId(), java.util.UUID.randomUUID().toString()));
        //endregion

        return true;
    }

    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output.
     *
     * @return a {@link java.lang.String} object.
     */
    public String helpInfo() {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        HelpFormatter helper = new HelpFormatter();
        helper.setOptionComparator(null);
        helper.printHelp("ScarfXML", setOptions(), false);

        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        return out.toString();

    }

    private static Options setOptions() {
        Options cmdLineArgs = new Options();

        Option AssessmentFramework = Option.builder(ScarfXMLId.AssessmentFramework.getId()).hasArg().argName("string").desc(ScarfXMLId.AssessmentFramework.getDesc()).build();
        AssessmentFramework.setType(String.class);
        AssessmentFramework.setOptionalArg(false);
        cmdLineArgs.addOption(AssessmentFramework);

        Option AssessmentFrameworkVersion = Option.builder(ScarfXMLId.AssessmentFrameworkVersion.getId()).hasArg().argName("string").desc(ScarfXMLId.AssessmentFrameworkVersion.getDesc()).build();
        AssessmentFrameworkVersion.setType(String.class);
        AssessmentFrameworkVersion.setOptionalArg(false);
        cmdLineArgs.addOption(AssessmentFrameworkVersion);

        Option BuildRootDir = Option.builder(ScarfXMLId.BuildRootDir.getId()).hasArg().argName("string").desc(ScarfXMLId.BuildRootDir.getDesc()).build();
        BuildRootDir.setType(String.class);
        BuildRootDir.setOptionalArg(false);
        cmdLineArgs.addOption(BuildRootDir);

        Option PackageRootDir = Option.builder(ScarfXMLId.PackageRootDir.getId()).hasArg().argName("string").desc(ScarfXMLId.PackageRootDir.getDesc()).build();
        PackageRootDir.setType(String.class);
        PackageRootDir.setOptionalArg(true);
        cmdLineArgs.addOption(PackageRootDir);

        Option ParserName = Option.builder(ScarfXMLId.ParserName.getId()).hasArg().argName("string").desc(ScarfXMLId.ParserName.getDesc()).build();
        ParserName.setType(String.class);
        ParserName.setOptionalArg(false);
        cmdLineArgs.addOption(ParserName);

        Option ParserVersion = Option.builder(ScarfXMLId.ParserVersion.getId()).hasArg().argName("string").desc(ScarfXMLId.ParserVersion.getDesc()).build();
        ParserVersion.setType(String.class);
        ParserVersion.setOptionalArg(false);
        cmdLineArgs.addOption(ParserVersion);

        Option UUID = Option.builder(ScarfXMLId.UUID.getId()).hasArg().argName("string").desc(ScarfXMLId.UUID.getDesc()).build();
        UUID.setType(String.class);
        UUID.setOptionalArg(false);
        cmdLineArgs.addOption(UUID);

        return cmdLineArgs;
    }

}

package frontEnd.MessagingSystem.routing.inputStructures;

import frontEnd.Interface.ExceptionHandler;
import frontEnd.Interface.ExceptionId;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.argsIdentifier;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.Properties;

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
public class ScarfXML implements Structure {

    /**
     * {@inheritDoc}
     * The overridden method for the ScarfXML output.
     *
     * @param info a {@link EnvironmentInformation} object.
     * @param args an array of {@link java.lang.String} objects.
     * @return a {@link java.lang.Boolean} object.
     * @throws ExceptionHandler if any.
     */
    public Boolean inputValidation(EnvironmentInformation info, String[] args) throws ExceptionHandler {

        CommandLine cmd = null;
        Options cmdLineArgs = setOptions();
        try {
            cmd = new DefaultParser().parse(cmdLineArgs, args);
        } catch (ParseException e) {
            String arg = null;

            if (e.getMessage().startsWith("Missing required option: "))
                arg = argsIdentifier.lookup(e.getMessage().replace("Missing required option: ", "")).getArg();

            if (arg == null)
                throw new ExceptionHandler(this.helpInfo(), ExceptionId.GEN_VALID);
            else
                throw new ExceptionHandler("Issue with the argument: " + arg, ExceptionId.GEN_VALID);
        }

        if (cmd.hasOption(ScarfXMLId.ConfigFile.getId())) {
            //region Retrieving and setting the values
            Properties SWAMPProperties = loadProperties(cmd.getOptionValue(ScarfXMLId.ConfigFile.getId()));

            info.setAssessmentFramework(SWAMPProperties.getProperty("assess_fw", "UNKNOWN"));
            info.setAssessmentFrameworkVersion(SWAMPProperties.getProperty("assess_fw_version", "UNKNOWN"));
            info.setAssessmentStartTime(SWAMPProperties.getProperty("assessment_start_ts", null));
            info.setBuildFramework(SWAMPProperties.getProperty("build_fw", "UNKNOWN"));
            info.setBuildFrameworkVersion(SWAMPProperties.getProperty("build_fw_version", "UNKNOWN"));
            info.setBuildRootDir(SWAMPProperties.getProperty("build_root_dir", "UNKNOWN"));
            info.setPackageName(SWAMPProperties.getProperty("package_name", "UNKNOWN"));
            info.setPackageRootDir(SWAMPProperties.getProperty("package_root_dir", "UNKNOWN"));
            info.setPackageVersion(SWAMPProperties.getProperty("package_version", "UNKNOWN"));
            info.setParserName(SWAMPProperties.getProperty("parser_fw", "UNKNOWN"));
            info.setParserVersion(SWAMPProperties.getProperty("parser_fw_version", "UNKNOWN"));
            info.setUUID(SWAMPProperties.getProperty("uuid", null));


            //endregion
        }

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

    Options setOptions() {
        Options cmdLineArgs = new Options();

        Option config = Option.builder(ScarfXMLId.ConfigFile.getId()).hasArg().argName("file.properties").desc(ScarfXMLId.ConfigFile.getDesc()).build();
        config.setType(String.class);
        config.setOptionalArg(false);
        cmdLineArgs.addOption(config);

        return cmdLineArgs;
    }

    Properties loadProperties(String path) throws ExceptionHandler {
        try {
            Properties SWAMPProperties = new Properties();
            SWAMPProperties.load(new FileInputStream(path));

            return SWAMPProperties;
        } catch (FileNotFoundException e) {
            throw new ExceptionHandler("Config File: " + path + " not found", ExceptionId.FILE_AFK);
        } catch (IOException e) {
            throw new ExceptionHandler("Error Loading: " + path, ExceptionId.FILE_READ);
        }
    }

}

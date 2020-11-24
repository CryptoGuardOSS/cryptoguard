/* Licensed under GPL-3.0 */
package frontEnd.Interface.formatArgument;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

/**
 * ScarfXML class.
 *
 * @author CryptoguardTeam Created on 2018-12-14.
 * @version 03.07.01
 * @since 01.01.07
 *     <p>The Scarf Input check implementation.
 */
public class ScarfXML implements TypeSpecificArg {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(ScarfXML.class);

  /** Constructor for ScarfXML. */
  public ScarfXML() {}

  /** {@inheritDoc} The overridden method for the ScarfXML output. */
  public Boolean inputValidation(EnvironmentInformation info, List<String> args)
      throws ExceptionHandler {

    CommandLine cmd = null;
    Options cmdLineArgs = getOptions();
    try {
      cmd = new DefaultParser().parse(cmdLineArgs, args.stream().toArray(String[]::new));
    } catch (ParseException e) {
      String arg = null;

      if (e.getMessage().startsWith("Missing required option: "))
        arg =
            argsIdentifier.lookup(e.getMessage().replace("Missing required option: ", "")).getArg();

      if (arg == null) {
        log.info("Retrieving help info");
        throw new ExceptionHandler("Issue using arguments", ExceptionId.GEN_VALID);
      } else {
        log.fatal("Issue with the argument: " + arg);
        throw new ExceptionHandler("Issue with the argument: " + arg, ExceptionId.GEN_VALID);
      }
    }

    if (cmd.hasOption(argsIdentifier.SCONFIG.getId())) {
      //region Retrieving and setting the values
      Properties SWAMPProperties =
          loadProperties(cmd.getOptionValue(argsIdentifier.SCONFIG.getId()));

      info.setAssessmentFramework(SWAMPProperties.getProperty("assess_fw", "UNKNOWN"));
      info.setAssessmentFrameworkVersion(
          SWAMPProperties.getProperty("assess_fw_version", "UNKNOWN"));
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
    } else {
      //region Setting the values if they're in the extra arguments list
      info.setAssessmentFramework(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SASSESSFW.getId()), null));
      info.setAssessmentFrameworkVersion(
          cmd.getOptionValue(
              StringUtils.trimToNull(argsIdentifier.SASSESSFWVERSION.getId()), null));
      info.setAssessmentStartTime(
          cmd.getOptionValue(
              StringUtils.trimToNull(argsIdentifier.SASSESSMENTSTARTTS.getId()), null));
      info.setBuildFramework(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SBUILDFW.getId()), null));
      info.setBuildFrameworkVersion(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SBUILDFWVERSION.getId()), null));
      info.setBuildRootDir(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SBUILDROOTDIR.getId()), null));
      info.setPackageName(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SPACKAGENAME.getId()), null));
      info.setPackageRootDir(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SPACKAGEROOTDIR.getId()), null));
      info.setPackageVersion(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SPACKAGEVERSION.getId()), null));
      info.setParserName(
          cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SPARSERFW.getId()), null));
      info.setParserVersion(
          cmd.getOptionValue(
              StringUtils.trimToNull(argsIdentifier.SPARSERFWVERSION.getId()), null));
      info.setUUID(cmd.getOptionValue(StringUtils.trimToNull(argsIdentifier.SUUID.getId()), null));
      //endregion
    }

    return true;
  }

  /**
   * getOptions.
   *
   * @return a {@link org.apache.commons.cli.Options} object.
   */
  public Options getOptions() {
    Options cmdLineArgs = new Options();

    for (argsIdentifier arg : Listing.ScarfXML.retrieveArgs()) {
      Option tempConfig =
          Option.builder(arg.getId())
              .hasArg()
              .argName(arg.getArgName())
              .desc(arg.getDesc())
              .build();

      if (arg.hasDefaultArg()) tempConfig.setType(String.class);

      tempConfig.setOptionalArg(arg.getRequired());
      cmdLineArgs.addOption(tempConfig);
    }

    return cmdLineArgs;
  }

  /**
   * loadProperties.
   *
   * @param path a {@link java.lang.String} object.
   * @return a {@link java.util.Properties} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public Properties loadProperties(String path) throws ExceptionHandler {
    try {
      Properties SWAMPProperties = new Properties();
      SWAMPProperties.load(new FileInputStream(path));

      return SWAMPProperties;
    } catch (FileNotFoundException e) {
      log.fatal("Config File: " + path + " not found");
      throw new ExceptionHandler("Config File: " + path + " not found", ExceptionId.FILE_AFK);
    } catch (IOException e) {
      log.fatal("Error Loading: " + path);
      throw new ExceptionHandler("Error Loading: " + path, ExceptionId.FILE_READ);
    }
  }
}

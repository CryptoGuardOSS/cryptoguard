/* Licensed under GPL-3.0 */
package frontEnd.Interface.outputRouting;

import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import util.Utils;

/**
 * ExceptionId class.
 *
 * @author CryptoguardTeam Created on 04/25/19.
 * @version 03.07.01
 * @since 03.04.04
 *     <p>The enumeration of the error codes.
 */
public class parcelHandling {

  private static final int Width = 140;
  private static final Logger log =
      org.apache.logging.log4j.LogManager.getLogger(parcelHandling.class);

  /**
   * retrieveHeaderInfo.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveHeaderInfo() {
    return Utils.projectName + ": " + Utils.projectVersion + "\n";
  }

  /**
   * getUsage.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getUsage() {
    StringBuilder output = new StringBuilder();
    output.append("java -jar ").append(Utils.projectName.toLowerCase()).append(" ");

    for (argsIdentifier arg : argsIdentifier.values())
      if (arg.getFormatType() == null) {
        output.append(arg.getArg()).append(" ");
        if (arg.hasDefaultArg()) output.append("<").append(arg.getDefaultArg()).append("> ");
      }

    output.append("\n");

    for (Listing type : Listing.values()) {
      List<argsIdentifier> args = type.retrieveArgs();
      if (args.size() > 1) {
        output
            .append("\n")
            .append("FormatType ")
            .append(type.getType())
            .append(" specific arguments")
            .append("\n");
        output.append(StringUtils.repeat("#", Width / 4)).append("\n");
        for (argsIdentifier arg : args) {
          output.append(arg.getArg()).append(" ");
          if (arg.hasDefaultArg()) output.append("<").append(arg.getDefaultArg()).append("> ");
        }
        output.append("\n");
      }
    }

    return output.toString();
  }

  /**
   * retrieveHelpFromOptions.
   *
   * @param args a {@link org.apache.commons.cli.Options} object.
   * @param argIssue a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveHelpFromOptions(Options args, String argIssue) {

    HelpFormatter helper = new HelpFormatter();
    helper.setOptionComparator(null);

    StringWriter message = new StringWriter();
    PrintWriter redirect = new PrintWriter(message);

    if (argIssue == null) message.append(StringUtils.repeat("#", Width)).append("\n");

    message.append(retrieveHeaderInfo()).append("\n");

    StringBuilder headerInfo = new StringBuilder();
    headerInfo.append("\n");

    if (argIssue == null) headerInfo.append("General Help");
    else headerInfo.append(argIssue);

    if (argIssue != null) {
      headerInfo
          .append(", please run java -jar ")
          .append(Utils.projectName.toLowerCase())
          .append(".jar -h for help.")
          .append("\n")
          .append(StringUtils.repeat("-", headerInfo.length()));
      message.append(headerInfo);
    } else {
      helper.printHelp(redirect, Width, getUsage(), headerInfo.toString(), args, 0, 0, null);
      for (Listing type : Listing.values()) {
        try {
          Options opt = type.retrieveSpecificArgHandler().getOptions();
          if (opt != null) {
            helper.printHelp(
                redirect, Width, type.getType() + " specific usage", null, opt, 0, 0, null);
          }
        } catch (ExceptionHandler e) {
          log.warn("Issue retrieving arguments from " + type.getType() + " listing type");
        }
      }
      message.append(StringUtils.repeat("#", Width)).append("\n");
    }

    return message.toString();
  }
}

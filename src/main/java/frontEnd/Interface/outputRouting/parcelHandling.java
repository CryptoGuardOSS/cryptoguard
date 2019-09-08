package frontEnd.Interface.outputRouting;

import frontEnd.argsIdentifier;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import util.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>ExceptionId class.</p>
 *
 * @author CryptoguardTeam
 * Created on 04/25/19.
 * @version 03.07.01
 * @since 03.04.04
 *
 * <p>The enumeration of the error codes.</p>
 */
public class parcelHandling {

    private static final int Width = 140;

    /**
     * <p>retrieveHeaderInfo.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveHeaderInfo() {
        StringBuilder output = new StringBuilder();
        output.append(Utils.projectName).append(": ").append(Utils.projectVersion);
        output.append("\n");
        return output.toString();
    }

    /**
     * <p>getUsage.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getUsage() {
        StringBuilder output = new StringBuilder();
        output.append("java -jar ").append(Utils.projectName.toLowerCase()).append(" ");

        for (argsIdentifier arg : argsIdentifier.values()) {
            output.append(arg.getArg()).append(" ");
            if (arg.hasDefaultArg())
                output.append("<").append(arg.getDefaultArg()).append("> ");
        }

        return output.toString();
    }

    /**
     * <p>retrieveHelpFromOptions.</p>
     *
     * @param args     a {@link org.apache.commons.cli.Options} object.
     * @param argIssue a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveHelpFromOptions(Options args, String argIssue) {

        HelpFormatter helper = new HelpFormatter();
        helper.setOptionComparator(null);

        StringWriter message = new StringWriter();
        PrintWriter redirect = new PrintWriter(message);

        if (argIssue == null)
            message.append(StringUtils.repeat("#", Width)).append("\n");

        message.append(retrieveHeaderInfo()).append("\n");

        StringBuilder headerInfo = new StringBuilder();
        headerInfo.append("\n");

        if (argIssue == null)
            headerInfo.append("General Validation Issue");
        else
            headerInfo.append(argIssue);


        if (argIssue != null) {
            headerInfo.append(", please run java7 -jar " + Utils.projectName.toLowerCase() + ".jar -h for help.").append("\n").append(StringUtils.repeat("-", headerInfo.length()));
            message.append(headerInfo);
        } else {
            helper.printHelp(redirect, Width, getUsage(), headerInfo.toString(), args, 0, 0, null);
            message.append(StringUtils.repeat("#", Width)).append("\n");
        }

        return message.toString();
    }

}

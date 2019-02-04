package main.frontEnd.Interface;

import groovyjarjarcommonscli.*;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.EngineType;

import java.util.List;

/**
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @since 01.01.02
 *
 * <p>The main check for the Arguments</p>
 */
public class ArgumentsCheck {

    enum argumentsMatch {

        FORMAT("in", "(Req'd) The format of input you want to scan."),
        SOURCE("s", "(Req'd) The source(s) to be scanned, use the absolute path)."),
        DEP("d", "The dependency to be scanned, (use the relative path)."),
        OUT("o", "The file to be created with the output (default will be the project name)."),
        TIMEMEASURE("t", "Output the time of the internal processes."),
        FORMATOUT("m", "The output format you want to produce."),
        PRETTY("n", "Output the analysis information in a 'pretty' format."),
        HELP("h", "Print out the Help Information.");


        private String id;
        private String desc;

        argumentsMatch(String id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public String getId() {
            return this.id;
        }

        public String getDesc() {
            return this.desc;
        }

        public static argumentsMatch lookup(String id) {
            for (argumentsMatch in : argumentsMatch.values())
                if (in.getId().equals(id))
                    return in;
            return null;
        }
    }

    /**
     * The fail fast parameter Check method
     * <p>This method will attempt to create the Environment Information and provide help if the usage doesn't match</p>
     *
     * @param args {@link java.lang.String} - the raw arguments passed into the console
     * @return {@link EnvironmentInformation} - when not null, the general Information is created for usage within any output structure.
     */
    public static EnvironmentInformation paramaterCheck(List<String> args) throws ExceptionHandler {

        EngineType flow;
        EnvironmentInformation info;

        //region CLI Section
        //region CLI Arguments
        Options cmdLineArgs = new Options();

        //cmdLineArgs.addOption("f", false, "Output the time of the internal processes.");
        Option format = OptionBuilder.withArgName("format").hasArg().withDescription(argumentsMatch.FORMAT.getDesc()).isRequired().create(argumentsMatch.FORMAT.getId());
        format.setType(String.class);
        format.setOptionalArg(false);
        cmdLineArgs.addOption(format);

        Option sources = OptionBuilder.withArgName("file(s)/dir").hasArgs().withDescription(argumentsMatch.SOURCE.getDesc()).isRequired().create(argumentsMatch.SOURCE.getId());
        sources.setType(String.class);
        sources.setValueSeparator(' ');
        sources.setOptionalArg(false);
        cmdLineArgs.addOption(sources);

        Option dependency = OptionBuilder.withArgName("dir").hasArg().withDescription(argumentsMatch.DEP.getDesc()).create(argumentsMatch.DEP.getId());
        dependency.setType(String.class);
        dependency.setOptionalArg(false);
        cmdLineArgs.addOption(dependency);

        Option output = OptionBuilder.withArgName("file").hasArg().withDescription(argumentsMatch.OUT.getDesc()).create(argumentsMatch.OUT.getId());
        output.setType(String.class);
        output.setOptionalArg(true);
        cmdLineArgs.addOption(output);

        Option timing = new Option(argumentsMatch.TIMEMEASURE.getId(), false, argumentsMatch.TIMEMEASURE.getDesc());
        timing.setOptionalArg(true);
        cmdLineArgs.addOption(timing);

        Option formatOut = OptionBuilder.withArgName("formatType").hasArg().withDescription(argumentsMatch.FORMATOUT.getDesc()).create(argumentsMatch.FORMATOUT.getId());
        formatOut.setOptionalArg(false);
        cmdLineArgs.addOption(formatOut);

        Option prettyPrint = new Option(argumentsMatch.PRETTY.getId(), true, argumentsMatch.PRETTY.getDesc());
        prettyPrint.setOptionalArg(true);
        cmdLineArgs.addOption(prettyPrint);

        Option help = new Option(argumentsMatch.HELP.getId(), false, argumentsMatch.HELP.getDesc());
        help.setOptionalArg(true);
        cmdLineArgs.addOption(help);

        //endregion

        CommandLine cmd = null;

        try {
            cmd = new DefaultParser().parse(cmdLineArgs, args.toArray(new String[0]));
        } catch (ParseException e) {//TODO
            System.out.println("====================================================");
            System.out.println(e.getMessage());
            System.out.println("====================================================");
            failFast(cmdLineArgs);
        }

        //endregion


        if (cmd.hasOption(argumentsMatch.HELP.getId()))
            failFast(cmdLineArgs);


        //region OldLogic
        /**
         Integer messageTypeLoc = args.indexOf("-m");
         Integer fileOutLoc = args.indexOf("-o");

         Integer messageLoc = messageTypeLoc < fileOutLoc ? messageTypeLoc : fileOutLoc;

         Map<String, List<String>> source_dependencies =
         flow.retrieveInputsFromInput(
         args.subList(1, args.size()), messageLoc
         );

         Listing messageType = Listing.retrieveListingType(
         messageLoc != -1 && args.size() == messageLoc + 2
         ? args.get(messageLoc + 1) : null);

         info = new EnvironmentInformation(
         source_dependencies.get("source"),
         flow,
         messageType,
         source_dependencies.get("dependencies"),
         source_dependencies.get("sourcePaths"),
         source_dependencies.get("sourcePkg").get(0)
         );

         //TODO - temp step
         info.setPackageVersion("0");


         List<String> messagingArgs = args.subList(
         messageLoc != -1 ? messageLoc + 1 : args.size() - 1, args.size() - 1
         );

         if (!info.getMessagingType().getTypeOfMessagingInput().inputValidation(info, messagingArgs.toArray(new String[0]))) {
         failFast();
         return null;
         }
         **/
        //endregion

        return null;//info;

        /*} else {
            failFast();
            return null;
        }*/

    }

    /**
     * A universal method to return failure/help message
     */
    private static void failFast(Options args) {

        HelpFormatter helper = new HelpFormatter();
        helper.setOptionComparator(null);
        helper.printHelp("Rigorityj", args, false);
        System.exit(0);
    }

}

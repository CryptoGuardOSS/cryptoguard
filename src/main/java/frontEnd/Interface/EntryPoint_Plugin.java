package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugSummary;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import rule.engine.EngineType;
import util.Utils;

import java.util.List;
import java.util.function.Function;

/**
 * <p>EntryPoint_Plugin class.</p>
 *
 * @author maister
 * @version 03.10.00
 */
public class EntryPoint_Plugin {

    public static String main(List<String> sourceFiles, List<String> dependencies, String outFile, String mainFile) {
        return main(sourceFiles, dependencies, outFile, mainFile, null, 0);
    }

    public static String main(List<String> sourceFiles, List<String> dependencies, String outFile, String mainFile, int debuggingLevel) {
        return main(sourceFiles, dependencies, outFile, mainFile, null, debuggingLevel);
    }

    public static String main(List<String> sourceFiles, List<String> dependencies, String outFile, String mainFile, EnvironmentInformation info, int debuggingLevel) {

        Function<AnalysisIssue, String> errorAddition = analysisIssue -> "Adding the issue: " + analysisIssue.toString();
        Function<BugSummary, String> bugSummaryHandler = bugSummary -> {
            StringBuilder out = new StringBuilder();
            bugSummary.getSummaryContainer().forEach(out::append);
            return out.toString();
        };

        return main(sourceFiles, dependencies, outFile, mainFile, errorAddition, bugSummaryHandler, info, debuggingLevel);
    }

    public static String main(List<String> sourceFiles, List<String> dependencies, String outFile, String mainFile, Function<AnalysisIssue, String> errorAddition, Function<BugSummary, String> bugSummaryHandler, int debuggingLevel) {
        return main(sourceFiles, dependencies, outFile, mainFile, errorAddition, bugSummaryHandler, null, debuggingLevel);
    }

    public static String main(List<String> sourceFiles, List<String> dependencies, String outFile, String mainFile, Function<AnalysisIssue, String> errorAddition, Function<BugSummary, String> bugSummaryHandler, EnvironmentInformation info, int debuggingLevel) {
        try {
            info = ArgumentsCheck.paramaterCheck(
                    sourceFiles, dependencies,
                    EngineType.CLASSFILES, Listing.Default,
                    outFile, mainFile);

            info.setErrorAddition(errorAddition);
            info.setBugSummaryHandler(bugSummaryHandler);

            info.setPrettyPrint(true);
            info.setKillJVM(false);

            //region Setting the logging level
            switch (debuggingLevel) {
                case 6:
                    Configurator.setRootLevel(Level.ALL);
                    break;
                case 5:
                    Configurator.setRootLevel(Level.TRACE);
                    break;
                case 4:
                    Configurator.setRootLevel(Level.DEBUG);
                    break;
                case 3:
                    Configurator.setRootLevel(Level.INFO);
                    break;
                case 1:
                    Configurator.setRootLevel(Level.FATAL);
                    break;
                //case 2
                default:
                    Configurator.setRootLevel(Level.WARN);
                    break;
            }
            //endregion


            return SubRunner.run(info);
        } catch (ExceptionHandler e) {
            Utils.handleErrorMessage(e);
        }
        return null;
    }
}

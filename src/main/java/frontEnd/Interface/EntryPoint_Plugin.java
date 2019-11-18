package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.Heuristics;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import rule.engine.EngineType;
import util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * <p>EntryPoint_Plugin class.</p>
 *
 * @author maister
 * @version 03.10.00
 */
public class EntryPoint_Plugin {

    public static String main(List<String> sourceFiles, List<String> dependencies, String outFile, String mainFile, Function<AnalysisIssue, String> errorAddition, Function<HashMap<Integer, Integer>, String> bugSummaryHandler, Function<Heuristics, String> heuristicsHandler, int debuggingLevel) {
        String outputFile = null;
        try {
            EnvironmentInformation info = ArgumentsCheck.paramaterCheck(
                    sourceFiles, dependencies,
                    EngineType.CLASSFILES, Listing.Default,
                    outFile, mainFile);

            info.setErrorAddition(errorAddition);
            info.setBugSummaryHandler(bugSummaryHandler);
            info.setHeuristicsHandler(heuristicsHandler);

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


            outputFile = SubRunner.run(info);

        } catch (ExceptionHandler e) {
            Utils.handleErrorMessage(e);
            outputFile = e.toString();
        }
        return outputFile;
    }
}

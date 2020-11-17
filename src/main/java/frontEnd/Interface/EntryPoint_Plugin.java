/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.parameterChecks.Core;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.Heuristics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import rule.engine.EngineType;
import util.Utils;

import static util.Utils.setDebuggingLevel;

/**
 * EntryPoint_Plugin class.
 *
 * @author maister
 * @version 03.10.00
 */
public class EntryPoint_Plugin {

  /**
   * main.
   *
   * @param sourceFiles a {@link java.util.List} object.
   * @param dependencies a {@link java.util.List} object.
   * @param outFile a {@link java.lang.String} object.
   * @param mainFile a {@link java.lang.String} object.
   * @param errorAddition a {@link java.util.function.Function} object.
   * @param bugSummaryHandler a {@link java.util.function.Function} object.
   * @param heuristicsHandler a {@link java.util.function.Function} object.
   * @param debuggingLevel a int.
   * @return a {@link java.lang.String} object.
   * @param extraArgs a {@link java.util.List} object.
   */
  public static String main(
      List<String> sourceFiles,
      List<String> dependencies,
      String outFile,
      String mainFile,
      Function<AnalysisIssue, String> errorAddition,
      Function<HashMap<Integer, Integer>, String> bugSummaryHandler,
      Function<Heuristics, String> heuristicsHandler,
      int debuggingLevel,
      List<String> extraArgs) {
    String outputFile = null;

    //region Setting the logging level
    setDebuggingLevel(debuggingLevel);
    //endregion

    try {
      EnvironmentInformation info =
          Core.paramaterCheck(
              sourceFiles,
              dependencies,
              EngineType.CLASSFILES,
              Listing.Default,
              outFile,
              mainFile,
              extraArgs);

      info.setErrorAddition(errorAddition);
      info.setBugSummaryHandler(bugSummaryHandler);
      info.setHeuristicsHandler(heuristicsHandler);

      info.setPrettyPrint(true);
      info.setKillJVM(false);

      outputFile = SubRunner.run(info);

    } catch (ExceptionHandler e) {
      Utils.handleErrorMessage(e);
      outputFile = e.toString();
    }
    return outputFile;
  }
}

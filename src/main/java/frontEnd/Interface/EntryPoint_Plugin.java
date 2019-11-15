package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import rule.engine.EngineType;
import util.Utils;

import java.util.List;

/**
 * <p>EntryPoint_Plugin class.</p>
 *
 * @author maister
 * @version 03.10.00
 */
public class EntryPoint_Plugin {
    /**
     * <p>main.</p>
     *
     * @param sourceFiles  a {@link java.util.List} object.
     * @param dependencies a {@link java.util.List} object.
     * @param outFile      a {@link java.lang.String} object.
     * @param mainFile     a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String main(List<String> sourceFiles, List<String> dependencies, String outFile, String mainFile) {
        try {
            EnvironmentInformation info = ArgumentsCheck.paramaterCheck(
                    sourceFiles, dependencies,
                    EngineType.CLASSFILES, Listing.Default,
                    outFile, mainFile);

            info.setErrorAddition(analysisIssue -> "Adding the issue: " + analysisIssue.toString());
            info.setBugSummaryHandler(bugSummary -> {
                StringBuilder out = new StringBuilder();
                bugSummary.getSummaryContainer().forEach(out::append);
                return out.toString();
            });

            info.setPrettyPrint(true);
            info.setKillJVM(false);

            return SubRunner.run(info);
        } catch (ExceptionHandler e) {
            Utils.handleErrorMessage(e);
        }
        return null;
    }
}

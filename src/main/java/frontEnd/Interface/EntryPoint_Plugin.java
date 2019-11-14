package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import rule.engine.EngineType;
import util.Utils;

import java.util.List;

public class EntryPoint_Plugin {
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

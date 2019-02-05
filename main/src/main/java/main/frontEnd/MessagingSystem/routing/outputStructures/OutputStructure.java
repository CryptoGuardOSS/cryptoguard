package main.frontEnd.MessagingSystem.routing.outputStructures;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

import java.util.ArrayList;

/**
 * The general interface for the structure.
 * This will be implemented by the different type of messaging structures
 *
 * @author franceme
 * @version $Id: $Id
 * @since V01.00.00
 */
public interface OutputStructure {
    /**
     * <p>getOutput.</p>
     *
     * @param source      a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @param brokenRules a {@link java.util.ArrayList} object.
     * @return a {@link java.lang.String} object.
     */
    String getOutput(EnvironmentInformation source, ArrayList<AnalysisIssue> brokenRules);
}

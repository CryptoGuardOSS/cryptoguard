package main.frontEnd.MessagingSystem;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The interface for the different types of output used for the library.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @since V01.00.01
 */
public class MessageRepresentation {

    //region Attributes
    private EnvironmentInformation env;
    private Queue analysisIssues;
    //endregion

    //region Constructor

    /**
     * The construction of the Message Represention, containing all of the pertinent information.
     * <p>Used to funnel all of the output.</p>
     * Also creates a buffer to rewrite the system out internally.
     *
     * @param source the name of the source being examined
     */
    public MessageRepresentation(EnvironmentInformation source) {
        this.env = source;
        this.analysisIssues = new LinkedList<>();
    }
    //endregion

    //region Getters/Setters/Adders

    /**
     * The getter for the source
     *
     * @return EnvironmentInformation - the source
     */
    public EnvironmentInformation getEnvironment() {
        return env;
    }

    /**
     * The getter for the Analysis Issues
     *
     * @return AnalysisRules - the list of broken rules
     */
    public ArrayList<AnalysisIssue> getAnalysisIssues() {
        return new ArrayList<>(analysisIssues);
    }


    /**
     * <p>addAnalysis.</p>
     *
     * @param issue a {@link AnalysisIssue} object.
     */
    public void addAnalysis(AnalysisIssue issue) {
        this.analysisIssues.add(issue);
    }

    /**
     * The method to get the structure of the output.
     * Also re-opens the console output.
     *
     * @return String - the string output is determined by the type of messaging system used
     */
    public String getMessage() {
        return this.env.getMessagingType().getTypeOfMessagingOutput().getOutput(this.env, this.getAnalysisIssues());
    }
    //endregion
}

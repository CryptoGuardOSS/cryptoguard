package main.frontEnd;

import main.rule.engine.EngineType;

import java.util.ArrayList;

/**
 * The interface for the different types of output used for the library.
 * <p>STATUS: IC</p>
 * @author RigorityJTeam
 * @since 1.0
 */
public abstract class MessageRepresentation {

    private String Source;
    private EngineType type;
    private ArrayList<AnalysisRule> analysisIssues;

    /**
     * The construction of the Message Represention, containing all of the pertinent information.
     * <p>Used to funnel all of the output.</p>
     *
     * @param type the type of engine to be used for the processing
     * @param source the name of the source being examined
     */
    MessageRepresentation(String source, EngineType type)
    {
        this.Source = source;
        this.type = type;
        this.analysisIssues = new ArrayList<>();
    };

    /**
     * The method allowing the output message to be changed by each varying implementation
     */
    public abstract Object getOutput();

    /**
     * A simple method to allow additional rule breaks to be added into the output.
     *
     * @param ruleInfo the information about the rule being broken
     */
    public void addRuleAnalysis(AnalysisRule ruleInfo)
    {
        this.analysisIssues.add(ruleInfo);
    }

    public String getSource() {
        return Source;
    }

    public EngineType getType() {
        return type;
    }

    public ArrayList<AnalysisRule> getAnalysisIssues() {
        return analysisIssues;
    }
}

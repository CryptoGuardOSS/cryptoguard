package main.frontEnd;

import main.frontEnd.outputStructures.Listing;
import main.rule.engine.EngineType;
import main.rule.engine.RuleList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * The interface for the different types of output used for the library.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class MessageRepresentation
{

	//region Attributes
	private String Source;
	private EngineType type;
	private ArrayList<AnalysisRule> analysisIssues;
	private PrintStream internalMessages;
	private OutputStructure messageEngine;
	//endregion

	//region Constructor
	/**
	 * The construction of the Message Represention, containing all of the pertinent information.
	 * <p>Used to funnel all of the output.</p>
	 * Also creates a buffer to rewrite the system out internally.
	 *
	 * @param type                     the type of engine to be used for the processing
	 * @param source                   the name of the source being examined
	 * @param typeOfMessagingStructure the flag used to determine the type of messaging structure to be used
	 */
	public MessageRepresentation(String source, EngineType type, String typeOfMessagingStructure)
	{
		this.Source = source;
		this.type = type;
		this.messageEngine = Listing.getTypeOfMessaging(typeOfMessagingStructure);
		this.internalMessages = new PrintStream(new ByteArrayOutputStream());
		System.setOut(this.internalMessages);
		this.analysisIssues = new ArrayList<>();

		for (RuleList rule : RuleList.values())
			this.analysisIssues.add(new AnalysisRule(rule.getRuleId()));
	}
	//endregion

	//region Getters/Setters/Adders

	/**
	 * The getter for the source
	 *
	 * @return string - the source
	 */
	public String getSource()
	{
		return Source;
	}

	/**
	 * The getter for the type
	 *
	 * @return EngineType - the type of engine running
	 */
	public EngineType getType()
	{
		return type;
	}

	/**
	 * The getter for the Analysis Issues
	 *
	 * @return AnalysisRules - the list of broken rules
	 */
	public ArrayList<AnalysisRule> getAnalysisIssues()
	{
		return analysisIssues;
	}

	/**
	 * The getter for the Messaging Engine
	 *
	 * @return OutputStructure - the messaging engine being used
	 */
	public OutputStructure getMessageEngine()
	{
		return messageEngine;
	}

	/**
	 * A simple method to allow additional rule breaks to be added into the output.
	 * This method will add a single Analysis Rule
	 *
	 * @param ruleNumber - the rule number to add the issue to
	 * @param issue - the specific issue being added
	 */
	public void addRuleAnalysis(Integer ruleNumber, AnalysisIssue issue)
	{
		this.analysisIssues.get(ruleNumber-1).addIssue(issue);

	}

	/**
	 * A simple overloaded method to add an arraylist of rule breaks into the output.
	 *
	 *
	 *    @param ruleNumber - the rule number to add the issue to
	 * 	@param issues - the specific issues being added
	 */
	public void addRuleAnalysis(Integer ruleNumber, ArrayList<AnalysisIssue> issues)
	{
		this.analysisIssues.get(ruleNumber-1).addIssue(issues);
	}

	/**
	 * The method to get the structure of the output.
	 *
	 * @return Object - the overloaded output is determined by the type of messaging system used
	 */
	public Object getMessage()
	{
		return messageEngine.getOutput(this.Source, this.type, this.analysisIssues, this.internalMessages);
	}
	//endregion
}

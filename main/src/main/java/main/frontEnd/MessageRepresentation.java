package main.frontEnd;

import main.frontEnd.outputStructures.Listing;
import main.rule.engine.EngineType;

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
	private OutputStructure messageEngine;//Defined as such for now
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
	MessageRepresentation(String source, EngineType type, String typeOfMessagingStructure)
	{
		this.Source = source;
		this.type = type;
		this.messageEngine = Listing.getTypeOfMessaging(typeOfMessagingStructure);
		this.internalMessages = new PrintStream(new ByteArrayOutputStream());
		System.setOut(this.internalMessages);
		this.analysisIssues = new ArrayList<>();
	}
	//endregion

	//region Getters/Setters/Adders
	/**
	 * A simple method to allow additional rule breaks to be added into the output.
	 * This method will add a single Analysis Rule
	 *
	 * @param ruleInfo the information about the rule being broken
	 */
	public void addRuleAnalysis(AnalysisRule ruleInfo)
	{
		this.analysisIssues.add(ruleInfo);
	}

	/**
	 * A simple overloaded method to add an arraylist of rule breaks into the output.
	 *
	 * @param ruleInfo the array list of the information about the rule being broken
	 */
	public void addRuleAnalysis(ArrayList<AnalysisRule> ruleInfo)
	{
		this.analysisIssues.addAll(ruleInfo);
	}

	/**
	 * The method to get the structure of the output.
	 *
	 * @return Object - the overloaded output is determined by the type of messaging system used
	 */
	public Object getMessage()
	{
		return messageEngine.getOutput(Source, type, analysisIssues, internalMessages);
	}
	//endregion
}

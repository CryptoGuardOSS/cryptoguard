package main.frontEnd;

import main.frontEnd.outputStructures.Listing;
import main.rule.engine.EngineType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The interface for the different types of output used for the library.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @since 01.01
 */
public class MessageRepresentation {

	//region Attributes
	private EnvironmentInformation env;
	private EngineType type;
	private Queue analysisIssues;
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
	 * @param typeOfMessagingStructure the flag used to determine the type of messaging structure to be used
	 * @param source                   the name of the source being examined
	 * @param typeOfMessagingStructure the flag used to determine the type of messaging structure to be used
	 */
	public MessageRepresentation(EnvironmentInformation source, EngineType type, String typeOfMessagingStructure) {
		this.env = source;
		this.type = type;
		this.messageEngine = Listing.getTypeOfMessaging(typeOfMessagingStructure);
		this.internalMessages = new PrintStream(new ByteArrayOutputStream());
		System.setOut(this.internalMessages);
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
	 * The getter for the type
	 *
	 * @return EngineType - the type of engine running
	 */
	public EngineType getType() {
		return type;
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
	 * <p>Getter for the field <code>messageEngine</code>.</p>
	 *
	 * @return a {@link main.frontEnd.OutputStructure} object.
	 */
	public OutputStructure getMessageEngine() {
		return messageEngine;
	}


	/**
	 * <p>addAnalysis.</p>
	 *
	 * @param issue a {@link main.frontEnd.AnalysisIssue} object.
	 */
	public void addAnalysis(AnalysisIssue issue) {
		this.analysisIssues.add(issue);
	}

	/**
	 * The method to get the structure of the output.
	 *
	 * @return String - the string output is determined by the type of messaging system used
	 */
	public String getMessage() {
		return messageEngine.getOutput(this.env, this.type, this.getAnalysisIssues(), this.internalMessages);
	}
	//endregion
}

package main.frontEnd;

import java.util.ArrayList;

/**
 * The class containing the analysis rule information.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class AnalysisRule
{

	//region Attributes
	private Integer ruleNumber;
	private String ruleType;
	private String message;
	private ArrayList<AnalysisIssue> issues;
	//endregion

	//region Constructor

	/**
	 * The constructor for the Analysis Rule
	 * Will set all of the necessary parameters to be returned to the user as well as instantiate a new list of issues
	 *
	 * @param ruleNumber - the rule number that was broken
	 * @param ruleType   - the type of rule that was broken
	 * @param message    - the message about the broken rule
	 */
	public AnalysisRule(Integer ruleNumber, String ruleType, String message)
	{
		this.ruleNumber = ruleNumber;
		this.ruleType = ruleType;
		this.message = message;
		this.issues = new ArrayList<>();
	}
	//endregion

	//region Getters/Setters/Adders

	/**
	 * The getter for the line number
	 *
	 * @return Integer - the number of the specified rule
	 */
	public Integer getRuleNumber()
	{
		return ruleNumber;
	}

	/**
	 * The getter for the type of rule
	 *
	 * @return Integer - the type of rule
	 */
	public String getRuleType()
	{
		return ruleType;
	}

	/**
	 * The getter for the message
	 *
	 * @return String - the message associated with the rule break
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * The getter for the list of issues
	 *
	 * @return ArrayList Issue - the list of issues
	 */
	public ArrayList<AnalysisIssue> getIssues()
	{
		return issues;
	}

	/**
	 * The adder for the list of issues
	 *
	 * @param issue - an notice of the location of the broken rule
	 */
	public void addIssue(AnalysisIssue issue)
	{
		this.issues.add(issue);
	}
	//endregion
}

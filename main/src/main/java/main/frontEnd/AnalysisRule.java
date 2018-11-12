package main.frontEnd;

import main.rule.engine.RuleList;

import java.util.ArrayList;
import java.util.Arrays;

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
	private String message;
	private ArrayList<AnalysisIssue> issues;
	//endregion

	//region Constructor

	/**
	 * A constructor for the Analysis Rule
	 * Will set all of the necessary parameters to be returned to the user as well as instantiate a new list of issues
	 *
	 * @param ruleNumber - the rule number that was broken
	 */
	public AnalysisRule(Integer ruleNumber)
	{
		this.ruleNumber = ruleNumber;
		this.issues = new ArrayList<>();
	}

	/**
	 * A constructor for the Analysis Rule
	 * Will set all of the necessary parameters to be returned to the user as well as instantiate a new list of issues
	 *
	 * @param ruleNumber - the rule number that was broken
	 * @param issues     - the issues being dynamically added
	 */
	public AnalysisRule(Integer ruleNumber, AnalysisIssue... issues)
	{
		this.ruleNumber = ruleNumber;
		this.issues = new ArrayList<>(Arrays.asList(issues));
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
		return RuleList.getDescByRuleNumber(this.ruleNumber);
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

	/**
	 * The adder for the list of issues
	 *
	 * @param issues - a list of notices of the location of the broken rule
	 */
	public void addIssue(ArrayList<AnalysisIssue> issues)
	{
		this.issues.addAll(issues);
	}
	//endregion
}

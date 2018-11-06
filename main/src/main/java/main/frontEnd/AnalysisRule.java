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

	private Integer ruleNumber;
	private String ruleType;
	private String message;
	private ArrayList<AnalysisIssue> issues;

	public AnalysisRule(Integer ruleNumber, String ruleType, String message)
	{
		this.ruleNumber = ruleNumber;
		this.ruleType = ruleType;
		this.message = message;
		this.issues = new ArrayList<>();
	}

	public Integer getRuleNumber()
	{
		return ruleNumber;
	}

	public String getRuleType()
	{
		return ruleType;
	}

	public String getMessage()
	{
		return message;
	}

	public ArrayList<AnalysisIssue> getIssues()
	{
		return issues;
	}

	public void addIssue(AnalysisIssue issue)
	{
		this.issues.add(issue);
	}
}

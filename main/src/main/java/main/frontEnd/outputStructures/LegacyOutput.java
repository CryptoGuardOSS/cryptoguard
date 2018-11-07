package main.frontEnd.outputStructures;

import main.frontEnd.AnalysisIssue;
import main.frontEnd.AnalysisRule;
import main.frontEnd.OutputStructure;
import main.rule.engine.EngineType;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * The class containing the implementation of the legacy output.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class LegacyOutput implements OutputStructure
{
	public final Listing typeOfStructure = Listing.LegacyOutput;

	/***
	 *  The overridden method for the Legacy output. Currently mimics the output as best seen.
	 *
	 * @return Object nothing is returned in legacy as legacy only prints
	 * information out to the console
	 */
	public Object getOutput(String source, EngineType type, ArrayList<AnalysisRule> brokenRules, PrintStream internalWarnings)
	{
		System.out.println("Type of messaging structure being used: " + this.typeOfStructure);
		System.out.println("Internal Warnings: " + internalWarnings.toString());
		System.out.println("Analyzing " + type + ": " + source);
		for (AnalysisRule rule : brokenRules)
		{
			System.out.println("=======================================");
			System.out.println("***Violated Rule " + rule.getRuleNumber() + ": " + rule.getRuleType());
			if (rule.getIssues().size() > 0)
			{
				for (AnalysisIssue issue : rule.getIssues())
				{
					StringBuilder message = new StringBuilder("***Found: ");

					message.append("[" + issue.getCapturedInformation() + "] ");
					if (issue.getLineNumber() != null)
					{
						message.append("in line " + issue.getLineNumber() + " ");
					}
					message.append("in Method: " + issue.getMethod());

					System.out.println(message.toString());
				}
			}
			else
			{
				System.out.println("***" + rule.getMessage());
			}
			System.out.println("=======================================");
		}
		return null;
	}
}

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
		StringBuilder output = new StringBuilder();

		//Only printing console output if it is set and there is output captured
		if (internalWarnings != null && internalWarnings.toString().split("\n").length > 1)
		{
			output.append("Internal Warnings: " + internalWarnings.toString() + "\n");
		}

		output.append("Analyzing " + type + ": " + source + "\n");

		//region Broken Rule Cycle
		for (AnalysisRule rule : brokenRules)
		{
			output.append("=======================================\n");
			output.append("***Violated Rule " + rule.getRuleNumber() + ": " + rule.getRuleType() + "\n");
			//region Specific Rule Broken
			if (rule.getIssues().size() > 0)
			{
				for (AnalysisIssue issue : rule.getIssues())
				{
					String outputMessage;
					//region For no general cause message
					if (issue.getCauseMessage() == null)
					{
						//region Describing A Method Location
						if (issue.getDescribingMethod())
						{
							StringBuilder message = new StringBuilder("***Found: ");

							message.append("[\"" + issue.getCapturedInformation() + "\"] ");

							if (issue.getLineNumber() != null)
							{
								message.append("in Line " + issue.getLineNumber() + " ");
							}

							message.append("in Method: " + issue.getLocationName());

							outputMessage = message.toString();
						}
						//endregion
						//region Describing Class Location
						else
						{
							StringBuilder message = new StringBuilder("***");
							message.append(issue.getCapturedInformation());
							message.append(issue.getLocationName());

							outputMessage = message.toString();
						}
						//endregion
					}
					//endregion
					//region Describing a general cause message
					else
					{
						StringBuilder message = new StringBuilder("***Cause: ");
						message.append(issue.getCauseMessage());

						outputMessage = message.toString();
					}
					//endregion
					output.append(outputMessage + "\n");
				}
			}
			//endregion
			//region General Message
			else
			{
				output.append("***" + rule.getMessage() + "\n");
			}
			//endregion
			output.append("=======================================\n");
		}
		//endregion

		return output.toString();
	}
}

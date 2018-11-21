package main.frontEnd.outputStructures;

import com.example.response.*;
import main.frontEnd.AnalysisIssue;
import main.frontEnd.AnalysisLocation;
import main.frontEnd.EnvironmentInformation;
import main.frontEnd.OutputStructure;
import main.rule.engine.EngineType;
import main.rule.engine.RuleList;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class containing the implementation of the Scarf XML output.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class ScarfXMLOutput implements OutputStructure {
	public final Listing typeOfStructure = Listing.LegacyOutput;

	/***
	 *  The overridden method for the Scarf XML output.
	 *
	 * @return Object The raw stream of the xml
	 */
	public String getOutput(EnvironmentInformation source, EngineType type, ArrayList<AnalysisIssue> brokenRules, PrintStream internalWarnings) {
		try {
			AnalyzerReport report = new AnalyzerReport();

			//region Setting the report for marshalling
			//region Setting Attributes
			report.setAssessFw(source.getAssessmentFramework());
			report.setAssessFwVersion(source.getAssessmentFrameworkVersion());
			report.setAssessmentStartTs(source.getStartTimeStamp());
			report.setBuildFw(source.getBuildFramework());
			report.setBuildFwVersion(source.getBuildFrameworkVersion());
			report.setPackageName(source.getPackageName());
			report.setPackageVersion(source.getPackageVersion());
			report.setPackageRootDir(source.getPackageRootDir());
			report.setParserFw(source.getParserName());
			report.setParserFwVersion(source.getParserVersion());
			report.setPlatformName(source.getPlatformName());
			report.setToolName(source.getToolFramework());
			report.setToolVersion(source.getToolFrameworkVersion());
			report.setUuid(source.getUUID());
			report.setBuildRootDir(source.getBuildRootDir());
			//endregion

			HashMap<Integer, Integer> countOfBugs = new HashMap<Integer, Integer>();

			//region Creating Bug Instances
			for (int ktr = 0; ktr < brokenRules.size(); ktr++) {
				BugInstanceType instance = new BugInstanceType();
				AnalysisIssue issue = brokenRules.get(ktr);

				if (!countOfBugs.containsKey(issue.getRuleId())) {
					countOfBugs.put(issue.getRuleId(), 1);
				}
				else {
					countOfBugs.put(issue.getRuleId(), countOfBugs.get(issue.getRuleId()) + 1);
				}

				instance.setId(ktr);
				instance.setBugCode(issue.getRuleId().toString());
				instance.setBugGroup(issue.getRule().getDesc());
				instance.setBugMessage(issue.getRule().getDesc());

				BugTraceType trace = new BugTraceType();
				trace.setBuildId(source.getBuildId());
				trace.setAssessmentReportFile(source.getxPath());
				instance.setBugTrace(trace);

				if (StringUtils.isNotBlank(issue.getClassName())) {
					instance.setClassName(issue.getClassName());
				}

				//region Setting Methods If there are any, currently the first is the primary one
				if (!issue.getMethods().isEmpty()) {
					MethodsType methods = new MethodsType();
					for (int methodKtr = 0; methodKtr < issue.getMethods().size(); methodKtr++) {
						MethodType newMethod = new MethodType();

						newMethod.setId(methodKtr);
						newMethod.setPrimary(methodKtr == 0);
						newMethod.setValue((String) issue.getMethods().get(methodKtr));

						methods.getMethod().add(newMethod);
					}
					instance.setMethods(methods);
				}
				//endregion

				//region Setting Bug Locations
				BugLocationsType bugLocations = new BugLocationsType();
				if (!issue.getLocations().isEmpty())
					for (int locationKtr = 0; locationKtr < issue.getLocations().size(); locationKtr++) {
						LocationType newLocation = new LocationType();
						AnalysisLocation createdLoc = issue.getLocations().get(locationKtr);

						newLocation.setId(locationKtr);
						newLocation.setPrimary(locationKtr == 0);
						newLocation.setStartLine(createdLoc.getLineStart());
						newLocation.setSourceFile(issue.getFullPathName());

						if (createdLoc.getLineEnd() > 0 && !createdLoc.getLineEnd().equals(createdLoc.getLineStart())) {
							newLocation.setEndLine(createdLoc.getLineEnd());
						}

						bugLocations.getLocation().add(newLocation);
					}
				else {
					LocationType newLocation = new LocationType();
					newLocation.setSourceFile(issue.getFullPathName());
					bugLocations.getLocation().add(newLocation);
				}
				instance.setBugLocations(bugLocations);
				//endregion

				String outputMessage = issue.getIssueCause() + " " + issue.getIssueInformation();
				instance.setBugMessage(StringUtils.trimToNull(outputMessage));

				report.getBugInstance().add(instance);
			}
			//endregion

			report.setBugSummary(createBugSummary(countOfBugs));


			//Creating Marshaller for the Analyzer Report
			JAXBContext context = JAXBContext.newInstance(AnalyzerReport.class);
			Marshaller marshaller = context.createMarshaller();

			//Settings Properties of the Marshaller
			if (source.prettyPrint()) {
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			}

			//Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			//From the marshaller output
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

			//Creating the Stream for the marshalling and marshalling
			ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
			marshaller.marshal(report, new PrintStream(xmlStream));

			return xmlStream.toString();
		} catch (PropertyException e) {
			return creatingErrorMessage("There has been an issue setting properties.");
		} catch (JAXBException e) {
			return creatingErrorMessage("There has been an issue marshalling the output.");
		}
	}

	public BugSummaryType createBugSummary(HashMap<Integer, Integer> brokenRuleCount) {
		BugSummaryType bugDict = new BugSummaryType();

		//region Creating A Bug Category with counts per the Broken Rules
		for (int ruleNumber : brokenRuleCount.keySet()) {
			BugCategoryType ruleType = new BugCategoryType();

			ruleType.setGroup(RuleList.getRuleByRuleNumber(ruleNumber).getDesc());
			ruleType.setCode(String.valueOf(ruleNumber));
			ruleType.setCount(brokenRuleCount.get(ruleNumber));

			bugDict.getBugCategory().add(ruleType);
		}
		//endregion

		return bugDict;
	}

	/**
	 * The method for this output to create error messages
	 *
	 * @param message -  the string of the message to be sent to the output
	 * @return string - the xml format of the error message
	 */
	private String creatingErrorMessage(String message) {
		StringBuilder output = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		output.append("<ERROR>");
		output.append(message);
		output.append("</ERROR>");
		return output.toString();
	}
}

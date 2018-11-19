package main.frontEnd;

import main.rule.engine.RuleList;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The class containing the specific analysis issue information.
 * The "flat" structure to be transformed to various formats.
 *
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class AnalysisIssue {

	//region Attributes
	private String fullPathName = "";
	private String className = "";
	private RuleList rule;
	private Stack methods;
	private ArrayList<AnalysisLocation> locations;
	private String issueInformation = "";
	private String issueCause = "";
	//endregion

	//region Constructors
	public AnalysisIssue(String className, Integer ruleNumber, String information) {
		this.fullPathName = className;
		this.className = className;
		this.issueInformation = information;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}

	public AnalysisIssue(Integer ruleNumber, String methodName, String information) {
		this.getMethods().push(methodName);
		this.issueInformation = information;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}

	public AnalysisIssue(Integer ruleNumber, String methodName, String information, AnalysisLocation location) {
		this.addMethod(methodName, location);
		this.issueInformation = information;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}

	public AnalysisIssue(Integer ruleNumber, String cause) {
		this.issueCause = cause;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}
	//endregion

	//region Getters/Setters
	public void setFullPathName(String fullPathName) {
		this.fullPathName = fullPathName;
	}


	public String getFullPathName() {
		return fullPathName;
	}

	public String getClassName() {
		return className;
	}

	public RuleList getRule() {
		return rule;
	}

	public Integer getRuleId() {
		return rule.getRuleId();
	}

	public Stack getMethods() {

		if (this.methods == null) {
			this.methods = new Stack();
		}

		return this.methods;
	}

	public ArrayList<AnalysisLocation> getLocations() {

		if (this.locations == null) {
			this.locations = new ArrayList<AnalysisLocation>();
		}

		return locations;
	}

	public void addLocation(AnalysisLocation newLocation) {
		this.getLocations().add(newLocation);
	}

	public void addMethod(String methodName) {
		this.getMethods().push(String.valueOf(methodName));
	}

	public void addMethod(String methodName, AnalysisLocation location) {
		location.setMethodNumber(this.getMethods().size());
		this.getMethods().push(String.valueOf(methodName));

		this.addLocation(location);
	}

	public String getIssueInformation() {
		return issueInformation;
	}

	public String getIssueCause() {
		return issueCause;
	}
	//endregion
}

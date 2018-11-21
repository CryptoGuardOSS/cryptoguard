package main.frontEnd;

import main.rule.engine.Criteria;
import main.rule.engine.RuleList;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The class containing the specific analysis issue information.
 * The "flat" structure to be transformed to various formats.
 *
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @since V01.00.01
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

	/**
	 * <p>Constructor for AnalysisIssue.</p>
	 *
	 * @param bugLocationInformation a {@link main.rule.engine.Criteria} object.
	 * @param information            a {@link java.lang.String} object.
	 * @param ruleNumber             a {@link java.lang.Integer} object.
	 */
	public AnalysisIssue(Criteria bugLocationInformation, String information, Integer ruleNumber) {
		this.fullPathName = bugLocationInformation.getClassName();
		this.className = bugLocationInformation.getClassName();
		this.addMethod(bugLocationInformation.getMethodName());
		this.issueInformation = information;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}

	/**
	 * <p>Constructor for AnalysisIssue.</p>
	 *
	 * @param className   a {@link java.lang.String} object.
	 * @param ruleNumber  a {@link java.lang.Integer} object.
	 * @param information a {@link java.lang.String} object.
	 */
	public AnalysisIssue(String className, Integer ruleNumber, String information) {
		this.fullPathName = className;
		this.className = className;
		this.issueInformation = information;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}

	/**
	 * <p>Constructor for AnalysisIssue.</p>
	 *
	 * @param ruleNumber  a {@link java.lang.Integer} object.
	 * @param methodName  a {@link java.lang.String} object.
	 * @param information a {@link java.lang.String} object.
	 */
	public AnalysisIssue(Integer ruleNumber, String methodName, String information) {
		this.fullPathName = "Unknown";
		this.getMethods().push(methodName);
		this.issueInformation = information;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}

	/**
	 * <p>Constructor for AnalysisIssue.</p>
	 *
	 * @param ruleNumber  a {@link java.lang.Integer} object.
	 * @param methodName  a {@link java.lang.String} object.
	 * @param information a {@link java.lang.String} object.
	 * @param location    a {@link main.frontEnd.AnalysisLocation} object.
	 */
	public AnalysisIssue(Integer ruleNumber, String methodName, String information, AnalysisLocation location) {
		this.fullPathName = "Unknown";
		this.addMethod(methodName, location);
		this.issueInformation = information;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}

	/**
	 * <p>Constructor for AnalysisIssue.</p>
	 *
	 * @param ruleNumber a {@link java.lang.Integer} object.
	 * @param cause      a {@link java.lang.String} object.
	 */
	public AnalysisIssue(Integer ruleNumber, String cause) {
		this.fullPathName = "Unknown";
		this.issueCause = cause;
		this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
	}
	//endregion

	//region Getters/Setters

	/**
	 * <p>Setter for the field <code>fullPathName</code>.</p>
	 *
	 * @param fullPathName a {@link java.lang.String} object.
	 */
	public void setFullPathName(String fullPathName) {
		this.fullPathName = fullPathName;
	}


	/**
	 * <p>Getter for the field <code>fullPathName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFullPathName() {
		return fullPathName;
	}

	/**
	 * <p>Getter for the field <code>className</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * <p>Getter for the field <code>rule</code>.</p>
	 *
	 * @return a {@link main.rule.engine.RuleList} object.
	 */
	public RuleList getRule() {
		return rule;
	}

	/**
	 * <p>getRuleId.</p>
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getRuleId() {
		return rule.getRuleId();
	}

	/**
	 * <p>Getter for the field <code>methods</code>.</p>
	 *
	 * @return a {@link java.util.Stack} object.
	 */
	public Stack getMethods() {

		if (this.methods == null) {
			this.methods = new Stack();
		}

		return this.methods;
	}

	/**
	 * <p>Getter for the field <code>locations</code>.</p>
	 *
	 * @return a {@link java.util.ArrayList} object.
	 */
	public ArrayList<AnalysisLocation> getLocations() {

		if (this.locations == null) {
			this.locations = new ArrayList<AnalysisLocation>();
		}

		return locations;
	}

	/**
	 * <p>addLocation.</p>
	 *
	 * @param newLocation a {@link main.frontEnd.AnalysisLocation} object.
	 */
	public void addLocation(AnalysisLocation newLocation) {
		this.getLocations().add(newLocation);
	}

	/**
	 * <p>addMethod.</p>
	 *
	 * @param methodName a {@link java.lang.String} object.
	 */
	public void addMethod(String methodName) {
		this.getMethods().push(String.valueOf(methodName));
	}

	/**
	 * <p>addMethod.</p>
	 *
	 * @param methodName a {@link java.lang.String} object.
	 * @param location   a {@link main.frontEnd.AnalysisLocation} object.
	 */
	public void addMethod(String methodName, AnalysisLocation location) {
		location.setMethodNumber(this.getMethods().size());
		this.getMethods().push(String.valueOf(methodName));

		this.addLocation(location);
	}

	/**
	 * <p>Getter for the field <code>issueInformation</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getIssueInformation() {
		return issueInformation;
	}

	/**
	 * <p>Getter for the field <code>issueCause</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getIssueCause() {
		return issueCause;
	}
	//endregion
}

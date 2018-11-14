package main.frontEnd;

/**
 * The class containing the specific analysis issue information.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class AnalysisIssue {

	//region Attributes
	private Integer lineNumber;
	private String locationName;
	private String capturedInformation;
	private String causeMessage;
	private Boolean describingMethod;
	//endregion

	//region Constructors

	/**
	 * The issue that was caused by a more generic reason instead of a specific line.
	 * <p>Replicates the structure: ***Cause: ...</p>
	 *
	 * @param causeMessage a message explaining the reason why the rule was broken
	 */
	public AnalysisIssue(String causeMessage) {
		this.causeMessage = causeMessage;
	}

	/**
	 * The issue that was caused by a specific reason (a specific string).
	 * <p>Replicates the structure: ***Found: ["\s"] [in Line \d] in Method: \s</p>
	 *
	 * @param capturedInformation the message containing the string that broke the rule
	 * @param lineNumber          the line number within the locationName the string is located at
	 * @param locationName        the locationName where the string is
	 */
	public AnalysisIssue(String locationName, Integer lineNumber, String capturedInformation) {
		this.locationName = locationName;
		this.lineNumber = lineNumber;
		this.capturedInformation = capturedInformation;
		this.describingMethod = true;
	}

	/**
	 * The issue that was caused by a specific reason (a specific string).
	 * <p>Replicates the structure: ***Found: ["\s"] [in Line \d] in Method: \s</p>
	 *
	 * @param capturedInformation the message containing the string that broke the rule
	 * @param locationName        the locationName where the string is
	 * @param describingMethod    the boolean to indicate whether or not the issue is describing a method or class
	 */
	public AnalysisIssue(String locationName, String capturedInformation, boolean describingMethod) {
		this.locationName = locationName;
		this.capturedInformation = capturedInformation;
		this.describingMethod = describingMethod;
	}
	//endregion

	//region Getters

	/**
	 * The getter for the Line Number
	 *
	 * @return Integer - the line number of the rule break
	 */
	public Integer getLineNumber() {
		return lineNumber;
	}

	/**
	 * The getter for the locationName name
	 *
	 * @return string - the name of the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * The getter for the Captured Information
	 *
	 * @return string - returns any error information
	 */
	public String getCapturedInformation() {
		return capturedInformation;
	}

	/**
	 * The getter for the cause message
	 *
	 * @return string - a generalized String set for a common error message, a less detailed error message
	 */
	public String getCauseMessage() {
		return causeMessage;
	}


	/**
	 * The getter for the Message First Boolean
	 *
	 * @return boolean - the indicator whether or not the message is describing a method or not
	 */
	public Boolean getDescribingMethod() {
		return describingMethod;
	}
	//endregion
}

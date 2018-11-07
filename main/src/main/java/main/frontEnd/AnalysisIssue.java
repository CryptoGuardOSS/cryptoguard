package main.frontEnd;

/**
 * The class containing the specific analysis issue information.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class AnalysisIssue
{

	//region Attributes
	private Integer lineNumber;
    private String method;
    private String capturedInformation;
    private String causeMessage;
	//endregion

	//region Constructors
    /**
     * The issue that was caused by a more generic reason instead of a specific line.
     * <p>Replicates the structure: ***Cause: ...</p>
     *
     * @param causeMessage a message explaining the reason why the rule was broken
     */
    public AnalysisIssue(String causeMessage)
    {
        this.causeMessage = causeMessage;
    }

    /**
     * The issue that was caused by a specific reason (a specific string).
     * <p>Replicates the structure: ***Found: ["\s"] [in Line \d] in Method: \s</p>
     *
     * @param capturedInformation the message containing the string that broke the rule
     * @param lineNumber          the line number within the method the string is located at
     * @param method              the method where the string is
     */
    public AnalysisIssue(String method, Integer lineNumber, String capturedInformation)
    {
        this.method = method;
        this.lineNumber = lineNumber;
        this.capturedInformation = capturedInformation;
    }
	//endregion

	//region Getters

	/**
	 * The getter for the Line Number
	 *
	 * @return Integer - the line number of the rule break
	 */
	public Integer getLineNumber()
	{
		return lineNumber;
	}

	/**
	 * The getter for the method name
	 *
	 * @return string - the name of the method
	 */
	public String getMethod()
    {
        return method;
	}

	/**
	 * The getter for the Captured Information
	 *
	 * @return string - returns any error information
	 */
	public String getCapturedInformation()
    {
        return capturedInformation;
	}

	/**
	 * The getter for the cause message
	 *
	 * @return string - a generalized String set for a common error message, a less detailed error message
	 */
	public String getCauseMessage()
	{
		return causeMessage;
	}
	//endregion
}

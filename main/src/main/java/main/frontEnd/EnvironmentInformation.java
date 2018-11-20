package main.frontEnd;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;

/**
 * The class containing the analysis rule information.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class EnvironmentInformation {

	//region Attributes
	//region Self Generated
	private final Properties Properties = new Properties();
	private final String PropertiesFile = "gradle.properties";
	private final String ToolFramework;
	private final String ToolFrameworkVersion;
	private final XMLGregorianCalendar startTimeStamp;
	private final String BuildFramework;
	private final String BuildFrameworkVersion;
	private final String platformName = System.getProperty("os.name") + "-" + System.getProperty("os.version");
	private final String Source;
	private final Boolean prettyPrint;
	private String packageName;
	private String packageVersion;
	//endregion
	//region From Outside
	private final String AssessmentFramework;
	private final String AssessmentFrameworkVersion;
	private final String AssessmentStartTime;

	private final String ParserName;
	private final String ParserVersion;

	private final String UUID;

	//TODO
	private final String packageRootDir;
	private final Integer buildId;
	private final String xPath;
	//endregion

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	//endregion


	/**
	 * The main constructor for setting all of the environmental variables used  for the outputs.
	 *
	 * @param source                     - String the source file name
	 * @param assessmentFramework        - String, can't be null, the name of the tool calling this library
	 * @param assessmentFrameworkVersion - String, can't be null, the version of the tool calling this library
	 * @param assessmentStartTime        - String, can't be null, the timestamp the tool calling started it's processing
	 * @param parserName                 - String, can't be null, the name of the parser being used
	 * @param parserVersion              - String, can't be null, the version of the parser being used
	 * @param givenUUID                  - String, the unique ID for this particular validation, if null a sudo number will be generated
	 */
	public EnvironmentInformation(@Nonnull String source, @Nonnull String assessmentFramework, @Nonnull String assessmentFrameworkVersion, @Nonnull String assessmentStartTime, @Nonnull String parserName, @Nonnull String parserVersion, String givenUUID, boolean printFormatted, String fileXPath) {
		//region Setting Internal Settings
		String tempToolFrameworkVersion;
		String tempToolFramework;
		String tempBuildFramework;
		String tempBuildFrameworkVersion;
		try {
			Properties.load(new FileInputStream(PropertiesFile));

			tempToolFrameworkVersion = Properties.getProperty("version");
			tempToolFramework = Properties.getProperty("projectName");
			tempBuildFramework = Properties.getProperty("buildFrameWork");
			tempBuildFrameworkVersion = Properties.getProperty("buildVersion");

		} catch (FileNotFoundException e) {
			tempToolFrameworkVersion = "Property Not Found";
			tempToolFramework = "Property Not Found";
			tempBuildFramework = "Property Not Found";
			tempBuildFrameworkVersion = "Property Not Found";
		} catch (IOException e) {
			tempToolFrameworkVersion = "Not Available";
			tempToolFramework = "Not Available";
			tempBuildFramework = "Not Available";
			tempBuildFrameworkVersion = "Not Available";
		}
		ToolFrameworkVersion = tempToolFrameworkVersion;
		ToolFramework = tempToolFramework;
		startTimeStamp = getCurrentDate();
		BuildFramework = tempBuildFramework;
		BuildFrameworkVersion = tempBuildFrameworkVersion;
		prettyPrint = printFormatted;
		//endregion

		//region Setting External Based Properties
		Source = source;
		AssessmentFramework = assessmentFramework;
		AssessmentFrameworkVersion = assessmentFrameworkVersion;
		AssessmentStartTime = assessmentStartTime;
		ParserName = parserName;
		ParserVersion = parserVersion;
		if (givenUUID == null || givenUUID.isEmpty()) {
			Random randomInst = new Random(getStringOfNumFromDate());
			this.UUID = Long.toHexString(randomInst.nextLong() ^ getStringOfNumFromDate());
		}
		else {
			UUID = givenUUID;
		}
		packageRootDir = "";
		buildId = -1;
		if (StringUtils.isNotBlank(fileXPath)) {
			xPath = fileXPath;
		}
		else {
			xPath = "Not Found";
		}
		//endregion
	}

	/**
	 * A short method to generate a  XMLGregorian type from the current date
	 * NOTE: For the future may need to push this method down into the Schema outputs and make this a normal timestamp
	 *
	 * @return XML Gregorian Calendar - a timestamp for use directly with the xsd schema
	 */
	public XMLGregorianCalendar getCurrentDate() {
		try {
			GregorianCalendar calander = new GregorianCalendar();
			DatatypeFactory translator = DatatypeFactory.newInstance();
			calander.setTime(new Date());
			return translator.newXMLGregorianCalendar(calander);
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}

	/**
	 * Returning a Long Number based on the time stamp
	 *
	 * @return - String with the current time stamp set
	 */
	private Long getStringOfNumFromDate() {
		StringBuilder date = new StringBuilder();
		Date currentDate = new Date();

		date.append(currentDate.getYear());
		date.append(currentDate.getMonth());
		date.append(currentDate.getDay());

		date.append(currentDate.getHours());
		date.append(currentDate.getMinutes());
		date.append(currentDate.getSeconds());

		return Long.valueOf(date.toString());
	}

	//region Getters and Setters
	public String getPackageRootDir() {
		return packageRootDir;
	}

	public String getToolFramework() {
		return ToolFramework;
	}

	public String getToolFrameworkVersion() {
		return ToolFrameworkVersion;
	}

	public XMLGregorianCalendar getStartTimeStamp() {
		return startTimeStamp;
	}

	public String getBuildFramework() {
		return BuildFramework;
	}

	public String getBuildFrameworkVersion() {
		return BuildFrameworkVersion;
	}

	public String getPlatformName() {
		return platformName;
	}

	public String getSource() {
		return Source;
	}

	public String getAssessmentFramework() {
		return AssessmentFramework;
	}

	public String getAssessmentFrameworkVersion() {
		return AssessmentFrameworkVersion;
	}

	public String getAssessmentStartTime() {
		return AssessmentStartTime;
	}

	public String getParserName() {
		return ParserName;
	}

	public String getParserVersion() {
		return ParserVersion;
	}

	public String getUUID() {
		return UUID;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageVersion() {
		return packageVersion;
	}

	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public String getxPath() {
		return xPath;
	}

	public Boolean prettyPrint() {
		return prettyPrint;
	}
	//endregion
}

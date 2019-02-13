package main.frontEnd.MessagingSystem.routing;

import main.rule.engine.EngineType;
import main.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class containing the analysis rule information.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V01.00.04
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
    private final String platformName = Utils.getPlatform();
    private String packageName = "UNKNOWN";
    private String packageVersion = "UNKNOWN";
    private boolean showTimes = false;
    private boolean addExperimentalRules = false;
    //endregion
    //region Required Elements Set From the Start
    private final List<String> Source;
    private final List<String> sourcePaths; //Could this be intertwined with source?
    private Boolean prettyPrint = false;
    private ByteArrayOutputStream internalErrors = new ByteArrayOutputStream();
    private List<String> dependencies;
    private EngineType sourceType;
    private Listing messagingType;
    private String UUID;
    private Long startAnalyisisTime;
    private Long analysisMilliSeconds;
    private String fileOut;
    private Boolean streaming = false;
    //endregion
    //region From Outside and defaulted unless set
    private String AssessmentFramework = "UNKNOWN";
    private String AssessmentFrameworkVersion = "UNKNOWN";
    private String AssessmentStartTime = "UNKNOWN";
    private String ParserName = "UNKNOWN";
    private String ParserVersion = "UNKNOWN";
    private String packageRootDir = "UNKNOWN";
    private String buildRootDir = "UNKNOWN";
    private Integer buildId;
    private String xPath;
    private Boolean printOut = false;
    //endregion
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    //endregion

    //region Constructor

    /**
     * The main constructor for setting all of the environmental variables used  for the outputs.
     *
     * @param source        {@link java.lang.String[]} - The source name to be analyzed
     * @param sourceType    {@link main.rule.engine.EngineType} - The type of source (APK/JAR/SourceCode)
     * @param sourceType    {@link main.rule.engine.EngineType} - The type of source (APK/JAR/SourceCode)
     * @param sourcePaths   a {@link java.util.List} object.
     * @param sourcePkg     a {@link java.lang.String} object.
     * @param sourceType    {@link main.rule.engine.EngineType} - The type of source (APK/JAR/SourceCode)
     * @param sourceType    {@link main.rule.engine.EngineType} - The type of source (APK/JAR/SourceCode)
     * @param sourceType    {@link main.rule.engine.EngineType} - The type of source (APK/JAR/SourceCode)
     * @param sourceType    {@link main.rule.engine.EngineType} - The type of source (APK/JAR/SourceCode)
     * @param dependencies  {@link java.lang.String} - The location of the directory of the sources dependencies
     * @param messagingType {@link java.lang.String} - The flag passed in to determine the type of messaging system from {@link main.frontEnd.MessagingSystem.routing.Listing}
     * @param sourcePaths   a {@link java.util.List} object.
     * @param sourcePkg     a {@link java.lang.String} object.
     */
    public EnvironmentInformation(@Nonnull List<String> source, @Nonnull EngineType sourceType, Listing messagingType, List<String> dependencies, List<String> sourcePaths, String sourcePkg) {

        //region Setting Internal Version Settings
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
        //endregion

        //region Setting Required Attributes
        System.setOut(new PrintStream(this.internalErrors));

        this.Source = source;
        this.sourceType = sourceType;
        if (dependencies != null)
            this.dependencies = dependencies;
        this.messagingType = messagingType;
        this.sourcePaths = sourcePaths;
        String[] pkgs = sourcePkg.split(System.getProperty("file.separator"));
        this.packageName = pkgs[pkgs.length - 1].split("\\.")[0];
        this.packageRootDir = sourcePkg;
        //endregion

    }
    //endregion

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
     * A simple method to "re-open" the console output after it was redirected for capture
     */
    public void openConsoleStream() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    //region Getters and Setters

    /**
     * <p>Getter for the field <code>addExperimentalRules</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public boolean isAddExperimentalRules() {
        return addExperimentalRules;
    }

    /**
     * <p>Setter for the field <code>addExperimentalRules</code>.</p>
     *
     * @param addExperimentalRules a {@link java.lang.Boolean} object.
     */
    public void setAddExperimentalRules(boolean addExperimentalRules) {
        this.addExperimentalRules = addExperimentalRules;
    }
    /**
     * <p>Setter for the field <code>streaming</code>.</p>
     *
     * @param flag a {@link java.lang.Boolean} object.
     */
    public void setStreaming(Boolean flag) {
        this.streaming = flag;
    }

    /**
     * <p>Getter for the field <code>streaming</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean getStreaming() {
        return streaming;
    }

    /**
     * Getter for internalErrors
     *
     * <p>getInternalErrors()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getInternalErrors() {
        return StringUtils.trimToEmpty(internalErrors.toString());
    }

    /**
     * The getter for Source
     *
     * <p>getSource()</p>
     *
     * @return {@link java.util.List<java.lang.String>} - Returns the Source field
     */
    public List<String> getSource() {
        return Source;
    }

    /**
     * Setter for prettyPrint
     *
     * <p>setPrettyPrint(java.lang.Boolean prettyPrint)</p>
     *
     * @param prettyPrint {@link java.lang.Boolean} - The value to set as prettyPrint
     */
    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    /**
     * Getter for printOut
     *
     * <p>getPrintOut()</p>
     *
     * @return {@link java.lang.Boolean} - The printOut.
     */
    public Boolean getPrintOut() {
        return printOut;
    }

    /**
     * <p>Getter for the field <code>packageRootDir</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPackageRootDir() {
        return packageRootDir;
    }

    /**
     * <p>getToolFramework.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getToolFramework() {
        return ToolFramework;
    }

    /**
     * <p>getToolFrameworkVersion.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getToolFrameworkVersion() {
        return ToolFrameworkVersion;
    }

    /**
     * <p>Getter for the field <code>startTimeStamp</code>.</p>
     *
     * @return a {@link javax.xml.datatype.XMLGregorianCalendar} object.
     */
    public XMLGregorianCalendar getStartTimeStamp() {
        return startTimeStamp;
    }

    /**
     * <p>getBuildFramework.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBuildFramework() {
        return BuildFramework;
    }

    /**
     * <p>getBuildFrameworkVersion.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBuildFrameworkVersion() {
        return BuildFrameworkVersion;
    }

    /**
     * <p>Getter for the field <code>platformName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * <p>getAssessmentFramework.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAssessmentFramework() {
        return AssessmentFramework;
    }

    /**
     * <p>getAssessmentFrameworkVersion.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAssessmentFrameworkVersion() {
        return AssessmentFrameworkVersion;
    }

    /**
     * <p>getAssessmentStartTime.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAssessmentStartTime() {
        return AssessmentStartTime;
    }

    /**
     * <p>getParserName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getParserName() {
        return ParserName;
    }

    /**
     * <p>getParserVersion.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getParserVersion() {
        return ParserVersion;
    }

    /**
     * <p>getUUID.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUUID() {

        if (this.UUID == null)
            this.UUID = java.util.UUID.randomUUID().toString();


        return this.UUID;

    }

    /**
     * <p>Getter for the field <code>dateFormat</code>.</p>
     *
     * @return a {@link java.text.SimpleDateFormat} object.
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * <p>Getter for the field <code>packageName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * <p>Setter for the field <code>packageName</code>.</p>
     *
     * @param packageName a {@link java.lang.String} object.
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * <p>Getter for the field <code>packageVersion</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPackageVersion() {
        return packageVersion;
    }

    /**
     * <p>Setter for the field <code>packageVersion</code>.</p>
     *
     * @param packageVersion a {@link java.lang.String} object.
     */
    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    /**
     * <p>Getter for the field <code>buildId</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getBuildId() {
        return buildId;
    }

    /**
     * <p>Getter for the field <code>xPath</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getxPath() {
        return xPath;
    }

    /**
     * <p>prettyPrint.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean prettyPrint() {
        return prettyPrint;
    }

    /**
     * <p>Getter for the field <code>buildRootDir</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBuildRootDir() {
        return buildRootDir;
    }

    /**
     * Getter for properties
     *
     * <p>getProperties()</p>
     *
     * @return a {@link java.util.Properties} object.
     */
    public java.util.Properties getProperties() {
        return Properties;
    }

    /**
     * Getter for propertiesFile
     *
     * <p>getPropertiesFile()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPropertiesFile() {
        return PropertiesFile;
    }

    /**
     * Getter for prettyPrint
     *
     * <p>getPrettyPrint()</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Getter for sourceDependencies
     *
     * <p>getDependencies()</p>
     *
     * @return a {@link java.util.List<java.lang.String>} object.
     */
    public List<String> getDependencies() {

        if (dependencies == null)
            dependencies = new ArrayList<>();

        return dependencies;

    }

    /**
     * Getter for sourceType
     *
     * <p>getSourceType()</p>
     *
     * @return a {@link main.rule.engine.EngineType} object.
     */
    public EngineType getSourceType() {
        return sourceType;
    }

    /**
     * Getter for messagingType
     *
     * <p>getMessagingType()</p>
     *
     * @return a {@link main.frontEnd.MessagingSystem.routing.Listing} object.
     */
    public Listing getMessagingType() {
        return messagingType;
    }


    /**
     * Setter for assessmentFramework
     *
     * <p>setAssessmentFramework(java.lang.String assessmentFramework)</p>
     *
     * @param assessmentFramework {@link java.lang.String} - The value to set as assessmentFramework
     */
    public void setAssessmentFramework(String assessmentFramework) {
        AssessmentFramework = assessmentFramework;
    }

    /**
     * Setter for assessmentFrameworkVersion
     *
     * <p>setAssessmentFrameworkVersion(java.lang.String assessmentFrameworkVersion)</p>
     *
     * @param assessmentFrameworkVersion {@link java.lang.String} - The value to set as assessmentFrameworkVersion
     */
    public void setAssessmentFrameworkVersion(String assessmentFrameworkVersion) {
        AssessmentFrameworkVersion = assessmentFrameworkVersion;
    }

    /**
     * Setter for assessmentStartTime
     *
     * <p>setAssessmentStartTime(java.lang.String assessmentStartTime)</p>
     *
     * @param assessmentStartTime {@link java.lang.String} - The value to set as assessmentStartTime
     */
    public void setAssessmentStartTime(String assessmentStartTime) {
        AssessmentStartTime = assessmentStartTime;
    }

    /**
     * Setter for parserName
     *
     * <p>setParserName(java.lang.String parserName)</p>
     *
     * @param parserName {@link java.lang.String} - The value to set as parserName
     */
    public void setParserName(String parserName) {
        ParserName = parserName;
    }

    /**
     * Setter for parserVersion
     *
     * <p>setParserVersion(java.lang.String parserVersion)</p>
     *
     * @param parserVersion {@link java.lang.String} - The value to set as parserVersion
     */
    public void setParserVersion(String parserVersion) {
        ParserVersion = parserVersion;
    }

    /**
     * Setter for packageRootDir
     *
     * <p>setPackageRootDir(java.lang.String packageRootDir)</p>
     *
     * @param packageRootDir {@link java.lang.String} - The value to set as packageRootDir
     */
    public void setPackageRootDir(String packageRootDir) {
        this.packageRootDir = packageRootDir;
    }

    /**
     * Setter for buildRootDir
     *
     * <p>setBuildRootDir(java.lang.String buildRootDir)</p>
     *
     * @param buildRootDir {@link java.lang.String} - The value to set as buildRootDir
     */
    public void setBuildRootDir(String buildRootDir) {
        this.buildRootDir = buildRootDir;
    }

    /**
     * Setter for buildId
     *
     * <p>setBuildId(java.lang.Integer buildId)</p>
     *
     * @param buildId {@link java.lang.Integer} - The value to set as buildId
     */
    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    /**
     * Setter for xPath
     *
     * <p>setxPath(java.lang.String xPath)</p>
     *
     * @param xPath {@link java.lang.String} - The value to set as xPath
     */
    public void setxPath(String xPath) {
        this.xPath = xPath;
    }


    /**
     * <p>Setter for the field <code>printOut</code>.</p>
     *
     * @param printOut a {@link java.lang.Boolean} object.
     */
    public void setPrintOut(Boolean printOut) {
        this.printOut = printOut;
    }

    /**
     * <p>Getter for the field <code>sourcePaths</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getSourcePaths() {
        return sourcePaths;
    }

    /**
     * <p>startAnalysis.</p>
     */
    public void startAnalysis() {
        this.startAnalyisisTime = System.currentTimeMillis();
    }

    /**
     * <p>stopAnalysis.</p>
     */
    public void stopAnalysis() {
        this.analysisMilliSeconds = System.currentTimeMillis() - this.startAnalyisisTime;
    }

    /**
     * <p>getAnalyisisTime.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getAnalyisisTime() {
        return this.analysisMilliSeconds;
    }

    /**
     * <p>Getter for the field <code>fileOut</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFileOut() {
        return fileOut;
    }

    /**
     * <p>Setter for the field <code>fileOut</code>.</p>
     *
     * @param fileOut a {@link java.lang.String} object.
     */
    public void setFileOut(String fileOut) {
        this.fileOut = fileOut;
    }

    /**
     * <p>isShowTimes.</p>
     *
     * @return a boolean.
     */
    public boolean isShowTimes() {
        return showTimes;
    }

    /**
     * <p>Setter for the field <code>showTimes</code>.</p>
     *
     * @param showTimes a boolean.
     */
    public void setShowTimes(boolean showTimes) {
        this.showTimes = showTimes;
    }
    //endregion
}
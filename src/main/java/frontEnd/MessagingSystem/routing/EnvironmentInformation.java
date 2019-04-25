package frontEnd.MessagingSystem.routing;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import rule.engine.EngineType;
import soot.G;
import util.Utils;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    private String BuildFramework;
    private String BuildFrameworkVersion;
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
    //endregion
    PrintStream old;
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
    private String AssessmentFramework;
    private String AssessmentFrameworkVersion;
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd_HH:mm:ss");
    private DateTime startTime = new DateTime();
    private String AssessmentStartTime = formatter.print(startTime);
    private String ParserName = "UNKNOWN";
    private String ParserVersion = "UNKNOWN";
    private String packageRootDir = "UNKNOWN";
    private String buildRootDir = "UNKNOWN";
    private Integer buildId;
    private String xPath;
    private Boolean printOut = false;
    private OutputStructure output;
    //endregion
    private ByteArrayOutputStream sootErrors = new ByteArrayOutputStream();
    //region Constructor

    /**
     * The main constructor for setting all of the environmental variables used  for the outputs.
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     * @param source a {@link java.util.List} object.
     * @param sourceType a {@link rule.engine.EngineType} object.
     * @param messagingType a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @param dependencies a {@link java.util.List} object.
     * @param sourcePaths a {@link java.util.List} object.
     * @param sourcePkg a {@link java.lang.String} object.
     */
    public EnvironmentInformation(@Nonnull List<String> source, @Nonnull EngineType sourceType, Listing messagingType, List<String> dependencies, List<String> sourcePaths, String sourcePkg) throws ExceptionHandler {

        //region Setting Internal Version Settings
        String tempToolFrameworkVersion;
        String tempToolFramework;
        try {
            Properties.load(new FileInputStream(PropertiesFile));
            tempToolFrameworkVersion = Properties.getProperty("versionNumber");
            tempToolFramework = Properties.getProperty("projectName");
        } catch (FileNotFoundException e) {
            tempToolFrameworkVersion = "Property Not Found";
            tempToolFramework = "Property Not Found";
        } catch (IOException e) {
            tempToolFrameworkVersion = "Not Available";
            tempToolFramework = "Not Available";
        }
        ToolFrameworkVersion = tempToolFrameworkVersion;
        ToolFramework = tempToolFramework;
        //endregion

        //region Setting Required Attributes

        //Redirecting the Soot Output - might need to change this
        G.v().out = new PrintStream(this.sootErrors);

        this.Source = source;
        this.sourceType = sourceType;
        if (dependencies != null)
            this.dependencies = dependencies;
        this.messagingType = messagingType;
        this.sourcePaths = sourcePaths;
        String[] pkgs = sourcePkg.split(System.getProperty("file.separator"));
        this.packageName = pkgs[pkgs.length - 1].split("\\.")[0];

        //this.setPackageRootDir(sourcePkg);
        //this.setBuildRootDir(sourcePkg);
        this.setPackageRootDir();
        this.setBuildRootDir();
        //endregion


    }
    //endregion

    //region Getters and Setters

    /**
     * <p>getAssessmentStartTime.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAssessmentStartTime() {
        return AssessmentStartTime;
    }

    /**
     * <p>startScanning.</p>
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void startScanning() throws ExceptionHandler {
        this.getOutput().startAnalyzing();
        this.startAnalysis();
    }

    /**
     * <p>stopScanning.</p>
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void stopScanning() throws ExceptionHandler {
        this.stopAnalysis();
        this.getOutput().stopAnalyzing();
    }

    /**
     * <p>Getter for the field <code>output</code>.</p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public OutputStructure getOutput() throws ExceptionHandler {
        if (this.output == null)
            this.output = this.messagingType.getTypeOfMessagingOutput(this.streaming, this);

        return this.output;
    }

    /**
     * <p>setBuildFramework.</p>
     *
     * @param buildFramework a {@link java.lang.String} object.
     */
    public void setBuildFramework(String buildFramework) {
        BuildFramework = buildFramework;
    }

    /**
     * <p>setBuildFrameworkVersion.</p>
     *
     * @param buildFrameworkVersion a {@link java.lang.String} object.
     */
    public void setBuildFrameworkVersion(String buildFrameworkVersion) {
        BuildFrameworkVersion = buildFrameworkVersion;
    }

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
     * Getter for sootErrors
     *
     * <p>getSootErrors()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSootErrors() {
        return StringUtils.trimToEmpty(sootErrors.toString());
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
        if (this.buildId == null)
            this.buildId = 0;

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
     * @return a {@link rule.engine.EngineType} object.
     */
    public EngineType getSourceType() {
        return sourceType;
    }

    /**
     * Getter for messagingType
     *
     * <p>getMessagingType()</p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.Listing} object.
     */
    public Listing getMessagingType() {
        return messagingType;
    }


    /**
     * <p>getMessagingOutput.</p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public OutputStructure getMessagingOutput() throws ExceptionHandler {
        if (this.output == null)
            this.output = messagingType.getTypeOfMessagingOutput(this.streaming, this);

        return this.output;
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
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void setPackageRootDir() throws ExceptionHandler {
        switch (this.getSourceType()) {
            /*case APK:
                this.packageRootDir = Utils.getBasePackageNameFromApk(this.getSource().get(0));
                this.packageRootDir = this.getSource().get(0);
                break;
            case DIR:
                String[] splitPart = this.getSource().get(0).split(Utils.fileSep);
                this.packageRootDir = splitPart[splitPart.length - 1].replaceAll(":dir",Utils.fileSep);
                break;
            case JAR:
                File jar = new File(this.getSource().get(0));
                this.packageRootDir = this.getSource().get(0); //Utils.getBasePackageNameFromJar(jar.getAbsolutePath(),true);*/
            case JAR:
            case DIR:
            case APK:
                String[] split = this.getSource().get(0).split(Utils.fileSep);
                this.packageRootDir = split[split.length - 1] + Utils.fileSep;
                break;
            case JAVAFILES:
            case CLASSFILES:
                this.packageRootDir = Utils.getRelativeFilePath(packageRootDir);
                break;
        }
    }

    /**
     * Setter for packageRootDir
     *
     * <p>setPackageRootDir(java.lang.String packageRootDir)</p>
     *
     * @param packageRootDir {@link java.lang.String} - The value to set as packageRootDir
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void setPackageRootDir(String packageRootDir) throws ExceptionHandler {
        this.packageRootDir = packageRootDir;
    }

    /**
     * Setter for buildRootDir
     *
     * <p>setBuildRootDir(java.lang.String buildRootDir)</p>
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void setBuildRootDir() throws ExceptionHandler {
        try {
            switch (this.getSourceType()) {
                case APK:
                case DIR:
                case JAR:
                    this.buildRootDir = new File(sourcePaths.get(0)).getCanonicalPath();
                    break;
                case JAVAFILES:
                case CLASSFILES:
                    this.buildRootDir = Utils.retrievePackageFromJavaFiles(sourcePaths);
                    break;
            }
        } catch (IOException e) {
            throw new ExceptionHandler("Error reading file: " + buildRootDir, ExceptionId.FILE_I);
        }
    }

    /**
     * Setter for buildRootDir
     *
     * <p>setBuildRootDir(java.lang.String buildRootDir)</p>
     *
     * @param buildRootDir {@link java.lang.String} - The value to set as buildRootDir
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void setBuildRootDir(String buildRootDir) throws ExceptionHandler {
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
     * <p>getFileOutName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFileOutName() {
        String[] split = this.fileOut.split(System.getProperty("file.separator"));

        return split[split.length - 1];
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

    /**
     * <p>Setter for the field <code>UUID</code>.</p>
     *
     * @param UUID a String.
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    //endregion
}

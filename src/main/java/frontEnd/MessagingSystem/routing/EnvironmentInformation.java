/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import frontEnd.MessagingSystem.routing.outputStructures.common.Heuristics;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import rule.engine.EngineType;
import soot.G;
import util.Utils;

/**
 * The class containing the analysis rule information.
 *
 * <p>STATUS: IC
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.04
 */
public class EnvironmentInformation {

  private static final Logger log =
      org.apache.logging.log4j.LogManager.getLogger(EnvironmentInformation.class);
  //region Attributes
  //region Self Generated
  private final Properties Properties = new Properties();
  private final String PropertiesFile = "gradle.properties";
  private String ToolFramework;
  private String ToolFrameworkVersion;
  private String platformName = Utils.getPlatform();
  //endregion
  //region Required Elements Set From the Start
  private final List<String> Source;
  private final List<String> sourcePaths; //Could this be intertwined with source?
  //endregion
  PrintStream old;
  private String BuildFramework;
  private String BuildFrameworkVersion;
  private String packageName = "UNKNOWN";
  private String packageVersion = "UNKNOWN";
  private boolean showTimes = false;
  private boolean addExperimentalRules = false;
  private String rawCommand;
  private String main;
  private boolean overWriteOutput = false;
  private String targetProjectName;
  private String targetProjectVersion;
  private Boolean isGradle;
  private Boolean prettyPrint = false;
  private Boolean killJVM = true;
  private List<String> dependencies;
  private EngineType sourceType;
  private Listing messagingType;
  private String UUID;
  private Long startAnalyisisTime;
  private Long analysisMilliSeconds;
  private String fileOut;
  private Boolean streaming = false;
  String javaHome;
  String androidHome;
  //region From Outside and defaulted unless set
  private String AssessmentFramework;
  private String AssessmentFrameworkVersion;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");

  private LocalDateTime startTime = LocalDateTime.now();
  private String AssessmentStartTime = startTime.format(formatter);
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
  //region Heuristics from Utils
  private Boolean displayHeuristics = false;
  private Heuristics heuristics = new Heuristics();
  //endregion
  //region Predicates used to help display streamed info
  private Function<AnalysisIssue, String> errorAddition;
  private Function<HashMap<Integer, Integer>, String> bugSummaryHandler;
  private Function<Heuristics, String> heuristicsHandler;
  //endregion
  //endregion
  //region Constructor

  /** Constructor for EnvironmentInformation. */
  public EnvironmentInformation() {
    this.ToolFramework = "";
    this.ToolFrameworkVersion = "";
    this.sourcePaths = new ArrayList<>();
    this.Source = new ArrayList<>();
  }

  /**
   * The main constructor for setting all of the environmental variables used for the outputs.
   *
   * @param source a {@link java.util.List} object.
   * @param sourceType a {@link rule.engine.EngineType} object.
   * @param messagingType a {@link frontEnd.MessagingSystem.routing.Listing} object.
   * @param dependencies a {@link java.util.List} object.
   * @param sourcePaths a {@link java.util.List} object.
   * @param sourcePkg a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public EnvironmentInformation(
      @Nonnull List<String> source,
      @Nonnull EngineType sourceType,
      Listing messagingType,
      List<String> dependencies,
      List<String> sourcePaths,
      String sourcePkg)
      throws ExceptionHandler {

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
    if (dependencies != null) this.dependencies = dependencies;
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
   * verifyBaseSettings.
   *
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void verifyBaseSettings() throws ExceptionHandler {
    switch (this.sourceType) {
      case DIR:
      case JAVAFILES:
        if (StringUtils.isEmpty(getJavaHome())) {
          log.fatal("Please set JAVA7_HOME or specify via the arguments.");
          throw new ExceptionHandler(
              "Please set JAVA7_HOME or specify via the arguments.", ExceptionId.ENV_VAR);
        }
        break;
      case APK:
        if (StringUtils.isEmpty(getAndroidHome())) {
          log.fatal("Please set ANDROID_HOME or specify via the arguments.");
          throw new ExceptionHandler(
              "Please set ANDROID_HOME or specify via the arguments.", ExceptionId.ENV_VAR);
        }
      case JAR:
      case CLASSFILES:
        if (StringUtils.isEmpty(getJavaHome())) {
          log.fatal("Please set JAVA_HOME or specify via the arguments.");
          throw new ExceptionHandler(
              "Please set JAVA_HOME or specify via the arguments.", ExceptionId.ENV_VAR);
        }
        break;
    }
  }

  /**
   * Getter for the field <code>androidHome</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getAndroidHome() {
    if (StringUtils.isEmpty(this.androidHome))
      this.androidHome = System.getenv("ANDROID_HOME").replaceAll("//", "/");

    return this.androidHome;
  }

  /**
   * Getter for the field <code>javaHome</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getJavaHome() {
    if (StringUtils.isEmpty(this.javaHome))
      switch (this.sourceType) {
        case CLASSFILES:
        case JAR:
        case APK:
          this.javaHome = System.getenv("JAVA_HOME").replaceAll("//", "/");
          break;
        case JAVAFILES:
        case DIR:
          this.javaHome = System.getenv("JAVA7_HOME").replaceAll("//", "/");
          break;
      }

    return this.javaHome;
  }

  /**
   * addToDepth_Count.
   *
   * @param item a {@link java.lang.String} object.
   */
  public void addToDepth_Count(String item) {
    this.getHeuristics().addDepthCount(item);
    //this.DEPTH_COUNT.add(item);
  }

  /** startAnalysis. */
  public void startAnalysis() {
    this.startAnalyisisTime = System.currentTimeMillis();
  }

  /** stopAnalysis. */
  public void stopAnalysis() {
    this.analysisMilliSeconds = System.currentTimeMillis() - this.startAnalyisisTime;
  }

  /**
   * Getter for sootErrors
   *
   * <p>getSootErrors()
   *
   * @return a {@link java.lang.String} object.
   */
  public String getSootErrors() {
    return StringUtils.trimToEmpty(sootErrors.toString());
  }

  /**
   * startScanning.
   *
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void startScanning() throws ExceptionHandler {
    this.getOutput().startAnalyzing();
    this.startAnalysis();
  }

  /**
   * stopScanning.
   *
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void stopScanning() throws ExceptionHandler {
    this.stopAnalysis();
    this.setHuristicsInfo();

    if (this.getHeuristicsHandler() != null)
      log.info(this.getHeuristicsHandler().apply(this.getHeuristics()));

    this.getOutput().stopAnalyzing();
  }

  /**
   * Getter for the field <code>output</code>.
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
   * getSLICE_AVERAGE_3SigFig.
   *
   * @return a double.
   */
  public double getSLICE_AVERAGE_3SigFig() {
    return this.getHeuristics().getSliceAverage();
  }

  /**
   * getUUID.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getUUID() {

    if (this.UUID == null) this.UUID = java.util.UUID.randomUUID().toString();

    return this.UUID;
  }

  /**
   * Getter for the field <code>buildId</code>.
   *
   * @return a {@link java.lang.Integer} object.
   */
  public Integer getBuildId() {
    if (this.buildId == null) this.buildId = 0;

    return buildId;
  }

  /**
   * Getter for sourceDependencies
   *
   * <p>getDependencies()
   *
   * @return a String object.
   */
  public List<String> getDependencies() {

    if (dependencies == null) dependencies = new ArrayList<>();

    return dependencies;
  }

  /**
   * getMessagingOutput.
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
   * Setter for packageRootDir
   *
   * <p>setPackageRootDir(java.lang.String packageRootDir)
   *
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void setPackageRootDir() throws ExceptionHandler {
    log.info("Building the Package Root Dir based on type");
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
   * Setter for buildRootDir
   *
   * <p>setBuildRootDir(java.lang.String buildRootDir)
   *
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void setBuildRootDir() throws ExceptionHandler {
    log.info("Building the Root Directory");
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
      log.fatal("Error reading file: " + buildRootDir);
      throw new ExceptionHandler("Error reading file: " + buildRootDir, ExceptionId.FILE_I);
    }
  }

  /**
   * getFileOutName.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getFileOutName() {
    String[] split = this.fileOut.split(System.getProperty("file.separator"));

    return split[split.length - 1];
  }

  public String getComputerOS() {
    return Utils.getPlatform();
  }

  public String getJVM() {
    return Utils.getJVMInfo();
  }
  //endregion
  //region Helpful Methods

  /** setHuristicsInfo. */
  public void setHuristicsInfo() {
    this.heuristics.setNumberOfOrthogonal(Utils.NUM_ORTHOGONAL);
    this.heuristics.setNumberOfConstantsToCheck(Utils.NUM_CONSTS_TO_CHECK);
    this.heuristics.setNumberOfSlices(Utils.NUM_SLICES);
    this.heuristics.setNumberOfHeuristics(Utils.NUM_HEURISTIC);
    this.heuristics.setSliceAverage(Utils.calculateAverage());
    this.heuristics.setDepthCount(Utils.createDepthCountList());
  }

  /**
   * getProperties.
   *
   * @return a {@link java.util.Properties} object.
   */
  public Properties getProperties() {
    return this.Properties;
  }

  /**
   * getPropertiesFile.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getPropertiesFile() {
    return this.PropertiesFile;
  }

  /**
   * getToolFramework.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getToolFramework() {
    return this.ToolFramework;
  }

  /**
   * getToolFrameworkVersion.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getToolFrameworkVersion() {
    return this.ToolFrameworkVersion;
  }

  /**
   * Getter for the field <code>platformName</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getPlatformName() {
    return this.platformName;
  }

  /**
   * getSource.
   *
   * @return a {@link java.util.List} object.
   */
  public List<String> getSource() {
    return this.Source;
  }

  /**
   * Getter for the field <code>sourcePaths</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<String> getSourcePaths() {
    return this.sourcePaths;
  }

  /**
   * getBuildFramework.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getBuildFramework() {
    return this.BuildFramework;
  }

  /**
   * getBuildFrameworkVersion.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getBuildFrameworkVersion() {
    return this.BuildFrameworkVersion;
  }

  /**
   * Getter for the field <code>packageName</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getPackageName() {
    return this.packageName;
  }

  /**
   * Getter for the field <code>packageVersion</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getPackageVersion() {
    return this.packageVersion;
  }

  /**
   * isShowTimes.
   *
   * @return a boolean.
   */
  public boolean isShowTimes() {
    return this.showTimes;
  }

  /**
   * isAddExperimentalRules.
   *
   * @return a boolean.
   */
  public boolean isAddExperimentalRules() {
    return this.addExperimentalRules;
  }

  /**
   * Getter for the field <code>rawCommand</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getRawCommand() {
    return this.rawCommand;
  }

  /**
   * Getter for the field <code>main</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getMain() {
    return this.main;
  }

  /**
   * isOverWriteOutput.
   *
   * @return a boolean.
   */
  public boolean isOverWriteOutput() {
    return this.overWriteOutput;
  }

  /**
   * Getter for the field <code>targetProjectName</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getTargetProjectName() {
    return this.targetProjectName;
  }

  /**
   * Getter for the field <code>targetProjectVersion</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getTargetProjectVersion() {
    return this.targetProjectVersion;
  }

  /**
   * Getter for the field <code>isGradle</code>.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  public Boolean getIsGradle() {
    return this.isGradle;
  }

  /**
   * Getter for the field <code>prettyPrint</code>.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  public Boolean getPrettyPrint() {
    return this.prettyPrint;
  }

  /**
   * Getter for the field <code>killJVM</code>.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  public Boolean getKillJVM() {
    return this.killJVM;
  }

  /**
   * Getter for the field <code>sourceType</code>.
   *
   * @return a {@link rule.engine.EngineType} object.
   */
  public EngineType getSourceType() {
    return this.sourceType;
  }

  /**
   * Getter for the field <code>messagingType</code>.
   *
   * @return a {@link frontEnd.MessagingSystem.routing.Listing} object.
   */
  public Listing getMessagingType() {
    return this.messagingType;
  }

  /**
   * Getter for the field <code>startAnalyisisTime</code>.
   *
   * @return a {@link java.lang.Long} object.
   */
  public Long getStartAnalyisisTime() {
    return this.startAnalyisisTime;
  }

  /**
   * Getter for the field <code>analysisMilliSeconds</code>.
   *
   * @return a {@link java.lang.Long} object.
   */
  public Long getAnalysisMilliSeconds() {
    return this.analysisMilliSeconds;
  }

  /**
   * Getter for the field <code>fileOut</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getFileOut() {
    return this.fileOut;
  }

  /**
   * Getter for the field <code>streaming</code>.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  public Boolean getStreaming() {
    return this.streaming;
  }

  /**
   * getAssessmentFramework.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getAssessmentFramework() {
    return this.AssessmentFramework;
  }

  /**
   * getAssessmentFrameworkVersion.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getAssessmentFrameworkVersion() {
    return this.AssessmentFrameworkVersion;
  }

  /**
   * Getter for the field <code>formatter</code>.
   *
   * @return a {@link java.time.format.DateTimeFormatter} object.
   */
  public DateTimeFormatter getFormatter() {
    return this.formatter;
  }

  /**
   * getAssessmentStartTime.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getAssessmentStartTime() {
    return this.AssessmentStartTime;
  }

  /**
   * getParserName.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getParserName() {
    return this.ParserName;
  }

  /**
   * getParserVersion.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getParserVersion() {
    return this.ParserVersion;
  }

  /**
   * Getter for the field <code>packageRootDir</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getPackageRootDir() {
    return this.packageRootDir;
  }

  /**
   * Getter for the field <code>buildRootDir</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getBuildRootDir() {
    return this.buildRootDir;
  }

  /**
   * Getter for the field <code>xPath</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getXPath() {
    return this.xPath;
  }

  /**
   * Getter for the field <code>printOut</code>.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  public Boolean getPrintOut() {
    return this.printOut;
  }

  /**
   * Getter for the field <code>displayHeuristics</code>.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  public Boolean getDisplayHeuristics() {
    return this.displayHeuristics;
  }

  /**
   * Getter for the field <code>heuristics</code>.
   *
   * @return a {@link frontEnd.MessagingSystem.routing.outputStructures.common.Heuristics} object.
   */
  public Heuristics getHeuristics() {
    return this.heuristics;
  }

  /**
   * Getter for the field <code>errorAddition</code>.
   *
   * @return a {@link java.util.function.Function} object.
   */
  public Function<AnalysisIssue, String> getErrorAddition() {
    return this.errorAddition;
  }

  /**
   * Getter for the field <code>bugSummaryHandler</code>.
   *
   * @return a {@link java.util.function.Function} object.
   */
  public Function<HashMap<Integer, Integer>, String> getBugSummaryHandler() {
    return this.bugSummaryHandler;
  }

  /**
   * Getter for the field <code>heuristicsHandler</code>.
   *
   * @return a {@link java.util.function.Function} object.
   */
  public Function<Heuristics, String> getHeuristicsHandler() {
    return this.heuristicsHandler;
  }

  /**
   * setToolFramework.
   *
   * @param ToolFramework a {@link java.lang.String} object.
   */
  public void setToolFramework(String ToolFramework) {
    this.ToolFramework = ToolFramework;
  }

  /**
   * setToolFrameworkVersion.
   *
   * @param ToolFrameworkVersion a {@link java.lang.String} object.
   */
  public void setToolFrameworkVersion(String ToolFrameworkVersion) {
    this.ToolFrameworkVersion = ToolFrameworkVersion;
  }

  /**
   * Setter for the field <code>platformName</code>.
   *
   * @param platformName a {@link java.lang.String} object.
   */
  public void setPlatformName(String platformName) {
    this.platformName = platformName;
  }

  /**
   * setBuildFramework.
   *
   * @param BuildFramework a {@link java.lang.String} object.
   */
  public void setBuildFramework(String BuildFramework) {
    this.BuildFramework = BuildFramework;
  }

  /**
   * setBuildFrameworkVersion.
   *
   * @param BuildFrameworkVersion a {@link java.lang.String} object.
   */
  public void setBuildFrameworkVersion(String BuildFrameworkVersion) {
    this.BuildFrameworkVersion = BuildFrameworkVersion;
  }

  /**
   * Setter for the field <code>packageName</code>.
   *
   * @param packageName a {@link java.lang.String} object.
   */
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  /**
   * Setter for the field <code>packageVersion</code>.
   *
   * @param packageVersion a {@link java.lang.String} object.
   */
  public void setPackageVersion(String packageVersion) {
    this.packageVersion = packageVersion;
  }

  /**
   * Setter for the field <code>showTimes</code>.
   *
   * @param showTimes a boolean.
   */
  public void setShowTimes(boolean showTimes) {
    this.showTimes = showTimes;
  }

  /**
   * Setter for the field <code>addExperimentalRules</code>.
   *
   * @param addExperimentalRules a boolean.
   */
  public void setAddExperimentalRules(boolean addExperimentalRules) {
    this.addExperimentalRules = addExperimentalRules;
  }

  /**
   * Setter for the field <code>rawCommand</code>.
   *
   * @param rawCommand a {@link java.lang.String} object.
   */
  public void setRawCommand(String rawCommand) {
    this.rawCommand = rawCommand;
  }

  /**
   * Setter for the field <code>main</code>.
   *
   * @param main a {@link java.lang.String} object.
   */
  public void setMain(String main) {
    this.main = main;
  }

  /**
   * Setter for the field <code>overWriteOutput</code>.
   *
   * @param overWriteOutput a boolean.
   */
  public void setOverWriteOutput(boolean overWriteOutput) {
    this.overWriteOutput = overWriteOutput;
  }

  /**
   * Setter for the field <code>targetProjectName</code>.
   *
   * @param targetProjectName a {@link java.lang.String} object.
   */
  public void setTargetProjectName(String targetProjectName) {
    this.targetProjectName = targetProjectName;
  }

  /**
   * Setter for the field <code>targetProjectVersion</code>.
   *
   * @param targetProjectVersion a {@link java.lang.String} object.
   */
  public void setTargetProjectVersion(String targetProjectVersion) {
    this.targetProjectVersion = targetProjectVersion;
  }

  /**
   * Setter for the field <code>isGradle</code>.
   *
   * @param isGradle a {@link java.lang.Boolean} object.
   */
  public void setIsGradle(Boolean isGradle) {
    this.isGradle = isGradle;
  }

  /**
   * Setter for the field <code>prettyPrint</code>.
   *
   * @param prettyPrint a {@link java.lang.Boolean} object.
   */
  public void setPrettyPrint(Boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
  }

  /**
   * Setter for the field <code>killJVM</code>.
   *
   * @param killJVM a {@link java.lang.Boolean} object.
   */
  public void setKillJVM(Boolean killJVM) {
    this.killJVM = killJVM;
  }

  /**
   * Setter for the field <code>dependencies</code>.
   *
   * @param dependencies a {@link java.util.List} object.
   */
  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  /**
   * Setter for the field <code>sourceType</code>.
   *
   * @param sourceType a {@link rule.engine.EngineType} object.
   */
  public void setSourceType(EngineType sourceType) {
    this.sourceType = sourceType;
  }

  /**
   * Setter for the field <code>messagingType</code>.
   *
   * @param messagingType a {@link frontEnd.MessagingSystem.routing.Listing} object.
   */
  public void setMessagingType(Listing messagingType) {
    this.messagingType = messagingType;
  }

  /**
   * setUUID.
   *
   * @param UUID a {@link java.lang.String} object.
   */
  public void setUUID(String UUID) {
    this.UUID = UUID;
  }

  /**
   * Setter for the field <code>startAnalyisisTime</code>.
   *
   * @param startAnalyisisTime a {@link java.lang.Long} object.
   */
  public void setStartAnalyisisTime(Long startAnalyisisTime) {
    this.startAnalyisisTime = startAnalyisisTime;
  }

  /**
   * Setter for the field <code>analysisMilliSeconds</code>.
   *
   * @param analysisMilliSeconds a {@link java.lang.Long} object.
   */
  public void setAnalysisMilliSeconds(Long analysisMilliSeconds) {
    this.analysisMilliSeconds = analysisMilliSeconds;
  }

  /**
   * Setter for the field <code>fileOut</code>.
   *
   * @param fileOut a {@link java.lang.String} object.
   */
  public void setFileOut(String fileOut) {
    this.fileOut = fileOut;
  }

  /**
   * Setter for the field <code>streaming</code>.
   *
   * @param streaming a {@link java.lang.Boolean} object.
   */
  public void setStreaming(Boolean streaming) {
    this.streaming = streaming;
  }

  /**
   * Setter for the field <code>javaHome</code>.
   *
   * @param javaHome a {@link java.lang.String} object.
   */
  public void setJavaHome(String javaHome) {
    this.javaHome = javaHome;
  }

  /**
   * Setter for the field <code>androidHome</code>.
   *
   * @param androidHome a {@link java.lang.String} object.
   */
  public void setAndroidHome(String androidHome) {
    this.androidHome = androidHome;
  }

  /**
   * setAssessmentFramework.
   *
   * @param AssessmentFramework a {@link java.lang.String} object.
   */
  public void setAssessmentFramework(String AssessmentFramework) {
    this.AssessmentFramework = AssessmentFramework;
  }

  /**
   * setAssessmentFrameworkVersion.
   *
   * @param AssessmentFrameworkVersion a {@link java.lang.String} object.
   */
  public void setAssessmentFrameworkVersion(String AssessmentFrameworkVersion) {
    this.AssessmentFrameworkVersion = AssessmentFrameworkVersion;
  }

  /**
   * Setter for the field <code>formatter</code>.
   *
   * @param formatter a {@link java.time.format.DateTimeFormatter} object.
   */
  public void setFormatter(DateTimeFormatter formatter) {
    this.formatter = formatter;
  }

  /**
   * setAssessmentStartTime.
   *
   * @param AssessmentStartTime a {@link java.lang.String} object.
   */
  public void setAssessmentStartTime(String AssessmentStartTime) {
    this.AssessmentStartTime = AssessmentStartTime;
  }

  /**
   * setParserName.
   *
   * @param ParserName a {@link java.lang.String} object.
   */
  public void setParserName(String ParserName) {
    this.ParserName = ParserName;
  }

  /**
   * setParserVersion.
   *
   * @param ParserVersion a {@link java.lang.String} object.
   */
  public void setParserVersion(String ParserVersion) {
    this.ParserVersion = ParserVersion;
  }

  /**
   * Setter for the field <code>packageRootDir</code>.
   *
   * @param packageRootDir a {@link java.lang.String} object.
   */
  public void setPackageRootDir(String packageRootDir) {
    this.packageRootDir = packageRootDir;
  }

  /**
   * Setter for the field <code>buildRootDir</code>.
   *
   * @param buildRootDir a {@link java.lang.String} object.
   */
  public void setBuildRootDir(String buildRootDir) {
    this.buildRootDir = buildRootDir;
  }

  /**
   * Setter for the field <code>buildId</code>.
   *
   * @param buildId a {@link java.lang.Integer} object.
   */
  public void setBuildId(Integer buildId) {
    this.buildId = buildId;
  }

  /**
   * Setter for the field <code>xPath</code>.
   *
   * @param xPath a {@link java.lang.String} object.
   */
  public void setXPath(String xPath) {
    this.xPath = xPath;
  }

  /**
   * Setter for the field <code>printOut</code>.
   *
   * @param printOut a {@link java.lang.Boolean} object.
   */
  public void setPrintOut(Boolean printOut) {
    this.printOut = printOut;
  }

  /**
   * Setter for the field <code>output</code>.
   *
   * @param output a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure}
   *     object.
   */
  public void setOutput(OutputStructure output) {
    this.output = output;
  }

  /**
   * Setter for the field <code>sootErrors</code>.
   *
   * @param sootErrors a {@link java.io.ByteArrayOutputStream} object.
   */
  public void setSootErrors(ByteArrayOutputStream sootErrors) {
    this.sootErrors = sootErrors;
  }

  /**
   * Setter for the field <code>displayHeuristics</code>.
   *
   * @param displayHeuristics a {@link java.lang.Boolean} object.
   */
  public void setDisplayHeuristics(Boolean displayHeuristics) {
    this.displayHeuristics = displayHeuristics;
  }

  /**
   * Setter for the field <code>heuristics</code>.
   *
   * @param heuristics a {@link frontEnd.MessagingSystem.routing.outputStructures.common.Heuristics}
   *     object.
   */
  public void setHeuristics(Heuristics heuristics) {
    this.heuristics = heuristics;
  }

  /**
   * Setter for the field <code>errorAddition</code>.
   *
   * @param errorAddition a {@link java.util.function.Function} object.
   */
  public void setErrorAddition(Function<AnalysisIssue, String> errorAddition) {
    this.errorAddition = errorAddition;
  }

  /**
   * Setter for the field <code>bugSummaryHandler</code>.
   *
   * @param bugSummaryHandler a {@link java.util.function.Function} object.
   */
  public void setBugSummaryHandler(Function<HashMap<Integer, Integer>, String> bugSummaryHandler) {
    this.bugSummaryHandler = bugSummaryHandler;
  }

  /**
   * Setter for the field <code>heuristicsHandler</code>.
   *
   * @param heuristicsHandler a {@link java.util.function.Function} object.
   */
  public void setHeuristicsHandler(Function<Heuristics, String> heuristicsHandler) {
    this.heuristicsHandler = heuristicsHandler;
  }
  //endregion
}

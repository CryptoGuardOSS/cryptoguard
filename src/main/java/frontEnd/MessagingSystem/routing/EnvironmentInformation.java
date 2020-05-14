package frontEnd.MessagingSystem.routing;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import frontEnd.MessagingSystem.routing.outputStructures.common.Heuristics;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
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
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

/**
 * The class containing the analysis rule information.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.04
 */
@Log4j2
public class EnvironmentInformation {

    //region Attributes
    //region Self Generated
    @Getter
    private final Properties Properties = new Properties();
    @Getter
    private final String PropertiesFile = "gradle.properties";
    @Getter
    @Setter
    private String ToolFramework;
    @Getter
    @Setter
    private String ToolFrameworkVersion;
    @Getter
    @Setter
    private String platformName = Utils.getPlatform();
    //endregion
    //region Required Elements Set From the Start
    @Getter
    private final List<String> Source;
    @Getter
    private final List<String> sourcePaths; //Could this be intertwined with source?
    //endregion
    PrintStream old;
    @Getter
    @Setter
    private String BuildFramework;
    @Getter
    @Setter
    private String BuildFrameworkVersion;
    @Getter
    @Setter
    private String packageName = "UNKNOWN";
    @Getter
    @Setter
    private String packageVersion = "UNKNOWN";
    @Getter
    @Setter
    private boolean showTimes = false;
    @Getter
    @Setter
    private boolean addExperimentalRules = false;
    @Getter
    @Setter
    private String rawCommand;
    @Getter
    @Setter
    private String main;
    @Getter
    @Setter
    private boolean overWriteOutput = false;
    @Getter
    @Setter
    private String targetProjectName;
    @Getter
    @Setter
    private String targetProjectVersion;
    @Getter
    @Setter
    private Boolean isGradle;
    @Getter
    @Setter
    private Boolean prettyPrint = false;
    @Getter
    @Setter
    private Boolean killJVM = true;
    @Setter
    private List<String> dependencies;
    @Getter
    @Setter
    private EngineType sourceType;
    @Getter
    @Setter
    private Listing messagingType;
    @Setter
    private String UUID;
    @Getter
    @Setter
    private Long startAnalyisisTime;
    @Getter
    @Setter
    private Long analysisMilliSeconds;
    @Getter
    @Setter
    private String fileOut;
    @Getter
    @Setter
    private Boolean streaming = false;
    @Setter
    String javaHome;
    @Setter
    String androidHome;
    //region From Outside and defaulted unless set
    @Getter
    @Setter
    private String AssessmentFramework;
    @Getter
    @Setter
    private String AssessmentFrameworkVersion;
    @Getter
    @Setter
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd_HH:mm:ss");
    private DateTime startTime = new DateTime();
    @Getter
    @Setter
    private String AssessmentStartTime = formatter.print(startTime);
    @Getter
    @Setter
    private String ParserName = "UNKNOWN";
    @Getter
    @Setter
    private String ParserVersion = "UNKNOWN";
    @Getter
    @Setter
    private String packageRootDir = "UNKNOWN";
    @Getter
    @Setter
    private String buildRootDir = "UNKNOWN";
    @Setter
    private Integer buildId;
    @Getter
    @Setter
    private String xPath;
    @Getter
    @Setter
    private Boolean printOut = false;
    @Setter
    private OutputStructure output;
    //endregion
    @Setter
    private ByteArrayOutputStream sootErrors = new ByteArrayOutputStream();
    //region Heuristics from Utils
    @Getter
    @Setter
    private Boolean displayHeuristics = false;
    @Getter
    @Setter
    private Heuristics heuristics = new Heuristics();
    //endregion
    //region Predicates used to help display streamed info
    @Getter
    @Setter
    private Function<AnalysisIssue, String> errorAddition;
    @Getter
    @Setter
    private Function<HashMap<Integer, Integer>, String> bugSummaryHandler;
    @Getter
    @Setter
    private Function<Heuristics, String> heuristicsHandler;
    //endregion
    //endregion
    //region Constructor

    /**
     * <p>Constructor for EnvironmentInformation.</p>
     */
    public EnvironmentInformation() {
        this.ToolFramework = "";
        this.ToolFrameworkVersion = "";
        this.sourcePaths = new ArrayList<>();
        this.Source = new ArrayList<>();
    }

    /**
     * The main constructor for setting all of the environmental variables used  for the outputs.
     *
     * @param source        a {@link java.util.List} object.
     * @param sourceType    a {@link rule.engine.EngineType} object.
     * @param messagingType a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @param dependencies  a {@link java.util.List} object.
     * @param sourcePaths   a {@link java.util.List} object.
     * @param sourcePkg     a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
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
     * <p>verifyBaseSettings.</p>
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void verifyBaseSettings() throws ExceptionHandler {
        switch (this.sourceType) {
            case DIR:
            case JAVAFILES:
                if (StringUtils.isEmpty(getJavaHome())) {
                    log.fatal("Please set JAVA7_HOME or specify via the arguments.");
                    throw new ExceptionHandler("Please set JAVA7_HOME or specify via the arguments.", ExceptionId.ENV_VAR);
                }
                break;
            case APK:
                if (StringUtils.isEmpty(getAndroidHome())) {
                    log.fatal("Please set ANDROID_HOME or specify via the arguments.");
                    throw new ExceptionHandler("Please set ANDROID_HOME or specify via the arguments.", ExceptionId.ENV_VAR);
                }
            case JAR:
            case CLASSFILES:
                if (StringUtils.isEmpty(getJavaHome())) {
                    log.fatal("Please set JAVA_HOME or specify via the arguments.");
                    throw new ExceptionHandler("Please set JAVA_HOME or specify via the arguments.", ExceptionId.ENV_VAR);
                }
                break;
        }
    }

    /**
     * <p>Getter for the field <code>androidHome</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAndroidHome() {
        if (StringUtils.isEmpty(this.androidHome))
            this.androidHome = System.getenv("ANDROID_HOME").replaceAll("//", "/");

        return this.androidHome;
    }

    /**
     * <p>Getter for the field <code>javaHome</code>.</p>
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
     * <p>addToDepth_Count.</p>
     *
     * @param item a {@link java.lang.String} object.
     */
    public void addToDepth_Count(String item) {
        this.getHeuristics().addDepthCount(item);
        //this.DEPTH_COUNT.add(item);
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
        this.setHuristicsInfo();

        if (this.getHeuristicsHandler() != null)
            log.info(this.getHeuristicsHandler().apply(this.getHeuristics()));

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
     * <p>getSLICE_AVERAGE_3SigFig.</p>
     *
     * @return a double.
     */
    public double getSLICE_AVERAGE_3SigFig() {
        return this.getHeuristics().getSliceAverage();
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
     * Setter for packageRootDir
     *
     * <p>setPackageRootDir(java.lang.String packageRootDir)</p>
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
     * <p>setBuildRootDir(java.lang.String buildRootDir)</p>
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
     * <p>getFileOutName.</p>
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

    /**
     * <p>setHuristicsInfo.</p>
     */
    public void setHuristicsInfo() {
        this.heuristics.setNumberOfOrthogonal(Utils.NUM_ORTHOGONAL);
        this.heuristics.setNumberOfConstantsToCheck(Utils.NUM_CONSTS_TO_CHECK);
        this.heuristics.setNumberOfSlices(Utils.NUM_SLICES);
        this.heuristics.setNumberOfHeuristics(Utils.NUM_HEURISTIC);
        this.heuristics.setSliceAverage(Utils.calculateAverage());
        this.heuristics.setDepthCount(Utils.createDepthCountList());

    }
    //endregion
}

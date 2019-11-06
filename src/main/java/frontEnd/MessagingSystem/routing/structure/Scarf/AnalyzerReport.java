package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.Listing;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * AnalyzerReport
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonPropertyOrder({
        "-assess_fw",
        "-assess_fw_version",
        "-assessment_start_ts",
        "-build_fw",
        "-build_fw_version",
        "-package_name",
        "-package_version",
        "-build_root_dir",
        "-package_root_dir",
        "-parser_fw",
        "-parser_fw_version",
        "-platform_name",
        "-tool_name",
        "-tool_version",
        "-uuid",
        "BugInstance",
        "BugSummary",
        "Metric",
        "MetricSummaries",
        "Heuristics"
})
public class AnalyzerReport implements Serializable {

    private final static long serialVersionUID = 4656075134214578571L;
    /**
     * The -assess_fw Schema
     * <p>
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assess_fw")
    @JsonProperty("-assess_fw")
    private String assessFw = "";
    /**
     * The -assess_fw_version Schema
     * <p>
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assess_fw_version")
    @JsonProperty("-assess_fw_version")
    private String assessFwVersion = "";
    /**
     * The -assessment_start_ts Schema
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assessment_start_ts")
    @JsonProperty("-assessment_start_ts")
    private String assessmentStartTs = "";
    /**
     * The -package_name Schema
     * <p>
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_fw")
    @JsonProperty("-build_fw")
    private String buildFw = "";
    /**
     * The -package_name Schema
     * <p>
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_fw_version")
    @JsonProperty("-build_fw_version")
    private String buildFwVersion = "";
    /**
     * The -package_name Schema
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "package_name")
    @JsonProperty("-package_name")
    private String packageName = "";
    /**
     * The -package_version Schema
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "package_version")
    @JsonProperty("-package_version")
    private String packageVersion = "";
    /**
     * The -build_root_dir Schema
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_root_dir")
    @JsonProperty("-build_root_dir")
    private String buildRootDir = "";
    /**
     * The -package_root_dir Schema
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-package_root_dir")
    @JacksonXmlProperty(isAttribute = true, localName = "package_root_dir")
    private String packageRootDir = "";
    /**
     * The -parser_fw Schema
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-parser_fw")
    @JacksonXmlProperty(isAttribute = true, localName = "parser_fw")
    private String parserFw = "";
    /**
     * The -parser_fw_version Schema
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-parser_fw_version")
    @JacksonXmlProperty(isAttribute = true, localName = "parser_fw_version")
    private String parserFwVersion = "";
    /**
     * The -platform_name Schema
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-platform_name")
    @JacksonXmlProperty(isAttribute = true, localName = "platform_name")
    private String platformName = "";
    /**
     * The -tool_name Schema
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-tool_name")
    @JacksonXmlProperty(isAttribute = true, localName = "tool_name")
    private String toolName = "";
    /**
     * The -tool_version Schema
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-tool_version")
    @JacksonXmlProperty(isAttribute = true, localName = "tool_version")
    private String toolVersion = "";
    /**
     * The -uuid Schema
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-uuid")
    @JacksonXmlProperty(isAttribute = true, localName = "uuid")
    private String uuid = "";
    /**
     * BugInstance
     * <p>
     */
    @JsonProperty("BugInstance")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<BugInstance> bugInstance = null;
    /**
     * BugCategory
     * <p>
     */
    @JacksonXmlElementWrapper(localName = "BugSummary")
    @JsonProperty("BugCategory")
    private List<BugCategory> bugCategory = null;
    /**
     * Metric
     * <p>
     */
    @JsonProperty("Metric")
    private List<Metric> metric = null;
    /**
     * MetricSummaryType
     * <p>
     */
    @JsonProperty("MetricSummaries")
    private MetricSummaries metricSummaries;
    /**
     * HeuristicsType
     * <p>
     */
    @JsonProperty("Heuristics")
    private Heuristics heuristics;

    /**
     * No args constructor for use in serialization
     */
    public AnalyzerReport() {
    }

    /**
     * <p>Constructor for AnalyzerReport.</p>
     *
     * @param buildFw           a {@link java.lang.String} object.
     * @param assessFw          a {@link java.lang.String} object.
     * @param packageName       a {@link java.lang.String} object.
     * @param toolVersion       a {@link java.lang.String} object.
     * @param buildFwVersion    a {@link java.lang.String} object.
     * @param parserFw          a {@link java.lang.String} object.
     * @param toolName          a {@link java.lang.String} object.
     * @param bugCategory       a {@link java.util.List} object.
     * @param bugInstance       a {@link java.util.List} object.
     * @param assessmentStartTs a {@link java.lang.String} object.
     * @param packageVersion    a {@link java.lang.String} object.
     * @param metricSummaries   a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
     * @param platformName      a {@link java.lang.String} object.
     * @param metric            a {@link java.util.List} object.
     * @param buildRootDir      a {@link java.lang.String} object.
     * @param uuid              a {@link java.lang.String} object.
     * @param packageRootDir    a {@link java.lang.String} object.
     * @param parserFwVersion   a {@link java.lang.String} object.
     * @param assessFwVersion   a {@link java.lang.String} object.
     * @param heuristics        a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics} object.
     */
    public AnalyzerReport(String assessFw, String assessFwVersion, String assessmentStartTs, String buildFw, String buildFwVersion, String packageName, String packageVersion, String buildRootDir, String packageRootDir, String parserFw, String parserFwVersion, String platformName, String toolName, String toolVersion, String uuid, List<BugInstance> bugInstance, List<BugCategory> bugCategory, List<Metric> metric, MetricSummaries metricSummaries, Heuristics heuristics) {
        super();
        this.assessFw = assessFw;
        this.assessFwVersion = assessFwVersion;
        this.assessmentStartTs = assessmentStartTs;
        this.buildFw = buildFw;
        this.buildFwVersion = buildFwVersion;
        this.packageName = packageName;
        this.packageVersion = packageVersion;
        this.buildRootDir = buildRootDir;
        this.packageRootDir = packageRootDir;
        this.parserFw = parserFw;
        this.parserFwVersion = parserFwVersion;
        this.platformName = platformName;
        this.toolName = toolName;
        this.toolVersion = toolVersion;
        this.uuid = uuid;
        this.bugInstance = bugInstance;
        this.bugCategory = bugCategory;
        this.metric = metric;
        this.metricSummaries = metricSummaries;
        this.heuristics = heuristics;
    }

    /**
     * <p>deserialize.</p>
     *
     * @param file a {@link java.io.File} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static AnalyzerReport deserialize(File file) throws ExceptionHandler {
        try {

            ObjectMapper deserializer = Listing.ScarfXML.getJacksonType().getOutputMapper();

            return deserializer.readValue(file, AnalyzerReport.class);

        } catch (IOException e) {
            throw new ExceptionHandler("Issue de-serializing file: " + file.getName(), ExceptionId.FILE_READ);
        }

    }

    /**
     * The -assess_fw Schema
     * <p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assess_fw")
    @JsonProperty("-assess_fw")
    public String getAssessFw() {
        return assessFw;
    }

    /**
     * The -assess_fw Schema
     * <p>
     *
     * @param assessFw a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assess_fw")
    @JsonProperty("-assess_fw")
    public void setAssessFw(String assessFw) {
        this.assessFw = assessFw;
    }

    /**
     * <p>withAssessFw.</p>
     *
     * @param assessFw a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withAssessFw(String assessFw) {
        this.assessFw = assessFw;
        return this;
    }

    /**
     * The -assess_fw_version Schema
     * <p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assess_fw_version")
    @JsonProperty("-assess_fw_version")
    public String getAssessFwVersion() {
        return assessFwVersion;
    }

    /**
     * The -assess_fw_version Schema
     * <p>
     *
     * @param assessFwVersion a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assess_fw_version")
    @JsonProperty("-assess_fw_version")
    public void setAssessFwVersion(String assessFwVersion) {
        this.assessFwVersion = assessFwVersion;
    }

    /**
     * <p>withAssessFwVersion.</p>
     *
     * @param assessFwVersion a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withAssessFwVersion(String assessFwVersion) {
        this.assessFwVersion = assessFwVersion;
        return this;
    }

    /**
     * The -assessment_start_ts Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assessment_start_ts")
    @JsonProperty("-assessment_start_ts")
    public String getAssessmentStartTs() {
        return assessmentStartTs;
    }

    /**
     * The -assessment_start_ts Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param assessmentStartTs a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "assessment_start_ts")
    @JsonProperty("-assessment_start_ts")
    public void setAssessmentStartTs(String assessmentStartTs) {
        this.assessmentStartTs = assessmentStartTs;
    }

    /**
     * <p>withAssessmentStartTs.</p>
     *
     * @param assessmentStartTs a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withAssessmentStartTs(String assessmentStartTs) {
        this.assessmentStartTs = assessmentStartTs;
        return this;
    }

    /**
     * The -package_name Schema
     * <p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_fw")
    @JsonProperty("-build_fw")
    public String getBuildFw() {
        return buildFw;
    }

    /**
     * The -package_name Schema
     * <p>
     *
     * @param buildFw a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_fw")
    @JsonProperty("-build_fw")
    public void setBuildFw(String buildFw) {
        this.buildFw = buildFw;
    }

    /**
     * <p>withBuildFw.</p>
     *
     * @param buildFw a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withBuildFw(String buildFw) {
        this.buildFw = buildFw;
        return this;
    }

    /**
     * The -package_name Schema
     * <p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-build_fw_version")
    @JacksonXmlProperty(isAttribute = true, localName = "build_fw_version")
    public String getBuildFwVersion() {
        return buildFwVersion;
    }

    /**
     * The -package_name Schema
     * <p>
     *
     * @param buildFwVersion a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_fw_version")
    @JsonProperty("-build_fw_version")
    public void setBuildFwVersion(String buildFwVersion) {
        this.buildFwVersion = buildFwVersion;
    }

    /**
     * <p>withBuildFwVersion.</p>
     *
     * @param buildFwVersion a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withBuildFwVersion(String buildFwVersion) {
        this.buildFwVersion = buildFwVersion;
        return this;
    }

    /**
     * The -package_name Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "package_name")
    @JsonProperty("-package_name")
    public String getPackageName() {
        return packageName;
    }

    /**
     * The -package_name Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param packageName a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "package_name")
    @JsonProperty("-package_name")
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * <p>withPackageName.</p>
     *
     * @param packageName a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * The -package_version Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "package_version")
    @JsonProperty("-package_version")
    public String getPackageVersion() {
        return packageVersion;
    }

    /**
     * The -package_version Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param packageVersion a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "package_version")
    @JsonProperty("-package_version")
    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    /**
     * <p>withPackageVersion.</p>
     *
     * @param packageVersion a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
        return this;
    }

    /**
     * The -build_root_dir Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_root_dir")
    @JsonProperty("-build_root_dir")
    public String getBuildRootDir() {
        return buildRootDir;
    }

    /**
     * The -build_root_dir Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param buildRootDir a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "build_root_dir")
    @JsonProperty("-build_root_dir")
    public void setBuildRootDir(String buildRootDir) {
        this.buildRootDir = buildRootDir;
    }

    /**
     * <p>withBuildRootDir.</p>
     *
     * @param buildRootDir a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withBuildRootDir(String buildRootDir) {
        this.buildRootDir = buildRootDir;
        return this;
    }

    /**
     * The -package_root_dir Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-package_root_dir")
    @JacksonXmlProperty(isAttribute = true, localName = "package_root_dir")
    public String getPackageRootDir() {
        return packageRootDir;
    }

    /**
     * The -package_root_dir Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param packageRootDir a {@link java.lang.String} object.
     */
    @JsonProperty("-package_root_dir")
    @JacksonXmlProperty(isAttribute = true, localName = "package_root_dir")
    public void setPackageRootDir(String packageRootDir) {
        this.packageRootDir = packageRootDir;
    }

    /**
     * <p>withPackageRootDir.</p>
     *
     * @param packageRootDir a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withPackageRootDir(String packageRootDir) {
        this.packageRootDir = packageRootDir;
        return this;
    }

    /**
     * The -parser_fw Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-parser_fw")
    @JacksonXmlProperty(isAttribute = true, localName = "parser_fw")
    public String getParserFw() {
        return parserFw;
    }

    /**
     * The -parser_fw Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param parserFw a {@link java.lang.String} object.
     */
    @JsonProperty("-parser_fw")
    @JacksonXmlProperty(isAttribute = true, localName = "parser_fw")
    public void setParserFw(String parserFw) {
        this.parserFw = parserFw;
    }

    /**
     * <p>withParserFw.</p>
     *
     * @param parserFw a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withParserFw(String parserFw) {
        this.parserFw = parserFw;
        return this;
    }

    /**
     * The -parser_fw_version Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-parser_fw_version")
    @JacksonXmlProperty(isAttribute = true, localName = "parser_fw_version")
    public String getParserFwVersion() {
        return parserFwVersion;
    }

    /**
     * The -parser_fw_version Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param parserFwVersion a {@link java.lang.String} object.
     */
    @JsonProperty("-parser_fw_version")
    @JacksonXmlProperty(isAttribute = true, localName = "parser_fw_version")
    public void setParserFwVersion(String parserFwVersion) {
        this.parserFwVersion = parserFwVersion;
    }

    /**
     * <p>withParserFwVersion.</p>
     *
     * @param parserFwVersion a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withParserFwVersion(String parserFwVersion) {
        this.parserFwVersion = parserFwVersion;
        return this;
    }

    /**
     * The -platform_name Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-platform_name")
    @JacksonXmlProperty(isAttribute = true, localName = "platform_name")
    public String getPlatformName() {
        return platformName;
    }

    /**
     * The -platform_name Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param platformName a {@link java.lang.String} object.
     */
    @JsonProperty("-platform_name")
    @JacksonXmlProperty(isAttribute = true, localName = "platform_name")
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * <p>withPlatformName.</p>
     *
     * @param platformName a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withPlatformName(String platformName) {
        this.platformName = platformName;
        return this;
    }

    /**
     * The -tool_name Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-tool_name")
    @JacksonXmlProperty(isAttribute = true, localName = "tool_name")
    public String getToolName() {
        return toolName;
    }

    /**
     * The -tool_name Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param toolName a {@link java.lang.String} object.
     */
    @JsonProperty("-tool_name")
    @JacksonXmlProperty(isAttribute = true, localName = "tool_name")
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    /**
     * <p>withToolName.</p>
     *
     * @param toolName a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withToolName(String toolName) {
        this.toolName = toolName;
        return this;
    }

    /**
     * The -tool_version Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-tool_version")
    @JacksonXmlProperty(isAttribute = true, localName = "tool_version")
    public String getToolVersion() {
        return toolVersion;
    }

    /**
     * The -tool_version Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param toolVersion a {@link java.lang.String} object.
     */
    @JsonProperty("-tool_version")
    @JacksonXmlProperty(isAttribute = true, localName = "tool_version")
    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    /**
     * <p>withToolVersion.</p>
     *
     * @param toolVersion a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
        return this;
    }

    /**
     * The -uuid Schema
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("-uuid")
    @JacksonXmlProperty(isAttribute = true, localName = "uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * The -uuid Schema
     * <p>
     * <p>
     * (Required)
     *
     * @param uuid a {@link java.lang.String} object.
     */
    @JsonProperty("-uuid")
    @JacksonXmlProperty(isAttribute = true, localName = "uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * <p>withUuid.</p>
     *
     * @param uuid a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /**
     * BugInstance
     * <p>
     *
     * @return a {@link java.util.List} object.
     */
    @JsonProperty("BugInstance")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<BugInstance> getBugInstance() {
        if (this.bugInstance == null)
            this.bugInstance = new ArrayList<>();
        return bugInstance;
    }

    /**
     * BugInstance
     * <p>
     *
     * @param bugInstance a {@link java.util.List} object.
     */
    @JsonProperty("BugInstance")
    @JacksonXmlElementWrapper(useWrapping = false)
    public void setBugInstance(List<BugInstance> bugInstance) {
        this.bugInstance = bugInstance;
    }

    /**
     * <p>withBugInstance.</p>
     *
     * @param bugInstance a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withBugInstance(List<BugInstance> bugInstance) {
        this.bugInstance = bugInstance;
        return this;
    }

    /**
     * BugCategory
     * <p>
     *
     * @return a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(localName = "BugSummary")
    @JsonProperty("BugCategory")
    public List<BugCategory> getBugCategory() {
        return bugCategory;
    }

    /**
     * BugCategory
     * <p>
     *
     * @param bugCategory a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(localName = "BugSummary")
    @JsonProperty("BugCategory")
    public void setBugCategory(List<BugCategory> bugCategory) {
        this.bugCategory = bugCategory;
    }

    /**
     * <p>withBugCategory.</p>
     *
     * @param bugCategory a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withBugCategory(List<BugCategory> bugCategory) {
        this.bugCategory = bugCategory;
        return this;
    }

    /**
     * Metric
     * <p>
     *
     * @return a {@link java.util.List} object.
     */
    @JsonProperty("Metric")
    public List<Metric> getMetric() {
        if (this.metric == null)
            this.metric = new ArrayList<>();
        return metric;
    }

    /**
     * Metric
     * <p>
     *
     * @param metric a {@link java.util.List} object.
     */
    @JsonProperty("Metric")
    public void setMetric(List<Metric> metric) {
        this.metric = metric;
    }

    /**
     * <p>withMetric.</p>
     *
     * @param metric a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withMetric(List<Metric> metric) {
        this.metric = metric;
        return this;
    }

    /**
     * MetricSummaryType
     * <p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
     */
    @JsonProperty("MetricSummaries")
    public MetricSummaries getMetricSummaries() {
        return metricSummaries;
    }

    /**
     * MetricSummaryType
     * <p>
     *
     * @param metricSummaries a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
     */
    @JsonProperty("MetricSummaries")
    public void setMetricSummaries(MetricSummaries metricSummaries) {
        this.metricSummaries = metricSummaries;
    }

    /**
     * <p>withMetricSummaries.</p>
     *
     * @param metricSummaries a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withMetricSummaries(MetricSummaries metricSummaries) {
        this.metricSummaries = metricSummaries;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics} object.
     */
    @JsonProperty("Heuristics")
    public Heuristics getHeuristics() {
        return heuristics;
    }

    /**
     * HeuristicsType
     * <p>
     *
     * @param heuristics a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics} object.
     */
    @JsonProperty("Heuristics")
    public void setHeuristics(Heuristics heuristics) {
        this.heuristics = heuristics;
    }

    /**
     * <p>withHeuristics.</p>
     *
     * @param heuristics a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     */
    public AnalyzerReport withHeuristics(Heuristics heuristics) {
        this.heuristics = heuristics;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("assessFw", assessFw).append("assessFwVersion", assessFwVersion).append("assessmentStartTs", assessmentStartTs).append("buildFw", buildFw).append("buildFwVersion", buildFwVersion).append("packageName", packageName).append("packageVersion", packageVersion).append("buildRootDir", buildRootDir).append("packageRootDir", packageRootDir).append("parserFw", parserFw).append("parserFwVersion", parserFwVersion).append("platformName", platformName).append("toolName", toolName).append("toolVersion", toolVersion).append("uuid", uuid).append("bugInstance", bugInstance).append("bugCategory", bugCategory).append("metric", metric).append("metricSummaries", metricSummaries).append("heuristics", heuristics).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(buildFw).append(bugCategory).append(packageName).append(assessFw).append(toolVersion).append(buildFwVersion).append(parserFw).append(toolName).append(bugInstance).append(packageVersion).append(assessmentStartTs).append(metricSummaries).append(platformName).append(metric).append(heuristics).append(buildRootDir).append(uuid).append(parserFwVersion).append(packageRootDir).append(assessFwVersion).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AnalyzerReport) == false) {
            return false;
        }
        AnalyzerReport rhs = ((AnalyzerReport) other);
        return new EqualsBuilder().append(buildFw, rhs.buildFw).append(bugCategory, rhs.bugCategory).append(packageName, rhs.packageName).append(assessFw, rhs.assessFw).append(toolVersion, rhs.toolVersion).append(buildFwVersion, rhs.buildFwVersion).append(parserFw, rhs.parserFw).append(toolName, rhs.toolName).append(bugInstance, rhs.bugInstance).append(packageVersion, rhs.packageVersion).append(assessmentStartTs, rhs.assessmentStartTs).append(metricSummaries, rhs.metricSummaries).append(platformName, rhs.platformName).append(metric, rhs.metric).append(heuristics, rhs.heuristics).append(buildRootDir, rhs.buildRootDir).append(uuid, rhs.uuid).append(parserFwVersion, rhs.parserFwVersion).append(packageRootDir, rhs.packageRootDir).append(assessFwVersion, rhs.assessFwVersion).isEquals();
    }

}

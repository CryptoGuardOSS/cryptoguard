package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Report
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "-SchemaVersion",
        "-DateTime",
        "-UUID",
        "ProjectName",
        "ProjectVersion",
        "Target",
        "Issues"
})
public class Report implements Serializable {

    /**
     * The Schema Version
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
    @JsonProperty("_SchemaVersion")
    private int schemaVersion;
    /**
     * Time the scan was instantiated
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "DateTime")
    @JsonProperty("_DateTime")
    private String dateTime;
    /**
     * string Time the scan was instantiated
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "UUID")
    @JsonProperty("_UUID")
    private String uUID;
    /**
     * The Project Name
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectName")
    private String projectName;
    /**
     * The Project Version
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectVersion")
    private String projectVersion;
    /**
     * TargetInfo
     * <p>
     */
    @JsonProperty("Target")
    private Target target;
    /**
     * Issues
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("Issues")
    private List<Issue> issues = new ArrayList<Issue>();
    private final static long serialVersionUID = 1588567781296372468L;

    /**
     * No args constructor for use in serialization
     */
    public Report() {
    }

    /**
     * @param projectVersion
     * @param dateTime
     * @param issues
     * @param target
     * @param uUID
     * @param projectName
     * @param schemaVersion
     */
    public Report(int schemaVersion, String dateTime, String uUID, String projectName, String projectVersion, Target target, List<Issue> issues) {
        super();
        this.schemaVersion = schemaVersion;
        this.dateTime = dateTime;
        this.uUID = uUID;
        this.projectName = projectName;
        this.projectVersion = projectVersion;
        this.target = target;
        this.issues = issues;
    }

    /**
     * The Schema Version
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
    @JsonProperty("_SchemaVersion")
    public int getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * The Schema Version
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
    @JsonProperty("_SchemaVersion")
    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public Report withSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
        return this;
    }

    /**
     * Time the scan was instantiated
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "DateTime")
    @JsonProperty("_DateTime")
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Time the scan was instantiated
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "DateTime")
    @JsonProperty("_DateTime")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Report withDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    /**
     * string Time the scan was instantiated
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "UUID")
    @JsonProperty("_UUID")
    public String getUUID() {
        return uUID;
    }

    /**
     * string Time the scan was instantiated
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "UUID")
    @JsonProperty("_UUID")
    public void setUUID(String uUID) {
        this.uUID = uUID;
    }

    public Report withUUID(String uUID) {
        this.uUID = uUID;
        return this;
    }

    /**
     * The Project Name
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectName")
    public String getProjectName() {
        return projectName;
    }

    /**
     * The Project Name
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectName")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Report withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    /**
     * The Project Version
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectVersion")
    public String getProjectVersion() {
        return projectVersion;
    }

    /**
     * The Project Version
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectVersion")
    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public Report withProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
        return this;
    }

    /**
     * TargetInfo
     * <p>
     */
    @JsonProperty("Target")
    public Target getTarget() {
        return target;
    }

    /**
     * TargetInfo
     * <p>
     */
    @JsonProperty("Target")
    public void setTarget(Target target) {
        this.target = target;
    }

    public Report withTarget(Target target) {
        this.target = target;
        return this;
    }

    /**
     * Issues
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("Issues")
    public List<Issue> getIssues() {
        return issues;
    }

    /**
     * Issues
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("Issues")
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public Report withIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("schemaVersion", schemaVersion).append("dateTime", dateTime).append("uUID", uUID).append("projectName", projectName).append("projectVersion", projectVersion).append("target", target).append("issues", issues).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(projectVersion).append(dateTime).append(issues).append(target).append(uUID).append(projectName).append(schemaVersion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Report) == false) {
            return false;
        }
        Report rhs = ((Report) other);
        return new EqualsBuilder().append(projectVersion, rhs.projectVersion).append(dateTime, rhs.dateTime).append(issues, rhs.issues).append(target, rhs.target).append(uUID, rhs.uUID).append(projectName, rhs.projectName).append(schemaVersion, rhs.schemaVersion).isEquals();
    }

    /**
     * <p>deserialize.</p>
     *
     * @param file a {@link java.io.File} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static Report deserialize(File file) throws ExceptionHandler {
        try {

            ObjectMapper deserializer = Listing.Default.getJacksonType().getOutputMapper();

            return deserializer.readValue(file, Report.class);

        } catch (IOException e) {
            throw new ExceptionHandler("Issue de-serializing file: " + file.getName(), ExceptionId.FILE_READ);
        }

    }

}

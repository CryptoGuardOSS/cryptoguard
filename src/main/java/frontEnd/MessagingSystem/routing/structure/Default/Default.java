
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The Default Schema
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ProjectName",
        "ProjectVersion",
        "SchemaVersion",
        "TargetSources",
        "-meta",
        "Issues"
})
public class Default implements Serializable {

    /**
     * The Project Name
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectName")
    private String projectName = "";
    /**
     * The Project Version
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("ProjectVersion")
    private String projectVersion = "";
    /**
     * The Schema Version
     * <p>
     */
    @JsonProperty("SchemaVersion")
    private String schemaVersion = "";
    /**
     * The Sources of the Target
     * <p>
     */
    @JsonProperty("TargetSources")
    private List<String> targetSources = new ArrayList<String>();
    /**
     * MetaData
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-meta")
    private Meta meta;
    /**
     * Issues
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("Issues")
    private List<Issue> issues = new ArrayList<Issue>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4617667662005254566L;

    /**
     * No args constructor for use in serialization
     */
    public Default() {
    }

    /**
     * @param projectVersion
     * @param targetSources
     * @param issues
     * @param schemaVersion
     * @param projectName
     * @param meta
     */
    public Default(String projectName, String projectVersion, String schemaVersion, List<String> targetSources, Meta meta, List<Issue> issues) {
        super();
        this.projectName = projectName;
        this.projectVersion = projectVersion;
        this.schemaVersion = schemaVersion;
        this.targetSources = targetSources;
        this.meta = meta;
        this.issues = issues;
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

    public Default withProjectName(String projectName) {
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

    public Default withProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
        return this;
    }

    /**
     * The Schema Version
     * <p>
     */
    @JsonProperty("SchemaVersion")
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * The Schema Version
     * <p>
     */
    @JsonProperty("SchemaVersion")
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public Default withSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
        return this;
    }

    /**
     * The Sources of the Target
     * <p>
     */
    @JsonProperty("TargetSources")
    public List<String> getTargetSources() {
        return targetSources;
    }

    /**
     * The Sources of the Target
     * <p>
     */
    @JsonProperty("TargetSources")
    public void setTargetSources(List<String> targetSources) {
        this.targetSources = targetSources;
    }

    public Default withTargetSources(List<String> targetSources) {
        this.targetSources = targetSources;
        return this;
    }

    /**
     * MetaData
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-meta")
    public Meta getMeta() {
        return meta;
    }

    /**
     * MetaData
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Default withMeta(Meta meta) {
        this.meta = meta;
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

    public Default withIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Default withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("projectName", projectName).append("projectVersion", projectVersion).append("schemaVersion", schemaVersion).append("targetSources", targetSources).append("meta", meta).append("issues", issues).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(projectVersion).append(targetSources).append(additionalProperties).append(issues).append(schemaVersion).append(projectName).append(meta).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Default) == false) {
            return false;
        }
        Default rhs = ((Default) other);
        return new EqualsBuilder().append(projectVersion, rhs.projectVersion).append(targetSources, rhs.targetSources).append(additionalProperties, rhs.additionalProperties).append(issues, rhs.issues).append(schemaVersion, rhs.schemaVersion).append(projectName, rhs.projectName).append(meta, rhs.meta).isEquals();
    }

}

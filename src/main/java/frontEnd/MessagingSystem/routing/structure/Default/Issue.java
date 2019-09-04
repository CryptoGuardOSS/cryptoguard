package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * Issue
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "-FullPath",
        "-Id",
        "Message",
        "Description",
        "RuleNumber",
        "RuleDesc",
        "CWEId",
        "Severity",
        "Location"
})
public class Issue implements Serializable {

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "FullPath")
    @JsonProperty("_FullPath")
    private String fullPath;
    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    @JsonProperty("_Id")
    private String id;
    /**
     * (Required)
     */
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Description")
    private String description;
    /**
     * (Required)
     */
    @JsonProperty("RuleNumber")
    private int ruleNumber;
    @JsonProperty("RuleDesc")
    private String ruleDesc;
    /**
     * (Required)
     */
    @JsonProperty("CWEId")
    private int cWEId;
    /**
     * (Required)
     */
    @JsonProperty("Severity")
    private String severity;
    /**
     * LocationType
     * <p>
     */
    @JsonProperty("Location")
    private Location location;
    private final static long serialVersionUID = -2265209324602378101L;

    /**
     * No args constructor for use in serialization
     */
    public Issue() {
    }

    /**
     * @param message
     * @param id
     * @param location
     * @param ruleNumber
     * @param description
     * @param ruleDesc
     * @param severity
     * @param cWEId
     * @param fullPath
     */
    public Issue(String fullPath, String id, String message, String description, int ruleNumber, String ruleDesc, int cWEId, String severity, Location location) {
        super();
        this.fullPath = fullPath;
        this.id = id;
        this.message = message;
        this.description = description;
        this.ruleNumber = ruleNumber;
        this.ruleDesc = ruleDesc;
        this.cWEId = cWEId;
        this.severity = severity;
        this.location = location;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "FullPath")
    @JsonProperty("_FullPath")
    public String getFullPath() {
        return fullPath;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "FullPath")
    @JsonProperty("_FullPath")
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Issue withFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    @JsonProperty("_Id")
    public String getId() {
        return id;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    @JsonProperty("_Id")
    public void setId(String id) {
        this.id = id;
    }

    public Issue withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }

    /**
     * (Required)
     */
    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    public Issue withMessage(String message) {
        this.message = message;
        return this;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Issue withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("RuleNumber")
    public int getRuleNumber() {
        return ruleNumber;
    }

    /**
     * (Required)
     */
    @JsonProperty("RuleNumber")
    public void setRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public Issue withRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
        return this;
    }

    @JsonProperty("RuleDesc")
    public String getRuleDesc() {
        return ruleDesc;
    }

    @JsonProperty("RuleDesc")
    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public Issue withRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("CWEId")
    public int getCWEId() {
        return cWEId;
    }

    /**
     * (Required)
     */
    @JsonProperty("CWEId")
    public void setCWEId(int cWEId) {
        this.cWEId = cWEId;
    }

    public Issue withCWEId(int cWEId) {
        this.cWEId = cWEId;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Severity")
    public String getSeverity() {
        return severity;
    }

    /**
     * (Required)
     */
    @JsonProperty("Severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    public Issue withSeverity(String severity) {
        this.severity = severity;
        return this;
    }

    /**
     * LocationType
     * <p>
     */
    @JsonProperty("Location")
    public Location getLocation() {
        return location;
    }

    /**
     * LocationType
     * <p>
     */
    @JsonProperty("Location")
    public void setLocation(Location location) {
        this.location = location;
    }

    public Issue withLocation(Location location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("fullPath", fullPath).append("id", id).append("message", message).append("description", description).append("ruleNumber", ruleNumber).append("ruleDesc", ruleDesc).append("cWEId", cWEId).append("severity", severity).append("location", location).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(message).append(id).append(location).append(ruleNumber).append(description).append(ruleDesc).append(severity).append(cWEId).append(fullPath).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Issue) == false) {
            return false;
        }
        Issue rhs = ((Issue) other);
        return new EqualsBuilder().append(message, rhs.message).append(id, rhs.id).append(location, rhs.location).append(ruleNumber, rhs.ruleNumber).append(description, rhs.description).append(ruleDesc, rhs.ruleDesc).append(severity, rhs.severity).append(cWEId, rhs.cWEId).append(fullPath, rhs.fullPath).isEquals();
    }

}

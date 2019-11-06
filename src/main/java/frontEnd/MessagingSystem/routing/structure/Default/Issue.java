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
 *
 * @author franceme
 * @version 03.07.01
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

    private final static long serialVersionUID = -2265209324602378101L;
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

    /**
     * No args constructor for use in serialization
     */
    public Issue() {
    }

    /**
     * <p>Constructor for Issue.</p>
     *
     * @param message     a {@link java.lang.String} object.
     * @param id          a {@link java.lang.String} object.
     * @param location    a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     * @param ruleNumber  a int.
     * @param description a {@link java.lang.String} object.
     * @param ruleDesc    a {@link java.lang.String} object.
     * @param severity    a {@link java.lang.String} object.
     * @param cWEId       a int.
     * @param fullPath    a {@link java.lang.String} object.
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
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "FullPath")
    @JsonProperty("_FullPath")
    public String getFullPath() {
        return fullPath;
    }

    /**
     * (Required)
     *
     * @param fullPath a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "FullPath")
    @JsonProperty("_FullPath")
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    /**
     * <p>withFullPath.</p>
     *
     * @param fullPath a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    @JsonProperty("_Id")
    public String getId() {
        return id;
    }

    /**
     * (Required)
     *
     * @param id a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    @JsonProperty("_Id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p>withId.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }

    /**
     * (Required)
     *
     * @param message a {@link java.lang.String} object.
     */
    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * <p>withMessage.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>withDescription.</p>
     *
     * @param description a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JsonProperty("RuleNumber")
    public int getRuleNumber() {
        return ruleNumber;
    }

    /**
     * (Required)
     *
     * @param ruleNumber a int.
     */
    @JsonProperty("RuleNumber")
    public void setRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    /**
     * <p>withRuleNumber.</p>
     *
     * @param ruleNumber a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
        return this;
    }

    /**
     * <p>Getter for the field <code>ruleDesc</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("RuleDesc")
    public String getRuleDesc() {
        return ruleDesc;
    }

    /**
     * <p>Setter for the field <code>ruleDesc</code>.</p>
     *
     * @param ruleDesc a {@link java.lang.String} object.
     */
    @JsonProperty("RuleDesc")
    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    /**
     * <p>withRuleDesc.</p>
     *
     * @param ruleDesc a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
        return this;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JsonProperty("CWEId")
    public int getCWEId() {
        return cWEId;
    }

    /**
     * (Required)
     *
     * @param cWEId a int.
     */
    @JsonProperty("CWEId")
    public void setCWEId(int cWEId) {
        this.cWEId = cWEId;
    }

    /**
     * <p>withCWEId.</p>
     *
     * @param cWEId a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withCWEId(int cWEId) {
        this.cWEId = cWEId;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Severity")
    public String getSeverity() {
        return severity;
    }

    /**
     * (Required)
     *
     * @param severity a {@link java.lang.String} object.
     */
    @JsonProperty("Severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * <p>withSeverity.</p>
     *
     * @param severity a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    public Issue withSeverity(String severity) {
        this.severity = severity;
        return this;
    }

    /**
     * LocationType
     * <p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    @JsonProperty("Location")
    public Location getLocation() {
        return location;
    }

    /**
     * LocationType
     * <p>
     *
     * @param location a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    @JsonProperty("Location")
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * <p>withLocation.</p>
     *
     * @param location a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
    public Issue withLocation(Location location) {
        this.location = location;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("fullPath", fullPath).append("id", id).append("message", message).append("description", description).append("ruleNumber", ruleNumber).append("ruleDesc", ruleDesc).append("cWEId", cWEId).append("severity", severity).append("location", location).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(message).append(id).append(location).append(ruleNumber).append(description).append(ruleDesc).append(severity).append(cWEId).append(fullPath).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
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

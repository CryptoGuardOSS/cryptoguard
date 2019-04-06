package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * BugInstanceType
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ClassName",
        "Method",
        "Location",
        "CweId",
        "BugGroup",
        "BugCode",
        "BugRank",
        "BugSeverity",
        "BugMessage",
        "ResolutionSuggestion",
        "BugTrace",
        "-id"
})
public class BugInstance implements Serializable {

    @JsonProperty("ClassName")
    private String className;
    /**
     * Method
     * <p>
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "Methods")
    @JsonProperty("Method")
    private List<Method> method = null;
    /**
     * Location
     * <p>
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "BugLocations")
    @JsonProperty("Location")
    private List<Location> location = null;
    /**
     * CweId
     * <p>
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("CweId")
    private List<String> cweId = null;
    @JsonProperty("BugGroup")
    private String bugGroup;
    /**
     * (Required)
     */
    @JsonProperty("BugCode")
    private String bugCode;
    @JsonProperty("BugRank")
    private int bugRank;
    @JsonProperty("BugSeverity")
    private int bugSeverity;
    @JsonProperty("BugMessage")
    private String bugMessage;
    @JsonProperty("ResolutionSuggestion")
    private String resolutionSuggestion;
    /**
     * BugTrace
     * <p>
     */
    @JsonProperty("BugTrace")
    private BugTrace bugTrace;
    /**
     * id
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private int id = 0;
    private final static long serialVersionUID = -3880439320289849435L;

    /**
     * No args constructor for use in serialization
     */
    public BugInstance() {
    }

    /**
     * @param id
     * @param bugCode
     * @param bugTrace
     * @param bugSeverity
     * @param bugRank
     * @param bugMessage
     * @param cweId
     * @param method
     * @param className
     * @param bugGroup
     * @param resolutionSuggestion
     * @param location
     */
    public BugInstance(String className, List<Method> method, List<Location> location, List<String> cweId, String bugGroup, String bugCode, int bugRank, int bugSeverity, String bugMessage, String resolutionSuggestion, BugTrace bugTrace, int id) {
        super();
        this.className = className;
        this.method = method;
        this.location = location;
        this.cweId = cweId;
        this.bugGroup = bugGroup;
        this.bugCode = bugCode;
        this.bugRank = bugRank;
        this.bugSeverity = bugSeverity;
        this.bugMessage = bugMessage;
        this.resolutionSuggestion = resolutionSuggestion;
        this.bugTrace = bugTrace;
        this.id = id;
    }

    @JsonProperty("ClassName")
    public String getClassName() {
        return className;
    }

    @JsonProperty("ClassName")
    public void setClassName(String className) {
        this.className = className;
    }

    public BugInstance withClassName(String className) {
        this.className = className;
        return this;
    }

    /**
     * Method
     * <p>
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "Methods")
    @JsonProperty("Method")
    public List<Method> getMethod() {
        if (this.method == null)
            this.method = new ArrayList<>();
        return method;
    }

    public void addMethod(Method newMethod) {
        if (this.method == null)
            this.method = new ArrayList<>();
        this.method.add(newMethod);
    }

    /**
     * Method
     * <p>
     */
    @JsonProperty("Method")
    @JacksonXmlElementWrapper(useWrapping = true, localName = "Methods")
    public void setMethod(List<Method> method) {
        this.method = method;
    }

    public BugInstance withMethod(List<Method> method) {
        this.method = method;
        return this;
    }

    /**
     * BugLocations
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "BugLocations")
    @JsonProperty("Location")
    public List<Location> getlocation() {
        if (this.location == null)
            this.location = new ArrayList<>();
        return location;
    }

    public void addBugLocation(Location location) {
        if (this.location == null)
            this.location = new ArrayList<>();
        this.location.add(location);
    }

    /**
     * BugLocations
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "BugLocations")
    @JsonProperty("Location")
    public void setLocation(List<Location> locations) {
        this.location = locations;
    }

    public BugInstance withBugLocations(List<Location> bugLocations) {
        this.location = bugLocations;
        return this;
    }

    /**
     * CweId
     * <p>
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("CweId")
    public List<String> getCweId() {
        if (this.cweId == null)
            this.cweId = new ArrayList<>();
        return cweId;
    }

    /**
     * CweId
     * <p>
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("CweId")
    public void setCweId(List<String> cweId) {
        this.cweId = cweId;
    }

    public BugInstance withCweId(List<String> cweId) {
        this.cweId = cweId;
        return this;
    }

    @JsonProperty("BugGroup")
    public String getBugGroup() {
        return bugGroup;
    }

    @JsonProperty("BugGroup")
    public void setBugGroup(String bugGroup) {
        this.bugGroup = bugGroup;
    }

    public BugInstance withBugGroup(String bugGroup) {
        this.bugGroup = bugGroup;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("BugCode")
    public String getBugCode() {
        return bugCode;
    }

    /**
     * (Required)
     */
    @JsonProperty("BugCode")
    public void setBugCode(String bugCode) {
        this.bugCode = bugCode;
    }

    public BugInstance withBugCode(String bugCode) {
        this.bugCode = bugCode;
        return this;
    }

    @JsonProperty("BugRank")
    public int getBugRank() {
        return bugRank;
    }

    @JsonProperty("BugRank")
    public void setBugRank(int bugRank) {
        this.bugRank = bugRank;
    }

    public BugInstance withBugRank(int bugRank) {
        this.bugRank = bugRank;
        return this;
    }

    @JsonProperty("BugSeverity")
    public int getBugSeverity() {
        return bugSeverity;
    }

    @JsonProperty("BugSeverity")
    public void setBugSeverity(int bugSeverity) {
        this.bugSeverity = bugSeverity;
    }

    public BugInstance withBugSeverity(int bugSeverity) {
        this.bugSeverity = bugSeverity;
        return this;
    }

    @JsonProperty("BugMessage")
    public String getBugMessage() {
        return bugMessage;
    }

    @JsonProperty("BugMessage")
    public void setBugMessage(String bugMessage) {
        this.bugMessage = bugMessage;
    }

    public BugInstance withBugMessage(String bugMessage) {
        this.bugMessage = bugMessage;
        return this;
    }

    @JsonProperty("ResolutionSuggestion")
    public String getResolutionSuggestion() {
        return resolutionSuggestion;
    }

    @JsonProperty("ResolutionSuggestion")
    public void setResolutionSuggestion(String resolutionSuggestion) {
        this.resolutionSuggestion = resolutionSuggestion;
    }

    public BugInstance withResolutionSuggestion(String resolutionSuggestion) {
        this.resolutionSuggestion = resolutionSuggestion;
        return this;
    }

    /**
     * BugTrace
     * <p>
     */
    @JsonProperty("BugTrace")
    public BugTrace getBugTrace() {
        return bugTrace;
    }

    /**
     * BugTrace
     * <p>
     */
    @JsonProperty("BugTrace")
    public void setBugTrace(BugTrace bugTrace) {
        this.bugTrace = bugTrace;
    }

    public BugInstance withBugTrace(BugTrace bugTrace) {
        this.bugTrace = bugTrace;
        return this;
    }

    /**
     * id
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public int getId() {
        return id;
    }

    /**
     * id
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public void setId(int id) {
        this.id = id;
    }

    public BugInstance withId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("className", className).append("method", method).append("location", location).append("cweId", cweId).append("bugGroup", bugGroup).append("bugCode", bugCode).append("bugRank", bugRank).append("bugSeverity", bugSeverity).append("bugMessage", bugMessage).append("resolutionSuggestion", resolutionSuggestion).append("bugTrace", bugTrace).append("id", id).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bugCode).append(bugTrace).append(bugSeverity).append(location).append(bugRank).append(bugMessage).append(resolutionSuggestion).append(id).append(cweId).append(method).append(className).append(bugGroup).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BugInstance) == false) {
            return false;
        }
        BugInstance rhs = ((BugInstance) other);
        return new EqualsBuilder().append(bugCode, rhs.bugCode).append(bugTrace, rhs.bugTrace).append(bugSeverity, rhs.bugSeverity).append(location, rhs.location).append(bugRank, rhs.bugRank).append(bugMessage, rhs.bugMessage).append(resolutionSuggestion, rhs.resolutionSuggestion).append(id, rhs.id).append(cweId, rhs.cweId).append(method, rhs.method).append(className, rhs.className).append(bugGroup, rhs.bugGroup).isEquals();
    }

}

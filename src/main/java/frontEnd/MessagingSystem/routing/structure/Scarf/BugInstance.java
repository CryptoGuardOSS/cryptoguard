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
 *
 * @author franceme
 * @version 03.07.01
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

    private final static long serialVersionUID = -3880439320289849435L;
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

    /**
     * No args constructor for use in serialization
     */
    public BugInstance() {
    }

    /**
     * <p>Constructor for BugInstance.</p>
     *
     * @param id                   a int.
     * @param bugCode              a {@link java.lang.String} object.
     * @param bugTrace             a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugTrace} object.
     * @param bugSeverity          a int.
     * @param bugRank              a int.
     * @param bugMessage           a {@link java.lang.String} object.
     * @param cweId                a {@link java.util.List} object.
     * @param method               a {@link java.util.List} object.
     * @param className            a {@link java.lang.String} object.
     * @param bugGroup             a {@link java.lang.String} object.
     * @param resolutionSuggestion a {@link java.lang.String} object.
     * @param location             a {@link java.util.List} object.
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

    /**
     * <p>Getter for the field <code>className</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("ClassName")
    public String getClassName() {
        return className;
    }

    /**
     * <p>Setter for the field <code>className</code>.</p>
     *
     * @param className a {@link java.lang.String} object.
     */
    @JsonProperty("ClassName")
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * <p>withClassName.</p>
     *
     * @param className a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withClassName(String className) {
        this.className = className;
        return this;
    }

    /**
     * Method
     * <p>
     *
     * @return a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "Methods")
    @JsonProperty("Method")
    public List<Method> getMethod() {
        if (this.method == null)
            this.method = new ArrayList<>();
        return method;
    }

    /**
     * Method
     * <p>
     *
     * @param method a {@link java.util.List} object.
     */
    @JsonProperty("Method")
    @JacksonXmlElementWrapper(useWrapping = true, localName = "Methods")
    public void setMethod(List<Method> method) {
        this.method = method;
    }

    /**
     * <p>addMethod.</p>
     *
     * @param newMethod a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Method} object.
     */
    public void addMethod(Method newMethod) {
        if (this.method == null)
            this.method = new ArrayList<>();
        this.method.add(newMethod);
    }

    /**
     * <p>withMethod.</p>
     *
     * @param method a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withMethod(List<Method> method) {
        this.method = method;
        return this;
    }

    /**
     * BugLocations
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "BugLocations")
    @JsonProperty("Location")
    public List<Location> getlocation() {
        if (this.location == null)
            this.location = new ArrayList<>();
        return location;
    }

    /**
     * <p>addBugLocation.</p>
     *
     * @param location a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Location} object.
     */
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
     *
     * @param locations a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(useWrapping = true, localName = "BugLocations")
    @JsonProperty("Location")
    public void setLocation(List<Location> locations) {
        this.location = locations;
    }

    /**
     * <p>withBugLocations.</p>
     *
     * @param bugLocations a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withBugLocations(List<Location> bugLocations) {
        this.location = bugLocations;
        return this;
    }

    /**
     * CweId
     * <p>
     *
     * @return a {@link java.util.List} object.
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
     *
     * @param cweId a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("CweId")
    public void setCweId(List<String> cweId) {
        this.cweId = cweId;
    }

    /**
     * <p>withCweId.</p>
     *
     * @param cweId a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withCweId(List<String> cweId) {
        this.cweId = cweId;
        return this;
    }

    /**
     * <p>Getter for the field <code>bugGroup</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("BugGroup")
    public String getBugGroup() {
        return bugGroup;
    }

    /**
     * <p>Setter for the field <code>bugGroup</code>.</p>
     *
     * @param bugGroup a {@link java.lang.String} object.
     */
    @JsonProperty("BugGroup")
    public void setBugGroup(String bugGroup) {
        this.bugGroup = bugGroup;
    }

    /**
     * <p>withBugGroup.</p>
     *
     * @param bugGroup a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withBugGroup(String bugGroup) {
        this.bugGroup = bugGroup;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("BugCode")
    public String getBugCode() {
        return bugCode;
    }

    /**
     * (Required)
     *
     * @param bugCode a {@link java.lang.String} object.
     */
    @JsonProperty("BugCode")
    public void setBugCode(String bugCode) {
        this.bugCode = bugCode;
    }

    /**
     * <p>withBugCode.</p>
     *
     * @param bugCode a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withBugCode(String bugCode) {
        this.bugCode = bugCode;
        return this;
    }

    /**
     * <p>Getter for the field <code>bugRank</code>.</p>
     *
     * @return a int.
     */
    @JsonProperty("BugRank")
    public int getBugRank() {
        return bugRank;
    }

    /**
     * <p>Setter for the field <code>bugRank</code>.</p>
     *
     * @param bugRank a int.
     */
    @JsonProperty("BugRank")
    public void setBugRank(int bugRank) {
        this.bugRank = bugRank;
    }

    /**
     * <p>withBugRank.</p>
     *
     * @param bugRank a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withBugRank(int bugRank) {
        this.bugRank = bugRank;
        return this;
    }

    /**
     * <p>Getter for the field <code>bugSeverity</code>.</p>
     *
     * @return a int.
     */
    @JsonProperty("BugSeverity")
    public int getBugSeverity() {
        return bugSeverity;
    }

    /**
     * <p>Setter for the field <code>bugSeverity</code>.</p>
     *
     * @param bugSeverity a int.
     */
    @JsonProperty("BugSeverity")
    public void setBugSeverity(int bugSeverity) {
        this.bugSeverity = bugSeverity;
    }

    /**
     * <p>withBugSeverity.</p>
     *
     * @param bugSeverity a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withBugSeverity(int bugSeverity) {
        this.bugSeverity = bugSeverity;
        return this;
    }

    /**
     * <p>Getter for the field <code>bugMessage</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("BugMessage")
    public String getBugMessage() {
        return bugMessage;
    }

    /**
     * <p>Setter for the field <code>bugMessage</code>.</p>
     *
     * @param bugMessage a {@link java.lang.String} object.
     */
    @JsonProperty("BugMessage")
    public void setBugMessage(String bugMessage) {
        this.bugMessage = bugMessage;
    }

    /**
     * <p>withBugMessage.</p>
     *
     * @param bugMessage a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withBugMessage(String bugMessage) {
        this.bugMessage = bugMessage;
        return this;
    }

    /**
     * <p>Getter for the field <code>resolutionSuggestion</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("ResolutionSuggestion")
    public String getResolutionSuggestion() {
        return resolutionSuggestion;
    }

    /**
     * <p>Setter for the field <code>resolutionSuggestion</code>.</p>
     *
     * @param resolutionSuggestion a {@link java.lang.String} object.
     */
    @JsonProperty("ResolutionSuggestion")
    public void setResolutionSuggestion(String resolutionSuggestion) {
        this.resolutionSuggestion = resolutionSuggestion;
    }

    /**
     * <p>withResolutionSuggestion.</p>
     *
     * @param resolutionSuggestion a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withResolutionSuggestion(String resolutionSuggestion) {
        this.resolutionSuggestion = resolutionSuggestion;
        return this;
    }

    /**
     * BugTrace
     * <p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugTrace} object.
     */
    @JsonProperty("BugTrace")
    public BugTrace getBugTrace() {
        return bugTrace;
    }

    /**
     * BugTrace
     * <p>
     *
     * @param bugTrace a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugTrace} object.
     */
    @JsonProperty("BugTrace")
    public void setBugTrace(BugTrace bugTrace) {
        this.bugTrace = bugTrace;
    }

    /**
     * <p>withBugTrace.</p>
     *
     * @param bugTrace a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugTrace} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withBugTrace(BugTrace bugTrace) {
        this.bugTrace = bugTrace;
        return this;
    }

    /**
     * id
     * <p>
     * <p>
     * (Required)
     *
     * @return a int.
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
     *
     * @param id a int.
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>withId.</p>
     *
     * @param id a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance} object.
     */
    public BugInstance withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("className", className).append("method", method).append("location", location).append("cweId", cweId).append("bugGroup", bugGroup).append("bugCode", bugCode).append("bugRank", bugRank).append("bugSeverity", bugSeverity).append("bugMessage", bugMessage).append("resolutionSuggestion", resolutionSuggestion).append("bugTrace", bugTrace).append("id", id).toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bugCode).append(bugTrace).append(bugSeverity).append(location).append(bugRank).append(bugMessage).append(resolutionSuggestion).append(id).append(cweId).append(method).append(className).append(bugGroup).toHashCode();
    }

    /** {@inheritDoc} */
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

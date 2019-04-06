package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * BugTrace
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "BuildId",
        "AssessmentReportFile",
        "InstanceLocation"
})
public class BugTrace implements Serializable {

    @JsonProperty("BuildId")
    private int buildId;
    @JsonProperty("AssessmentReportFile")
    private String assessmentReportFile;
    @JsonProperty("InstanceLocation")
    private InstanceLocation instanceLocation;
    private final static long serialVersionUID = 3446050256881658399L;

    /**
     * No args constructor for use in serialization
     */
    public BugTrace() {
    }

    /**
     * @param assessmentReportFile
     * @param buildId
     * @param instanceLocation
     */
    public BugTrace(int buildId, String assessmentReportFile, InstanceLocation instanceLocation) {
        super();
        this.buildId = buildId;
        this.assessmentReportFile = assessmentReportFile;
        this.instanceLocation = instanceLocation;
    }

    @JsonProperty("BuildId")
    public int getBuildId() {
        return buildId;
    }

    @JsonProperty("BuildId")
    public void setBuildId(int buildId) {
        this.buildId = buildId;
    }

    public BugTrace withBuildId(int buildId) {
        this.buildId = buildId;
        return this;
    }

    @JsonProperty("AssessmentReportFile")
    public String getAssessmentReportFile() {
        return assessmentReportFile;
    }

    @JsonProperty("AssessmentReportFile")
    public void setAssessmentReportFile(String assessmentReportFile) {
        this.assessmentReportFile = assessmentReportFile;
    }

    public BugTrace withAssessmentReportFile(String assessmentReportFile) {
        this.assessmentReportFile = assessmentReportFile;
        return this;
    }

    @JsonProperty("InstanceLocation")
    public InstanceLocation getInstanceLocation() {
        return instanceLocation;
    }

    @JsonProperty("InstanceLocation")
    public void setInstanceLocation(InstanceLocation instanceLocation) {
        this.instanceLocation = instanceLocation;
    }

    public BugTrace withInstanceLocation(InstanceLocation instanceLocation) {
        this.instanceLocation = instanceLocation;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("buildId", buildId).append("assessmentReportFile", assessmentReportFile).append("instanceLocation", instanceLocation).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(assessmentReportFile).append(buildId).append(instanceLocation).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BugTrace) == false) {
            return false;
        }
        BugTrace rhs = ((BugTrace) other);
        return new EqualsBuilder().append(assessmentReportFile, rhs.assessmentReportFile).append(buildId, rhs.buildId).append(instanceLocation, rhs.instanceLocation).isEquals();
    }

}

/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * BugTrace
 *
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"BuildId", "AssessmentReportFile", "InstanceLocation"})
public class BugTrace implements Serializable {

  private static final long serialVersionUID = 3446050256881658399L;

  @JsonProperty("BuildId")
  private int buildId;

  @JsonProperty("AssessmentReportFile")
  private String assessmentReportFile;

  @JsonProperty("InstanceLocation")
  private InstanceLocation instanceLocation;

  /** No args constructor for use in serialization */
  public BugTrace() {}

  /**
   * Constructor for BugTrace.
   *
   * @param assessmentReportFile a {@link java.lang.String} object.
   * @param buildId a int.
   * @param instanceLocation a {@link
   *     frontEnd.MessagingSystem.routing.structure.Scarf.InstanceLocation} object.
   */
  public BugTrace(int buildId, String assessmentReportFile, InstanceLocation instanceLocation) {
    super();
    this.buildId = buildId;
    this.assessmentReportFile = assessmentReportFile;
    this.instanceLocation = instanceLocation;
  }

  /**
   * Getter for the field <code>buildId</code>.
   *
   * @return a int.
   */
  @JsonProperty("BuildId")
  public int getBuildId() {
    return buildId;
  }

  /**
   * Setter for the field <code>buildId</code>.
   *
   * @param buildId a int.
   */
  @JsonProperty("BuildId")
  public void setBuildId(int buildId) {
    this.buildId = buildId;
  }

  /**
   * withBuildId.
   *
   * @param buildId a int.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugTrace} object.
   */
  public BugTrace withBuildId(int buildId) {
    this.buildId = buildId;
    return this;
  }

  /**
   * Getter for the field <code>assessmentReportFile</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("AssessmentReportFile")
  public String getAssessmentReportFile() {
    return assessmentReportFile;
  }

  /**
   * Setter for the field <code>assessmentReportFile</code>.
   *
   * @param assessmentReportFile a {@link java.lang.String} object.
   */
  @JsonProperty("AssessmentReportFile")
  public void setAssessmentReportFile(String assessmentReportFile) {
    this.assessmentReportFile = assessmentReportFile;
  }

  /**
   * withAssessmentReportFile.
   *
   * @param assessmentReportFile a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugTrace} object.
   */
  public BugTrace withAssessmentReportFile(String assessmentReportFile) {
    this.assessmentReportFile = assessmentReportFile;
    return this;
  }

  /**
   * Getter for the field <code>instanceLocation</code>.
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.InstanceLocation} object.
   */
  @JsonProperty("InstanceLocation")
  public InstanceLocation getInstanceLocation() {
    return instanceLocation;
  }

  /**
   * Setter for the field <code>instanceLocation</code>.
   *
   * @param instanceLocation a {@link
   *     frontEnd.MessagingSystem.routing.structure.Scarf.InstanceLocation} object.
   */
  @JsonProperty("InstanceLocation")
  public void setInstanceLocation(InstanceLocation instanceLocation) {
    this.instanceLocation = instanceLocation;
  }

  /**
   * withInstanceLocation.
   *
   * @param instanceLocation a {@link
   *     frontEnd.MessagingSystem.routing.structure.Scarf.InstanceLocation} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugTrace} object.
   */
  public BugTrace withInstanceLocation(InstanceLocation instanceLocation) {
    this.instanceLocation = instanceLocation;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("buildId", buildId)
        .append("assessmentReportFile", assessmentReportFile)
        .append("instanceLocation", instanceLocation)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(assessmentReportFile)
        .append(buildId)
        .append(instanceLocation)
        .toHashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof BugTrace) == false) {
      return false;
    }
    BugTrace rhs = ((BugTrace) other);
    return new EqualsBuilder()
        .append(assessmentReportFile, rhs.assessmentReportFile)
        .append(buildId, rhs.buildId)
        .append(instanceLocation, rhs.instanceLocation)
        .isEquals();
  }
}

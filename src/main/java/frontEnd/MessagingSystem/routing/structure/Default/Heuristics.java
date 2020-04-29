/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * HeuristicsType
 *
 * <p>
 *
 * @author maister
 * @version 03.13.00
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "NumberOfOrthogonal",
  "NumberOfConstantsToCheck",
  "NumberOfSlices",
  "NumberOfHeuristics",
  "AverageSlice",
  "DepthCount"
})
public class Heuristics implements Serializable {

  /** (Required) */
  @JsonProperty("NumberOfOrthogonal")
  private long numberOfOrthogonal;
  /** (Required) */
  @JsonProperty("NumberOfConstantsToCheck")
  private long numberOfConstantsToCheck;
  /** (Required) */
  @JsonProperty("NumberOfSlices")
  private long numberOfSlices;
  /** (Required) */
  @JsonProperty("NumberOfHeuristics")
  private long numberOfHeuristics;
  /** (Required) */
  @JsonProperty("AverageSlice")
  private double averageSlice;
  /**
   * DepthCount
   *
   * <p>(Required)
   */
  @JsonProperty("DepthCount")
  private List<String> depthCount = new ArrayList<String>();

  private static final long serialVersionUID = -5264580463433832404L;

  /** No args constructor for use in serialization */
  public Heuristics() {}

  /**
   * Constructor for Heuristics.
   *
   * @param numberOfConstantsToCheck a long.
   * @param numberOfOrthogonal a long.
   * @param averageSlice a double.
   * @param numberOfHeuristics a long.
   * @param numberOfSlices a long.
   * @param depthCount a {@link java.util.List} object.
   */
  public Heuristics(
      long numberOfOrthogonal,
      long numberOfConstantsToCheck,
      long numberOfSlices,
      long numberOfHeuristics,
      double averageSlice,
      List<String> depthCount) {
    super();
    this.numberOfOrthogonal = numberOfOrthogonal;
    this.numberOfConstantsToCheck = numberOfConstantsToCheck;
    this.numberOfSlices = numberOfSlices;
    this.numberOfHeuristics = numberOfHeuristics;
    this.averageSlice = averageSlice;
    this.depthCount = depthCount;
  }

  /**
   * (Required)
   *
   * @return a long.
   */
  @JsonProperty("NumberOfOrthogonal")
  public long getNumberOfOrthogonal() {
    return numberOfOrthogonal;
  }

  /**
   * (Required)
   *
   * @param numberOfOrthogonal a long.
   */
  @JsonProperty("NumberOfOrthogonal")
  public void setNumberOfOrthogonal(long numberOfOrthogonal) {
    this.numberOfOrthogonal = numberOfOrthogonal;
  }

  /**
   * withNumberOfOrthogonal.
   *
   * @param numberOfOrthogonal a long.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  public Heuristics withNumberOfOrthogonal(long numberOfOrthogonal) {
    this.numberOfOrthogonal = numberOfOrthogonal;
    return this;
  }

  /**
   * (Required)
   *
   * @return a long.
   */
  @JsonProperty("NumberOfConstantsToCheck")
  public long getNumberOfConstantsToCheck() {
    return numberOfConstantsToCheck;
  }

  /**
   * (Required)
   *
   * @param numberOfConstantsToCheck a long.
   */
  @JsonProperty("NumberOfConstantsToCheck")
  public void setNumberOfConstantsToCheck(long numberOfConstantsToCheck) {
    this.numberOfConstantsToCheck = numberOfConstantsToCheck;
  }

  /**
   * withNumberOfConstantsToCheck.
   *
   * @param numberOfConstantsToCheck a long.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  public Heuristics withNumberOfConstantsToCheck(long numberOfConstantsToCheck) {
    this.numberOfConstantsToCheck = numberOfConstantsToCheck;
    return this;
  }

  /**
   * (Required)
   *
   * @return a long.
   */
  @JsonProperty("NumberOfSlices")
  public long getNumberOfSlices() {
    return numberOfSlices;
  }

  /**
   * (Required)
   *
   * @param numberOfSlices a long.
   */
  @JsonProperty("NumberOfSlices")
  public void setNumberOfSlices(long numberOfSlices) {
    this.numberOfSlices = numberOfSlices;
  }

  /**
   * withNumberOfSlices.
   *
   * @param numberOfSlices a long.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  public Heuristics withNumberOfSlices(long numberOfSlices) {
    this.numberOfSlices = numberOfSlices;
    return this;
  }

  /**
   * (Required)
   *
   * @return a long.
   */
  @JsonProperty("NumberOfHeuristics")
  public long getNumberOfHeuristics() {
    return numberOfHeuristics;
  }

  /**
   * (Required)
   *
   * @param numberOfHeuristics a long.
   */
  @JsonProperty("NumberOfHeuristics")
  public void setNumberOfHeuristics(long numberOfHeuristics) {
    this.numberOfHeuristics = numberOfHeuristics;
  }

  /**
   * withNumberOfHeuristics.
   *
   * @param numberOfHeuristics a long.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  public Heuristics withNumberOfHeuristics(long numberOfHeuristics) {
    this.numberOfHeuristics = numberOfHeuristics;
    return this;
  }

  /**
   * (Required)
   *
   * @return a double.
   */
  @JsonProperty("AverageSlice")
  public double getAverageSlice() {
    return averageSlice;
  }

  /**
   * (Required)
   *
   * @param averageSlice a double.
   */
  @JsonProperty("AverageSlice")
  public void setAverageSlice(double averageSlice) {
    this.averageSlice = averageSlice;
  }

  /**
   * withAverageSlice.
   *
   * @param averageSlice a double.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  public Heuristics withAverageSlice(double averageSlice) {
    this.averageSlice = averageSlice;
    return this;
  }

  /**
   * DepthCount
   *
   * <p>
   *
   * <p>(Required)
   *
   * @return a {@link java.util.List} object.
   */
  @JsonProperty("DepthCount")
  public List<String> getDepthCount() {
    return depthCount;
  }

  /**
   * DepthCount
   *
   * <p>
   *
   * <p>(Required)
   *
   * @param depthCount a {@link java.util.List} object.
   */
  @JsonProperty("DepthCount")
  public void setDepthCount(List<String> depthCount) {
    this.depthCount = depthCount;
  }

  /**
   * withDepthCount.
   *
   * @param depthCount a {@link java.util.List} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  public Heuristics withDepthCount(List<String> depthCount) {
    this.depthCount = depthCount;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("numberOfOrthogonal", numberOfOrthogonal)
        .append("numberOfConstantsToCheck", numberOfConstantsToCheck)
        .append("numberOfSlices", numberOfSlices)
        .append("numberOfHeuristics", numberOfHeuristics)
        .append("averageSlice", averageSlice)
        .append("depthCount", depthCount)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(numberOfConstantsToCheck)
        .append(numberOfOrthogonal)
        .append(averageSlice)
        .append(numberOfHeuristics)
        .append(numberOfSlices)
        .append(depthCount)
        .toHashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Heuristics) == false) {
      return false;
    }
    Heuristics rhs = ((Heuristics) other);
    return new EqualsBuilder()
        .append(numberOfConstantsToCheck, rhs.numberOfConstantsToCheck)
        .append(numberOfOrthogonal, rhs.numberOfOrthogonal)
        .append(averageSlice, rhs.averageSlice)
        .append(numberOfHeuristics, rhs.numberOfHeuristics)
        .append(numberOfSlices, rhs.numberOfSlices)
        .append(depthCount, rhs.depthCount)
        .isEquals();
  }
}

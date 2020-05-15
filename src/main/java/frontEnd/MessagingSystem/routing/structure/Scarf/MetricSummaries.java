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
 * MetricSummaryType
 *
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "Type",
  "Count",
  "Sum",
  "SumOfSquares",
  "Minimum",
  "Maximum",
  "Average",
  "StandardDeviation"
})
public class MetricSummaries implements Serializable {

  private static final long serialVersionUID = 8722113363437002060L;
  /** (Required) */
  @JsonProperty("Type")
  private String type;
  /** (Required) */
  @JsonProperty("Count")
  private int count;
  /** (Required) */
  @JsonProperty("Sum")
  private int sum;
  /** (Required) */
  @JsonProperty("SumOfSquares")
  private int sumOfSquares;
  /** (Required) */
  @JsonProperty("Minimum")
  private int minimum;
  /** (Required) */
  @JsonProperty("Maximum")
  private int maximum;
  /** (Required) */
  @JsonProperty("Average")
  private double average;
  /** (Required) */
  @JsonProperty("StandardDeviation")
  private double standardDeviation;

  /** No args constructor for use in serialization */
  public MetricSummaries() {}

  /**
   * Constructor for MetricSummaries.
   *
   * @param sumOfSquares a int.
   * @param count a int.
   * @param minimum a int.
   * @param maximum a int.
   * @param standardDeviation a double.
   * @param sum a int.
   * @param type a {@link java.lang.String} object.
   * @param average a double.
   */
  public MetricSummaries(
      String type,
      int count,
      int sum,
      int sumOfSquares,
      int minimum,
      int maximum,
      double average,
      double standardDeviation) {
    super();
    this.type = type;
    this.count = count;
    this.sum = sum;
    this.sumOfSquares = sumOfSquares;
    this.minimum = minimum;
    this.maximum = maximum;
    this.average = average;
    this.standardDeviation = standardDeviation;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("Type")
  public String getType() {
    return type;
  }

  /**
   * (Required)
   *
   * @param type a {@link java.lang.String} object.
   */
  @JsonProperty("Type")
  public void setType(String type) {
    this.type = type;
  }

  /**
   * withType.
   *
   * @param type a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withType(String type) {
    this.type = type;
    return this;
  }

  /**
   * (Required)
   *
   * @return a int.
   */
  @JsonProperty("Count")
  public int getCount() {
    return count;
  }

  /**
   * (Required)
   *
   * @param count a int.
   */
  @JsonProperty("Count")
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * withCount.
   *
   * @param count a int.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withCount(int count) {
    this.count = count;
    return this;
  }

  /**
   * (Required)
   *
   * @return a int.
   */
  @JsonProperty("Sum")
  public int getSum() {
    return sum;
  }

  /**
   * (Required)
   *
   * @param sum a int.
   */
  @JsonProperty("Sum")
  public void setSum(int sum) {
    this.sum = sum;
  }

  /**
   * withSum.
   *
   * @param sum a int.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withSum(int sum) {
    this.sum = sum;
    return this;
  }

  /**
   * (Required)
   *
   * @return a int.
   */
  @JsonProperty("SumOfSquares")
  public int getSumOfSquares() {
    return sumOfSquares;
  }

  /**
   * (Required)
   *
   * @param sumOfSquares a int.
   */
  @JsonProperty("SumOfSquares")
  public void setSumOfSquares(int sumOfSquares) {
    this.sumOfSquares = sumOfSquares;
  }

  /**
   * withSumOfSquares.
   *
   * @param sumOfSquares a int.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withSumOfSquares(int sumOfSquares) {
    this.sumOfSquares = sumOfSquares;
    return this;
  }

  /**
   * (Required)
   *
   * @return a int.
   */
  @JsonProperty("Minimum")
  public int getMinimum() {
    return minimum;
  }

  /**
   * (Required)
   *
   * @param minimum a int.
   */
  @JsonProperty("Minimum")
  public void setMinimum(int minimum) {
    this.minimum = minimum;
  }

  /**
   * withMinimum.
   *
   * @param minimum a int.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withMinimum(int minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * (Required)
   *
   * @return a int.
   */
  @JsonProperty("Maximum")
  public int getMaximum() {
    return maximum;
  }

  /**
   * (Required)
   *
   * @param maximum a int.
   */
  @JsonProperty("Maximum")
  public void setMaximum(int maximum) {
    this.maximum = maximum;
  }

  /**
   * withMaximum.
   *
   * @param maximum a int.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withMaximum(int maximum) {
    this.maximum = maximum;
    return this;
  }

  /**
   * (Required)
   *
   * @return a double.
   */
  @JsonProperty("Average")
  public double getAverage() {
    return average;
  }

  /**
   * (Required)
   *
   * @param average a double.
   */
  @JsonProperty("Average")
  public void setAverage(double average) {
    this.average = average;
  }

  /**
   * withAverage.
   *
   * @param average a double.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withAverage(double average) {
    this.average = average;
    return this;
  }

  /**
   * (Required)
   *
   * @return a double.
   */
  @JsonProperty("StandardDeviation")
  public double getStandardDeviation() {
    return standardDeviation;
  }

  /**
   * (Required)
   *
   * @param standardDeviation a double.
   */
  @JsonProperty("StandardDeviation")
  public void setStandardDeviation(double standardDeviation) {
    this.standardDeviation = standardDeviation;
  }

  /**
   * withStandardDeviation.
   *
   * @param standardDeviation a double.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.MetricSummaries} object.
   */
  public MetricSummaries withStandardDeviation(double standardDeviation) {
    this.standardDeviation = standardDeviation;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("type", type)
        .append("count", count)
        .append("sum", sum)
        .append("sumOfSquares", sumOfSquares)
        .append("minimum", minimum)
        .append("maximum", maximum)
        .append("average", average)
        .append("standardDeviation", standardDeviation)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(sumOfSquares)
        .append(count)
        .append(minimum)
        .append(maximum)
        .append(standardDeviation)
        .append(sum)
        .append(type)
        .append(average)
        .toHashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof MetricSummaries) == false) {
      return false;
    }
    MetricSummaries rhs = ((MetricSummaries) other);
    return new EqualsBuilder()
        .append(sumOfSquares, rhs.sumOfSquares)
        .append(count, rhs.count)
        .append(minimum, rhs.minimum)
        .append(maximum, rhs.maximum)
        .append(standardDeviation, rhs.standardDeviation)
        .append(sum, rhs.sum)
        .append(type, rhs.type)
        .append(average, rhs.average)
        .isEquals();
  }
}

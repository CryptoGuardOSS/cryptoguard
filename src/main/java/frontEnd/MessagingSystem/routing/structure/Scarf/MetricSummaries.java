package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * MetricSummaryType
 * <p>
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

    /**
     * (Required)
     */
    @JsonProperty("Type")
    private String type;
    /**
     * (Required)
     */
    @JsonProperty("Count")
    private int count;
    /**
     * (Required)
     */
    @JsonProperty("Sum")
    private int sum;
    /**
     * (Required)
     */
    @JsonProperty("SumOfSquares")
    private int sumOfSquares;
    /**
     * (Required)
     */
    @JsonProperty("Minimum")
    private int minimum;
    /**
     * (Required)
     */
    @JsonProperty("Maximum")
    private int maximum;
    /**
     * (Required)
     */
    @JsonProperty("Average")
    private double average;
    /**
     * (Required)
     */
    @JsonProperty("StandardDeviation")
    private double standardDeviation;
    private final static long serialVersionUID = 8722113363437002060L;

    /**
     * No args constructor for use in serialization
     */
    public MetricSummaries() {
    }

    /**
     * @param sumOfSquares
     * @param count
     * @param minimum
     * @param maximum
     * @param standardDeviation
     * @param sum
     * @param type
     * @param average
     */
    public MetricSummaries(String type, int count, int sum, int sumOfSquares, int minimum, int maximum, double average, double standardDeviation) {
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
     */
    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    /**
     * (Required)
     */
    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    public MetricSummaries withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Count")
    public int getCount() {
        return count;
    }

    /**
     * (Required)
     */
    @JsonProperty("Count")
    public void setCount(int count) {
        this.count = count;
    }

    public MetricSummaries withCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Sum")
    public int getSum() {
        return sum;
    }

    /**
     * (Required)
     */
    @JsonProperty("Sum")
    public void setSum(int sum) {
        this.sum = sum;
    }

    public MetricSummaries withSum(int sum) {
        this.sum = sum;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("SumOfSquares")
    public int getSumOfSquares() {
        return sumOfSquares;
    }

    /**
     * (Required)
     */
    @JsonProperty("SumOfSquares")
    public void setSumOfSquares(int sumOfSquares) {
        this.sumOfSquares = sumOfSquares;
    }

    public MetricSummaries withSumOfSquares(int sumOfSquares) {
        this.sumOfSquares = sumOfSquares;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Minimum")
    public int getMinimum() {
        return minimum;
    }

    /**
     * (Required)
     */
    @JsonProperty("Minimum")
    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public MetricSummaries withMinimum(int minimum) {
        this.minimum = minimum;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Maximum")
    public int getMaximum() {
        return maximum;
    }

    /**
     * (Required)
     */
    @JsonProperty("Maximum")
    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public MetricSummaries withMaximum(int maximum) {
        this.maximum = maximum;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Average")
    public double getAverage() {
        return average;
    }

    /**
     * (Required)
     */
    @JsonProperty("Average")
    public void setAverage(double average) {
        this.average = average;
    }

    public MetricSummaries withAverage(double average) {
        this.average = average;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("StandardDeviation")
    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * (Required)
     */
    @JsonProperty("StandardDeviation")
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public MetricSummaries withStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("count", count).append("sum", sum).append("sumOfSquares", sumOfSquares).append("minimum", minimum).append("maximum", maximum).append("average", average).append("standardDeviation", standardDeviation).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sumOfSquares).append(count).append(minimum).append(maximum).append(standardDeviation).append(sum).append(type).append(average).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MetricSummaries) == false) {
            return false;
        }
        MetricSummaries rhs = ((MetricSummaries) other);
        return new EqualsBuilder().append(sumOfSquares, rhs.sumOfSquares).append(count, rhs.count).append(minimum, rhs.minimum).append(maximum, rhs.maximum).append(standardDeviation, rhs.standardDeviation).append(sum, rhs.sum).append(type, rhs.type).append(average, rhs.average).isEquals();
    }

}

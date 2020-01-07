package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * HeuristicsType
 * <p>
 *
 * @author franceme
 * @version 03.07.01
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

    private final static long serialVersionUID = -1017071453464845745L;
    /**
     * (Required)
     */
    @JsonProperty("NumberOfOrthogonal")
    private int numberOfOrthogonal;
    /**
     * (Required)
     */
    @JsonProperty("NumberOfConstantsToCheck")
    private int numberOfConstantsToCheck;
    /**
     * (Required)
     */
    @JsonProperty("NumberOfSlices")
    private int numberOfSlices;
    /**
     * (Required)
     */
    @JsonProperty("NumberOfHeuristics")
    private int numberOfHeuristics;
    /**
     * (Required)
     */
    @JsonProperty("AverageSlice")
    private double averageSlice;
    /**
     * DepthCount
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("DepthCount")
    private List<String> depthCount = new ArrayList<>();

    /**
     * No args constructor for use in serialization
     */
    public Heuristics() {
    }

    /**
     * <p>Constructor for Heuristics.</p>
     *
     * @param averageSlice             a double.
     * @param numberOfHeuristics       a int.
     * @param numberOfOrthogonal       a int.
     * @param numberOfSlices           a int.
     * @param depthCount               a {@link java.util.List} object.
     * @param numberOfConstantsToCheck a int.
     */
    public Heuristics(int numberOfOrthogonal, int numberOfConstantsToCheck, int numberOfSlices, int numberOfHeuristics, double averageSlice, List<String> depthCount) {
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
     * @return a int.
     */
    @JsonProperty("NumberOfOrthogonal")
    public int getNumberOfOrthogonal() {
        return numberOfOrthogonal;
    }

    /**
     * (Required)
     *
     * @param numberOfOrthogonal a int.
     */
    @JsonProperty("NumberOfOrthogonal")
    public void setNumberOfOrthogonal(int numberOfOrthogonal) {
        this.numberOfOrthogonal = numberOfOrthogonal;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JsonProperty("NumberOfConstantsToCheck")
    public int getNumberOfConstantsToCheck() {
        return numberOfConstantsToCheck;
    }

    /**
     * (Required)
     *
     * @param numberOfConstantsToCheck a int.
     */
    @JsonProperty("NumberOfConstantsToCheck")
    public void setNumberOfConstantsToCheck(int numberOfConstantsToCheck) {
        this.numberOfConstantsToCheck = numberOfConstantsToCheck;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JsonProperty("NumberOfSlices")
    public int getNumberOfSlices() {
        return numberOfSlices;
    }

    /**
     * (Required)
     *
     * @param numberOfSlices a int.
     */
    @JsonProperty("NumberOfSlices")
    public void setNumberOfSlices(int numberOfSlices) {
        this.numberOfSlices = numberOfSlices;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JsonProperty("NumberOfHeuristics")
    public int getNumberOfHeuristics() {
        return numberOfHeuristics;
    }

    /**
     * (Required)
     *
     * @param numberOfHeuristics a int.
     */
    @JsonProperty("NumberOfHeuristics")
    public void setNumberOfHeuristics(int numberOfHeuristics) {
        this.numberOfHeuristics = numberOfHeuristics;
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
     * DepthCount
     * <p>
     * <p>
     * (Required)
     *
     * @return a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("DepthCount")
    public List<String> getDepthCount() {
        return depthCount;
    }

    /**
     * DepthCount
     * <p>
     * <p>
     * (Required)
     *
     * @param depthCount a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("DepthCount")
    public void setDepthCount(List<String> depthCount) {
        this.depthCount = depthCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("numberOfOrthogonal", numberOfOrthogonal).append("numberOfConstantsToCheck", numberOfConstantsToCheck).append("numberOfSlices", numberOfSlices).append("numberOfHeuristics", numberOfHeuristics).append("averageSlice", averageSlice).append("depthCount", depthCount).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(averageSlice).append(numberOfHeuristics).append(numberOfOrthogonal).append(numberOfSlices).append(depthCount).append(numberOfConstantsToCheck).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Heuristics) == false) {
            return false;
        }
        Heuristics rhs = ((Heuristics) other);
        return new EqualsBuilder().append(averageSlice, rhs.averageSlice).append(numberOfHeuristics, rhs.numberOfHeuristics).append(numberOfOrthogonal, rhs.numberOfOrthogonal).append(numberOfSlices, rhs.numberOfSlices).append(depthCount, rhs.depthCount).append(numberOfConstantsToCheck, rhs.numberOfConstantsToCheck).isEquals();
    }

}

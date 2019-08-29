
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * Timing
 * <p>
 *
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Total",
        "Scan",
        "TimeScale"
})
public class Timing implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Total")
    private String total;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Scan")
    private String scan;
    /**
     * (Required)
     */
    @JsonProperty("TimeScale")
    private String timeScale;
    private final static long serialVersionUID = -7831748094033719806L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Timing() {
    }

    /**
     * 
     * @param total
     * @param timeScale
     * @param scan
     */
    public Timing(String total, String scan, String timeScale) {
        super();
        this.total = total;
        this.scan = scan;
        this.timeScale = timeScale;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("Total")
    public String getTotal() {
        return total;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Total")
    public void setTotal(String total) {
        this.total = total;
    }

    public Timing withTotal(String total) {
        this.total = total;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Scan")
    public String getScan() {
        return scan;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Scan")
    public void setScan(String scan) {
        this.scan = scan;
    }

    public Timing withScan(String scan) {
        this.scan = scan;
        return this;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("TimeScale")
    public String getTimeScale() {
        return timeScale;
    }

    /**
     * (Required)
     */
    @JsonProperty("TimeScale")
    public void setTimeScale(String timeScale) {
        this.timeScale = timeScale;
    }

    public Timing withTimeScale(String timeScale) {
        this.timeScale = timeScale;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("total", total).append("scan", scan).append("timeScale", timeScale).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(total).append(timeScale).append(scan).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Timing) == false) {
            return false;
        }
        Timing rhs = ((Timing) other);
        return new EqualsBuilder().append(total, rhs.total).append(timeScale, rhs.timeScale).append(scan, rhs.scan).isEquals();
    }

}

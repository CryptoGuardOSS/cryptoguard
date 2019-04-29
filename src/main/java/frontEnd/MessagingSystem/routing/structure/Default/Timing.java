
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
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Total",
        "Scan"
})
public class Timing implements Serializable {

    /**
     * (Required)
     */
    @JsonProperty("Total")
    private String total;
    /**
     * (Required)
     */
    @JsonProperty("Scan")
    private String scan;
    private final static long serialVersionUID = 8015087845422072204L;

    /**
     * No args constructor for use in serialization
     */
    public Timing() {
    }

    /**
     * @param total
     * @param scan
     */
    public Timing(String total, String scan) {
        super();
        this.total = total;
        this.scan = scan;
    }

    /**
     * (Required)
     */
    @JsonProperty("Total")
    public String getTotal() {
        return total;
    }

    /**
     * (Required)
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
     * (Required)
     */
    @JsonProperty("Scan")
    public String getScan() {
        return scan;
    }

    /**
     * (Required)
     */
    @JsonProperty("Scan")
    public void setScan(String scan) {
        this.scan = scan;
    }

    public Timing withScan(String scan) {
        this.scan = scan;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("total", total).append("scan", scan).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(total).append(scan).toHashCode();
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
        return new EqualsBuilder().append(total, rhs.total).append(scan, rhs.scan).isEquals();
    }

}

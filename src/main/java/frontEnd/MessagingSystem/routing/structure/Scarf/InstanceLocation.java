package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Xpath",
        "LineNum"
})
public class InstanceLocation implements Serializable {

    @JsonProperty("Xpath")
    private String xpath;
    /**
     * LineNum
     * <p>
     */
    @JsonProperty("LineNum")
    private LineNum lineNum;
    private final static long serialVersionUID = -2084587122423583152L;

    /**
     * No args constructor for use in serialization
     */
    public InstanceLocation() {
    }

    /**
     * @param lineNum
     * @param xpath
     */
    public InstanceLocation(String xpath, LineNum lineNum) {
        super();
        this.xpath = xpath;
        this.lineNum = lineNum;
    }

    @JsonProperty("Xpath")
    public String getXpath() {
        return xpath;
    }

    @JsonProperty("Xpath")
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public InstanceLocation withXpath(String xpath) {
        this.xpath = xpath;
        return this;
    }

    /**
     * LineNum
     * <p>
     */
    @JsonProperty("LineNum")
    public LineNum getLineNum() {
        return lineNum;
    }

    /**
     * LineNum
     * <p>
     */
    @JsonProperty("LineNum")
    public void setLineNum(LineNum lineNum) {
        this.lineNum = lineNum;
    }

    public InstanceLocation withLineNum(LineNum lineNum) {
        this.lineNum = lineNum;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("xpath", xpath).append("lineNum", lineNum).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lineNum).append(xpath).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof InstanceLocation) == false) {
            return false;
        }
        InstanceLocation rhs = ((InstanceLocation) other);
        return new EqualsBuilder().append(lineNum, rhs.lineNum).append(xpath, rhs.xpath).isEquals();
    }

}

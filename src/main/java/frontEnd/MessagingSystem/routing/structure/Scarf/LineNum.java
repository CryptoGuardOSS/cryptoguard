package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * LineNum
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Start",
        "End"
})
public class LineNum implements Serializable {

    /**
     * (Required)
     */
    @JsonProperty("Start")
    private int start;
    /**
     * (Required)
     */
    @JsonProperty("End")
    private int end;
    private final static long serialVersionUID = 1255562489400430665L;

    /**
     * No args constructor for use in serialization
     */
    public LineNum() {
    }

    /**
     * @param start
     * @param end
     */
    public LineNum(int start, int end) {
        super();
        this.start = start;
        this.end = end;
    }

    /**
     * (Required)
     */
    @JsonProperty("Start")
    public int getStart() {
        return start;
    }

    /**
     * (Required)
     */
    @JsonProperty("Start")
    public void setStart(int start) {
        this.start = start;
    }

    public LineNum withStart(int start) {
        this.start = start;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("End")
    public int getEnd() {
        return end;
    }

    /**
     * (Required)
     */
    @JsonProperty("End")
    public void setEnd(int end) {
        this.end = end;
    }

    public LineNum withEnd(int end) {
        this.end = end;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("start", start).append("end", end).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(start).append(end).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LineNum) == false) {
            return false;
        }
        LineNum rhs = ((LineNum) other);
        return new EqualsBuilder().append(start, rhs.start).append(end, rhs.end).isEquals();
    }

}

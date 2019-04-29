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
 *
 * @author franceme
 * @version $Id: $Id
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
     * <p>Constructor for LineNum.</p>
     *
     * @param start a int.
     * @param end   a int.
     */
    public LineNum(int start, int end) {
        super();
        this.start = start;
        this.end = end;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JsonProperty("Start")
    public int getStart() {
        return start;
    }

    /**
     * (Required)
     *
     * @param start a int.
     */
    @JsonProperty("Start")
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * <p>withStart.</p>
     *
     * @param start a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.LineNum} object.
     */
    public LineNum withStart(int start) {
        this.start = start;
        return this;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JsonProperty("End")
    public int getEnd() {
        return end;
    }

    /**
     * (Required)
     *
     * @param end a int.
     */
    @JsonProperty("End")
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * <p>withEnd.</p>
     *
     * @param end a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.LineNum} object.
     */
    public LineNum withEnd(int end) {
        this.end = end;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("start", start).append("end", end).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(start).append(end).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
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

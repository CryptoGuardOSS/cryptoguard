package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * LocationType
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ClassName",
        "MethodName",
        "LineNumber",
        "ColumnNumber"
})
public class Location implements Serializable {

    private final static long serialVersionUID = 1728740023126089054L;
    /**
     * (Required)
     */
    @JsonProperty("ClassName")
    private String className;
    /**
     * (Required)
     */
    @JsonProperty("MethodName")
    private String methodName;
    @JsonProperty("LineNumber")
    private int lineNumber;
    @JsonProperty("ColumnNumber")
    private int columnNumber;

    /**
     * No args constructor for use in serialization
     */
    public Location() {
    }

    /**
     * <p>Constructor for Location.</p>
     *
     * @param lineNumber   a int.
     * @param className    a {@link java.lang.String} object.
     * @param methodName   a {@link java.lang.String} object.
     * @param columnNumber a int.
     */
    public Location(String className, String methodName, int lineNumber, int columnNumber) {
        super();
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("ClassName")
    public String getClassName() {
        return className;
    }

    /**
     * (Required)
     *
     * @param className a {@link java.lang.String} object.
     */
    @JsonProperty("ClassName")
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * <p>withClassName.</p>
     *
     * @param className a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withClassName(String className) {
        this.className = className;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("MethodName")
    public String getMethodName() {
        return methodName;
    }

    /**
     * (Required)
     *
     * @param methodName a {@link java.lang.String} object.
     */
    @JsonProperty("MethodName")
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * <p>withMethodName.</p>
     *
     * @param methodName a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * <p>Getter for the field <code>lineNumber</code>.</p>
     *
     * @return a int.
     */
    @JsonProperty("LineNumber")
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * <p>Setter for the field <code>lineNumber</code>.</p>
     *
     * @param lineNumber a int.
     */
    @JsonProperty("LineNumber")
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * <p>withLineNumber.</p>
     *
     * @param lineNumber a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    /**
     * <p>Getter for the field <code>columnNumber</code>.</p>
     *
     * @return a int.
     */
    @JsonProperty("ColumnNumber")
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * <p>Setter for the field <code>columnNumber</code>.</p>
     *
     * @param columnNumber a int.
     */
    @JsonProperty("ColumnNumber")
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    /**
     * <p>withColumnNumber.</p>
     *
     * @param columnNumber a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("className", className).append("methodName", methodName).append("lineNumber", lineNumber).append("columnNumber", columnNumber).toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lineNumber).append(className).append(methodName).append(columnNumber).toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Location) == false) {
            return false;
        }
        Location rhs = ((Location) other);
        return new EqualsBuilder().append(lineNumber, rhs.lineNumber).append(className, rhs.className).append(methodName, rhs.methodName).append(columnNumber, rhs.columnNumber).isEquals();
    }

}

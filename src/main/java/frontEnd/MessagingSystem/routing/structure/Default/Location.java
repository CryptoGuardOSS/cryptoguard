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
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ClassName",
        "MethodName",
        "LineNumber",
        "ColumnNumber"
})
public class Location implements Serializable {

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
    private final static long serialVersionUID = 1728740023126089054L;

    /**
     * No args constructor for use in serialization
     */
    public Location() {
    }

    /**
     * @param lineNumber
     * @param className
     * @param methodName
     * @param columnNumber
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
     */
    @JsonProperty("ClassName")
    public String getClassName() {
        return className;
    }

    /**
     * (Required)
     */
    @JsonProperty("ClassName")
    public void setClassName(String className) {
        this.className = className;
    }

    public Location withClassName(String className) {
        this.className = className;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("MethodName")
    public String getMethodName() {
        return methodName;
    }

    /**
     * (Required)
     */
    @JsonProperty("MethodName")
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Location withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @JsonProperty("LineNumber")
    public int getLineNumber() {
        return lineNumber;
    }

    @JsonProperty("LineNumber")
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Location withLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    @JsonProperty("ColumnNumber")
    public int getColumnNumber() {
        return columnNumber;
    }

    @JsonProperty("ColumnNumber")
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public Location withColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("className", className).append("methodName", methodName).append("lineNumber", lineNumber).append("columnNumber", columnNumber).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lineNumber).append(className).append(methodName).append(columnNumber).toHashCode();
    }

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

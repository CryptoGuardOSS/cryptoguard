
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
    "SourceFile",
        "StartLine",
        "EndLine",
        "StartColumn",
        "EndColumn",
        "Explanation",
        "ClassName",
        "MethodName",
        "Primary",
        "-id"
})
public class Location implements Serializable {

    /**
     * (Required)
     */
    @JsonProperty("SourceFile")
    private String sourceFile;
    @JsonProperty("StartLine")
    private long startLine;
    @JsonProperty("EndLine")
    private long endLine;
    @JsonProperty("StartColumn")
    private long startColumn;
    @JsonProperty("EndColumn")
    private long endColumn;
    @JsonProperty("Explanation")
    private String explanation;
    @JsonProperty("ClassName")
    private String className;
    @JsonProperty("MethodName")
    private String methodName;
    @JsonProperty("Primary")
    private boolean primary;
    /**
     * id
     * <p>
     */
    @JsonProperty("-id")
    private long id = 0L;
    private final static long serialVersionUID = 8894548646396622400L;

    /**
     * No args constructor for use in serialization
     */
    public Location() {
    }

    /**
     * @param endLine
     * @param endColumn
     * @param startColumn
     * @param startLine
     * @param methodName
     * @param className
     * @param id
     * @param explanation
     * @param sourceFile
     * @param primary
     */
    public Location(String sourceFile, long startLine, long endLine, long startColumn, long endColumn, String explanation, String className, String methodName, boolean primary, long id) {
        super();
        this.sourceFile = sourceFile;
        this.startLine = startLine;
        this.endLine = endLine;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.explanation = explanation;
        this.className = className;
        this.methodName = methodName;
        this.primary = primary;
        this.id = id;
    }

    /**
     * (Required)
     */
    @JsonProperty("SourceFile")
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * (Required)
     */
    @JsonProperty("SourceFile")
    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Location withSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
        return this;
    }

    @JsonProperty("StartLine")
    public long getStartLine() {
        return startLine;
    }

    @JsonProperty("StartLine")
    public void setStartLine(long startLine) {
        this.startLine = startLine;
    }

    public Location withStartLine(long startLine) {
        this.startLine = startLine;
        return this;
    }

    @JsonProperty("EndLine")
    public long getEndLine() {
        return endLine;
    }

    @JsonProperty("EndLine")
    public void setEndLine(long endLine) {
        this.endLine = endLine;
    }

    public Location withEndLine(long endLine) {
        this.endLine = endLine;
        return this;
    }

    @JsonProperty("StartColumn")
    public long getStartColumn() {
        return startColumn;
    }

    @JsonProperty("StartColumn")
    public void setStartColumn(long startColumn) {
        this.startColumn = startColumn;
    }

    public Location withStartColumn(long startColumn) {
        this.startColumn = startColumn;
        return this;
    }

    @JsonProperty("EndColumn")
    public long getEndColumn() {
        return endColumn;
    }

    @JsonProperty("EndColumn")
    public void setEndColumn(long endColumn) {
        this.endColumn = endColumn;
    }

    public Location withEndColumn(long endColumn) {
        this.endColumn = endColumn;
        return this;
    }

    @JsonProperty("Explanation")
    public String getExplanation() {
        return explanation;
    }

    @JsonProperty("Explanation")
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Location withExplanation(String explanation) {
        this.explanation = explanation;
        return this;
    }

    @JsonProperty("ClassName")
    public String getClassName() {
        return className;
    }

    @JsonProperty("ClassName")
    public void setClassName(String className) {
        this.className = className;
    }

    public Location withClassName(String className) {
        this.className = className;
        return this;
    }

    @JsonProperty("MethodName")
    public String getMethodName() {
        return methodName;
    }

    @JsonProperty("MethodName")
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Location withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @JsonProperty("Primary")
    public boolean isPrimary() {
        return primary;
    }

    @JsonProperty("Primary")
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Location withPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

    /**
     * id
     * <p>
     */
    @JsonProperty("-id")
    public long getId() {
        return id;
    }

    /**
     * id
     * <p>
     */
    @JsonProperty("-id")
    public void setId(long id) {
        this.id = id;
    }

    public Location withId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("sourceFile", sourceFile).append("startLine", startLine).append("endLine", endLine).append("startColumn", startColumn).append("endColumn", endColumn).append("explanation", explanation).append("className", className).append("methodName", methodName).append("primary", primary).append("id", id).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(endLine).append(endColumn).append(startColumn).append(startLine).append(methodName).append(className).append(id).append(explanation).append(sourceFile).append(primary).toHashCode();
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
        return new EqualsBuilder().append(endLine, rhs.endLine).append(endColumn, rhs.endColumn).append(startColumn, rhs.startColumn).append(startLine, rhs.startLine).append(methodName, rhs.methodName).append(className, rhs.className).append(id, rhs.id).append(explanation, rhs.explanation).append(sourceFile, rhs.sourceFile).append(primary, rhs.primary).isEquals();
    }

}

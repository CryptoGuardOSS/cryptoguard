
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
 * @author maister
 * @version 03.13.00
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
     * <p>Constructor for Location.</p>
     *
     * @param endLine     a long.
     * @param endColumn   a long.
     * @param startColumn a long.
     * @param startLine   a long.
     * @param methodName  a {@link java.lang.String} object.
     * @param className   a {@link java.lang.String} object.
     * @param id          a long.
     * @param explanation a {@link java.lang.String} object.
     * @param sourceFile  a {@link java.lang.String} object.
     * @param primary     a boolean.
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
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("SourceFile")
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * (Required)
     *
     * @param sourceFile a {@link java.lang.String} object.
     */
    @JsonProperty("SourceFile")
    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * <p>withSourceFile.</p>
     *
     * @param sourceFile a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
        return this;
    }

    /**
     * <p>Getter for the field <code>startLine</code>.</p>
     *
     * @return a long.
     */
    @JsonProperty("StartLine")
    public long getStartLine() {
        return startLine;
    }

    /**
     * <p>Setter for the field <code>startLine</code>.</p>
     *
     * @param startLine a long.
     */
    @JsonProperty("StartLine")
    public void setStartLine(long startLine) {
        this.startLine = startLine;
    }

    /**
     * <p>withStartLine.</p>
     *
     * @param startLine a long.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withStartLine(long startLine) {
        this.startLine = startLine;
        return this;
    }

    /**
     * <p>Getter for the field <code>endLine</code>.</p>
     *
     * @return a long.
     */
    @JsonProperty("EndLine")
    public long getEndLine() {
        return endLine;
    }

    /**
     * <p>Setter for the field <code>endLine</code>.</p>
     *
     * @param endLine a long.
     */
    @JsonProperty("EndLine")
    public void setEndLine(long endLine) {
        this.endLine = endLine;
    }

    /**
     * <p>withEndLine.</p>
     *
     * @param endLine a long.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withEndLine(long endLine) {
        this.endLine = endLine;
        return this;
    }

    /**
     * <p>Getter for the field <code>startColumn</code>.</p>
     *
     * @return a long.
     */
    @JsonProperty("StartColumn")
    public long getStartColumn() {
        return startColumn;
    }

    /**
     * <p>Setter for the field <code>startColumn</code>.</p>
     *
     * @param startColumn a long.
     */
    @JsonProperty("StartColumn")
    public void setStartColumn(long startColumn) {
        this.startColumn = startColumn;
    }

    /**
     * <p>withStartColumn.</p>
     *
     * @param startColumn a long.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withStartColumn(long startColumn) {
        this.startColumn = startColumn;
        return this;
    }

    /**
     * <p>Getter for the field <code>endColumn</code>.</p>
     *
     * @return a long.
     */
    @JsonProperty("EndColumn")
    public long getEndColumn() {
        return endColumn;
    }

    /**
     * <p>Setter for the field <code>endColumn</code>.</p>
     *
     * @param endColumn a long.
     */
    @JsonProperty("EndColumn")
    public void setEndColumn(long endColumn) {
        this.endColumn = endColumn;
    }

    /**
     * <p>withEndColumn.</p>
     *
     * @param endColumn a long.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withEndColumn(long endColumn) {
        this.endColumn = endColumn;
        return this;
    }

    /**
     * <p>Getter for the field <code>explanation</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Explanation")
    public String getExplanation() {
        return explanation;
    }

    /**
     * <p>Setter for the field <code>explanation</code>.</p>
     *
     * @param explanation a {@link java.lang.String} object.
     */
    @JsonProperty("Explanation")
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * <p>withExplanation.</p>
     *
     * @param explanation a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withExplanation(String explanation) {
        this.explanation = explanation;
        return this;
    }

    /**
     * <p>Getter for the field <code>className</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("ClassName")
    public String getClassName() {
        return className;
    }

    /**
     * <p>Setter for the field <code>className</code>.</p>
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
     * <p>Getter for the field <code>methodName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("MethodName")
    public String getMethodName() {
        return methodName;
    }

    /**
     * <p>Setter for the field <code>methodName</code>.</p>
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
     * <p>isPrimary.</p>
     *
     * @return a boolean.
     */
    @JsonProperty("Primary")
    public boolean isPrimary() {
        return primary;
    }

    /**
     * <p>Setter for the field <code>primary</code>.</p>
     *
     * @param primary a boolean.
     */
    @JsonProperty("Primary")
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * <p>withPrimary.</p>
     *
     * @param primary a boolean.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

    /**
     * id
     * <p>
     *
     * @return a long.
     */
    @JsonProperty("-id")
    public long getId() {
        return id;
    }

    /**
     * id
     * <p>
     *
     * @param id a long.
     */
    @JsonProperty("-id")
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>withId.</p>
     *
     * @param id a long.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public Location withId(long id) {
        this.id = id;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("sourceFile", sourceFile).append("startLine", startLine).append("endLine", endLine).append("startColumn", startColumn).append("endColumn", endColumn).append("explanation", explanation).append("className", className).append("methodName", methodName).append("primary", primary).append("id", id).toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(endLine).append(endColumn).append(startColumn).append(startLine).append(methodName).append(className).append(id).append(explanation).append(sourceFile).append(primary).toHashCode();
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
        return new EqualsBuilder().append(endLine, rhs.endLine).append(endColumn, rhs.endColumn).append(startColumn, rhs.startColumn).append(startLine, rhs.startLine).append(methodName, rhs.methodName).append(className, rhs.className).append(id, rhs.id).append(explanation, rhs.explanation).append(sourceFile, rhs.sourceFile).append(primary, rhs.primary).isEquals();
    }

}

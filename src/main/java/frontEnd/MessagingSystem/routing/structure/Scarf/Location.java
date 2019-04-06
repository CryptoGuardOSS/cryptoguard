package frontEnd.MessagingSystem.routing.structure.Scarf;

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
        "Explanation"
})
public class Location implements Serializable {

    /**
     * (Required)
     */
    @JsonProperty("SourceFile")
    private String sourceFile;
    @JsonProperty("StartLine")
    private int startLine;
    @JsonProperty("EndLine")
    private int endLine;
    @JsonProperty("StartColumn")
    private int startColumn;
    @JsonProperty("EndColumn")
    private int endColumn;
    @JsonProperty("Explanation")
    private String explanation;
    private final static long serialVersionUID = 1200285316537190850L;

    /**
     * No args constructor for use in serialization
     */
    public Location() {
    }

    /**
     * @param startLine
     * @param sourceFile
     * @param explanation
     * @param endLine
     * @param startColumn
     * @param endColumn
     */
    public Location(String sourceFile, int startLine, int endLine, int startColumn, int endColumn, String explanation) {
        super();
        this.sourceFile = sourceFile;
        this.startLine = startLine;
        this.endLine = endLine;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.explanation = explanation;
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
    public int getStartLine() {
        return startLine;
    }

    @JsonProperty("StartLine")
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public Location withStartLine(int startLine) {
        this.startLine = startLine;
        return this;
    }

    @JsonProperty("EndLine")
    public int getEndLine() {
        return endLine;
    }

    @JsonProperty("EndLine")
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public Location withEndLine(int endLine) {
        this.endLine = endLine;
        return this;
    }

    @JsonProperty("StartColumn")
    public int getStartColumn() {
        return startColumn;
    }

    @JsonProperty("StartColumn")
    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public Location withStartColumn(int startColumn) {
        this.startColumn = startColumn;
        return this;
    }

    @JsonProperty("EndColumn")
    public int getEndColumn() {
        return endColumn;
    }

    @JsonProperty("EndColumn")
    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public Location withEndColumn(int endColumn) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("sourceFile", sourceFile).append("startLine", startLine).append("endLine", endLine).append("startColumn", startColumn).append("endColumn", endColumn).append("explanation", explanation).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(startLine).append(sourceFile).append(explanation).append(endLine).append(startColumn).append(endColumn).toHashCode();
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
        return new EqualsBuilder().append(startLine, rhs.startLine).append(sourceFile, rhs.sourceFile).append(explanation, rhs.explanation).append(endLine, rhs.endLine).append(startColumn, rhs.startColumn).append(endColumn, rhs.endColumn).isEquals();
    }

}

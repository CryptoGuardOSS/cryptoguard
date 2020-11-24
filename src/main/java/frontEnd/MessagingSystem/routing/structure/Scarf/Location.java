/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * LocationType
 *
 * <p>
 *
 * @author franceme
 * @version 03.07.01
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

  private static final long serialVersionUID = 1200285316537190850L;
  /** (Required) */
  @JsonProperty("SourceFile")
  private String sourceFile;

  @JsonProperty("StartLine")
  private Integer startLine;

  @JsonProperty("EndLine")
  private Integer endLine;

  @JsonProperty("StartColumn")
  private Integer startColumn;

  @JsonProperty("EndColumn")
  private Integer endColumn;

  @JsonProperty("Explanation")
  private String explanation;

  /** No args constructor for use in serialization */
  public Location() {}

  /**
   * Constructor for Location.
   *
   * @param startLine a Integer.
   * @param sourceFile a {@link java.lang.String} object.
   * @param explanation a {@link java.lang.String} object.
   * @param endLine a Integer.
   * @param startColumn a Integer.
   * @param endColumn a Integer.
   */
  public Location(
      String sourceFile,
      Integer startLine,
      Integer endLine,
      Integer startColumn,
      Integer endColumn,
      String explanation) {
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
   * withSourceFile.
   *
   * @param sourceFile a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Location} object.
   */
  public Location withSourceFile(String sourceFile) {
    this.sourceFile = sourceFile;
    return this;
  }

  /**
   * Getter for the field <code>startLine</code>.
   *
   * @return a Integer.
   */
  @JsonProperty("StartLine")
  public Integer getStartLine() {
    return startLine;
  }

  /**
   * Setter for the field <code>startLine</code>.
   *
   * @param startLine a Integer.
   */
  @JsonProperty("StartLine")
  public void setStartLine(Integer startLine) {
    this.startLine = startLine;
  }

  /**
   * withStartLine.
   *
   * @param startLine a Integer.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Location} object.
   */
  public Location withStartLine(Integer startLine) {
    this.startLine = startLine;
    return this;
  }

  /**
   * Getter for the field <code>endLine</code>.
   *
   * @return a Integer.
   */
  @JsonProperty("EndLine")
  public Integer getEndLine() {
    return endLine;
  }

  /**
   * Setter for the field <code>endLine</code>.
   *
   * @param endLine a Integer.
   */
  @JsonProperty("EndLine")
  public void setEndLine(Integer endLine) {
    this.endLine = endLine;
  }

  /**
   * withEndLine.
   *
   * @param endLine a Integer.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Location} object.
   */
  public Location withEndLine(Integer endLine) {
    this.endLine = endLine;
    return this;
  }

  /**
   * Getter for the field <code>startColumn</code>.
   *
   * @return a Integer.
   */
  @JsonProperty("StartColumn")
  public Integer getStartColumn() {
    return startColumn;
  }

  /**
   * Setter for the field <code>startColumn</code>.
   *
   * @param startColumn a Integer.
   */
  @JsonProperty("StartColumn")
  public void setStartColumn(Integer startColumn) {
    this.startColumn = startColumn;
  }

  /**
   * withStartColumn.
   *
   * @param startColumn a Integer.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Location} object.
   */
  public Location withStartColumn(Integer startColumn) {
    this.startColumn = startColumn;
    return this;
  }

  /**
   * Getter for the field <code>endColumn</code>.
   *
   * @return a Integer.
   */
  @JsonProperty("EndColumn")
  public Integer getEndColumn() {
    return endColumn;
  }

  /**
   * Setter for the field <code>endColumn</code>.
   *
   * @param endColumn a Integer.
   */
  @JsonProperty("EndColumn")
  public void setEndColumn(Integer endColumn) {
    this.endColumn = endColumn;
  }

  /**
   * withEndColumn.
   *
   * @param endColumn a Integer.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Location} object.
   */
  public Location withEndColumn(Integer endColumn) {
    this.endColumn = endColumn;
    return this;
  }

  /**
   * Getter for the field <code>explanation</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("Explanation")
  public String getExplanation() {
    return explanation;
  }

  /**
   * Setter for the field <code>explanation</code>.
   *
   * @param explanation a {@link java.lang.String} object.
   */
  @JsonProperty("Explanation")
  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  /**
   * withExplanation.
   *
   * @param explanation a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Location} object.
   */
  public Location withExplanation(String explanation) {
    this.explanation = explanation;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("sourceFile", sourceFile)
        .append("startLine", startLine)
        .append("endLine", endLine)
        .append("startColumn", startColumn)
        .append("endColumn", endColumn)
        .append("explanation", explanation)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(startLine)
        .append(sourceFile)
        .append(explanation)
        .append(endLine)
        .append(startColumn)
        .append(endColumn)
        .toHashCode();
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
    return new EqualsBuilder()
        .append(startLine, rhs.startLine)
        .append(sourceFile, rhs.sourceFile)
        .append(explanation, rhs.explanation)
        .append(endLine, rhs.endLine)
        .append(startColumn, rhs.startColumn)
        .append(endColumn, rhs.endColumn)
        .isEquals();
  }
}

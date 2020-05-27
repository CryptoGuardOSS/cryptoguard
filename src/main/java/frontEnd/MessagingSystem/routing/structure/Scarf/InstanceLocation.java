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
 * InstanceLocation class.
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"Xpath", "LineNum"})
public class InstanceLocation implements Serializable {

  private static final long serialVersionUID = -2084587122423583152L;

  @JsonProperty("Xpath")
  private String xpath;
  /** LineNum */
  @JsonProperty("LineNum")
  private LineNum lineNum;

  /** No args constructor for use in serialization */
  public InstanceLocation() {}

  /**
   * Constructor for InstanceLocation.
   *
   * @param lineNum a {@link frontEnd.MessagingSystem.routing.structure.Scarf.LineNum} object.
   * @param xpath a {@link java.lang.String} object.
   */
  public InstanceLocation(String xpath, LineNum lineNum) {
    super();
    this.xpath = xpath;
    this.lineNum = lineNum;
  }

  /**
   * Getter for the field <code>xpath</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("Xpath")
  public String getXpath() {
    return xpath;
  }

  /**
   * Setter for the field <code>xpath</code>.
   *
   * @param xpath a {@link java.lang.String} object.
   */
  @JsonProperty("Xpath")
  public void setXpath(String xpath) {
    this.xpath = xpath;
  }

  /**
   * withXpath.
   *
   * @param xpath a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.InstanceLocation} object.
   */
  public InstanceLocation withXpath(String xpath) {
    this.xpath = xpath;
    return this;
  }

  /**
   * LineNum
   *
   * <p>
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.LineNum} object.
   */
  @JsonProperty("LineNum")
  public LineNum getLineNum() {
    return lineNum;
  }

  /**
   * LineNum
   *
   * <p>
   *
   * @param lineNum a {@link frontEnd.MessagingSystem.routing.structure.Scarf.LineNum} object.
   */
  @JsonProperty("LineNum")
  public void setLineNum(LineNum lineNum) {
    this.lineNum = lineNum;
  }

  /**
   * withLineNum.
   *
   * @param lineNum a {@link frontEnd.MessagingSystem.routing.structure.Scarf.LineNum} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.InstanceLocation} object.
   */
  public InstanceLocation withLineNum(LineNum lineNum) {
    this.lineNum = lineNum;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("xpath", xpath).append("lineNum", lineNum).toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(lineNum).append(xpath).toHashCode();
  }

  /** {@inheritDoc} */
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

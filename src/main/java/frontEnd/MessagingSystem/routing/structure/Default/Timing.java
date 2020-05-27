/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Timing
 *
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"Total", "Scan", "TimeScale"})
public class Timing implements Serializable {

  private static final long serialVersionUID = -7831748094033719806L;
  /** (Required) */
  @JsonProperty("Total")
  private String total;
  /** (Required) */
  @JsonProperty("Scan")
  private String scan;
  /** (Required) */
  @JsonProperty("TimeScale")
  private String timeScale;

  /** No args constructor for use in serialization */
  public Timing() {}

  /**
   * Constructor for Timing.
   *
   * @param total a {@link java.lang.String} object.
   * @param timeScale a {@link java.lang.String} object.
   * @param scan a {@link java.lang.String} object.
   */
  public Timing(String total, String scan, String timeScale) {
    super();
    this.total = total;
    this.scan = scan;
    this.timeScale = timeScale;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("Total")
  public String getTotal() {
    return total;
  }

  /**
   * (Required)
   *
   * @param total a {@link java.lang.String} object.
   */
  @JsonProperty("Total")
  public void setTotal(String total) {
    this.total = total;
  }

  /**
   * withTotal.
   *
   * @param total a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Timing} object.
   */
  public Timing withTotal(String total) {
    this.total = total;
    return this;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("Scan")
  public String getScan() {
    return scan;
  }

  /**
   * (Required)
   *
   * @param scan a {@link java.lang.String} object.
   */
  @JsonProperty("Scan")
  public void setScan(String scan) {
    this.scan = scan;
  }

  /**
   * withScan.
   *
   * @param scan a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Timing} object.
   */
  public Timing withScan(String scan) {
    this.scan = scan;
    return this;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("TimeScale")
  public String getTimeScale() {
    return timeScale;
  }

  /**
   * (Required)
   *
   * @param timeScale a {@link java.lang.String} object.
   */
  @JsonProperty("TimeScale")
  public void setTimeScale(String timeScale) {
    this.timeScale = timeScale;
  }

  /**
   * withTimeScale.
   *
   * @param timeScale a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Timing} object.
   */
  public Timing withTimeScale(String timeScale) {
    this.timeScale = timeScale;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("total", total)
        .append("scan", scan)
        .append("timeScale", timeScale)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(total).append(timeScale).append(scan).toHashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Timing) == false) {
      return false;
    }
    Timing rhs = ((Timing) other);
    return new EqualsBuilder()
        .append(total, rhs.total)
        .append(timeScale, rhs.timeScale)
        .append(scan, rhs.scan)
        .isEquals();
  }
}

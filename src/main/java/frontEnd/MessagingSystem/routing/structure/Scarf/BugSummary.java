/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * BugSummary class.
 *
 * @author CryptoguardTeam Created on 4/5/19.
 * @version 03.07.01
 * @since 03.04.08
 *     <p>This is the only custom created class as the BugSummary container was self contained
 *     within the Analyzer Report
 *     <p>{Description Here}
 */
public class BugSummary {

  //region Attribute
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "BugCategory")
  private List<BugCategory> summaryContainer;

  //endregion
  //region Getters/Setters

  /**
   * Getter for the field <code>summaryContainer</code>.
   *
   * @return a {@link java.util.List} object.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  public List<BugCategory> getSummaryContainer() {
    if (this.summaryContainer == null) this.summaryContainer = new ArrayList<>();
    return summaryContainer;
  }

  /**
   * Setter for the field <code>summaryContainer</code>.
   *
   * @param summaryContainer a {@link java.util.List} object.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  public void setSummaryContainer(List<BugCategory> summaryContainer) {
    this.summaryContainer = summaryContainer;
  }

  /**
   * addBugSummary.
   *
   * @param summary a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory} object.
   */
  public void addBugSummary(BugCategory summary) {
    if (this.summaryContainer == null) this.summaryContainer = new ArrayList<>();
    this.summaryContainer.add(summary);
  }

  //endregion
  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("summaryContainer", summaryContainer).toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(summaryContainer).toHashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof BugSummary) == false) {
      return false;
    }
    BugSummary rhs = ((BugSummary) other);
    return new EqualsBuilder().append(summaryContainer, rhs.summaryContainer).isEquals();
  }
  //endregion
}

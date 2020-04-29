/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Issues class.
 *
 * @author franceme Created on 05/01/2019.
 * @version 03.07.01
 * @since 03.05.02
 *     <p>{Description Here}
 */
public class Issues implements Serializable {

  private static final long serialVersionUID = -2265209324602399901L;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "Issue")
  @JsonProperty("Issues")
  private List<Issue> issues = new ArrayList<>();

  /** Constructor for Issues. */
  public Issues() {}

  /**
   * Issues
   *
   * <p>
   *
   * <p>(Required)
   *
   * @return a {@link java.util.List} object.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "Issue")
  @JsonProperty("Issues")
  public List<Issue> getIssues() {
    return issues;
  }

  /**
   * Issues
   *
   * <p>
   *
   * <p>(Required)
   *
   * @param issues a {@link java.util.List} object.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "Issue")
  @JsonProperty("Issues")
  public void setIssues(List<Issue> issues) {
    this.issues = issues;
  }

  /**
   * withIssues.
   *
   * @param issues a {@link java.util.List} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issues} object.
   */
  public Issues withIssues(List<Issue> issues) {
    this.issues = issues;
    return this;
  }
}

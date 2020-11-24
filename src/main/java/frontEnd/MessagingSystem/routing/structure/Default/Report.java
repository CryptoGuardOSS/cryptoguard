/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.Listing;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Report
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "-SchemaVersion",
  "-DateTime",
  "-UUID",
  "projectName",
  "ProjectVersion",
  "Target",
  "Issues",
  "Heuristics"
})
public class Report implements Serializable {

  private static final long serialVersionUID = 1588567781296372468L;
  /**
   * The Schema Version
   *
   * <p>(Required)
   */
  @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
  @JsonProperty("_SchemaVersion")
  private int schemaVersion;
  /**
   * Time the scan was instantiated
   *
   * <p>(Required)
   */
  @JacksonXmlProperty(isAttribute = true, localName = "DateTime")
  @JsonProperty("_DateTime")
  private String dateTime;
  /**
   * string Time the scan was instantiated
   *
   * <p>(Required)
   */
  @JacksonXmlProperty(isAttribute = true, localName = "UUID")
  @JsonProperty("_UUID")
  private String uUID;
  /**
   * The Project Name
   *
   * <p>(Required)
   */
  @JsonProperty("projectName")
  private String projectName;
  /**
   * The Project Version
   *
   * <p>(Required)
   */
  @JsonProperty("ProjectVersion")
  private String projectVersion;
  /** TargetInfo */
  @JsonProperty("Target")
  private Target target;
  /**
   * Issues
   *
   * <p>(Required)
   */
  @JsonProperty("Issues")
  private List<Issue> issues = new ArrayList<>();
  /** HeuristicsType */
  @JsonProperty("Heuristics")
  private Heuristics heuristics;

  /** No args constructor for use in serialization */
  public Report() {}

  /**
   * Constructor for Report.
   *
   * @param projectVersion a {@link java.lang.String} object.
   * @param dateTime a {@link java.lang.String} object.
   * @param heuristics a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics}
   *     object.
   * @param issues a {@link java.util.List} object.
   * @param target a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   * @param uUID a {@link java.lang.String} object.
   * @param projectName a {@link java.lang.String} object.
   * @param schemaVersion a int.
   */
  public Report(
      int schemaVersion,
      String dateTime,
      String uUID,
      String projectName,
      String projectVersion,
      Target target,
      List<Issue> issues,
      Heuristics heuristics) {
    super();
    this.schemaVersion = schemaVersion;
    this.dateTime = dateTime;
    this.uUID = uUID;
    this.projectName = projectName;
    this.projectVersion = projectVersion;
    this.target = target;
    this.issues = issues;
    this.heuristics = heuristics;
  }

  /**
   * deserialize.
   *
   * @param file a {@link java.io.File} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static Report deserialize(File file) throws ExceptionHandler {
    return deserialize(file, Listing.Default);
  }

  /**
   * deserialize.
   *
   * @param file a {@link java.io.File} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport} object.
   * @param readType a {@link frontEnd.MessagingSystem.routing.Listing} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static Report deserialize(File file, Listing readType) throws ExceptionHandler {
    try {

      ObjectMapper deserializer = readType.getJacksonType().getOutputMapper();

      return deserializer.readValue(file, Report.class);

    } catch (IOException e) {
      throw new ExceptionHandler(
          "Issue de-serializing file: " + file.getName(), ExceptionId.FILE_READ);
    }
  }

  /**
   * The Schema Version
   *
   * <p>(Required)
   *
   * @return a int.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
  @JsonProperty("_SchemaVersion")
  public int getSchemaVersion() {
    return schemaVersion;
  }

  /**
   * The Schema Version
   *
   * <p>(Required)
   *
   * @param schemaVersion a int.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
  @JsonProperty("_SchemaVersion")
  public void setSchemaVersion(int schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  /**
   * withSchemaVersion.
   *
   * @param schemaVersion a int.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   */
  public Report withSchemaVersion(int schemaVersion) {
    this.schemaVersion = schemaVersion;
    return this;
  }

  /**
   * Time the scan was instantiated
   *
   * <p>(Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "DateTime")
  @JsonProperty("_DateTime")
  public String getDateTime() {
    return dateTime;
  }

  /**
   * Time the scan was instantiated
   *
   * <p>(Required)
   *
   * @param dateTime a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "DateTime")
  @JsonProperty("_DateTime")
  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  /**
   * withDateTime.
   *
   * @param dateTime a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   */
  public Report withDateTime(String dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  /**
   * string Time the scan was instantiated
   *
   * <p>(Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "UUID")
  @JsonProperty("_UUID")
  public String getUUID() {
    return uUID;
  }

  /**
   * string Time the scan was instantiated
   *
   * <p>(Required)
   *
   * @param uUID a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "UUID")
  @JsonProperty("_UUID")
  public void setUUID(String uUID) {
    this.uUID = uUID;
  }

  /**
   * withUUID.
   *
   * @param uUID a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   */
  public Report withUUID(String uUID) {
    this.uUID = uUID;
    return this;
  }

  /**
   * The Project Name
   *
   * <p>(Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("projectName")
  public String getProjectName() {
    return projectName;
  }

  /**
   * The Project Name
   *
   * <p>(Required)
   *
   * @param projectName a {@link java.lang.String} object.
   */
  @JsonProperty("projectName")
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * withProjectName.
   *
   * @param projectName a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   */
  public Report withProjectName(String projectName) {
    this.projectName = projectName;
    return this;
  }

  /**
   * The Project Version
   *
   * <p>(Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("ProjectVersion")
  public String getProjectVersion() {
    return projectVersion;
  }

  /**
   * The Project Version
   *
   * <p>(Required)
   *
   * @param projectVersion a {@link java.lang.String} object.
   */
  @JsonProperty("ProjectVersion")
  public void setProjectVersion(String projectVersion) {
    this.projectVersion = projectVersion;
  }

  /**
   * withProjectVersion.
   *
   * @param projectVersion a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   */
  public Report withProjectVersion(String projectVersion) {
    this.projectVersion = projectVersion;
    return this;
  }

  /**
   * TargetInfo
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  @JsonProperty("Target")
  public Target getTarget() {
    return target;
  }

  /**
   * TargetInfo
   *
   * @param target a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  @JsonProperty("Target")
  public void setTarget(Target target) {
    this.target = target;
  }

  /**
   * withTarget.
   *
   * @param target a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   */
  public Report withTarget(Target target) {
    this.target = target;
    return this;
  }

  /**
   * Issues
   *
   * <p>(Required)
   *
   * @return a {@link java.util.List} object.
   */
  @JsonProperty("Issues")
  public List<Issue> getIssues() {
    return issues;
  }

  /**
   * Issues
   *
   * <p>(Required)
   *
   * @param issues a {@link java.util.List} object.
   */
  @JsonProperty("Issues")
  public void setIssues(List<Issue> issues) {
    this.issues = issues;
  }

  /**
   * withIssues.
   *
   * @param issues a {@link java.util.List} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
   */
  public Report withIssues(List<Issue> issues) {
    this.issues = issues;
    return this;
  }

  /**
   * HeuristicsType
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  @JsonProperty("Heuristics")
  public Heuristics getHeuristics() {
    return heuristics;
  }

  /**
   * HeuristicsType
   *
   * @param heuristics a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics}
   *     object.
   */
  @JsonProperty("Heuristics")
  public void setHeuristics(Heuristics heuristics) {
    this.heuristics = heuristics;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("schemaVersion", schemaVersion)
        .append("dateTime", dateTime)
        .append("uUID", uUID)
        .append("projectName", projectName)
        .append("projectVersion", projectVersion)
        .append("target", target)
        .append("issues", issues)
        .append("heuristics", heuristics)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(projectVersion)
        .append(dateTime)
        .append(heuristics)
        .append(issues)
        .append(target)
        .append(uUID)
        .append(projectName)
        .append(schemaVersion)
        .toHashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Report) == false) {
      return false;
    }
    Report rhs = ((Report) other);
    return new EqualsBuilder()
        .append(projectVersion, rhs.projectVersion)
        .append(dateTime, rhs.dateTime)
        .append(heuristics, rhs.heuristics)
        .append(issues, rhs.issues)
        .append(target, rhs.target)
        .append(uUID, rhs.uUID)
        .append(projectName, rhs.projectName)
        .append(schemaVersion, rhs.schemaVersion)
        .isEquals();
  }
}

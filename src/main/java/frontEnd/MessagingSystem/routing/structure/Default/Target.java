/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TargetInfo
 *
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "-PropertiesFilePath",
  "-ComputerOS",
  "-JVMVersion",
  "-ProjectName",
  "-ProjectVersion",
  "RawCommand",
  "Timing",
  "FullPath",
  "Type",
  "ProjectType",
  "TargetSources"
})
public class Target implements Serializable {

  private static final long serialVersionUID = 8557085304013589L;

  @JacksonXmlProperty(isAttribute = true, localName = "PropertiesFilePath")
  @JsonProperty("_PropertiesFilePath")
  private String propertiesFilePath;
  /** (Required) */
  @JacksonXmlProperty(isAttribute = true, localName = "ComputerOS")
  @JsonProperty("_ComputerOS")
  private String computerOS;
  /** (Required) */
  @JacksonXmlProperty(isAttribute = true, localName = "JVMVersion")
  @JsonProperty("_JVMVersion")
  private String jVMVersion;

  @JacksonXmlProperty(isAttribute = true, localName = "ProjectName")
  @JsonProperty("_ProjectName")
  private String projectName;

  @JacksonXmlProperty(isAttribute = true, localName = "ProjectVersion")
  @JsonProperty("_ProjectVersion")
  private String projectVersion;
  /** (Required) */
  @JsonProperty("RawCommand")
  private String rawCommand;
  /**
   * Timing
   *
   * <p>
   */
  @JsonProperty("Timing")
  private Timing timing;
  /** (Required) */
  @JsonProperty("FullPath")
  private String fullPath;
  /** (Required) */
  @JsonProperty("Type")
  private Type type;

  @JsonProperty("ProjectType")
  private ProjectType projectType;
  /**
   * The Sources of the Target
   *
   * <p>
   */
  @JsonProperty("TargetSources")
  private List<String> targetSources = new ArrayList<>();

  /** No args constructor for use in serialization */
  public Target() {}

  /**
   * Constructor for Target.
   *
   * @param projectVersion a {@link java.lang.String} object.
   * @param propertiesFilePath a {@link java.lang.String} object.
   * @param computerOS a {@link java.lang.String} object.
   * @param targetSources a {@link java.util.List} object.
   * @param rawCommand a {@link java.lang.String} object.
   * @param projectType a {@link
   *     frontEnd.MessagingSystem.routing.structure.Default.Target.ProjectType} object.
   * @param fullPath a {@link java.lang.String} object.
   * @param type a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.Type} object.
   * @param timing a {@link frontEnd.MessagingSystem.routing.structure.Default.Timing} object.
   * @param projectName a {@link java.lang.String} object.
   * @param jVMVersion a {@link java.lang.String} object.
   */
  public Target(
      String propertiesFilePath,
      String computerOS,
      String jVMVersion,
      String projectName,
      String projectVersion,
      String rawCommand,
      Timing timing,
      String fullPath,
      Type type,
      ProjectType projectType,
      List<String> targetSources) {
    super();
    this.propertiesFilePath = propertiesFilePath;
    this.computerOS = computerOS;
    this.jVMVersion = jVMVersion;
    this.projectName = projectName;
    this.projectVersion = projectVersion;
    this.rawCommand = rawCommand;
    this.timing = timing;
    this.fullPath = fullPath;
    this.type = type;
    this.projectType = projectType;
    this.targetSources = targetSources;
  }

  /**
   * Getter for the field <code>propertiesFilePath</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "PropertiesFilePath")
  @JsonProperty("_PropertiesFilePath")
  public String getPropertiesFilePath() {
    return propertiesFilePath;
  }

  /**
   * Setter for the field <code>propertiesFilePath</code>.
   *
   * @param propertiesFilePath a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "PropertiesFilePath")
  @JsonProperty("_PropertiesFilePath")
  public void setPropertiesFilePath(String propertiesFilePath) {
    this.propertiesFilePath = propertiesFilePath;
  }

  /**
   * withPropertiesFilePath.
   *
   * @param propertiesFilePath a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withPropertiesFilePath(String propertiesFilePath) {
    this.propertiesFilePath = propertiesFilePath;
    return this;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "ComputerOS")
  @JsonProperty("_ComputerOS")
  public String getComputerOS() {
    return computerOS;
  }

  /**
   * (Required)
   *
   * @param computerOS a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "ComputerOS")
  @JsonProperty("_ComputerOS")
  public void setComputerOS(String computerOS) {
    this.computerOS = computerOS;
  }

  /**
   * withComputerOS.
   *
   * @param computerOS a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withComputerOS(String computerOS) {
    this.computerOS = computerOS;
    return this;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "JVMVersion")
  @JsonProperty("_JVMVersion")
  public String getJVMVersion() {
    return jVMVersion;
  }

  /**
   * (Required)
   *
   * @param jVMVersion a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "JVMVersion")
  @JsonProperty("_JVMVersion")
  public void setJVMVersion(String jVMVersion) {
    this.jVMVersion = jVMVersion;
  }

  /**
   * withJVMVersion.
   *
   * @param jVMVersion a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withJVMVersion(String jVMVersion) {
    this.jVMVersion = jVMVersion;
    return this;
  }

  /**
   * Getter for the field <code>projectName</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "ProjectName")
  @JsonProperty("_ProjectName")
  public String getProjectName() {
    return projectName;
  }

  /**
   * Setter for the field <code>projectName</code>.
   *
   * @param projectName a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "ProjectName")
  @JsonProperty("_ProjectName")
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * withProjectName.
   *
   * @param projectName a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withProjectName(String projectName) {
    this.projectName = projectName;
    return this;
  }

  /**
   * Getter for the field <code>projectVersion</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "ProjectVersion")
  @JsonProperty("_ProjectVersion")
  public String getProjectVersion() {
    return projectVersion;
  }

  /**
   * Setter for the field <code>projectVersion</code>.
   *
   * @param projectVersion a {@link java.lang.String} object.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "ProjectVersion")
  @JsonProperty("_ProjectVersion")
  public void setProjectVersion(String projectVersion) {
    this.projectVersion = projectVersion;
  }

  /**
   * withProjectVersion.
   *
   * @param projectVersion a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withProjectVersion(String projectVersion) {
    this.projectVersion = projectVersion;
    return this;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("RawCommand")
  public String getRawCommand() {
    return rawCommand;
  }

  /**
   * (Required)
   *
   * @param rawCommand a {@link java.lang.String} object.
   */
  @JsonProperty("RawCommand")
  public void setRawCommand(String rawCommand) {
    this.rawCommand = rawCommand;
  }

  /**
   * withRawCommand.
   *
   * @param rawCommand a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withRawCommand(String rawCommand) {
    this.rawCommand = rawCommand;
    return this;
  }

  /**
   * Timing
   *
   * <p>
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Timing} object.
   */
  @JsonProperty("Timing")
  public Timing getTiming() {
    return timing;
  }

  /**
   * Timing
   *
   * <p>
   *
   * @param timing a {@link frontEnd.MessagingSystem.routing.structure.Default.Timing} object.
   */
  @JsonProperty("Timing")
  public void setTiming(Timing timing) {
    this.timing = timing;
  }

  /**
   * withTiming.
   *
   * @param timing a {@link frontEnd.MessagingSystem.routing.structure.Default.Timing} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withTiming(Timing timing) {
    this.timing = timing;
    return this;
  }

  /**
   * (Required)
   *
   * @return a {@link java.lang.String} object.
   */
  @JsonProperty("FullPath")
  public String getFullPath() {
    return fullPath;
  }

  /**
   * (Required)
   *
   * @param fullPath a {@link java.lang.String} object.
   */
  @JsonProperty("FullPath")
  public void setFullPath(String fullPath) {
    this.fullPath = fullPath;
  }

  /**
   * withFullPath.
   *
   * @param fullPath a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withFullPath(String fullPath) {
    this.fullPath = fullPath;
    return this;
  }

  /**
   * (Required)
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.Type} object.
   */
  @JsonProperty("Type")
  public Type getType() {
    return type;
  }

  /**
   * (Required)
   *
   * @param type a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.Type} object.
   */
  @JsonProperty("Type")
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * withType.
   *
   * @param type a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.Type} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withType(Type type) {
    this.type = type;
    return this;
  }

  /**
   * Getter for the field <code>projectType</code>.
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.ProjectType} object.
   */
  @JsonProperty("ProjectType")
  public ProjectType getProjectType() {
    return projectType;
  }

  /**
   * Setter for the field <code>projectType</code>.
   *
   * @param projectType a {@link
   *     frontEnd.MessagingSystem.routing.structure.Default.Target.ProjectType} object.
   */
  @JsonProperty("ProjectType")
  public void setProjectType(ProjectType projectType) {
    this.projectType = projectType;
  }

  /**
   * withProjectType.
   *
   * @param projectType a {@link
   *     frontEnd.MessagingSystem.routing.structure.Default.Target.ProjectType} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withProjectType(ProjectType projectType) {
    this.projectType = projectType;
    return this;
  }

  /**
   * The Sources of the Target
   *
   * <p>
   *
   * @return a {@link java.util.List} object.
   */
  @JsonProperty("TargetSources")
  public List<String> getTargetSources() {
    return targetSources;
  }

  /**
   * The Sources of the Target
   *
   * <p>
   *
   * @param targetSources a {@link java.util.List} object.
   */
  @JsonProperty("TargetSources")
  public void setTargetSources(List<String> targetSources) {
    this.targetSources = targetSources;
  }

  /**
   * withTargetSources.
   *
   * @param targetSources a {@link java.util.List} object.
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
   */
  public Target withTargetSources(List<String> targetSources) {
    this.targetSources = targetSources;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("propertiesFilePath", propertiesFilePath)
        .append("computerOS", computerOS)
        .append("jVMVersion", jVMVersion)
        .append("projectName", projectName)
        .append("projectVersion", projectVersion)
        .append("rawCommand", rawCommand)
        .append("timing", timing)
        .append("fullPath", fullPath)
        .append("type", type)
        .append("projectType", projectType)
        .append("targetSources", targetSources)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(projectVersion)
        .append(propertiesFilePath)
        .append(computerOS)
        .append(targetSources)
        .append(rawCommand)
        .append(projectType)
        .append(fullPath)
        .append(type)
        .append(timing)
        .append(projectName)
        .append(jVMVersion)
        .toHashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Target) == false) {
      return false;
    }
    Target rhs = ((Target) other);
    return new EqualsBuilder()
        .append(projectVersion, rhs.projectVersion)
        .append(propertiesFilePath, rhs.propertiesFilePath)
        .append(computerOS, rhs.computerOS)
        .append(targetSources, rhs.targetSources)
        .append(rawCommand, rhs.rawCommand)
        .append(projectType, rhs.projectType)
        .append(fullPath, rhs.fullPath)
        .append(type, rhs.type)
        .append(timing, rhs.timing)
        .append(projectName, rhs.projectName)
        .append(jVMVersion, rhs.jVMVersion)
        .isEquals();
  }

  public enum ProjectType {
    GRADLE("Gradle"),
    MAVEN("Maven");
    private static final Map<String, ProjectType> CONSTANTS = new HashMap<>();

    static {
      for (ProjectType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ProjectType(String value) {
      this.value = value;
    }

    @JsonCreator
    public static ProjectType fromValue(String value) {
      ProjectType constant = CONSTANTS.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

    @Override
    public String toString() {
      return this.value;
    }

    @JsonValue
    public String value() {
      return this.value;
    }
  }

  public enum Type {
    APK("APK"),
    JAR("JAR"),
    SOURCE("Source"),
    JAVA("Java"),
    CLASS("Class");
    private static final Map<String, Type> CONSTANTS = new HashMap<>();

    static {
      for (Type c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    Type(String value) {
      this.value = value;
    }

    @JsonCreator
    public static Type fromValue(String value) {
      Type constant = CONSTANTS.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

    @Override
    public String toString() {
      return this.value;
    }

    @JsonValue
    public String value() {
      return this.value;
    }
  }
}

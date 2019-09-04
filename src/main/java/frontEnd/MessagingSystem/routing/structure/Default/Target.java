package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TargetInfo
 * <p>
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

    @JacksonXmlProperty(isAttribute = true, localName = "PropertiesFilePath")
    @JsonProperty("_PropertiesFilePath")
    private String propertiesFilePath;
    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "ComputerOS")
    @JsonProperty("_ComputerOS")
    private String computerOS;
    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "JVMVersion")
    @JsonProperty("_JVMVersion")
    private String jVMVersion;
    @JacksonXmlProperty(isAttribute = true, localName = "ProjectName")
    @JsonProperty("_ProjectName")
    private String projectName;
    @JacksonXmlProperty(isAttribute = true, localName = "ProjectVersion")
    @JsonProperty("_ProjectVersion")
    private String projectVersion;
    /**
     * (Required)
     */
    @JsonProperty("RawCommand")
    private String rawCommand;
    /**
     * Timing
     * <p>
     */
    @JsonProperty("Timing")
    private Timing timing;
    /**
     * (Required)
     */
    @JsonProperty("FullPath")
    private String fullPath;
    /**
     * (Required)
     */
    @JsonProperty("Type")
    private Type type;
    @JsonProperty("ProjectType")
    private ProjectType projectType;
    /**
     * The Sources of the Target
     * <p>
     */
    @JsonProperty("TargetSources")
    private List<String> targetSources = new ArrayList<String>();
    private final static long serialVersionUID = 8557085304013589L;

    /**
     * No args constructor for use in serialization
     */
    public Target() {
    }

    /**
     * @param projectVersion
     * @param propertiesFilePath
     * @param computerOS
     * @param targetSources
     * @param rawCommand
     * @param projectType
     * @param fullPath
     * @param type
     * @param timing
     * @param projectName
     * @param jVMVersion
     */
    public Target(String propertiesFilePath, String computerOS, String jVMVersion, String projectName, String projectVersion, String rawCommand, Timing timing, String fullPath, Type type, ProjectType projectType, List<String> targetSources) {
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

    @JacksonXmlProperty(isAttribute = true, localName = "PropertiesFilePath")
    @JsonProperty("_PropertiesFilePath")
    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "PropertiesFilePath")
    @JsonProperty("_PropertiesFilePath")
    public void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    public Target withPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
        return this;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "ComputerOS")
    @JsonProperty("_ComputerOS")
    public String getComputerOS() {
        return computerOS;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "ComputerOS")
    @JsonProperty("_ComputerOS")
    public void setComputerOS(String computerOS) {
        this.computerOS = computerOS;
    }

    public Target withComputerOS(String computerOS) {
        this.computerOS = computerOS;
        return this;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "JVMVersion")
    @JsonProperty("_JVMVersion")
    public String getJVMVersion() {
        return jVMVersion;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "JVMVersion")
    @JsonProperty("_JVMVersion")
    public void setJVMVersion(String jVMVersion) {
        this.jVMVersion = jVMVersion;
    }

    public Target withJVMVersion(String jVMVersion) {
        this.jVMVersion = jVMVersion;
        return this;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "ProjectName")
    @JsonProperty("_ProjectName")
    public String getProjectName() {
        return projectName;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "ProjectName")
    @JsonProperty("_ProjectName")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Target withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "ProjectVersion")
    @JsonProperty("_ProjectVersion")
    public String getProjectVersion() {
        return projectVersion;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "ProjectVersion")
    @JsonProperty("_ProjectVersion")
    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public Target withProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("RawCommand")
    public String getRawCommand() {
        return rawCommand;
    }

    /**
     * (Required)
     */
    @JsonProperty("RawCommand")
    public void setRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
    }

    public Target withRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
        return this;
    }

    /**
     * Timing
     * <p>
     */
    @JsonProperty("Timing")
    public Timing getTiming() {
        return timing;
    }

    /**
     * Timing
     * <p>
     */
    @JsonProperty("Timing")
    public void setTiming(Timing timing) {
        this.timing = timing;
    }

    public Target withTiming(Timing timing) {
        this.timing = timing;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("FullPath")
    public String getFullPath() {
        return fullPath;
    }

    /**
     * (Required)
     */
    @JsonProperty("FullPath")
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Target withFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Type")
    public Type getType() {
        return type;
    }

    /**
     * (Required)
     */
    @JsonProperty("Type")
    public void setType(Type type) {
        this.type = type;
    }

    public Target withType(Type type) {
        this.type = type;
        return this;
    }

    @JsonProperty("ProjectType")
    public ProjectType getProjectType() {
        return projectType;
    }

    @JsonProperty("ProjectType")
    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public Target withProjectType(ProjectType projectType) {
        this.projectType = projectType;
        return this;
    }

    /**
     * The Sources of the Target
     * <p>
     */
    @JsonProperty("TargetSources")
    public List<String> getTargetSources() {
        return targetSources;
    }

    /**
     * The Sources of the Target
     * <p>
     */
    @JsonProperty("TargetSources")
    public void setTargetSources(List<String> targetSources) {
        this.targetSources = targetSources;
    }

    public Target withTargetSources(List<String> targetSources) {
        this.targetSources = targetSources;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("propertiesFilePath", propertiesFilePath).append("computerOS", computerOS).append("jVMVersion", jVMVersion).append("projectName", projectName).append("projectVersion", projectVersion).append("rawCommand", rawCommand).append("timing", timing).append("fullPath", fullPath).append("type", type).append("projectType", projectType).append("targetSources", targetSources).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(projectVersion).append(propertiesFilePath).append(computerOS).append(targetSources).append(rawCommand).append(projectType).append(fullPath).append(type).append(timing).append(projectName).append(jVMVersion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Target) == false) {
            return false;
        }
        Target rhs = ((Target) other);
        return new EqualsBuilder().append(projectVersion, rhs.projectVersion).append(propertiesFilePath, rhs.propertiesFilePath).append(computerOS, rhs.computerOS).append(targetSources, rhs.targetSources).append(rawCommand, rhs.rawCommand).append(projectType, rhs.projectType).append(fullPath, rhs.fullPath).append(type, rhs.type).append(timing, rhs.timing).append(projectName, rhs.projectName).append(jVMVersion, rhs.jVMVersion).isEquals();
    }

    public enum ProjectType {

        GRADLE("Gradle"),
        MAVEN("Maven");
        private final String value;
        private final static Map<String, ProjectType> CONSTANTS = new HashMap<String, ProjectType>();

        static {
            for (ProjectType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private ProjectType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
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

    }

    public enum Type {

        APK("APK"),
        JAR("JAR"),
        SOURCE("Source"),
        JAVA("Java"),
        CLASS("Class");
        private final String value;
        private final static Map<String, Type> CONSTANTS = new HashMap<String, Type>();

        static {
            for (Type c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
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

    }

}

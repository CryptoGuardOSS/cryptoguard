
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Target
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "FullPath",
        "ProjectName",
        "Type",
        "ProjectType"
})
public class Target implements Serializable {

    /**
     * (Required)
     */
    @JsonProperty("FullPath")
    private String fullPath;
    @JsonProperty("ProjectName")
    private String projectName;
    /**
     * (Required)
     */
    @JsonProperty("Type")
    private Type type;
    @JsonProperty("ProjectType")
    private String projectType;
    private final static long serialVersionUID = 1575442056191754862L;

    /**
     * No args constructor for use in serialization
     */
    public Target() {
    }

    /**
     * @param projectType
     * @param fullPath
     * @param type
     * @param projectName
     */
    public Target(String fullPath, String projectName, Type type, String projectType) {
        super();
        this.fullPath = fullPath;
        this.projectName = projectName;
        this.type = type;
        this.projectType = projectType;
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

    @JsonProperty("ProjectName")
    public String getProjectName() {
        return projectName;
    }

    @JsonProperty("ProjectName")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Target withProjectName(String projectName) {
        this.projectName = projectName;
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
    public String getProjectType() {
        return projectType;
    }

    @JsonProperty("ProjectType")
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Target withProjectType(String projectType) {
        this.projectType = projectType;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("fullPath", fullPath).append("projectName", projectName).append("type", type).append("projectType", projectType).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(projectType).append(fullPath).append(type).append(projectName).toHashCode();
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
        return new EqualsBuilder().append(projectType, rhs.projectType).append(fullPath, rhs.fullPath).append(type, rhs.type).append(projectName, rhs.projectName).isEquals();
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

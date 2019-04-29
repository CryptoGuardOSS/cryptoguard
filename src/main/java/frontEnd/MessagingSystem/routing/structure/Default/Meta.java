
package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * MetaData
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "PropertiesFilePath",
        "DateTime",
        "ComputerOS",
        "JVMVersion",
        "RawCommand",
        "UUID",
        "Timing",
        "Target"
})
public class Meta implements Serializable {

    @JsonProperty("PropertiesFilePath")
    private String propertiesFilePath;
    @JsonProperty("DateTime")
    private String dateTime;
    @JsonProperty("ComputerOS")
    private String computerOS;
    @JsonProperty("JVMVersion")
    private String jVMVersion;
    @JsonProperty("RawCommand")
    private String rawCommand;
    @JsonProperty("UUID")
    private String uUID;
    /**
     * Timing
     * <p>
     */
    @JsonProperty("Timing")
    private Timing timing;
    /**
     * Target
     * <p>
     */
    @JsonProperty("Target")
    private Target target;
    private final static long serialVersionUID = 5463909596139029020L;

    /**
     * No args constructor for use in serialization
     */
    public Meta() {
    }

    /**
     * @param propertiesFilePath
     * @param computerOS
     * @param dateTime
     * @param rawCommand
     * @param target
     * @param uUID
     * @param timing
     * @param jVMVersion
     */
    public Meta(String propertiesFilePath, String dateTime, String computerOS, String jVMVersion, String rawCommand, String uUID, Timing timing, Target target) {
        super();
        this.propertiesFilePath = propertiesFilePath;
        this.dateTime = dateTime;
        this.computerOS = computerOS;
        this.jVMVersion = jVMVersion;
        this.rawCommand = rawCommand;
        this.uUID = uUID;
        this.timing = timing;
        this.target = target;
    }

    @JsonProperty("PropertiesFilePath")
    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    @JsonProperty("PropertiesFilePath")
    public void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    public Meta withPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
        return this;
    }

    @JsonProperty("DateTime")
    public String getDateTime() {
        return dateTime;
    }

    @JsonProperty("DateTime")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Meta withDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    @JsonProperty("ComputerOS")
    public String getComputerOS() {
        return computerOS;
    }

    @JsonProperty("ComputerOS")
    public void setComputerOS(String computerOS) {
        this.computerOS = computerOS;
    }

    public Meta withComputerOS(String computerOS) {
        this.computerOS = computerOS;
        return this;
    }

    @JsonProperty("JVMVersion")
    public String getJVMVersion() {
        return jVMVersion;
    }

    @JsonProperty("JVMVersion")
    public void setJVMVersion(String jVMVersion) {
        this.jVMVersion = jVMVersion;
    }

    public Meta withJVMVersion(String jVMVersion) {
        this.jVMVersion = jVMVersion;
        return this;
    }

    @JsonProperty("RawCommand")
    public String getRawCommand() {
        return rawCommand;
    }

    @JsonProperty("RawCommand")
    public void setRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
    }

    public Meta withRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
        return this;
    }

    @JsonProperty("UUID")
    public String getUUID() {
        return uUID;
    }

    @JsonProperty("UUID")
    public void setUUID(String uUID) {
        this.uUID = uUID;
    }

    public Meta withUUID(String uUID) {
        this.uUID = uUID;
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

    public Meta withTiming(Timing timing) {
        this.timing = timing;
        return this;
    }

    /**
     * Target
     * <p>
     */
    @JsonProperty("Target")
    public Target getTarget() {
        return target;
    }

    /**
     * Target
     * <p>
     */
    @JsonProperty("Target")
    public void setTarget(Target target) {
        this.target = target;
    }

    public Meta withTarget(Target target) {
        this.target = target;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("propertiesFilePath", propertiesFilePath).append("dateTime", dateTime).append("computerOS", computerOS).append("jVMVersion", jVMVersion).append("rawCommand", rawCommand).append("uUID", uUID).append("timing", timing).append("target", target).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(propertiesFilePath).append(computerOS).append(dateTime).append(rawCommand).append(target).append(uUID).append(timing).append(jVMVersion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meta) == false) {
            return false;
        }
        Meta rhs = ((Meta) other);
        return new EqualsBuilder().append(propertiesFilePath, rhs.propertiesFilePath).append(computerOS, rhs.computerOS).append(dateTime, rhs.dateTime).append(rawCommand, rhs.rawCommand).append(target, rhs.target).append(uUID, rhs.uUID).append(timing, rhs.timing).append(jVMVersion, rhs.jVMVersion).isEquals();
    }

}

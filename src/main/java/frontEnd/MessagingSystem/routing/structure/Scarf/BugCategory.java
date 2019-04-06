package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * BugCategoryType
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "-group",
        "-code",
        "-count",
        "-bytes"
})
public class BugCategory implements Serializable {

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "group")
    @JsonProperty("-group")
    private String group;
    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "code")
    @JsonProperty("-code")
    private String code;
    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "count")
    @JsonProperty("-count")
    private int count;
    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "bytes")
    @JsonProperty("-bytes")
    private int bytes;
    private final static long serialVersionUID = -6063334270290646651L;

    /**
     * No args constructor for use in serialization
     */
    public BugCategory() {
    }

    /**
     * @param bytes
     * @param count
     * @param code
     * @param group
     */
    public BugCategory(String group, String code, int count, int bytes) {
        super();
        this.group = group;
        this.code = code;
        this.count = count;
        this.bytes = bytes;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "group")
    @JsonProperty("-group")
    public String getGroup() {
        return group;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "group")
    @JsonProperty("-group")
    public void setGroup(String group) {
        this.group = group;
    }

    public BugCategory withGroup(String group) {
        this.group = group;
        return this;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "code")
    @JsonProperty("-code")
    public String getCode() {
        return code;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "code")
    @JsonProperty("-code")
    public void setCode(String code) {
        this.code = code;
    }

    public BugCategory withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "count")
    @JsonProperty("-count")
    public int getCount() {
        return count;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "count")
    @JsonProperty("-count")
    public void setCount(int count) {
        this.count = count;
    }

    public BugCategory withCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "bytes")
    @JsonProperty("-bytes")
    public int getBytes() {
        return bytes;
    }

    /**
     * (Required)
     */
    @JacksonXmlProperty(isAttribute = true, localName = "bytes")
    @JsonProperty("-bytes")
    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public BugCategory withBytes(int bytes) {
        this.bytes = bytes;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("group", group).append("code", code).append("count", count).append("bytes", bytes).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bytes).append(count).append(code).append(group).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BugCategory) == false) {
            return false;
        }
        BugCategory rhs = ((BugCategory) other);
        return new EqualsBuilder().append(bytes, rhs.bytes).append(count, rhs.count).append(code, rhs.code).append(group, rhs.group).isEquals();
    }

}

package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;


/**
 * BugCategoryType
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "-group",
        "-code",
        "-count",
        "-bytes"
})
public class BugCategory implements Serializable {

    private final static long serialVersionUID = -6063334270290646651L;
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

    /**
     * No args constructor for use in serialization
     */
    public BugCategory() {
    }

    /**
     * <p>Constructor for BugCategory.</p>
     *
     * @param bytes a int.
     * @param count a int.
     * @param code  a {@link java.lang.String} object.
     * @param group a {@link java.lang.String} object.
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
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "group")
    @JsonProperty("-group")
    public String getGroup() {
        return group;
    }

    /**
     * (Required)
     *
     * @param group a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "group")
    @JsonProperty("-group")
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * <p>withGroup.</p>
     *
     * @param group a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory} object.
     */
    public BugCategory withGroup(String group) {
        this.group = group;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "code")
    @JsonProperty("-code")
    public String getCode() {
        return code;
    }

    /**
     * (Required)
     *
     * @param code a {@link java.lang.String} object.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "code")
    @JsonProperty("-code")
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * <p>withCode.</p>
     *
     * @param code a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory} object.
     */
    public BugCategory withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "count")
    @JsonProperty("-count")
    public int getCount() {
        return count;
    }

    /**
     * (Required)
     *
     * @param count a int.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "count")
    @JsonProperty("-count")
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * <p>withCount.</p>
     *
     * @param count a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory} object.
     */
    public BugCategory withCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * (Required)
     *
     * @return a int.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "bytes")
    @JsonProperty("-bytes")
    public int getBytes() {
        return bytes;
    }

    /**
     * (Required)
     *
     * @param bytes a int.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "bytes")
    @JsonProperty("-bytes")
    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    /**
     * <p>withBytes.</p>
     *
     * @param bytes a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory} object.
     */
    public BugCategory withBytes(int bytes) {
        this.bytes = bytes;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BugCategory[" +
                "group:" + group +
                ", code: " + code +
                ", count: " + count +
                ", bytes: " + bytes +
                "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bytes).append(count).append(code).append(group).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
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

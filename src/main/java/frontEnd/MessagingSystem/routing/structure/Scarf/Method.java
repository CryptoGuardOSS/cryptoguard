package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * MethodType
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "-id",
        "-primary",
        "-self"
})
public class Method implements Serializable {

    /**
     * id
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private int id = 0;
    /**
     * (Required)
     */
    @JsonProperty("-primary")
    @JacksonXmlProperty(isAttribute = true, localName = "primary")
    private boolean primary;

    @JacksonXmlText
    @JsonProperty("-self")
    private String self;
    private final static long serialVersionUID = -3356334068209554081L;

    /**
     * No args constructor for use in serialization
     */
    public Method() {
    }

    /**
     * @param id
     * @param primary
     * @param self
     */
    public Method(int id, boolean primary, String self) {
        super();
        this.id = id;
        this.primary = primary;
        this.self = self;
    }

    /**
     * id
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public int getId() {
        return id;
    }

    /**
     * id
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public void setId(int id) {
        this.id = id;
    }

    public Method withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("-primary")
    @JacksonXmlProperty(isAttribute = true, localName = "primary")
    public boolean isPrimary() {
        return primary;
    }

    /**
     * (Required)
     */
    @JsonProperty("-primary")
    @JacksonXmlProperty(isAttribute = true, localName = "primary")
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Method withPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

    @JacksonXmlText
    @JsonProperty("-self")
    public String getSelf() {
        return self;
    }

    @JacksonXmlText
    @JsonProperty("-self")
    public void setSelf(String self) {
        this.self = self;
    }

    public Method withSelf(String self) {
        this.self = self;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("primary", primary).append("self", self).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(primary).append(self).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Method) == false) {
            return false;
        }
        Method rhs = ((Method) other);
        return new EqualsBuilder().append(id, rhs.id).append(primary, rhs.primary).append(self, rhs.self).isEquals();
    }

}

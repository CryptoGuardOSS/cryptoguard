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
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "-id",
        "-primary",
        "-self"
})
public class Method implements Serializable {

    private final static long serialVersionUID = -3356334068209554081L;
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

    /**
     * No args constructor for use in serialization
     */
    public Method() {
    }

    /**
     * <p>Constructor for Method.</p>
     *
     * @param id      a int.
     * @param primary a boolean.
     * @param self    a {@link java.lang.String} object.
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
     *
     * @return a int.
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
     *
     * @param id a int.
     */
    @JsonProperty("-id")
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>withId.</p>
     *
     * @param id a int.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Method} object.
     */
    public Method withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * (Required)
     *
     * @return a boolean.
     */
    @JsonProperty("-primary")
    @JacksonXmlProperty(isAttribute = true, localName = "primary")
    public boolean isPrimary() {
        return primary;
    }

    /**
     * (Required)
     *
     * @param primary a boolean.
     */
    @JsonProperty("-primary")
    @JacksonXmlProperty(isAttribute = true, localName = "primary")
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * <p>withPrimary.</p>
     *
     * @param primary a boolean.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Method} object.
     */
    public Method withPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

    /**
     * <p>Getter for the field <code>self</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JacksonXmlText
    @JsonProperty("-self")
    public String getSelf() {
        return self;
    }

    /**
     * <p>Setter for the field <code>self</code>.</p>
     *
     * @param self a {@link java.lang.String} object.
     */
    @JacksonXmlText
    @JsonProperty("-self")
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * <p>withSelf.</p>
     *
     * @param self a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Method} object.
     */
    public Method withSelf(String self) {
        this.self = self;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("primary", primary).append("self", self).toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(primary).append(self).toHashCode();
    }

    /** {@inheritDoc} */
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

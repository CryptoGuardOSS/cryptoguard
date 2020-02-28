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
 * MetricType
 * <p>
 *
 * @author franceme
 * @version 03.07.01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "-id",
        "Location",
        "Class",
        "Method",
        "Type",
        "Value"
})
public class Metric implements Serializable {

    private final static long serialVersionUID = 8542989459224252190L;
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
    @JsonProperty("Location")
    private String location;
    @JsonProperty("Class")
    private String _class;
    @JsonProperty("Method")
    private String method;
    /**
     * (Required)
     */
    @JsonProperty("Type")
    private String type;
    /**
     * (Required)
     */
    @JsonProperty("Value")
    private String value;

    /**
     * No args constructor for use in serialization
     */
    public Metric() {
    }

    /**
     * <p>Constructor for Metric.</p>
     *
     * @param id       a int.
     * @param _class   a {@link java.lang.String} object.
     * @param location a {@link java.lang.String} object.
     * @param value    a {@link java.lang.String} object.
     * @param method   a {@link java.lang.String} object.
     * @param type     a {@link java.lang.String} object.
     */
    public Metric(int id, String location, String _class, String method, String type, String value) {
        super();
        this.id = id;
        this.location = location;
        this._class = _class;
        this.method = method;
        this.type = type;
        this.value = value;
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
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Metric} object.
     */
    public Metric withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Location")
    public String getLocation() {
        return location;
    }

    /**
     * (Required)
     *
     * @param location a {@link java.lang.String} object.
     */
    @JsonProperty("Location")
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * <p>withLocation.</p>
     *
     * @param location a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Metric} object.
     */
    public Metric withLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * <p>getClass_.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Class")
    public String getClass_() {
        return _class;
    }

    /**
     * <p>setClass_.</p>
     *
     * @param _class a {@link java.lang.String} object.
     */
    @JsonProperty("Class")
    public void setClass_(String _class) {
        this._class = _class;
    }

    /**
     * <p>withClass.</p>
     *
     * @param _class a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Metric} object.
     */
    public Metric withClass(String _class) {
        this._class = _class;
        return this;
    }

    /**
     * <p>Getter for the field <code>method</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Method")
    public String getMethod() {
        return method;
    }

    /**
     * <p>Setter for the field <code>method</code>.</p>
     *
     * @param method a {@link java.lang.String} object.
     */
    @JsonProperty("Method")
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * <p>withMethod.</p>
     *
     * @param method a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Metric} object.
     */
    public Metric withMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    /**
     * (Required)
     *
     * @param type a {@link java.lang.String} object.
     */
    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>withType.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Metric} object.
     */
    public Metric withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * (Required)
     *
     * @return a {@link java.lang.String} object.
     */
    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    /**
     * (Required)
     *
     * @param value a {@link java.lang.String} object.
     */
    @JsonProperty("Value")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * <p>withValue.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Metric} object.
     */
    public Metric withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("location", location).append("_class", _class).append("method", method).append("type", type).append("value", value).toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(_class).append(location).append(value).append(method).append(type).toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Metric) == false) {
            return false;
        }
        Metric rhs = ((Metric) other);
        return new EqualsBuilder().append(id, rhs.id).append(_class, rhs._class).append(location, rhs.location).append(value, rhs.value).append(method, rhs.method).append(type, rhs.type).isEquals();
    }

}

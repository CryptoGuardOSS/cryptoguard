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
    private final static long serialVersionUID = 8542989459224252190L;

    /**
     * No args constructor for use in serialization
     */
    public Metric() {
    }

    /**
     * @param id
     * @param _class
     * @param location
     * @param value
     * @param method
     * @param type
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

    public Metric withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Location")
    public String getLocation() {
        return location;
    }

    /**
     * (Required)
     */
    @JsonProperty("Location")
    public void setLocation(String location) {
        this.location = location;
    }

    public Metric withLocation(String location) {
        this.location = location;
        return this;
    }

    @JsonProperty("Class")
    public String getClass_() {
        return _class;
    }

    @JsonProperty("Class")
    public void setClass_(String _class) {
        this._class = _class;
    }

    public Metric withClass(String _class) {
        this._class = _class;
        return this;
    }

    @JsonProperty("Method")
    public String getMethod() {
        return method;
    }

    @JsonProperty("Method")
    public void setMethod(String method) {
        this.method = method;
    }

    public Metric withMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    /**
     * (Required)
     */
    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    public Metric withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    /**
     * (Required)
     */
    @JsonProperty("Value")
    public void setValue(String value) {
        this.value = value;
    }

    public Metric withValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("location", location).append("_class", _class).append("method", method).append("type", type).append("value", value).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(_class).append(location).append(value).append(method).append(type).toHashCode();
    }

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

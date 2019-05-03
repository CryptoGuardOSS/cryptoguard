package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Issues class.</p>
 *
 * @author franceme
 * Created on 05/01/2019.
 * @since 03.05.02
 *
 * <p>{Description Here}</p>
 */
public class Issues implements Serializable {

    private final static long serialVersionUID = -2265209324602399901L;

    public Issues() {

    }


    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Issue")
    @JsonProperty("Issues")
    private List<Issue> issues = new ArrayList<Issue>();

    /**
     * Issues
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Issue")
    @JsonProperty("Issues")
    public List<Issue> getIssues() {
        return issues;
    }

    /**
     * Issues
     * <p>
     * <p>
     * (Required)
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Issue")
    @JsonProperty("Issues")
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public Issues withIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }
}

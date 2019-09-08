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
 * @version 03.07.01
 */
public class Issues implements Serializable {

    private final static long serialVersionUID = -2265209324602399901L;

    /**
     * <p>Constructor for Issues.</p>
     */
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
     *
     * @return a {@link java.util.List} object.
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
     *
     * @param issues a {@link java.util.List} object.
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Issue")
    @JsonProperty("Issues")
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    /**
     * <p>withIssues.</p>
     *
     * @param issues a {@link java.util.List} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issues} object.
     */
    public Issues withIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }
}

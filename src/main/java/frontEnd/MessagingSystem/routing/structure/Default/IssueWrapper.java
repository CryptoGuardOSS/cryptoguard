package frontEnd.MessagingSystem.routing.structure.Default;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>IssueWrapper class.</p>
 *
 * @author franceme
 * Created on 05/01/2019.
 * @since 03.05.02
 *
 * <p>{Description Here}</p>
 */
public class IssueWrapper implements Serializable {

    private final static long serialVersionUID = -2265209324602399901L;

    public IssueWrapper() {

    }

    @JsonProperty("Issues")
    private List<Issue> issues = new ArrayList<Issue>();

    /**
     * Issues
     * <p>
     * <p>
     * (Required)
     */
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
    @JsonProperty("Issues")
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public IssueWrapper withIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }
}

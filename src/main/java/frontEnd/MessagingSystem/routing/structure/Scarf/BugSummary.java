package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RigorityJTeam
 * Created on 4/5/19.
 * @since {VersionHere}
 * <p>
 * This is the only custom created class as the BugSummary container was self contained within the Analyzer Report
 *
 * <p>{Description Here}</p>
 */
public class BugSummary {

    //region Attribute
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "BugCategory")
    private List<BugCategory> summaryContainer;

    //endregion
    //region Getters/Setters
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<BugCategory> getSummaryContainer() {
        if (this.summaryContainer == null)
            this.summaryContainer = new ArrayList<>();
        return summaryContainer;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    public void setSummaryContainer(List<BugCategory> summaryContainer) {
        this.summaryContainer = summaryContainer;
    }

    public void addBugSummary(BugCategory summary) {
        if (this.summaryContainer == null)
            this.summaryContainer = new ArrayList<>();
        this.summaryContainer.add(summary);
    }

    //endregion
    //region Overridden Methods
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("summaryContainer", summaryContainer).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(summaryContainer).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BugSummary) == false) {
            return false;
        }
        BugSummary rhs = ((BugSummary) other);
        return new EqualsBuilder().append(summaryContainer, rhs.summaryContainer).isEquals();
    }
    //endregion
}

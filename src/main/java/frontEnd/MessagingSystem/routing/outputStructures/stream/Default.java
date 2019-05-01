package frontEnd.MessagingSystem.routing.outputStructures.stream;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Default.Issue;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Default.mapper;

/**
 * <p>Default class.</p>
 *
 * @author franceme
 * Created on 04/30/2019.
 * @since 03.05.01
 *
 * <p>{Description Here}</p>
 */
@Log4j2
public class Default extends Structure {
    //region Attributes
    private Integer issueID = 0;
    //endregion

    //region Constructors

    /**
     * <p>Constructor for ScarfXML.</p>
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public Default(EnvironmentInformation info) throws ExceptionHandler {
        super(info);
    }
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHeader() throws ExceptionHandler {

        log.info("Marshalling the header.");

        Report report = mapper(super.getSource());

        String output = JacksonSerializer.serialize(report, true, Listing.Default.getJacksonType());

        this.write(StringUtils.trimToNull(output));


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
        super.addIssue(issue);

        log.debug("Marshalling and writing the issue: " + issue.getInfo());

        Issue instance = mapper(issue, this.getId());

        String output = JacksonSerializer.serialize(instance, true, Listing.Default.getJacksonType());

        this.write(StringUtils.trimToNull(output));

        //endregion
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFooter() throws ExceptionHandler {


    }
    //endregion

    //region Helper Methods
    private Integer getId() {
        return this.issueID++;
    }

    //endregion
}

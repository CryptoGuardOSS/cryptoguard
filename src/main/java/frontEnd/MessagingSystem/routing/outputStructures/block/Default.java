package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import lombok.extern.log4j.Log4j2;
import util.Utils;

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

    /**
     * {@inheritDoc}
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public Default(EnvironmentInformation info) {
        super(info);
    }
    //endregion

    //region Constructor
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public String handleOutput() throws ExceptionHandler {

        //reopening the console stream

        //region Setting the report for marshalling
        log.info("Marshalling the Report from the Env. Info.");
        Report report = mapper(super.getSource());

        log.trace("Marshalling the Target Info from the Env. Info.");
        report.setTarget(mapper(super.getSource(), Utils.getPlatform(), Utils.getJVMInfo()));

        //region Creating Bug Instances
        Integer bugCounter = 0;
        log.trace("Adding all of the collected issues");
        for (AnalysisIssue in : super.getCollection()) {
            log.debug("Marshalling and adding the issue: " + in.getInfo());
            report.getIssues().add(mapper(in, bugCounter));
            bugCounter++;
        }
        //endregion

        //endregion

        //region Marshalling
        log.trace("Creating the marshaller");
        String xmlStream = JacksonSerializer.serialize(report, super.getSource().prettyPrint(), Listing.Default.getJacksonType());
        //endregion

        return xmlStream;

    }

    //endregion
}

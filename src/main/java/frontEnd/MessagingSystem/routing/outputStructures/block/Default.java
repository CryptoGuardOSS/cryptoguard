package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import lombok.extern.log4j.Log4j2;
import util.Utils;

import java.io.File;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Default.mapper;

/**
 * <p>Default class.</p>
 *
 * @author franceme
 * Created on 04/30/2019.
 * @version 03.07.01
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

    /**
     * <p>Constructor for Default.</p>
     *
     * @param filePath a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public Default(String filePath) throws ExceptionHandler {
        Report struct = Report.deserialize(new File(filePath));

        EnvironmentInformation info = mapper(struct);
        super.setSource(info);
        super.setOutfile(new File(info.getFileOut()));
        super.setType(mapper(struct.getTarget().getType()));


        for (frontEnd.MessagingSystem.routing.structure.Default.Issue issue : struct.getIssues())
            super.addIssueToCollection(mapper(issue));
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

        //region Heuristics
        if (super.getSource().getDisplayHeuristics()) {
            log.trace("Writing the heuristics");
            report.setHeuristics(super.getSource().getHeuristics().getDefaultHeuristics());
        }
        //endregion

        //endregion

        //region Marshalling
        log.trace("Creating the marshaller");
        String xmlStream = JacksonSerializer.serialize(report, super.getSource().getPrettyPrint(), Listing.Default.getJacksonType());
        //endregion

        return xmlStream;

    }

    //endregion
}

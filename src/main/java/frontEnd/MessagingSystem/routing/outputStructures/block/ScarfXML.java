package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance;
import lombok.extern.log4j.Log4j2;

import java.io.File;

import static frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling;

/**
 * The class containing the implementation of the Scarf XML output.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.03
 */
@Log4j2
public class ScarfXML extends Structure {


    //region Attributes

    /**
     * {@inheritDoc}
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public ScarfXML(EnvironmentInformation info) {
        super(info);
    }

    /**
     * <p>Constructor for ScarfXML.</p>
     *
     * @param filePath a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public ScarfXML(String filePath) throws ExceptionHandler {
        AnalyzerReport report = AnalyzerReport.deserialize(new File(filePath));

        EnvironmentInformation info = marshalling(report);
        super.setSource(info);
        super.setOutfile(new File(info.getFileOut()));

        report.getBugInstance().stream().forEach(instance -> {
            try {
                super.addIssueToCollection(marshalling(instance));
            } catch (ExceptionHandler e) {
                //TODO - catch here
            }
        });

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
        log.info("Marshalling the AnalyzerReport from the Env. Info.");
        AnalyzerReport report = marshalling(super.getSource());

        //region Creating Bug Instances
        Integer numOfBugs = 0;
        log.trace("Adding all of the collected issues");
        for (AnalysisIssue in : super.getCollection()) {
            log.debug("Marshalling and adding the issue: " + in.getInfo());
            BugInstance marshalled = marshalling(in, super.getCwes(), super.getSource().getFileOutName(), numOfBugs++, super.getSource().getBuildId(), super.getSource().getXPath());
            report.getBugInstance().add(marshalled);
        }
        //endregion

        log.info("Marshalling the bug category summary.");
        report.setBugCategory(super.createBugCategoryList().getSummaryContainer());

        //region Heuristics
        if (super.getSource().getDisplayHeuristics()) {
            log.trace("Writing the heuristics");
            report.setHeuristics(super.getSource().getHeuristics().getScarfXMLHeuristics());
        }
        //endregion

        //endregion

        //region Marshalling
        log.trace("Creating the marshaller");
        String xmlStream = JacksonSerializer.serialize(report, super.getSource().getPrettyPrint(), Listing.ScarfXML.getJacksonType());
        //endregion

        String footer = frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.writeFooter(super.getSource());

        return xmlStream + footer;

    }

    //endregion

}

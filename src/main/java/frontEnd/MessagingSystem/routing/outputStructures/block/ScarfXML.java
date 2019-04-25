package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance;
import org.apache.commons.lang3.StringUtils;

/**
 * The class containing the implementation of the Scarf XML output.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V01.00.03
 */
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
        AnalyzerReport report = frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling(super.getSource());

        //region Creating Bug Instances
        Integer numOfBugs = 0;
        for (AnalysisIssue in : super.getCollection()) {
            BugInstance marshalled = frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling(in, super.getCwes(), super.getSource().getFileOutName(), numOfBugs++, super.getSource().getBuildId(), super.getSource().getxPath());
            report.getBugInstance().add(marshalled);
        }
        //endregion

        report.setBugCategory(super.createBugCategoryList().getSummaryContainer());

        //endregion

        //region Marshalling
        String xmlStream = JacksonSerializer.serialize(report, true, JacksonSerializer.JacksonType.XML);
        //endregion

        //region Writing any extra footer comments
        String footer = "";
        String prettyTab = super.getSource().prettyPrint() ? "\t" : "";
        String prettyLine = super.getSource().prettyPrint() ? "\n" : " ";

        StringBuilder commentedFooter = new StringBuilder();

        if (super.getSource().getSootErrors() != null && super.getSource().getSootErrors().split("\n").length >= 1)
            commentedFooter.append(prettyTab).append(super.getSource().getSootErrors().replaceAll("\n", prettyLine)).append(prettyLine);

        if (super.getSource().isShowTimes())
            commentedFooter.append("Analysis Timing (ms): ").append(super.getSource().getAnalyisisTime()).append(".").append(prettyLine);

        if (StringUtils.isNotBlank(commentedFooter.toString()))
            footer = prettyLine + "<!--" + prettyLine + commentedFooter.toString() + "-->";
        //endregion

        return xmlStream.toString() + footer;


    }

    //endregion

}

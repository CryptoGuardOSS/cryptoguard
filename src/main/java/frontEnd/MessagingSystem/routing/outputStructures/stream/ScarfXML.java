package frontEnd.MessagingSystem.routing.outputStructures.stream;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugSummary;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>ScarfXML class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @version $Id: $Id
 * @since 03.02.00
 *
 * <p>The ScarfXML stream writer.</p>
 */
public class ScarfXML extends Structure {

    //region Attributes
    private final String footerCatch = "</AnalyzerReport>";
    private Integer issueID = 0;
    private Integer buildId;
    private String xPath;
    //endregion

    //region Constructors

    /**
     * <p>Constructor for ScarfXML.</p>
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public ScarfXML(EnvironmentInformation info) throws ExceptionHandler {
        super(info);
        this.buildId = info.getBuildId();
        this.xPath = info.getxPath();
    }
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHeader() throws ExceptionHandler {

        AnalyzerReport report = frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling(this.getSource());

        String xmlStream = JacksonSerializer.serialize(report, true, JacksonSerializer.JacksonType.XML);

        String xml = StringUtils.trimToNull(xmlStream.toString().replace("/>", ">"));

        this.write(xml);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
        super.addIssue(issue);

        BugInstance instance = frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling(issue, super.getCwes(), super.getSource().getFileOutName(), getId(), this.buildId, this.xPath);

        String xml = JacksonSerializer.serialize(instance, true, JacksonSerializer.JacksonType.XML);

        if (!xml.endsWith("/>"))
            this.write(xml);

        //endregion
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFooter() throws ExceptionHandler {

        //region Setting the BugSummary
        BugSummary summary = super.createBugCategoryList();

        String xml = JacksonSerializer.serialize(summary, true, JacksonSerializer.JacksonType.XML);

        if (!xml.endsWith("/>"))
            this.write(xml);

        //endregion

        this.write(footerCatch);

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

        if (footer != null)
            this.writeln(footer);

        //endregion

    }
    //endregion

    //region Helper Methods
    private Integer getId() {
        return this.issueID++;
    }

    //endregion
}

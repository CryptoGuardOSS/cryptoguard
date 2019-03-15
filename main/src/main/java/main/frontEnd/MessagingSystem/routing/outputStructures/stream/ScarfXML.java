package main.frontEnd.MessagingSystem.routing.outputStructures.stream;

import com.example.response.AnalyzerReport;
import com.example.response.BugInstanceType;
import com.example.response.BugSummaryType;
import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.Interface.ExceptionId;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static main.frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling;

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
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
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

        try {
            AnalyzerReport report = marshalling(this.getSource());

            //Creating Marshaller for the Analyzer Report
            Marshaller marshaller = JAXBContext.newInstance(AnalyzerReport.class).createMarshaller();

            //Settings Properties of the Marshaller
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, super.getSource().prettyPrint());

            //Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            //From the marshaller output
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            //Creating the Stream for the marshalling and marshalling
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            marshaller.marshal(report, new PrintStream(xmlStream));

            String xml = StringUtils.trimToNull(xmlStream.toString().replace("/>", ">"));

            this.write(xml);

        } catch (JAXBException e) {
            throw new ExceptionHandler("Error writing xml.", ExceptionId.MAR_VAR);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
        super.addIssue(issue);

        BugInstanceType instance = marshalling(issue, super.getCwes(), super.getSource().getFileOutName(), getId(), this.buildId, this.xPath);


        //region Writing the Bug
        try {
            //Creating Marshaller for the Analyzer Report
            Marshaller marshaller = JAXBContext.newInstance(BugInstanceType.class).createMarshaller();

            // To format XML
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, this.getSource().prettyPrint());

            //If we DO NOT have JAXB annotated class
            JAXBElement<BugInstanceType> jaxbElement = new JAXBElement<BugInstanceType>(
                    new QName("", "BugInstance"),
                    BugInstanceType.class,
                    instance);

            //Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            //From the marshaller output
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            //Creating the Stream for the marshalling and marshalling
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            marshaller.marshal(jaxbElement, new PrintStream(xmlStream));

            String xml = xmlStringClean(xmlStream.toString());

            if (!xml.endsWith("/>"))
                this.write(xml);

        } catch (JAXBException e) {
            throw new ExceptionHandler("Error writing Xml.", ExceptionId.MAR_VAR);
        }
        //endregion
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFooter() throws ExceptionHandler {

        //region Setting the BugSummary
        try {
            //Creating Marshaller for the Analyzer Report
            Marshaller marshaller = JAXBContext.newInstance(BugSummaryType.class).createMarshaller();

            // To format XML
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, this.getSource().prettyPrint());

            //If we DO NOT have JAXB annotated class
            JAXBElement<BugSummaryType> jaxbElement = new JAXBElement<>(
                    new QName("", "BugSummary"),
                    BugSummaryType.class,
                    super.createBugSummary());

            //Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            //From the marshaller output
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            //Creating the Stream for the marshalling and marshalling
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            marshaller.marshal(jaxbElement, new PrintStream(xmlStream));

            String xml = xmlStringClean(xmlStream.toString());

            if (!xml.endsWith("/>"))
                this.write(xml);

        } catch (JAXBException e) {
            throw new ExceptionHandler("Error writing XML.", ExceptionId.MAR_VAR);
        }
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
    private String xmlStringClean(String in) {
        return StringUtils.trimToNull(in.replace(" xmlns:ns2=\"https://www.swamp.com/com/scarf/struct\"", "").replaceAll("<ns2:", "<").replaceAll("</ns2:", "</"));
    }

    private Integer getId() {
        return this.issueID++;
    }

    //endregion
}

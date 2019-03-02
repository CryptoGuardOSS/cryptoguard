package main.frontEnd.MessagingSystem.routing.outputStructures.block;

import com.example.response.AnalyzerReport;
import com.example.response.BugInstanceType;
import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.Interface.ExceptionId;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static main.frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling;

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
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
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
        try {
            //region Setting the report for marshalling
            AnalyzerReport report = marshalling(super.getSource());


            //region Creating Bug Instances
            Integer numOfBugs = 0;
            for (AnalysisIssue in : super.getCollection()) {
                BugInstanceType marshalled = marshalling(in, super.getCwes(), super.getSource().getFileOutName(), numOfBugs++, super.getSource().getBuildId(), super.getSource().getxPath());
                report.getBugInstance().add(marshalled);
            }
            //endregion

            report.setBugSummary(super.createBugSummary());

            //endregion

            //region Marshalling
            //Creating Marshaller for the Analyzer Report
            JAXBContext context = JAXBContext.newInstance(AnalyzerReport.class);
            Marshaller marshaller = context.createMarshaller();

            //Settings Properties of the Marshaller
            if (super.getSource().prettyPrint())
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            //Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            //From the marshaller output
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            //Creating the Stream for the marshalling and marshalling
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            marshaller.marshal(report, new PrintStream(xmlStream));
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

        } catch (PropertyException e) {
            throw new ExceptionHandler("There has been an issue setting properties.", ExceptionId.FILE_O);
        } catch (JAXBException e) {
            throw new ExceptionHandler("There has been an issue marshalling the output.", ExceptionId.FILE_O);
        }
    }

    //endregion

}

package main.frontEnd.outputStructures;

import com.example.response.AnalyzerReport;
import main.frontEnd.AnalysisIssue;
import main.frontEnd.EnvironmentInformation;
import main.frontEnd.OutputStructure;
import main.rule.engine.EngineType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * The class containing the implementation of the Scarf XML output.
 * <p>STATUS: IC</p>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class ScarfXMLOutput implements OutputStructure {
	public final Listing typeOfStructure = Listing.LegacyOutput;

	/***
	 *  The overridden method for the Scarf XML output.
	 *
	 * @return Object The raw stream of the xml
	 */
	public String getOutput(EnvironmentInformation source, EngineType type, ArrayList<AnalysisIssue> brokenRules, PrintStream internalWarnings) {
		try {
			AnalyzerReport report = new AnalyzerReport();

			//TODO - Set business-level marshalling here

			//Creating Marshaller for the Analyzer Report
			Marshaller marshaller = JAXBContext.newInstance(AnalyzerReport.class).createMarshaller();

			//Settings Properties of the Marshaller
			marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, true);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//Creating the Stream for the marshalling and marshalling
			ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
			marshaller.marshal(report, new PrintStream(xmlStream));


			return xmlStream.toString();
		} catch (PropertyException e) {
			return creatingErrorMessage("There has been an issue setting properties.");
		} catch (JAXBException e) {
			return creatingErrorMessage("There has been an issue marshalling the output.");
		}
	}

	/**
	 * The method for this output to create error messages
	 *
	 * @param message -  the string of the message to be sent to the output
	 * @return string - the xml format of the error message
	 */
	private String creatingErrorMessage(String message) {
		StringBuilder output = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		output.append("<ERROR>");
		output.append(message);
		output.append("</ERROR>");
		return output.toString();
	}
}

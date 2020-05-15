/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.block;

import static frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance;
import java.io.File;
import org.apache.logging.log4j.Logger;

/**
 * The class containing the implementation of the Scarf XML output.
 *
 * <p>STATUS: IC
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.03
 */
public class ScarfXML extends Structure {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(ScarfXML.class);

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
   * Constructor for ScarfXML.
   *
   * @param filePath a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public ScarfXML(String filePath) throws ExceptionHandler {
    AnalyzerReport report = AnalyzerReport.deserialize(new File(filePath));

    EnvironmentInformation info = marshalling(report);
    super.setSource(info);
    super.setOutfile(new File(info.getFileOut()));

    for (BugInstance instance : report.getBugInstance())
      super.addIssueToCollection(marshalling(instance));
  }
  //endregion

  //region Constructor
  //endregion

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public String handleOutput() throws ExceptionHandler {

    //reopening the console stream

    //region Setting the report for marshalling
    log.info("Marshalling the AnalyzerReport from the Env. Info.");
    AnalyzerReport report = marshalling(super.getSource());

    //region Creating Bug Instances
    Integer numOfBugs = 0;
    log.debug("Adding all of the collected issues");
    for (AnalysisIssue in : super.getCollection()) {
      log.debug("Marshalling and adding the issue: " + in.getInfo());
      BugInstance marshalled =
          marshalling(
              in,
              super.getCwes(),
              super.getSource().getFileOutName(),
              numOfBugs++,
              super.getSource().getBuildId(),
              super.getSource().getXPath());
      report.getBugInstance().add(marshalled);
    }
    //endregion

    log.info("Marshalling the bug category summary.");
    report.setBugCategory(super.createBugCategoryList().getSummaryContainer());

    //region Heuristics
    if (super.getSource().getDisplayHeuristics()) {
      log.debug("Writing the heuristics");
      report.setHeuristics(super.getSource().getHeuristics().getScarfXMLHeuristics());
    }
    //endregion

    //endregion

    //region Marshalling
    log.debug("Creating the marshaller");
    String xmlStream =
        JacksonSerializer.serialize(
            report, super.getSource().getPrettyPrint(), Listing.ScarfXML.getJacksonType());
    //endregion

    String footer =
        frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.writeFooter(
            super.getSource());

    return xmlStream + footer;
  }

  //endregion

}

/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.stream;

import static frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.marshalling;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugSummary;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

/**
 * ScarfXML class.
 *
 * @author CryptoguardTeam Created on 2/7/19.
 * @version 03.07.01
 * @since 03.02.00
 *     <p>The ScarfXML stream writer.
 */
public class ScarfXML extends Structure {

  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(ScarfXML.class);
  //region Attributes
  private final String footerCatch = "</AnalyzerReport>";
  private Integer issueID = 0;
  private Integer buildId;
  private String xPath;
  //endregion

  //region Constructors

  /**
   * Constructor for ScarfXML.
   *
   * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public ScarfXML(EnvironmentInformation info) throws ExceptionHandler {
    super(info);
    this.buildId = info.getBuildId();
    this.xPath = info.getXPath();
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

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public void writeHeader() throws ExceptionHandler {

    log.info("Marshalling the header.");
    AnalyzerReport report = marshalling(this.getSource());

    String xmlStream = JacksonSerializer.serialize(report, true, Listing.ScarfXML.getJacksonType());

    String xml = StringUtils.trimToNull(xmlStream.replace("/>", ">"));

    this.write(xml);
  }

  /** {@inheritDoc} */
  @Override
  public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
    super.addIssue(issue);

    log.debug("Marshalling and writing the issue: " + issue.getInfo());
    BugInstance instance =
        marshalling(
            issue,
            super.getCwes(),
            super.getSource().getFileOutName(),
            getId(),
            this.buildId,
            this.xPath);

    String xml = JacksonSerializer.serialize(instance, true, Listing.ScarfXML.getJacksonType());

    if (!xml.endsWith("/>")) this.write(xml);

    //endregion
  }

  /** {@inheritDoc} */
  @Override
  public void writeFooter() throws ExceptionHandler {

    //region Setting the BugSummary
    BugSummary summary = super.createBugCategoryList();

    log.info("Marshalling the bug category summary.");
    String xml =
        JacksonSerializer.serialize(
            summary, super.getSource().getPrettyPrint(), Listing.ScarfXML.getJacksonType());

    if (!xml.endsWith("/>")) this.write(xml);

    //endregion

    //region Heuristics
    if (super.getSource().getDisplayHeuristics()) {
      log.trace("Writing the heuristics");
      String heuristicsXML =
          JacksonSerializer.serialize(
              super.getSource().getHeuristics().getScarfXMLHeuristics(),
              super.getSource().getPrettyPrint(),
              Listing.ScarfXML.getJacksonType());

      if (!heuristicsXML.endsWith("/>")) this.write(heuristicsXML);
    }
    //endregion

    this.write(footerCatch);

    String footer =
        frontEnd.MessagingSystem.routing.outputStructures.common.ScarfXML.writeFooter(
            super.getSource());

    if (footer != null) this.writeln(footer);
  }
  //endregion

  //region Helper Methods
  private Integer getId() {
    return this.issueID++;
  }

  //endregion
}

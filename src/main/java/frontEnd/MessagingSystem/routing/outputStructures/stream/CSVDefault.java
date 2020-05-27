/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.stream;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Default.mapper;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.common.CSVMapper;
import frontEnd.MessagingSystem.routing.structure.Default.Issue;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import java.io.File;
import org.apache.logging.log4j.Logger;

/**
 * Default class.
 *
 * @author franceme Created on 04/30/2019.
 * @version 03.07.01
 * @since 03.05.01
 *     <p>{Description Here}
 */
public class CSVDefault extends Structure {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CSVDefault.class);

  //region Attributes
  public final CSVMapper mapper;
  private Integer issueID = 0;
  private Boolean started = false;
  //endregion

  //region Constructors

  /**
   * Constructor for ScarfXML.
   *
   * @param info a {@link EnvironmentInformation} object.
   * @throws ExceptionHandler if any.
   */
  public CSVDefault(EnvironmentInformation info) throws ExceptionHandler {
    super(info);
    mapper = new CSVMapper(info);
  }

  /**
   * Constructor for Default.
   *
   * @param filePath a {@link String} object.
   * @throws ExceptionHandler if any.
   */
  public CSVDefault(String filePath) throws ExceptionHandler {
    Report struct = Report.deserialize(new File(filePath));

    EnvironmentInformation info = mapper(struct);
    mapper = new CSVMapper(info);
    super.setSource(info);
    super.setOutfile(new File(info.getFileOut()));
    super.setType(mapper(struct.getTarget().getType()));

    for (Issue issue : struct.getIssues()) super.addIssueToCollection(mapper(issue));
  }
  //endregion

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public void writeHeader() throws ExceptionHandler {

    log.info("Marshalling the header.");
    this.writeln(mapper.writeHeader());
  }

  /** {@inheritDoc} */
  @Override
  public void addIssue(AnalysisIssue issue) throws ExceptionHandler {

    log.debug("Writing the marshaled output");
    this.writeln(mapper.writeIssue(issue));
  }

  /** {@inheritDoc} */
  @Override
  public void writeFooter() throws ExceptionHandler {}

  //endregion
}

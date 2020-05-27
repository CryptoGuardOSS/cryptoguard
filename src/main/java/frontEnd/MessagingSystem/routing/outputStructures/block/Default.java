/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.block;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Default.mapper;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import java.io.File;
import org.apache.logging.log4j.Logger;
import util.Utils;

/**
 * Default class.
 *
 * @author franceme Created on 04/30/2019.
 * @version 03.07.01
 * @since 03.05.01
 *     <p>{Description Here}
 */
public class Default extends Structure {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Default.class);

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
   * Constructor for Default.
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

  /** {@inheritDoc} */
  @Override
  public String handleOutput() throws ExceptionHandler {

    //reopening the console stream

    //region Setting the report for marshalling
    log.info("Marshalling the Report from the Env. Info.");
    Report report = mapper(super.getSource());

    log.debug("Marshalling the Target Info from the Env. Info.");
    report.setTarget(mapper(super.getSource(), Utils.getPlatform(), Utils.getJVMInfo()));

    //region Creating Bug Instances
    Integer bugCounter = 0;
    log.debug("Adding all of the collected issues");
    for (AnalysisIssue in : super.getCollection()) {
      log.debug("Marshalling and adding the issue: " + in.getInfo());
      report.getIssues().add(mapper(in, bugCounter));
      bugCounter++;
    }
    //endregion

    //region Heuristics
    if (super.getSource().getDisplayHeuristics()) {
      log.debug("Writing the heuristics");
      report.setHeuristics(super.getSource().getHeuristics().getDefaultHeuristics());
    }
    //endregion

    //endregion

    //region Marshalling
    log.debug("Creating the marshaller");
    return JacksonSerializer.serialize(
        report,
        super.getSource().getPrettyPrint(),
        super.getSource().getMessagingType().getJacksonType());
    //endregion
  }

  //endregion
}

/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.stream;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Default.mapper;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Default.Issue;
import frontEnd.MessagingSystem.routing.structure.Default.Issues;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
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
  private Integer issueID = 0;
  private Boolean started = false;
  //endregion

  //region Constructors

  /**
   * Constructor for ScarfXML.
   *
   * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public Default(EnvironmentInformation info) throws ExceptionHandler {
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

    for (Issue issue : struct.getIssues()) super.addIssueToCollection(mapper(issue));
  }
  //endregion

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public void writeHeader() throws ExceptionHandler {

    log.info("Marshalling the header.");

    Report report = mapper(super.getSource());

    log.trace("Marshalling the Target Info from the Env. Info.");
    report.setTarget(mapper(super.getSource(), Utils.getPlatform(), Utils.getJVMInfo()));

    log.trace("Setting the issues to null to not be output");
    report.setIssues(null);

    log.debug("Marshalling the header");
    String output =
        StringUtils.trimToNull(
            JacksonSerializer.serialize(
                report, super.getSource().getPrettyPrint(), Listing.Default.getJacksonType()));

    log.debug("Replacing the header based on the type");
    switch (super.getSource().getMessagingType().getJacksonType()) {
      case JSON:
        output = output.substring(0, output.lastIndexOf("}"));
        break;
      case XML:
        output = output.replace("</" + Report.class.getSimpleName() + ">", "");
        break;
      case YAML:
        break;
    }

    log.trace("Current String:\n" + output);

    this.write(output);
  }

  /** {@inheritDoc} */
  @Override
  public void addIssue(AnalysisIssue issue) throws ExceptionHandler {

    super.addIssue(issue);

    log.debug("Marshalling the issue: " + issue.getInfo());
    Issue instance = mapper(issue, this.getId());

    String output;

    if (!this.started) {

      //region Instantiating and trimming the wrapper content
      log.trace("Writing a initialized list");
      Issues issueWrapper = new Issues();
      issueWrapper.getIssues().add(instance);

      log.debug("Marshalling the issue wrapper");
      output =
          JacksonSerializer.serialize(
              issueWrapper, super.getSource().getPrettyPrint(), Listing.Default.getJacksonType());

      log.trace("Manipulating the output based on the type");
      switch (super.getSource().getMessagingType().getJacksonType()) {
        case JSON:
          log.debug("Manipulating the JSON header output");

          //Manipulating the string to contain only the contents of the class
          //otherwise the class would auto close the array, and begin being closed
          output = output.substring(output.indexOf("{") + 1, output.lastIndexOf("]") - 1);
          output = ", " + output;
          break;
        case XML:
          output = output.replace("</" + Issues.class.getSimpleName() + ">", "");
          break;
        case YAML:
          output = output.replace("---\n", "\n ").replace("\n", "\n  ").replaceFirst("\n  ", "\n");
          output = output.substring(0, output.lastIndexOf("\n"));
          log.debug("Altered output: " + output);
          break;
      }

      this.started = true;
      //endregion
    } else {

      log.debug("Marshalling the issue");
      output =
          JacksonSerializer.serialize(
              instance, super.getSource().getPrettyPrint(), Listing.Default.getJacksonType());

      switch (super.getSource().getMessagingType().getJacksonType()) {
        case JSON:
          output = ", " + output;
          break;
        case XML:
          break;
        case YAML:
          output =
              output.replace("\n", "\n    ").replace("---\n", "\n- ").replace("\n-     ", "\n  - ");
          output = output.substring(0, output.lastIndexOf("\n"));
          break;
      }
    }

    log.debug("Writing the marshaled output");
    this.write(output);
  }

  /** {@inheritDoc} */
  @Override
  public void writeFooter() throws ExceptionHandler {

    this.write("\n]\n");
    if (super.getSource().getDisplayHeuristics()) {
      this.write(", \"Heuristics\" : ");
      log.debug("Writing the Heuristics");
      this.write(
          JacksonSerializer.serialize(
              super.getSource().getHeuristics().getDefaultHeuristics(),
              super.getSource().getPrettyPrint(),
              super.getSource().getMessagingType().getJacksonType()));
    }

    String ending = "";
    log.debug("Adding the footer to the output");
    switch (super.getSource().getMessagingType().getJacksonType()) {
      case JSON:
        ending = "\n}";
        break;
      case XML:
        ending = "\n</" + Issues.class.getSimpleName() + ">\n";
        ending = ending + "</" + Report.class.getSimpleName() + ">";
      case YAML:
        break;
    }

    this.write(ending);
  }
  //endregion

  //region Helper Methods
  private Integer getId() {
    return this.issueID++;
  }
  //endregion
}

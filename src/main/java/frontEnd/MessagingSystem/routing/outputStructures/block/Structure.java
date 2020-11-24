/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

/**
 * Abstract TypeSpecificArg class.
 *
 * @author CryptoguardTeam Created on 3/2/19.
 * @version 03.07.01
 * @since 03.03.00
 *     <p>The overarching structure encompassing the block marshalling, extending from the output
 *     structure.
 */
public abstract class Structure extends OutputStructure {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Structure.class);

  //region Attributes
  //endregion

  //region Constructor

  /**
   * Constructor for TypeSpecificArg.
   *
   * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   */
  public Structure(EnvironmentInformation info) {
    super(info);
  }

  /** Constructor for TypeSpecificArg. */
  public Structure() {}

  //endregion

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public void startAnalyzing() {}

  /** {@inheritDoc} */
  @Override
  public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
    super.addIssueToCollection(issue);
  }

  /** {@inheritDoc} */
  @Override
  public void stopAnalyzing() throws ExceptionHandler {
    WriteIntoFile(StringUtils.stripToNull(this.handleOutput()));
  }

  //endregion
  //region Self OverriddenMethods

  /**
   * handleOutput.
   *
   * @return a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public abstract String handleOutput() throws ExceptionHandler;
  //endregion

  //region Helper Methods

  /**
   * WriteIntoFile.
   *
   * @param in a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void WriteIntoFile(String in) throws ExceptionHandler {
    try {
      Files.write(this.getOutfile().toPath(), in.getBytes(super.getChars()));
    } catch (IOException e) {
      log.fatal("Error: " + e.getMessage());
      throw new ExceptionHandler(
          "Error writing to file: " + this.getSource().getFileOutName(), ExceptionId.FILE_O);
    }
  }
  //endregion

}

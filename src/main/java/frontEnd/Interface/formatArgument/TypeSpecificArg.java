/* Licensed under GPL-3.0 */
package frontEnd.Interface.formatArgument;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import java.util.List;
import org.apache.commons.cli.Options;

/**
 * TypeSpecificArg interface.
 *
 * @author CryptoguardTeam Created on 12/13/18.
 * @version 03.07.01
 * @since 01.01.02
 *     <p>The interface containing the commandline check for each messaging useage
 */
public interface TypeSpecificArg {

  /**
   * The interface for each type of routing to handle the raw command line arguments.
   *
   * @param info {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} - the continuation
   *     of the environmental info to be added onto.
   * @param args {@link List<String>} - The full set of arguments passed from the command line
   * @return {@link java.lang.Boolean} - an indication if the validation passed.
   * @throws {@link frontEnd.Interface.outputRouting.ExceptionHandler} - The main controlled
   *     exception.
   */
  Boolean inputValidation(EnvironmentInformation info, List<String> args) throws ExceptionHandler;

  /**
   * getOptions.
   *
   * @return a {@link org.apache.commons.cli.Options} object.
   */
  Options getOptions();
}

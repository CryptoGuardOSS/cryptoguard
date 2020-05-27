/* Licensed under GPL-3.0 */
package frontEnd.Interface.formatArgument;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import java.util.List;
import org.apache.commons.cli.Options;

/**
 * Default class.
 *
 * @author franceme Created on 04/30/2019.
 * @version 03.07.01
 * @since 03.05.01
 *     <p>{Description Here}
 */
public class Default implements TypeSpecificArg {
  /** Constructor for Default. */
  public Default() {}

  /** {@inheritDoc} The overridden method for the Legacy output. */
  public Boolean inputValidation(EnvironmentInformation info, List<String> args) {
    return true;
  }

  /**
   * getOptions.
   *
   * @return a {@link org.apache.commons.cli.Options} object.
   */
  public Options getOptions() {
    return null;
  }
}

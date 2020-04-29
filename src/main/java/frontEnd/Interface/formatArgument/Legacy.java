/* Licensed under GPL-3.0 */
package frontEnd.Interface.formatArgument;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import java.util.List;
import org.apache.commons.cli.Options;

/**
 * Legacy class.
 *
 * @author CryptoguardTeam Created on 12/13/18.
 * @version 03.07.01
 * @since 01.01.02
 *     <p>The input check for the Legacy Output
 */
public class Legacy implements TypeSpecificArg {
  /** Constructor for Legacy. */
  public Legacy() {}

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

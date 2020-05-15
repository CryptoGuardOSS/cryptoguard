/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import static util.Utils.handleErrorMessage;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.argsIdentifier;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import util.Utils;

/**
 * EntryPoint class.
 *
 * @author CryptoguardTeam Created on 12/5/18.
 * @version 03.07.01
 * @since 01.00.06
 *     <p>The main entry point of the program when this program is used via command-line and not as
 *     a library
 */
public class EntryPoint {

  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(EntryPoint.class);

  /**
   * main.
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {
    //Removing all of the empty string values, i.e. " "
    ArrayList<String> strippedArgs = Utils.stripEmpty(args);
    log.debug("Removed the empty arguments.");

    boolean exitingJVM = !strippedArgs.contains(argsIdentifier.NOEXIT.getArg());

    try {
      //Fail Fast on the input validation
      EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(strippedArgs);

      System.out.println(SubRunner.run(generalInfo));

      if (exitingJVM) System.exit(ExceptionId.SUCCESS.getId());

    } catch (ExceptionHandler e) {
      handleErrorMessage(e);

      if (exitingJVM) System.exit(e.getErrorCode().getId());
    }
  }
}

package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.argsIdentifier;
import lombok.extern.log4j.Log4j2;
import util.Utils;

import java.util.ArrayList;

import static util.Utils.handleErrorMessage;

/**
 * <p>EntryPoint class.</p>
 *
 * @author CryptoguardTeam
 * Created on 12/5/18.
 * @version 03.07.01
 * @since 01.00.06
 *
 * <p>The main entry point of the program when this program
 * is used via command-line and not as a library</p>
 */
@Log4j2
public class EntryPoint {

    /**
     * <p>main.</p>
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
            exitingJVM = generalInfo.getKillJVM();

            System.out.println(SubRunner.run(generalInfo));

            if (exitingJVM)
                System.exit(ExceptionId.SUCCESS.getId());

        } catch (ExceptionHandler e) {
            handleErrorMessage(e);

            if (exitingJVM)
                System.exit(e.getErrorCode().getId());
        }

    }
}

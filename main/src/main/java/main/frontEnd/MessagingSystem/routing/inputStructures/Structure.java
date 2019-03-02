package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>Structure interface.</p>
 *
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @version $Id: $Id
 * @since 01.01.02
 *
 * <p>The interface containing the commandline check for each messaging useage</p>
 */
public interface Structure {

    /**
     * The interface for each type of routing to handle the raw command line arguments.
     *
     * @param info {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} - the continuation of the environmental info to be added onto.
     * @param args {@link String[]} - The subset of arguments passed from the command line
     * @return {@link java.lang.Boolean} - an indication if the validation passed.
     * @throws {@link main.frontEnd.Interface.ExceptionHandler} - The main controlled exception.
     */
    Boolean inputValidation(EnvironmentInformation info, String[] args) throws ExceptionHandler;

    /**
     * A method to print out help for using a given messaging input
     *
     * @return {@link java.lang.String} - the string used for help
     */
    String helpInfo();

}

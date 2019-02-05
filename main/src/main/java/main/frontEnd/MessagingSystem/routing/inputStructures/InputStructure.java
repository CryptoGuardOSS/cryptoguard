package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>InputStructure interface.</p>
 *
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @version $Id: $Id
 * @since 01.01.02
 *
 * <p>The interface containing the commandline check for each messaging useage</p>
 */
public interface InputStructure {

    /**
     * The interface for each type of routing to handle the raw command line arguments.
     *
     * @param info {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} - the continuation of the environmental info to be added onto.
     * @param args {@link String[]} - The subset of arguments passed from the command line
     * @return {@link java.lang.Boolean} - an indication if the validation passed.
     */
    Boolean inputValidation(EnvironmentInformation info, String[] args);

    /**
     * A method to print out help for using a given messaging input
     *
     * @return {@link java.lang.String} - the string used for help
     */
    String helpInfo();

}

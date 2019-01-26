package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @since 01.01.02
 *
 * <p>The interface containing the commandline check for each messaging useage</p>
 */
public interface InputStructure {

    /**
     * The interface for each type of routing to handle the raw command line arguments.
     *
     * @param info {@link EnvironmentInformation} - the continuation of the environmental info to be added onto.
     * @param args {@link String[]} - The subset of arguments passed from the command line
     * @return {@link Boolean} - an indication if the validation passed.
     */
    Boolean inputValidation(EnvironmentInformation info, String[] args);

    /**
     * A method to print out help for using a given messaging input
     *
     * @return {@link java.lang.String} - the string used for help
     */
    String helpInfo();

}

package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.EngineType;

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
     * @param args         {@link java.lang.String[]} - the raw string arguments from the command line.
     * @param dependencies {@link java.lang.String} - the path to the source dependencies
     * @param type         {@link EngineType} - the type of source being handled
     * @return {@link EnvironmentInformation} - if the environment info is successfully returned (not null) then validation passed.
     */
    EnvironmentInformation inputValidation(String[] args, String dependencies, EngineType type);

    /**
     * A method to print out help for using a given messaging input
     *
     * @return {@link java.lang.String} - the string used for help
     */
    String helpInfo();

}

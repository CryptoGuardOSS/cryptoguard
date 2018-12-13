package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.frontEnd.MessagingSystem.routing.inputStructures.InputStructure;
import main.rule.engine.EngineType;

import java.io.File;

/**
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @since 01.01.02
 *
 * <p>The main check for the Arguments</p>
 */
public class ArgumentsCheck {

    /**
     * The fail fast parameter Check method
     * <p>This method will attempt to create the Environment Information and provide help if the usage doesn't match</p>
     *
     * @param args {@link java.lang.String[]} - the raw arguments passed into the console
     * @return {@link EnvironmentInformation} - when not null, the general Information is created for usage within any output structure.
     */
    public static EnvironmentInformation paramaterCheck(String[] args) {
        if (args.length >= 2) {
            EngineType flow = EngineType.getFromExt(args[0]);
            File fileCheck = new File(args[0]);
            if (flow == null && fileCheck.exists() && fileCheck.isDirectory()) {
                flow = EngineType.SOURCE;
                fileCheck = null;
            } else {
                System.out.println("Source Code Directory " + fileCheck.getPath() + " not available.");
                return null;
            }
            String dependencies = args[1];

            InputStructure inputCheck = Listing.getTypeOfMessagingInput(args.length == 2 ? null : args[3]);

            return inputCheck.inputValidation(args, dependencies, flow);

        } else {
            System.out.println(Listing.getInputHelp());
            return null;
        }

    }

}

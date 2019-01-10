package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @since 01.01.02
 *
 * <p>The input check for the Legacy Output</p>
 */
public class Legacy implements InputStructure {

    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output.
     */
    public Boolean inputValidation(EnvironmentInformation info, String[] args) {
        return true;
    }

    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output.
     */
    public String helpInfo() {
        return "No extra inputs are needed for this output type\nThis is the default output type";
    }
}

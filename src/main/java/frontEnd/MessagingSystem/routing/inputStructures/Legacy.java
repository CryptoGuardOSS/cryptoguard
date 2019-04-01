package frontEnd.MessagingSystem.routing.inputStructures;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>Legacy class.</p>
 *
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @version $Id: $Id
 * @since 01.01.02
 *
 * <p>The input check for the Legacy Output</p>
 */
public class Legacy implements Structure {

    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output.
     *
     * @param info a {@link EnvironmentInformation} object.
     * @param args an array of {@link java.lang.String} objects.
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean inputValidation(EnvironmentInformation info, String[] args) {
        return true;
    }

    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output.
     *
     * @return a {@link java.lang.String} object.
     */
    public String helpInfo() {
        return "No extra inputs are needed for this output type\nThis is the default output type";
    }
}

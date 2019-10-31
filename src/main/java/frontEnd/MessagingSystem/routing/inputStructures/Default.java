package frontEnd.MessagingSystem.routing.inputStructures;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>Default class.</p>
 *
 * @author franceme
 * Created on 04/30/2019.
 * @version 03.07.01
 * @since 03.05.01
 *
 * <p>{Description Here}</p>
 */
public class Default implements Structure {
    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output.
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
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

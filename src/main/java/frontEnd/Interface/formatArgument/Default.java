package frontEnd.Interface.formatArgument;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.commons.cli.Options;

import java.util.List;

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
public class Default implements TypeSpecificArg {
    /**
     * <p>Constructor for Default.</p>
     */
    public Default() {
    }

    /**
     * {@inheritDoc}
     * The overridden method for the Legacy output.
     */
    public Boolean inputValidation(EnvironmentInformation info, List<String> args) {
        return true;
    }

    /**
     * <p>getOptions.</p>
     *
     * @return a {@link org.apache.commons.cli.Options} object.
     */
    public Options getOptions() {
        return null;
    }
}

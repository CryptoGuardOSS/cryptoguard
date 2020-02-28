package frontEnd.Interface.formatArgument;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.commons.cli.Options;

import java.util.List;

/**
 * <p>Legacy class.</p>
 *
 * @author CryptoguardTeam
 * Created on 12/13/18.
 * @version 03.07.01
 * @since 01.01.02
 *
 * <p>The input check for the Legacy Output</p>
 */
public class Legacy implements TypeSpecificArg {
    /**
     * <p>Constructor for Legacy.</p>
     */
    public Legacy() {
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

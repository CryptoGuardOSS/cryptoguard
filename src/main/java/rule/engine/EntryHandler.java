package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>EntryHandler interface.</p>
 *
 * @author CryptoguardTeam
 * Created on 2019-01-20.
 * @version 03.07.01
 * @since 02.02.00
 *
 * <p>The interface for how the the different use cases are called.</p>
 */
public interface EntryHandler {

    /**
     * <p>Scan.</p>
     *
     * @param generalInfo a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler;
}

package rule.engine;

import frontEnd.Interface.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>EntryHandler interface.</p>
 *
 * @author RigorityJTeam
 * Created on 2019-01-20.
 * @version $Id: $Id
 * @since 02.02.00
 *
 * <p>The interface for how the the different use cases are called.</p>
 */
public interface EntryHandler {

    /**
     * <p>Scan.</p>
     *
     * @param generalInfo a {@link EnvironmentInformation} object.
     * @throws ExceptionHandler if any.
     */
    void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler;
}

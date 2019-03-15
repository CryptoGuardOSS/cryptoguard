package main.rule.engine;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

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
     * @param generalInfo a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler;
}

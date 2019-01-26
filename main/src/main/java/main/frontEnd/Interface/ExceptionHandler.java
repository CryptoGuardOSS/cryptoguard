package main.frontEnd.Interface;

/**
 * @author RigorityJTeam
 * Created on 2019-01-25.
 * @since 02.00.03
 *
 * <p>The Main Exception Handling for the whole project</p>
 */
public class ExceptionHandler extends Exception {

    //region Creations
    public ExceptionHandler(String message) {
        super(message);
    }

    public ExceptionHandler(Throwable cause) {
        super(cause);
    }

    public ExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
    //endregion
}

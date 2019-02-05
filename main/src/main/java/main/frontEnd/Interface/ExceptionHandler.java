package main.frontEnd.Interface;

/**
 * <p>ExceptionHandler class.</p>
 *
 * @author RigorityJTeam
 * Created on 2019-01-25.
 * @version $Id: $Id
 * @since 02.00.03
 *
 * <p>The Main Exception Handling for the whole project</p>
 */
public class ExceptionHandler extends Exception {

    //region Creations

    /**
     * <p>Constructor for ExceptionHandler.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public ExceptionHandler(String message) {
        super(message);
    }

    /**
     * <p>Constructor for ExceptionHandler.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    public ExceptionHandler(Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructor for ExceptionHandler.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param cause   a {@link java.lang.Throwable} object.
     */
    public ExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
    //endregion
}

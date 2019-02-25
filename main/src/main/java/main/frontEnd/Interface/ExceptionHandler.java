package main.frontEnd.Interface;

import org.apache.commons.lang3.StringUtils;

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

    //region Attributes
    private ExceptionId errorCode;
    private String longDesciption;
    //endregion

    //region Creations

    /**
     * <p>Constructor for ExceptionHandler.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param id      a {@link main.frontEnd.Interface.ExceptionId} object.
     */
    public ExceptionHandler(String message, ExceptionId id) {
        this.errorCode = id;
        this.longDesciption = message;
        //super(message);
    }
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder resp = new StringBuilder();

        resp.append("==================================\n");
        resp.append("ErrorType: ").append(this.errorCode.getMessage()).append("\n");
        resp.append("Error Message: ").append(this.longDesciption).append("\n");
        resp.append("==================================");

        return StringUtils.trimToNull(resp.toString());
    }

    @Override
    public void printStackTrace() {
        System.err.println(this.toString());
    }
    //endregion

    //region Getters
    /**
     * <p>Getter for the field <code>errorCode</code>.</p>
     *
     * @return a {@link main.frontEnd.Interface.ExceptionId} object.
     */
    public ExceptionId getErrorCode() {
        return errorCode;
    }
    //endregion
}

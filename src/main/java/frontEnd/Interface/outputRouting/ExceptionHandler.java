package frontEnd.Interface.outputRouting;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * <p>ExceptionHandler class.</p>
 *
 * @author CryptoguardTeam
 * Created on 2019-01-25.
 * @version 03.07.01
 * @since 02.00.03
 *
 * <p>The Main Exception Handling for the whole project</p>
 */
public class ExceptionHandler extends Exception implements Supplier<String> {

    //region Attributes
    private ExceptionId errorCode;
    private String longDesciption;
    //endregion

    //region Creations

    /**
     * <p>Constructor for ExceptionHandler.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param id      a {@link frontEnd.Interface.outputRouting.ExceptionId} object.
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

        String resp = "==================================\n" +
                "Error ID: " + this.errorCode.getId() + "\n" +
                "Error Type: " + this.errorCode.getMessage() + "\n" +
                "Error Message: \n" + this.longDesciption + "\n" +
                "==================================";
        return StringUtils.trimToNull(resp).concat("\n\n\n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printStackTrace() {
        System.err.println(this.toString());
    }
    //endregion

    //region Getters

    /**
     * <p>Getter for the field <code>errorCode</code>.</p>
     *
     * @return a {@link frontEnd.Interface.outputRouting.ExceptionId} object.
     */
    public ExceptionId getErrorCode() {
        return errorCode;
    }

    /**
     * <p>Getter for the field <code>longDesciption</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLongDesciption() {
        return longDesciption;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get() {
        return null;
    }
    //endregion
}

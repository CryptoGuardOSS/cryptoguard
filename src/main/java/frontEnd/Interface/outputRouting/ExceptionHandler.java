package frontEnd.Interface.outputRouting;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
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
@Log4j2
public class ExceptionHandler extends Exception implements Supplier<String> {

    //region Attributes
    private ExceptionId errorCode;
    private ArrayList<String> longDesciption;
    //endregion

    //region Creations

    /**
     * <p>Constructor for ExceptionHandler.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param id      a {@link frontEnd.Interface.outputRouting.ExceptionId} object.
     */
    public ExceptionHandler(String message, ExceptionId id) {
        this(id, message);
        //super(message);
    }

    /**
     * <p>Constructor for ExceptionHandler with multiple strings.</p>
     *
     * @param id      a {@link frontEnd.Interface.outputRouting.ExceptionId} object.
     * @param message a {@link java.lang.String}... object.
     */
    public ExceptionHandler(ExceptionId id, String... message) {
        this.errorCode = id;
        Arrays.stream(message).forEach(this.getLongDesciption()::add);
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
                "Error Type: " + this.getLongDescriptionString() + "\n" +
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
    public ArrayList<String> getLongDesciption() {
        if (this.longDesciption == null)
            this.longDesciption = new ArrayList<>();
        return longDesciption;
    }

    public String getLongDescriptionString() {
        return Utils.join("\n", this.getLongDesciption());
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

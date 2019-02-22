package main.frontEnd.Interface;

/**
 * <p>ExceptionId class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/21/19.
 * @version $Id: $Id
 * @since 03.02.11
 *
 * <p>{Description Here}</p>
 */
public enum ExceptionId {
    //region Values
    HELP(0, "Asking For Help"),
    VERSION(0, "Asking For Version"),
    GEN_VALID(1, "General Argument Validation"),
    FORMAT_VALID(2, "Format Specific Argument Validation"),
    ENV_VAR(3, "Environment Variable Not Set"),
    FILE_IO(4, "File IO"),
    UNKWN(27, "Unknown");
    //endregion

    //region Attributes
    private Integer id;
    private String message;
    //endregion

    //region constructor
    ExceptionId(Integer id, String msg) {
        this.id = id;
        this.message = msg;
    }

    //endregion

    //region Getter

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getId() {
        return id;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMessage() {
        return message;
    }
    //endregion
}

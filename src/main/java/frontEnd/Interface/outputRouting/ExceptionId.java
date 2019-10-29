package frontEnd.Interface.outputRouting;

/**
 * <p>ExceptionId class.</p>
 *
 * @author CryptoguardTeam
 * Created on 2/21/19.
 * @version 03.07.01
 * @since 03.02.11
 *
 * <p>The enumeration of the error codes.</p>
 */
public enum ExceptionId {
    //region Values

    //region Range 0: Success
    SUCCESS(0, "Successful"),
    HELP(0, "Asking For Help"),
    VERSION(0, "Asking For Version"),
    //endregion
    //region Range 1 -> 14 : Input Validation
    GEN_VALID(1, "General Argument Validation"),
    ARG_VALID(2, "Argument Value Validation"),
    FORMAT_VALID(7, "Format Specific Argument Validation"),
    //endregion
    //region Range 15 -> 29 : File I
    FILE_I(15, "File Input Error"),
    FILE_READ(16, "Reading File Error"),
    FILE_AFK(17, "File Not Available"),
    //endregion
    //region Range 30 -> 44 : File O
    FILE_O(30, "File Output Error"),
    FILE_CON(31, "Output File Creation Error"),
    FILE_CUT(32, "Error Closing The File"),
    //endregion
    //region Range 45 -> 49 : Environment Variable
    ENV_VAR(45, "Environment Variable Not Set"),
    //endregion
    //region Range 50 ->... -> 99 : TBD
    //endregion
    //region Range 100 -> 119 : Our Issue
    MAR_VAR(100, "Error Marshalling The Output"),
    //endregion
    //region Range 120 -> 127 : Hail Mary
    SCAN_GEN(120, "General Error Scanning The Program"),
    LOADING(121, "Error Loading Class"),
    UNKWN(127, "Unknown"),
    //endregion
    ;
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

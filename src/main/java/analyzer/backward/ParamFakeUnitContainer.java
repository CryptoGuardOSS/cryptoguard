package analyzer.backward;

/**
 * <p>ParamFakeUnitContainer class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class ParamFakeUnitContainer extends UnitContainer {

    private int param;
    private String callee;

    /**
     * <p>Getter for the field <code>param</code>.</p>
     *
     * @return a int.
     */
    public int getParam() {
        return param;
    }

    /**
     * <p>Setter for the field <code>param</code>.</p>
     *
     * @param param a int.
     */
    public void setParam(int param) {
        this.param = param;
    }

    /**
     * <p>Getter for the field <code>callee</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCallee() {
        return callee;
    }

    /**
     * <p>Setter for the field <code>callee</code>.</p>
     *
     * @param callee a {@link java.lang.String} object.
     */
    public void setCallee(String callee) {
        this.callee = callee;
    }
}

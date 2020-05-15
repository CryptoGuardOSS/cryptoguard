/* Licensed under GPL-3.0 */
package analyzer.backward;

/**
 * ParamFakeUnitContainer class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class ParamFakeUnitContainer extends UnitContainer {

  private int param;
  private String callee;

  /**
   * Getter for the field <code>param</code>.
   *
   * @return a int.
   */
  public int getParam() {
    return param;
  }

  /**
   * Setter for the field <code>param</code>.
   *
   * @param param a int.
   */
  public void setParam(int param) {
    this.param = param;
  }

  /**
   * Getter for the field <code>callee</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getCallee() {
    return callee;
  }

  /**
   * Setter for the field <code>callee</code>.
   *
   * @param callee a {@link java.lang.String} object.
   */
  public void setCallee(String callee) {
    this.callee = callee;
  }
}

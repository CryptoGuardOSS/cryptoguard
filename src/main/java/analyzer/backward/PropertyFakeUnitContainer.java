/* Licensed under GPL-3.0 */
package analyzer.backward;

/**
 * PropertyFakeUnitContainer class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class PropertyFakeUnitContainer extends UnitContainer {

  private String originalProperty;

  /**
   * Getter for the field <code>originalProperty</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getOriginalProperty() {
    return originalProperty;
  }

  /**
   * Setter for the field <code>originalProperty</code>.
   *
   * @param originalProperty a {@link java.lang.String} object.
   */
  public void setOriginalProperty(String originalProperty) {
    this.originalProperty = originalProperty;
  }
}

/* Licensed under GPL-3.0 */
package slicer.forward;

/**
 * Created by RigorityJTeam on 7/3/17.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class SlicingCriteria {

  private String methodName;

  /**
   * Constructor for SlicingCriteria.
   *
   * @param methodName a {@link java.lang.String} object.
   */
  public SlicingCriteria(String methodName) {
    this.methodName = methodName;
  }

  /**
   * Getter for the field <code>methodName</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Setter for the field <code>methodName</code>.
   *
   * @param methodName a {@link java.lang.String} object.
   */
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SlicingCriteria that = (SlicingCriteria) o;

    return methodName.equals(that.methodName);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return methodName.hashCode();
  }
}

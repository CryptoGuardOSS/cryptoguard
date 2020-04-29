/* Licensed under GPL-3.0 */
package slicer.backward;

import analyzer.backward.MethodWrapper;

/**
 * Created by RigorityJTeam on 7/3/17.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class MethodCallSiteInfo {

  private MethodWrapper callee;
  private MethodWrapper caller;
  private int lineNumber;
  private int columnNumber;

  /**
   * Constructor for MethodCallSiteInfo.
   *
   * @param caller a {@link analyzer.backward.MethodWrapper} object.
   * @param callee a {@link analyzer.backward.MethodWrapper} object.
   * @param lineNumber a int.
   * @param columnNumber a int.
   */
  public MethodCallSiteInfo(
      MethodWrapper caller, MethodWrapper callee, int lineNumber, int columnNumber) {
    this.caller = caller;
    this.callee = callee;
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
  }

  /**
   * Getter for the field <code>callee</code>.
   *
   * @return a {@link analyzer.backward.MethodWrapper} object.
   */
  public MethodWrapper getCallee() {
    return callee;
  }

  /**
   * Setter for the field <code>callee</code>.
   *
   * @param callee a {@link analyzer.backward.MethodWrapper} object.
   */
  public void setCallee(MethodWrapper callee) {
    this.callee = callee;
  }

  /**
   * Getter for the field <code>lineNumber</code>.
   *
   * @return a int.
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Setter for the field <code>lineNumber</code>.
   *
   * @param lineNumber a int.
   */
  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  /**
   * Getter for the field <code>columnNumber</code>.
   *
   * @return a int.
   */
  public int getColumnNumber() {
    return columnNumber;
  }

  /**
   * Setter for the field <code>columnNumber</code>.
   *
   * @param columnNumber a int.
   */
  public void setColumnNumber(int columnNumber) {
    this.columnNumber = columnNumber;
  }

  /**
   * Getter for the field <code>caller</code>.
   *
   * @return a {@link analyzer.backward.MethodWrapper} object.
   */
  public MethodWrapper getCaller() {
    return caller;
  }

  /**
   * Setter for the field <code>caller</code>.
   *
   * @param caller a {@link analyzer.backward.MethodWrapper} object.
   */
  public void setCaller(MethodWrapper caller) {
    this.caller = caller;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MethodCallSiteInfo that = (MethodCallSiteInfo) o;

    if (lineNumber != that.lineNumber) return false;
    if (columnNumber != that.columnNumber) return false;
    return callee.equals(that.callee) && caller.equals(that.caller);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int result = callee.hashCode();
    result = 31 * result + caller.hashCode();
    result = 31 * result + lineNumber;
    result = 31 * result + columnNumber;
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "MethodCallSiteInfo{"
        + "callee="
        + callee
        + ", caller="
        + caller
        + ", lineNumber="
        + lineNumber
        + ", columnNumber="
        + columnNumber
        + '}';
  }
}

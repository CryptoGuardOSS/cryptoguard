/* Licensed under GPL-3.0 */
package analyzer.backward;

import java.util.ArrayList;
import java.util.List;
import slicer.backward.MethodCallSiteInfo;
import soot.SootMethod;

/**
 * Created by RigorityJTeam on 12/27/16.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class MethodWrapper {

  private boolean isTopLevel = true;
  private SootMethod method;
  private List<MethodCallSiteInfo> calleeList;
  private List<MethodWrapper> callerList;

  /**
   * Constructor for MethodWrapper.
   *
   * @param method a {@link soot.SootMethod} object.
   */
  public MethodWrapper(SootMethod method) {
    this.method = method;
    this.calleeList = new ArrayList<>();
    this.callerList = new ArrayList<>();
  }

  /**
   * Getter for the field <code>method</code>.
   *
   * @return a {@link soot.SootMethod} object.
   */
  public SootMethod getMethod() {
    return method;
  }

  /**
   * Setter for the field <code>method</code>.
   *
   * @param method a {@link soot.SootMethod} object.
   */
  public void setMethod(SootMethod method) {
    this.method = method;
  }

  /**
   * Getter for the field <code>calleeList</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<MethodCallSiteInfo> getCalleeList() {
    return calleeList;
  }

  /**
   * isTopLevel.
   *
   * @return a boolean.
   */
  public boolean isTopLevel() {
    return isTopLevel;
  }

  /**
   * setTopLevel.
   *
   * @param topLevel a boolean.
   */
  public void setTopLevel(boolean topLevel) {
    isTopLevel = topLevel;
  }

  /**
   * Getter for the field <code>callerList</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<MethodWrapper> getCallerList() {
    return callerList;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MethodWrapper methodWrapper = (MethodWrapper) o;
    return method.toString().equals(methodWrapper.method.toString());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return method.toString().hashCode();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return method.toString();
  }
}

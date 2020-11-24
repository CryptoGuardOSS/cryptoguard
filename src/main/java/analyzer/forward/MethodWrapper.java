/* Licensed under GPL-3.0 */
package analyzer.forward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slicer.forward.MethodCallSiteInfo;
import slicer.forward.SlicingResult;
import soot.SootMethod;

/**
 * Created by RigorityJTeam on 12/27/16.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class MethodWrapper {

  private SootMethod method;
  private List<MethodWrapper> calleeList;
  private Map<MethodCallSiteInfo, SlicingResult> analysisListMap;

  /**
   * Constructor for MethodWrapper.
   *
   * @param method a {@link soot.SootMethod} object.
   */
  public MethodWrapper(SootMethod method) {
    this.method = method;
    this.calleeList = new ArrayList<>();
    this.analysisListMap = new HashMap<>();
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
  public List<MethodWrapper> getCalleeList() {
    return calleeList;
  }

  /**
   * Setter for the field <code>calleeList</code>.
   *
   * @param calleeList a {@link java.util.List} object.
   */
  public void setCalleeList(List<MethodWrapper> calleeList) {
    this.calleeList = calleeList;
  }

  /**
   * Getter for the field <code>analysisListMap</code>.
   *
   * @return a {@link java.util.Map} object.
   */
  public Map<MethodCallSiteInfo, SlicingResult> getAnalysisListMap() {
    return analysisListMap;
  }

  /**
   * Setter for the field <code>analysisListMap</code>.
   *
   * @param analysisListMap a {@link java.util.Map} object.
   */
  public void setAnalysisListMap(Map<MethodCallSiteInfo, SlicingResult> analysisListMap) {
    this.analysisListMap = analysisListMap;
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

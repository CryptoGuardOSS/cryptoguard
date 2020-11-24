/* Licensed under GPL-3.0 */
package slicer.forward;

import java.util.List;
import soot.Unit;

/**
 * Created by RigorityJTeam on 10/14/16.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class SlicingResult {

  private MethodCallSiteInfo callSiteInfo;
  private List<Unit> analysisResult;

  /**
   * Getter for the field <code>callSiteInfo</code>.
   *
   * @return a {@link slicer.forward.MethodCallSiteInfo} object.
   */
  public MethodCallSiteInfo getCallSiteInfo() {
    return callSiteInfo;
  }

  /**
   * Setter for the field <code>callSiteInfo</code>.
   *
   * @param callSiteInfo a {@link slicer.forward.MethodCallSiteInfo} object.
   */
  public void setCallSiteInfo(MethodCallSiteInfo callSiteInfo) {
    this.callSiteInfo = callSiteInfo;
  }

  /**
   * Getter for the field <code>analysisResult</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<Unit> getAnalysisResult() {
    return analysisResult;
  }

  /**
   * Setter for the field <code>analysisResult</code>.
   *
   * @param analysisResult a {@link java.util.List} object.
   */
  public void setAnalysisResult(List<Unit> analysisResult) {
    this.analysisResult = analysisResult;
  }
}

/* Licensed under GPL-3.0 */
package slicer.backward.orthogonal;

import analyzer.backward.UnitContainer;
import java.util.List;
import java.util.Map;
import slicer.backward.MethodCallSiteInfo;
import slicer.backward.property.PropertyAnalysisResult;

/**
 * Created by RigorityJTeam on 10/14/16.
 *
 * @author franceme
 * @version 03.07.01
 */
public class OrthogonalSlicingResult {

  private MethodCallSiteInfo callSiteInfo;
  private List<UnitContainer> analysisResult;
  private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

  /**
   * Getter for the field <code>callSiteInfo</code>.
   *
   * @return a {@link slicer.backward.MethodCallSiteInfo} object.
   */
  public MethodCallSiteInfo getCallSiteInfo() {
    return callSiteInfo;
  }

  /**
   * Setter for the field <code>callSiteInfo</code>.
   *
   * @param callSiteInfo a {@link slicer.backward.MethodCallSiteInfo} object.
   */
  public void setCallSiteInfo(MethodCallSiteInfo callSiteInfo) {
    this.callSiteInfo = callSiteInfo;
  }

  /**
   * Getter for the field <code>analysisResult</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<UnitContainer> getAnalysisResult() {
    return analysisResult;
  }

  /**
   * Setter for the field <code>analysisResult</code>.
   *
   * @param analysisResult a {@link java.util.List} object.
   */
  public void setAnalysisResult(List<UnitContainer> analysisResult) {
    this.analysisResult = analysisResult;
  }

  /**
   * Getter for the field <code>propertyUseMap</code>.
   *
   * @return a {@link java.util.Map} object.
   */
  public Map<String, List<PropertyAnalysisResult>> getPropertyUseMap() {
    return propertyUseMap;
  }

  /**
   * Setter for the field <code>propertyUseMap</code>.
   *
   * @param propertyUseMap a {@link java.util.Map} object.
   */
  public void setPropertyUseMap(Map<String, List<PropertyAnalysisResult>> propertyUseMap) {
    this.propertyUseMap = propertyUseMap;
  }
}

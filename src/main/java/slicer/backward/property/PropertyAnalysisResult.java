/* Licensed under GPL-3.0 */
package slicer.backward.property;

import analyzer.backward.MethodWrapper;
import analyzer.backward.UnitContainer;
import java.util.List;
import java.util.Map;

/**
 * PropertyAnalysisResult class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class PropertyAnalysisResult {

  private MethodWrapper methodWrapper;
  private List<Integer> influencingParams;
  private List<UnitContainer> slicingResult;
  private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

  /**
   * Getter for the field <code>methodWrapper</code>.
   *
   * @return a {@link analyzer.backward.MethodWrapper} object.
   */
  public MethodWrapper getMethodWrapper() {
    return methodWrapper;
  }

  /**
   * Setter for the field <code>methodWrapper</code>.
   *
   * @param methodWrapper a {@link analyzer.backward.MethodWrapper} object.
   */
  public void setMethodWrapper(MethodWrapper methodWrapper) {
    this.methodWrapper = methodWrapper;
  }

  /**
   * Getter for the field <code>influencingParams</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<Integer> getInfluencingParams() {
    return influencingParams;
  }

  /**
   * Setter for the field <code>influencingParams</code>.
   *
   * @param influencingParams a {@link java.util.List} object.
   */
  public void setInfluencingParams(List<Integer> influencingParams) {
    this.influencingParams = influencingParams;
  }

  /**
   * Getter for the field <code>slicingResult</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<UnitContainer> getSlicingResult() {
    return slicingResult;
  }

  /**
   * Setter for the field <code>slicingResult</code>.
   *
   * @param slicingResult a {@link java.util.List} object.
   */
  public void setSlicingResult(List<UnitContainer> slicingResult) {
    this.slicingResult = slicingResult;
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

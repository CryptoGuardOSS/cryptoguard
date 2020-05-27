/* Licensed under GPL-3.0 */
package analyzer.backward;

import java.util.List;

/**
 * Analysis class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class Analysis {
  private String methodChain;
  private List<UnitContainer> analysisResult;

  /**
   * Getter for the field <code>methodChain</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getMethodChain() {
    return methodChain;
  }

  /**
   * Setter for the field <code>methodChain</code>.
   *
   * @param methodChain a {@link java.lang.String} object.
   */
  public void setMethodChain(String methodChain) {
    this.methodChain = methodChain;
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
}

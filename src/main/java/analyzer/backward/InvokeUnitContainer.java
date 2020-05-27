/* Licensed under GPL-3.0 */
package analyzer.backward;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * InvokeUnitContainer class.
 *
 * @author franceme
 * @version 03.07.01
 */
public class InvokeUnitContainer extends UnitContainer {

  private List<Integer> args = new ArrayList<>();
  private Set<String> definedFields = new HashSet<>();
  private List<UnitContainer> analysisResult = new ArrayList<>();

  /**
   * Getter for the field <code>args</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<Integer> getArgs() {
    return args;
  }

  /**
   * Setter for the field <code>args</code>.
   *
   * @param args a {@link java.util.List} object.
   */
  public void setArgs(List<Integer> args) {
    this.args = args;
  }

  /**
   * Getter for the field <code>definedFields</code>.
   *
   * @return a {@link java.util.Set} object.
   */
  public Set<String> getDefinedFields() {
    return definedFields;
  }

  /**
   * Setter for the field <code>definedFields</code>.
   *
   * @param definedFields a {@link java.util.Set} object.
   */
  public void setDefinedFields(Set<String> definedFields) {
    this.definedFields = definedFields;
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

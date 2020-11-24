/* Licensed under GPL-3.0 */
package analyzer.backward;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AssignInvokeUnitContainer class.
 *
 * @author franceme
 * @version 03.07.01
 */
public class AssignInvokeUnitContainer extends UnitContainer {
  private List<Integer> args = new ArrayList<>();
  private Set<String> properties = new HashSet<>();
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
   * Getter for the field <code>properties</code>.
   *
   * @return a {@link java.util.Set} object.
   */
  public Set<String> getProperties() {
    return properties;
  }

  /**
   * Setter for the field <code>properties</code>.
   *
   * @param properties a {@link java.util.Set} object.
   */
  public void setProperties(Set<String> properties) {
    this.properties = properties;
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

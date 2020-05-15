/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.common;

import java.util.ArrayList;

/**
 * Heuristics class.
 *
 * @author maister
 * @version 03.10.04
 */
public class Heuristics {

  private int numberOfOrthogonal = 0;
  private int numberOfConstantsToCheck = 0;
  private int numberOfHeuristics = 0;
  private int numberOfSlices = 0;
  private double sliceAverage = 0;
  private ArrayList<String> depthCount;

  /**
   * Getter for the field <code>depthCount</code>.
   *
   * @return a {@link java.util.ArrayList} object.
   */
  public ArrayList<String> getDepthCount() {
    if (this.depthCount == null) this.depthCount = new ArrayList<>();

    return this.depthCount;
  }

  /**
   * Setter for the field <code>depthCount</code>.
   *
   * @param depthCount a {@link java.util.ArrayList} object.
   */
  public void setDepthCount(ArrayList<String> depthCount) {
    this.depthCount = depthCount;
  }

  /**
   * addDepthCount.
   *
   * @param depth a {@link java.lang.String} object.
   */
  public void addDepthCount(String depth) {
    this.getDepthCount().add(depth);
  }

  /**
   * getDefaultHeuristics.
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
   */
  public frontEnd.MessagingSystem.routing.structure.Default.Heuristics getDefaultHeuristics() {
    frontEnd.MessagingSystem.routing.structure.Default.Heuristics output =
        new frontEnd.MessagingSystem.routing.structure.Default.Heuristics();

    output.setNumberOfOrthogonal(this.numberOfOrthogonal);
    output.setNumberOfConstantsToCheck(this.numberOfConstantsToCheck);
    output.setNumberOfSlices(this.numberOfSlices);
    output.setNumberOfHeuristics(this.numberOfHeuristics);
    output.setAverageSlice(this.sliceAverage);
    output.setDepthCount(this.getDepthCount());

    return output;
  }

  /**
   * getScarfXMLHeuristics.
   *
   * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics} object.
   */
  public frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics getScarfXMLHeuristics() {
    frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics output =
        new frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics();

    output.setNumberOfOrthogonal(this.numberOfOrthogonal);
    output.setNumberOfConstantsToCheck(this.numberOfConstantsToCheck);
    output.setNumberOfSlices(this.numberOfSlices);
    output.setNumberOfHeuristics(this.numberOfHeuristics);
    output.setAverageSlice(this.sliceAverage);
    output.setDepthCount(this.getDepthCount());

    return output;
  }

  /**
   * toString.
   *
   * @return a {@link java.lang.String} object.
   */
  public String toString() {
    return "Heuristics(numberOfOrthogonal="
        + this.numberOfOrthogonal
        + ", numberOfConstantsToCheck="
        + this.numberOfConstantsToCheck
        + ", numberOfHeuristics="
        + this.numberOfHeuristics
        + ", numberOfSlices="
        + this.numberOfSlices
        + ", sliceAverage="
        + this.sliceAverage
        + ", depthCount="
        + this.getDepthCount()
        + ")";
  }

  /**
   * Getter for the field <code>numberOfOrthogonal</code>.
   *
   * @return a int.
   */
  public int getNumberOfOrthogonal() {
    return this.numberOfOrthogonal;
  }

  /**
   * Getter for the field <code>numberOfConstantsToCheck</code>.
   *
   * @return a int.
   */
  public int getNumberOfConstantsToCheck() {
    return this.numberOfConstantsToCheck;
  }

  /**
   * Getter for the field <code>numberOfHeuristics</code>.
   *
   * @return a int.
   */
  public int getNumberOfHeuristics() {
    return this.numberOfHeuristics;
  }

  /**
   * Getter for the field <code>numberOfSlices</code>.
   *
   * @return a int.
   */
  public int getNumberOfSlices() {
    return this.numberOfSlices;
  }

  /**
   * Getter for the field <code>sliceAverage</code>.
   *
   * @return a double.
   */
  public double getSliceAverage() {
    return this.sliceAverage;
  }

  /**
   * Setter for the field <code>numberOfOrthogonal</code>.
   *
   * @param numberOfOrthogonal a int.
   */
  public void setNumberOfOrthogonal(int numberOfOrthogonal) {
    this.numberOfOrthogonal = numberOfOrthogonal;
  }

  /**
   * Setter for the field <code>numberOfConstantsToCheck</code>.
   *
   * @param numberOfConstantsToCheck a int.
   */
  public void setNumberOfConstantsToCheck(int numberOfConstantsToCheck) {
    this.numberOfConstantsToCheck = numberOfConstantsToCheck;
  }

  /**
   * Setter for the field <code>numberOfHeuristics</code>.
   *
   * @param numberOfHeuristics a int.
   */
  public void setNumberOfHeuristics(int numberOfHeuristics) {
    this.numberOfHeuristics = numberOfHeuristics;
  }

  /**
   * Setter for the field <code>numberOfSlices</code>.
   *
   * @param numberOfSlices a int.
   */
  public void setNumberOfSlices(int numberOfSlices) {
    this.numberOfSlices = numberOfSlices;
  }

  /**
   * Setter for the field <code>sliceAverage</code>.
   *
   * @param sliceAverage a double.
   */
  public void setSliceAverage(double sliceAverage) {
    this.sliceAverage = sliceAverage;
  }
}

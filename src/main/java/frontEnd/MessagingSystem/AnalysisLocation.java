/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem;

/**
 * AnalysisLocation class.
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.00
 */
public class AnalysisLocation {

  //region Attributes
  private Integer lineStart = null;
  private Integer lineEnd = null;
  private Integer colStart = null;
  private Integer colEnd = null;
  private Integer methodNumber = -1;
  //endregion

  //region Constructor

  /**
   * Constructor for AnalysisLocation.
   *
   * @param start a {@link java.lang.Integer} object.
   * @param end a {@link java.lang.Integer} object.
   */
  public AnalysisLocation(Integer start, Integer end) {
    this.lineStart = start;
    this.lineEnd = end;
  }

  /**
   * Constructor for AnalysisLocation.
   *
   * @param lineNumber a {@link java.lang.Integer} object.
   */
  public AnalysisLocation(Integer lineNumber) {
    this.lineStart = lineNumber;
    this.lineEnd = lineNumber;
  }

  /**
   * Constructor for AnalysisLocation.
   *
   * @param start a {@link java.lang.Integer} object.
   * @param end a {@link java.lang.Integer} object.
   * @param methodNumber a {@link java.lang.Integer} object.
   */
  public AnalysisLocation(Integer start, Integer end, Integer methodNumber) {
    this.lineStart = start;
    this.lineEnd = end;
    this.methodNumber = methodNumber;
  }
  //endregion

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();

    output.append(this.lineStart);

    if (!this.lineEnd.equals(this.lineStart)) {
      output.append("-");
      output.append(this.lineEnd);
    }

    return output.toString();
  }
  //endregion

  //region Getters

  /**
   * Getter for the field <code>lineStart</code>.
   *
   * @return a {@link java.lang.Integer} object.
   */
  public Integer getLineStart() {
    return lineStart;
  }

  /**
   * Getter for the field <code>lineEnd</code>.
   *
   * @return a {@link java.lang.Integer} object.
   */
  public Integer getLineEnd() {
    return lineEnd;
  }

  /**
   * Getter for the field <code>methodNumber</code>.
   *
   * @return a {@link java.lang.Integer} object.
   */
  public Integer getMethodNumber() {
    return methodNumber;
  }

  /**
   * Setter for the field <code>methodNumber</code>.
   *
   * @param methodNumber a {@link java.lang.Integer} object.
   */
  public void setMethodNumber(Integer methodNumber) {
    this.methodNumber = methodNumber;
  }

  /**
   * Getter for colStart
   *
   * <p>getColStart()
   *
   * @return {@link java.lang.Integer} - The colStart.
   */
  public Integer getColStart() {
    return colStart;
  }

  /**
   * Setter for colStart
   *
   * <p>setColStart(java.lang.Integer colStart)
   *
   * @param colStart {@link java.lang.Integer} - The value to set as colStart
   */
  public void setColStart(Integer colStart) {
    this.colStart = colStart;
  }

  /**
   * Getter for colEnd
   *
   * <p>getColEnd()
   *
   * @return {@link java.lang.Integer} - The colEnd.
   */
  public Integer getColEnd() {
    return colEnd;
  }

  /**
   * Setter for colEnd
   *
   * <p>setColEnd(java.lang.Integer colEnd)
   *
   * @param colEnd {@link java.lang.Integer} - The value to set as colEnd
   */
  public void setColEnd(Integer colEnd) {
    this.colEnd = colEnd;
  }
  //endregion

}

/* Licensed under GPL-3.0 */
package frontEnd.Interface.outputRouting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import util.Utils;

/**
 * ExceptionHandler class.
 *
 * @author CryptoguardTeam Created on 2019-01-25.
 * @version 03.07.01
 * @since 02.00.03
 *     <p>The Main Exception Handling for the whole project
 */
public class ExceptionHandler extends Exception implements Supplier<String> {

  private static final Logger log =
      org.apache.logging.log4j.LogManager.getLogger(ExceptionHandler.class);
  //region Attributes
  private ExceptionId errorCode;
  private ArrayList<String> longDesciption;
  //endregion

  //region Creations

  /**
   * Constructor for ExceptionHandler.
   *
   * @param message a {@link java.lang.String} object.
   * @param id a {@link frontEnd.Interface.outputRouting.ExceptionId} object.
   */
  public ExceptionHandler(String message, ExceptionId id) {
    this(id, message);
    //super(message);
  }

  /**
   * Constructor for ExceptionHandler with multiple strings.
   *
   * @param id a {@link frontEnd.Interface.outputRouting.ExceptionId} object.
   * @param message a {@link java.lang.String}... object.
   */
  public ExceptionHandler(ExceptionId id, String... message) {
    this.errorCode = id;
    Arrays.stream(message).forEach(this.getLongDesciption()::add);
  }
  //endregion

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public String toString() {

    String resp =
        "==================================\n"
            + "Error ID: "
            + this.errorCode.getId()
            + "\n"
            + "Error Type: "
            + this.getLongDescriptionString()
            + "\n"
            + "Error Message: \n"
            + this.longDesciption
            + "\n"
            + "==================================";
    return StringUtils.trimToNull(resp).concat("\n\n\n");
  }

  /** {@inheritDoc} */
  @Override
  public void printStackTrace() {
    System.err.println(this.toString());
  }
  //endregion

  //region Getters

  /**
   * Getter for the field <code>errorCode</code>.
   *
   * @return a {@link frontEnd.Interface.outputRouting.ExceptionId} object.
   */
  public ExceptionId getErrorCode() {
    return errorCode;
  }

  /**
   * Getter for the field <code>longDesciption</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public ArrayList<String> getLongDesciption() {
    if (this.longDesciption == null) this.longDesciption = new ArrayList<>();
    return longDesciption;
  }

  /**
   * getLongDescriptionString.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getLongDescriptionString() {
    return Utils.join("\n", this.getLongDesciption());
  }

  /** {@inheritDoc} */
  @Override
  public String get() {
    return null;
  }
  //endregion
}

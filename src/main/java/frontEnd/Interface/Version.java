/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import java.util.Arrays;
import org.apache.logging.log4j.Logger;
import util.Utils;

/**
 * version handling class.
 *
 * @author franceme Created on 10/27/19.
 * @version V03.07.06
 * @since V03.07.06
 *     <p>The central point for handling the Java Version ().
 */
public enum Version {
  //region Values
  ONE(1, 45),
  TWO(2, 46),
  THREE(3, 47),
  FOUR(4, 48),
  FIVE(5, 49),
  SIX(6, 50),
  SEVEN(7, 51),
  EIGHT(8, 52),
  NINE(9, 53),
  TEN(10, 54),
  ELEVEN(11, 55),
  TWELVE(12, 56),
  THIRTEEN(13, 57),
  FOURTEEN(14, 58),
  FIFTEEN(15, 59),
  SIXTEEN(16, 60),
  SEVENTEEN(17, 61);
  //endregion

  //region Attributes
  private int versionNumber;
  private int majorVersion;
  private static Logger log = org.apache.logging.log4j.LogManager.getLogger(Version.class);
  //endregion

  //region Constructor
  Version(int versionNumber, int majorVersion) {
    this.versionNumber = versionNumber;
    this.majorVersion = majorVersion;
  }
  //endregion

  //region Methods

  //region Overridden Methods

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.valueOf(this.versionNumber);
  }
  //endregion

  /**
   * supported.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  public Boolean supported() {
    return this.majorVersion == Utils.supportedVersion.majorVersion;
  }

  /**
   * retrieveByMajor.
   *
   * @param majorVersion a int.
   * @return a {@link frontEnd.Interface.Version} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static Version retrieveByMajor(int majorVersion) throws ExceptionHandler {
    return Arrays.stream(Version.values())
        .filter(v -> v.getMajorVersion() == majorVersion)
        .findFirst()
        .orElseThrow(
            () ->
                new ExceptionHandler(
                    "Major Version: " + majorVersion + " not valid.", ExceptionId.FILE_AFK));
  }

  /**
   * getRunningVersion.
   *
   * @return a {@link frontEnd.Interface.Version} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static Version getRunningVersion() throws ExceptionHandler {
    String version = System.getProperty("java.version");

    log.debug("Java Version being used:" + version);

    //Used for Java JRE versions below 9
    if (version.startsWith("1.")) version = version.replaceFirst("1.", "");

    //Getting the major number
    int versionNumber = Integer.parseInt(version.substring(0, version.indexOf(".", 0)));

    return Arrays.stream(Version.values())
        .filter(v -> v.getVersionNumber() == versionNumber)
        .findFirst()
        .orElse(Version.ONE);
  }

  /**
   * supportedByMajor.
   *
   * @param majorVersion a int.
   * @return a boolean.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static boolean supportedByMajor(int majorVersion) throws ExceptionHandler {
    return retrieveByMajor(majorVersion).getVersionNumber()
        <= Utils.supportedVersion.getVersionNumber();
  }

  /**
   * supportedFile.
   *
   * @return a boolean.
   */
  public boolean supportedFile() {
    return this.getVersionNumber() <= Utils.supportedVersion.getVersionNumber();
  }

  /**
   * Getter for the field <code>versionNumber</code>.
   *
   * @return a int.
   */
  public int getVersionNumber() {
    return this.versionNumber;
  }

  /**
   * Getter for the field <code>majorVersion</code>.
   *
   * @return a int.
   */
  public int getMajorVersion() {
    return this.majorVersion;
  }
  //endregion
}

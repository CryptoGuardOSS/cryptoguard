/* Licensed under GPL-3.0 */
package util;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import java.util.List;
import java.util.Map;

/**
 * BuildFileParser interface.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public interface BuildFileParser {

  /**
   * getDependencyList.
   *
   * @return a {@link java.util.Map} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  Map<String, List<String>> getDependencyList() throws ExceptionHandler;

  /**
   * getProjectName.
   *
   * @return a {@link java.lang.String} object.
   */
  String getProjectName();

  /**
   * getProjectVersion.
   *
   * @return a {@link java.lang.String} object.
   */
  String getProjectVersion();

  /**
   * isGradle.
   *
   * @return a {@link java.lang.Boolean} object.
   */
  Boolean isGradle();
}

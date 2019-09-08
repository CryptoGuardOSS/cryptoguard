package util;

import frontEnd.Interface.outputRouting.ExceptionHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>BuildFileParser interface.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public interface BuildFileParser {

    /**
     * <p>getDependencyList.</p>
     *
     * @return a {@link java.util.Map} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    Map<String, List<String>> getDependencyList() throws ExceptionHandler;

    /**
     * <p>getProjectName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getProjectName();

    /**
     * <p>getProjectVersion.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getProjectVersion();

    /**
     * <p>isGradle.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    Boolean isGradle();

}

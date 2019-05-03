package util;

import frontEnd.Interface.outputRouting.ExceptionHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>BuildFileParser interface.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
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

    String getProjectName();

    String getProjectVersion();

    Boolean isGradle();

}

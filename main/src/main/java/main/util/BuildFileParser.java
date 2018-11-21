package main.util;

import java.util.List;
import java.util.Map;

/**
 * <p>BuildFileParser interface.</p>
 *
 * @author RigorityJTeam
 * @since V01.00
 */
public interface BuildFileParser {

	/**
	 * <p>getDependencyList.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 * @throws java.lang.Exception if any.
	 */
	Map<String, List<String>> getDependencyList() throws Exception;

}

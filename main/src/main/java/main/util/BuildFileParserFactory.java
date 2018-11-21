package main.util;

import java.io.File;

/**
 * <p>BuildFileParserFactory class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00
 */
public class BuildFileParserFactory {

	/**
	 * <p>getBuildfileParser.</p>
	 *
	 * @param projectRoot a {@link java.lang.String} object.
	 * @return a {@link main.util.BuildFileParser} object.
	 * @throws java.lang.Exception if any.
	 */
	public static BuildFileParser getBuildfileParser(String projectRoot) throws Exception {

		File pomFile = new File(projectRoot + "/" + "pom.xml");

		if (pomFile.exists()) {
			return new MvnPomFileParser(projectRoot + "/" + "pom.xml");
		}

		File gradleFile = new File(projectRoot + "/" + "settings.gradle");

		if (gradleFile.exists()) {
			return new GradleBuildFileParser(projectRoot + "/" + "settings.gradle");
		}

		throw new RuntimeException("Only maven and gradle projects are supported ...");

	}
}

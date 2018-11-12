package main.rule.engine;

/**
 * The different types of "sources" accepted to examine.
 * <ul>
 * <li>JAR - A Jar file with or without external dependencies</li>
 * <li>APK - A Apk file with or without external dependencies</li>
 * <li>SOURCE - The source code of a Maven or a Gradle Project</li>
 * </ul>
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public enum EngineType
{
	JAR,
	APK,
	SOURCE;
}

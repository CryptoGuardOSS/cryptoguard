package main.util;

import java.io.File;

public class BuildFileParserFactory {

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

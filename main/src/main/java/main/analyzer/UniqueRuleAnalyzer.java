package main.analyzer;

import main.rule.engine.EngineType;
import main.util.Utils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author RigorityJTeam
 * Created on 2019-01-25.
 * @since 02.00.04
 *
 * <p>The funneling class to handle the various different implementations of setting up the SOOT environment</p>
 * <p>
 * Used for the following rules...
 * <ol>
 * <li>{@link main.rule.UntrustedPrngFinder UntrustedPrngFinder}</li>
 * <li>{@link main.rule.SSLSocketFactoryFinder SSLSocketFactoryFinder}</li>
 * <li>{@link main.rule.CustomTrustManagerFinder CustomTrustManagerFinder}</li>
 * <li>{@link main.rule.HostNameVerifierFinder HostNameVerifierFinder}</li>
 * <li>{@link main.rule.DefaultExportGradeKeyFinder DefaultExportGradeKeyFinder}</li>
 * </ol>
 *
 * </p>
 */
public class UniqueRuleAnalyzer {

    public static List<String> environmentRouting(List<String> projectJarPath, List<String> projectDependencyPath, EngineType routingType) {
        if (routingType == EngineType.JAR) {
            return setupBaseJarEnv(projectJarPath.get(0),
                    projectDependencyPath.size() >= 1
                            ? projectDependencyPath.get(0)
                            : null); //TODO - Add dep null
        } else if (routingType == EngineType.APK) {
            return setupBaseAPKEnv(projectJarPath.get(0));
        } else if (routingType == EngineType.DIR) {
            return setupBaseSourceEnv(projectJarPath, projectDependencyPath);
        } else if (routingType == EngineType.JAVAFILES) {
            return setupJavaFileEnv(projectJarPath, projectDependencyPath);
        } else { //if (routingType == EngineType.JAVACLASSFILES)
            return setupJavaClassFileEnv(projectJarPath, projectDependencyPath);
        }
    }

    public static List<String> setupBaseJarEnv(String projectJarPath, String projectDependencyPath) {

        String java_home = Utils.getJAVA_HOME();

        List<String> sootPaths = new ArrayList<>();
        sootPaths.add(projectJarPath);
        sootPaths.add(Utils.osPathJoin(java_home, "jre", "lib", "rt.jar"));
        sootPaths.add(Utils.osPathJoin(java_home, "jre", "lib", "jce.jar"));

        if (projectDependencyPath != null)
            sootPaths.add(projectJarPath);

        Scene.v().setSootClassPath(Utils.buildSootClassPath(sootPaths));

        Utils.loadSootClasses(null);

        try {
            return Utils.getClassNamesFromJarArchive(projectJarPath);
        } catch (IOException e) {
            return null;
        }

    }

    public static List<String> setupBaseAPKEnv(String projectJarPath) {

        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(Utils.getANDROID() + "/platforms");
        Options.v().set_soot_classpath(Utils.getBaseSOOT());

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);

        Utils.loadSootClasses(null);

        try {
            return Utils.getClassNamesFromApkArchive(projectJarPath);
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> setupBaseSourceEnv(List<String> snippetPath, List<String> projectDependencyPath) {

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        String srcPaths = Utils.join(":", snippetPath);

        Options.v().set_soot_classpath(Utils.getBaseSOOT7() +
                ":" + srcPaths + Utils.buildSootClassPath(projectDependencyPath));

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Utils.loadSootClasses(classNames);

        return classNames;
    }

    //region Java Files
    //Like Dir
    public static List<String> setupJavaFileEnv(List<String> snippetPath, List<String> projectDependencyPath) {

        List<String> classNames = Utils.retrieveFullyQualifiedName(snippetPath);

        StringBuilder sootPath = new StringBuilder();
        sootPath.append(Utils.getBaseSOOT7())
                .append(":")
                .append(Utils.join(":", snippetPath));

        if (projectDependencyPath.size() >= 1) {
            List<String> classPaths = Utils.retrieveTrimmedSourcePaths(snippetPath);
            sootPath.append(Utils.buildSootClassPath(Utils.retrieveBaseSourcePath(classPaths, projectDependencyPath.get(0))));
        }

        Options.v().set_soot_classpath(sootPath.toString());

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Utils.loadSootClasses(classNames);

        return classNames;
    }
    //endregion

    //region JavaClassFiles
    //Like Jar
    public static List<String> setupJavaClassFileEnv(List<String> javaClassFiles, List<String> projectDependencyPath) {

        Scene.v().setSootClassPath(Utils.getBaseSOOT());

        List<String> classNames = new ArrayList<>();
        for (String in : javaClassFiles)
            classNames.add(Utils.retrieveFullyQualifiedName(in));

        Utils.loadSootClasses(null);

        return classNames;
    }
    //endregion

}

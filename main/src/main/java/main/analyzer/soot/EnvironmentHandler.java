package main.analyzer.soot;

import main.util.Utils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

//TODO - fully implement

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
 * </ol>
 *
 * </p>
 */
public class EnvironmentHandler {

    private static final String JAVA7_HOME = System.getenv("JAVA7_HOME");
    private static final String JAVA_HOME = System.getenv("JAVA_HOME");
    private static final String ANDROID_HOME = System.getenv("ANDROID_SDK_HOME");

    public static List<String> setupBaseJarEnv(String projectJarPath, String projectDependencyPath) {

        if (JAVA_HOME.isEmpty()) {

            System.err.println("Please set JAVA_HOME");
            System.exit(1);
        }

        String sootClassPath = Utils.buildSootClassPath(projectJarPath,
                JAVA_HOME + "/jre/lib/rt.jar",
                JAVA_HOME + "/jre/lib/jce.jar",
                projectDependencyPath);

        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        Scene.v().setSootClassPath(sootClassPath);

        Scene.v().loadBasicClasses();

        try {
            return Utils.getClassNamesFromJarArchive(projectJarPath);
        } catch (IOException e) {
            return null;
        }

    }

    public static List<String> setupBaseAPKEnv(String projectJarPath) {
        if (JAVA_HOME == null) {

            System.err.println("Please set JAVA_HOME");
            System.exit(1);
        }

        if (ANDROID_HOME == null) {

            System.err.println("Please set ANDROID_SDK_HOME");
            System.exit(1);
        }

        Options.v().set_keep_line_number(true);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(ANDROID_HOME + "/platforms");
        Options.v().set_soot_classpath(JAVA_HOME + "/jre/lib/rt.jar:" + JAVA_HOME + "/jre/lib/jce.jar");

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);

        Scene.v().loadBasicClasses();

        try {
            return Utils.getClassNamesFromApkArchive(projectJarPath);
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> setupBaseSourceEnv(List<String> snippetPath, List<String> projectDependencyPath) {
        if (JAVA7_HOME.isEmpty()) {
            System.err.println("Please set JAVA7_HOME");
            System.exit(1);
        }

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        StringBuilder srcPaths = new StringBuilder();

        for (String srcDir : snippetPath) {
            srcPaths.append(srcDir)
                    .append(":");
        }

        Options.v().set_soot_classpath(JAVA7_HOME + "/jre/lib/rt.jar:"
                + JAVA7_HOME + "/jre/lib/jce.jar:" + srcPaths.toString() + Utils.buildSootClassPath(projectDependencyPath));

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        for (String className : classNames) {
            Options.v().classes().add(className);
        }

        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        Scene.v().loadBasicClasses();

        return classNames;
    }

    /** Stubbed Current Work
     //TODO - Dependencies?
     //Follows Source Dir
     public static List<String> setupJavaFileEnv(List<String> snippetPath, List<String> projectDependencyPath) {
     if (JAVA7_HOME.isEmpty()) {

     System.err.println("Please set JAVA7_HOME");
     System.exit(1);
     }


     List<String> classNames = Utils.retrieveFullyQualifiedName(snippetPath);
     List<String> classPaths = Utils.retrieveTrimmedSourcePaths(snippetPath);

     StringBuilder srcPaths = new StringBuilder();

     for (String srcDir : classPaths) {
     srcPaths.append(srcDir)
     .append(":");
     }

     String sootPath = JAVA7_HOME + "/jre/lib/rt.jar:"
     + JAVA7_HOME + "/jre/lib/jce.jar:" + srcPaths.toString()
     + Utils.buildSootClassPath(Utils.retrieveBaseSourcePath(classPaths,projectDependencyPath.get(0)));

     Options.v().set_soot_classpath(sootPath);

     Options.v().set_output_format(Options.output_format_jimple);
     Options.v().set_src_prec(Options.src_prec_java);

     for (String className : Utils.retrieveFullyQualifiedName(snippetPath)) {
     Options.v().classes().add(className);
     }

     Options.v().set_keep_line_number(true);
     Options.v().set_allow_phantom_refs(true);

     Scene.v().loadBasicClasses();

     return classNames;
     }

     //TODO - Create ClassNames from javaClassFiles, dependencies
     //Follows Jar
     public static List<String> setupJavaClassFileEnv(List<String> javaClassFiles, List<String> projectDependencyPath) {
     if (JAVA_HOME.isEmpty()) {

     System.err.println("Please set JAVA_HOME");
     System.exit(1);
     }

     StringBuilder sootClassPathBuilder = new StringBuilder();
     for (String in : javaClassFiles)
     sootClassPathBuilder.append(Utils.retrieveFullFilePath(in)).append(":");


     String sootClassPath = Utils.buildSootClassPath(
     JAVA_HOME + "/jre/lib/rt.jar",
     JAVA_HOME + "/jre/lib/jce.jar");

     Options.v().set_keep_line_number(true);
     Options.v().set_allow_phantom_refs(true);

     Scene.v().setSootClassPath(sootClassPath);

     Scene.v().loadBasicClasses();

     List<String> out = new ArrayList<>();
     for (String in : javaClassFiles)
     out.add(Utils.trimFilePath(in));

     //return javaClassFiles.stream().map(Utils::trimFilePath).collect(Collectors.toList());
     return out;
     }
     **/

}

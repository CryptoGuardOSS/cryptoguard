package analyzer;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import rule.*;
import rule.engine.EngineType;
import soot.Scene;
import soot.options.Options;
import util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>UniqueRuleAnalyzer class.</p>
 *
 * @author CryptoguardTeam
 * Created on 2019-01-25.
 * @version 03.07.01
 * @since 02.00.04
 *
 * <p>The funneling class to handle the various different implementations of setting up the SOOT environment</p>
 * <p>
 * Used for the following rules...
 * <ol>
 * <li>{@link UntrustedPrngFinder UntrustedPrngFinder}</li>
 * <li>{@link SSLSocketFactoryFinder SSLSocketFactoryFinder}</li>
 * <li>{@link CustomTrustManagerFinder CustomTrustManagerFinder}</li>
 * <li>{@link HostNameVerifierFinder HostNameVerifierFinder}</li>
 * <li>{@link DefaultExportGradeKeyFinder DefaultExportGradeKeyFinder}</li>
 * </ol>
 *
 * </p>
 */
public class UniqueRuleAnalyzer {

    /**
     * <p>environmentRouting.</p>
     *
     * @param projectJarPath        a {@link java.util.List} object.
     * @param projectDependencyPath a {@link java.util.List} object.
     * @param routingType           a {@link rule.engine.EngineType} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> environmentRouting(List<String> projectJarPath, List<String> projectDependencyPath, EngineType routingType) throws ExceptionHandler {
        if (routingType == EngineType.JAR) {
            return setupBaseJarEnv(projectJarPath.get(0),
                    projectDependencyPath.size() >= 1
                            ? projectDependencyPath.get(0)
                            : null);
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

    /**
     * <p>setupBaseJarEnv.</p>
     *
     * @param projectJarPath        a {@link java.lang.String} object.
     * @param projectDependencyPath a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> setupBaseJarEnv(String projectJarPath, String projectDependencyPath) throws ExceptionHandler {

        String java_home = Utils.getJAVA_HOME();

        List<String> sootPaths = new ArrayList<>();
        sootPaths.add(projectJarPath);
        sootPaths.add(Utils.osPathJoin(java_home, "jre", "lib", "rt.jar"));
        sootPaths.add(Utils.osPathJoin(java_home, "jre", "lib", "jce.jar"));

        if (projectDependencyPath != null)
            sootPaths.add(projectJarPath);

        Scene.v().setSootClassPath(Utils.buildSootClassPath(sootPaths));

        Utils.loadSootClasses(null);


        return Utils.getClassNamesFromJarArchive(projectJarPath);

    }

    /**
     * <p>setupBaseAPKEnv.</p>
     *
     * @param projectJarPath a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> setupBaseAPKEnv(String projectJarPath) throws ExceptionHandler {

        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(Utils.getANDROID() + "/platforms");
        Options.v().set_soot_classpath(Utils.getBaseSOOT());

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);

        Utils.loadSootClasses(null);

        return Utils.getClassNamesFromApkArchive(projectJarPath);

    }

    /**
     * <p>setupBaseSourceEnv.</p>
     *
     * @param snippetPath           a {@link java.util.List} object.
     * @param projectDependencyPath a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> setupBaseSourceEnv(List<String> snippetPath, List<String> projectDependencyPath) throws ExceptionHandler {

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

    /**
     * <p>setupJavaFileEnv.</p>
     *
     * @param snippetPath           a {@link java.util.List} object.
     * @param projectDependencyPath a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> setupJavaFileEnv(List<String> snippetPath, List<String> projectDependencyPath) throws ExceptionHandler {

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

    /**
     * <p>setupJavaClassFileEnv.</p>
     *
     * @param javaClassFiles        a {@link java.util.List} object.
     * @param projectDependencyPath a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> setupJavaClassFileEnv(List<String> javaClassFiles, List<String> projectDependencyPath) throws ExceptionHandler {

        Scene.v().setSootClassPath(Utils.getBaseSOOT());

        List<String> classNames = new ArrayList<>();
        for (String in : javaClassFiles)
            classNames.add(Utils.retrieveFullyQualifiedName(in));

        Utils.loadSootClasses(null);

        return classNames;
    }
    //endregion

}

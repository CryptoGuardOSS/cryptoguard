package main.analyzer;

import main.rule.base.BaseRuleChecker;
import main.rule.engine.EngineType;
import main.util.Utils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author RigorityJTeam
 * Created on 2019-01-26.
 * @since 02.02.00
 *
 * <p>The class to handle the routing for the different use cases.</p>
 */
public class BaseAnalyzerRouting {

    public static void environmentRouting(EngineType routingType,
                                          String criteriaClass, String criteriaMethod,
                                          int criteriaParam, List<String> snippetPath,
                                          List<String> projectDependency, BaseRuleChecker checker) {
        try {
            if (routingType == EngineType.JAR) {
                setupBaseJar(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0), projectDependency.get(0), checker);
            } else if (routingType == EngineType.APK) {
                setupBaseAPK(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0), checker);
            } else if (routingType == EngineType.DIR) {
                setupBaseDir(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker);
            } else if (routingType == EngineType.JAVAFILES) {
                setupBaseJava(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker);
            } else { //if (routingType == EngineType.JAVACLASSFILES)
                setupBaseJavaClass(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker);
            }
        } catch (IOException e) {

        }
    }

    //region Case Handlers

    //region JAR
    public static void setupBaseJar(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    String projectDependencyPath, BaseRuleChecker checker) throws IOException {

        String java_home = Utils.getJAVA_HOME();

        String basePackageName = Utils.getBasePackageNameFromJar(projectJarPath, true);

        List<String> classNames = Utils.getClassNamesFromJarArchive(projectJarPath);

        for (String dependency : Utils.getJarsInDirectory(projectDependencyPath)) {
            for (String dependencyClazz : Utils.getClassNamesFromJarArchive(dependency)) {
                if (dependencyClazz.contains(basePackageName)) {
                    classNames.add(dependencyClazz);
                }
            }
        }

        String sootClassPath = Utils.buildSootClassPath(projectJarPath,
                java_home + "/jre/lib/rt.jar",
                java_home + "/jre/lib/jce.jar",
                projectDependencyPath);


        Scene.v().setSootClassPath(sootClassPath);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);

    }
    //endregion
    //region APK
    public static void setupBaseAPK(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    BaseRuleChecker checker) throws IOException {

        List<String> classNames = Utils.getClassNamesFromApkArchive(projectJarPath);

        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(Utils.getANDROID() + "/platforms");
        Options.v().set_soot_classpath(Utils.getBaseSOOT());

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);
    }
    //endregion
    //region BaseDir
    public static void setupBaseDir(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    List<String> snippetPath,
                                    List<String> projectDependency,
                                    BaseRuleChecker checker) throws IOException {

        Utils.getJAVA7_HOME();

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        String srcPaths = Utils.join(":", snippetPath);

        String tempSoot = Utils.getBaseSOOT() + ":" + srcPaths + ":" + Utils.buildSootClassPath(projectDependency);

        Scene.v().setSootClassPath(tempSoot);

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);
    }
    //endregion
    //region JavaFiles
    //Like Dir
    public static void setupBaseJava(String criteriaClass,
                                     String criteriaMethod,
                                     int criteriaParam,
                                     List<String> snippetPath,
                                     List<String> projectDependency,
                                     BaseRuleChecker checker) throws IOException {

        Scene.v().setSootClassPath(Utils.getBaseSOOT7() + ":" + Utils.buildSootClassPath(projectDependency));


        List<String> classNames = new ArrayList<>();

        for (String snippit : snippetPath)
            classNames.addAll(Utils.retrieveFullyQualifiedName(Arrays.asList(snippit)));


        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);
    }
    //endregion
    //region JavaClassFiles
    //Like Jar
    public static void setupBaseJavaClass(String criteriaClass,
                                          String criteriaMethod,
                                          int criteriaParam,
                                          List<String> sourceJavaClasses,
                                          List<String> projectDependencyPath,
                                          BaseRuleChecker checker) throws IOException {

        Scene.v().setSootClassPath(Utils.getBaseSOOT() + ":" + Utils.join(":", projectDependencyPath));

        List<String> classNames = new ArrayList<>();
        for (String in : sourceJavaClasses)
            classNames.add(Utils.retrieveFullyQualifiedName(in));

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);

    }

    //endregion

    //endregion
    public static void loadBaseSootInfo(List<String> classNames, String criteriaClass,
                                        String criteriaMethod,
                                        int criteriaParam, BaseRuleChecker checker) {


        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        for (String clazz : BaseAnalyzer.CRITERIA_CLASSES) {
            Scene.v().loadClassAndSupport(clazz);
        }

        for (String clazz : classNames) {
            Scene.v().loadClassAndSupport(clazz);
        }

        Scene.v().loadNecessaryClasses();

        String endPoint = "<" + criteriaClass + ": " + criteriaMethod + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteriaParam);

        BaseAnalyzer.analyzeSliceInternal(criteriaClass, classNames, endPoint, slicingParameters, checker);
    }
}

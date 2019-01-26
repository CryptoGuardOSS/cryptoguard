package main.analyzer;

import main.rule.base.BaseRuleChecker;
import main.rule.engine.EngineType;
import main.util.Utils;
import org.apache.commons.lang3.StringUtils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.ArrayList;
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

    private static final String JAVA7_HOME = System.getenv("JAVA7_HOME");
    private static final String JAVA_HOME = System.getenv("JAVA_HOME");
    private static final String ANDROID_HOME = System.getenv("ANDROID_SDK_HOME");
    private static final String BASE_SOOT = JAVA_HOME + Utils.join(JAVA_HOME, "/jre/lib/rt.jar:", "/jre/lib/jce.jar");


    public static void environmentRouting(EngineType routingType,
                                          String criteriaClass, String criteriaMethod,
                                          int criteriaParam, List<String> snippetPath,
                                          List<String> projectDependency, BaseRuleChecker checker) {
        try {
            if (routingType == EngineType.JAR) {
                setupBaseJar(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0), projectDependency.get(0), checker);
            } else if (routingType == EngineType.APK) {
                setupBaseAPK(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0), checker);
            } else { // if (routingType == EngineType.DIR) {
                setupBaseDir(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker);
            } /*else if (routingType == EngineType.JAVAFILES) {
            setupJavaFileEnv(projectJarPath, projectDependencyPath));
        } else { //if (routingType == EngineType.JAVACLASSFILES)
            setupJavaClassFileEnv(projectJarPath, projectDependencyPath));
        } *///TODO - Route These
        } catch (IOException e) {

        }
    }


    //region System Environment Handlers
    private static void checkJAVA() {
        if (StringUtils.isEmpty(JAVA_HOME)) {
            System.out.println("Please Set JAVA_HOME");
            System.exit(1);
        }
    }

    private static void checkJAVA7() {
        if (StringUtils.isEmpty(JAVA7_HOME)) {
            System.out.println("Please Set JAVA7_HOME");
            System.exit(1);
        }
    }

    private static void checkANDROID() {
        if (StringUtils.isEmpty(ANDROID_HOME)) {
            System.out.println("Please Set ANDROID_HOME");
            System.exit(1);
        }
    }
    //endregion

    //region Case Handlers

    //region JAR
    public static void setupBaseJar(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    String projectDependencyPath, BaseRuleChecker checker) throws IOException {
        checkJAVA();
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
                JAVA_HOME + "/jre/lib/rt.jar",
                JAVA_HOME + "/jre/lib/jce.jar",
                projectDependencyPath);

        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

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
        checkJAVA();
        checkANDROID();

        List<String> classNames = Utils.getClassNamesFromApkArchive(projectJarPath);

        Options.v().set_keep_line_number(true);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(ANDROID_HOME + "/platforms");
        Options.v().set_soot_classpath(BASE_SOOT);

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);

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
        checkJAVA7();

        Options.v().set_allow_phantom_refs(true);
        Options.v().set_keep_line_number(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        String srcPaths = Utils.join(":", snippetPath);

        String sooPathTemp = BASE_SOOT + ":" + srcPaths + ":" + Utils.buildSootClassPath(projectDependency);

        Scene.v().setSootClassPath(BASE_SOOT + ":" + srcPaths + ":" + Utils.buildSootClassPath(projectDependency));

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);
    }
    //endregion

    //region JavaFiles
    /*
    public static void analyzeSlices(Criteria criteria,
                                     List<String> snippetPath,
                                     List<String> projectDependency,
                                     BaseRuleChecker checker) throws IOException {

        String java7Home = System.getenv("JAVA7_HOME");

        if (StringUtils.isBlank(java7Home)) {
            System.err.println("Please set JAVA7_HOME");
            System.exit(1);
        }

        Options.v().set_allow_phantom_refs(true);
        Options.v().set_keep_line_number(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        String split = System.getProperty("file.separator");
        StringBuilder rt = new StringBuilder(java7Home);
        for (String in : new String[]{"jre", "lib", "rt.jar"})
            rt.append(split).append(in);
        //String jce = String.join(split, javaHome, "jre", "lib", "jce.jar");
        StringBuilder jce = new StringBuilder(java7Home);
        for (String in : new String[]{"jre", "lib", "jce.jar"})
            rt.append(split).append(in);

        Scene.v().setSootClassPath(rt.toString() + ":" + jce.toString() + ":" + Utils.buildSootClassPath(projectDependency));


        List<String> classNames = new ArrayList<>();

        for (String snippit : snippetPath)
            classNames.addAll(Utils.retrieveFullyQualifiedName(Arrays.asList(snippit)));

        for (String clazz : BaseAnalyzer.CRITERIA_CLASSES) {
            Scene.v().loadClassAndSupport(clazz);
        }

        for (String clazz : classNames) {
            Scene.v().loadClassAndSupport(clazz);
        }

        Scene.v().loadNecessaryClasses();

        String endPoint = "<" + criteria.getClassName() + ": " + criteria.getMethodName() + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteria.getParam());


        BaseAnalyzer.analyzeSliceInternal(criteria.getClassName(), classNames, endPoint, slicingParameters, checker);

    }
     */
    //endregion

    //region JavaClassFiles

    /**
     * //TODO - Have to fix dependencies
     * public static void analyzeSlices(Criteria criteria,
     * List<String> sourceJavaClasses,
     * List<String> projectDependencyPath,
     * BaseRuleChecker checker) throws IOException {
     * <p>
     * //region Checking For Java_HOME System Env
     * String javaHome = System.getenv("JAVA_HOME");
     * <p>
     * if (javaHome.isEmpty()) {
     * System.err.println("Please set JAVA_HOME");
     * System.exit(1);
     * }
     * //endregion
     * <p>
     * <p>
     * //TODO - Use Utils.getClassNameFromJavaClassFile?
     * <p>
     * //for (String dependency : Utils.getJarsInDirectory(projectDependencyPath)) {
     * //for (String dependencyClazz : Utils.getClassNamesFromJarArchive(dependency)) {
     * //if (dependencyClazz.contains(basePackageName)) {
     * //sourceJavaClasses.add(dependencyClazz);
     * //}
     * //}
     * //}
     * <p>
     * //region Stock Soot Stuff
     * Options.v().set_keep_line_number(true);
     * Options.v().set_allow_phantom_refs(true);
     * <p>
     * String split = System.getProperty("file.separator");
     * //String rt = String.join(split, javaHome, "jre", "lib", "rt.jar");
     * StringBuilder rt = new StringBuilder(javaHome);
     * for (String in : new String[]{"jre", "lib", "rt.jar"})
     * rt.append(split).append(in);
     * //String jce = String.join(split, javaHome, "jre", "lib", "jce.jar");
     * StringBuilder jce = new StringBuilder(javaHome);
     * for (String in : new String[]{"jre", "lib", "jce.jar"})
     * rt.append(split).append(in);
     * <p>
     * String sootPath = Utils.buildSootClassPath(rt.toString(), jce.toString());
     * <p>
     * StringBuilder projectDependency = new StringBuilder();
     * for (String in : projectDependencyPath)
     * projectDependency.append(":").append(in);
     * <p>
     * Scene.v().setSootClassPath(rt + ":" + jce + ":" + projectDependency);//String.join(":", rt, jce, String.join(":",projectDependencyPath)));
     * //endregion
     * <p>
     * String endPoint = "<" + criteria.getClassName() + ": " + criteria.getMethodName() + ">";
     * ArrayList<Integer> slicingParameters = new ArrayList<>();
     * slicingParameters.add(criteria.getParam());
     * <p>
     * //Loading all of the classes
     * //BaseAnalyzer.CRITERIA_CLASSES.forEach(Scene.v()::loadClassAndSupport);
     * for (String className : BaseAnalyzer.CRITERIA_CLASSES) {
     * Scene.v().loadClassAndSupport(className);
     * }
     * <p>
     * //sourceJavaClasses.forEach(Scene.v()::loadClassAndSupport);
     * for (String className : sourceJavaClasses) {
     * Scene.v().loadClassAndSupport(className);
     * }
     * <p>
     * Scene.v().loadNecessaryClasses();
     * //endregion
     * <p>
     * BaseAnalyzer.analyzeSliceInternal(criteria.getClassName(), sourceJavaClasses, endPoint, slicingParameters, checker);
     * <p>
     * }
     */
    //endregion

    //endregion
    public static void loadBaseSootInfo(List<String> classNames, String criteriaClass,
                                        String criteriaMethod,
                                        int criteriaParam, BaseRuleChecker checker) {

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

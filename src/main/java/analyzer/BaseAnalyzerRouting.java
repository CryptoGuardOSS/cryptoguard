package analyzer;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import rule.base.BaseRuleChecker;
import rule.engine.EngineType;
import soot.Scene;
import soot.options.Options;
import util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>BaseAnalyzerRouting class.</p>
 *
 * @author RigorityJTeam
 * Created on 2019-01-26.
 * @version $Id: $Id
 * @since 02.02.00
 *
 * <p>The class to handle the routing for the different use cases.</p>
 */
public class BaseAnalyzerRouting {

    /**
     * <p>environmentRouting.</p>
     *
     * @param routingType       a {@link EngineType} object.
     * @param criteriaClass     a {@link java.lang.String} object.
     * @param criteriaMethod    a {@link java.lang.String} object.
     * @param criteriaParam     a int.
     * @param snippetPath       a {@link java.util.List} object.
     * @param projectDependency a {@link java.util.List} object.
     * @param checker           a {@link BaseRuleChecker} object.
     * @throws ExceptionHandler if any.
     */
    public static void environmentRouting(EngineType routingType,
                                          String criteriaClass, String criteriaMethod,
                                          int criteriaParam, List<String> snippetPath,
                                          List<String> projectDependency, BaseRuleChecker checker) throws ExceptionHandler {

        switch (routingType) {
            case JAR:
                setupBaseJar(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0),
                        projectDependency.size() >= 1
                                ? projectDependency.get(0)
                                : null,
                        checker);
                break;
            case APK:
                setupBaseAPK(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0), checker);
                break;
            case DIR:
                setupBaseDir(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker);
                break;
            case JAVAFILES:
                setupBaseJava(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker);
                break;
            case CLASSFILES:
                setupBaseJavaClass(criteriaClass, criteriaMethod, criteriaParam, snippetPath,
                        projectDependency.size() >= 1
                                ? projectDependency.get(0)
                                : null,
                        checker);
                break;
        }


    }

    //region Case Handlers

    //region JAR

    /**
     * <p>setupBaseJar.</p>
     *
     * @param criteriaClass         a {@link java.lang.String} object.
     * @param criteriaMethod        a {@link java.lang.String} object.
     * @param criteriaParam         a int.
     * @param projectJarPath        a {@link java.lang.String} object.
     * @param projectDependencyPath a {@link java.lang.String} object.
     * @param checker               a {@link BaseRuleChecker} object.
     * @throws ExceptionHandler if any.
     */
    public static void setupBaseJar(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    String projectDependencyPath, BaseRuleChecker checker) throws ExceptionHandler {

        List<String> classNames = Utils.getClassNamesFromJarArchive(projectJarPath);

        if (projectDependencyPath != null)
            for (String dependency : Utils.getJarsInDirectory(projectDependencyPath)) {
                classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));
            }

        List<String> sootPaths = new ArrayList<>();
        sootPaths.add(projectJarPath);
        sootPaths.add(Utils.getBaseSOOT());

        if (projectDependencyPath != null)
            sootPaths.add(projectDependencyPath);

        Scene.v().setSootClassPath(Utils.buildSootClassPath(sootPaths));

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);

    }

    //endregion
    //region APK

    /**
     * <p>setupBaseAPK.</p>
     *
     * @param criteriaClass  a {@link java.lang.String} object.
     * @param criteriaMethod a {@link java.lang.String} object.
     * @param criteriaParam  a int.
     * @param projectJarPath a {@link java.lang.String} object.
     * @param checker        a {@link BaseRuleChecker} object.
     * @throws ExceptionHandler if any.
     */
    public static void setupBaseAPK(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    BaseRuleChecker checker) throws ExceptionHandler {

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

    /**
     * <p>setupBaseDir.</p>
     *
     * @param criteriaClass     a {@link java.lang.String} object.
     * @param criteriaMethod    a {@link java.lang.String} object.
     * @param criteriaParam     a int.
     * @param snippetPath       a {@link java.util.List} object.
     * @param projectDependency a {@link java.util.List} object.
     * @param checker           a {@link BaseRuleChecker} object.
     * @throws ExceptionHandler if any.
     */
    public static void setupBaseDir(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    List<String> snippetPath,
                                    List<String> projectDependency,
                                    BaseRuleChecker checker) throws ExceptionHandler {

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Scene.v().setSootClassPath(Utils.getBaseSOOT() + ":" + Utils.join(":", snippetPath) + ":" + Utils.buildSootClassPath(projectDependency));

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);
    }

    //endregion
    //region JavaFiles
    //Like Dir

    /**
     * <p>setupBaseJava.</p>
     *
     * @param criteriaClass     a {@link java.lang.String} object.
     * @param criteriaMethod    a {@link java.lang.String} object.
     * @param criteriaParam     a int.
     * @param snippetPath       a {@link java.util.List} object.
     * @param projectDependency a {@link java.util.List} object.
     * @param checker           a {@link BaseRuleChecker} object.
     * @throws ExceptionHandler if any.
     */
    public static void setupBaseJava(String criteriaClass,
                                     String criteriaMethod,
                                     int criteriaParam,
                                     List<String> snippetPath,
                                     List<String> projectDependency,
                                     BaseRuleChecker checker) throws ExceptionHandler {

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Scene.v().setSootClassPath(Utils.getBaseSOOT() + ":" + Utils.retrievePackageFromJavaFiles(snippetPath) + ":" + Utils.buildSootClassPath(projectDependency));

        List<String> classNames = Utils.retrieveFullyQualifiedName(snippetPath);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);
    }

    //endregion
    //region JavaClassFiles
    //Like Jar

    /**
     * <p>setupBaseJavaClass.</p>
     *
     * @param criteriaClass         a {@link java.lang.String} object.
     * @param criteriaMethod        a {@link java.lang.String} object.
     * @param criteriaParam         a int.
     * @param sourceJavaClasses     a {@link java.util.List} object.
     * @param projectDependencyPath a {@link java.util.List} object.
     * @param checker               a {@link BaseRuleChecker} object.
     * @throws ExceptionHandler if any.
     */
    public static void setupBaseJavaClass(String criteriaClass,
                                          String criteriaMethod,
                                          int criteriaParam,
                                          List<String> sourceJavaClasses,
                                          String projectDependencyPath,
                                          BaseRuleChecker checker) throws ExceptionHandler {

        Options.v().set_src_prec(Options.src_prec_only_class);
        Options.v().set_output_format(Options.output_format_jimple);

        Options.v().set_prepend_classpath(true);
        Options.v().set_whole_program(true);

        List<String> classNames = Utils.retrieveFullyQualifiedName(sourceJavaClasses);

        if (projectDependencyPath != null)
            for (String dependency : Utils.getJarsInDirectory(projectDependencyPath)) {
                classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));
            }

        String tempTest = Utils.retrievePackageFromJavaFiles(sourceJavaClasses);
        String tempPath = tempTest.substring(0, tempTest.lastIndexOf(System.getProperty("file.separator")));

        String temp = Utils.join(":", Utils.getBaseSOOT(), tempPath, projectDependencyPath);
        //String temp = Utils.join(":",Utils.getBaseSOOT(),Utils.join(":", sourceJavaClasses),projectDependencyPath);
        Scene.v().setSootClassPath(temp);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);

    }

    //endregion

    //endregion

    /**
     * <p>loadBaseSootInfo.</p>
     *
     * @param classNames     a {@link java.util.List} object.
     * @param criteriaClass  a {@link java.lang.String} object.
     * @param criteriaMethod a {@link java.lang.String} object.
     * @param criteriaParam  a int.
     * @param checker        a {@link BaseRuleChecker} object.
     * @throws ExceptionHandler if any.
     */
    public static void loadBaseSootInfo(List<String> classNames, String criteriaClass,
                                        String criteriaMethod,
                                        int criteriaParam, BaseRuleChecker checker) throws ExceptionHandler {


        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        for (String clazz : BaseAnalyzer.CRITERIA_CLASSES) {
            try {
                Scene.v().loadClassAndSupport(clazz);
            } catch (Error e) {
                throw new ExceptionHandler("Error loading Class: " + clazz, ExceptionId.LOADING);
            }
        }

        for (String clazz : classNames) {
            try {
                Scene.v().loadClassAndSupport(clazz);
            } catch (Error e) {
                throw new ExceptionHandler("Error loading class: " + clazz, ExceptionId.LOADING);
            }

        }

        Scene.v().loadNecessaryClasses();

        String endPoint = "<" + criteriaClass + ": " + criteriaMethod + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteriaParam);

        BaseAnalyzer.analyzeSliceInternal(criteriaClass, classNames, endPoint, slicingParameters, checker);
    }
}

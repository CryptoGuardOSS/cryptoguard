package analyzer;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import lombok.extern.log4j.Log4j2;
import rule.base.BaseRuleChecker;
import rule.engine.EngineType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;
import util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>BaseAnalyzerRouting class.</p>
 *
 * @author CryptoguardTeam
 * Created on 2019-01-26.
 * @version 03.07.01
 * @since 02.02.00
 *
 * <p>The class to handle the routing for the different use cases.</p>
 */
@Log4j2
public class BaseAnalyzerRouting {

    /**
     * <p>environmentRouting.</p>
     *
     * @param routingType       a {@link rule.engine.EngineType} object.
     * @param criteriaClass     a {@link java.lang.String} object.
     * @param criteriaMethod    a {@link java.lang.String} object.
     * @param criteriaParam     a int.
     * @param snippetPath       a {@link java.util.List} object.
     * @param projectDependency a {@link java.util.List} object.
     * @param checker           a {@link rule.base.BaseRuleChecker} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
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
     * @param checker               a {@link rule.base.BaseRuleChecker} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseJar(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    String projectDependencyPath, BaseRuleChecker checker) throws ExceptionHandler {

        List<String> classNames = Utils.getClassNamesFromJarArchive(projectJarPath);

        for (String dependency : Utils.getJarsInDirectory(projectDependencyPath))
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));

        Scene.v().setSootClassPath(Utils.join(":",
                projectJarPath,
                Utils.getBaseSOOT(),
                Utils.join(":", Utils.getJarsInDirectory(projectDependencyPath)))
        );

        String sootClassPath = Scene.v().getSootClassPath();

            /*
            * Class Names
                0 = "tester.Crypto$2"
                1 = "tester.SymCrypto"
                2 = "tester.Crypto$3"
                3 = "tester.UrlFrameWorks"
                4 = "tester.PasswordUtils"
                5 = "tester.LiveVarsClass"
                6 = "tester.NewTestCase1"
                7 = "tester.PBEUsage"
                8 = "tester.NewTestCase2"
                9 = "tester.Crypto$1"
                10 = "tester.PassEncryptor"
                11 = "tester.Crypto"
                12 = "tester.VeryBusyClass"
            * Scene.v.classpath :
                ./samples/testable-jar/build/libs/testable-jar.jar
                :~/8/rt.jar
                :~/8/jce.jar
                :./samples/testable-jar/build/dependencies/converter-gson-2.1.0.jar
                :./samples/testable-jar/build/dependencies/okio-1.13.0.jar
                :./samples/testable-jar/build/dependencies/retrofit-2.1.0.jar
                :./samples/testable-jar/build/dependencies/gson-2.7.jar
                :./samples/testable-jar/build/dependencies/okhttp-3.9.0.jar
            * */

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
     * @param checker        a {@link rule.base.BaseRuleChecker} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
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
     * @param checker           a {@link rule.base.BaseRuleChecker} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseDir(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    List<String> snippetPath,
                                    List<String> projectDependency,
                                    BaseRuleChecker checker) throws ExceptionHandler {
        //region Old Attempt

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Scene.v().setSootClassPath(Utils.getBaseSOOT() + ":"
                + Utils.join(":", snippetPath)
                + ":" + Utils.buildSootClassPath(projectDependency));

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);

        //endregion
        //region New Attempt
        /*
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Options.v().set_whole_program(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_app(true);
        Options.v().set_process_dir(snippetPath);


        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        Scene.v().setSootClassPath(Utils.getBaseSOOT() + ":"
                + Utils.join(":", snippetPath)
                + ":" + Utils.buildSootClassPath(projectDependency));
        /*
        for (String clazz : Utils.retrieveJavaFilesFromDir(snippetPath.get(0))) {
            log.debug("Adding basic class: " + clazz);
            //SootClass clazs = new SootClass(clazz);
            //Scene.v().addClass(clazs);
            //Scene.v().extendSootClassPath(clazz);
            //Scene.v().loadClassAndSupport(clazz);
        }

        //Doesn't break it but doesn't display errors
        for (String dependency : Utils.getJarsInDirectories(projectDependency)) {
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));
        }



        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);
        */
        //endregion
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
     * @param checker           a {@link rule.base.BaseRuleChecker} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseJava(String criteriaClass,
                                     String criteriaMethod,
                                     int criteriaParam,
                                     List<String> snippetPath,
                                     List<String> projectDependency,
                                     BaseRuleChecker checker) throws ExceptionHandler {

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Options.v().set_prepend_classpath(true);
        Options.v().set_whole_program(true);

        Options.v().set_app(true);

        Scene.v().setSootClassPath(Utils.join(":",
                Utils.getBaseSOOT(),
                Utils.join(":", snippetPath),
                Utils.buildSootClassPath(projectDependency)));

        List<String> classNames = Utils.retrieveFullyQualifiedName(snippetPath);

        for (String dependency : Utils.getJarsInDirectories(projectDependency)) {
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));
        }

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
     * @param checker               a {@link rule.base.BaseRuleChecker} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseJavaClass(String criteriaClass,
                                          String criteriaMethod,
                                          int criteriaParam,
                                          List<String> sourceJavaClasses,
                                          String projectDependencyPath,
                                          BaseRuleChecker checker) throws ExceptionHandler {

        if (sourceJavaClasses.get(0).contains("very.class"))
            very(criteriaClass, criteriaMethod, criteriaParam, sourceJavaClasses, projectDependencyPath, checker);
        else
            pbeUsage(criteriaClass, criteriaMethod, criteriaParam, sourceJavaClasses, projectDependencyPath, checker);

    }

    //region VERY
    public static void very(String criteriaClass,
                            String criteriaMethod,
                            int criteriaParam,
                            List<String> sourceJavaClasses,
                            String projectDependencyPath,
                            BaseRuleChecker checker) throws ExceptionHandler {

        Options.v().set_src_prec(Options.src_prec_only_class);
        Options.v().set_output_format(Options.output_format_jimple);

        Options.v().set_verbose(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);

        List<String> classNames = Utils.retrieveFullyQualifiedName(sourceJavaClasses);

        Scene.v().setSootClassPath(Utils.surround(":",
                Utils.retrieveBaseDirectory(sourceJavaClasses),
                Utils.getBaseSOOT(),
                Utils.join(":", Utils.getJarsInDirectory(projectDependencyPath)),
                Utils.join(":", sourceJavaClasses)));
        log.debug("Setting the soot class path as: " + Scene.v().getSootClassPath());

        String suck = Scene.v().getSootClassPath();

        for (String clazz : classNames) {
            log.debug("Working with the full class path: " + clazz);
            Options.v().classes().add(clazz);
        }

        for (String dependency : Utils.getJarsInDirectory(projectDependencyPath))
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker);

    }

    //endregion
    //region PBE
    public static void pbeUsage(String criteriaClass,
                                String criteriaMethod,
                                int criteriaParam,
                                List<String> sourceJavaClasses,
                                String projectDependencyPath,
                                BaseRuleChecker checker) throws ExceptionHandler {

        Options.v().set_src_prec(Options.src_prec_only_class);
        Options.v().set_output_format(Options.output_format_jimple);

        Options.v().set_verbose(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);

        projectDependencyPath = "";

        List<String> classNames = Utils.retrieveFullyQualifiedName(sourceJavaClasses);

        Scene.v().setSootClassPath(Utils.surround(":",
                Utils.joinSpecialSootClassPath(sourceJavaClasses),
                Utils.getBaseSOOT(),
                Utils.join(":", Utils.getJarsInDirectory(projectDependencyPath))
        ));
        //region Old Attempt
        /*
        Scene.v().setSootClassPath(Utils.surround(":",
                Utils.retrieveBaseDirectory(sourceJavaClasses),
                Utils.getBaseSOOT(),
                Utils.join(":", Utils.getJarsInDirectory(projectDependencyPath)),
                Utils.join(":", sourceJavaClasses)));
        * */
        //endregion
        log.debug("Setting the soot class path as: " + Scene.v().getSootClassPath());

        //region Old Attempt
        for (String clazz : classNames) {
            log.debug("Working with the full class path: " + clazz);
            Options.v().classes().add(clazz);
        }
        //endregion

        for (String dependency : Utils.getJarsInDirectory(projectDependencyPath))
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));

        loadBaseSootInfo_Class(classNames, classNames.get(0), criteriaClass, criteriaMethod, criteriaParam, checker);

    }

    //endregion

    //endregion

    public static void loadBaseSootInfo_Class(List<String> classNames, String mainClass, String criteriaClass,
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

        //TODO Remove - Very Temp Step
        SootClass main = null;

        for (String clazz : classNames) {
            try {
                if ((main = Scene.v().loadClassAndSupport(clazz)).isPhantom())
                    throw new ExceptionHandler("Class " + clazz + " is not properly loaded", ExceptionId.LOADING);
            } catch (Error e) {
                throw new ExceptionHandler("Error loading class: " + clazz, ExceptionId.LOADING);
            }
        }
        if (!Scene.v().getMainClass().equals(main))
            throw new ExceptionHandler("Class " + main.getJavaStyleName() + " is not properly loaded", ExceptionId.LOADING);

        Scene.v().loadNecessaryClasses();
        Scene.v().setDoneResolving();
        Options.v().set_prepend_classpath(true);
        Options.v().set_no_bodies_for_excluded(true);


        //SootClass klass = Scene.v().getMainClass();

        String endPoint = "<" + criteriaClass + ": " + criteriaMethod + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteriaParam);

        BaseAnalyzer.analyzeSliceInternal(criteriaClass, classNames, endPoint, slicingParameters, checker);
    }

    /**
     * <p>loadBaseSootInfo.</p>
     *
     * @param classNames     a {@link java.util.List} object.
     * @param criteriaClass  a {@link java.lang.String} object.
     * @param criteriaMethod a {@link java.lang.String} object.
     * @param criteriaParam  a int.
     * @param checker        a {@link rule.base.BaseRuleChecker} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
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

        Set<String> newralph = Scene.v().nameToClass.keySet().stream().filter(classNames::contains).collect(Collectors.toSet());

        for (String clazz : classNames) {
            try {
                Scene.v().loadClassAndSupport(clazz);
            } catch (Error e) {
                throw new ExceptionHandler("Error loading class: " + clazz, ExceptionId.LOADING);
            }
        }

        Set<String> ralph = Scene.v().nameToClass.keySet().stream().filter(classNames::contains).collect(Collectors.toSet());
        for (String in : ralph)
            if (classNames.contains(in)) {
                RefType base = Scene.v().nameToClass.get(in);
                if (base.getSootClass().isPhantom())
                    throw new ExceptionHandler("Loaded Class is not fully loaded.", ExceptionId.UNKWN);
            }

        Scene.v().loadNecessaryClasses();
        Scene.v().setDoneResolving();

        String endPoint = "<" + criteriaClass + ": " + criteriaMethod + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteriaParam);

        BaseAnalyzer.analyzeSliceInternal(criteriaClass, classNames, endPoint, slicingParameters, checker);
    }
}

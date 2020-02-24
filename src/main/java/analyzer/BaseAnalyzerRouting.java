package analyzer;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import rule.base.BaseRuleChecker;
import rule.engine.EngineType;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;
import util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
     * @param mainKlass         a {@link java.lang.String} object.
     * @param androidHome       a {@link java.lang.String} object.
     * @param javaHome          a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void environmentRouting(EngineType routingType,
                                          String criteriaClass, String criteriaMethod,
                                          int criteriaParam, List<String> snippetPath,
                                          List<String> projectDependency, BaseRuleChecker checker, String mainKlass,
                                          String androidHome, String javaHome) throws ExceptionHandler {

        switch (routingType) {
            case JAR:
                setupBaseJar(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0),
                        projectDependency.size() >= 1
                                ? projectDependency.get(0)
                                : null,
                        checker, mainKlass, javaHome);
                break;
            case APK:
                setupBaseAPK(criteriaClass, criteriaMethod, criteriaParam, snippetPath.get(0), checker, mainKlass, androidHome, javaHome);
                break;
            case DIR:
                setupBaseDir(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker, mainKlass, javaHome);
                break;
            case JAVAFILES:
                setupBaseJava(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker, mainKlass, javaHome);
                break;
            case CLASSFILES:
                setupBaseJavaClass(criteriaClass, criteriaMethod, criteriaParam, snippetPath, projectDependency, checker, mainKlass, javaHome);
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
     * @param mainKlass             a {@link java.lang.String} object.
     * @param javaHome              a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseJar(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    String projectDependencyPath, BaseRuleChecker checker,
                                    String mainKlass, String javaHome) throws ExceptionHandler {

        List<String> classNames = Utils.getClassNamesFromJarArchive(projectJarPath);

        for (String dependency : Utils.getJarsInDirectory(projectDependencyPath))
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));

        Scene.v().setSootClassPath(Utils.join(":",
                projectJarPath,
                Utils.getBaseSoot(javaHome),
                Utils.join(":", Utils.getJarsInDirectory(projectDependencyPath)))
        );
        log.debug("Setting the soot class path as: " + Scene.v().getSootClassPath());

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker, "_JAR_");
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
     * @param mainKlass      a {@link java.lang.String} object.
     * @param androidHome    a {@link java.lang.String} object.
     * @param javaHome       a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseAPK(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    String projectJarPath,
                                    BaseRuleChecker checker, String mainKlass,
                                    String androidHome, String javaHome) throws ExceptionHandler {

        List<String> classNames = Utils.getClassNamesFromApkArchive(projectJarPath);

        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(Utils.osPathJoin(androidHome, "platforms"));
        Options.v().set_soot_classpath(Utils.getBaseSoot(javaHome));

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker, "_APK_");
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
     * @param mainKlass         a {@link java.lang.String} object.
     * @param javaHome          a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseDir(String criteriaClass,
                                    String criteriaMethod,
                                    int criteriaParam,
                                    List<String> snippetPath,
                                    List<String> projectDependency,
                                    BaseRuleChecker checker, String mainKlass,
                                    String javaHome) throws ExceptionHandler {

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        Scene.v().setSootClassPath(Utils.getBaseSoot(javaHome) + ":"
                + Utils.join(":", snippetPath)
                + ":" + Utils.buildSootClassPath(projectDependency));

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker, "_DIR_");

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
     * @param mainKlass         a {@link java.lang.String} object.
     * @param javaHome          a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseJava(String criteriaClass,
                                     String criteriaMethod,
                                     int criteriaParam,
                                     List<String> snippetPath,
                                     List<String> projectDependency,
                                     BaseRuleChecker checker, String mainKlass,
                                     String javaHome) throws ExceptionHandler {

        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_output_format(Options.output_format_jimple);

        Options.v().set_verbose(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);

        List<String> classNames = Utils.retrieveFullyQualifiedName(snippetPath);

        Scene.v().setSootClassPath(Utils.surround(":",
                Utils.getBaseSoot(javaHome),
                Utils.joinSpecialSootClassPath(snippetPath),
                Utils.buildSootClassPath(projectDependency))
        );
        log.debug("Setting the soot class path as: " + Scene.v().getSootClassPath());


        for (String dependency : Utils.getJarsInDirectories(projectDependency)) {
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));
        }

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker, mainKlass);
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
     * @param mainKlass             a {@link java.lang.String} object.
     * @param javaHome              a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void setupBaseJavaClass(String criteriaClass,
                                          String criteriaMethod,
                                          int criteriaParam,
                                          List<String> sourceJavaClasses,
                                          List<String> projectDependencyPath,
                                          BaseRuleChecker checker, String mainKlass,
                                          String javaHome) throws ExceptionHandler {

        Options.v().set_src_prec(Options.src_prec_only_class);
        Options.v().set_output_format(Options.output_format_jimple);

        Options.v().set_verbose(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);

        List<String> classNames = Utils.retrieveFullyQualifiedName(sourceJavaClasses);

        Scene.v().setSootClassPath(Utils.surround(":",
                Utils.joinSpecialSootClassPath(sourceJavaClasses),
                Utils.getBaseSoot(javaHome),
                Utils.join(":", Utils.getJarsInDirectories(projectDependencyPath))
        ));
        log.debug("Setting the soot class path as: " + Scene.v().getSootClassPath());

        for (String clazz : classNames) {
            log.debug("Working with the full class path: " + clazz);
            Options.v().classes().add(clazz);
        }

        for (String dependency : Utils.getJarsInDirectories(projectDependencyPath))
            classNames.addAll(Utils.getClassNamesFromJarArchive(dependency));

        loadBaseSootInfo(classNames, criteriaClass, criteriaMethod, criteriaParam, checker, mainKlass);

    }
    //endregion

    /**
     * <p>loadBaseSootInfo.</p>
     *
     * @param classNames     a {@link java.util.List} object.
     * @param criteriaClass  a {@link java.lang.String} object.
     * @param criteriaMethod a {@link java.lang.String} object.
     * @param criteriaParam  a int.
     * @param checker        a {@link rule.base.BaseRuleChecker} object.
     * @param mainKlass      a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void loadBaseSootInfo(List<String> classNames, String criteriaClass,
                                        String criteriaMethod,
                                        int criteriaParam, BaseRuleChecker checker, String mainKlass) throws ExceptionHandler {

        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);
        List<String> ignoreLibs = Arrays.asList("okhttp3.Request$Builder", "retrofit2.Retrofit$Builder");
        for (String clazz : BaseAnalyzer.CRITERIA_CLASSES) {
            log.debug("Loading with the class: " + clazz);
            try {
                SootClass runningClass;
                if ((runningClass = Scene.v().loadClassAndSupport(clazz)).isPhantom() && !ignoreLibs.contains(runningClass.getName())) {
                    log.fatal("Class: " + clazz + " is not properly loaded");
                    throw new ExceptionHandler("Class: " + clazz + " is not properly loaded", ExceptionId.LOADING);
                }
            } catch (ExceptionHandler e) {
                throw e;
            } catch (Error | Exception e) {
                log.fatal("Error loading Class: " + clazz);
                throw new ExceptionHandler("Error loading Class: " + clazz, ExceptionId.LOADING);
            }
        }

        Boolean mainMethodFound = false;
        boolean avoidMainKlass = StringUtils.isNotEmpty(mainKlass) && !mainKlass.equals("_JAR_") && !mainKlass.equals("_APK_") && !mainKlass.equals("_DIR_");

        for (String clazz : classNames) {
            log.debug("Working with the internal class path: " + clazz);
            try {
                SootClass runningClass = Scene.v().loadClassAndSupport(clazz);
                if (runningClass.isPhantom()) {
                    log.fatal("Class: " + clazz + " is not properly loaded");
                    throw new ExceptionHandler("Class " + clazz + " is not properly loaded", ExceptionId.LOADING);
                }

                boolean containsMain = runningClass.getMethods().stream().anyMatch(m -> m.getName().equals("main"));
                if (!mainMethodFound)
                    mainMethodFound = containsMain;
                else if (avoidMainKlass && containsMain && StringUtils.isEmpty(mainKlass)) {
                    log.fatal("Multiple Entry-points (main) found within the files included.");
                    throw new ExceptionHandler("Multiple Entry-points (main) found within the files included.", ExceptionId.FILE_READ);
                }

            } catch (ExceptionHandler e) {
                throw e;
            } catch (Error | Exception e) {
                log.fatal("Error loading class " + clazz);
                throw new ExceptionHandler("Error loading class " + clazz, ExceptionId.LOADING);
            }
        }

        Scene.v().loadNecessaryClasses();
        Scene.v().setDoneResolving();
        Options.v().set_prepend_classpath(true);
        Options.v().set_no_bodies_for_excluded(true);

        if ((StringUtils.isNotEmpty(mainKlass) && avoidMainKlass) && (!Scene.v().hasMainClass() || classNames.stream().noneMatch(str -> str.equals(Scene.v().getMainClass().getName())))) {
            log.fatal("Could not detected an entry-point (main method) within any of the files provided.");
            throw new ExceptionHandler("Could not detected an entry-point (main method) within any of the files provided.", ExceptionId.FILE_READ);
        }

        if (StringUtils.isNotEmpty(mainKlass) && avoidMainKlass && !Scene.v().getMainClass().getName().equals(mainKlass)) {
            SootClass mainClass = null;
            try {
                mainClass = Scene.v().getSootClass(Utils.retrieveFullyQualifiedName(mainKlass));
            } catch (RuntimeException e) {
                log.fatal("The class " + mainKlass + " was not loaded correctly.");
                throw new ExceptionHandler("The class " + mainKlass + " was not loaded correctly.", ExceptionId.LOADING);
            }
            try {
                Scene.v().setMainClass(mainClass);
            } catch (RuntimeException e) {
                log.fatal("The class " + mainKlass + " does not have a main method.");
                throw new ExceptionHandler("The class " + mainKlass + " does not have a main method.", ExceptionId.LOADING);
            }
        }

        String endPoint = "<" + criteriaClass + ": " + criteriaMethod + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteriaParam);

        BaseAnalyzer.analyzeSliceInternal(criteriaClass, classNames, endPoint, slicingParameters, checker);
    }
}

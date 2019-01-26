package main.analyzer;

import main.rule.base.BaseRuleChecker;
import main.rule.engine.Criteria;
import main.util.Utils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RigorityJTeam
 * Created on 2019-01-18.
 * @since 01.01.12
 *
 * <p>The transformation of the Java Source File Criteria.</p>
 */
public class JavaClassFileAnalyzer {


    //TODO - Have to fix dependencies
    public static void analyzeSlices(Criteria criteria,
                                     List<String> sourceJavaClasses,
                                     List<String> projectDependencyPath,
                                     BaseRuleChecker checker) throws IOException {

        //region Checking For Java_HOME System Env
        String javaHome = System.getenv("JAVA_HOME");

        if (javaHome.isEmpty()) {
            System.err.println("Please set JAVA_HOME");
            System.exit(1);
        }
        //endregion


        //TODO - Use Utils.getClassNameFromJavaClassFile?

        /*for (String dependency : Utils.getJarsInDirectory(projectDependencyPath)) {
            for (String dependencyClazz : Utils.getClassNamesFromJarArchive(dependency)) {
                if (dependencyClazz.contains(basePackageName)) {
                    sourceJavaClasses.add(dependencyClazz);
                }
            }
        }*/

        //region Stock Soot Stuff
        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        String split = System.getProperty("file.separator");
        //String rt = String.join(split, javaHome, "jre", "lib", "rt.jar");
        StringBuilder rt = new StringBuilder(javaHome);
        for (String in : new String[]{"jre", "lib", "rt.jar"})
            rt.append(split).append(in);
        //String jce = String.join(split, javaHome, "jre", "lib", "jce.jar");
        StringBuilder jce = new StringBuilder(javaHome);
        for (String in : new String[]{"jre", "lib", "jce.jar"})
            rt.append(split).append(in);

        String sootPath = Utils.buildSootClassPath(rt.toString(), jce.toString());

        StringBuilder projectDependency = new StringBuilder();
        for (String in : projectDependencyPath)
            projectDependency.append(":").append(in);

        Scene.v().setSootClassPath(rt + ":" + jce + ":" + projectDependency);//String.join(":", rt, jce, String.join(":",projectDependencyPath)));
        //endregion

        String endPoint = "<" + criteria.getClassName() + ": " + criteria.getMethodName() + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteria.getParam());

        //Loading all of the classes
        //BaseAnalyzer.CRITERIA_CLASSES.forEach(Scene.v()::loadClassAndSupport);
        for (String className : BaseAnalyzer.CRITERIA_CLASSES) {
            Scene.v().loadClassAndSupport(className);
        }

        //sourceJavaClasses.forEach(Scene.v()::loadClassAndSupport);
        for (String className : sourceJavaClasses) {
            Scene.v().loadClassAndSupport(className);
        }

        Scene.v().loadNecessaryClasses();
        //endregion

        BaseAnalyzer.analyzeSliceInternal(criteria.getClassName(), sourceJavaClasses, endPoint, slicingParameters, checker);

    }
}

package main.analyzer;

import main.rule.base.BaseRuleChecker;
import main.util.Utils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApkAnalyzer {

    public static void analyzeSlices(String criteriaClass,
                                     String criteriaMethod,
                                     int criteriaParam,
                                     String projectJarPath, BaseRuleChecker checker) throws IOException {

        String javaHome = System.getenv("JAVA_HOME");
        String androidHome = System.getenv("ANDROID_SDK_HOME");

        if (javaHome == null) {

            System.err.println("Please set JAVA_HOME");
            System.exit(1);
        }

        if (androidHome == null) {

            System.err.println("Please set ANDROID_SDK_HOME");
            System.exit(1);
        }

        List<String> classNames = Utils.getClassNamesFromApkArchive(projectJarPath);

        Options.v().set_keep_line_number(true);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(androidHome + "/platforms");
        Options.v().set_soot_classpath(javaHome + "/jre/lib/rt.jar:" + javaHome + "/jre/lib/jce.jar");

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);
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

package main.rule;

import main.analyzer.backward.UnitContainer;
import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;
import main.slicer.backward.other.OtherInfluencingInstructions;
import main.util.FieldInitializationInstructionMap;
import main.util.NamedMethodMap;
import main.util.Utils;
import soot.*;
import soot.jimple.Constant;
import soot.options.Options;

import java.io.IOException;
import java.util.*;

import static main.util.Utils.getClassNamesFromApkArchive;

public class HostNameVerifierFinder implements RuleChecker {

    private static final String HOST_NAME_VERIFIER = "HostnameVerifier";
    private static final String METHOD_TO_SLICE = "boolean verify(java.lang.String,javax.net.ssl.SSLSession)";
    private static final String SLICING_INSTRUCTION = "return";

    @Override
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath) throws IOException {

        Map<String, List<UnitContainer>> analysisLists;
        if (type == EngineType.JAR) {
            analysisLists = analyzeJar(projectJarPath.get(0), projectDependencyPath.get(0));
        } else if (type == EngineType.APK) {
            analysisLists = analyzeApk(projectJarPath.get(0));
        } else {
            analysisLists = analyzeSnippet(projectJarPath, projectDependencyPath);
        }

        for (String className : analysisLists.keySet()) {
            List<UnitContainer> analysis = analysisLists.get(className);
            if (!analysis.isEmpty()) {

                List<Value> constants = new ArrayList<>();

                boolean usedFirstParam = false;
                boolean usedSecondParam = false;

                for (UnitContainer unit : analysis) {

                    if (unit.toString().contains("@parameter1: javax.net.ssl.SSLSession")) {
                        usedSecondParam = true;
                    }

                    for (ValueBox usebox : unit.getUnit().getUseBoxes()) {
                        if (usebox.getValue() instanceof Constant) {
                            constants.add(usebox.getValue());
                        }
                    }
                }

                if (!usedSecondParam) {
                    System.out.println("=======================================");
                    String output = "***Violated Rule 6: Uses untrusted HostNameVerifier";
                    if (!constants.isEmpty()) {
                        output += "\n***Cause: Fixed " + constants.toString() + " used in " + className;
                    }
                    System.out.println(output);
                    System.out.println("=======================================");
                }
            }
        }

    }

    private Map<String, List<UnitContainer>> analyzeJar(String projectJarPath, String projectDependencyPath) throws IOException {
        String javaHome = System.getenv("JAVA_HOME");

        if (javaHome.isEmpty()) {

            System.err.println("Please set JAVA_HOME");
            System.exit(1);
        }

        String sootClassPath = Utils.buildSootClassPath(projectJarPath,
                javaHome + "/jre/lib/rt.jar",
                javaHome + "/jre/lib/jce.jar",
                projectDependencyPath);
        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        Scene.v().setSootClassPath(sootClassPath);

        Scene.v().loadBasicClasses();

        List<String> classNames = Utils.getClassNamesFromJarArchive(projectJarPath);
        return getHostNameVerifiers(classNames);
    }

    private Map<String, List<UnitContainer>> analyzeApk(String projectJarPath) throws IOException {
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

        Options.v().set_keep_line_number(true);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(androidHome + "/platforms");
        Options.v().set_soot_classpath(javaHome + "/jre/lib/rt.jar:" + javaHome + "/jre/lib/jce.jar");

        Options.v().set_process_dir(Collections.singletonList(projectJarPath));
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);

        Scene.v().loadBasicClasses();

        List<String> classNames = getClassNamesFromApkArchive(projectJarPath);
        return getHostNameVerifiers(classNames);
    }

    private Map<String, List<UnitContainer>> analyzeSnippet(List<String> snippetPath, List<String> projectDependencyPath) throws IOException {

        String javaHome = System.getenv("JAVA7_HOME");

        if (javaHome.isEmpty()) {

            System.err.println("Please set JAVA7_HOME");
            System.exit(1);
        }

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        StringBuilder srcPaths = new StringBuilder();

        for (String srcDir : snippetPath) {
            srcPaths.append(srcDir)
                    .append(":");
        }

        Options.v().set_soot_classpath(javaHome + "/jre/lib/rt.jar:"
                + javaHome + "/jre/lib/jce.jar:" + srcPaths.toString() + Utils.buildSootClassPath(projectDependencyPath));

        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        for (String className : classNames) {
            Options.v().classes().add(className);
        }

        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        Scene.v().loadBasicClasses();

        return getHostNameVerifiers(classNames);
    }

    private static Map<String, List<UnitContainer>> getHostNameVerifiers(List<String> classNames) {

        Map<String, List<UnitContainer>> analysisList = new HashMap<>();

        NamedMethodMap.build(classNames);
        FieldInitializationInstructionMap.build(classNames);

        for (String className : classNames) {
            SootClass sClass = Scene.v().loadClassAndSupport(className);

            if (sClass.getInterfaces().toString().contains(HOST_NAME_VERIFIER)) {

                List<SootMethod> methodList = sClass.getMethods();

                for (SootMethod method : methodList) {
                    if (method.toString().contains(METHOD_TO_SLICE) && method.isConcrete()) {
                        OtherInfluencingInstructions returnInfluencingInstructions = new OtherInfluencingInstructions(method,
                                SLICING_INSTRUCTION);
                        List<UnitContainer> analysis = returnInfluencingInstructions.getAnalysisResult().getAnalysis();
                        analysisList.put(className, analysis);
                    }
                }
            }
        }

        return analysisList;
    }
}

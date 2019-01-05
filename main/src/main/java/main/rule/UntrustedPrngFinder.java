package main.rule;

import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;
import main.util.Utils;
import soot.*;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.io.IOException;
import java.util.*;

import static main.util.Utils.getClassNamesFromApkArchive;

public class UntrustedPrngFinder implements RuleChecker {

    private static final List<String> UNTRUSTED_PRNGS = new ArrayList<>();

    static {
        UNTRUSTED_PRNGS.add("java.util.Random: void <init>");
        UNTRUSTED_PRNGS.add("java.lang.Math: double random");
    }

    @Override
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath) throws IOException {

        Map<String, List<Unit>> analysisLists;

        if (type == EngineType.JAR) {
            analysisLists = analyzeJar(projectJarPath.get(0), projectDependencyPath.get(0));
        } else if (type == EngineType.APK) {
            analysisLists = analyzeApk(projectJarPath.get(0));
        } else {
            analysisLists = analyzeSnippet(projectJarPath, projectDependencyPath);
        }

        if (!analysisLists.isEmpty()) {
            for (String method : analysisLists.keySet()) {
                List<Unit> analysis = analysisLists.get(method);

                if (!analysis.isEmpty()) {
                    System.out.println("=============================================");
                    String output = "***Violated Rule 13: Untrused PRNG (java.util.Random) Found in " + method;
                    System.out.println(output);
                    System.out.println("=============================================");
                }
            }
        }

    }

    private Map<String, List<Unit>> analyzeJar(String projectJarPath, String projectDependencyPath) throws IOException {
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
        return getUntrustedPrngInstructions(classNames);
    }

    private Map<String, List<Unit>> analyzeApk(String projectJarPath) throws IOException {
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
        return getUntrustedPrngInstructions(classNames);
    }

    private Map<String, List<Unit>> analyzeSnippet(List<String> snippetPath, List<String> projectDependencyPath) throws IOException {

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

        return getUntrustedPrngInstructions(classNames);
    }

    private static Map<String, List<Unit>> getUntrustedPrngInstructions(List<String> classNames) {

        Map<String, List<Unit>> analysisList = new HashMap<>();

        for (String className : classNames) {
            SootClass sClass = Scene.v().loadClassAndSupport(className);

            for (SootMethod method : sClass.getMethods()) {
                if (method.isConcrete()) {

                    List<Unit> analysis = new ArrayList<>();

                    Body b = method.retrieveActiveBody();
                    DirectedGraph g = new ExceptionalUnitGraph(b);
                    Iterator gitr = g.iterator();
                    while (gitr.hasNext()) {
                        Unit unit = (Unit) gitr.next();

                        for (String prng : UNTRUSTED_PRNGS) {
                            if (unit.toString().contains(prng)) {
                                analysis.add(unit);
                            }
                        }

                    }

                    analysisList.put(method.toString(), analysis);

                }
            }
        }

        return analysisList;
    }
}

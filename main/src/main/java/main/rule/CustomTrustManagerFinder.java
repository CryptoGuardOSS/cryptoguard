package main.rule;

import main.analyzer.backward.UnitContainer;
import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;
import main.slicer.backward.other.OtherAnalysisResult;
import main.slicer.backward.other.OtherInfluencingInstructions;
import main.util.FieldInitializationInstructionMap;
import main.util.NamedMethodMap;
import main.util.Utils;
import soot.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.io.IOException;
import java.util.*;

import static main.util.Utils.getClassNamesFromApkArchive;

public class CustomTrustManagerFinder implements RuleChecker {

    private static final String TRUST_MANAGER = "TrustManager";
    private static final Map<String, String> METHOD_VS_SLICING_CRITERIA = new HashMap<>();

    static {

        METHOD_VS_SLICING_CRITERIA.put("void checkClientTrusted(java.security.cert.X509Certificate[],java.lang.String)", "throw");
        METHOD_VS_SLICING_CRITERIA.put("void checkServerTrusted(java.security.cert.X509Certificate[],java.lang.String)", "throw");
        METHOD_VS_SLICING_CRITERIA.put("void checkServerTrusted(java.security.cert.X509Certificate[],java.lang.String)", "checkValidity()");
        METHOD_VS_SLICING_CRITERIA.put("java.security.cert.X509Certificate[] getAcceptedIssuers()", "return");
    }

    @Override
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath) throws IOException {

        Map<String, List<OtherAnalysisResult>> analysisLists;
        if (type == EngineType.JAR) {
            analysisLists = analyzeJar(projectJarPath.get(0), projectDependencyPath.get(0));
        } else if (type == EngineType.APK) {
            analysisLists = analyzeApk(projectJarPath.get(0));
        } else {
            analysisLists = analyzeSnippet(projectJarPath, projectDependencyPath);
        }

        for (String className : analysisLists.keySet()) {

            List<OtherAnalysisResult> analysisList = analysisLists.get(className);

            for (OtherAnalysisResult analysis : analysisList) {

                if (analysis.getInstruction().equals("throw") &&
                        analysis.getAnalysis().isEmpty() &&
                        (!isThrowException(analysis.getMethod()) ||
                                hasTryCatch(analysis.getMethod()))) {

                    System.out.println("=======================================");
                    String output = "***Violated Rule 4: Uses untrusted TrustManager";
                    output += " ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of " + className;
                    System.out.println(output);
                    System.out.println("=======================================");
                }

                if (analysis.getInstruction().equals("checkValidity()") &&
                        !analysis.getAnalysis().isEmpty()) {

                    for (UnitContainer unit : analysis.getAnalysis()) {
                        if (unit.getUnit() instanceof JAssignStmt &&
                                unit.getUnit().toString().contains("[0]")) {
                            System.out.println("=======================================");
                            String output = "***Violated Rule 4: Uses untrusted TrustManager";
                            output += " ***Should not use unpinned self-signed certification in " + className;
                            System.out.println(output);
                            System.out.println("=======================================");
                        }
                    }
                }

                if (analysis.getInstruction().equals("return") && !analysis.getAnalysis().isEmpty()) {
                    boolean callsGetAcceptedIssuers = false;
                    for (UnitContainer unit : analysis.getAnalysis()) {

                        if (unit.getUnit().toString().contains("getAcceptedIssuers()")) {
                            callsGetAcceptedIssuers = true;
                            break;
                        }
                    }

                    if (!callsGetAcceptedIssuers) {
                        System.out.println("=======================================");
                        String output = "***Violated Rule 4: Uses untrusted TrustManager";
                        output += " ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of " + className;
                        System.out.println(output);
                        System.out.println("=======================================");
                    }
                }
            }
        }

    }

    private boolean isThrowException(SootMethod method) {
        Body b = method.retrieveActiveBody();
        DirectedGraph graph = new ExceptionalUnitGraph(b);

        Iterator unitIt = graph.iterator();

        while (unitIt.hasNext()) {
            Unit unit = (Unit) unitIt.next();

            if (unit instanceof JInvokeStmt) {
                List<SootClass> exceptions = ((JInvokeStmt) unit).getInvokeExpr().getMethod().getExceptions();

                return exceptions.toString().contains("CertificateException");
            }
        }

        return false;
    }

    private boolean hasTryCatch(SootMethod method) {
        Body b = method.retrieveActiveBody();
        return b.getTraps().size() > 0;
    }

    private Map<String, List<OtherAnalysisResult>> analyzeSnippet(List<String> snippetPath, List<String> projectDependencyPath) {

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

        return getAnalysisForTrustManager(classNames);
    }

    private Map<String, List<OtherAnalysisResult>> analyzeJar(String projectJarPath, String projectDependencyPath) throws IOException {
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
        return getAnalysisForTrustManager(classNames);
    }

    private Map<String, List<OtherAnalysisResult>> analyzeApk(String projectJarPath) throws IOException {
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
        return getAnalysisForTrustManager(classNames);
    }

    private static Map<String, List<OtherAnalysisResult>> getAnalysisForTrustManager(List<String> classNames) {

        Map<String, List<OtherAnalysisResult>> analysisList = new HashMap<>();

        NamedMethodMap.build(classNames);
        FieldInitializationInstructionMap.build(classNames);

        for (String className : classNames) {
            SootClass sClass = Scene.v().loadClassAndSupport(className);

            if (sClass.getInterfaces().toString().contains(TRUST_MANAGER)) {

                List<OtherAnalysisResult> otherAnalysisResults = new ArrayList<>();

                for (String methodName : METHOD_VS_SLICING_CRITERIA.keySet()) {

                    SootMethod method;
                    try {

                        method = sClass.getMethod(methodName);
                    } catch (RuntimeException e) {
                        continue;
                    }

                    if (method.isConcrete()) {

                        String slicingInstruction = METHOD_VS_SLICING_CRITERIA.get(methodName);

                        OtherInfluencingInstructions returnInfluencingInstructions =
                                new OtherInfluencingInstructions(method, slicingInstruction);

                        OtherAnalysisResult analysis = returnInfluencingInstructions.getAnalysisResult();
                        otherAnalysisResults.add(analysis);
                    }
                }

                analysisList.put(sClass.getName(), otherAnalysisResults);
            }
        }

        return analysisList;
    }
}

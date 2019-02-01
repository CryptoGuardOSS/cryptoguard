package main.rule;

import main.analyzer.UniqueRuleAnalyzer;
import main.analyzer.backward.UnitContainer;
import main.frontEnd.MessagingSystem.AnalysisIssue;
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
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.io.IOException;
import java.util.*;

/**
 * <p>CustomTrustManagerFinder class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public class CustomTrustManagerFinder implements RuleChecker {

    private static final String TRUST_MANAGER = "TrustManager";
    private static final Map<String, String> METHOD_VS_SLICING_CRITERIA = new HashMap<>();

    static {

        METHOD_VS_SLICING_CRITERIA.put("void checkClientTrusted(java.security.cert.X509Certificate[],java.lang.String)", "throw");
        METHOD_VS_SLICING_CRITERIA.put("void checkServerTrusted(java.security.cert.X509Certificate[],java.lang.String)", "throw");
        METHOD_VS_SLICING_CRITERIA.put("void checkServerTrusted(java.security.cert.X509Certificate[],java.lang.String)", "checkValidity()");
        METHOD_VS_SLICING_CRITERIA.put("java.security.cert.X509Certificate[] getAcceptedIssuers()", "return");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<AnalysisIssue> checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, Boolean printOut) throws IOException {

        Map<String, List<OtherAnalysisResult>> analysisLists =
                getAnalysisForTrustManager(
                        UniqueRuleAnalyzer.environmentRouting(projectJarPath, projectDependencyPath, type)
                );

        ArrayList<AnalysisIssue> issues = printOut ? null : new ArrayList<AnalysisIssue>();

        for (String className : analysisLists.keySet()) {

            List<OtherAnalysisResult> analysisList = analysisLists.get(className);

            for (OtherAnalysisResult analysis : analysisList) {

                if (analysis.getInstruction().equals("throw") &&
                        analysis.getAnalysis().isEmpty() &&
                        (!isThrowException(analysis.getMethod()) ||
                                hasTryCatch(analysis.getMethod()))) {

                    if (printOut) {
                        System.out.println("=======================================");
                        String output = "***Violated Rule 4: Uses untrusted TrustManager";
                        output += " ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of " + className;
                        System.out.println(output);
                        System.out.println("=======================================");
                    } else {
                        issues.add(
                                new AnalysisIssue(className,
                                        4,
                                        "Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of " +
                                                Utils.retrieveClassNameFromSootString(className)));
                    }

                }

                if (analysis.getInstruction().equals("checkValidity()") &&
                        !analysis.getAnalysis().isEmpty()) {

                    for (UnitContainer unit : analysis.getAnalysis()) {
                        if (unit.getUnit() instanceof JAssignStmt &&
                                unit.getUnit().toString().contains("[0]")) {

                            if (printOut) {
                                System.out.println("=======================================");
                                String output = "***Violated Rule 4: Uses untrusted TrustManager";
                                output += " ***Should not use unpinned self-signed certification in " + className;
                                System.out.println(output);
                                System.out.println("=======================================");
                            } else {
                                issues.add(new AnalysisIssue(
                                        unit, 4, className
                                ));
                            }
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
                        if (printOut) {
                            System.out.println("=======================================");
                            String output = "***Violated Rule 4: Uses untrusted TrustManager";
                            output += " ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of " + className;
                            System.out.println(output);
                            System.out.println("=======================================");
                        } else {
                            issues.add(
                                    new AnalysisIssue(className + " <getAcceptedIssuers>",
                                            4,
                                            "Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of " +
                                                    Utils.retrieveClassNameFromSootString(className)));
                        }
                    }
                }
            }
        }

        return issues;
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

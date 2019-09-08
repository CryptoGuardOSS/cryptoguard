package rule;

import analyzer.UniqueRuleAnalyzer;
import analyzer.backward.UnitContainer;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import rule.engine.EngineType;
import rule.engine.RuleChecker;
import slicer.backward.other.OtherAnalysisResult;
import slicer.backward.other.OtherInfluencingInstructions;
import soot.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import util.FieldInitializationInstructionMap;
import util.NamedMethodMap;
import util.Utils;

import java.util.*;

/**
 * <p>CustomTrustManagerFinder class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
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
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, List<String> sourcePaths, OutputStructure output) throws ExceptionHandler {

        Map<String, List<OtherAnalysisResult>> analysisLists =
                getAnalysisForTrustManager(
                        UniqueRuleAnalyzer.environmentRouting(projectJarPath, projectDependencyPath, type)
                );


        for (String className : analysisLists.keySet()) {

            List<OtherAnalysisResult> analysisList = analysisLists.get(className);

            for (OtherAnalysisResult analysis : analysisList) {

                if (analysis.getInstruction().equals("throw") &&
                        analysis.getAnalysis().isEmpty() &&
                        (!isThrowException(analysis.getMethod()) ||
                                hasTryCatch(analysis.getMethod()))) {

                    AnalysisIssue issue = new AnalysisIssue(className,
                            4,
                            "Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of " +
                                    Utils.retrieveClassNameFromSootString(className), sourcePaths);

                    output.addIssue(issue);

                }

                if (analysis.getInstruction().equals("checkValidity()") &&
                        !analysis.getAnalysis().isEmpty()) {

                    for (UnitContainer unit : analysis.getAnalysis()) {
                        if (unit.getUnit() instanceof JAssignStmt &&
                                unit.getUnit().toString().contains("[0]")) {

                            AnalysisIssue issue = new AnalysisIssue(unit, 4, className, sourcePaths);

                            output.addIssue(issue);
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

                        AnalysisIssue issue = new AnalysisIssue(className + " <getAcceptedIssuers>",
                                4,
                                "Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of " +
                                        Utils.retrieveClassNameFromSootString(className), sourcePaths);

                        output.addIssue(issue);
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

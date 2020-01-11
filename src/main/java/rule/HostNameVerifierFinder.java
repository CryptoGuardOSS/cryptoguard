package rule;

import analyzer.UniqueRuleAnalyzer;
import analyzer.backward.UnitContainer;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import rule.engine.EngineType;
import rule.engine.RuleChecker;
import slicer.backward.other.OtherInfluencingInstructions;
import soot.*;
import soot.jimple.Constant;
import util.FieldInitializationInstructionMap;
import util.NamedMethodMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>HostNameVerifierFinder class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class HostNameVerifierFinder implements RuleChecker {

    private static final String HOST_NAME_VERIFIER = "HostnameVerifier";
    private static final String METHOD_TO_SLICE = "boolean verify(java.lang.String,javax.net.ssl.SSLSession)";
    private static final String SLICING_INSTRUCTION = "return";

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, List<String> sourcePaths, OutputStructure output, String mainKlass, String androidHome, String javaHome) throws ExceptionHandler {

        Map<String, List<UnitContainer>> analysisLists = getHostNameVerifiers(
                UniqueRuleAnalyzer.environmentRouting(projectJarPath, projectDependencyPath, type, androidHome, javaHome)
        );

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
                    AnalysisIssue issue = new AnalysisIssue(className, 6,
                            "Cause: Fixed \"" + constants.toString().replaceAll("\"", "") + "\"",
                            sourcePaths);

                    output.addIssue(issue);
                }
            }
        }
    }
}

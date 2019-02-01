package main.rule;

import main.analyzer.UniqueRuleAnalyzer;
import main.analyzer.backward.UnitContainer;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;
import main.slicer.backward.other.OtherInfluencingInstructions;
import main.util.FieldInitializationInstructionMap;
import main.util.NamedMethodMap;
import main.util.Utils;
import soot.*;
import soot.jimple.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>HostNameVerifierFinder class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public class HostNameVerifierFinder implements RuleChecker {

    private static final String HOST_NAME_VERIFIER = "HostnameVerifier";
    private static final String METHOD_TO_SLICE = "boolean verify(java.lang.String,javax.net.ssl.SSLSession)";
    private static final String SLICING_INSTRUCTION = "return";

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<AnalysisIssue> checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, Boolean printOut) throws IOException {

        Map<String, List<UnitContainer>> analysisLists = getHostNameVerifiers(
                UniqueRuleAnalyzer.environmentRouting(projectJarPath, projectDependencyPath, type)
        );

        ArrayList<AnalysisIssue> issues = printOut ? null : new ArrayList<AnalysisIssue>();

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
                    if (printOut) {
                        System.out.println("=======================================");
                        String output = "*** Violated Rule 6: Uses untrusted HostNameVerifier";
                        if (!constants.isEmpty()) {
                            output += "\n***Cause: Fixed " + constants.toString() + " used in " + className;
                        }
                        System.out.println(output);
                        System.out.println("=======================================");
                    } else {
                        issues.add(new AnalysisIssue(
                                className,
                                6,
                                "Fixed " + constants.toString() + " used in " + Utils.retrieveCauseFromSootString(className)
                        ));
                    }
                }
            }
        }
        return issues;
    }

    private static Map<String, List<UnitContainer>> getHostNameVerifiers(List<String> classNames) {

        Map<String, List<UnitContainer>> analysisList = new HashMap<>();

        NamedMethodMap.build(classNames);
        FieldInitializationInstructionMap.build(classNames);

        for (String className : classNames) {
            SootClass sClass = Scene.v().loadClassAndSupport(className);

            if (sClass.getInterfaces().toString().contains(HOST_NAME_VERIFIER)) {

                SootMethod method = sClass.getMethod(METHOD_TO_SLICE);

                if (method.isConcrete()) {

                    OtherInfluencingInstructions returnInfluencingInstructions = new OtherInfluencingInstructions(method,
                            SLICING_INSTRUCTION);
                    List<UnitContainer> analysis = returnInfluencingInstructions.getAnalysisResult().getAnalysis();
                    analysisList.put(className, analysis);
                }
            }
        }

        return analysisList;
    }
}

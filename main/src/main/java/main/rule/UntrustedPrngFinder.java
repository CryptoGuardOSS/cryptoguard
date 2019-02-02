package main.rule;

import main.analyzer.UniqueRuleAnalyzer;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;
import soot.*;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.io.IOException;
import java.util.*;

/**
 * <p>UntrustedPrngFinder class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public class UntrustedPrngFinder implements RuleChecker {

    private static final List<String> UNTRUSTED_PRNGS = new ArrayList<>();

    static {
        UNTRUSTED_PRNGS.add("java.util.Random: void <init>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<AnalysisIssue> checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, Boolean printOut, List<String> sourcePaths) throws IOException {

        Map<String, List<Unit>> analysisLists = getUntrustedPrngInstructions(
                UniqueRuleAnalyzer.environmentRouting(projectJarPath, projectDependencyPath, type));

        ArrayList<AnalysisIssue> issues = printOut ? null : new ArrayList<AnalysisIssue>();

        if (!analysisLists.isEmpty()) {
            for (String method : analysisLists.keySet()) {
                List<Unit> analysis = analysisLists.get(method);

                if (!analysis.isEmpty()) {
                    if (printOut) {
                        System.out.println("=============================================");
                        String output = "***Violated Rule 13: Untrused PRNG (java.util.Random) Found in " + method;
                        System.out.println(output);
                        System.out.println("=============================================");
                    } else {
                        //TODO - Location not showing up
                        issues.add(new AnalysisIssue(
                                method,
                                13,
                                "Found: Untrused PRNG (java.util.Random)", sourcePaths

                        ));
                    }
                }
            }
        }
        return issues;
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

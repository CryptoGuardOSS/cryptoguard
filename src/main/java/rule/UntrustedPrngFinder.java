package rule;

import analyzer.UniqueRuleAnalyzer;
import frontEnd.Interface.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import rule.engine.EngineType;
import rule.engine.RuleChecker;
import soot.*;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.util.*;

/**
 * <p>UntrustedPrngFinder class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class UntrustedPrngFinder implements RuleChecker {

    private static final List<String> UNTRUSTED_PRNGS = new ArrayList<>();

    static {
        UNTRUSTED_PRNGS.add("java.util.Random: void <init>");
        UNTRUSTED_PRNGS.add("java.lang.Math: double random");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, List<String> sourcePaths, OutputStructure output) throws ExceptionHandler {

        Map<String, List<Unit>> analysisLists = getUntrustedPrngInstructions(
                UniqueRuleAnalyzer.environmentRouting(projectJarPath, projectDependencyPath, type));

        if (!analysisLists.isEmpty()) {
            for (String method : analysisLists.keySet()) {
                List<Unit> analysis = analysisLists.get(method);

                if (!analysis.isEmpty()) {
                    //region LEGACY
                    /*
                        System.out.println("=============================================");
                        String output = "***Violated Rule 13: Untrused PRNG (java.util.Random) Found in " + method;
                        System.out.println(output);
                        System.out.println("=============================================");
                    */
                    //endregion
                    //TODO - Location not showing up
                    AnalysisIssue issue = new AnalysisIssue(method, 13,
                            "Found: Untrused PRNG (java.util.Random)", sourcePaths);

                    output.addIssue(issue);
                }
            }
        }
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

package rule;

import analyzer.UniqueRuleAnalyzer;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import rule.engine.EngineType;
import rule.engine.RuleChecker;
import slicer.forward.ForwardInfluenceInstructions;
import slicer.forward.SlicingCriteria;
import slicer.forward.SlicingResult;
import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>DefaultExportGradeKeyFinder class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class DefaultExportGradeKeyFinder implements RuleChecker {

    private static final List<String> SLICING_CRITERIA = new ArrayList<>();

    static {
        SLICING_CRITERIA.add("<java.security.KeyPairGenerator: java.security.KeyPairGenerator getInstance(java.lang.String)>");
        SLICING_CRITERIA.add("<java.security.KeyPairGenerator: java.security.KeyPairGenerator getInstance(java.lang.String,java.lang.String)>");
        SLICING_CRITERIA.add("<java.security.KeyPairGenerator: java.security.KeyPairGenerator getInstance(java.lang.String,java.security.Provider)>");
    }

    private static final String METHOD_TO_FIND = "<java.security.KeyPairGenerator: void initialize(";

    private boolean defaultSecure;

    private ArrayList<String> methodsToLook;

    private ArrayList<String> initializeCallsites = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, List<String> sourcePaths, OutputStructure output) throws ExceptionHandler {

        for (String slicing_criterion : SLICING_CRITERIA) {

            Map<String, List<Unit>> analysisLists = getForwardSlice(
                    UniqueRuleAnalyzer.environmentRouting(projectJarPath, projectDependencyPath, type),
                    new SlicingCriteria(slicing_criterion));

            for (String method : analysisLists.keySet()) {

                if (!methodsToLook.toString().contains(method)) {
                    continue;
                }

                List<Unit> analysis = analysisLists.get(method);

                if (!analysis.isEmpty()) {

                    boolean isDefault = true;

                    for (Unit unit : analysis) {

                        if (unit.toString().contains(METHOD_TO_FIND)) {

                            String containingMethod = method.substring(0, method.indexOf('['));
                            initializeCallsites.add(containingMethod + "[" + unit.getJavaSourceStartLineNumber() + "]");
                            isDefault = false;
                            break;
                        }
                    }

                    if (isDefault && !defaultSecure) {

                        AnalysisIssue issue = new AnalysisIssue(method, 5,
                                "Cause: Used default key size", sourcePaths);

                        output.addIssue(issue);

                    }

                }
            }
        }
    }

    /**
     * <p>Setter for the field <code>methodsToLook</code>.</p>
     *
     * @param methodsToLook a {@link java.util.ArrayList} object.
     */
    public void setMethodsToLook(ArrayList<String> methodsToLook) {
        this.methodsToLook = methodsToLook;
    }

    /**
     * <p>getInitializationCallsites.</p>
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<String> getInitializationCallsites() {
        return this.initializeCallsites;
    }

    /**
     * <p>Setter for the field <code>defaultSecure</code>.</p>
     *
     * @param defaultSecure a boolean.
     */
    public void setDefaultSecure(boolean defaultSecure) {
        this.defaultSecure = defaultSecure;
    }

    private static Map<String, List<Unit>> getForwardSlice(List<String> classNames, SlicingCriteria slicingCriteria) {

        Map<String, List<Unit>> analysisListMap = new HashMap<>();

        for (String className : classNames) {

            SootClass sClass = Scene.v().loadClassAndSupport(className);

            sClass.setApplicationClass();

            for (SootMethod method : sClass.getMethods()) {
                SlicingResult slicingResult = getInfluencingInstructions(slicingCriteria, method);

                if (slicingResult != null) {
                    analysisListMap.put(method.toString() + "[" + slicingResult.getCallSiteInfo().getLineNumber() + "]", slicingResult.getAnalysisResult());
                }
            }
        }

        return analysisListMap;
    }

    /**
     * <p>getInfluencingInstructions.</p>
     *
     * @param slicingCriteria a {@link slicer.forward.SlicingCriteria} object.
     * @param m               a {@link soot.SootMethod} object.
     * @return a {@link slicer.forward.SlicingResult} object.
     */
    public static SlicingResult getInfluencingInstructions(SlicingCriteria slicingCriteria,
                                                           SootMethod m) {
        if (m.isConcrete()) {

            Body b = m.retrieveActiveBody();

            UnitGraph graph = new ExceptionalUnitGraph(b);
            ForwardInfluenceInstructions vbe = new ForwardInfluenceInstructions(graph, slicingCriteria);
            return vbe.getSlicingResult();
        }
        return null;
    }
}

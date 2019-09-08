package rule.base;

import analyzer.BaseAnalyzerRouting;
import analyzer.backward.Analysis;
import analyzer.backward.UnitContainer;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import rule.engine.Criteria;
import rule.engine.EngineType;
import rule.engine.RuleChecker;
import util.Utils;

import java.util.*;

/**
 * This file checks on several of the main factors within the outputed Jimple format.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public abstract class BaseRuleChecker implements RuleChecker {

    /**
     * Constant <code>RULE_VS_DESCRIPTION</code>
     */
    public static final Map<String, String> RULE_VS_DESCRIPTION = new HashMap<>();

    static {

        RULE_VS_DESCRIPTION.put("1", "Found broken crypto schemes"); // Rule 11, 14
        RULE_VS_DESCRIPTION.put("2", "Found broken hash functions"); // Rule 16
        RULE_VS_DESCRIPTION.put("3", "Used constant keys in code");  // Rule 1, 2
        RULE_VS_DESCRIPTION.put("4", "Used untrusted TrustManager"); // Rule 5
        RULE_VS_DESCRIPTION.put("5", "Used export grade public Key"); // Rule 15
        RULE_VS_DESCRIPTION.put("6", "Used untrusted HostNameVerifier"); // Rule 4
        RULE_VS_DESCRIPTION.put("7", "Used HTTP Protocol"); // Rule 7
        RULE_VS_DESCRIPTION.put("8", "Used < 1000 iteration for PBE"); // Rule 13
        RULE_VS_DESCRIPTION.put("9", "Found constant salts in code"); // Rule 10
        RULE_VS_DESCRIPTION.put("10", "Found constant IV in code"); // Rule 12
        RULE_VS_DESCRIPTION.put("11", "Found predictable seeds in code"); // Rule 8
        RULE_VS_DESCRIPTION.put("12", "Should check HostnameVerification manually"); // Rule 6
        RULE_VS_DESCRIPTION.put("13", "Used untrusted PRNG"); // Rule 9
        RULE_VS_DESCRIPTION.put("14", "Used Predictable KeyStore Password"); // Rule 3

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRule(EngineType type, List<String> projectPaths, List<String> projectDependencyPath, List<String> sourcePaths, OutputStructure output) throws ExceptionHandler {

        String[] excludes = {"web.xml", "pom.xml"};

        Map<String, String> xmlFileStr = Utils.getXmlFiles(projectPaths.get(0), Arrays.asList(excludes));

        for (Criteria criteria : getCriteriaList()) {
            BaseAnalyzerRouting.environmentRouting(type, criteria.getClassName(),
                    criteria.getMethodName(),
                    criteria.getParam(),
                    projectPaths,
                    projectDependencyPath, this);
        }

        createAnalysisOutput(xmlFileStr, sourcePaths, output);
    }

    /**
     * <p>getCriteriaList.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public abstract List<Criteria> getCriteriaList();

    /**
     * <p>analyzeSlice.</p>
     *
     * @param analysis a {@link analyzer.backward.Analysis} object.
     */
    public abstract void analyzeSlice(Analysis analysis);

    /**
     * <p>createAnalysisOutput.</p>
     *
     * @param xmlFileStr  a {@link java.util.Map} object.
     * @param sourcePaths a {@link java.util.List} object.
     * @param output      a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public abstract void createAnalysisOutput(Map<String, String> xmlFileStr, List<String> sourcePaths, OutputStructure output) throws ExceptionHandler;

    /**
     * <p>putIntoMap.</p>
     *
     * @param unitStringMap a {@link java.util.Map} object.
     * @param e             a {@link analyzer.backward.UnitContainer} object.
     * @param value         a {@link java.lang.String} object.
     */
    protected void putIntoMap(Map<UnitContainer, List<String>> unitStringMap, UnitContainer e, String value) {

        List<String> values = unitStringMap.get(e);
        if (values == null) {
            values = new ArrayList<>();
            values.add(value);
            unitStringMap.put(e, values);
            return;
        }

        if (!values.toString().contains(value)) {
            values.add(value);
        }
    }
}

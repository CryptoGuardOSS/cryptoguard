package rule;

import analyzer.backward.Analysis;
import analyzer.backward.UnitContainer;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import rule.base.BaseRuleChecker;
import rule.engine.Criteria;
import soot.ValueBox;
import soot.jimple.Constant;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>AssymCryptoFinder class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class AssymCryptoFinder extends BaseRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {

        Criteria criteria1 = new Criteria();
        criteria1.setClassName("java.security.KeyPairGenerator");
        criteria1.setMethodName("java.security.KeyPairGenerator getInstance(java.lang.String)");
        criteria1.setParam(0);
        CRITERIA_LIST.add(criteria1);

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("java.security.KeyPairGenerator");
        criteria2.setMethodName("java.security.KeyPairGenerator getInstance(java.lang.String,java.lang.String)");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("java.security.KeyPairGenerator");
        criteria3.setMethodName("java.security.KeyPairGenerator getInstance(java.lang.String,java.security.Provider)");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);
    }
    //endregion

    //region
    String rule = "00";
    private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
    private List<String> crypto;
    private ArrayList<String> occurrences = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void analyzeSlice(Analysis analysis) {
        if (analysis.getAnalysisResult().isEmpty()) {
            return;
        }

        String[] splits = analysis.getMethodChain().split("--->");

        for (UnitContainer e : analysis.getAnalysisResult()) {
            for (ValueBox usebox : e.getUnit().getUseBoxes()) {
                if (usebox.getValue() instanceof Constant) {

                    for (String regex : crypto) {
                        if (usebox.getValue().toString().matches(regex)) {
                            occurrences.add(splits[splits.length - 2]);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * <p>getOccurrenceSites.</p>
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<String> getOccurrenceSites() {
        return occurrences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    /**
     * <p>Setter for the field <code>crypto</code>.</p>
     *
     * @param crypto a {@link java.util.List} object.
     */
    public void setCrypto(List<String> crypto) {
        this.crypto = crypto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAnalysisOutput(Map<String, String> xmlFileStr, List<String> sourcePaths, OutputStructure output) throws ExceptionHandler {
        //region New Analysis
        //region PBEInterationCountFinder
        Utils.createAnalysisOutput(xmlFileStr, sourcePaths, predictableSourcMap, rule, output);
        //endregion
        //region ExportGradeKeyIniFinder
        /*
        ArrayList<AnalysisIssue> outList = new ArrayList<>();

        for (UnitContainer unit : predictableSourcMap.keySet()) {
            String sootString = predictableSourcMap.get(unit).size() <= 0 ? ""
                    : "Found: \"" + predictableSourcMap.get(unit).get(0).replaceAll("\"", "") + "\"";

            output.addIssue(new AnalysisIssue(unit, Integer.parseInt(rule), sootString, sourcePaths));
        }
        */
        //endregion
        //endregion
    }
}

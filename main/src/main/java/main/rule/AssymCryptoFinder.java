package main.rule;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.UnitContainer;
import main.rule.base.BaseRuleChecker;
import main.rule.engine.Criteria;
import soot.ValueBox;
import soot.jimple.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssymCryptoFinder extends BaseRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    private List<String> crypto;

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

    private ArrayList<String> occurrences = new ArrayList<>();

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

    public ArrayList<String> getOccurrenceSites() {
        return occurrences;
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    public void setCrypto(List<String> crypto) {
        this.crypto = crypto;
    }

    @Override
    public void printAnalysisOutput(Map<String, String> xmlFileStr) { }
}

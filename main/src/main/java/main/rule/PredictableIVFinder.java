package main.rule;

import main.rule.base.PredictableSourceRuleChecker;
import main.rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

public class PredictableIVFinder extends PredictableSourceRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("javax.crypto.spec.IvParameterSpec");
        criteria2.setMethodName("void <init>(byte[])");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("javax.crypto.spec.IvParameterSpec");
        criteria4.setMethodName("void <init>(byte[],int,int)");
        criteria4.setParam(0);
        CRITERIA_LIST.add(criteria4);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    @Override
    public String getRuleId() {
        return "10";
    }
}

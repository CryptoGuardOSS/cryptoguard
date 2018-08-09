package main.rule;


import main.rule.base.PredictableSourceRuleChecker;
import main.rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

public class ConstantKeyFinder extends PredictableSourceRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {
        Criteria criteria1 = new Criteria();
        criteria1.setClassName("javax.crypto.spec.SecretKeySpec");
        criteria1.setMethodName("void <init>(byte[],java.lang.String)");
        criteria1.setParam(0);
        CRITERIA_LIST.add(criteria1);

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("javax.crypto.spec.SecretKeySpec");
        criteria2.setMethodName("void <init>(byte[],int,int,java.lang.String)");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("javax.crypto.spec.PBEKeySpec");
        criteria3.setMethodName("void <init>(char[])");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("javax.crypto.spec.PBEKeySpec");
        criteria4.setMethodName("void <init>(char[],byte[],int,int)");
        criteria4.setParam(0);
        CRITERIA_LIST.add(criteria4);

        Criteria criteria5 = new Criteria();
        criteria5.setClassName("javax.crypto.spec.PBEKeySpec");
        criteria5.setMethodName("void <init>(char[],byte[],int)");
        criteria5.setParam(0);
        CRITERIA_LIST.add(criteria5);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }


    @Override
    public String getRuleId() {
        return "3";
    }
}

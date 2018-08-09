package main.rule;

import main.rule.base.PredictableSourceRuleChecker;
import main.rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * https://www.javamex.com/tutorials/cryptography/pbe_salt.shtml
 */
public class PBESaltFinder extends PredictableSourceRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("javax.crypto.spec.PBEParameterSpec");
        criteria2.setMethodName("void <init>(byte[],int)");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("javax.crypto.spec.PBEParameterSpec");
        criteria3.setMethodName("void <init>(byte[],int,java.security.spec.AlgorithmParameterSpec)");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("javax.crypto.spec.PBEKeySpec");
        criteria4.setMethodName("void <init>(char[],byte[],int,int)");
        criteria4.setParam(1);
        CRITERIA_LIST.add(criteria4);

        Criteria criteria5 = new Criteria();
        criteria5.setClassName("javax.crypto.spec.PBEKeySpec");
        criteria5.setMethodName("void <init>(char[],byte[],int)");
        criteria5.setParam(1);
        CRITERIA_LIST.add(criteria5);

    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    @Override
    public String getRuleId() {
        return "9";
    }
}

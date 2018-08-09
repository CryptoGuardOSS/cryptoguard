package main.rule;

import main.rule.base.PredictableSourceRuleChecker;
import main.rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

public class PredictableKeyStorePasswordFinder extends PredictableSourceRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {
        Criteria criteria1 = new Criteria();
        criteria1.setClassName("java.security.KeyStore");
        criteria1.setMethodName("void load(java.io.InputStream,char[])");
        criteria1.setParam(1);
        CRITERIA_LIST.add(criteria1);

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("java.security.KeyStore");
        criteria2.setMethodName("void store(java.io.OutputStream,char[])");
        criteria2.setParam(1);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("java.security.KeyStore");
        criteria4.setMethodName("void setKeyEntry(java.lang.String,java.security.Key,char[],java.security.cert.Certificate[])");
        criteria4.setParam(2);
        CRITERIA_LIST.add(criteria4);

        Criteria criteria5 = new Criteria();
        criteria5.setClassName("java.security.KeyStore");
        criteria5.setMethodName("java.security.Key getKey(java.lang.String,char[])");
        criteria5.setParam(1);
        CRITERIA_LIST.add(criteria5);
    }

    @Override
    public String getRuleId() {
        return "14";
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }
}

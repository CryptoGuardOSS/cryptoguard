package main.rule;

import main.rule.base.PatternMatcherRuleChecker;
import main.rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

public class BrokenHashFinder extends PatternMatcherRuleChecker {

    private static final List<String> BROKEN_HASH = new ArrayList<>();
    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {
        BROKEN_HASH.add("\"MD2\"");
        BROKEN_HASH.add("(.)*MD5\"");
        BROKEN_HASH.add("(.)*MD4\"");
        BROKEN_HASH.add("(.)*SHA-1\"");
        BROKEN_HASH.add("\"SHA1\"");
        BROKEN_HASH.add("\"SHA\"");

        Criteria criteria1 = new Criteria();
        criteria1.setClassName("java.security.MessageDigest");
        criteria1.setMethodName("java.security.MessageDigest getInstance(java.lang.String)");
        criteria1.setParam(0);
        CRITERIA_LIST.add(criteria1);

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("java.security.MessageDigest");
        criteria2.setMethodName("java.security.MessageDigest getInstance(java.lang.String,java.lang.String)");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("java.security.MessageDigest");
        criteria3.setMethodName("java.security.MessageDigest getInstance(java.lang.String,java.security.Provider)");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    @Override
    public List<String> getPatternsToMatch() {
        return BROKEN_HASH;
    }

    @Override
    public String getRuleId() {
        return "2";
    }
}

package main.rule;

import main.rule.base.PatternMatcherRuleChecker;
import main.rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

public class HttpUrlFinder extends PatternMatcherRuleChecker {

    private static final List<String> HTTP_URL_PATTERN = new ArrayList<>();
    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {
        HTTP_URL_PATTERN.add("\"http:(.)*");
        HTTP_URL_PATTERN.add("\"http$");

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("java.net.URL");
        criteria2.setMethodName("void <init>(java.lang.String,java.lang.String,java.lang.String)");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("java.net.URL");
        criteria3.setMethodName("void <init>(java.lang.String,java.lang.String,int,java.lang.String)");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("okhttp3.Request$Builder");
        criteria4.setMethodName("okhttp3.Request$Builder url(java.lang.String)");
        criteria4.setParam(0);
        CRITERIA_LIST.add(criteria4);

        Criteria criteria5 = new Criteria();
        criteria5.setClassName("retrofit2.Retrofit$Builder");
        criteria5.setMethodName("retrofit2.Retrofit$Builder baseUrl(java.lang.String)");
        criteria5.setParam(0);
        CRITERIA_LIST.add(criteria5);

        Criteria criteria1 = new Criteria();
        criteria1.setClassName("java.net.URL");
        criteria1.setMethodName("void <init>(java.lang.String)");
        criteria1.setParam(0);
        CRITERIA_LIST.add(criteria1);

    }


    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    @Override
    public List<String> getPatternsToMatch() {
        return HTTP_URL_PATTERN;
    }

    @Override
    public String getRuleId() {
        return "7";
    }
}

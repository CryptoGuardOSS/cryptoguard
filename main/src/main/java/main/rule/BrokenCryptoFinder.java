package main.rule;

import main.rule.base.PatternMatcherRuleChecker;
import main.rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

public class BrokenCryptoFinder extends PatternMatcherRuleChecker {

    private static final List<String> BROKEN_CRYPTO = new ArrayList<>();
    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {
        BROKEN_CRYPTO.add("(.)*DES(.)*");
        BROKEN_CRYPTO.add("(.)*DESede(.)*");
        BROKEN_CRYPTO.add("\"AES\"");
        BROKEN_CRYPTO.add("\"AES/ECB(.)*");
        BROKEN_CRYPTO.add("(.)*DESedeWrap(.)*");
        BROKEN_CRYPTO.add("(.)*RC2(.)*");
        BROKEN_CRYPTO.add("(.)*RC4(.)*");
        BROKEN_CRYPTO.add("(.)*RC5(.)*");
        BROKEN_CRYPTO.add("(.)*Blowfish(.)*");
        BROKEN_CRYPTO.add("(.)*IDEA(.)*");
        BROKEN_CRYPTO.add("PBEWithMD5AndDES");
        BROKEN_CRYPTO.add("PBEWithMD5AndTripleDES");
        BROKEN_CRYPTO.add("PBEWithSHA1AndDESede");
        BROKEN_CRYPTO.add("PBEWithSHA1AndRC2_40");

        Criteria criteria1 = new Criteria();
        criteria1.setClassName("javax.crypto.Cipher");
        criteria1.setMethodName("javax.crypto.Cipher getInstance(java.lang.String)");
        criteria1.setParam(0);
        CRITERIA_LIST.add(criteria1);

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("javax.crypto.Cipher");
        criteria2.setMethodName("javax.crypto.Cipher getInstance(java.lang.String,java.lang.String)");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("javax.crypto.Cipher");
        criteria3.setMethodName("javax.crypto.Cipher getInstance(java.lang.String,java.security.Provider)");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    @Override
    public List<String> getPatternsToMatch() {
        return BROKEN_CRYPTO;
    }

    @Override
    public String getRuleId() {
        return "1";
    }
}

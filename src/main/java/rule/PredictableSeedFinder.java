package rule;

import rule.base.PredictableSourceRuleChecker;
import rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RigorityJTeam on 11/15/17.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class PredictableSeedFinder extends PredictableSourceRuleChecker {

    private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();

    static {

        Criteria criteria2 = new Criteria();
        criteria2.setClassName("java.security.SecureRandom");
        criteria2.setMethodName("void <init>(byte[])");
        criteria2.setParam(0);
        CRITERIA_LIST.add(criteria2);

        Criteria criteria3 = new Criteria();
        criteria3.setClassName("java.security.SecureRandom");
        criteria3.setMethodName("void setSeed(byte[])");
        criteria3.setParam(0);
        CRITERIA_LIST.add(criteria3);

        Criteria criteria4 = new Criteria();
        criteria4.setClassName("java.security.SecureRandom");
        criteria4.setMethodName("void setSeed(long)");
        criteria4.setParam(0);
        CRITERIA_LIST.add(criteria4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Criteria> getCriteriaList() {
        return CRITERIA_LIST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRuleId() {
        return "11";
    }
}

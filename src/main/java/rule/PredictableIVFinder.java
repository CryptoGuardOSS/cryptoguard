package rule;

import rule.base.PredictableSourceRuleChecker;
import rule.engine.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>PredictableIVFinder class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
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
        return "10";
    }
}

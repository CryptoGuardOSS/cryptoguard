/* Licensed under GPL-3.0 */
package rule;

import java.util.ArrayList;
import java.util.List;
import rule.base.PredictableSourceRuleChecker;
import rule.engine.Criteria;

/**
 * https://www.javamex.com/tutorials/cryptography/pbe_salt.shtml
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
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

  /** {@inheritDoc} */
  @Override
  public List<Criteria> getCriteriaList() {
    return CRITERIA_LIST;
  }

  /** {@inheritDoc} */
  @Override
  public String getRuleId() {
    return "9";
  }
}

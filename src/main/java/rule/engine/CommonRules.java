package rule.engine;

import rule.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>CommonRules class.</p>
 *
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @version $Id: $Id
 * @since 01.01.06
 *
 * <p>A common stash of rules used by all three methods</p>
 */
public class CommonRules {
    /**
     * Constant <code>ruleCheckerList</code>
     */
    public static List<RuleChecker> ruleCheckerList = new ArrayList<>();

    static {

        //region BaseAnalyzer Routes
        ruleCheckerList.add(new InsecureAssymCryptoFinder());
        ruleCheckerList.add(new BrokenCryptoFinder());
        //endregion
        ruleCheckerList.add(new UntrustedPrngFinder());
        ruleCheckerList.add(new SSLSocketFactoryFinder());
        ruleCheckerList.add(new CustomTrustManagerFinder());
        ruleCheckerList.add(new HostNameVerifierFinder());
        //region BaseAnalyzer Routes
        ruleCheckerList.add(new BrokenHashFinder());
        ruleCheckerList.add(new ConstantKeyFinder());
        ruleCheckerList.add(new PredictableIVFinder());
        ruleCheckerList.add(new PBESaltFinder());
        ruleCheckerList.add(new PBEInterationCountFinder());
        ruleCheckerList.add(new PredictableSeedFinder());
        ruleCheckerList.add(new PredictableKeyStorePasswordFinder());
        ruleCheckerList.add(new HttpUrlFinder());
        //endregion
    }
}

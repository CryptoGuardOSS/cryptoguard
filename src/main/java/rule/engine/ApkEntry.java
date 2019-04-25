package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>ApkEntry class.</p>
 *
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @version $Id: $Id
 * @since 01.01.06
 *
 * <p>The method in the Engine handling Apk Scanning</p>
 */
public class ApkEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {

        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
            ruleChecker.checkRule(EngineType.APK, generalInfo.getSource(), null, generalInfo.getSourcePaths(), generalInfo.getOutput());
        }
    }

}

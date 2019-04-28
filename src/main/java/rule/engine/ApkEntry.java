package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class ApkEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {
        log.trace("Starting scanner looper");
        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
            log.debug("Checking the rule: " + ruleChecker.toString());
            ruleChecker.checkRule(EngineType.APK, generalInfo.getSource(), null, generalInfo.getSourcePaths(), generalInfo.getOutput());
        }
        log.trace("Scanner looper stopped");
    }

}

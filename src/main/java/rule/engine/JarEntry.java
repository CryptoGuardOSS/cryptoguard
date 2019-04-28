package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>JarEntry class.</p>
 *
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @version $Id: $Id
 * @since 01.01.06
 *
 * <p>The method in the Engine handling Jar Scanning</p>
 */
@Slf4j
public class JarEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {

        log.trace("Starting scanner looper");
        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
            log.debug("Checking the rule: " + ruleChecker.toString());
            ruleChecker.checkRule(EngineType.JAR, generalInfo.getSource(), generalInfo.getDependencies(), generalInfo.getSourcePaths(), generalInfo.getOutput());
        }
        log.trace("Scanner looper stopped");
    }

}

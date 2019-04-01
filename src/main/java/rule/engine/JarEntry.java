package rule.engine;

import frontEnd.Interface.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;

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
public class JarEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {

        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
            ruleChecker.checkRule(EngineType.JAR, generalInfo.getSource(), generalInfo.getDependencies(), generalInfo.getSourcePaths(), generalInfo.getOutput());
        }
    }

}

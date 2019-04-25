package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>JavaClassFileEntry class.</p>
 *
 * @author RigorityJTeam
 * Created on 2019-01-17.
 * @version $Id: $Id
 * @since 01.01.11
 *
 * <p>The method in the Engine handling Java Class File(s) Scanning.</p>
 */
public class JavaClassFileEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {

        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
            ruleChecker.checkRule(generalInfo.getSourceType(), generalInfo.getSource(), generalInfo.getDependencies(), generalInfo.getSourcePaths(), generalInfo.getOutput());

        }
    }


}

/* Licensed under GPL-3.0 */
package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.logging.log4j.Logger;

/**
 * ApkEntry class.
 *
 * @author CryptoguardTeam Created on 2018-12-14.
 * @version 03.07.01
 * @since 01.01.06
 *     <p>The method in the Engine handling Apk Scanning
 */
public class ApkEntry implements EntryHandler {

  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(ApkEntry.class);

  /** {@inheritDoc} */
  public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {
    log.debug("Starting scanner looper");
    for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
      log.info("Checking the rule: " + ruleChecker.getClass().getSimpleName());
      ruleChecker.checkRule(
          EngineType.APK,
          generalInfo.getSource(),
          null,
          generalInfo.getSourcePaths(),
          generalInfo.getOutput(),
          generalInfo.getMain(),
          generalInfo.getAndroidHome(),
          generalInfo.getJavaHome());
    }
    log.debug("Scanner looper stopped");
  }
}

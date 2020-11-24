/* Licensed under GPL-3.0 */
package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import util.BuildFileParser;
import util.BuildFileParserFactory;
import util.FieldInitializationInstructionMap;
import util.NamedMethodMap;

/**
 * SourceEntry class.
 *
 * @author CryptoguardTeam Created on 2018-12-14.
 * @version 03.07.01
 * @since 01.01.06
 *     <p>The method in the Engine handling Source Scanning
 */
public class SourceEntry implements EntryHandler {

  private static final Logger log =
      org.apache.logging.log4j.LogManager.getLogger(SourceEntry.class);

  /** {@inheritDoc} */
  public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {

    log.debug("Retrieving the specific project-based build parser.");
    BuildFileParser buildFileParser =
        BuildFileParserFactory.getBuildfileParser(generalInfo.getSource().get(0));
    log.debug("Using the build parser: " + buildFileParser.toString());

    log.debug("Setting the project name/version");
    generalInfo.setTargetProjectName(buildFileParser.getProjectName());
    generalInfo.setTargetProjectVersion(buildFileParser.getProjectVersion());

    generalInfo.setIsGradle(buildFileParser.isGradle());

    Map<String, List<String>> moduleVsDependency = buildFileParser.getDependencyList();
    List<String> analyzedModules = new ArrayList<>();

    log.debug("Module Iteration Start");
    for (String module : moduleVsDependency.keySet()) {

      if (!analyzedModules.contains(module)) {

        List<String> dependencies = moduleVsDependency.get(module);
        List<String> otherdependencies = new ArrayList<>();

        log.debug("Dependency Builder Start");
        for (String dependency : dependencies) {

          String dependencyModule;

          if (dependency.equals(generalInfo.getSource().get(0) + "/src/main/java"))
            dependencyModule =
                generalInfo
                    .getSource()
                    .get(0)
                    .substring(generalInfo.getSource().get(0).lastIndexOf("/") + 1);
          else
            dependencyModule =
                dependency.substring(
                    generalInfo.getSource().get(0).length() + 1, dependency.length() - 14);

          /* This is needed when the dependency path is relative*/
          //otherdependencies.add(dependency.substring(0, dependency.length() - 13) + generalInfo.getDependencies());
          otherdependencies.addAll(generalInfo.getDependencies());

          log.debug("Added the module: " + dependencyModule);
          analyzedModules.add(dependencyModule);
        }
        log.debug("Dependency Builder Stop");

        log.debug("Starting scanner looper");
        for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
          log.info("Checking the rule: " + ruleChecker.getClass().getSimpleName());
          ruleChecker.checkRule(
              EngineType.DIR,
              dependencies,
              otherdependencies,
              generalInfo.getSourcePaths(),
              generalInfo.getOutput(),
              generalInfo.getMain(),
              null,
              generalInfo.getJavaHome());
        }
        log.debug("Scanner looper stopped");

        NamedMethodMap.clearCallerCalleeGraph();
        FieldInitializationInstructionMap.reset();
      }
    }
    log.debug("Module Iteration Stop");
  }
}

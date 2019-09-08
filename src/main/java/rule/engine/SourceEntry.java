package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import lombok.extern.log4j.Log4j2;
import util.BuildFileParser;
import util.BuildFileParserFactory;
import util.FieldInitializationInstructionMap;
import util.NamedMethodMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>SourceEntry class.</p>
 *
 * @author CryptoguardTeam
 * Created on 2018-12-14.
 * @version 03.07.01
 * @since 01.01.06
 *
 * <p>The method in the Engine handling Source Scanning</p>
 */
@Log4j2
public class SourceEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {


        log.trace("Retrieving the specific project-based build parser.");
        BuildFileParser buildFileParser = BuildFileParserFactory.getBuildfileParser(generalInfo.getSource().get(0));
        log.debug("Using the build parser: " + buildFileParser.toString());

        log.trace("Setting the project name/version");
        generalInfo.setTargetProjectName(buildFileParser.getProjectName());
        generalInfo.setTargetProjectVersion(buildFileParser.getProjectVersion());

        generalInfo.setIsGradle(buildFileParser.isGradle());

        Map<String, List<String>> moduleVsDependency = buildFileParser.getDependencyList();
        List<String> analyzedModules = new ArrayList<>();

        log.trace("Module Iteration Start");
        for (String module : moduleVsDependency.keySet()) {

            if (!analyzedModules.contains(module)) {

                List<String> dependencies = moduleVsDependency.get(module);
                List<String> otherdependencies = new ArrayList<>();

                log.trace("Dependency Builder Start");
                for (String dependency : dependencies) {

                    String dependencyModule;

                    if (dependency.equals(generalInfo.getSource().get(0) + "/src/main/java"))
                        dependencyModule = generalInfo.getSource().get(0).substring(generalInfo.getSource().get(0).lastIndexOf("/") + 1);
                    else
                        dependencyModule = dependency.substring(generalInfo.getSource().get(0).length() + 1, dependency.length() - 14);

                    /* This is needed when the dependency path is relative*/
                    //otherdependencies.add(dependency.substring(0, dependency.length() - 13) + generalInfo.getDependencies());
                    otherdependencies.addAll(generalInfo.getDependencies());

                    log.debug("Added the module: " + dependencyModule);
                    analyzedModules.add(dependencyModule);
                }
                log.trace("Dependency Builder Stop");

                log.trace("Starting scanner looper");
                for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                    log.info("Checking the rule: " + ruleChecker.getClass().getSimpleName());
                    ruleChecker.checkRule(EngineType.DIR, dependencies, otherdependencies, generalInfo.getSourcePaths(), generalInfo.getOutput());
                }
                log.trace("Scanner looper stopped");

                NamedMethodMap.clearCallerCalleeGraph();
                FieldInitializationInstructionMap.reset();
            }
        }
        log.trace("Module Iteration Stop");
    }


}

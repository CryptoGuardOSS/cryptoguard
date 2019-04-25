package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
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
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @version $Id: $Id
 * @since 01.01.06
 *
 * <p>The method in the Engine handling Source Scanning</p>
 */
public class SourceEntry implements EntryHandler {

    /**
     * {@inheritDoc}
     */
    public void Scan(EnvironmentInformation generalInfo) throws ExceptionHandler {


        BuildFileParser buildFileParser = BuildFileParserFactory.getBuildfileParser(generalInfo.getSource().get(0));

        Map<String, List<String>> moduleVsDependency = buildFileParser.getDependencyList();
        List<String> analyzedModules = new ArrayList<>();

        for (String module : moduleVsDependency.keySet()) {

            if (!analyzedModules.contains(module)) {

                List<String> dependencies = moduleVsDependency.get(module);
                List<String> otherdependencies = new ArrayList<>();

                for (String dependency : dependencies) {

                    String dependencyModule;

                    if (dependency.equals(generalInfo.getSource().get(0) + "/src/main/java"))
                        dependencyModule = generalInfo.getSource().get(0).substring(generalInfo.getSource().get(0).lastIndexOf("/") + 1);
                    else
                        dependencyModule = dependency.substring(generalInfo.getSource().get(0).length() + 1, dependency.length() - 14);

                    /* This is needed when the dependency path is relative*/
                    //otherdependencies.add(dependency.substring(0, dependency.length() - 13) + generalInfo.getDependencies());
                    otherdependencies.addAll(generalInfo.getDependencies());

                    analyzedModules.add(dependencyModule);
                }

                for (RuleChecker ruleChecker : CommonRules.ruleCheckerList) {
                    ruleChecker.checkRule(EngineType.DIR, dependencies, otherdependencies, generalInfo.getSourcePaths(), generalInfo.getOutput());
                }

                NamedMethodMap.clearCallerCalleeGraph();
                FieldInitializationInstructionMap.reset();
            }
        }
    }


}

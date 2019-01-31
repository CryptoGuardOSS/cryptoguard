package main.rule.engine;

import main.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The point of entry for this library. This class instantiates all of the rules and
 * passes them to the entry point of the program for processing.
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public class RuleEngine {
    private static List<RuleChecker> ruleCheckerList = CommonRules.ruleCheckerList;

    /**
     * The point of entry for this library. Essentially the frontend, this will
     * handle all of the arguments and routing for the operation required.
     *
     * @param args the arguments passed in from being called from the command line
     * @throws java.lang.Exception throws exceptions in case of extreme error of being called.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.exit(1);
        }

        if (args[0].equals("jar")) {
            String projectJarPath = args[1];
            String projectDependencyPath = args[2];

            System.out.println("Analyzing JAR: " + projectJarPath);

            for (RuleChecker ruleChecker : ruleCheckerList) {
                ruleChecker.checkRule(EngineType.JAR, Arrays.asList(projectJarPath), Arrays.asList(projectDependencyPath), true);
            }

        } else if (args[0].equals("apk")) {
            String projectJarPath = args[1];

            System.out.println("Analyzing APK: " + projectJarPath);

            String basePackage = Utils.getBasePackageNameFromApk(projectJarPath);

            for (RuleChecker ruleChecker : ruleCheckerList) {
                ruleChecker.checkRule(EngineType.APK, Arrays.asList(projectJarPath), null, true);
            }
        } else if (args[0].equals("source")) {

            String projectRoot = args[1];
            String projectDependencyPath = args[2];

            System.out.println("Analyzing Project: " + projectRoot);

            BuildFileParser buildFileParser = BuildFileParserFactory.getBuildfileParser(projectRoot);

            Map<String, List<String>> moduleVsDependency = buildFileParser.getDependencyList();

            List<String> analyzedModules = new ArrayList<>();

            for (String module : moduleVsDependency.keySet()) {

                if (!analyzedModules.contains(module)) {

                    String output = "Analyzing Module: ";

                    List<String> dependencies = moduleVsDependency.get(module);
                    List<String> otherdependencies = new ArrayList<>();

                    for (String dependency : dependencies) {

                        String dependencyModule = null;

                        if (dependency.equals(projectRoot + "/src/main/java")) {
                            dependencyModule = projectRoot.substring(projectRoot.lastIndexOf("/") + 1);
                            otherdependencies.add(dependency.substring(0, dependency.length() - 13) + projectDependencyPath);

                        } else {
                            dependencyModule = dependency.substring(projectRoot.length() + 1, dependency.length() - 14);
                            otherdependencies.add(dependency.substring(0, dependency.length() - 13) + projectDependencyPath);

                        }

                        output += dependencyModule + ", ";
                        analyzedModules.add(dependencyModule);
                    }

                    System.out.println(output.substring(0, output.length() - 2));

                    for (RuleChecker ruleChecker : ruleCheckerList) {
                        ruleChecker.checkRule(EngineType.DIR, dependencies, otherdependencies, true);
                    }

                    NamedMethodMap.clearCallerCalleeGraph();
                    FieldInitializationInstructionMap.reset();
                }
            }
        }
    }
}

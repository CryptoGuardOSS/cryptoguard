package main.rule.engine;

import fj.function.Strings;
import main.rule.*;
import main.util.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RuleEngine {
    private static List<RuleChecker> ruleCheckerList = new ArrayList<>();

    static {

        ruleCheckerList.add(new InsecureAssymCryptoFinder());
        ruleCheckerList.add(new BrokenCryptoFinder());
        ruleCheckerList.add(new UntrustedPrngFinder());
        ruleCheckerList.add(new SSLSocketFactoryFinder());
        ruleCheckerList.add(new CustomTrustManagerFinder());
        ruleCheckerList.add(new HostNameVerifierFinder());
        ruleCheckerList.add(new BrokenHashFinder());
        ruleCheckerList.add(new ConstantKeyFinder());
        ruleCheckerList.add(new PredictableIVFinder());
        ruleCheckerList.add(new PBESaltFinder());
        ruleCheckerList.add(new PBEInterationCountFinder());
        ruleCheckerList.add(new PredictableSeedFinder());
        ruleCheckerList.add(new PredictableKeyStorePasswordFinder());
        ruleCheckerList.add(new HttpUrlFinder());
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.exit(1);
        }

        if (args[0].equals("jar")) {
            String projectJarPath = args[1];
            String projectDependencyPath = args[2];

            System.out.println("Analyzing JAR: " + projectJarPath);

            for (RuleChecker ruleChecker : ruleCheckerList) {
                ruleChecker.checkRule(EngineType.JAR, Arrays.asList(projectJarPath), Arrays.asList(projectDependencyPath));
            }

        } else if (args[0].equals("apk")) {
            String projectJarPath = args[1];

            System.out.println("Analyzing APK: " + projectJarPath);

            String basePackage = Utils.getBasePackageNameFromApk(projectJarPath);

            for (RuleChecker ruleChecker : ruleCheckerList) {
                ruleChecker.checkRule(EngineType.APK, Arrays.asList(projectJarPath), null);
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
                        ruleChecker.checkRule(EngineType.SOURCE, dependencies, otherdependencies);
                    }

                    NamedMethodMap.clearCallerCalleeGraph();
                    FieldInitializationInstructionMap.reset();
                }
            }
        }
    }
}

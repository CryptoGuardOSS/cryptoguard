package main.rule.engine;

import main.rule.*;
import main.util.*;

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

        Utils.initDepth(Integer.parseInt(args[3]));

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
            System.out.println("*** Base package: " + basePackage);

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

        System.out.println("Total Heuristics: " + Utils.NUM_HEURISTIC);
        System.out.println("Total Orthogonal: " + Utils.NUM_ORTHOGONAL);
        System.out.println("Total Constants: " + Utils.NUM_CONSTS_TO_CHECK);
        System.out.println("Total Slices: " + Utils.NUM_SLICES);
        System.out.println("Average Length: " + RuleEngine.calculateAverage(Utils.SLICE_LENGTH));

        for (int i = 0; i < Utils.DEPTH_COUNT.length; i++) {
            System.out.println(String.format("Depth: %d, Count %d", i + 1, Utils.DEPTH_COUNT[i]));
        }
    }

    private static double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}

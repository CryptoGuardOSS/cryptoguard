package main.rule.engine;

import main.rule.*;
import main.util.Utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        if (args.length < 2) {
            System.exit(1);
        }

        if (args[0].equals("jar")) {
            String projectJarPath = args[1];
            String projectDependencyPath = args[2];

            for (RuleChecker ruleChecker : ruleCheckerList) {
                ruleChecker.checkRule(EngineType.JAR, projectJarPath, projectDependencyPath);
            }

        } else if (args[0].equals("apk")) {
            String projectJarPath = args[1];

            String basePackage = Utils.getBasePackageNameFromApk(projectJarPath);
            System.out.println("*** Base package: " + basePackage);

            for (RuleChecker ruleChecker : ruleCheckerList) {
                ruleChecker.checkRule(EngineType.APK, projectJarPath, null);
            }
        } else if (args[0].equals("snippet")) {

            String projectJarPath = args[1];

            for (RuleChecker ruleChecker : ruleCheckerList) {
                ruleChecker.checkRule(EngineType.SNIPPET, projectJarPath, null);
            }

        }
    }
}

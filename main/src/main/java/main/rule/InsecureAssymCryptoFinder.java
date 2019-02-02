package main.rule;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>InsecureAssymCryptoFinder class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public class InsecureAssymCryptoFinder implements RuleChecker {

    private enum AssymType {
        RSA,
        EC;
    }

    private static final Map<AssymType, Integer> SIZE_MAP = new HashMap<>();
    private static final Map<AssymType, Boolean> IS_DEFAULT_SECURE_MAP = new HashMap<>();

    static {
        SIZE_MAP.put(AssymType.RSA, 2048);
        SIZE_MAP.put(AssymType.EC, 512);

        IS_DEFAULT_SECURE_MAP.put(AssymType.RSA, Boolean.FALSE);
        IS_DEFAULT_SECURE_MAP.put(AssymType.EC, Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<AnalysisIssue> checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, Boolean printOut, List<String> sourcePaths) throws IOException {

        ArrayList<AnalysisIssue> issues = printOut ? null : new ArrayList<AnalysisIssue>();
        ArrayList<AnalysisIssue> resultOne = checkAssym(type, projectJarPath, projectDependencyPath, AssymType.RSA, printOut, sourcePaths);
        ArrayList<AnalysisIssue> resultTwo = checkAssym(type, projectJarPath, projectDependencyPath, AssymType.EC, printOut, sourcePaths);

        if (!printOut) {
            issues.addAll(resultOne);
            issues.addAll(resultTwo);
        }

        return issues;

    }

    private ArrayList<AnalysisIssue> checkAssym(EngineType type,
                                                List<String> projectJarPath,
                                                List<String> projectDependencyPath,
                                                AssymType assymType, Boolean printOut, List<String> sourcePaths) throws IOException {

        ArrayList<AnalysisIssue> issues = printOut ? null : new ArrayList<AnalysisIssue>();

        List<String> cryptoType = new ArrayList<>();
        cryptoType.add("\"" + assymType.name() + "\"");

        AssymCryptoFinder assymCryptoFinder = new AssymCryptoFinder();
        DefaultExportGradeKeyFinder initializationFinder = new DefaultExportGradeKeyFinder();
        ExportGradeKeyInitializationFinder insecureInitializationFinder = new ExportGradeKeyInitializationFinder();

        assymCryptoFinder.setCrypto(cryptoType);
        ArrayList<AnalysisIssue> resultOne = assymCryptoFinder.checkRule(type, projectJarPath, projectDependencyPath, printOut, sourcePaths);

        ArrayList<String> foundSites = assymCryptoFinder.getOccurrenceSites();

        initializationFinder.setMethodsToLook(foundSites);
        initializationFinder.setDefaultSecure(IS_DEFAULT_SECURE_MAP.get(assymType));
        ArrayList<AnalysisIssue> resultTwo = initializationFinder.checkRule(type, projectJarPath, projectDependencyPath, printOut, sourcePaths);

        ArrayList<String> initializationCallsites = initializationFinder.getInitializationCallsites();

        insecureInitializationFinder.setInitializationCallsites(initializationCallsites);
        insecureInitializationFinder.setMinSize(SIZE_MAP.get(assymType));
        ArrayList<AnalysisIssue> resultThree = insecureInitializationFinder.checkRule(type, projectJarPath, projectDependencyPath, printOut, sourcePaths);

        if (!printOut) {
            issues.addAll(resultOne);
            issues.addAll(resultTwo);
            issues.addAll(resultThree);
        }

        return issues;
    }
}

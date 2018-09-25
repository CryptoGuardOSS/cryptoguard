package main.rule;

import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Override
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath) throws IOException {

        checkAssym(type, projectJarPath, projectDependencyPath, AssymType.RSA);
        checkAssym(type, projectJarPath, projectDependencyPath, AssymType.EC);

    }

    private void checkAssym(EngineType type,
                            List<String> projectJarPath,
                            List<String> projectDependencyPath,
                            AssymType assymType) throws IOException {

        List<String> cryptoType = new ArrayList<>();
        cryptoType.add("\"" + assymType.name() + "\"");

        AssymCryptoFinder assymCryptoFinder = new AssymCryptoFinder();
        DefaultExportGradeKeyFinder initializationFinder = new DefaultExportGradeKeyFinder();
        ExportGradeKeyInitializationFinder insecureInitializationFinder = new ExportGradeKeyInitializationFinder();

        assymCryptoFinder.setCrypto(cryptoType);
        assymCryptoFinder.checkRule(type, projectJarPath, projectDependencyPath);

        ArrayList<String> foundSites = assymCryptoFinder.getOccurrenceSites();

        initializationFinder.setMethodsToLook(foundSites);
        initializationFinder.setDefaultSecure(IS_DEFAULT_SECURE_MAP.get(assymType));
        initializationFinder.checkRule(type, projectJarPath, projectDependencyPath);

        ArrayList<String> initializationCallsites = initializationFinder.getInitializationCallsites();

        insecureInitializationFinder.setInitializationCallsites(initializationCallsites);
        insecureInitializationFinder.setMinSize(SIZE_MAP.get(assymType));
        insecureInitializationFinder.checkRule(type, projectJarPath, projectDependencyPath);
    }
}

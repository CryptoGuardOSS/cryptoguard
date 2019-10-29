package rule;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import rule.engine.EngineType;
import rule.engine.RuleChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>InsecureAssymCryptoFinder class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class InsecureAssymCryptoFinder implements RuleChecker {

    private enum AssymType {
        RSA,
        EC
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
    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, List<String> sourcePaths, OutputStructure output, String mainKlass) throws ExceptionHandler {

        checkAssym(type, projectJarPath, projectDependencyPath, AssymType.RSA, sourcePaths, output, mainKlass);
        checkAssym(type, projectJarPath, projectDependencyPath, AssymType.EC, sourcePaths, output, mainKlass);

    }

    private void checkAssym(EngineType type,
                            List<String> projectJarPath,
                            List<String> projectDependencyPath,
                            AssymType assymType, List<String> sourcePaths, OutputStructure output, String mainKlass) throws ExceptionHandler {

        List<String> cryptoType = new ArrayList<>();
        cryptoType.add("\"" + assymType.name() + "\"");

        AssymCryptoFinder assymCryptoFinder = new AssymCryptoFinder();
        DefaultExportGradeKeyFinder initializationFinder = new DefaultExportGradeKeyFinder();
        ExportGradeKeyInitializationFinder insecureInitializationFinder = new ExportGradeKeyInitializationFinder();

        assymCryptoFinder.setCrypto(cryptoType);
        assymCryptoFinder.checkRule(type, projectJarPath, projectDependencyPath, sourcePaths, output, mainKlass);

        ArrayList<String> foundSites = assymCryptoFinder.getOccurrenceSites();

        initializationFinder.setMethodsToLook(foundSites);
        initializationFinder.setDefaultSecure(IS_DEFAULT_SECURE_MAP.get(assymType));
        initializationFinder.checkRule(type, projectJarPath, projectDependencyPath, sourcePaths, output, mainKlass);

        ArrayList<String> initializationCallsites = initializationFinder.getInitializationCallsites();

        insecureInitializationFinder.setInitializationCallsites(initializationCallsites);
        insecureInitializationFinder.setMinSize(SIZE_MAP.get(assymType));
        insecureInitializationFinder.checkRule(type, projectJarPath, projectDependencyPath, sourcePaths, output, mainKlass);

    }
}

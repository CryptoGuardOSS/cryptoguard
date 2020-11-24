/* Licensed under GPL-3.0 */
package rule;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rule.engine.EngineType;
import rule.engine.RuleChecker;

/**
 * InsecureAssymCryptoFinder class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class InsecureAssymCryptoFinder implements RuleChecker {

  private static final Map<AssymType, Integer> SIZE_MAP = new HashMap<>();
  private static final Map<AssymType, Boolean> IS_DEFAULT_SECURE_MAP = new HashMap<>();

  static {
    SIZE_MAP.put(AssymType.RSA, 2048);
    SIZE_MAP.put(AssymType.EC, 512);

    IS_DEFAULT_SECURE_MAP.put(AssymType.RSA, Boolean.FALSE);
    IS_DEFAULT_SECURE_MAP.put(AssymType.EC, Boolean.TRUE);
  }

  /** {@inheritDoc} */
  @Override
  public void checkRule(
      EngineType type,
      List<String> projectJarPath,
      List<String> projectDependencyPath,
      List<String> sourcePaths,
      OutputStructure output,
      String mainKlass,
      String androidHome,
      String javaHome)
      throws ExceptionHandler {

    checkAssym(
        type,
        projectJarPath,
        projectDependencyPath,
        AssymType.RSA,
        sourcePaths,
        output,
        mainKlass,
        androidHome,
        javaHome);
    checkAssym(
        type,
        projectJarPath,
        projectDependencyPath,
        AssymType.EC,
        sourcePaths,
        output,
        mainKlass,
        androidHome,
        javaHome);
  }

  private void checkAssym(
      EngineType type,
      List<String> projectJarPath,
      List<String> projectDependencyPath,
      AssymType assymType,
      List<String> sourcePaths,
      OutputStructure output,
      String mainKlass,
      String androidHome,
      String javaHome)
      throws ExceptionHandler {

    List<String> cryptoType = new ArrayList<>();
    cryptoType.add("\"" + assymType.name() + "\"");

    AssymCryptoFinder assymCryptoFinder = new AssymCryptoFinder();
    DefaultExportGradeKeyFinder initializationFinder = new DefaultExportGradeKeyFinder();
    ExportGradeKeyInitializationFinder insecureInitializationFinder =
        new ExportGradeKeyInitializationFinder();

    assymCryptoFinder.setCrypto(cryptoType);
    assymCryptoFinder.checkRule(
        type,
        projectJarPath,
        projectDependencyPath,
        sourcePaths,
        output,
        mainKlass,
        androidHome,
        javaHome);

    ArrayList<String> foundSites = assymCryptoFinder.getOccurrenceSites();

    initializationFinder.setMethodsToLook(foundSites);
    initializationFinder.setDefaultSecure(IS_DEFAULT_SECURE_MAP.get(assymType));
    initializationFinder.checkRule(
        type,
        projectJarPath,
        projectDependencyPath,
        sourcePaths,
        output,
        mainKlass,
        androidHome,
        javaHome);

    ArrayList<String> initializationCallsites = initializationFinder.getInitializationCallsites();

    insecureInitializationFinder.setInitializationCallsites(initializationCallsites);
    insecureInitializationFinder.setMinSize(SIZE_MAP.get(assymType));
    insecureInitializationFinder.checkRule(
        type,
        projectJarPath,
        projectDependencyPath,
        sourcePaths,
        output,
        mainKlass,
        androidHome,
        javaHome);
  }

  private enum AssymType {
    RSA,
    EC
  }
}

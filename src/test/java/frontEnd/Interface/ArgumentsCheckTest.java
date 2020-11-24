/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static test.TestUtilities.*;
import static util.Utils.makeArg;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import util.Utils;

/**
 * ArgumentsCheckTest class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class ArgumentsCheckTest {

  //region Attributes
  private final String fileOut = Utils.osPathJoin(testPath, "txt.xml");
  private final String fileOutTxt = Utils.osPathJoin(testPath, "txt.txt");
  private final String fileOutJson = Utils.osPathJoin(testPath, "txt.json");
  //endregion

  //region Test Environment Setup
  @Before
  public void setup() {
    File running = null;
    try {
      if (!(running = new File(fileOut)).exists()) running.createNewFile();
      if (!(running = new File(fileOutTxt)).exists()) running.createNewFile();
      if (!(running = new File(fileOutJson)).exists()) running.createNewFile();
    } catch (IOException e) {
    }
  }
  //endregion

  //region Tests

  /** testEnvironmentVariables. */
  @Test
  public void testEnvironmentVariables() {
    String[] fileLists = new String[] {jarOne};
    String[] dirLists = new String[] {srcOneGrv, srcOneGrvDep};

    for (String file : fileLists) {
      File tempFile = new File(file);

      assertTrue(tempFile.exists());
      assertTrue(tempFile.isFile());
    }

    for (String dir : dirLists) {
      File tempDir = new File(dir);

      assertTrue(tempDir.exists());
      assertTrue(tempDir.isDirectory());
    }
  }

  @Test
  public void parameterCheck_verifyingJavaSevenHome() {
    String fileOut = tempFileOutApk_Scarf;
    String javaHome = System.getenv("JAVA7_HOME");

    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.JAVA, javaHome)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.PRETTY);

      try {
        EnvironmentInformation generalInfo =
            ArgumentsCheck.paramaterCheck(Utils.stripEmpty(args.split(" ")));

        assertEquals(javaHome, generalInfo.getJavaHome());
      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void parameterCheck_verifyingJavaAndroidHome() {
    String fileOut = tempFileOutApk_Scarf;
    String javaHome = System.getenv("JAVA_HOME");
    String androidHome = System.getenv("ANDROID_HOME");

    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.ANDROID, androidHome)
              + makeArg(argsIdentifier.JAVA, javaHome)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.PRETTY);

      try {
        EnvironmentInformation generalInfo =
            ArgumentsCheck.paramaterCheck(Utils.stripEmpty(args.split(" ")));

        assertEquals(javaHome, generalInfo.getJavaHome());
        assertEquals(androidHome, generalInfo.getAndroidHome());
      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void parameterCheck_VersionOut() {
    String args = makeArg(argsIdentifier.VERSION) + makeArg(argsIdentifier.NOEXIT);

    try {
      String outputFile = captureNewFileOutViaStdOut(args.split(" "));

      assertEquals(Utils.projectName + ": " + Utils.projectVersion, outputFile);
    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }
  }

  @Test
  public void parameterCheck_HelpOut() {
    String args = makeArg(argsIdentifier.HELP) + makeArg(argsIdentifier.NOEXIT);

    try {
      String outputFile = captureNewFileOutViaStdOut(args.split(" "));

      assertNotNull(outputFile);
    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }
  }

  /** paramaterCheck_jar. */
  @Test
  public void paramaterCheck_jar_enhancedInputFile() {

    String args =
        makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
            + makeArg(argsIdentifier.FORMATOUT, Listing.Legacy)
            + makeArg(argsIdentifier.SOURCE, srcOneGrvInputFile)
            + makeArg(argsIdentifier.PRETTY)
            + makeArg(argsIdentifier.OUT, tempTestJJava_Txt);

    EnvironmentInformation info = null;

    try {
      info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }

    assertNotNull(info);
    assertEquals(EngineType.JAVAFILES, info.getSourceType());
    assertEquals(10, info.getSource().size());
    try {
      BufferedReader reader = new BufferedReader(new FileReader(srcOneGrvInputFile));
      String curLine = null;

      ArrayList<String> sourceFiles = new ArrayList<>();
      for (String in : info.getSource()) sourceFiles.add(in.replace(basePath, "."));

      while ((curLine = reader.readLine()) != null) assertTrue(sourceFiles.remove(curLine));

      assertTrue(sourceFiles.isEmpty());

    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }
    assertTrue(info.getPrettyPrint());
    assertEquals(tempTestJJava_Txt, info.getFileOut());
    assertEquals(Listing.Legacy, info.getMessagingType());
  }

  /** paramaterCheck_jar_SkipValidation. */
  @Test
  public void paramaterCheck_jar_SkipValidation() {

    String args =
        makeArg(argsIdentifier.FORMAT, EngineType.JAR)
            + makeArg(argsIdentifier.SOURCE, jarOne)
            + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
            + makeArg(argsIdentifier.OUT, fileOutJson)
            + makeArg(argsIdentifier.TIMEMEASURE)
            + makeArg(argsIdentifier.PRETTY);

    EnvironmentInformation info = null;

    try {
      info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }

    assertNotNull(info);
    assertEquals(EngineType.JAR, info.getSourceType());
    assertEquals(1, info.getSource().size());
    assertEquals(jarOne, info.getSource().get(0));
    assertEquals(1, info.getDependencies().size());
    assertEquals(srcOneGrvDep, info.getDependencies().get(0));
    assertTrue(info.isShowTimes());
    assertTrue(info.getPrettyPrint());
    assertEquals(fileOutTxt.replace(".txt", ".json"), info.getFileOut());
    assertEquals(Listing.Default, info.getMessagingType());
  }

  /** paramaterCheck_jar. */
  @Test
  public void paramaterCheck_jar() {

    String args =
        makeArg(argsIdentifier.FORMAT, EngineType.JAR)
            + makeArg(argsIdentifier.SOURCE, jarOne)
            + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
            + makeArg(argsIdentifier.OUT, fileOut)
            + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
            + makeArg(argsIdentifier.TIMEMEASURE)
            + makeArg(argsIdentifier.PRETTY);

    EnvironmentInformation info = null;

    try {
      info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }

    assertNotNull(info);
    assertEquals(EngineType.JAR, info.getSourceType());
    assertEquals(1, info.getSource().size());
    assertEquals(jarOne, info.getSource().get(0));
    assertEquals(1, info.getDependencies().size());
    assertEquals(srcOneGrvDep, info.getDependencies().get(0));
    assertTrue(info.isShowTimes());
    assertTrue(info.getPrettyPrint());
    assertEquals(fileOut, info.getFileOut());
    assertEquals(Listing.ScarfXML, info.getMessagingType());
  }

  /** paramaterCheck_Barejar. */
  @Test
  public void paramaterCheck_Barejar() {

    String args =
        makeArg(argsIdentifier.FORMAT, EngineType.JAR) + makeArg(argsIdentifier.SOURCE, jarOne);

    EnvironmentInformation info = null;

    try {
      info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }

    assertNotNull(info);
    assertEquals(EngineType.JAR, info.getSourceType());
    assertEquals(1, info.getSource().size());
    assertEquals(jarOne, info.getSource().get(0));
    assertFalse(info.isShowTimes());
    assertFalse(info.getPrettyPrint());
    assertEquals(Listing.Default, info.getMessagingType());
  }

  /** parameterCheck_gdl. */
  @Test
  public void parameterCheck_gdl() {

    String args =
        makeArg(argsIdentifier.FORMAT, EngineType.DIR)
            + makeArg(argsIdentifier.SOURCE, srcOneGrv)
            + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
            + makeArg(argsIdentifier.OUT, fileOut)
            + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
            + makeArg(argsIdentifier.TIMEMEASURE)
            + makeArg(argsIdentifier.PRETTY);

    EnvironmentInformation info = null;

    try {
      info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);
    }

    assertNotNull(info);
    assertEquals(EngineType.DIR, info.getSourceType());
    assertEquals(1, info.getSource().size());
    assertEquals(srcOneGrv, info.getSource().get(0));
    assertEquals(1, info.getDependencies().size());
    assertEquals(srcOneGrvDep, info.getDependencies().get(0));
    assertTrue(info.isShowTimes());
    assertTrue(info.getPrettyPrint());
    assertEquals(fileOut, info.getFileOut());
    assertEquals(Listing.ScarfXML, info.getMessagingType());
  }
  //endregion
}

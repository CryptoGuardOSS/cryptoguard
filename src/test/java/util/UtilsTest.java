/* Licensed under GPL-3.0 */
package util;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;
import static util.Utils.join;
import static util.Utils.trimFilePath;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.TestUtilities;

/**
 * UtilsTest class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class UtilsTest {

  //region Attributes
  private String fullJavaClassFile;
  private String fullJavaFile;
  private String fileSep = System.getProperty("file.separator");

  //endregion

  //region Test Environment

  /**
   * setUp.
   *
   * @throws java.lang.Exception if any.
   */
  @Before
  public void setUp() throws Exception {

    fullJavaFile =
        String.join(fileSep, "src", "main", "java", "com", "full", "fun", "test", "main.java");
    fullJavaClassFile =
        String.join(fileSep, "target", "main", "java", "com", "full", "fun", "test", "main.class");
  }

  /**
   * tearDown.
   *
   * @throws java.lang.Exception if any.
   */
  @After
  public void tearDown() throws Exception {
    fullJavaFile = null;
    fullJavaClassFile = null;
  }
  //endregion

  //region Tests

  /** trimFilePathTestOne. */
  @Test
  public void trimFilePathTestOne() {
    String fileNameExt = trimFilePath(fullJavaFile);

    assertNotNull(fileNameExt);
    assertEquals("main.java", fileNameExt);
  }

  /** trimFilePathTestTwo. */
  @Test
  public void trimFilePathTestTwo() {
    String fileNameExt = trimFilePath(fullJavaClassFile);

    assertNotNull(fileNameExt);
    assertEquals("main.class", fileNameExt);
  }

  @Test
  public void testVerifyClassPathsOne() {
    try {
      ArrayList<String> returnedOutput =
          Utils.retrieveFilePathTypes(
              TestUtilities.sampleAuxClassPathOne, null, false, false, true);
      for (String output : returnedOutput)
        assertTrue(TestUtilities.sampleAuxClassPathOneList.contains(output));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertNull(e);
    }
  }

  @Test
  public void testVerifyClassPathsTwo() {
    try {
      ArrayList<String> returnedOutput =
          Utils.retrieveFilePathTypes(
              TestUtilities.sampleAuxClassPathTwo, null, false, false, true);
      TestUtilities.sampleAuxClassPathTwoList.forEach(
          str -> assertTrue(returnedOutput.contains(str)));
      assertEquals(TestUtilities.sampleAuxClassPathTwoList.size(), returnedOutput.size());
    } catch (ExceptionHandler e) {
      assertEquals(e.getErrorCode(), ExceptionId.ARG_VALID);
      assertTrue(e.getLongDesciption().contains(TestUtilities.scarfArgs));
    }
  }

  @Test
  public void test_retrieveFullyQualifiedPath_Single() {
    String testFile = classFiles[0];
    String base =
        join(".", "tester", classFiles[0].replace(classSource + "/", "").replace(".class", ""));

    try {
      String fullyQualifiedName = Utils.retrieveFullyQualifiedName(testFile);
      assertEquals(base, fullyQualifiedName);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertNull(e);
    }
  }

  @Test
  public void test_retrievePackageFromJavaFiles() {
    String testFile = pracitceJavaPackage;
    String pkg = "tester/Crypto_VeryTemp.java";

    try {
      String fullyQualifiedName = Utils.retrievePackageFromJavaFiles(testFile);
      assertEquals(pkg, fullyQualifiedName);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertNull(e);
    }
  }

  @Test
  public void test_retrieveFullyQualifiedName() {
    String testFile = pracitceJavaPackage;
    String pkg = "tester" + ".Crypto_VeryTemp";

    try {
      String fullyQualifiedName = Utils.retrieveFullyQualifiedName(testFile);
      assertEquals(pkg, fullyQualifiedName);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      assertNull(e);
    }
  }
  //endregion
}

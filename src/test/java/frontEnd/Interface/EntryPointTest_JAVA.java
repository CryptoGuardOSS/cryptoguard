/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;
import static util.Utils.makeArg;

import com.binarytweed.test.Quarantine;
import com.binarytweed.test.QuarantiningRunner;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import rule.engine.EngineType;
import soot.G;
import util.Utils;

/**
 * EntryPointTest_JAVA class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
@RunWith(QuarantiningRunner.class)
@Quarantine({"com.binarytweed", "frontEnd.Interface"})
public class EntryPointTest_JAVA {

  //region Test Environment Setup
  private static final Boolean activate = false;
  /**
   * setUp.
   *
   * @throws java.lang.Exception if any.
   */
  @Before
  public void setUp() throws Exception {
    //Cleaning the current scene since setup carries throughout the VM
    //tldr - one test setting up the scene will carry over to the next test, this'll stop that
    G.reset();
    //endregion
  }

  /**
   * tearDown.
   *
   * @throws java.lang.Exception if any.
   */
  @After
  public void tearDown() throws Exception {}
  //endregion

  //region Tests

  @Test
  public void main_TestableFile_VerySimple() {
    soot.G.v().reset();
    String source = verySimple_Gradle_File;
    String fileOut = tempFileOutJson_File;
    new File(fileOut).delete();

    if (activate && isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        Report report = Report.deserialize(new File(outputFile));

        assertFalse(report.getIssues().isEmpty());
        assertTrue(
            report
                .getIssues()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                            .getFullPath()
                            .contains(Utils.retrieveFullyQualifiedName(source));
                      } catch (ExceptionHandler exceptionHandler) {
                        exceptionHandler.printStackTrace();
                        return false;
                      }
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_Crypto_Example_File() {
    soot.G.v().reset();
    String source = crypto_Example_File;
    String fileOut = crypto_Example_Json_File;
    new File(fileOut).delete();

    if (activate && isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        Report report = Report.deserialize(new File(outputFile));

        assertFalse(report.getIssues().isEmpty());
        assertTrue(
            report
                .getIssues()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                            .getFullPath()
                            .contains(Utils.retrieveFullyQualifiedName(source));
                      } catch (ExceptionHandler exceptionHandler) {
                        exceptionHandler.printStackTrace();
                        return false;
                      }
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_PasswordUtils_Example_File() {
    soot.G.v().reset();
    String source = passwordUtils_Example_File;
    String fileOut = passwordUtils_Example_Json_File;
    new File(fileOut).delete();

    if (activate && isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        Report report = Report.deserialize(new File(outputFile));

        assertFalse(report.getIssues().isEmpty());
        assertTrue(
            report
                .getIssues()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                            .getFullPath()
                            .contains(Utils.retrieveFullyQualifiedName(source));
                      } catch (ExceptionHandler exceptionHandler) {
                        exceptionHandler.printStackTrace();
                        return false;
                      }
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_SymCrypto_Example_File() {
    soot.G.v().reset();
    String source = symCrypto_Example_File;
    String fileOut = symCrypto_Example_Json_File;
    new File(fileOut).delete();

    if (activate && isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        Report report = Report.deserialize(new File(outputFile));

        assertFalse(report.getIssues().isEmpty());
        assertTrue(
            report
                .getIssues()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                            .getFullPath()
                            .contains(Utils.retrieveFullyQualifiedName(source));
                      } catch (ExceptionHandler exceptionHandler) {
                        exceptionHandler.printStackTrace();
                        return false;
                      }
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_SymCrypto_Example_Package_File() {
    soot.G.v().reset();
    String source = symCrypto_Example_Package_File;

    String className = source;
    try {
      className = Utils.retrieveFullyQualifiedName(source);
    } catch (ExceptionHandler e) {
    }

    String fileOut = symCrypto_Example_Package_Json_File;
    new File(fileOut).delete();

    if (activate && isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        Report report = Report.deserialize(new File(outputFile));

        assertFalse(report.getIssues().isEmpty());
        System.out.println(report.getIssues());
        String finalClassName = className.replaceAll("\\.", "/");
        assertTrue(
            report
                .getIssues()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                            .getFullPath()
                            .contains(Utils.retrieveFullyQualifiedName(finalClassName));
                      } catch (ExceptionHandler exceptionHandler) {
                        exceptionHandler.printStackTrace();
                        return false;
                      }
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_SymCrypto_Multiple_File() {
    soot.G.v().reset();
    String source_1 = symCrypto_Multiple_Example_File_1;
    String source_2 = symCrypto_Multiple_Example_File_2;
    String fileOut = symCrypto_Multiple_Files;
    new File(fileOut).delete();

    if (activate && isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.SOURCE, Utils.join(":", source_1, source_2))
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        Report report = Report.deserialize(new File(outputFile));

        assertFalse(report.getIssues().isEmpty());
        assertTrue(
            report
                .getIssues()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                                .getFullPath()
                                .contains(Utils.retrieveFullyQualifiedName(source_1))
                            || bugInstance
                                .getFullPath()
                                .contains(Utils.retrieveFullyQualifiedName(source_2));
                      } catch (ExceptionHandler exceptionHandler) {
                        exceptionHandler.printStackTrace();
                        return false;
                      }
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_SymCrypto_Example_File_Failure_Test() {
    soot.G.v().reset();
    String source = symCrypto_Example_File;
    String fileOut = symCrypto_Example_Json_File;
    new File(fileOut).delete();

    if (activate && isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));

        assertFalse(report.getBugInstance().isEmpty());
        assertTrue(
            report
                .getBugInstance()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                            .getClassName()
                            .contains(Utils.retrieveFullyQualifiedName(source));
                      } catch (ExceptionHandler exceptionHandler) {
                        exceptionHandler.printStackTrace();
                        return false;
                      }
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  //endregion
}

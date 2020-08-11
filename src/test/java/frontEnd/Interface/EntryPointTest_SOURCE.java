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
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import rule.engine.EngineType;
import soot.G;
import util.Utils;

/**
 * EntryPointTest_SOURCE class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
@RunWith(QuarantiningRunner.class)
@Quarantine({"com.binarytweed", "frontEnd.Interface"})
public class EntryPointTest_SOURCE {

  //region Attributes
  //endregion

  //region Test Environment Setup

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
  public void main_VerySimpleGradleProject() {
    String fileOut = tempFileOutJSON;
    String source = verySimple_Gradle;

    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.DIR)
              + makeArg(argsIdentifier.SOURCE, verySimple_Gradle)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 10);

        List<String> filesFound =
            Utils.retrieveFilesPredicate(
                verySimple_Gradle,
                s -> s.endsWith(".java"),
                file -> {
                  try {
                    return Utils.retrieveFullyQualifiedName(file.getAbsolutePath()) + ".java";
                  } catch (ExceptionHandler exceptionHandler) {
                    exceptionHandler.printStackTrace();
                    return null;
                  }
                },
                false);

        assertTrue(results.stream().anyMatch(str -> filesFound.stream().anyMatch(str::contains)));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_TestableJarSource() {
    String fileOut = tempFileOutTxt;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.DIR)
              + makeArg(argsIdentifier.SOURCE, srcOneGrv)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.FORMATOUT, Listing.Legacy)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 10);

        List<String> filesFound =
            Utils.retrieveFilesPredicate(
                srcOneGrv,
                s -> s.endsWith(".java"),
                file -> {
                  try {
                    return Utils.retrieveFullyQualifiedName(file.getAbsolutePath()) + ".java";
                  } catch (ExceptionHandler exceptionHandler) {
                    exceptionHandler.printStackTrace();
                    return null;
                  }
                },
                false);

        assertTrue(results.stream().anyMatch(str -> filesFound.stream().anyMatch(str::contains)));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_TestableJarSourceScarf() {
    String fileOut = tempFileOutXML;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.DIR)
              + makeArg(argsIdentifier.SOURCE, srcOneGrv)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());
        assertTrue(
            report
                .getBugInstance()
                .stream()
                .anyMatch(bugInstance -> bugInstance.getClassName().startsWith(srcOneGrv_base)));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_TestableJarSourceScarf_SpecifyHome() {
    String fileOut = tempFileOutXML;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.DIR)
              + makeArg(argsIdentifier.SOURCE, srcOneGrv)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.JAVA, System.getenv("JAVA7_HOME"))
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());
        assertTrue(
            report
                .getBugInstance()
                .stream()
                .anyMatch(bugInstance -> bugInstance.getClassName().startsWith(srcOneGrv_base)));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_TestableJarSourceScarf_Stream() {
    String fileOut = tempStreamXML;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.DIR)
              + makeArg(argsIdentifier.SOURCE, srcOneGrv)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.STREAM);
      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());
        assertTrue(
            report
                .getBugInstance()
                .stream()
                .anyMatch(bugInstance -> bugInstance.getClassName().startsWith(srcOneGrv_base)));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  //endregion
}

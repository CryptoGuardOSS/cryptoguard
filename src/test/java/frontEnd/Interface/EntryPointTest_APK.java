/* Licensed under GPL-3.0 */
package frontEnd.Interface;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static test.TestUtilities.*;
import static util.Utils.makeArg;

import com.binarytweed.test.Quarantine;
import com.binarytweed.test.QuarantiningRunner;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
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

/**
 * EntryPointTest_APK class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
@RunWith(QuarantiningRunner.class)
@Quarantine({"com.binarytweed", "frontEnd.Interface.*"})
public class EntryPointTest_APK {

  //region Attributes
  private EntryPoint engine;

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
  public void tearDown() throws Exception {
    engine = null;
  }
  //endregion

  //region Tests

  /** main_TestableApk. */
  @Test
  public void main_TestableApk_Legacy() {
    String fileOut = tempFileOutApk;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Legacy)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 10);

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk. */
  @Test
  public void main_TestableApk_Legacy_Stream() {
    String fileOut = tempFileOutApk_Steam;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Legacy)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.STREAM);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 10);

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk. */
  @Test
  public void main_TestableApk_CSV() {
    String fileOut = tempFileOutApk_CSV;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.FORMATOUT, Listing.CSVDefault)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 10);

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk. */
  @Test
  public void main_TestableApk_CSV_Stream() {
    String fileOut = tempFileOutApk_CSVStream;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.FORMATOUT, Listing.CSVDefault)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.STREAM);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 10);

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk. */
  //@Test
  public void main_TestableApk_YAML_Stream() {
    String fileOut = tempFileOutApk_YAMLStream;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              +
              //makeArg(argsIdentifier.FORMATOUT, Listing.YDefault) +
              makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.STREAM);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 10);

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_TestableApk_Scarf_XArgs() {
    String fileOut = tempFileOutApk_Scarf_XArgs;
    new File(fileOut).delete();

    String dataInput = pathToAPK;
    TestUtilities.setIn(dataInput);

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, "xargs")
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.PRETTY);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk_Scarf. */
  @Test
  public void main_TestableApk_Scarf() {
    String fileOut = tempFileOutApk_Scarf;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.PRETTY);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_TestableApk_Scarf_SpecifyAndroidHome() {
    String fileOut = tempFileOutApk_Scarf;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.ANDROID, System.getenv("ANDROID_HOME"))
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.PRETTY);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  @Test
  public void main_TestableApk_Scarf_SpecifyHome() {
    String fileOut = tempFileOutApk_Scarf;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.ANDROID, System.getenv("ANDROID_HOME"))
              + makeArg(argsIdentifier.JAVA, System.getenv("JAVA_HOME"))
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.PRETTY);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk_Scarf. */
  @Test
  public void main_TestableApk_Scarf_Stream() {
    String fileOut = tempFileOutApk_Scarf_Steam;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.STREAM);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk_Scarf. */
  @Test
  public void main_TestableApk_Default() {
    String fileOut = tempFileOutApk_Default;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.FORMATOUT, Listing.Default)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.PRETTY);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        Report report = Report.deserialize(new File(outputFile));
        assertFalse(report.getIssues().isEmpty());

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk_Scarf. */
  @Test
  public void main_TestableApk_Default_Stream() {
    String fileOut = tempFileOutApk_Scarf_Steam;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.STREAM);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableApk_Scarf. */
  @Test
  public void main_TestableApk_Default_Stream_Defensive_0() {

    String fileOut = tempFileOutApk_Scarf_Steam;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.APK)
              + makeArg(argsIdentifier.SOURCE, pathToAPK)
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.JAVA, System.getProperty("user.home"))
              + makeArg(argsIdentifier.STREAM);

      assertEquals(null, captureNewFileOutViaStdOut(args.split(" "), true));
    }
  }

  //endregion
}

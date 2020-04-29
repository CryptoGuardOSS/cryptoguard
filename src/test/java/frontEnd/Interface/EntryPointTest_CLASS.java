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
import frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance;
import frontEnd.argsIdentifier;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import rule.engine.EngineType;
import soot.G;
import test.TestUtilities;
import util.Utils;

/**
 * EntryPointTest_CLASS class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
@RunWith(QuarantiningRunner.class)
@Quarantine({"com.binarytweed", "frontEnd.Interface.*"})
public class EntryPointTest_CLASS {

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
  public void main_TestableFile_VerySimple() {
    soot.G.v().reset();
    String source = verySimple_Klass;
    String fileOut = verySimple_Klass_xml_1;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.STREAM)
              + makeArg(argsIdentifier.PRETTY)
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
        assertFalse(report.getBugInstance().isEmpty());

        List<String> style = new ArrayList<>();
        for (BugInstance instance : report.getBugInstance()) style.add(instance.getClassName());

        assertTrue(
            report
                .getBugInstance()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return bugInstance
                                .getClassName()
                                .contains(Utils.retrieveFullyQualifiedName(source))
                            || bugInstance
                                .getlocation()
                                .get(0)
                                .getSourceFile()
                                .startsWith(Utils.retrieveFullyQualifiedName(source));
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

  /** main_TestableFiles_SingleTest. */
  @Test
  public void main_TestableFiles_SingleTest() {
    soot.G.v().reset();
    soot.G.reset();

    String source = testablejar_Crypto_class;
    String fileOut = testablejar_Crypto_class_xml_0;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
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

  @Test
  public void main_TestableFile_NewTestCaseTwo() {
    String fileOut = newTestCaseTwo_xml_0;
    String source = newTestCaseTwo_Class;
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
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

  /** main_TestableFiles_MultiTest. */
  @Test
  public void main_TestableFiles_MultiTest() {
    String fileOut = tempFileOutTxt_two_0;
    String source = Utils.join(" ", TestUtilities.arr(classFiles));
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
              + makeArg(argsIdentifier.SOURCE, Utils.join(" ", classFiles))
              + makeArg(argsIdentifier.FORMATOUT, Listing.Legacy)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.MAIN, classFiles[3])
              + makeArg(argsIdentifier.OUT, fileOut);

      try {
        String outputFile = captureNewFileOutViaStdOut(args.split(" "));

        List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
        assertTrue(results.size() >= 2);

        assertTrue(results.stream().anyMatch(line -> Utils.containsAny(line, source.split(" "))));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }

  /** main_TestableFiles_MultiTest_Scarf. */
  @Test
  public void main_TestableFiles_MultiTest_Scarf() {
    String fileOut = tempFileOutXML_Class_0;
    String source = String.join(" ", classFiles);
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.MAIN, classFiles[3])
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.NOEXIT)
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
                        return Utils.containsAny(
                            bugInstance.getClassName(),
                            Utils.retrieveFullyQualifiedName(source.split(" ")));
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

  /** main_TestableFiles_MultiTest_Scarf. */
  @Test
  public void main_TestableFiles_MultiTest_Scarf_ClassPath() {
    String fileOut = tempFileOutXML_Class_1;
    String source = String.join(":", classFiles);
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.MAIN, classFiles[3])
              + makeArg(argsIdentifier.VERYVERBOSE)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.NOEXIT)
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
                        return Utils.containsAny(
                            bugInstance.getClassName(),
                            Utils.retrieveFullyQualifiedName(source.split(":")));
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

  /** main_TestableFiles_MultiTest_Scarf_Stream. */
  @Test
  public void main_TestableFiles_MultiTest_Scarf_Stream() {
    String fileOut = tempFileOutXML_Class_Stream_0;
    String source = String.join(" ", classFiles);
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.MAIN, classFiles[3])
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.OUT, fileOut)
              + makeArg(argsIdentifier.STREAM);

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
                        return Utils.containsAny(
                            bugInstance.getClassName(),
                            Utils.retrieveFullyQualifiedName(source.split(" ")));
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

  //- specify main class?

  /** main_TestableFiles_SingleTest. */
  @Test
  public void main_TestableFiles_FullProject() {
    String fileOut = tempFileOutTxt_Class_fullproj_0;
    String source = Utils.join(" ", TestUtilities.arr(srcOneGrvInputArr_Class));
    new File(fileOut).delete();

    if (isLinux) {
      String args =
          makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES)
              + makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML)
              + makeArg(argsIdentifier.SOURCE, source)
              + makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep)
              + makeArg(argsIdentifier.NOEXIT)
              + makeArg(argsIdentifier.ANDROID, "/InvalidPath/")
              + makeArg(argsIdentifier.MAIN, srcOneGrvInputArr_Class.get(2))
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
                        return Utils.containsAny(
                            bugInstance.getClassName(),
                            Utils.retrieveFullyQualifiedName(source.split(" ")));
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

  /** main_TestableFiles_SingleTest. */
  @Test
  public void main_TestableFiles_SingleTest_PluginBase_ClassFileOnly() {
    soot.G.v().reset();

    String source = testablejar_Crypto_class;
    String fileOut = testablejar_Crypto_plugin_class_json_0;
    new File(fileOut).delete();

    if (isLinux) {

      try {
        String outputFile =
            EntryPoint_Plugin.main(
                Arrays.asList(source), new ArrayList<>(), fileOut, null, null, null, null, 0, null);

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
                            .contains(
                                Utils.retrieveFullyQualifiedName(source)
                                    .replace(".", Utils.fileSep));
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
  public void main_TestableFiles_MultiTest_Plugin() {
    String fileOut = tempFileOutTxt_default_0;
    ArrayList<String> source = TestUtilities.arr(classFiles);
    new File(fileOut).delete();

    if (isLinux) {

      try {
        String outputFile =
            EntryPoint_Plugin.main(
                source,
                Utils.getJarsInDirectory(srcOneGrvDep),
                fileOut,
                classFiles[3],
                null,
                null,
                null,
                0,
                null);

        Report report = Report.deserialize(new File(outputFile));
        assertFalse(report.getIssues().isEmpty());
        assertTrue(
            report
                .getIssues()
                .stream()
                .anyMatch(
                    bugInstance -> {
                      try {
                        return Utils.containsAny(
                            bugInstance.getFullPath(),
                            Utils.retrieveFullyQualifiedNameFileSep(source));
                      } catch (ExceptionHandler e) {
                        assertNull(e);
                        e.printStackTrace();
                      }
                      return false;
                    }));

      } catch (Exception e) {
        e.printStackTrace();
        assertNull(e);
      }
    }
  }
  //endregion
}

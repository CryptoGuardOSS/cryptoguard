package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import soot.G;
import test.TestUtilities;
import util.Utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;

/**
 * <p>EntryPointTest_CLASS class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_CLASS {

    //region Attributes
    private EntryPoint engine;
    //endregion

    //region Test Environment Setup

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
        //Cleaning the current scene since setup carries throughout the VM
        //tldr - one test setting up the scene will carry over to the next test, this'll stop that
        G.reset();

        engine = new EntryPoint();
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        engine = null;
    }
    //endregion

    //region Tests
    @Test
    /**
     * <p>main_TestableFiles_SingleTest.</p>
     */
    public void main_TestableFiles_SingleTest() {
        String fileOut = tempFileOutTxt_Class;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.SOURCE, classFiles[0]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                results.removeIf(bugInstance -> !bugInstance.contains(getFileNameFromString(fileOut)));
                assertTrue(results.size() >= 1);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    /**
     * <p>main_TestableFiles_SingleTest.</p>
     */
    public void main_TestableFiles_FullProject() {
        String fileOut = tempFileOutTxt_Class_fullproj;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.SOURCE, Utils.join(" ", TestUtilities.arr(srcOneGrvInputArr_Class))) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                assertFalse(report.getBugInstance().isEmpty());

                report.getBugInstance().removeIf(bugInstance -> !bugInstance.getClassName().contains(getFileNameFromString(fileOut)));
                assertFalse(report.getBugInstance().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableFiles_SingleTest.</p>
     */
    @Test
    public void main_TestableFiles_SingleTest_ExtremelyBaseTest() {
        String fileOut = tempFileOutTxt_Class_tester_test;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.SOURCE, testRec_tester_test_Class) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                results.removeIf(bugInstance -> !bugInstance.contains(getFileNameFromString(fileOut)));
                assertTrue(results.size() >= 1);

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    /**
     * <p>main_TestableFiles_MultiTest.</p>
     */
    public void main_TestableFiles_MultiTest() {
        String fileOut = tempFileOutTxt_two;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES) +
                            makeArg(argsIdentifier.SOURCE, Utils.join(" ", classFiles)) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                results.removeIf(bugInstance -> !bugInstance.contains(getFileNameFromString(fileOut)));
                assertTrue(results.size() >= 1);

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    /**
     * <p>main_TestableFiles_MultiTest_Scarf.</p>
     */
    public void main_TestableFiles_MultiTest_Scarf() {
        String fileOut = tempFileOutXML_Class;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES) +
                            makeArg(argsIdentifier.SOURCE, String.join(" ", classFiles)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                assertFalse(report.getBugInstance().isEmpty());

                report.getBugInstance().removeIf(bugInstance -> !bugInstance.getClassName().contains(getFileNameFromString(fileOut)));
                assertFalse(report.getBugInstance().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    /**
     * <p>main_TestableFiles_MultiTest_Scarf_Stream.</p>
     */
    public void main_TestableFiles_MultiTest_Scarf_Stream() {
        String fileOut = tempFileOutXML_Class_Stream;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES) +
                            makeArg(argsIdentifier.SOURCE, String.join(" ", classFiles)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.STREAM);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                assertFalse(report.getBugInstance().isEmpty());

                report.getBugInstance().removeIf(bugInstance -> !bugInstance.getClassName().contains(getFileNameFromString(fileOut)));
                assertFalse(report.getBugInstance().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //endregion
}

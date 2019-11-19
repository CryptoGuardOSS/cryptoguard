package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import rule.engine.EngineType;
import soot.G;
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
import static util.Utils.makeArg;

/**
 * <p>EntryPointTest_JAVA class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_JAVA {

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
        //endregion
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
    }
    //endregion

    //region Tests
    //@Test
    public void main_TestableFile_test_nobreak() {
        soot.G.v().reset();
        String source = testRec_tester_test_Java;
        String fileOut = testRec_tester_test_Java_xml;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.SOURCE, source) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.VERYVERBOSE) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                assertTrue(report.getBugInstance().isEmpty());


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //@Test
    public void main_TestableFile_VerySimple() {
        soot.G.v().reset();
        String source = verySimple_Java;
        String fileOut = verySimple_Java_xml;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.SOURCE, source) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.VERYVERBOSE) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));

                assertFalse(report.getBugInstance().isEmpty());
                assertTrue(report.getBugInstance().stream().allMatch(bugInstance -> {
                    try {
                        return bugInstance.getClassName().contains(Utils.retrieveFullyQualifiedName(source));
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

    /**
     * <p>main_TestableFiles_SingleTest.</p>
     */
    //@Test
    public void main_TestableFiles_SingleTest() {
        String fileOut = javaFileTwo;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.SOURCE, javaFiles[1]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 10);

                results.removeIf(bugInstance -> !bugInstance.contains(getFileNameFromString(fileOut)));
                assertTrue(results.size() >= 1);

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableFiles_SingleTest_Scarf.</p>
     */
    //@Test
    public void main_TestableFiles_SingleTest_Scarf() {
        String fileOut = javaFileOne;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.SOURCE, javaFiles[0]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, javaFileOne) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                assertFalse(report.getBugInstance().isEmpty());

                report.getBugInstance().removeIf(bugInstance -> !bugInstance.getClassName().contains(getFileNameFromString(fileOut)));
                assertFalse(report.getBugInstance().isEmpty());

            } catch (Exception e) {
                assertNull(e);
                e.printStackTrace();
            }


        }

    }


    /**
     * <p>main_TestableFiles_MultiTest.</p>
     */
    //@Test
    public void main_TestableFiles_MultiTest() {
        String fileOut = javaFileThreeXML;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.SOURCE, String.join(" ", javaFiles)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, javaFileThreeXML);

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

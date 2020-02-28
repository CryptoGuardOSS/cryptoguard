package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import soot.G;
import test.TestUtilities;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;
import static util.Utils.makeArg;

/**
 * <p>EntryPointTest_JAR class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_JAR {

    //region Test Environment Setup

    public static String replace(String temp) {
        return temp.replace(".xml", ".yaml");
    }

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
    //endregion

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
    }

    //region Tests
    @Test
    public void main_VerySimple_Scarf() {
        String fileOut = verySimple_Jar_xml;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, verySimple_Jar) +
                            //makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.STREAM) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

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

    /**
     * <p>main_TestableJar.</p>
     */
    @Test
    public void main_TestableJar() {
        String fileOut = tempJarFile_txt;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, fileOut);

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

    /**
     * <p>main_TestableJar_Scarf.</p>
     */
    @Test
    public void main_TestableJar_Scarf() {
        String fileOut = tempJarFile_Scarf_0;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport.deserialize(new File(outputFile));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableJar_Scarf.</p>
     */
    @Test
    public void main_TestableJar_Scarf_Heuristics() {
        String fileOut = tempJarFile_Scarf_0;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.HEURISTICS) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                assertNotNull(report.getHeuristics());
                assertFalse(report.getBugInstance().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableJar_Scarf.</p>
     */
    @Test
    public void main_TestableJar_Default() {
        String fileOut = tempJarFile_Default_0;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

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

    /**
     * <p>main_TestableJar_Scarf.</p>
     */
    @Test
    public void main_TestableJar_Default_SpecifyJavaHome() {
        String fileOut = tempJarFile_Default_0;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.JAVA, System.getenv("JAVA_HOME")) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

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

    /**
     * <p>main_TestableJar_Scarf.</p>
     */
    @Test
    public void main_TestableJar_Default_Heuristics() {
        String fileOut = tempJarFile_Default_0;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.HEURISTICS) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                Report report = Report.deserialize(new File(outputFile));
                assertNotNull(report.getHeuristics());
                assertFalse(report.getIssues().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableJar_Scarf_Args.</p>
     */
    @Test
    public void main_TestableJar_Scarf_Args() {
        String fileOut = tempJarFile_Scarf_1;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.SCONFIG, scarfArgs);

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

    /**
     * <p>main_TestableJar_Scarf_Stream.</p>
     */
    @Test
    public void main_TestableJar_Scarf_Stream() {
        String fileOut = tempJarFile_Scarf_Steam_1;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.STREAM) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

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

    /**
     * <p>main_TestableJar_Scarf_Stream.</p>
     */
    @Test
    public void main_TestableJar_Default_Stream() {
        String fileOut = tempJarFile_Default_Stream_0;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.STREAM) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

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

    /**
     * <p>main_TestableJar_ScarfTimeStamp.</p>
     */
    @Test
    public void main_TestableJar_ScarfTimeStamp() {
        String fileOut = tempJarFile_Scarf_2;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.TIMESTAMP) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                String outputFile = TestUtilities.captureNewFileOutViaStdOut(args.split(" "));

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                assertFalse(report.getBugInstance().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }


    //region

    /**
     * <p>main_TestableJar_ScarfTimeStamp.</p>
     */
    @Test
    public void main_TestableJar_ScarfTimeStamp_UsingClassPaths() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Scarf_2) +
                            makeArg(argsIdentifier.TIMESTAMP) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {

                String outputFile = TestUtilities.captureNewFileOutViaStdOut(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 10);

                AnalyzerReport.deserialize(new File(outputFile));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }
    //endregion

    /**
     * <p>main_TestableJar_Scarf.</p>
     */
    @Test
    public void main_TestableJar_Default_WithHeuristics() {
        String fileOut = tempJarFile_Default_0;
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.HEURISTICS);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                Report report = Report.deserialize(new File(outputFile));
                assertNotNull(report.getHeuristics());
                assertFalse(report.getIssues().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    public void main_TestableJar_Default_WithClassPath() {
        String fileOut = tempJarFile_Default_0;
        String deps = srcOneGrvDep;
        deps = ":" + deps + ":" + deps + ":";
        new File(fileOut).delete();

        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, deps) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, fileOut) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.ANDROID, "/InvalidPath/") +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.HEURISTICS) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                Report report = Report.deserialize(new File(outputFile));
                assertNotNull(report.getHeuristics());
                assertFalse(report.getIssues().isEmpty());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }
    //endregion
}

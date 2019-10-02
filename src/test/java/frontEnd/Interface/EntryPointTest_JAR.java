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
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;

/**
 * <p>EntryPointTest_JAR class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_JAR {

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

    /**
     * <p>main_TestableJar.</p>
     */
    @Test
    public void main_TestableJar() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.OUT, tempJarFile_txt);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_txt), StandardCharsets.UTF_8);
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
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Scarf_0) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Scarf_0), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempJarFile_Scarf_0));

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
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Scarf_0) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.HEURISTICS) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Scarf_0), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempJarFile_Scarf_0));
                assertNotNull(report.getHeuristics());

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    public static String replace(String temp) {
        return temp.replace(".xml", ".yaml");
    }

    /**
     * <p>main_TestableJar_Scarf.</p>
     */
    @Test
    public void main_TestableJar_Default() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Default_0) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Default_0), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                Report report = Report.deserialize(new File(tempJarFile_Default_0));

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
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Default_0) +
                            makeArg(argsIdentifier.HEURISTICS) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Default_0), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                Report report = Report.deserialize(new File(tempJarFile_Default_0));
                assertNotNull(report.getHeuristics());

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
        if (isLinux) {
            //TODO - Check out Sconfig, missing from argsIdentifier
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Scarf_1) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY) +
                            " -Sconfig " + scarfArgs;

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Scarf_1), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);


                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempJarFile_Scarf_1));
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
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Scarf_Steam_1) +
                            makeArg(argsIdentifier.STREAM) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Scarf_Steam_1), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);


                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempJarFile_Scarf_Steam_1));
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
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Default_Stream_0) +
                            makeArg(argsIdentifier.STREAM) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Default_Stream_0), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                Report report = Report.deserialize(new File(tempJarFile_Default_Stream_0));

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
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Scarf_2) +
                            makeArg(argsIdentifier.TIMESTAMP) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                String outputFile = TestUtilities.captureNewFileOutViaStdOut(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 10);

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));

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
                            makeArg(argsIdentifier.AUXCLASSPATH, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Scarf_2) +
                            makeArg(argsIdentifier.TIMESTAMP) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY);

            try {

                String outputFile = TestUtilities.captureNewFileOutViaStdOut(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 10);

                AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));

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
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                            makeArg(argsIdentifier.SOURCE, jarOne) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Default) +
                            makeArg(argsIdentifier.OUT, tempJarFile_Default_0) +
                            makeArg(argsIdentifier.TIMEMEASURE) +
                            makeArg(argsIdentifier.NOEXIT) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.HEURISTICS);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempJarFile_Default_0), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 2);

                Report report = Report.deserialize(new File(tempJarFile_Default_0));

                assertTrue(report.getHeuristics() != null);

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }
    //endregion
}

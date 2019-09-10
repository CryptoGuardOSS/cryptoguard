package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import soot.G;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;

/**
 * <p>EntryPointTest_JAVA class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_JAVA {

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
        //endregion
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        engine = null;
        //endregion
    }
    //endregion

    //region Tests

    /**
     * <p>testEnvironmentVariables.</p>
     */
    @Test
    public void testEnvironmentVariables() {
        String[] dirLists = new String[]{srcOneGrv, srcOneGrvDep};

        for (String file : javaFiles) {
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

    //@Test
    /**
     * <p>main_TestableFiles_SingleTest.</p>
     */
    public void main_TestableFiles_SingleTest() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.SOURCE, javaFiles[1]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.OUT, javaFileTwo);

            try {

                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(javaFileTwo), StandardCharsets.UTF_8);

                int count = 0;
                for (String line : results)
                    if (line.contains("Violated"))
                        count++;
                assertTrue(count > 0);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //@Test - TODO Reimplement this test
    /**
     * <p>main_TestableFiles_SingleTest_Scarf.</p>
     */
    public void main_TestableFiles_SingleTest_Scarf() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.SOURCE, javaFiles[0]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, javaFileOne) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                //region Validating output
                AnalyzerReport report = AnalyzerReport.deserialize(new File(javaFileOne));
                //endregion

                List<String> results = Files.readAllLines(Paths.get(javaFileOne), StandardCharsets.UTF_8);

                assertTrue(results.size() > 1);
            } catch (Exception e) {
                assertNull(e);
                e.printStackTrace();
            }


        }

    }

    //@Test - TODO Reimplement this test
    /**
     * <p>main_TestableFiles_MultiTest.</p>
     */
    public void main_TestableFiles_MultiTest() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                            makeArg(argsIdentifier.SOURCE, String.join(" ", javaFiles)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.OUT, javaFileThree);

            try {

                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(javaFileThree), StandardCharsets.UTF_8);

                assertTrue(results.size() > 1);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //endregion
}

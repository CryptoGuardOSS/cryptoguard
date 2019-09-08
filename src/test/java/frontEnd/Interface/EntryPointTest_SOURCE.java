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
 * <p>EntryPointTest_SOURCE class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_SOURCE {

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
    public void testEnvironmentVariables() {
        String[] fileLists = new String[]{jarOne, pathToSchema};
        String[] dirLists = new String[]{srcOneGrv, srcOneGrvDep};

        for (String file : fileLists) {
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

    //@Test - TODO Reimplement this test
    public void main_TestableJarSource() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.DIR) +
                            makeArg(argsIdentifier.SOURCE, srcOneGrv) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.OUT, tempFileOutTxt);


            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutTxt), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 10);
            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //@Test - TODO Reimplement this test
    public void main_TestableJarSourceScarf() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.DIR) +
                            makeArg(argsIdentifier.SOURCE, srcOneGrv) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempFileOutXML) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 1);


                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempFileOutXML));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //@Test - TODO Reimplement this test
    public void main_TestableJarSourceScarf_Stream() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.DIR) +
                            makeArg(argsIdentifier.SOURCE, srcOneGrv) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempStreamXML) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.STREAM);
            try {
                EntryPoint.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempStreamXML), StandardCharsets.UTF_8);
                assertTrue(results.size() >= 1);


                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempStreamXML));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //endregion
}

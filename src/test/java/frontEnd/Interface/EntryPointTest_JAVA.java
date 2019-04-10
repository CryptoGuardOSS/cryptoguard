package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import soot.G;
import util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;

/**
 * <p>EntryPointTest_JAVA class.</p>
 *
 * @author drmonster
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_JAVA {

    //region Attributes
    private EntryPoint engine;
    private ByteArrayOutputStream out;

    //region Scarf Properties
    private String assessment_start_ts;
    private String build_fw;
    private String build_fw_version;
    private String package_name;
    private String package_version;
    private String assess_fw;
    private String assess_fw_version;
    private String build_root_dir;
    private String package_root_dir;
    private String parser_fw;
    private String parser_fw_version;
    private String uuid;
    //endregion

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
        out = new ByteArrayOutputStream();

        //region Properties Setup
        assess_fw = "java-assess";
        assess_fw_version = "1.0.0c";
        assessment_start_ts = "1516116551.639144";
        build_fw = "c-assess";
        build_fw_version = "1.1.12";
        build_root_dir = "/home";
        package_name = "RigorityJ";
        package_root_dir = "CryptoGuard";
        package_version = "8675309";
        parser_fw = "example_tool";
        parser_fw_version = "x.y.z";
        uuid = "fa109792-9234-4jk2-9f68-alp9woofbeef";
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
        out = null;

        //region Properties
        assess_fw = null;
        assess_fw_version = null;
        assessment_start_ts = null;
        build_fw = null;
        build_fw_version = null;
        build_root_dir = null;
        package_root_dir = null;
        package_name = null;
        package_root_dir = null;
        package_version = null;
        parser_fw = null;
        parser_fw_version = null;
        uuid = null;
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
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, javaFiles[1]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.PRETTY) +
                            makeArg(argsIdentifier.OUT, javaFileTwo);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(javaFileTwo), Charset.forName("UTF-8"));

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

    //@Test
    /**
     * <p>main_TestableFiles_SingleTest_Scarf.</p>
     */
    public void main_TestableFiles_SingleTest_Scarf() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, javaFiles[0]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML.getFlag()) +
                            makeArg(argsIdentifier.OUT, javaFileOne) +
                            makeArg(argsIdentifier.PRETTY);

            try {
                engine.main(args.split(" "));

                //region Validating output
                AnalyzerReport report = AnalyzerReport.deserialize(JacksonSerializer.JacksonType.XML, new File(javaFileOne));
                //endregion

                List<String> results = Files.readAllLines(Paths.get(javaFileOne), Charset.forName("UTF-8"));

                assertTrue(results.size() > 1);
            } catch (Exception e) {
                assertNull(e);
                e.printStackTrace();
            }


        }

    }

    //@Test
    /**
     * <p>main_TestableFiles_MultiTest.</p>
     */
    public void main_TestableFiles_MultiTest() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, Utils.join(" ", javaFiles)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.OUT, javaFileThree);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(javaFileThree), Charset.forName("UTF-8"));

                assertTrue(results.size() > 1);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //endregion
}

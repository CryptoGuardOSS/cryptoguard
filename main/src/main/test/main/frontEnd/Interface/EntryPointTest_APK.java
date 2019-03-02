package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;
import main.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import soot.G;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static main.TestUtilities.*;
import static org.junit.Assert.assertNull;

public class EntryPointTest_APK {

    //region Attributes
    private EntryPoint engine;
    private ByteArrayOutputStream out;
    private final String tempFileOutApk = Utils.osPathJoin(testPath, "app-debug.txt");
    private final String tempFileOutApk_Scarf = Utils.osPathJoin(testPath, "app-debug.xml");

    private Boolean validateXML = false;

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
    @Before
    public void setUp() throws Exception {
        //Cleaning the current scene since setup carries throughout the VM
        //tldr - one test setting up the scene will carry over to the next test, this'll stop that
        G.reset();

        //region Determining to validate the XML against the schema or not
        try {
            File scarfXMLMarshaller = new File(Utils.osPathJoin(basePath, "src", "main", "java", "com", "example", "response", "package-info.java"));
            assertTrue(scarfXMLMarshaller.exists());

            List<String> contents = Files.readAllLines(scarfXMLMarshaller.toPath(), Charset.forName("UTF-8"));
            if (contents.contains("@javax.xml.bind.annotation.XmlSchema(namespace = \"https://www.swamp.com/com/scarf/struct\", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)"))
                validateXML = true;
        } catch (Exception e) {
        }
        //endregion

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

    @Test
    public void main_TestableApk() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk;

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    public void main_TestableApk_Scarf() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk_Scarf + " -m " + Listing.ScarfXML.getFlag() + " -n";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk_Scarf), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //endregion
}
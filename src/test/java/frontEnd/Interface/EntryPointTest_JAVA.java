package frontEnd.Interface;

import com.example.response.AnalyzerReport;
import com.example.response.BugInstanceType;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import soot.G;
import test.TestUtilities;
import util.Utils;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static test.TestUtilities.*;

public class EntryPointTest_JAVA {

    private final String src = Utils.osPathJoin(srcOneGrv, "src", "main", "java", "tester");
    private final String[] files = {Utils.osPathJoin(src, "PBEUsage.java"), Utils.osPathJoin(src, "UrlFrameWorks.java")};
    private final String srcOneGrvDep = Utils.osPathJoin(srcOneGrv, "build", "dependencies");
    private final String tempFileOutTxt = Utils.osPathJoin(testPath, "testable-jar_javaFiles.txt");
    private final String tempFileOutXML = Utils.osPathJoin(testPath, "testable-jar_javaFiles.xml");
    //region Attributes
    private EntryPoint engine;
    private ByteArrayOutputStream out;
    private Boolean validateXML = TestUtilities.validateXML();

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
        String[] dirLists = new String[]{srcOneGrv, srcOneGrvDep};

        for (String file : files) {
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

    //TODO - Re-enable this
    //@Test
    public void main_TestableFiles_SingleTest() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, files[0]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.OUT, tempFileOutTxt);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutTxt), Charset.forName("UTF-8"));

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

    //TODO - Re-enable this
    //@Test
    public void main_TestableFiles_SingleTest_Scarf() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, files[0]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML.getFlag()) +
                            makeArg(argsIdentifier.OUT, tempFileOutXML) +
                            makeArg(argsIdentifier.PRETTY);

            try {

                engine.main(args.split(" "));

                if (validateXML) {
                    Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                    Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                    xmlToJava.setSchema(schema);

                    AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempFileOutXML));

                    for (BugInstanceType in : result.getBugInstance()) {
                        assertEquals(1, in.getCweId().size());
                        assertNotEquals(-1, in.getCweId().get(0));
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //TODO - Re-enable this
    //@Test
    public void main_TestableFiles_MultiTest() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, Utils.join(" ", files)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.OUT, tempFileOutTxt);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutTxt), Charset.forName("UTF-8"));

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

    //endregion
}
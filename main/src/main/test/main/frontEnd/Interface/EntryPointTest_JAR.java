package main.frontEnd.Interface;

import com.example.response.AnalyzerReport;
import com.example.response.BugInstanceType;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.frontEnd.argsIdentifier;
import main.rule.engine.EngineType;
import main.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import soot.G;

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

public class EntryPointTest_JAR {

    private final Boolean isLinux = !System.getProperty("os.name").contains("Windows");
    private final String basePath = System.getProperty("user.dir");
    private final String scarfArgs = Utils.osPathJoin(basePath, "rsc", "sample.properties");
    private final String srcOneGrv = basePath.replace("main", "testable-jar");
    private final String jarOne = Utils.osPathJoin(srcOneGrv, "build", "libs", "testable-jar.jar");
    private final String srcOneGrvDep = Utils.osPathJoin(srcOneGrv, "build", "dependencies");
    private final String tempFileOutTxt = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar.txt");
    private final String tempFileOutXML = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar.xml");
    private final String tempStreamXML = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar_Stream.xml");
    private final String pathToSchema = Utils.osPathJoin(basePath, "src", "main", "schema", "xsd", "Scarf", "Scarf.xsd");
    //region Attributes
    private EntryPoint engine;
    private ByteArrayOutputStream out;
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
    public void main_TestableJar() {
        if (isLinux) {
            String args = "-in " + EngineType.JAR.getFlag() + " -s " + jarOne + " -d " + srcOneGrvDep + " -o " + tempFileOutTxt;

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutTxt), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    public void main_TestableJar_Scarf() {
        if (isLinux) {
            String args = "-in " + EngineType.JAR.getFlag() + " -s " + jarOne + " -d " + srcOneGrvDep + " -m " + Listing.ScarfXML.getFlag() + " -o " + tempFileOutXML + " -t";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

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

    @Test
    public void main_TestableJar_Scarf_Args() {
        if (isLinux) {
            String args = "-in " + EngineType.JAR.getFlag() +
                    " -s " + jarOne +
                    " -d " + srcOneGrvDep +
                    " -m " + Listing.ScarfXML.getFlag() +
                    " -o " + tempFileOutXML +
                    " -t" +
                    " -n" +
                    " -Sconfig " + scarfArgs;

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                if (validateXML) {
                    Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                    Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                    xmlToJava.setSchema(schema);

                    AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempFileOutXML));

                    for (BugInstanceType in : result.getBugInstance()) {
                        assertEquals(1, in.getCweId().size());
                        assertNotEquals(-1, in.getCweId().get(0));
                    }

                    assertEquals(assess_fw, result.getAssessFw());
                    assertEquals(assess_fw_version, result.getAssessFwVersion());
                    assertEquals(assessment_start_ts, result.getAssessmentStartTs());
                    assertEquals(build_fw, result.getBuildFw());
                    assertEquals(build_fw_version, result.getBuildFwVersion());
                    assertEquals(build_root_dir, result.getBuildRootDir());
                    assertEquals(package_root_dir, result.getPackageRootDir());
                    assertEquals(package_name, result.getPackageName());
                    assertEquals(package_version, result.getPackageVersion());
                    assertEquals(parser_fw, result.getParserFw());
                    assertEquals(parser_fw_version, result.getParserFwVersion());
                    assertEquals(uuid, result.getUuid());

                }
            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    public void main_TestableJar_Scarf_Stream() {
        if (isLinux) {
            String args = "-in " + EngineType.JAR.getFlag() + " -s " + jarOne + " -d " + srcOneGrvDep + " -m " + Listing.ScarfXML.getFlag() + " -o " + tempStreamXML + " -st" + " -t";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempStreamXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                if (validateXML) {
                    Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                    Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                    xmlToJava.setSchema(schema);

                    AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempStreamXML));

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

    @Test
    public void main_TestableJar_ScarfTimeStamp() {
        if (isLinux) {
            String args = "-in " + EngineType.JAR.getFlag() + " -s " + jarOne + " -d " + srcOneGrvDep + " -m " + Listing.ScarfXML.getFlag() + " -o " + tempFileOutXML + " " + argsIdentifier.TIMESTAMP.getArg() + " " + argsIdentifier.PRETTY.getArg();

            try {
                engine.main(args.split(" "));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }
    //endregion
}
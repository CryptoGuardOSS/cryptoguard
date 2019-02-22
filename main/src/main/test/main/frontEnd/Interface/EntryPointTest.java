package main.frontEnd.Interface;

import com.example.response.AnalyzerReport;
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
import static org.junit.Assert.assertNull;

public class EntryPointTest {

    //region Attributes
    private EntryPoint engine;
    private ByteArrayOutputStream out;
    private final Boolean isLinux = !System.getProperty("os.name").contains("Windows");

    private final String basePath = System.getProperty("user.dir");
    private final String srcOneGrv = basePath.replace("main", "testable-jar");
    private final String jarOne = Utils.osPathJoin(srcOneGrv, "build", "libs", "testable-jar.jar");
    private final String srcOneGrvDep = Utils.osPathJoin(srcOneGrv, "build", "dependencies");
    private final String tempFileOutTxt = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar.txt");
    private final String tempFileOutApk = Utils.osPathJoin(System.getProperty("user.dir"), "app-debug.txt");
    private final String tempFileOutApk_Scarf = Utils.osPathJoin(System.getProperty("user.dir"), "app-debug.xml");
    private final String tempFileOutXML = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar.xml");
    private final String tempStreamXML = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar_Stream.xml");
    private final String pathToSchema = Utils.osPathJoin(basePath, "src", "main", "schema", "xsd", "Scarf", "Scarf.xsd");
    private final String pathToAPK = Utils.osPathJoin(basePath.replace("main", ""), "app-debug.apk");

    private Boolean validateXML = false;
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
    }

    @After
    public void tearDown() throws Exception {
        engine = null;
        out = null;
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
                    " -Saf " + "java-assess" +
                    " -Safv " + "1.0.1" +
                    " -Sbrd " + "/home/test" +
                    " -Sprd " + "octopus" +
                    " -Spn " + "octopus" +
                    " -Spv " + "2019-02-14" +
                    " -Sid " + "12345";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                if (validateXML) {
                    Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                    Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                    xmlToJava.setSchema(schema);

                    AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempFileOutXML));
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

    @Test
    public void main_TestableJarSource() {
        if (isLinux) {
            String args = "-in " + EngineType.DIR.getFlag() + " -s " + srcOneGrv + " -d " + srcOneGrvDep + " -o " + tempFileOutTxt;

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
    public void main_TestableJarSourceScarf() {
        if (isLinux) {
            String args = "-in " + EngineType.DIR.getFlag() + " -s " + srcOneGrv + " -d " + srcOneGrvDep + " -m " + Listing.ScarfXML.getFlag() + " -o " + tempFileOutXML;

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                if (validateXML) {
                    Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                    Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                    xmlToJava.setSchema(schema);

                    AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempFileOutXML));
                }

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    public void main_TestableJarSourceScarf_Stream() {
        if (isLinux) {
            String args = "-in " + EngineType.DIR.getFlag() + " -s " + srcOneGrv + " -d " + srcOneGrvDep + " -n -m " + Listing.ScarfXML.getFlag() + " -o " + tempStreamXML + " -st";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempStreamXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                if (validateXML) {
                    Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                    Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                    xmlToJava.setSchema(schema);

                    AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempStreamXML));
                }
            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
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
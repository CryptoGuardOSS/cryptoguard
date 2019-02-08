package main.frontEnd.Interface;

import com.example.response.AnalyzerReport;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.frontEnd.argsIdentifier;
import main.rule.engine.EngineType;
import main.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
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
    private final String tempFileOutXML = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar.xml");
    private final String tempStreamXML = Utils.osPathJoin(System.getProperty("user.dir"), "testable-jar_Stream.xml");
    private final String pathToSchema = Utils.osPathJoin(basePath, "src", "main", "schema", "xsd", "Scarf", "Scarf.xsd");

    private StringBuilder main_TestableJar_results = new StringBuilder();
    private StringBuilder main_TestableJar_results_noheader = new StringBuilder();
    private StringBuilder main_TestableJarSource_results = new StringBuilder();
    //endregion

    //region Test Methods
    private void redirectOutput() {
        System.setOut(new PrintStream(out));
    }

    private void resetOutput() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
    //endregion

    //region Test Environment Setup
    @Before
    public void setUp() throws Exception {

        //region Building main_TestableJar_results String
        main_TestableJar_results.append("Analyzing " + EngineType.JAR.getFlag().toUpperCase() + ": " + this.jarOne + "\n");
        main_TestableJar_results.append("Warning: okhttp3.Request$Builder is a phantom class!\n");
        main_TestableJar_results.append("Warning: retrofit2.Retrofit$Builder is a phantom class!\n");
        main_TestableJar_results.append("Warning: okhttp3.OkHttpClient$Builder is a phantom class!\n");
        main_TestableJar_results.append("Warning: okhttp3.Request is a phantom class!\n");
        main_TestableJar_results.append("Warning: retrofit2.converter.gson.GsonConverterFactory is a phantom class!\n");
        main_TestableJar_results.append("Warning: okhttp3.HttpUrl$Builder is a phantom class!\n");
        main_TestableJar_results.append("Warning: okhttp3.HttpUrl is a phantom class!\n");
        main_TestableJar_results.append("Warning: retrofit2.Converter$Factory is a phantom class!\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 5: Used export grade public Key \n");
        main_TestableJar_results.append("***Cause: Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 5: Used export grade public Key\n");
        main_TestableJar_results.append("***Found: [1024] in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 1: Found broken crypto schemes\n");
        main_TestableJar_results.append("***Found: [\"AES/ECB/PKCS5PADDING\"] in Method: <tester.Crypto: void <init>()>\n");
        main_TestableJar_results.append("***Found: [\"PBEWithMD5AndDES\"] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=============================================\n");
        main_TestableJar_results.append("***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <tester.Crypto: byte[] randomNumberGeneration(long)>\n");
        main_TestableJar_results.append("=============================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 4: Uses untrusted TrustManager ***Should not use unpinned self-signed certification in tester.Crypto$2\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of tester.Crypto$2\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 2: Found broken hash functions\n");
        main_TestableJar_results.append("***Found: [\"MD5\"] in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        main_TestableJar_results.append("***Found: [\"SHA1\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJar_results.append("***Found: [\"SHA\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 3: Used constant keys in code\n");
        main_TestableJar_results.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        main_TestableJar_results.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJar_results.append("***Found: [\"Bar12345Bar12345\"] in Line 152 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJar_results.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJar_results.append("***Found: [\"tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV\"] in Line 79 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 10: Found constant IV in code\n");
        main_TestableJar_results.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJar_results.append("***Found: [\"RandomInitVector\"] in Line 153 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJar_results.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 9: Found constant salts in code\n");
        main_TestableJar_results.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        main_TestableJar_results.append("***Found: [\"f77aLYLo\"] in Line 80 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 8: Used < 1000 iteration for PBE\n");
        main_TestableJar_results.append("***Found: [1] in Line 50 in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        main_TestableJar_results.append("***Found: [17] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 14: Used Predictable KeyStore Password\n");
        main_TestableJar_results.append("***Found: [\"mypass\"] in Line 108 in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("=======================================\n");
        main_TestableJar_results.append("***Violated Rule 7: Used HTTP Protocol\n");
        main_TestableJar_results.append("***Found: [\"http://publicobject.com/helloworld.txt\"] in Method: <tester.UrlFrameWorks: java.net.HttpURLConnection createURL(java.lang.String)>\n");
        main_TestableJar_results.append("=======================================\n");
        //endregion

        //region Building main_TestableJarSource_results-Jar String
        //main_TestableJarSource_results.append("Analyzing Project: " + srcOneGrv + "\n");
        main_TestableJarSource_results.append("Analyzing Module: testable-jar\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 5: Used export grade public Key \n");
        main_TestableJarSource_results.append("***Cause: Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 5: Used export grade public Key\n");
        main_TestableJarSource_results.append("***Found: [1024] in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 1: Found broken crypto schemes\n");
        main_TestableJarSource_results.append("***Found: [\"AES/ECB/PKCS5PADDING\"] in Method: <tester.Crypto: void <init>()>\n");
        main_TestableJarSource_results.append("***Found: [\"PBEWithMD5AndDES\"] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=============================================\n");
        main_TestableJarSource_results.append("***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <tester.Crypto: byte[] randomNumberGeneration(long)>\n");
        main_TestableJarSource_results.append("=============================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 2: Found broken hash functions\n");
        main_TestableJarSource_results.append("***Found: [\"MD5\"] in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        main_TestableJarSource_results.append("***Found: [\"SHA1\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJarSource_results.append("***Found: [\"SHA\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 3: Used constant keys in code\n");
        main_TestableJarSource_results.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        main_TestableJarSource_results.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJarSource_results.append("***Found: [\"Bar12345Bar12345\"] in Line 152 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJarSource_results.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJarSource_results.append("***Found: [\"tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV\"] in Line 79 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 10: Found constant IV in code\n");
        main_TestableJarSource_results.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJarSource_results.append("***Found: [\"RandomInitVector\"] in Line 153 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        main_TestableJarSource_results.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 9: Found constant salts in code\n");
        main_TestableJarSource_results.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        main_TestableJarSource_results.append("***Found: [\"f77aLYLo\"] in Line 80 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 8: Used < 1000 iteration for PBE\n");
        main_TestableJarSource_results.append("***Found: [1] in Line 50 in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        main_TestableJarSource_results.append("***Found: [17] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 14: Used Predictable KeyStore Password\n");
        main_TestableJarSource_results.append("***Found: [\"mypass\"] in Line 108 in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("=======================================\n");
        main_TestableJarSource_results.append("***Violated Rule 7: Used HTTP Protocol\n");
        main_TestableJarSource_results.append("***Found: [\"http://publicobject.com/helloworld.txt\"] in Method: <tester.UrlFrameWorks: java.net.HttpURLConnection createURL(java.lang.String)>\n");
        main_TestableJarSource_results.append("=======================================\n");
        //endregion

        for (String line : main_TestableJar_results.toString().split("\n"))
            if (!line.startsWith("Analyzing "))
                main_TestableJar_results_noheader.append(line).append("\n");
        main_TestableJar_results.append("\n");

        engine = new EntryPoint();
        out = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        main_TestableJar_results = null;
        main_TestableJarSource_results = null;
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

            redirectOutput();

            try {
                engine.main(args.split(" "));

                resetOutput();

                for (String in : out.toString().split("\n"))
                    assertTrue(StringUtils.isAllBlank(in) || in.startsWith("Warning"));

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

            redirectOutput();

            try {
                engine.main(args.split(" "));

                resetOutput();

                for (String in : out.toString().split("\n"))
                    assertTrue(StringUtils.isAllBlank(in) || in.startsWith("Warning"));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                xmlToJava.setSchema(schema);

                AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempFileOutXML));

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

            redirectOutput();

            try {
                engine.main(args.split(" "));

                resetOutput();

                for (String in : out.toString().split("\n"))
                    assertTrue(StringUtils.isAllBlank(in) || in.startsWith("Warning"));

                List<String> results = Files.readAllLines(Paths.get(tempStreamXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                xmlToJava.setSchema(schema);

                AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempStreamXML));

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

            redirectOutput();

            try {
                engine.main(args.split(" "));

                resetOutput();

                for (String in : out.toString().split("\n"))
                    assertTrue(StringUtils.isAllBlank(in) || in.startsWith("Warning"));

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

            redirectOutput();

            try {
                engine.main(args.split(" "));

                resetOutput();

                for (String in : out.toString().split("\n"))
                    assertTrue(StringUtils.isAllBlank(in) || in.startsWith("Warning"));

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

            redirectOutput();

            try {
                engine.main(args.split(" "));

                resetOutput();

                for (String in : out.toString().split("\n"))
                    assertTrue(StringUtils.isAllBlank(in) || in.startsWith("Warning"));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                xmlToJava.setSchema(schema);

                AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempFileOutXML));
            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    public void main_TestableJarSourceScarf_Stream() {
        if (isLinux) {
            String args = "-in " + EngineType.DIR.getFlag() + " -s " + srcOneGrv + " -d " + srcOneGrvDep + " -m " + Listing.ScarfXML.getFlag() + " -o " + tempStreamXML + " -st";

            redirectOutput();

            try {
                engine.main(args.split(" "));

                resetOutput();

                for (String in : out.toString().split("\n"))
                    assertTrue(StringUtils.isAllBlank(in) || in.startsWith("Warning"));

                List<String> results = Files.readAllLines(Paths.get(tempStreamXML), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 1);

                Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(pathToSchema));

                Unmarshaller xmlToJava = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
                xmlToJava.setSchema(schema);

                AnalyzerReport result = (AnalyzerReport) xmlToJava.unmarshal(new File(tempStreamXML));
            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }
    //endregion
}
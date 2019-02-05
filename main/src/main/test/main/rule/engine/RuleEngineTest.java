package main.rule.engine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.02
 */
public class RuleEngineTest {
    //region Attributes
    private final String basePath = System.getProperty("user.dir");
    private final String jarOne = basePath + "/rsc/test/" + "testable-jar.jar";
    private final String srcOneGrv = basePath.replace("/main", "/testable-jar");
    private final String srcOneGrvDep = "build/dependencies";
    private String jarOneResults;
    private String jarOneResults_NoPhantoms;
    private final EngineType jarType = EngineType.JAR;
    private final EngineType srcType = EngineType.DIR;
    private String[] args;
    private RuleEngine engine;
    private ByteArrayOutputStream customStream;
    private String testableJarSource;

    //A boolean to enable the differentiation between the old routing and the new routing
    private Boolean migratedToNew = false;
    private final String oldSrcType = "source";
    private final String srcTypeString = migratedToNew ? srcType.getFlag() : oldSrcType;
    //endregion

    //region Test Environment Setup
    @Before
    public void setUp() throws Exception {
        engine = new RuleEngine();

        StringBuilder sampleOne = new StringBuilder();
        //region Building Results String
        sampleOne.append("Analyzing " + this.jarType.getFlag().toUpperCase() + ": " + this.jarOne + "\n");
        sampleOne.append("Warning: okhttp3.Request$Builder is a phantom class!\n");
        sampleOne.append("Warning: retrofit2.Retrofit$Builder is a phantom class!\n");
        sampleOne.append("Warning: okhttp3.OkHttpClient$Builder is a phantom class!\n");
        sampleOne.append("Warning: okhttp3.Request is a phantom class!\n");
        sampleOne.append("Warning: retrofit2.converter.gson.GsonConverterFactory is a phantom class!\n");
        sampleOne.append("Warning: okhttp3.HttpUrl$Builder is a phantom class!\n");
        sampleOne.append("Warning: okhttp3.HttpUrl is a phantom class!\n");
        sampleOne.append("Warning: retrofit2.Converter$Factory is a phantom class!\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 5: Used export grade public Key \n");
        sampleOne.append("***Cause: Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 5: Used export grade public Key\n");
        sampleOne.append("***Found: [1024] in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 1: Found broken crypto schemes\n");
        sampleOne.append("***Found: [\"AES/ECB/PKCS5PADDING\"] in Method: <tester.Crypto: void <init>()>\n");
        sampleOne.append("***Found: [\"PBEWithMD5AndDES\"] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=============================================\n");
        sampleOne.append("***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <tester.Crypto: byte[] randomNumberGeneration(long)>\n");
        sampleOne.append("=============================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 4: Uses untrusted TrustManager ***Should not use unpinned self-signed certification in tester.Crypto$2\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of tester.Crypto$2\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 2: Found broken hash functions\n");
        sampleOne.append("***Found: [\"MD5\"] in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        sampleOne.append("***Found: [\"SHA1\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("***Found: [\"SHA\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 3: Used constant keys in code\n");
        sampleOne.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("***Found: [\"Bar12345Bar12345\"] in Line 152 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("***Found: [\"tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV\"] in Line 79 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 10: Found constant IV in code\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("***Found: [\"RandomInitVector\"] in Line 153 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 9: Found constant salts in code\n");
        sampleOne.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        sampleOne.append("***Found: [\"f77aLYLo\"] in Line 80 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 8: Used < 1000 iteration for PBE\n");
        sampleOne.append("***Found: [1] in Line 50 in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        sampleOne.append("***Found: [17] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 14: Used Predictable KeyStore Password\n");
        sampleOne.append("***Found: [\"mypass\"] in Line 108 in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 7: Used HTTP Protocol\n");
        sampleOne.append("***Found: [\"http://publicobject.com/helloworld.txt\"] in Method: <tester.UrlFrameWorks: java.net.HttpURLConnection createURL(java.lang.String)>\n");
        sampleOne.append("=======================================\n");
        //endregion
        this.jarOneResults = sampleOne.toString();

        StringBuilder noPhantoms = new StringBuilder();
        for (String line : jarOneResults.split("\n"))
            if (!line.endsWith(" is a phantom class!"))
                noPhantoms.append(line).append("\n");
        jarOneResults_NoPhantoms = noPhantoms.toString();

        StringBuilder expected = new StringBuilder();
        //region Building Testable-Jar Output String
        expected.append("Analyzing Project: /media/sf_selfbase_rigorityj/testable-jar\n");
        expected.append("Analyzing Module: testable-jar\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 5: Used export grade public Key \n");
        expected.append("***Cause: Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 5: Used export grade public Key\n");
        expected.append("***Found: [1024] in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 1: Found broken crypto schemes\n");
        expected.append("***Found: [\"AES/ECB/PKCS5PADDING\"] in Method: <tester.Crypto: void <init>()>\n");
        expected.append("***Found: [\"PBEWithMD5AndDES\"] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        expected.append("=======================================\n");
        expected.append("=============================================\n");
        expected.append("***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <tester.Crypto: byte[] randomNumberGeneration(long)>\n");
        expected.append("=============================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 2: Found broken hash functions\n");
        expected.append("***Found: [\"MD5\"] in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        expected.append("***Found: [\"SHA1\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        expected.append("***Found: [\"SHA\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 3: Used constant keys in code\n");
        expected.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        expected.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        expected.append("***Found: [\"Bar12345Bar12345\"] in Line 152 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        expected.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        expected.append("***Found: [\"tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV\"] in Line 79 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 10: Found constant IV in code\n");
        expected.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        expected.append("***Found: [\"RandomInitVector\"] in Line 153 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        expected.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 9: Found constant salts in code\n");
        expected.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        expected.append("***Found: [\"f77aLYLo\"] in Line 80 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 8: Used < 1000 iteration for PBE\n");
        expected.append("***Found: [1] in Line 50 in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        expected.append("***Found: [17] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 14: Used Predictable KeyStore Password\n");
        expected.append("***Found: [\"mypass\"] in Line 108 in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
        expected.append("=======================================\n");
        expected.append("=======================================\n");
        expected.append("***Violated Rule 7: Used HTTP Protocol\n");
        expected.append("***Found: [\"http://publicobject.com/helloworld.txt\"] in Method: <tester.UrlFrameWorks: java.net.HttpURLConnection createURL(java.lang.String)>\n");
        expected.append("=======================================\n");
        //endregion
        this.testableJarSource = expected.toString();

        this.customStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        this.engine = null;
        this.args = null;
        this.jarOneResults = null;
        this.customStream = null;
    }
    //endregion

    //region Test Methods
    private void redirectOutput() {
        System.setOut(new PrintStream(this.customStream));
    }

    private void resetOutput() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
    //endregion

    //region Tests
    @Test
    public void mainTestJar() {
        if (!System.getProperty("os.name").contains("Windows")) {
            File temp = new File(this.jarOne);

            assertTrue(temp.isFile());

            args = new String[]{jarType.getFlag(), this.jarOne, ""};

            redirectOutput();

            try {
                this.engine.main(args);

                resetOutput();

                assertTrue(this.customStream.toString().split("\n").length > 1);

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    public void testSelfGradle() {
        if (!System.getProperty("os.name").contains("Windows")) {

            args = new String[]{srcTypeString, srcOneGrv, srcOneGrvDep};

            redirectOutput();

            try {
                this.engine.main(args);

                resetOutput();

                assertTrue(this.customStream.toString().split("\n").length > 1);

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }
    //endregion
}
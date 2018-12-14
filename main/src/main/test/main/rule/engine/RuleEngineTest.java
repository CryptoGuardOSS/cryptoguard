package main.rule.engine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertEquals;
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
    private final String srcOneGrv = basePath.replace("/main", "");
    private String jarOneResults;
    private final EngineType jarType = EngineType.JAR;
    private final EngineType srcType = EngineType.SOURCE;
    private String[] args;
    private RuleEngine engine;
    private ByteArrayOutputStream customStream;
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
        sampleOne.append("Warning: okhttp3.HttpUrl$Builder is a phantom class!\n");
        sampleOne.append("Warning: retrofit2.converter.gson.GsonConverterFactory is a phantom class!\n");
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
        sampleOne.append("***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of tester.Crypto$2\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 4: Uses untrusted TrustManager ***Should not use unpinned self-signed certification in tester.Crypto$2\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 2: Found broken hash functions\n");
        sampleOne.append("***Found: [\"MD5\"] in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
        sampleOne.append("***Found: [\"SHA\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("***Found: [\"SHA1\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 3: Used constant keys in code\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("***Found: [\"tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV\"] in Line 79 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
        sampleOne.append("***Found: [\"Bar12345Bar12345\"] in Line 152 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 10: Found constant IV in code\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
        sampleOne.append("***Found: [\"RandomInitVector\"] in Line 153 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("=======================================\n");
        sampleOne.append("***Violated Rule 9: Found constant salts in code\n");
        sampleOne.append("***Found: [\"f77aLYLo\"] in Line 80 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
        sampleOne.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
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
        File temp = new File(this.jarOne);

        assertTrue(temp.isFile());

        args = new String[]{jarType.getFlag(), this.jarOne, ""};

        redirectOutput();

        try {
            this.engine.main(args);

            resetOutput();

            assertTrue(this.customStream.toString().split("\n").length > 1);

            assertEquals(jarOneResults, this.customStream.toString());
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testSelfProject() {
		/*
		System.out.println(srcOneGrv);

		args = new String[]{srcType.getFlag(), srcOneGrv, ""};

		System.out.println(System.getenv("JAVA7_HOME"));

		//redirectOutput();

		try
		{
			this.engine.main(args);

			resetOutput();

			System.out.println(this.customStream);

			assertTrue(this.customStream.toString().split("\n").length > 1);

			assertEquals(jarOneResults, this.customStream.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
			assertNull(e);
		}
		*/
    }
    //endregion
}
package frontEnd.outputStructures;

import main.frontEnd.AnalysisIssue;
import main.frontEnd.AnalysisRule;
import main.frontEnd.OutputStructure;
import main.frontEnd.outputStructures.LegacyOutput;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class LegacyOutputTest
{

	//region Attributes
	private String result;
	private OutputStructure messagingSystem;
	private String source;
	private EngineType type;
	private ArrayList<AnalysisRule> brokenRules;
	//endregion

	//region Test Environment Setup
	@Before
	public void setUp()
	{
		this.source = "testable-jar/build/libs/testable-jar.jar";
		this.type = EngineType.JAR;

		StringBuilder sampleOne = new StringBuilder();

		//region Creating Sample Output
		//region OLD
		/*
		sampleOne.append("Analyzing "+this.type+": "+this.source);
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 5: Used export grade public Key\n");
		sampleOne.append("***Cause: Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]\n");
		sampleOne.append("=======================================\n");
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 5: Used export grade public Key\n");
		sampleOne.append("***Found: [1024] in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
		sampleOne.append("=======================================\n");
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 1: Found broken crypto schemes\n");
		sampleOne.append("***Found: [\"AES/ECB/PKCS5.appendING\"] in Method: <tester.Crypto: void <init>()>\n");
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
		*/
		//endregion
		sampleOne.append("Analyzing " + this.type + ": " + this.source + "\n");
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 5: Used export grade public Key\n");
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
		//endregion

		this.result = sampleOne.toString();

		this.messagingSystem = new LegacyOutput();

		this.brokenRules = new ArrayList<>();

		//region Adding Rules
		//region Rule 1
		AnalysisRule brokenOne = new AnalysisRule(1, "Found broken crypto schemes", "Found broken crypto schemes");
		brokenOne.addIssue(new AnalysisIssue("<tester.Crypto: void <init>()>", "AES/ECB/PKCS5PADDING", true));
		brokenOne.addIssue(new AnalysisIssue("<tester.PasswordUtils: void <init>(java.lang.String)>", "PBEWithMD5AndDES", true));
		//endregion
		//region Rule 2
		AnalysisRule brokenTwo = new AnalysisRule(2, "Found broken hash functions", "Found broken hash functions");
		brokenTwo.addIssue(new AnalysisIssue("<tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>", "MD5", true));
		brokenTwo.addIssue(new AnalysisIssue("<tester.Crypto: void main(java.lang.String[])>", "SHA", true));
		brokenTwo.addIssue(new AnalysisIssue("<tester.Crypto: void main(java.lang.String[])>", "SHA1", true));
		//endregion
		//region Rule 3
		AnalysisRule brokenThree = new AnalysisRule(3, "Used constant keys in code", "Used constant keys in code");
		brokenThree.addIssue(new AnalysisIssue("<tester.LiveVarsClass: void <clinit>()>", "aaaaaaa", true));
		brokenThree.addIssue(new AnalysisIssue("<tester.PasswordUtils: void <init>(java.lang.String)>", 79, "tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV"));
		brokenThree.addIssue(new AnalysisIssue("<tester.LiveVarsClass: void <clinit>()>", 4, "aaaaaaa"));
		brokenThree.addIssue(new AnalysisIssue("<tester.VeryBusyClass: void main(java.lang.String[])>", "helloworld", true));
		brokenThree.addIssue(new AnalysisIssue("<tester.Crypto: void main(java.lang.String[])>", 152, "Bar12345Bar12345"));
		//endregion
		//region Rule 4
		AnalysisRule brokenFour = new AnalysisRule(4, "Uses untrusted TrustManager", "Uses untrusted TrustManager");
		brokenFour.addIssue(new AnalysisIssue("tester.Crypto$2", "Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of ", false));
		brokenFour.addIssue(new AnalysisIssue("tester.Crypto$2", "Should not use unpinned self-signed certification in ", false));
		//endregion
		//region Rule 5
		AnalysisRule brokenFive = new AnalysisRule(5, "Used export grade public Key", "Used export grade public Key");
		brokenFive.addIssue(new AnalysisIssue("Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]"));
		brokenFive.addIssue(new AnalysisIssue("<tester.Crypto: java.security.KeyPair generateKeyPair()>", "1024", true));
		//endregion

		this.brokenRules.add(brokenFive);
		this.brokenRules.add(brokenOne);
		this.brokenRules.add(brokenFour);
		this.brokenRules.add(brokenTwo);
		this.brokenRules.add(brokenThree);
		//endregion
	}

	@After
	public void tearDown()
	{
		this.result = null;
		this.messagingSystem = null;
		this.source = null;
		this.type = null;
		this.brokenRules = null;
	}
	//endregion

	@Test
	public void getOutputTest()
	{
		assertEquals(this.result, this.messagingSystem.getOutput(this.source, this.type, this.brokenRules, null));
	}
}
package frontEnd.outputStructures;

import main.frontEnd.AnalysisIssue;
import main.frontEnd.AnalysisLocation;
import main.frontEnd.EnvironmentInformation;
import main.frontEnd.OutputStructure;
import main.frontEnd.outputStructures.LegacyOutput;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
 * @since 01.01
 */
public class LegacyOutputTest {

	//region Attributes
	private String result;
	private OutputStructure messagingSystem;
	private String source;
	private EnvironmentInformation env;
	private EngineType type;
	private ArrayList<AnalysisIssue> brokenRules;
	//endregion

	//region Test Environment Setup
	@Before
	public void setUp() {
		this.source = "testable-jar/build/libs/testable-jar.jar";
		this.env = new EnvironmentInformation(this.source, "", "", "", "", "", null, false, "");
		this.type = EngineType.JAR;

		StringBuilder sampleOne = new StringBuilder();

		//region Creating Sample Output
		sampleOne.append("Analyzing " + this.type + ": " + this.source + "\n");
		//region Rule 1
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 1: Found broken crypto schemes\n");
		sampleOne.append("***Found: [\"AES/ECB/PKCS5PADDING\"] in Method: <tester.Crypto: void <init>()>\n");
		sampleOne.append("***Found: [\"PBEWithMD5AndDES\"] in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
		sampleOne.append("=======================================\n");
		//endregion
		//region Rule 2
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 2: Found broken hash functions\n");
		sampleOne.append("***Found: [\"MD5\"] in Method: <tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>\n");
		sampleOne.append("***Found: [\"SHA\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
		sampleOne.append("***Found: [\"SHA1\"] in Method: <tester.Crypto: void main(java.lang.String[])>\n");
		sampleOne.append("=======================================\n");
		//endregion
		//region Rule 3
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 3: Used constant keys in code\n");
		sampleOne.append("***Found: [\"aaaaaaa\"] in Method: <tester.LiveVarsClass: void <clinit>()>\n");
		sampleOne.append("***Found: [\"tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV\"] in Line 79 in Method: <tester.PasswordUtils: void <init>(java.lang.String)>\n");
		sampleOne.append("***Found: [\"aaaaaaa\"] in Line 4 in Method: <tester.LiveVarsClass: void <clinit>()>\n");
		sampleOne.append("***Found: [\"helloworld\"] in Method: <tester.VeryBusyClass: void main(java.lang.String[])>\n");
		sampleOne.append("***Found: [\"Bar12345Bar12345\"] in Line 152 in Method: <tester.Crypto: void main(java.lang.String[])>\n");
		sampleOne.append("=======================================\n");
		//endregion
		//region Rule 4
		sampleOne.append("=======================================\n");
		//Legacy Output Slight Alteration
		//sampleOne.append("***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of tester.Crypto$2\n");
		//sampleOne.append("=======================================\n");
		//sampleOne.append("=======================================\n");
		//sampleOne.append("***Violated Rule 4: Uses untrusted TrustManager ***Should not use unpinned self-signed certification in tester.Crypto$2\n");
		sampleOne.append("***Violated Rule 4: Uses untrusted TrustManager\n");
		sampleOne.append("***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of tester.Crypto$2\n");
		sampleOne.append("***Should not use unpinned self-signed certification in tester.Crypto$2\n");
		sampleOne.append("=======================================\n");
		//endregion
		//region Rule 5
		sampleOne.append("=======================================\n");
		sampleOne.append("***Violated Rule 5: Used export grade public Key\n");
		sampleOne.append("***Cause: Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]\n");
		//Legacy Output Slight Alteration
		//sampleOne.append("=======================================\n");
		//sampleOne.append("=======================================\n");
		//sampleOne.append("***Violated Rule 5: Used export grade public Key\n");
		//sampleOne.append("***Found: ["1024"] in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
		sampleOne.append("***Found: [\"1024\"] in Method: <tester.Crypto: java.security.KeyPair generateKeyPair()>\n");
		sampleOne.append("=======================================\n");
		//endregion
		//endregion

		this.result = sampleOne.toString();

		this.messagingSystem = new LegacyOutput();

		this.brokenRules = new ArrayList<>();

		//region Adding Rules
		//region Rule 5
		brokenRules.add(new AnalysisIssue(5, "Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]"));
		brokenRules.add(new AnalysisIssue(5, "<tester.Crypto: java.security.KeyPair generateKeyPair()>", "1024"));
		//endregion
		//region Rule 1
		brokenRules.add(new AnalysisIssue(1, "<tester.Crypto: void <init>()>", "AES/ECB/PKCS5PADDING"));
		brokenRules.add(new AnalysisIssue(1, "<tester.PasswordUtils: void <init>(java.lang.String)>", "PBEWithMD5AndDES"));
		//endregion
		//region Rule 4
		brokenRules.add(new AnalysisIssue("tester.Crypto$2", 4, "Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of "));
		brokenRules.add(new AnalysisIssue("tester.Crypto$2", 4, "Should not use unpinned self-signed certification in "));
		//endregion
		//region Rule 2
		brokenRules.add(new AnalysisIssue(2, "<tester.PBEUsage: javax.crypto.spec.PBEKeySpec getPBEParameterSpec(java.lang.String)>", "MD5"));
		brokenRules.add(new AnalysisIssue(2, "<tester.Crypto: void main(java.lang.String[])>", "SHA"));
		brokenRules.add(new AnalysisIssue(2, "<tester.Crypto: void main(java.lang.String[])>", "SHA1"));
		//endregion
		//region Rule 3
		brokenRules.add(new AnalysisIssue(3, "<tester.LiveVarsClass: void <clinit>()>", "aaaaaaa"));
		brokenRules.add(new AnalysisIssue(3, "<tester.PasswordUtils: void <init>(java.lang.String)>", "tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV", new AnalysisLocation(79, 79)));
		brokenRules.add(new AnalysisIssue(3, "<tester.LiveVarsClass: void <clinit>()>", "aaaaaaa", new AnalysisLocation(4, 4)));
		brokenRules.add(new AnalysisIssue(3, "<tester.VeryBusyClass: void main(java.lang.String[])>", "helloworld"));
		brokenRules.add(new AnalysisIssue(3, "<tester.Crypto: void main(java.lang.String[])>", "Bar12345Bar12345", new AnalysisLocation(152, 152)));
		//endregion
		//endregion
	}

	@After
	public void tearDown() {
		this.result = null;
		this.messagingSystem = null;
		this.source = null;
		this.type = null;
		this.brokenRules = null;
	}
	//endregion

	@Test
	public void getOutputTest() {
		assertEquals(this.result, this.messagingSystem.getOutput(env, this.type, this.brokenRules, null));
	}
}
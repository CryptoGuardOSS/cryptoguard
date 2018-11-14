package frontEnd;

import main.frontEnd.AnalysisIssue;
import main.frontEnd.AnalysisRule;
import main.frontEnd.MessageRepresentation;
import main.frontEnd.outputStructures.LegacyOutput;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MessageRepresentationTest {
	//region Attributes
	private MessageRepresentation outputEngine;
	private final String jarOneName = "testable-jar/build/libs/testable-jar.jar";
	private final String jarOneAnalysis = "Analyzing JAR: testable-jar/build/libs/testable-jar.jar\n";
	private final EngineType jarType = EngineType.JAR;
	private AnalysisIssue ruleOneIssue;
	private AnalysisIssue ruleTwoIssue;
	//endregion

	//region Test Environment Creation
	@Before
	public void setUp() throws Exception {
		this.ruleOneIssue = new AnalysisIssue("<tester.Crypto: void <init>()>", "AES/ECB/PKCS5PADDING", true);
		this.ruleTwoIssue = new AnalysisIssue("<tester.PasswordUtils: void <init>(java.lang.String)>", "PBEWithMD5AndDES", true);
	}

	@After
	public void tearDown() throws Exception {
		this.outputEngine = null;
		this.ruleOneIssue = null;
		this.ruleTwoIssue = null;
	}
	//endregion

	//region Tests
	@Test
	public void legacyTest0() {
		this.outputEngine = new MessageRepresentation(this.jarOneName, this.jarType, "L");

		assertNotNull(this.outputEngine);
		assertEquals(this.jarOneName, this.outputEngine.getSource());
		assertEquals(this.jarType, this.outputEngine.getType());
		assertTrue(this.outputEngine.getMessageEngine() instanceof LegacyOutput);
		assertEquals(0, this.outputEngine.getAnalysisIssues().size());
	}

	@Test
	public void addLegacyRuleAnalysis() {
		this.outputEngine = new MessageRepresentation(this.jarOneName, this.jarType, "L");

		this.outputEngine.addRuleAnalysis(1, this.ruleOneIssue);

		assertEquals(1, this.outputEngine.getAnalysisIssues().get(0).getIssues().size());
		assertEquals(this.ruleOneIssue.getLocationName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getLocationName());
		assertEquals(this.ruleOneIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getCapturedInformation());
	}

	@Test
	public void addLegacyRuleAnalysis1() {
		this.outputEngine = new MessageRepresentation(this.jarOneName, this.jarType, "L");

		ArrayList<AnalysisIssue> issues = new ArrayList<>();
		issues.add(this.ruleOneIssue);
		issues.add(this.ruleTwoIssue);

		this.outputEngine.addRuleAnalysis(1, issues);

		assertEquals(2, this.outputEngine.getAnalysisIssues().get(0).getIssues().size());

		assertEquals(this.ruleOneIssue.getLocationName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getLocationName());
		assertEquals(this.ruleOneIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getCapturedInformation());

		assertEquals(this.ruleTwoIssue.getLocationName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getLocationName());
		assertEquals(this.ruleTwoIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getCapturedInformation());

	}

	@Test
	public void addLegacyRuleAnalysis2() {
		this.outputEngine = new MessageRepresentation(this.jarOneName, this.jarType, "L");

		AnalysisRule newRule = new AnalysisRule(1, new AnalysisIssue[]{this.ruleOneIssue, this.ruleTwoIssue});

		this.outputEngine.addRuleAnalysis(newRule);

		assertEquals(1, this.outputEngine.getAnalysisIssues().size());

		assertEquals(2, this.outputEngine.getAnalysisIssues().get(0).getIssues().size());

		assertEquals(this.ruleOneIssue.getLocationName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getLocationName());
		assertEquals(this.ruleOneIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getCapturedInformation());

		assertEquals(this.ruleTwoIssue.getLocationName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getLocationName());
		assertEquals(this.ruleTwoIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getCapturedInformation());

	}

	@Test
	public void getLegacyMessage() {
		this.outputEngine = new MessageRepresentation(this.jarOneName, this.jarType, "L");
		String message = (String) this.outputEngine.getMessage();

		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

		assertNotNull(message);

		assertEquals(this.jarOneAnalysis, message);

	}
	//endregion
}
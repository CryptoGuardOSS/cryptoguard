package frontEnd;

import main.frontEnd.AnalysisIssue;
import main.frontEnd.EnvironmentInformation;
import main.frontEnd.MessageRepresentation;
import main.frontEnd.outputStructures.LegacyOutput;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
 * @since V01.01
 */
public class MessageRepresentationTest {
	//region Attributes
	private MessageRepresentation outputEngine;
	private final String jarOneName = "testable-jar/build/libs/testable-jar.jar";
	private final String jarOneAnalysis = "Analyzing JAR: testable-jar/build/libs/testable-jar.jar\n";
	private final EngineType jarType = EngineType.JAR;
	private EnvironmentInformation env;
	private AnalysisIssue ruleOneIssue;
	private AnalysisIssue ruleTwoIssue;
	//endregion

	//region Test Environment Creation
	@Before
	public void setUp() throws Exception {
		this.env = new EnvironmentInformation(jarOneName, "", "", "", "", "", null, false, "");
		this.ruleOneIssue = new AnalysisIssue(1, "<tester.Crypto: void <init>()>", "AES/ECB/PKCS5PADDING");
		this.ruleTwoIssue = new AnalysisIssue(1, "<tester.PasswordUtils: void <init>(java.lang.String)>", "PBEWithMD5AndDES");
	}

	@After
	public void tearDown() throws Exception {
		this.outputEngine = null;
		this.ruleOneIssue = null;
		this.ruleTwoIssue = null;
		this.env = null;
	}
	//endregion

	//region Tests
	@Test
	public void legacyTest0() {
		this.outputEngine = new MessageRepresentation(this.env, this.jarType, "L");

		assertNotNull(this.outputEngine);
		assertEquals(this.jarOneName, this.outputEngine.getEnvironment().getSource());
		assertEquals(this.jarType, this.outputEngine.getType());
		assertTrue(this.outputEngine.getMessageEngine() instanceof LegacyOutput);
		assertEquals(0, this.outputEngine.getAnalysisIssues().size());
	}

	//TODO - Update these tests
	/*
	@Test
	public void addLegacyRuleAnalysis() {
		this.outputEngine = new MessageRepresentation(this.env, this.jarType, "L");

		this.outputEngine.addRuleAnalysis(1, this.ruleOneIssue);

		assertEquals(1, this.outputEngine.getAnalysisIssues().get(0).getIssues().size());
		assertEquals(this.ruleOneIssue.getClassName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getClassName());
		assertEquals(this.ruleOneIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getCapturedInformation());
	}

	@Test
	public void addLegacyRuleAnalysis1() {
		this.outputEngine = new MessageRepresentation(this.env, this.jarType, "L");

		ArrayList<AnalysisIssue> issues = new ArrayList<>();
		issues.add(this.ruleOneIssue);
		issues.add(this.ruleTwoIssue);

		this.outputEngine.addRuleAnalysis(1, issues);

		assertEquals(2, this.outputEngine.getAnalysisIssues().get(0).getIssues().size());

		assertEquals(this.ruleOneIssue.getClassName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getClassName());
		assertEquals(this.ruleOneIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getCapturedInformation());

		assertEquals(this.ruleTwoIssue.getClassName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getClassName());
		assertEquals(this.ruleTwoIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getCapturedInformation());

	}

	@Test
	public void addLegacyRuleAnalysis2() {
		this.outputEngine = new MessageRepresentation(this.jarOneName, this.jarType, "L");

		AnalysisRule newRule = new AnalysisRule(1, new AnalysisIssue[]{this.ruleOneIssue, this.ruleTwoIssue});

		this.outputEngine.addRuleAnalysis(newRule);

		assertEquals(1, this.outputEngine.getAnalysisIssues().size());

		assertEquals(2, this.outputEngine.getAnalysisIssues().get(0).getIssues().size());

		assertEquals(this.ruleOneIssue.getClassName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getClassName());
		assertEquals(this.ruleOneIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(0).getCapturedInformation());

		assertEquals(this.ruleTwoIssue.getClassName(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getClassName());
		assertEquals(this.ruleTwoIssue.getCapturedInformation(), this.outputEngine.getAnalysisIssues().get(0).getIssues().get(1).getCapturedInformation());

	}
	*/

	@Test
	public void getLegacyMessage() {
		this.outputEngine = new MessageRepresentation(this.env, this.jarType, "L");
		String message = (String) this.outputEngine.getMessage();

		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

		assertNotNull(message);

		assertEquals(this.jarOneAnalysis, message);

	}
	//endregion
}
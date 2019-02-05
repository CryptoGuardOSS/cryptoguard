package main.frontEnd.MessagingSystem;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.01
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
    }

    @After
    public void tearDown() throws Exception {
        this.outputEngine = null;
        this.ruleOneIssue = null;
        this.ruleTwoIssue = null;
        this.env = null;
    }
    //endregion


}
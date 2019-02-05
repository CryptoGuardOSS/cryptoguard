package main.frontEnd.MessagingSystem.routing.outputStructures;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.01
 */
public class LegacyTest {

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
        this.type = EngineType.JAR;

        StringBuilder sampleOne = new StringBuilder();

        this.result = sampleOne.toString();

        this.messagingSystem = new Legacy();

        this.brokenRules = new ArrayList<>();
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
}
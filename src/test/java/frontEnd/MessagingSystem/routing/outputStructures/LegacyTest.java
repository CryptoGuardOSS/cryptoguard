package frontEnd.MessagingSystem.routing.outputStructures;

import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.junit.After;
import org.junit.Before;
import rule.engine.EngineType;

import java.util.ArrayList;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author CryptoguardTeam
 * @version $Id: $Id
 * @since V01.00.01
 */
public class LegacyTest {

    //region Attributes
    private String result;
    private String source;
    private EnvironmentInformation env;
    private EngineType type;
    private ArrayList<AnalysisIssue> brokenRules;
    //endregion

    //region Test Environment Setup

    /**
     * <p>setUp.</p>
     */
    @Before
    public void setUp() {
        this.source = "testable-jar/build/libs/testable-jar.jar";
        this.type = EngineType.JAR;

        this.result = "";


        this.brokenRules = new ArrayList<>();
        //endregion
    }

    /**
     * <p>tearDown.</p>
     */
    @After
    public void tearDown() {
        this.result = null;
        this.source = null;
        this.type = null;
        this.brokenRules = null;
    }
    //endregion
}

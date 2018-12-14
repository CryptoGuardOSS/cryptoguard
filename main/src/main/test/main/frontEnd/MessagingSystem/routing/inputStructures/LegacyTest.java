package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LegacyTest {

    //region Attributes
    private EnvironmentInformation testInfo;
    private String[] args;
    private Legacy inputTest;
    private final EngineType type = EngineType.APK;
    private final String dependencies = null;
    private final String helpString = "No extra inputs are needed for this output type\nThis is the default output type";
    //endregion

    //region Test Environment Management
    @Before
    public void setUp() throws Exception {
        inputTest = new Legacy();
        args = new String[]{"TestSource", type.getName(), dependencies, Listing.Legacy.getFlag()};
        testInfo = new EnvironmentInformation(args[0], type, args[2], args[3]);
    }

    @After
    public void tearDown() throws Exception {
        testInfo = null;
        args = null;
        inputTest = null;
    }
    //endregion

    //region Tests
    @Test
    public void testSampleCreation() {
        EnvironmentInformation newInfo = inputTest.inputValidation(args, dependencies, type);

        assertNotNull(newInfo);
        assertEquals(testInfo.getMessagingType(), newInfo.getMessagingType());
        assertEquals(testInfo.getSource(), newInfo.getSource());
        assertEquals(testInfo.getSourceDependencies(), newInfo.getSourceDependencies());
        assertEquals(testInfo.getSourceType(), newInfo.getSourceType());
    }

    @Test
    public void testHelpInfo() {
        assertEquals(helpString, inputTest.helpInfo());
    }
    //endregion
}
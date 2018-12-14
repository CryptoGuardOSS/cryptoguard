package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScarfXMLTest {

    //region Attributes
    private EnvironmentInformation testInfo;
    private String[] args;
    private ScarfXML inputTest;
    private final EngineType type = EngineType.APK;
    private final String dependencies = null;
    private String helpString;
    //endregion

    //region Test Environment Management
    @Before
    public void setUp() throws Exception {
        inputTest = new ScarfXML();
        args = new String[]{"TestSource", type.getName(), dependencies, Listing.ScarfXML.getFlag()};
        testInfo = new EnvironmentInformation(args[0], type, args[2], args[3]);

        StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append("Usage: (AssessmentFramework) (AssessmentFrameworkVersion) (BuildRootDir) (PackageRootDir) (ParserName) (ParserVersion)\n")
                .append("\tAssessmentFramework: Default => STUBBED").append("\n")
                .append("\tAssessmentFrameworkVersion: Default => STUBBED").append("\n")
                .append("\tBuildRootDir: Default => STUBBED").append("\n")
                .append("\tPackageRootDir: Default => STUBBED").append("\n")
                .append("\tParserName: Default => STUBBED").append("\n")
                .append("\tParserVersion: Default => STUBBED");

        helpString = helpBuilder.toString();

    }

    @After
    public void tearDown() throws Exception {
        testInfo = null;
        args = null;
        inputTest = null;
        helpString = null;
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
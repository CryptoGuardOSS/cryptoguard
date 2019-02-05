package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;

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

}
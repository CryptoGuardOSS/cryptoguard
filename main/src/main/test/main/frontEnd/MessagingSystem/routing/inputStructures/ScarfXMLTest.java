package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.frontEnd.argsIdentifier;
import main.rule.engine.EngineType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ScarfXMLTest {

    //region Attributes
    private EnvironmentInformation testInfo;
    private String[] args;
    private ScarfXML inputTest;
    private final EngineType type = EngineType.APK;
    private final String dependencies = null;
    private String helpString;
    private String assessmentFramework;
    private String assessmentFrameworkVersion;
    private String buildRootDir;
    private String packageRootDir;
    private String parserName;
    private String parserVersion;
    private String UUID;
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

        assessmentFramework = "java-assess";
        assessmentFrameworkVersion = "1.0.0c";
        buildRootDir = "/home";
        packageRootDir = "CryptoGuard";
        parserName = "CryptoGuard";
        parserVersion = "1970-01-01";
        UUID = "8675309";

    }

    @After
    public void tearDown() throws Exception {
        testInfo = null;
        args = null;
        inputTest = null;
        helpString = null;
        assessmentFramework = null;
        assessmentFrameworkVersion = null;
        buildRootDir = null;
        packageRootDir = null;
        parserName = null;
        parserVersion = null;
        UUID = null;
    }
    //endregion

    //region Helper Methods
    private String makeArg(String id, String value) {
        return "-" + id + " " + value + " ";
    }
    //endregion

    @Test
    public void testAllArguments_inputValidation() {
        StringBuilder args = new StringBuilder();

        //region Setting the arguments string
        args.append(makeArg(ScarfXMLId.AssessmentFramework.getId(), assessmentFramework));
        args.append(makeArg(ScarfXMLId.AssessmentFrameworkVersion.getId(), assessmentFrameworkVersion));
        args.append(makeArg(ScarfXMLId.BuildRootDir.getId(), buildRootDir));
        args.append(makeArg(ScarfXMLId.PackageRootDir.getId(), packageRootDir));
        args.append(makeArg(ScarfXMLId.ParserName.getId(), parserName));
        args.append(makeArg(ScarfXMLId.ParserVersion.getId(), parserVersion));
        args.append(makeArg(ScarfXMLId.UUID.getId(), UUID));
        //endregion

        testInfo = new EnvironmentInformation(new ArrayList<String>(), EngineType.JAR, Listing.ScarfXML, null, new ArrayList<String>(), "");

        try {
            assertTrue(inputTest.inputValidation(testInfo, args.toString().split(" ")));

            //region Checking arguments
            assertEquals(assessmentFramework, testInfo.getAssessmentFramework());
            assertEquals(assessmentFrameworkVersion, testInfo.getAssessmentFrameworkVersion());
            assertEquals(buildRootDir, testInfo.getBuildRootDir());
            assertEquals(packageRootDir, testInfo.getPackageRootDir());
            assertEquals(parserName, testInfo.getParserName());
            assertEquals(parserVersion, testInfo.getParserVersion());
            assertEquals(UUID, testInfo.getUUID());
            //endregion
        } catch (Exception e) {
            assertNull(e);
            e.printStackTrace();
        }

    }

    @Test
    public void testAllArguments_inputValidation_realisticArgs() {
        StringBuilder args = new StringBuilder();

        //region Setting the arguments string
        args.append(makeArg(argsIdentifier.FORMAT.getId(), "jar"));
        args.append(makeArg(argsIdentifier.SOURCE.getId(), "./testable-jar.jar"));
        args.append(makeArg(argsIdentifier.DEPENDENCY.getId(), "./testable-jar/build/dependencies"));
        args.append(makeArg(argsIdentifier.OUT.getId(), "./results.xml"));
        args.append(makeArg(ScarfXMLId.AssessmentFramework.getId(), assessmentFramework));
        args.append(makeArg(ScarfXMLId.AssessmentFrameworkVersion.getId(), assessmentFrameworkVersion));
        args.append(makeArg(ScarfXMLId.BuildRootDir.getId(), buildRootDir));
        args.append(makeArg(ScarfXMLId.PackageRootDir.getId(), packageRootDir));
        args.append(makeArg(ScarfXMLId.ParserName.getId(), parserName));
        args.append(makeArg(ScarfXMLId.ParserVersion.getId(), parserVersion));
        args.append(makeArg(ScarfXMLId.UUID.getId(), UUID));
        //endregion

        testInfo = new EnvironmentInformation(new ArrayList<String>(), EngineType.JAR, Listing.ScarfXML, null, new ArrayList<String>(), "");

        try {
            assertTrue(inputTest.inputValidation(testInfo, args.toString().split(" ")));

            //region Checking arguments
            assertEquals(assessmentFramework, testInfo.getAssessmentFramework());
            assertEquals(assessmentFrameworkVersion, testInfo.getAssessmentFrameworkVersion());
            assertEquals(buildRootDir, testInfo.getBuildRootDir());
            assertEquals(packageRootDir, testInfo.getPackageRootDir());
            assertEquals(parserName, testInfo.getParserName());
            assertEquals(parserVersion, testInfo.getParserVersion());
            assertEquals(UUID, testInfo.getUUID());
            //endregion
        } catch (Exception e) {
            assertNull(e);
            e.printStackTrace();
        }

    }

}
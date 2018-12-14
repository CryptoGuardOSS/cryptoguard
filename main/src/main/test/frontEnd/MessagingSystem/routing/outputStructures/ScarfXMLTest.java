package frontEnd.MessagingSystem.routing.outputStructures;

import com.example.response.AnalyzerReport;
import com.example.response.BugCategoryType;
import com.example.response.BugInstanceType;
import com.example.response.LocationType;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.AnalysisLocation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import main.frontEnd.MessagingSystem.routing.outputStructures.ScarfXML;
import main.rule.engine.Criteria;
import main.rule.engine.EngineType;
import main.rule.engine.RuleList;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.03
 */
public class ScarfXMLTest {

    //region Test Attributes
    private String propertyErrorMessage;
    private String marshallErrorMessage;
    private String result;
    private OutputStructure messagingSystem;
    private EnvironmentInformation env;
    private EngineType type;
    private ArrayList<AnalysisIssue> brokenRules;
    private final String SchemaPath = System.getProperty("user.dir") + "/src/main/";
    private final File ScarfSchema = new File(SchemaPath + "schema/xsd/Scarf/Scarf.xsd");
    private final String jar = "testable-jar.jar";
    private final String pathToJar = "testable-jar/build/libs/";
    private String source = this.pathToJar + this.jar;
    //endregion

    //region Test Environment Management
    @Before
    public void setUp() throws Exception {
        propertyErrorMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ERROR>\nThere has been an issue setting properties.\n</ERROR>";
        marshallErrorMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ERROR>\nThere has been an issue marshalling the output.\n</ERROR>";


        this.type = EngineType.JAR;

        this.env = new EnvironmentInformation(this.source, this.type, null, Listing.ScarfXML.getFlag());
        //region Setting Scarf XML Required Fields
        this.env.setAssessmentFramework("STUBBED");
        this.env.setAssessmentFrameworkVersion("STUBBED");
        this.env.setBuildRootDir("STUBBED");
        this.env.setPackageRootDir("STUBBED");
        this.env.setParserName("STUBBED");
        this.env.setParserVersion("STUBBED");
        this.env.setPackageName(this.jar);
        this.env.setPackageVersion("0.0");
        //endregion
        this.env.setPrettyPrint(true);


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

        this.messagingSystem = new ScarfXML();

        this.brokenRules = new ArrayList<>();

        //region Adding Rules
        //region Rule 1
        brokenRules.add(new AnalysisIssue(1, "<tester.Crypto: void <init>()>", "AES/ECB/PKCS5PADDING"));
        brokenRules.add(new AnalysisIssue(1, "<tester.PasswordUtils: void <init>(java.lang.String)>", "PBEWithMD5AndDES"));
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
        //region Rule 4
        brokenRules.add(new AnalysisIssue("tester.Crypto$2", 4, "Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of "));
        brokenRules.add(new AnalysisIssue("tester.Crypto$2", 4, "Should not use unpinned self-signed certification in "));
        //endregion
        //region Rule 5
        brokenRules.add(new AnalysisIssue(5, "Used default key size in method: <tester.Crypto: java.security.KeyPair generateKeyPairDefaultKeySize()>[122]"));
        brokenRules.add(new AnalysisIssue(5, "<tester.Crypto: java.security.KeyPair generateKeyPair()>", "1024"));
        //endregion
        //region Rule 6
        Criteria simpleCriteriaOne = new Criteria();
        simpleCriteriaOne.setClassName("ClassOne");
        simpleCriteriaOne.setMethodName("MethodOne");

        Criteria simpleCriteriaTwo = new Criteria();
        simpleCriteriaTwo.setClassName("ClassTwo");
        simpleCriteriaTwo.setMethodName("MethodTwo");

        Criteria simpleCriteriaThree = new Criteria();
        simpleCriteriaThree.setClassName("ClassThree");
        simpleCriteriaThree.setMethodName("MethodThree");

        brokenRules.add(new AnalysisIssue(simpleCriteriaOne, "Borked Message Here", 6));
        brokenRules.add(new AnalysisIssue(simpleCriteriaTwo, "New Borked Message Here", 6));
        brokenRules.add(new AnalysisIssue(simpleCriteriaThree, "The Borked Message Here", 6));
        //endregion
        //endregion
    }

    @After
    public void tearDown() throws Exception {
        this.propertyErrorMessage = null;
        this.marshallErrorMessage = null;
        this.result = null;
        this.messagingSystem = null;
        this.source = null;
        this.type = null;
        this.brokenRules = null;
    }
    //endregion

    //region Tests
    @Test
    public void getOutput() {
    }

    @Test
    public void getPropertyError() {

    }

    @Test
    public void getJAXBError() {

    }

    @Test
    public void simpleFiveRuleTest() {
        String xmlStream = this.messagingSystem.getOutput(this.env, this.brokenRules);
        this.env.openConsoleStream();

        assertTrue(StringUtils.isNoneBlank(xmlStream));
        assertFalse(xmlStream.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        try {
            //Creating the settings for the unmarshaller
            Unmarshaller unmarshaller = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(ScarfSchema);
            unmarshaller.setSchema(schema);

            //Unmarshalling the AnalyzerReport from the stream, as well validating the xml
            AnalyzerReport report = (AnalyzerReport) unmarshaller.unmarshal(new ByteArrayInputStream(xmlStream.getBytes(StandardCharsets.UTF_8)));
            assertNotNull(report);

            //Keeping the dictionary of the bugs
            HashMap<Integer, Integer> countOfBugs = new HashMap<Integer, Integer>();

            //region Validating the bugs from the AnalyzerReport
            assertEquals(brokenRules.size(), report.getBugInstance().size());
            for (int bugNumber = 0; bugNumber < brokenRules.size(); bugNumber++) {
                AnalysisIssue currentIssue = brokenRules.get(bugNumber);
                BugInstanceType currentBug = report.getBugInstance().get(bugNumber);

                //region Adding a dictionary of bugs
                if (!countOfBugs.containsKey(Integer.valueOf(currentBug.getBugCode()))) {
                    countOfBugs.put(Integer.valueOf(currentBug.getBugCode()), 1);
                } else {
                    countOfBugs.put(Integer.valueOf(currentBug.getBugCode()), countOfBugs.get(Integer.valueOf(currentBug.getBugCode())) + 1);
                }

                //endregion

                assertEquals(StringUtils.trimToNull(currentIssue.getClassName()), currentBug.getClassName());
                if (!currentIssue.getMethods().isEmpty()) {
                    assertEquals(currentIssue.getMethods().size(), currentBug.getMethods().getMethod().size());
                }

                for (int methodNumber = 0; methodNumber < currentIssue.getMethods().size(); methodNumber++)
                    assertEquals(currentIssue.getMethods().get(methodNumber), currentBug.getMethods().getMethod().get(methodNumber).getValue());

                for (int locNumber = 0; locNumber < currentIssue.getLocations().size(); locNumber++) {
                    AnalysisLocation currentLoc = currentIssue.getLocations().get(locNumber);
                    LocationType currentBugLoc = currentBug.getBugLocations().getLocation().get(locNumber);

                    assertEquals(currentLoc.getLineStart(), currentBugLoc.getStartLine());

                    if (!currentLoc.getLineStart().equals(currentLoc.getLineEnd())) {
                        assertEquals(currentLoc.getLineEnd(), currentBugLoc.getEndLine());
                    }

                }
            }
            //endregion

            //region Validating the Bug Summary
            assertEquals(countOfBugs.size(), report.getBugSummary().getBugCategory().size());
            for (BugCategoryType bugCategory : report.getBugSummary().getBugCategory()) {
                assertEquals(RuleList.getRuleByRuleNumber(Integer.valueOf(bugCategory.getCode())).getDesc(), bugCategory.getGroup());
                assertEquals(countOfBugs.get(Integer.valueOf(bugCategory.getCode())).intValue(), bugCategory.getCount());
            }
            //endregion
        } catch (JAXBException | SAXException e) {
            assertNull(e);
            e.printStackTrace();
        }
    }
    //endregion
}
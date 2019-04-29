package frontEnd.MessagingSystem.routing.outputStructures;

import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import rule.engine.EngineType;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static test.TestUtilities.jarOne;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.03
 */
public class ScarfXMLTest {

    //region Test Attributes
    private String propertyErrorMessage;
    private String marshallErrorMessage;
    private String result;
    private EnvironmentInformation env;
    private EngineType type;
    private ArrayList<AnalysisIssue> brokenRules;
    //endregion

    //region Test Environment Management

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
        propertyErrorMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ERROR>\nThere has been an issue setting properties.\n</ERROR>";
        marshallErrorMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ERROR>\nThere has been an issue marshalling the output.\n</ERROR>";


        this.type = EngineType.JAR;

        this.env = new EnvironmentInformation(Arrays.asList(jarOne), this.type, Listing.ScarfXML, null, null, null);
        //region Setting Scarf XML Required Fields
        this.env.setAssessmentFramework("STUBBED");
        this.env.setAssessmentFrameworkVersion("STUBBED");
        this.env.setBuildRootDir("STUBBED");
        this.env.setPackageRootDir("STUBBED");
        this.env.setParserName("STUBBED");
        this.env.setParserVersion("STUBBED");
        this.env.setPackageName(jarOne);
        this.env.setPackageVersion("0.0");
        //endregion
        this.env.setPrettyPrint(true);


        StringBuilder sampleOne = new StringBuilder();
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        this.propertyErrorMessage = null;
        this.marshallErrorMessage = null;
        this.result = null;
        this.type = null;
        this.brokenRules = null;
    }
    //endregion

    //region Tests
    //@Test

    /**
     * <p>simpleFiveRuleTest.</p>
     */
    public void simpleFiveRuleTest() {
        //String xmlStream = this.messagingSystem.getOutput(this.env, this.brokenRules);
        //TODO - Fix this
        String xmlStream = "";
        assertTrue(StringUtils.isNoneBlank(xmlStream));
        assertFalse(xmlStream.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        /*
        try {
            //Creating the settings for the unmarshaller
            Unmarshaller unmarshaller = JAXBContext.newInstance(AnalyzerReport.class).createUnmarshaller();
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(TestUtilities.pathToSchema));
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
        */
    }
    //endregion
}

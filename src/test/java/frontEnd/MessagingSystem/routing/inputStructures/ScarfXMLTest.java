package frontEnd.MessagingSystem.routing.inputStructures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Scarf.Method;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import rule.engine.EngineType;
import test.TestUtilities;

import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;

/**
 * <p>ScarfXMLTest class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
@RunWith(MockitoJUnitRunner.class)
public class ScarfXMLTest {

    //region Attributes
    private EnvironmentInformation testInfo;
    private String[] args;
    private ScarfXML inputTest;
    private final EngineType type = EngineType.APK;
    private final String dependencies = null;
    private String assessment_start_ts;
    private String build_fw;
    private String build_fw_version;
    private String package_name;
    private String package_version;
    private String assess_fw;
    private String assess_fw_version;
    private String build_root_dir;
    private String package_root_dir;
    private String parser_fw;
    private String parser_fw_version;
    private String uuid;
    private Properties sampleProp = new Properties();
    //endregion

    //region Test Environment Management

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
        inputTest = spy(ScarfXML.class);

        args = new String[]{"TestSource", type.getName(), dependencies, Listing.ScarfXML.getFlag()};

        assess_fw = "java-assess";
        assess_fw_version = "1.0.0c";
        assessment_start_ts = "1516116551.639144";
        build_fw = "c-assess";
        build_fw_version = "1.1.12";
        build_root_dir = "/home";
        package_name = "Rigorityj";
        package_root_dir = "CryptoGuard";
        package_version = "8675309";
        parser_fw = "example_tool";
        parser_fw_version = "x.y.z";
        uuid = "fa109792-9234-4jk2-9f68-alp9woofbeef";

        sampleProp.setProperty("assess_fw", assess_fw);
        sampleProp.setProperty("assess_fw_version", assess_fw_version);
        sampleProp.setProperty("assessment_start_ts", assessment_start_ts);
        sampleProp.setProperty("build_fw", build_fw);
        sampleProp.setProperty("build_fw_version", build_fw_version);
        sampleProp.setProperty("build_root_dir", build_root_dir);
        sampleProp.setProperty("package_name", package_name);
        sampleProp.setProperty("package_root_dir", package_root_dir);
        sampleProp.setProperty("parser_fw", parser_fw);
        sampleProp.setProperty("parser_fw_version", parser_fw_version);
        sampleProp.setProperty("package_version", package_version);
        parser_fw = "example_tool";
        parser_fw_version = "x.y.z";
        sampleProp.setProperty("uuid", uuid);
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        testInfo = null;
        args = null;
        inputTest = null;
        assess_fw = null;
        assess_fw_version = null;
        assessment_start_ts = null;
        build_fw = null;
        build_fw_version = null;
        build_root_dir = null;
        package_root_dir = null;
        package_name = null;
        package_root_dir = null;
        package_version = null;
        parser_fw = null;
        parser_fw_version = null;
        uuid = null;
        sampleProp = null;
    }
    //endregion

    /**
     * <p>simpleTest_1.</p>
     */
    @Test
    public void simpleTest_1() {
        try {
            Method method = new Method(23, true, "hai");

            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(method);
            assertNotNull(xml);
            System.out.println(xml);
        } catch (JsonProcessingException e) {
            assertNull(e);
            e.printStackTrace();
        }
    }

    /**
     * <p>testAllArguments_inputValidation.</p>
     */
    @Test
    public void testAllArguments_inputValidation() {
        StringBuilder args = new StringBuilder();

        //region Setting the arguments string
        args.append(TestUtilities.makeArg(ScarfXMLId.ConfigFile.getId(), assess_fw));
        //endregion

        try {
            testInfo = new EnvironmentInformation(Arrays.asList("test"), EngineType.JAR, Listing.ScarfXML, null, Arrays.asList("test"), "");

            doReturn(sampleProp).when(inputTest).loadProperties(Mockito.anyString());

            assertTrue(inputTest.inputValidation(testInfo, args.toString().split(" ")));

            //region Checking arguments
            assertEquals(assess_fw, testInfo.getAssessmentFramework());
            assertEquals(assess_fw_version, testInfo.getAssessmentFrameworkVersion());
            assertEquals(assessment_start_ts, testInfo.getAssessmentStartTime());
            assertEquals(build_fw, testInfo.getBuildFramework());
            assertEquals(build_fw_version, testInfo.getBuildFrameworkVersion());
            assertEquals(build_root_dir, testInfo.getBuildRootDir());
            assertEquals(package_root_dir, testInfo.getPackageRootDir());
            assertEquals(package_name, testInfo.getPackageName());
            assertEquals(package_version, testInfo.getPackageVersion());
            assertEquals(uuid, testInfo.getUUID());
            //endregion
        } catch (Exception e) {
            assertNull(e);
            e.printStackTrace();
        }

    }

}

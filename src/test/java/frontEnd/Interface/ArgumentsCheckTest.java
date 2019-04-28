package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import util.Utils;

import java.io.File;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static test.TestUtilities.*;

/**
 * <p>ArgumentsCheckTest class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class ArgumentsCheckTest {

    //region Attributes
    private EnvironmentInformation info;

    private final String fileOut = Utils.osPathJoin(testPath, "txt.xml");
    private final String fileOutTxt = Utils.osPathJoin(testPath, "txt.txt");
    //endregion

    //region Test Environment Setup

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        info = null;
    }
    //endregion

    //region Tests

    /**
     * <p>testEnvironmentVariables.</p>
     */
    @Test
    public void testEnvironmentVariables() {
        String[] fileLists = new String[]{jarOne};
        String[] dirLists = new String[]{srcOneGrv, srcOneGrvDep};

        for (String file : fileLists) {
            File tempFile = new File(file);

            assertTrue(tempFile.exists());
            assertTrue(tempFile.isFile());
        }

        for (String dir : dirLists) {
            File tempDir = new File(dir);

            assertTrue(tempDir.exists());
            assertTrue(tempDir.isDirectory());
        }


    }

    /**
     * <p>paramaterCheck_jar_SkipValidation.</p>
     */
    @Test
    public void paramaterCheck_jar_SkipValidation() {
        StringBuilder args = new StringBuilder();

        args.append("-in").append(" ").append(EngineType.JAR.getFlag()).append(" ");
        args.append("-s ").append(jarOne).append(" ");
        args.append("-d ").append(srcOneGrvDep).append(" ");
        args.append("-o ").append(fileOutTxt).append(" ");
        args.append("-t").append(" ");
        args.append("-n").append(" ");
        args.append("-x");

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(args.toString().split(" ")));
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }

        assertNotNull(info);
        assertEquals(EngineType.JAR, info.getSourceType());
        assertEquals(1, info.getSource().size());
        assertEquals(jarOne, info.getSource().get(0));
        assertEquals(1, info.getDependencies().size());
        assertEquals(srcOneGrvDep, info.getDependencies().get(0));
        assertTrue(info.isShowTimes());
        assertTrue(info.getPrettyPrint());
        assertEquals(fileOutTxt, info.getFileOut());
        assertEquals(Listing.Legacy, info.getMessagingType());
    }

    /**
     * <p>paramaterCheck_jar.</p>
     */
    @Test
    public void paramaterCheck_jar() {

        StringBuilder args = new StringBuilder();

        args.append("-in").append(" ").append(EngineType.JAR.getFlag()).append(" ");
        args.append("-s ").append(jarOne).append(" ");
        args.append("-d ").append(srcOneGrvDep).append(" ");
        args.append("-o ").append(fileOut).append(" ");
        args.append("-m ").append(Listing.ScarfXML.getFlag()).append(" ");
        args.append("-t").append(" ");
        args.append("-n");

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(args.toString().split(" ")));
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }

        assertNotNull(info);
        assertEquals(EngineType.JAR, info.getSourceType());
        assertEquals(1, info.getSource().size());
        assertEquals(jarOne, info.getSource().get(0));
        assertEquals(1, info.getDependencies().size());
        assertEquals(srcOneGrvDep, info.getDependencies().get(0));
        assertTrue(info.isShowTimes());
        assertTrue(info.getPrettyPrint());
        assertEquals(fileOut, info.getFileOut());
        assertEquals(Listing.ScarfXML, info.getMessagingType());
    }

    /**
     * <p>paramaterCheck_Barejar.</p>
     */
    @Test
    public void paramaterCheck_Barejar() {

        StringBuilder args = new StringBuilder();

        args.append("-in").append(" ").append(EngineType.JAR.getFlag()).append(" ");
        args.append("-s ").append(jarOne);

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(args.toString().split(" ")));
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }

        assertNotNull(info);
        assertEquals(EngineType.JAR, info.getSourceType());
        assertEquals(1, info.getSource().size());
        assertEquals(jarOne, info.getSource().get(0));
        assertFalse(info.isShowTimes());
        assertFalse(info.getPrettyPrint());
        assertEquals(Listing.Legacy, info.getMessagingType());
    }

    /**
     * <p>parameterCheck_gdl.</p>
     */
    @Test
    public void parameterCheck_gdl() {
        StringBuilder args = new StringBuilder();

        args.append("-in").append(" ").append(EngineType.DIR.getFlag()).append(" ");
        args.append("-s ").append(srcOneGrv).append(" ");
        args.append("-d ").append(srcOneGrvDep).append(" ");
        args.append("-o ").append(fileOut).append(" ");
        args.append("-m ").append(Listing.ScarfXML.getFlag()).append(" ");
        args.append("-t").append(" ");
        args.append("-n");

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(args.toString().split(" ")));
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }

        assertNotNull(info);
        assertEquals(EngineType.DIR, info.getSourceType());
        assertEquals(1, info.getSource().size());
        assertEquals(srcOneGrv, info.getSource().get(0));
        assertEquals(1, info.getDependencies().size());
        assertEquals(srcOneGrvDep, info.getDependencies().get(0));
        assertTrue(info.isShowTimes());
        assertTrue(info.getPrettyPrint());
        assertEquals(fileOut, info.getFileOut());
        assertEquals(Listing.ScarfXML, info.getMessagingType());
    }
    //endregion
}

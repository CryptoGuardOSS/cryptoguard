package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import org.junit.Test;
import rule.engine.EngineType;
import util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
    private final String fileOut = Utils.osPathJoin(testPath, "txt.xml");
    private final String fileOutTxt = Utils.osPathJoin(testPath, "txt.txt");
    //endregion

    //region Test Environment Setup

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
     * <p>paramaterCheck_jar.</p>
     */
    @Test
    public void paramaterCheck_jar_enhancedInputFile() {

        String args =
                makeArg(argsIdentifier.FORMAT, EngineType.JAVAFILES) +
                        makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                        makeArg(argsIdentifier.SOURCE, srcOneGrvInputFile) +
                        makeArg(argsIdentifier.PRETTY) +
                        makeArg(argsIdentifier.OUT, tempTestJJava_Txt);

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }

        assertNotNull(info);
        assertEquals(EngineType.JAVAFILES, info.getSourceType());
        assertEquals(10, info.getSource().size());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(srcOneGrvInputFile));
            String curLine = null;

            ArrayList<String> sourceFiles = new ArrayList<>();
            for (String in : info.getSource())
                sourceFiles.add(in.replace(basePath, "."));

            while ((curLine = reader.readLine()) != null)
                assertTrue(sourceFiles.remove(curLine));

            assertTrue(sourceFiles.isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
        assertTrue(info.getPrettyPrint());
        assertEquals(tempTestJJava_Txt, info.getFileOut());
        assertEquals(Listing.Legacy, info.getMessagingType());
    }

    /**
     * <p>paramaterCheck_jar_SkipValidation.</p>
     */
    @Test
    public void paramaterCheck_jar_SkipValidation() {

        String args =
                makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                        makeArg(argsIdentifier.SOURCE, jarOne) +
                        makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                        makeArg(argsIdentifier.OUT, fileOutTxt) +
                        makeArg(argsIdentifier.TIMEMEASURE) +
                        makeArg(argsIdentifier.PRETTY) +
                        makeArg(argsIdentifier.SKIPINPUTVALIDATION);

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
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
        assertEquals(Listing.Default, info.getMessagingType());
    }

    /**
     * <p>paramaterCheck_jar.</p>
     */
    @Test
    public void paramaterCheck_jar() {

        String args =
                makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                        makeArg(argsIdentifier.SOURCE, jarOne) +
                        makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                        makeArg(argsIdentifier.OUT, fileOut) +
                        makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                        makeArg(argsIdentifier.TIMEMEASURE) +
                        makeArg(argsIdentifier.PRETTY);

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
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

        String args =
                makeArg(argsIdentifier.FORMAT, EngineType.JAR) +
                        makeArg(argsIdentifier.SOURCE, jarOne);

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
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
        assertEquals(Listing.Default, info.getMessagingType());
    }

    /**
     * <p>parameterCheck_gdl.</p>
     */
    @Test
    public void parameterCheck_gdl() {

        String args =
                makeArg(argsIdentifier.FORMAT, EngineType.DIR) +
                        makeArg(argsIdentifier.SOURCE, srcOneGrv) +
                        makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                        makeArg(argsIdentifier.OUT, fileOut) +
                        makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                        makeArg(argsIdentifier.TIMEMEASURE) +
                        makeArg(argsIdentifier.PRETTY);

        EnvironmentInformation info = null;

        try {
            info = ArgumentsCheck.paramaterCheck(Arrays.asList(cleaningArgs(args)));
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

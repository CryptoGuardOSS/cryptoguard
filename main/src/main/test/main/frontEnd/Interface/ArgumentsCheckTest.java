package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;
import main.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNull;

public class ArgumentsCheckTest {

    //region Attributes
    private EnvironmentInformation info;

    private final String basePath = System.getProperty("user.dir");
    private final String jarOne = Utils.osPathJoin(basePath, "rsc", "test", "testable-jar.jar");
    private final String srcOneGrv = basePath.replace("main", "testable-jar");
    private final String srcOneGrvDep = "build/dependencies";
    //endregion

    //region Test Environment Setup
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        info = null;
    }
    //endregion

    //region Tests
    @Test
    public void testEnvironmentVariables() {
        String[] fileLists = new String[]{jarOne};
        String[] dirLists = new String[]{srcOneGrv, Utils.osPathJoin(srcOneGrv, srcOneGrvDep)};

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

    @Test //TODO - Need to fix the sourcePaths
    public void paramaterCheck_jar() {
        info = new EnvironmentInformation(
                Arrays.asList(jarOne),
                EngineType.JAR,
                Listing.Legacy,
                null, null, null
        );

        String args = "jar -s " + jarOne;

        try {
            EnvironmentInformation checkedInfo =
                    ArgumentsCheck.paramaterCheck(
                            Arrays.asList(args.split(" ")));

            assertNotNull(checkedInfo);

            for (Field feld : EnvironmentInformation.class.getFields())
                assertEquals(info.getClass().getField(feld.getName()),
                        checkedInfo.getClass().getField(feld.getName()));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test //TODO - Need to fix the sourcePaths
    public void paramaterCheck_Scarfjar() {
        info = new EnvironmentInformation(
                Arrays.asList(jarOne),
                EngineType.JAR,
                Listing.ScarfXML,
                null, null, null
        );

        String args = "jar -s " + jarOne + " -m " + Listing.ScarfXML.getFlag();

        try {
            EnvironmentInformation checkedInfo =
                    ArgumentsCheck.paramaterCheck(
                            Arrays.asList(args.split(" ")));

            assertNotNull(checkedInfo);

            for (Field feld : EnvironmentInformation.class.getFields())
                assertEquals(info.getClass().getField(feld.getName()),
                        checkedInfo.getClass().getField(feld.getName()));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test //TODO - Need to fix the sourcePaths
    public void parameterCheck_gdl() {
        info = new EnvironmentInformation(
                Arrays.asList(srcOneGrv),
                EngineType.DIR,
                Listing.Legacy,
                Arrays.asList(srcOneGrvDep), null, null
        );

        String args = "source -s " + srcOneGrv + " -d " + srcOneGrvDep;

        try {
            EnvironmentInformation checkedInfo =
                    ArgumentsCheck.paramaterCheck(
                            Arrays.asList(args.split(" ")));

            assertNotNull(checkedInfo);

            for (Field feld : EnvironmentInformation.class.getFields())
                assertEquals(info.getClass().getField(feld.getName()),
                        checkedInfo.getClass().getField(feld.getName()));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void parameterCheck_ScarfGDL() {
        info = new EnvironmentInformation(
                Arrays.asList(srcOneGrv),
                EngineType.DIR,
                Listing.ScarfXML,
                Arrays.asList(srcOneGrvDep), null, null
        );

        String args = "source -s " + srcOneGrv + " -d " + srcOneGrvDep + "-m" + Listing.ScarfXML.getFlag();

        try {
            EnvironmentInformation checkedInfo =
                    ArgumentsCheck.paramaterCheck(
                            Arrays.asList(args.split(" ")));

            assertNotNull(checkedInfo);

            for (Field feld : EnvironmentInformation.class.getFields())
                assertEquals(info.getClass().getField(feld.getName()),
                        checkedInfo.getClass().getField(feld.getName()));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }
    //endregion
}
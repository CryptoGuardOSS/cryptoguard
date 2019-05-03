package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import soot.G;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;

/**
 * <p>EntryPointTest_APK class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_APK {

    //region Attributes
    private EntryPoint engine;

    //endregion

    //region Test Environment Setup

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
        //Cleaning the current scene since setup carries throughout the VM
        //tldr - one test setting up the scene will carry over to the next test, this'll stop that
        G.reset();

        engine = new EntryPoint();
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        engine = null;
    }
    //endregion

    //region Tests

    /**
     * <p>testEnvironmentVariables.</p>
     */
    @Test
    public void testEnvironmentVariables() {
        String[] fileLists = new String[]{jarOne, pathToSchema};
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
     * <p>main_TestableApk.</p>
     */
    @Test
    public void main_TestableApk_Legacy() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk;

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableApk.</p>
     */
    @Test
    public void main_TestableApk_Legacy_Stream() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk_Steam + "-st";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk_Steam), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableApk_Scarf.</p>
     */
    @Test
    public void main_TestableApk_Scarf() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk_Scarf + " -m " + Listing.ScarfXML.getFlag() + " -n";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk_Scarf), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);

                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempFileOutApk_Scarf));


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableApk_Scarf.</p>
     */
    @Test
    public void main_TestableApk_Scarf_Stream() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk_Scarf_Steam + " -m " + Listing.ScarfXML.getFlag() + " -n -st";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk_Scarf_Steam), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);

                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempFileOutApk_Scarf_Steam));


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableApk_Scarf.</p>
     */
    @Test
    public void main_TestableApk_Default() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk_Default + " -m " + Listing.Default.getFlag() + " -n";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk_Default), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);

                Report report = Report.deserialize(new File(tempFileOutApk_Default));


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    /**
     * <p>main_TestableApk_Scarf.</p>
     */
    @Test
    public void main_TestableApk_Default_Stream() {
        if (isLinux) {
            String args = "-in " + EngineType.APK.getFlag() + " -s " + pathToAPK + " -o " + tempFileOutApk_Default_Steam + " -m " + Listing.ScarfXML.getFlag() + " -n -st";

            try {
                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutApk_Default_Steam), Charset.forName("UTF-8"));
                assertTrue(results.size() >= 10);

                Report report = Report.deserialize(new File(tempFileOutApk_Default_Steam));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //endregion
}

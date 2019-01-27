package main.rule.engine;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

public class JavaFileEntryTest {


    //region Attributes
    private final String baseRigorityJPath = System.getProperty("user.dir").replace("/main", "");
    private final String javaFilePath = Utils.osPathJoin(
            baseRigorityJPath, "testable-jar", "src",
            "main", "java", "tester", "Crypto.java");

    private final EngineType fileType = EngineType.JAVAFILES;
    private final EntryHandler handler = new JavaFileEntry();

    //region Used for any testing through the src/main entry points
    private ByteArrayOutputStream customStream;
    private EnvironmentInformation info;
    private final Listing output = Listing.ScarfXML;
    //endregion

    //endregion

    //region Test Methods
    private void redirectOutput() {
        System.setOut(new PrintStream(this.customStream));
    }

    private void resetOutput() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
    //endregion

    //region Test Environment Setup
    @Before
    public void setUp() throws Exception {
        this.customStream = new ByteArrayOutputStream();
        this.info = new EnvironmentInformation(new String[]{javaFilePath}, fileType, output.getFlag(), null);
    }

    @After
    public void tearDown() throws Exception {
        this.customStream = null;
        this.info = null;
    }
    //endregion

    //region Tests
    @Test
    public void nonStreamScanOne() {
        try {
            redirectOutput();

            ArrayList<AnalysisIssue> output = handler.NonStreamScan(this.info);

            resetOutput();

            assertTrue(this.customStream.toString().split("\n").length >= 1);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }
    //endregion
}
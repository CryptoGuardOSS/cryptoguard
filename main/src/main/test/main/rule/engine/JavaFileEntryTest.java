package main.rule.engine;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.util.Utils;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class JavaFileEntryTest {


    //region Attributes
    private final String fileSep = System.getProperty("file.separator");


    //region Version Specific Change
    //Java 1.8.181 Implementation
    //private final String javaFilePath = String.join(fileSep, "rsc", "test", "testDir",  "Crypto.java");
    //Java 1.8.181 Implementation

    //Java 1.7.80 Implementation
    private final String javaFilePath = Utils.join(fileSep, "rsc", "test", "testDir", "Crypto.java");
    //Java 1.7.80 Implementation
    //endregion


    private final String javaFullyQualified = "tester.Crypto.java";
    private final String javaFileDependencies = "dependencies";
    private final Boolean isWin = System.getProperty("os.name").contains("Windows");
    private final EngineType fileType = EngineType.JAVAFILES;
    private final EntryHandler handler = new JavaFileEntry();

    //region Used for any testing through the src/main entry points
    private String[] args;
    private RuleEngine engine;
    private ByteArrayOutputStream customStream;
    private String results;
    private JavaFileEntry entry;
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
        this.engine = new RuleEngine();
        this.customStream = new ByteArrayOutputStream();
        this.results = "";
        this.info = new EnvironmentInformation(new String[]{this.javaFilePath}, fileType, output.getFlag(), javaFileDependencies);
    }

    @After
    public void tearDown() throws Exception {
        this.engine = null;
        this.args = null;
        this.results = null;
        this.customStream = null;
        this.info = null;
    }
    //endregion

    //region Tests
    /*@Test
    public void nonStreamScanOne() {
        try {
            this.redirectOutput();

            ArrayList<AnalysisIssue> output = handler.NonStreamScan(this.info);

            this.resetOutput();

            System.out.println(this.customStream);

            assertFalse(StringUtils.isNotBlank(this.customStream.toString()));
            assertTrue(this.customStream.toString().split("\n").length > 1);


        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }*/
    //endregion
}
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

public class JavaClassFileEntryTest {

    //region Attributes
    private final String fileSep = System.getProperty("file.separator");

    //region Version Specific Change
    //Java 1.8.181 Implementation
    //private final String javaClassPath = String.join(fileSep, "rsc", "test","testDir", "Crypto.class");
    //Java 1.8.181 Implementation

    //Java 1.7.80 Implementation
    private final String javaClassPath = Utils.join(fileSep, "rsc", "test", "testDir", "Crypto.class");
    //Java 1.7.80 Implementation
    //endregion

    private final String javaFullyQualified = "Crypto.java";
    private final Boolean isWin = System.getProperty("os.name").contains("Windows");
    private final EngineType fileType = EngineType.CLASSFILES;
    private final EntryHandler handler = new JavaClassFileEntry();

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

    //region Test Environment Handling
    @Before
    public void setUp() throws Exception {
        this.engine = new RuleEngine();
        this.customStream = new ByteArrayOutputStream();
        this.results = "";
        this.info = new EnvironmentInformation(new String[]{this.javaClassPath}, fileType, null, output.getFlag());

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
    public void nonStreamScan() {

        try {

            redirectOutput();

            ArrayList<AnalysisIssue> output = handler.NonStreamScan(this.info);

            resetOutput();

            System.out.println(this.customStream);

            assertTrue(this.customStream.toString().split("\n").length > 1);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }

    }
    */
    //endregion
}
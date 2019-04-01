package rule.engine;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import org.junit.After;
import org.junit.Before;
import util.Utils;

import java.io.ByteArrayOutputStream;

public class JavaClassFileEntryTest {

    //region Attributes

    private final String baseRigorityJPath = System.getProperty("user.dir").replace("/main", "");
    private final String javaClassPath = Utils.osPathJoin(
            baseRigorityJPath, "testable-jar", "build", "classes",
            "java", "main", "tester", "Crypto.class");

    private final EngineType fileType = EngineType.CLASSFILES;
    private final EntryHandler handler = new JavaClassFileEntry();

    //region Used for any testing through the src/main entry points
    private ByteArrayOutputStream customStream;
    private EnvironmentInformation info;
    private final Listing output = Listing.ScarfXML;
    //endregion

    //endregion

    //region Test Environment Handling
    @Before
    public void setUp() throws Exception {
        this.customStream = new ByteArrayOutputStream();

    }

    @After
    public void tearDown() throws Exception {
        this.customStream = null;
        this.info = null;
    }
    //endregion

}
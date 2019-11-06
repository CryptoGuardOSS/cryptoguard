package frontEnd.MessagingSystem.routing.inputStructures;

import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import org.junit.After;
import org.junit.Before;
import rule.engine.EngineType;

/**
 * <p>LegacyTest class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class LegacyTest {

    private final EngineType type = EngineType.APK;
    private final String dependencies = null;
    private final String helpString = "No extra inputs are needed for this output type\nThis is the default output type";
    //region Attributes
    private EnvironmentInformation testInfo;
    private String[] args;
    private Legacy inputTest;
    //endregion

    //region Test Environment Management

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
        inputTest = new Legacy();
        args = new String[]{"TestSource", type.getName(), dependencies, Listing.Legacy.getFlag()};
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
    }
    //endregion
}

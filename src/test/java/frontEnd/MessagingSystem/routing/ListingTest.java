package frontEnd.MessagingSystem.routing;

import org.junit.After;
import org.junit.Before;
import rule.engine.EngineType;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author CryptoguardTeam
 * @version $Id: $Id
 * @since V01.00.02
 */
public class ListingTest {
    //region Test Attributes
    private ArrayList<Listing> ListingSet;
    private ArrayList<String> ListingStringValues;
    private ArrayList<String> ListingFlags;
    private EnvironmentInformation info;
    private frontEnd.MessagingSystem.routing.inputStructures.Legacy legacyInput;
    private frontEnd.MessagingSystem.routing.inputStructures.ScarfXML scarfInput;
    private frontEnd.MessagingSystem.routing.outputStructures.stream.Legacy legacyStream;
    private frontEnd.MessagingSystem.routing.outputStructures.stream.ScarfXML scarfXMLStream;
    private frontEnd.MessagingSystem.routing.outputStructures.block.Legacy legacyBlock;
    private frontEnd.MessagingSystem.routing.outputStructures.block.ScarfXML scarfXMLBlock;
    //endregion

    //region Test Environment Configuration

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {

        this.info = new EnvironmentInformation(Arrays.asList("test.java"), EngineType.DIR, Listing.ScarfXML, new ArrayList<String>(), new ArrayList<String>(), "");

        this.ListingSet = new ArrayList<>();
        this.ListingSet.add(Listing.Legacy);
        this.ListingSet.add(Listing.ScarfXML);

        this.ListingStringValues = new ArrayList<>();
        this.ListingStringValues.add("{ \"type\": \"Legacy\", \"flag\": \"L\"}");
        this.ListingStringValues.add("{ \"type\": \"ScarfXML\", \"flag\": \"SX\"}");

        this.ListingFlags = new ArrayList<>();
        this.ListingFlags.add("L");
        this.ListingFlags.add("SX");

        this.legacyInput = new frontEnd.MessagingSystem.routing.inputStructures.Legacy();
        this.scarfInput = new frontEnd.MessagingSystem.routing.inputStructures.ScarfXML();
        this.legacyStream = new frontEnd.MessagingSystem.routing.outputStructures.stream.Legacy(info);
        this.scarfXMLStream = new frontEnd.MessagingSystem.routing.outputStructures.stream.ScarfXML(info);
        this.legacyBlock = new frontEnd.MessagingSystem.routing.outputStructures.block.Legacy(info);
        this.scarfXMLBlock = new frontEnd.MessagingSystem.routing.outputStructures.block.ScarfXML(info);
    }

    /**
     * <p>tearDown.</p>
     */
    @After
    public void tearDown() {
        this.info = null;
        this.ListingSet = null;
        this.ListingStringValues = null;
        this.ListingFlags = null;
        this.legacyInput = null;
        this.scarfInput = null;
        this.legacyStream = null;
        this.scarfXMLStream = null;
        this.legacyBlock = null;
        this.scarfXMLBlock = null;
    }
    //endregion

    //region Tests
    //@Test - TODO Reimplement this test

    /**
     * <p>toStringTest.</p>
     */
    public void toStringTest() {
        for (int sizeKounter = 0; sizeKounter < this.ListingSet.size(); sizeKounter++)
            assertEquals(this.ListingSet.get(sizeKounter).toString(), this.ListingStringValues.get(sizeKounter));
    }

    //@Test - TODO Reimplement this test

    /**
     * <p>getTypeOfMessagingTest.</p>
     */
    public void getTypeOfMessagingTest() {
        try {
            assertTrue(Listing.Legacy.getTypeOfMessagingOutput(false, this.info) instanceof frontEnd.MessagingSystem.routing.outputStructures.block.Legacy);
            assertTrue(Listing.ScarfXML.getTypeOfMessagingOutput(false, this.info) instanceof frontEnd.MessagingSystem.routing.outputStructures.block.ScarfXML);
            assertTrue(Listing.Legacy.getTypeOfMessagingOutput(true, this.info) instanceof frontEnd.MessagingSystem.routing.outputStructures.stream.Legacy);
            assertTrue(Listing.ScarfXML.getTypeOfMessagingOutput(true, this.info) instanceof frontEnd.MessagingSystem.routing.outputStructures.stream.ScarfXML);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }
    //endregion
}

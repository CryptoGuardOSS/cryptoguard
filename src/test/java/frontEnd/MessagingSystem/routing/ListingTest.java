package frontEnd.MessagingSystem.routing;

import org.junit.After;
import org.junit.Before;
import rule.engine.EngineType;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * <p>MessageRepresentationTest class.</p>
 *
 * @author RigorityJTeam
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
    //@Test
    public void toStringTest() {
        for (int sizeKounter = 0; sizeKounter < this.ListingSet.size(); sizeKounter++)
            assertEquals(this.ListingSet.get(sizeKounter).toString(), this.ListingStringValues.get(sizeKounter));
    }

    //@Test
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
package frontEnd.MessagingSystem.outputStructures;

import main.frontEnd.MessagingSystem.routing.Listing;
import main.frontEnd.MessagingSystem.routing.outputStructures.Legacy;
import main.frontEnd.MessagingSystem.routing.outputStructures.ScarfXML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    //endregion

    //region Test Environment Configuration
    @Before
    public void setUp() {
        this.ListingSet = new ArrayList<>();
        this.ListingSet.add(Listing.LegacyOutput);
        this.ListingSet.add(Listing.ScarfXMLOutput);

        this.ListingStringValues = new ArrayList<>();
        this.ListingStringValues.add("{ \"type\": \"Legacy\", \"flag\": \"L\"}");
        this.ListingStringValues.add("{ \"type\": \"ScarfXML\", \"flag\": \"SX\"}");

        this.ListingFlags = new ArrayList<>();
        this.ListingFlags.add("L");
        this.ListingFlags.add("SX");
    }

    @After
    public void tearDown() {
        this.ListingSet = null;
        this.ListingStringValues = null;
        this.ListingFlags = null;
    }
    //endregion

    //region Tests
    @Test
    public void toStringTest() {
        for (int sizeKounter = 0; sizeKounter < this.ListingSet.size(); sizeKounter++)
            assertEquals(this.ListingSet.get(sizeKounter).toString(), this.ListingStringValues.get(sizeKounter));
    }

    @Test
    public void getTypeOfMessagingTest() {
        assertTrue(Listing.getTypeOfMessagingOutput(this.ListingFlags.get(0)) instanceof Legacy);
        assertTrue(Listing.getTypeOfMessagingOutput(this.ListingFlags.get(1)) instanceof ScarfXML);
    }
    //endregion
}
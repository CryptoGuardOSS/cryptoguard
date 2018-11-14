package frontEnd.outputStructures;

import main.frontEnd.outputStructures.LegacyOutput;
import main.frontEnd.outputStructures.Listing;
import main.frontEnd.outputStructures.ScarfXMLOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
		this.ListingStringValues.add("{ \"type\": \"LegacyOutput\", \"flag\": \"L\"}");
		this.ListingStringValues.add("{ \"type\": \"ScarfXMLOutput\", \"flag\": \"SX\"}");

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
		assertTrue(Listing.getTypeOfMessaging(this.ListingFlags.get(0)) instanceof LegacyOutput);
		assertTrue(Listing.getTypeOfMessaging(this.ListingFlags.get(1)) instanceof ScarfXMLOutput);
	}
	//endregion
}
package main.frontEnd.MessagingSystem.routing;

import main.frontEnd.MessagingSystem.routing.outputStructures.Legacy;
import main.frontEnd.MessagingSystem.routing.outputStructures.ScarfXML;
import main.rule.engine.EngineType;
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
    private main.frontEnd.MessagingSystem.routing.inputStructures.Legacy legacyInput;
    private main.frontEnd.MessagingSystem.routing.inputStructures.ScarfXML scarfInput;
    private String baseHelp;
    //endregion

    //region Test Environment Configuration
    @Before
    public void setUp() {
        this.ListingSet = new ArrayList<>();
        this.ListingSet.add(Listing.Legacy);
        this.ListingSet.add(Listing.ScarfXML);

        this.ListingStringValues = new ArrayList<>();
        this.ListingStringValues.add("{ \"type\": \"Legacy\", \"flag\": \"L\"}");
        this.ListingStringValues.add("{ \"type\": \"ScarfXML\", \"flag\": \"SX\"}");

        this.ListingFlags = new ArrayList<>();
        this.ListingFlags.add("L");
        this.ListingFlags.add("SX");

        this.legacyInput = new main.frontEnd.MessagingSystem.routing.inputStructures.Legacy();
        this.scarfInput = new main.frontEnd.MessagingSystem.routing.inputStructures.ScarfXML();

        StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append("===========================================================\n")
                .append("key: {}=required ()=optional \n")
                .append("General Useage : java -jar {thisJar} {Engine Flag, as shown below} {.apk/.jar file, .class/.java file(s), or sourcecode dir} ({-d} {dir of dependencies for .class/.jar file(s), \"\" if there are none}) (Output Type flag) ({required depending on the output Type}) \n")
                .append(EngineType.getHelp())
                .append("===========================================================\n\n");

        this.baseHelp = helpBuilder.toString();
    }

    @After
    public void tearDown() {
        this.ListingSet = null;
        this.ListingStringValues = null;
        this.ListingFlags = null;
        this.legacyInput = null;
        this.scarfInput = null;
        this.baseHelp = null;
    }

    private String generateTypeHelp(Listing type) {
        StringBuilder help = new StringBuilder();

        help.append("===========================================================\n")
                .append("Type : ").append(type.getType()).append("\n")
                .append("Flag : ").append(type.getFlag()).append("\n")
                .append(type.getTypeOfMessagingInput().helpInfo()).append("\n")
                .append("===========================================================\n\n");

        return help.toString();
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
        assertTrue(Listing.Legacy.getTypeOfMessagingOutput() instanceof Legacy);
        assertTrue(Listing.ScarfXML.getTypeOfMessagingOutput() instanceof ScarfXML);
    }

    @Test
    public void getHelpInfoTest() {
        StringBuilder helpfulString = new StringBuilder();

        helpfulString.append(baseHelp);
        for (Listing listingType : ListingSet)
            helpfulString.append(generateTypeHelp(listingType));

        assertEquals(helpfulString.toString(), Listing.getInputHelp());

        System.out.println(helpfulString);
    }
    //endregion
}
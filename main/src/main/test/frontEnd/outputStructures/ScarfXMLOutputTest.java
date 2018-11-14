package frontEnd.outputStructures;

import main.frontEnd.outputStructures.ScarfXMLOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScarfXMLOutputTest {

	//region Test Attributes
	private ScarfXMLOutput xmlEngine;
	private String propertyErrorMessage;
	private String marshallErrorMessage;
	//endregion

	//region Test Environment Management
	@Before
	public void setUp() throws Exception {
		xmlEngine = new ScarfXMLOutput();
		propertyErrorMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ERROR>\nThere has been an issue setting properties.\n</ERROR>";
		marshallErrorMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ERROR>\nThere has been an issue marshalling the output.\n</ERROR>";
	}

	@After
	public void tearDown() throws Exception {
		xmlEngine = null;
		propertyErrorMessage = null;
		marshallErrorMessage = null;
	}
	//endregion

	//region Tests
	@Test
	public void getOutput() {
	}

	@Test
	public void getPropertyError() {

	}

	@Test
	public void getJAXBError() {

	}
	//endregion
}
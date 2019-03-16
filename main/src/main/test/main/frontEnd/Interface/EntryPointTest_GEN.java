package main.frontEnd.Interface;

import main.frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import soot.G;

import static main.TestUtilities.isLinux;
import static main.TestUtilities.makeArg;
import static org.junit.Assert.assertNull;

public class EntryPointTest_GEN {

    //region Attributes
    private EntryPoint engine;
    //endregion

    //region Test Environment Setup
    @Before
    public void setUp() throws Exception {
        //Cleaning the current scene since setup carries throughout the VM
        //tldr - one test setting up the scene will carry over to the next test, this'll stop that
        G.reset();

        engine = new EntryPoint();
    }

    @After
    public void tearDown() throws Exception {
        engine = null;
    }
    //endregion

    //region Tests
    //@Test
    public void main_BareTest_HELP() {
        if (isLinux) {
            String args = makeArg(argsIdentifier.HELP);

            try {

                engine.main(args.split(" "));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //@Test
    public void main_BareTest_VERSION() {
        if (isLinux) {
            String args = makeArg(argsIdentifier.VERSION);

            try {

                engine.main(args.split(" "));

            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }
    //endregion
}
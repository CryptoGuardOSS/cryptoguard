package frontEnd.Interface;

import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
import frontEnd.argsIdentifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rule.engine.EngineType;
import soot.G;
import util.Utils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.*;

/**
 * <p>EntryPointTest_CLASS class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class EntryPointTest_CLASS {

    //region Attributes
    private EntryPoint engine;
    //endregion

    //region Test Environment Setup

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
        //Cleaning the current scene since setup carries throughout the VM
        //tldr - one test setting up the scene will carry over to the next test, this'll stop that
        G.reset();

        engine = new EntryPoint();
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        engine = null;
    }
    //endregion

    //region Tests
    @Test
    /**
     * <p>main_TestableFiles_SingleTest.</p>
     */
    public void main_TestableFiles_SingleTest() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES.getFlag()) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.SOURCE, classFiles[0]) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.OUT, tempFileOutTxt_Class);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutTxt_Class), Charset.forName("UTF-8"));

                int count = 0;
                for (String line : results)
                    if (line.contains("Violated"))
                        count++;

                assertTrue(count > 0);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    /**
     * <p>main_TestableFiles_MultiTest.</p>
     */
    public void main_TestableFiles_MultiTest() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, Utils.join(" ", classFiles)) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.Legacy) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.OUT, tempFileOutTxt_two);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutTxt_two), Charset.forName("UTF-8"));

                int count = 0;
                for (String line : results)
                    if (line.contains("Violated"))
                        count++;

                assertTrue(count > 0);


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    /**
     * <p>main_TestableFiles_MultiTest_Scarf.</p>
     */
    public void main_TestableFiles_MultiTest_Scarf() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, Utils.join(" ", classFiles)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempFileOutXML_Class);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML_Class), Charset.forName("UTF-8"));

                int count = 0;
                for (String line : results)
                    if (line.contains("Violated"))
                        count++;

                assertTrue(count > 0);


                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempFileOutXML_Class));


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    @Test
    /**
     * <p>main_TestableFiles_MultiTest_Scarf_Stream.</p>
     */
    public void main_TestableFiles_MultiTest_Scarf_Stream() {
        if (isLinux) {
            String args =
                    makeArg(argsIdentifier.FORMAT, EngineType.CLASSFILES.getFlag()) +
                            makeArg(argsIdentifier.SOURCE, Utils.join(" ", classFiles)) +
                            makeArg(argsIdentifier.DEPENDENCY, srcOneGrvDep) +
                            makeArg(argsIdentifier.FORMATOUT, Listing.ScarfXML) +
                            makeArg(argsIdentifier.OUT, tempFileOutXML_Class_Stream) +
                            makeArg(argsIdentifier.STREAM);

            try {

                engine.main(args.split(" "));

                List<String> results = Files.readAllLines(Paths.get(tempFileOutXML_Class_Stream), Charset.forName("UTF-8"));

                int count = 0;
                for (String line : results)
                    if (line.contains("Violated"))
                        count++;

                assertTrue(count > 0);


                AnalyzerReport report = AnalyzerReport.deserialize(new File(tempFileOutXML_Class_Stream));


            } catch (Exception e) {
                e.printStackTrace();
                assertNull(e);
            }
        }
    }

    //endregion
}

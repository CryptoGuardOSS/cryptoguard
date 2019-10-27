package util;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.TestUtilities;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static test.TestUtilities.classFiles;
import static test.TestUtilities.classSource;
import static util.Utils.*;

/**
 * <p>UtilsTest class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class UtilsTest {

    //region Attributes
    private String fullJavaClassFile;
    private String fullJavaFile;
    private String fileSep = System.getProperty("file.separator");
    private String javaFile = String.join(fileSep, "rsc", "test", "main.java");

    //endregion

    //region Test Environment

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {

        fullJavaFile = String.join(fileSep, "src", "main", "java", "com", "full", "fun", "test", "main.java");
        fullJavaClassFile = String.join(fileSep, "target", "main", "java", "com", "full", "fun", "test", "main.class");

    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        fullJavaFile = null;
        fullJavaClassFile = null;
    }
    //endregion

    //region Tests

    /**
     * <p>trimFilePathTestOne.</p>
     */
    @Test
    public void trimFilePathTestOne() {
        String fileNameExt = trimFilePath(fullJavaFile);

        assertNotNull(fileNameExt);
        assertEquals("main.java", fileNameExt);

    }

    /**
     * <p>trimFilePathTestTwo.</p>
     */
    @Test
    public void trimFilePathTestTwo() {
        String fileNameExt = trimFilePath(fullJavaClassFile);

        assertNotNull(fileNameExt);
        assertEquals("main.class", fileNameExt);

    }

    @Test

    /**
     * <p>retrieveFullyQualifiedNameTest.</p>
     */
    public void retrieveFullyQualifiedNameTest() {
        try {
            String packageName = retrieveFullyQualifiedName(Arrays.asList(javaFile)).get(0);

            assertNotNull(packageName);
            assertEquals("main", packageName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertNull(e);
        }
    }

    @Test
    public void testVerifyClassPathsOne() {
        try {
            ArrayList<String> returnedOutput = Utils.verifyClassPaths(TestUtilities.sampleAuxClassPathOne);
            for (String output : returnedOutput)
                assertTrue(TestUtilities.sampleAuxClassPathOneList.contains(output));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertNull(e);
        }
    }

    @Test
    public void testVerifyClassPathsTwo() {
        try {
            ArrayList<String> returnedOutput = Utils.verifyClassPaths(TestUtilities.sampleAuxClassPathTwo);
            assertNull(returnedOutput);
        } catch (ExceptionHandler e) {
            assertEquals(e.getErrorCode(), ExceptionId.ARG_VALID);
            assertTrue(e.getLongDesciption().contains(TestUtilities.scarfArgs));
        }
    }

    @Test
    public void test_retrieveFullyQualifiedPath_Single() {
        String testFile = classFiles[0];
        String base = join(".", "tester", classFiles[0].replace(classSource + "/", "").replace(".class", ""));

        try {
            String fullyQualifiedName = Utils.retrieveFullyQualifiedName(testFile);
            assertEquals(base, fullyQualifiedName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertNull(e);
        }
    }
    //endregion
}

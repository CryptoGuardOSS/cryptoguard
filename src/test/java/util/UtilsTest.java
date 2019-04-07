package util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static util.Utils.retrieveFullyQualifiedName;
import static util.Utils.trimFilePath;

/**
 * <p>UtilsTest class.</p>
 *
 * @author drmonster
 * @version $Id: $Id
 * @since V03.03.10
 */
public class UtilsTest {

    //region Attributes
    private String fullJavaClassFile;
    private String fullJavaFile;
    private String fileSep = System.getProperty("file.separator");

    //region Version Specific Change
    //Java 1.8.181 Implementation
    //private String javaFile = String.join(fileSep, "rsc", "test", "main.java");
    //Java 1.8.181 Implementation

    //Java 1.7.80 Implementation
    private String javaFile = Utils.join(fileSep, "rsc", "test", "main.java");
    //Java 1.7.80 Implementation
    //endregion

    //endregion

    //region Test Environment

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {

        //region Version Specific Change
        //Java 1.8.181 Implementation
        //fullJavaFile = String.join(fileSep, "src", "main", "java", "com", "full", "fun", "test", "main.java");
        //Java 1.8.181 Implementation

        //Java 1.7.80 Implementation
        fullJavaFile = Utils.join(fileSep, "src", "main", "java", "com", "full", "fun", "test", "main.java");
        //Java 1.7.80 Implementation
        //endregion

        //region Version Specific Change
        //Java 1.8.181 Implementation
        //fullJavaClassFile = String.join(fileSep, "target", "main", "java", "com", "full", "fun", "test", "main.class");
        //Java 1.8.181 Implementation

        //Java 1.7.80 Implementation
        fullJavaClassFile = Utils.join(fileSep, "target", "main", "java", "com", "full", "fun", "test", "main.class");
        //Java 1.7.80 Implementation
        //endregion

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

    //TODO - reimplement this
    //@Test

    /**
     * <p>retrieveFullyQualifiedNameTest.</p>
     */
    public void retrieveFullyQualifiedNameTest() {
        String packageName = retrieveFullyQualifiedName(Arrays.asList(javaFile)).get(0);

        assertNotNull(packageName);
        assertEquals("src.main.java.com.full.fun.test.main", packageName);
    }
    //endregion
}

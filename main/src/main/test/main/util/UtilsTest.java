package main.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static main.util.Utils.retrieveFullyQualifiedName;
import static main.util.Utils.trimFilePath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    private String javaFile = Utils.stringJoiner(fileSep, "rsc", "test", "main.java");
    //Java 1.7.80 Implementation
    //endregion

    //endregion

    //region Test Environment
    @Before
    public void setUp() throws Exception {

        //region Version Specific Change
        //Java 1.8.181 Implementation
        //fullJavaFile = String.join(fileSep, "src", "main", "java", "com", "full", "fun", "test", "main.java");
        //Java 1.8.181 Implementation

        //Java 1.7.80 Implementation
        fullJavaFile = Utils.stringJoiner(fileSep, "src", "main", "java", "com", "full", "fun", "test", "main.java");
        //Java 1.7.80 Implementation
        //endregion

        //region Version Specific Change
        //Java 1.8.181 Implementation
        //fullJavaClassFile = String.join(fileSep, "target", "main", "java", "com", "full", "fun", "test", "main.class");
        //Java 1.8.181 Implementation

        //Java 1.7.80 Implementation
        fullJavaClassFile = Utils.stringJoiner(fileSep, "target", "main", "java", "com", "full", "fun", "test", "main.class");
        //Java 1.7.80 Implementation
        //endregion

    }

    @After
    public void tearDown() throws Exception {
        fullJavaFile = null;
        fullJavaClassFile = null;
    }
    //endregion

    //region Tests

    @Test
    public void trimFilePathTestOne() {
        String fileNameExt = trimFilePath(fullJavaFile);

        assertNotNull(fileNameExt);
        assertEquals("main.java", fileNameExt);

    }

    @Test
    public void trimFilePathTestTwo() {
        String fileNameExt = trimFilePath(fullJavaClassFile);

        assertNotNull(fileNameExt);
        assertEquals("main.class", fileNameExt);

    }

    @Test
    public void retrieveFullyQualifiedNameTest() {
        String packageName = retrieveFullyQualifiedName(javaFile);

        assertNotNull(packageName);
        assertEquals("src.main.java.com.full.fun.test.main.java", packageName);
    }
    //endregion
}
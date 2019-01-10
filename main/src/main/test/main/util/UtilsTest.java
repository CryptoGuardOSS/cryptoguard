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
    private String javaFile = String.join(fileSep, "rsc", "test", "main.java");
    //endregion

    //region Test Environment
    @Before
    public void setUp() throws Exception {
        fullJavaFile = String.join(fileSep, "src", "main", "java", "com", "full", "fun", "test", "main.java");
        fullJavaClassFile = String.join(fileSep, "target", "main", "java", "com", "full", "fun", "test", "main.class");
    }

    @After
    public void tearDown() throws Exception {
        fullJavaFile = null;
        fullJavaClassFile = null;
    }
    //endregion

    //region Tests
    //region Stubbed
    /*
    @Test
    public void getClassNamesFromJarArchiveTest() {
    }

    @Test
    public void getBasePackageNameFromApkTest() {
    }

    @Test
    public void getBasePackageNameFromJarTest() {
    }

    @Test
    public void getClassNamesFromApkArchiveTest() {
    }

    @Test
    public void buildSootClassPathTest() {
    }

    @Test
    public void buildSootClassPath1Test() {
    }

    @Test
    public void getJarsInDirectoryTest() {
    }

    @Test
    public void getClassHierarchyAnalysisTest() {
    }

    @Test
    public void getXmlFilesTest() {
    }

    @Test
    public void findInfluencingParamtersTest() {
    }

    @Test
    public void isSpecialInvokeOnTest() {
    }

    @Test
    public void listfTest() {
    }

    @Test
    public void getClassNamesFromSnippetTest() {
    }
    */
    //endregion

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
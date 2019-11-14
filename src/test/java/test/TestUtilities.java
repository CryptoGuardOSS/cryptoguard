package test;

import frontEnd.Interface.EntryPoint;
import org.apache.commons.lang3.StringUtils;
import util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;

/**
 * <p>TestUtilities class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class TestUtilities {


    //region String Statics
    public static final Boolean isLinux = !System.getProperty("os.name").contains("Windows");
    public static final String basePath = System.getProperty("user.dir");
    public static final String scarfArgs = Utils.osPathJoin(basePath, "src", "main", "resources", "Scarf", "sample.properties");
    public static final String testRec = Utils.osPathJoin(basePath, "samples");
    public static final String testPath = Utils.osPathJoin(basePath, "build", "tmp");
    public static final String testRec_tester_test = Utils.osPathJoin(testRec, "temp", "tester");
    public static final String testRec_tester_test_Class = Utils.osPathJoin(testRec_tester_test, "test.class");
    public static final String testRec_tester_test_Java = Utils.osPathJoin(testRec_tester_test, "test.java");
    public static final String testRec_tester_test_Java_xml = Utils.osPathJoin(testPath, "test_java.xml");
    public static final String srcOneGrv = Utils.osPathJoin(testRec, "testable-jar");
    public static final String srcOneGrv_base = "tester";
    public static final String srcOneGrvInputFile = Utils.osPathJoin(srcOneGrv, "input.in");
    public static final String javaSource = Utils.osPathJoin(srcOneGrv, "src", "main", "java", "tester");
    public static final String classSource = Utils.osPathJoin(srcOneGrv, "build", "classes", "java", "main", "tester");
    public static final String[] classFiles = {Utils.osPathJoin(classSource, "PBEUsage.class"), Utils.osPathJoin(classSource, "UrlFrameWorks.class"), Utils.osPathJoin(classSource, "NewTestCase1.class"), Utils.osPathJoin(classSource, "NewTestCase2.class")};
    public static final String newTestCaseTwo_Class = Utils.osPathJoin(classSource, "NewTestCase2.class");
    public static final String newTestCaseTwo_xml = Utils.osPathJoin(testPath, "NewTestCase2-class.xml");
    public static final String testablejar_PBEUsage_class_xml = Utils.osPathJoin(testPath, "PBEUsage-class-file_One.xml");
    public static final String testablejar_Crypto_class = Utils.osPathJoin(classSource, "Crypto.class");
    public static final String testablejar_Crypto_class_xml = Utils.osPathJoin(testPath, "java-file_One.xml");
    public static final String[] javaFiles = {Utils.osPathJoin(javaSource, "PBEUsage.java"), Utils.osPathJoin(javaSource, "UrlFrameWorks.java")};
    public static final String jarOne = Utils.osPathJoin(testRec, "testable-jar", "build", "libs", "testable-jar.jar");
    public static final String srcOneGrvDep = Utils.osPathJoin(testRec, "testable-jar", "build", "dependencies");
    public static final String javaFileOne = Utils.osPathJoin(testPath, "java-file_One.xml");
    public static final String javaFileTwo = Utils.osPathJoin(testPath, "java-file_Two.txt");
    public static final String javaFileThree = Utils.osPathJoin(testPath, "java-file_Three.txt");
    public static final String javaFileThreeXML = Utils.osPathJoin(testPath, "java-file_Three.xml");
    public static final String pathToSchema = Utils.osPathJoin(basePath, "src", "main", "resources", "Scarf", "scarf_v1.2.xsd");
    public static final String pathToAPK = Utils.osPathJoin(testRec, "app-debug.apk");
    public static final String tempFileOutTxt = Utils.osPathJoin(testPath, "testable-jar.txt");
    public static final String tempFileOutXML = Utils.osPathJoin(testPath, "testable-jar.xml");
    public static final String tempTestJJava_Txt = Utils.osPathJoin(testPath, "temp_java-jar.txt");
    public static final String tempStreamXML = Utils.osPathJoin(testPath, "testable-jar_Stream.xml");
    public static final String tempFileOutTxt_two = Utils.osPathJoin(testPath, "testable-jar_classFiles.txt");
    public static final String tempFileOutXML_two = Utils.osPathJoin(testPath, "testable-jar_classFiles.xml");
    public static final String tempFileOutTxt_Class = Utils.osPathJoin(testPath, "java-class-file.txt");
    public static final String tempFileOutTxt_Class_fullproj = Utils.osPathJoin(testPath, "java-class-proj-file.xml");
    public static final String tempFileOutTxt_Class_tester_test = Utils.osPathJoin(testPath, "java-class-file_sample_tester-test.txt");
    public static final String tempFileOutXML_Class = Utils.osPathJoin(testPath, "java-class-file.xml");
    public static final String tempFileOutXML_Class_Stream = Utils.osPathJoin(testPath, "java-class-file-stream.xml");
    public static final String tempFileOutApk = Utils.osPathJoin(testPath, "app-debug.txt");
    public static final String tempFileOutApk_Steam = Utils.osPathJoin(testPath, "app-debug_Stream.txt");
    public static final String tempFileOutApk_Scarf = Utils.osPathJoin(testPath, "app-debug.xml");
    public static final String tempFileOutApk_Scarf_Steam = Utils.osPathJoin(testPath, "app-debug_Stream.xml");
    public static final String tempFileOutApk_Default = Utils.osPathJoin(testPath, "app-debug.json");
    public static final String tempFileOutApk_Default_Steam = Utils.osPathJoin(testPath, "app-debug_Stream.json");
    public static final String tempJarFile_txt = Utils.osPathJoin(testPath, "tempJarFile_txt.txt");
    public static final String tempJarFile_Scarf_0 = Utils.osPathJoin(testPath, "tempJarFile_Scarf_0.xml");
    public static final String tempJarFile_Scarf_1 = Utils.osPathJoin(testPath, "tempJarFile_Scarf_1.xml");
    public static final String tempJarFile_Scarf_2 = Utils.osPathJoin(testPath, "tempJarFile_Scarf_2.xml");
    public static final String tempJarFile_Scarf_Steam_1 = Utils.osPathJoin(testPath, "tempJarFile_Scarf_Steam_1.xml");
    public static final String tempJarFile_Default_0 = Utils.osPathJoin(testPath, "tempJarFile_Default_0.json");
    public static final String tempJarFile_Default_Stream_0 = Utils.osPathJoin(testPath, "tempJarFile_Default_Stream_0.json");
    public static final ArrayList<String> sampleAuxClassPathOneList = new ArrayList<>(Arrays.asList(testRec, jarOne, javaFiles[0], javaFiles[1], classFiles[0], classFiles[1]));
    public static final String sampleAuxClassPathOne = String.join(":", sampleAuxClassPathOneList);
    public static final ArrayList<String> sampleAuxClassPathTwoList = new ArrayList<>(Arrays.asList(testRec, jarOne, javaFiles[0], javaFiles[1], classFiles[0], classFiles[1], scarfArgs));
    public static final String sampleAuxClassPathTwo = String.join(":", sampleAuxClassPathTwoList);
    public static final String verySimple_Path = Utils.osPathJoin(testRec, "VerySimple");
    public static final String verySimple_Jar = Utils.osPathJoin(verySimple_Path, "very.jar");
    public static final String verySimple_Jar_xml = Utils.osPathJoin(testPath, "verySimple_jar.xml");
    public static final String verySimple_Java = Utils.osPathJoin(verySimple_Path, "very.java");
    public static final String verySimple_Java_xml = Utils.osPathJoin(testPath, "verySimple_java.xml");
    public static final String verySimple_Klass = Utils.osPathJoin(verySimple_Path, "very.class");
    public static final String verySimple_Klass_xml = Utils.osPathJoin(testPath, "verySimple_klass.xml");
    public static final ArrayList<String> srcOneGrvInputArr = new ArrayList<>(Arrays.asList(
            Utils.osPathJoin(javaSource, "UrlFrameWorks.java"),
            Utils.osPathJoin(javaSource, "PasswordUtils.java"),
            Utils.osPathJoin(javaSource, "Crypto.java"),
            Utils.osPathJoin(javaSource, "PBEUsage.java"),
            Utils.osPathJoin(javaSource, "NewTestCase2.java"),
            Utils.osPathJoin(javaSource, "VeryBusyClass.java"),
            Utils.osPathJoin(javaSource, "SymCrypto.java"),
            Utils.osPathJoin(javaSource, "NewTestCase1.java"),
            Utils.osPathJoin(javaSource, "LiveVarsClass.java"),
            Utils.osPathJoin(javaSource, "PassEncryptor.java")
    ));
    public static final ArrayList<String> srcOneGrvInputArr_Class = new ArrayList<>(Arrays.asList(
            Utils.osPathJoin(classSource, "UrlFrameWorks.class"),
            Utils.osPathJoin(classSource, "PasswordUtils.class"),
            Utils.osPathJoin(classSource, "Crypto.class"),
            Utils.osPathJoin(classSource, "Crypto$1.class"),
            Utils.osPathJoin(classSource, "Crypto$2.class"),
            Utils.osPathJoin(classSource, "Crypto$3.class"),
            Utils.osPathJoin(classSource, "PBEUsage.class"),
            Utils.osPathJoin(classSource, "NewTestCase2.class"),
            Utils.osPathJoin(classSource, "VeryBusyClass.class"),
            Utils.osPathJoin(classSource, "SymCrypto.class"),
            Utils.osPathJoin(classSource, "NewTestCase1.class"),
            Utils.osPathJoin(classSource, "LiveVarsClass.class"),
            Utils.osPathJoin(classSource, "PassEncryptor.class")
    ));
    //endregion

    public static String[] cleaningArgs(String arg) {
        ArrayList<String> cleanArgs = new ArrayList<>(Arrays.asList(arg.split(" ")));
        cleanArgs.removeIf(StringUtils::isEmpty);
        return cleanArgs.toArray(new String[cleanArgs.size()]);
    }

    public static String captureNewFileOutViaStdOut(String[] args) throws Exception {
        //region Redirecting the std out to capture the file out
        //The new std out
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        //Old out
        PrintStream old = System.out;

        //Redirecting the std out to the capture
        System.setOut(ps);

        //region Critical Section
        EntryPoint.main(args);
        //endregion

        //Resetting the std out
        System.out.flush();
        System.setOut(old);
        //endregion

        //The output string
        String outputString = StringUtils.trimToNull(baos.toString());
        assertTrue(StringUtils.isNotBlank(outputString));

        String[] lines = baos.toString().split(Utils.lineSep);
        outputString = StringUtils.trimToNull(lines[lines.length - 1]);
        assertTrue(StringUtils.isNotBlank(outputString));

        return outputString;
    }

    public static String[] arr(ArrayList<String> in) {
        return in.toArray(new String[in.size()]);
    }

    public static ArrayList<String> arr(String[] in) {
        return new ArrayList<>(Arrays.asList(in));
    }

    public static String getFileNameFromString(String filePath) {
        if (StringUtils.isBlank(filePath))
            return "";

        filePath = StringUtils.trimToNull(filePath);
        String[] splits = filePath.split(Utils.fileSep);
        String fileName = splits[splits.length - 1];

        return StringUtils.trimToNull(fileName.substring(0, fileName.indexOf(".")));
    }
}

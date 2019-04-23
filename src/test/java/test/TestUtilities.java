package test;

import frontEnd.MessagingSystem.routing.inputStructures.ScarfXMLId;
import frontEnd.argsIdentifier;
import util.Utils;

/**
 * <p>TestUtilities class.</p>
 *
 * @author drmonster
 * @version $Id: $Id
 * @since V03.03.10
 */
public class TestUtilities {

    /**
     * Constant <code>isLinux</code>
     */
    public static final Boolean isLinux = !System.getProperty("os.name").contains("Windows");

    /** Constant <code>basePath="System.getProperty(user.dir)"</code> */
    public static final String basePath = System.getProperty("user.dir");
    /** Constant <code>scarfArgs="Utils.osPathJoin(basePath, src, main, r"{trunked}</code> */
    public static final String scarfArgs = Utils.osPathJoin(basePath, "src", "main", "resources", "Scarf", "sample.properties");
    /** Constant <code>srcOneGrv="Utils.osPathJoin(basePath, samples, tes"{trunked}</code> */
    public static final String testRec = Utils.osPathJoin(basePath, "src", "test", "resources");
    public static final String srcOneGrv = Utils.osPathJoin(testRec, "testable-jar");
    public static final String javaSource = Utils.osPathJoin(srcOneGrv, "src", "main", "java", "tester");
    public static final String classSource = Utils.osPathJoin(srcOneGrv, "build", "classes", "java", "main", "tester");
    public static final String[] classFiles = {Utils.osPathJoin(classSource, "PBEUsage.class"), Utils.osPathJoin(classSource, "UrlFrameWorks.class")};
    public static final String[] javaFiles = {Utils.osPathJoin(javaSource, "PBEUsage.java"), Utils.osPathJoin(javaSource, "UrlFrameWorks.java")};
    /** Constant <code>jarOne="Utils.osPathJoin(basePath, samples, tes"{trunked}</code> */
    public static final String jarOne = Utils.osPathJoin(testRec, "testable-jar", "build", "libs", "testable-jar.jar");
    /** Constant <code>srcOneGrvDep="Utils.osPathJoin(basePath, samples, tes"{trunked}</code> */
    public static final String srcOneGrvDep = Utils.osPathJoin(testRec, "testable-jar", "build", "dependencies");
    /** Constant <code>testPath="Utils.osPathJoin(basePath, build, tmp)"</code> */
    public static final String testPath = Utils.osPathJoin(basePath, "build", "tmp");
    public static final String javaFileOne = Utils.osPathJoin(basePath, "build", "tmp", "java-file_One.xml");
    public static final String javaFileTwo = Utils.osPathJoin(basePath, "build", "tmp", "java-file_Two.txt");
    public static final String javaFileThree = Utils.osPathJoin(basePath, "build", "tmp", "java-file_Three.txt");
    /** Constant <code>pathToSchema="Utils.osPathJoin(basePath, src, main, r"{trunked}</code> */
    public static final String pathToSchema = Utils.osPathJoin(basePath, "src", "main", "resources", "Scarf", "scarf_v1.2.xsd");
    /** Constant <code>pathToAPK="Utils.osPathJoin(basePath, samples, app"{trunked}</code> */
    public static final String pathToAPK = Utils.osPathJoin(testRec, "app-debug.apk");

    /**
     * <p>makeArg.</p>
     *
     * @param id a {@link frontEnd.argsIdentifier} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id, String value) {
        return makeArg(id.getId(), value);
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id a {@link frontEnd.argsIdentifier} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id) {
        return id.getArg() + " ";
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id a {@link frontEnd.MessagingSystem.routing.inputStructures.ScarfXMLId} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(ScarfXMLId id, String value) {
        return makeArg(id.getId(), value);
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(String id, String value) {
        return "-" + id + " " + value + " ";
    }
}

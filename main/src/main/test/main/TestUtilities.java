package main;

import main.frontEnd.MessagingSystem.routing.inputStructures.ScarfXMLId;
import main.frontEnd.argsIdentifier;
import main.util.Utils;

public class TestUtilities {

    public static final Boolean isLinux = !System.getProperty("os.name").contains("Windows");

    public static final String basePath = System.getProperty("user.dir");
    public static final String scarfArgs = Utils.osPathJoin(basePath, "rsc", "sample.properties");
    public static final String srcOneGrv = basePath.replace("main", "testable-jar");

    public static final String jarOne = Utils.osPathJoin(srcOneGrv, "build", "libs", "testable-jar.jar");
    public static final String srcOneGrvDep = Utils.osPathJoin(srcOneGrv, "build", "dependencies");

    public static final String testPath = Utils.osPathJoin(basePath, "build", "tmp");
    public static final String pathToSchema = Utils.osPathJoin(basePath, "src", "main", "schema", "xsd", "Scarf", "Scarf.xsd");
    public static final String pathToAPK = Utils.osPathJoin(basePath.replace("main", ""), "app-debug.apk");

    public static String makeArg(argsIdentifier id, String value) {
        return makeArg(id.getId(), value);
    }

    public static String makeArg(argsIdentifier id) {
        return id.getArg() + " ";
    }

    public static String makeArg(ScarfXMLId id, String value) {
        return makeArg(id.getId(), value);
    }

    public static String makeArg(String id, String value) {
        return "-" + id + " " + value + " ";
    }

}

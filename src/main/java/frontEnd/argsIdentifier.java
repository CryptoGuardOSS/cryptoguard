package frontEnd;

import frontEnd.MessagingSystem.routing.Listing;
import lombok.Getter;
import rule.engine.EngineType;

/**
 * <p>argsIdentifier class.</p>" -o " + tempFileOutApk_Scarf
 *
 * @author CryptoguardTeam
 * Created on 2/5/19.
 * @version 03.07.01
 * @since 03.00.00
 *
 * <p>The central point for identifying the arguments and their description.</p>
 */
public enum argsIdentifier {

    //region Values
    FORMAT("in", "format", "Required: The format of input you want to scan, available styles " + EngineType.retrieveEngineTypeValues() + ".", "format"),
    SOURCE("s", "file/files/*.in/dir/ClassPathString", "Required: The source to be scanned use the absolute path or send all of the source files via the file input.in; ex. find -type f *.java >> input.in.", "file(s)/*.in/dir"),
    DEPENDENCY("d", "dir", "The dependency to be scanned use the relative path.", "dir"),
    OUT("o", "file", "The file to be created with the output default will be the project name.", "file"),
    NEW("new", null, "The file to be created with the output if existing will be overwritten.", null),
    TIMEMEASURE("t", null, "Output the time of the internal processes.", null),
    FORMATOUT("m", "formatType", "The output format you want to produce, " + Listing.getShortHelp() + ".", "formatType"),
    PRETTY("n", null, "Output the analysis information in a 'pretty' format.", null),
    NOEXIT("X", null, "Upon completion of scanning, don't kill the JVM", null),
    EXPERIMENTRESULTS("exp", null, "View the experiment based results.", null),
    VERSION("V", null, "Output the version number.", null),
    NOLOGS("vx", null, "Display logs only from the fatal logs", null),
    VERBOSE("v", null, "Display logs from debug levels", null),
    VERYVERBOSE("vv", null, "Display logs from trace levels", null),
    TIMESTAMP("ts", null, "Add a timestamp to the file output.", null),
    DEPTH("depth", null, "The depth of slicing to go into", "depth"),
    LOG("L", null, "Enable logging to the console.", null),
    JAVA("java", "envVariable", "Directory of Java to be used JDK 7 for JavaFiles/Project and JDK 8 for ClassFiles/Jar", "java"),
    ANDROID("android", "envVariable", "Specify of Android SDK", "android"),
    HEURISTICS("H", null, "The flag determining whether or not to display heuristics.", null),
    STREAM("st", null, "Stream the analysis to the output file.", null),
    HELP("h", null, "Print out the Help Information.", null),
    MAIN("main", "className", "Choose the main class if there are multiple main classes in the files given.", "main")
    //TODO - Implement an option to specify the base package
    //BASEPACKAGE("package", null, "Specify the base package path (src/main/java) if the project is not a generic Maven/Gradle project.")
    ;
    //endregion

    private String id;
    private String defaultArg;
    private String desc;
    @Getter
    private String argName;

    argsIdentifier(String id, String defaultArg, String desc, String argName) {
        this.id = id;
        this.defaultArg = defaultArg;
        this.desc = desc;
        this.argName = argName;
    }

    /**
     * <p>lookup.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @return a {@link frontEnd.argsIdentifier} object.
     */
    public static argsIdentifier lookup(String id) {
        for (argsIdentifier in : argsIdentifier.values())
            if (in.getId().equals(id))
                return in;
        return null;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getId() {
        return this.id;
    }

    /**
     * <p>getArg.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getArg() {
        return "-" + this.id;
    }

    /**
     * <p>Getter for the field <code>desc</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDesc() {
        return this.name() + ": " + this.desc;
    }

    /**
     * <p>hasDefaultArg.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean hasDefaultArg() {
        return defaultArg != null;
    }

    /**
     * <p>Getter for the field <code>defaultArg</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDefaultArg() {
        return defaultArg;
    }

    /**
     * <p>Setter for the field <code>defaultArg</code>.</p>
     *
     * @param defaultArg a {@link java.lang.String} object.
     */
    public void setDefaultArg(String defaultArg) {
        this.defaultArg = defaultArg;
    }

}

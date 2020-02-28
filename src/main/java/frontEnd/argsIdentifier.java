package frontEnd;

import frontEnd.MessagingSystem.routing.Listing;
import lombok.Getter;
import rule.engine.EngineType;

import java.util.ArrayList;
import java.util.List;

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
    FORMAT("in", "format", "Required: The format of input you want to scan, available styles " + EngineType.retrieveEngineTypeValues() + ".", "format", null, true),
    SOURCE("s", "file/files/*.in/dir/ClassPathString", "Required: The source to be scanned use the absolute path or send all of the source files via the file input.in; ex. find -type f *.java >> input.in.", "file(s)/*.in/dir", null, true),
    DEPENDENCY("d", "dir", "The dependency to be scanned use the relative path.", "dir", null, false),
    OUT("o", "file", "The file to be created with the output default will be the project name.", "file", null, false),
    NEW("new", null, "The file to be created with the output if existing will be overwritten.", null, null, false),
    TIMEMEASURE("t", null, "Output the time of the internal processes.", null, null, false),
    FORMATOUT("m", "formatType", "The output format you want to produce, " + Listing.getShortHelp() + ".", "formatType", null, true),
    PRETTY("n", null, "Output the analysis information in a 'pretty' format.", null, null, false),
    NOEXIT("X", null, "Upon completion of scanning, don't kill the JVM", null, null, false),
    //EXPERIMENTRESULTS("exp", null, "View the experiment based results.", null, null, false),
    VERSION("v", null, "Output the version number.", null, null, false),
    NOLOGS("VX", null, "Display logs only from the fatal logs", null, null, false),
    VERBOSE("V", null, "Display logs from debug levels", null, null, false),
    VERYVERBOSE("VV", null, "Display logs from trace levels", null, null, false),
    TIMESTAMP("ts", null, "Add a timestamp to the file output.", null, null, false),
    DEPTH("depth", null, "The depth of slicing to go into", "depth", null, false),
    //LOG("L", null, "Enable logging to the console.", null, null, false),
    JAVA("java", "envVariable", "Directory of Java to be used JDK 7 for JavaFiles/Project and JDK 8 for ClassFiles/Jar", "java", null, false),
    ANDROID("android", "envVariable", "Specify of Android SDK", "android", null, false),
    HEURISTICS("H", null, "The flag determining whether or not to display heuristics.", null, null, false),
    STREAM("st", null, "Stream the analysis to the output file.", null, null, false),
    HELP("h", null, "Print out the Help Information.", null, null, false),
    MAIN("main", "className", "Choose the main class if there are multiple main classes in the files given.", "main", null, false),
    SCONFIG("Sconfig", "file", "Choose the Scarf property configuration file.", "file.properties", Listing.ScarfXML, false),
    SASSESSFW("Sassessfw", "variable", "The assessment framework", "variable", Listing.ScarfXML, false),
    SASSESSFWVERSION("Sassessfwversion", "variable", "The assessment framework version", "variable", Listing.ScarfXML, false),
    SASSESSMENTSTARTTS("Sassessmentstartts", "variable", "The assessment start timestamp", "variable", Listing.ScarfXML, false),
    SBUILDFW("Sbuildfw", "variable", "The build framework", "variable", Listing.ScarfXML, false),
    SBUILDFWVERSION("Sbuildfwversion", "variable", "The build framework version", "variable", Listing.ScarfXML, false),
    SBUILDROOTDIR("Sbuildrootdir", "dir", "The build root directory", "variable", Listing.ScarfXML, false),
    SPACKAGENAME("Spackagename", "variable", "The package name", "variable", Listing.ScarfXML, false),
    SPACKAGEROOTDIR("Spackagerootdir", "dir", "The package root directory", "variable", Listing.ScarfXML, false),
    SPACKAGEVERSION("Spackageversion", "variable", "The package version", "variable", Listing.ScarfXML, false),
    SPARSERFW("Sparserfw", "variable", "The parser framework", "variable", Listing.ScarfXML, false),
    SPARSERFWVERSION("Sparserfwversion", "variable", "The parser framework version", "variable", Listing.ScarfXML, false),
    SUUID("Suuid", "uuid", "The uuid of the current pipeline progress", "variable", Listing.ScarfXML, false);
    //endregion

    private String id;
    private String defaultArg;
    private String desc;
    @Getter
    private Listing formatType;
    @Getter
    private String argName;
    @Getter
    private Boolean required;

    argsIdentifier(String id, String defaultArg, String desc, String argName, Listing format, Boolean required) {
        this.id = id;
        this.defaultArg = defaultArg;
        this.desc = desc;
        this.argName = argName;
        this.formatType = format;
        this.required = required;
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
     * <p>lookup.</p>
     *
     * @param type a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @return a {@link java.util.List} object.
     */
    public static List<argsIdentifier> lookup(Listing type) {
        List<argsIdentifier> args = new ArrayList<>();

        for (argsIdentifier in : argsIdentifier.values())
            if (in.formatType != null && in.formatType.equals(type))
                args.add(in);

        return args;
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

}

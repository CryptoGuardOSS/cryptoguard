package frontEnd;

import rule.engine.EngineType;

/**
 * <p>argsIdentifier class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/5/19.
 * @version $Id: $Id
 * @since 03.00.00
 *
 * <p>The central point for identifying the arguments and their description.</p>
 */
public enum argsIdentifier {

    FORMAT("in", "(Req'd) The format of input you want to scan, available styles " + EngineType.retrieveEngineTypeValues() + "."),
    SOURCE("s", "(Req'd) The source(s) to be scanned, use the absolute path)."),
    DEPENDENCY("d", "The dependency to be scanned, (use the relative path)."),
    OUT("o", "The file to be created with the output (default will be the project name)."),
    TIMEMEASURE("t", "Output the time of the internal processes."),
    FORMATOUT("m", "The output format you want to produce."),
    PRETTY("n", "Output the analysis information in a 'pretty' format."),
    SKIPINPUTVALIDATION("x", "Skip input validation."),
    EXPERIMENTRESULTS("exp", "View the experiment based results."),
    VERSION("v", "Output the version number."),
    TIMESTAMP("ts", "Add a timestamp to the file output."),
    STREAM("st", "Stream the analysis to the output file (still being tested)."),
    HELP("h", "Print out the Help Information.");

    private String id;
    private String desc;

    argsIdentifier(String id, String desc) {
        this.id = id;
        this.desc = desc;
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
     * <p>lookup.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @return a {@link argsIdentifier} object.
     */
    public static argsIdentifier lookup(String id) {
        for (argsIdentifier in : argsIdentifier.values())
            if (in.getId().equals(id))
                return in;
        return null;
    }

}

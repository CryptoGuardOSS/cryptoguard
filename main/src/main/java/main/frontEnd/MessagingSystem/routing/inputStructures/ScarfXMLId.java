package main.frontEnd.MessagingSystem.routing.inputStructures;

public enum ScarfXMLId {

    AssessmentFramework("Saf", "The assessment framework."),
    AssessmentFrameworkVersion("Safv", "The assessment framework version."),
    BuildRootDir("Sbrd", "The directory of the root."),
    PackageRootDir("Sprd", "The directory of the package ."),
    ParserName("Spn", "The name of the parser."),
    ParserVersion("Spv", "The version of the parser."),
    UUID("Sid", "The uuid of the current id.");

    private String id;
    private String desc;

    ScarfXMLId(String id, String desc) {
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
     * @return a {@link main.frontEnd.argsIdentifier} object.
     */
    public static ScarfXMLId lookup(String id) {
        for (ScarfXMLId in : ScarfXMLId.values())
            if (in.getId().equals(id))
                return in;
        return null;
    }

}
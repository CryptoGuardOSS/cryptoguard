package frontEnd.MessagingSystem.routing.inputStructures;

/**
 * <p>ScarfXMLId class.</p>
 *
 * @author drmonster
 * @version $Id: $Id
 */
public enum ScarfXMLId {

    ConfigFile("Sconfig", "The configuration file for Scarf Header attributes.");

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
     * @return a {@link frontEnd.argsIdentifier} object.
     */
    public static ScarfXMLId lookup(String id) {
        for (ScarfXMLId in : ScarfXMLId.values())
            if (in.getId().equals(id))
                return in;
        return null;
    }

}

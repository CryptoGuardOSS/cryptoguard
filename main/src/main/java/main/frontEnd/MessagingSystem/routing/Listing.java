package main.frontEnd.MessagingSystem.routing;

import main.frontEnd.MessagingSystem.routing.inputStructures.InputStructure;
import main.frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;

/**
 * The enum containing all of the different messaging types available for the user.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V01.00.00
 */
public enum Listing {
    //region Different Values
    Legacy("Legacy", "L", ".txt"),
    ScarfXML("ScarfXML", "SX", ".xml");
    //endregion
    //region Attributes
    private String type;
    private String flag;
    private String outputFileExt;
    private final String inputPath = "main.frontEnd.MessagingSystem.routing.inputStructures";
    private final String outputPath = "main.frontEnd.MessagingSystem.routing.outputStructures";
    //endregion

    //region Constructor

    /**
     * The inherint constructor of all the enum value types listed here
     *
     * @param Type - the string value of the type of
     * @param Flag - the flag used to identify the specific messaging type
     */
    Listing(String Type, String Flag, String outputFileExt) {
        this.type = Type;
        this.flag = Flag;
        this.outputFileExt = outputFileExt;
    }
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{ \"type\": \"" + this.type + "\", \"flag\": \"" + this.flag + "\"}";
    }
    //endregion

    //region Getter

    /**
     * The dynamic loader for the Listing Type based on the flag
     *
     * @param flag {@link java.lang.String} - The input type looking for the flag type
     * @return {@link main.frontEnd.MessagingSystem.routing.Listing} - The messaging Type retrieved by flag, if not found the default will be used
     */
    public static Listing retrieveListingType(String flag) {
        if (flag != null)
            for (Listing type : Listing.values())
                if (type.flag.equals(flag))
                    return type;

        return Listing.Legacy;
    }

    /**
     * Getter for flag
     *
     * <p>getFlag()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFlag() {
        return flag;
    }

    /**
     * Getter for type
     *
     * <p>getType()</p>
     *
     * @return {@link java.lang.String} - The type.
     */
    public String getType() {
        return type;
    }
    //endregion

    //region Helpers Based on the enum type

    /**
     * A method to dynamically retrieve the type of messaging structure asked for by the flag type.
     * NOTE: if there is any kind of issue instantiating the class name, it will default to the Legacy Output
     *
     * @return outputStructure - the type of messaging structure to be used to return information
     */
    public OutputStructure getTypeOfMessagingOutput() {


        try {
            //Return a dynamically loaded instantiation of the class
            return (OutputStructure) Class.forName(this.outputPath + "." + this.type).newInstance();
        }
        //In Case of any error, default to the Legacy Output
        catch (Exception e) {
            return new main.frontEnd.MessagingSystem.routing.outputStructures.Legacy();
        }
    }

    /**
     * A method to dynamically retrieve the type of messaging structure asked for by the flag type.
     * NOTE: if there is any kind of issue instantiating the class name, it will default to the Legacy Input
     *
     * @return outputStructure - the type of messaging structure to be used to return information
     */
    public InputStructure getTypeOfMessagingInput() {

        try {
            //Return a dynamically loaded instantiation of the class
            return (InputStructure) Class.forName(inputPath + "." + this.type).newInstance();
        } catch (Exception e) {
            return new main.frontEnd.MessagingSystem.routing.inputStructures.Legacy();
        }
    }

    /**
     * A method to dynamically retrieve the args of messaging structure asked for by the flag type for help
     * with error checking.
     *
     * @return string - the help string provided by each messaging interface
     */
    public String getInputHelp() {

        try {
            //Return a dynamically loaded instantiation of the class
            return ((InputStructure) Class.forName(inputPath + "." + this.type).newInstance()).helpInfo();
        } catch (Exception e) {
            return new main.frontEnd.MessagingSystem.routing.inputStructures.Legacy().helpInfo();
        }
    }

    /**
     * <p>Getter for the field <code>outputFileExt</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutputFileExt() {
        return outputFileExt;
    }
    //endregion
}

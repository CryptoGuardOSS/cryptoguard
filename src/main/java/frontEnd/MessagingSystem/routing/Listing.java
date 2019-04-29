package frontEnd.MessagingSystem.routing;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import frontEnd.MessagingSystem.routing.outputStructures.block.Structure;
import frontEnd.MessagingSystem.routing.outputStructures.stream.Legacy;

/**
 * The enum containing all of the different messaging types available for the user.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V01.00.00
 */
public enum Listing {
    //region Different Values
    Legacy("Legacy", "L", ".txt", true),
    ScarfXML("ScarfXML", "SX", ".xml", true);
    //endregion
    //region Attributes
    private String type;
    private String flag;
    private String outputFileExt;
    private final String blockPath = "frontEnd.MessagingSystem.routing.outputStructures.block.";
    private final String inputPath = "frontEnd.MessagingSystem.routing.inputStructures";
    private final String streamPath = "frontEnd.MessagingSystem.routing.outputStructures.stream.";
    private Boolean streamEnabled;
    //endregion

    //region Constructor

    /**
     * The inherint constructor of all the enum value types listed here
     *
     * @param Type - the string value of the type of
     * @param Flag - the flag used to identify the specific messaging type
     */
    Listing(String Type, String Flag, String outputFileExt, Boolean streamed) {
        this.type = Type;
        this.flag = Flag;
        this.outputFileExt = outputFileExt;
        this.streamEnabled = streamed;
    }
    //endregion

    //region Overridden Methods

    /**
     * <p>getInputFullHelp.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getInputFullHelp() {
        StringBuilder out = new StringBuilder();

        for (Listing listingType : Listing.values())
            out.append(listingType.getInputHelp()).append("\n");

        return out.toString();
    }
    //endregion

    //region Getter

    /**
     * The dynamic loader for the Listing Type based on the flag
     *
     * @param flag {@link java.lang.String} - The input type looking for the flag type
     * @return {@link frontEnd.MessagingSystem.routing.Listing} - The messaging Type retrieved by flag, if not found the default will be used
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

    //region Output Helpers

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{ \"type\": \"" + this.type + "\", \"flag\": \"" + this.flag + "\"}";
    }

    /**
     * <p>getTypeOfMessagingOutput.</p>
     *
     * @param stream a boolean.
     * @param info   a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @return a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public OutputStructure getTypeOfMessagingOutput(boolean stream, EnvironmentInformation info) throws ExceptionHandler {

        if (stream) {
            if (!this.streamEnabled)
                throw new ExceptionHandler("Streaming is not supported for the format: " + this.getType(), ExceptionId.GEN_VALID);
            else {
                try {
                    return (frontEnd.MessagingSystem.routing.outputStructures.stream.Structure) Class.forName(this.streamPath + this.type).getConstructor(EnvironmentInformation.class).newInstance(info);
                } catch (Exception e) {
                    return new Legacy(info);
                }
            }
        } else //non-streamed
        {
            try {
                return (Structure) Class.forName(this.blockPath + this.type).getConstructor(EnvironmentInformation.class).newInstance(info);
            } catch (Exception e) {
                return new frontEnd.MessagingSystem.routing.outputStructures.block.Legacy(info);
            }
        }

    }

    //endregion

    //region InputHelpers

    /**
     * <p>Getter for the field <code>outputFileExt</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutputFileExt() {
        return outputFileExt;
    }

    /**
     * A method to dynamically retrieve the type of messaging structure asked for by the flag type.
     * NOTE: if there is any kind of issue instantiating the class name, it will default to the Legacy Input
     *
     * @return outputStructure - the type of messaging structure to be used to return information
     */
    public frontEnd.MessagingSystem.routing.inputStructures.Structure getTypeOfMessagingInput() {

        try {
            //Return a dynamically loaded instantiation of the class
            return (frontEnd.MessagingSystem.routing.inputStructures.Structure) Class.forName(inputPath + "." + this.type).newInstance();
        } catch (Exception e) {
            return new frontEnd.MessagingSystem.routing.inputStructures.Legacy();
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
            return ((frontEnd.MessagingSystem.routing.inputStructures.Structure) Class.forName(inputPath + "." + this.type).newInstance()).helpInfo();
        } catch (Exception e) {
            return new frontEnd.MessagingSystem.routing.inputStructures.Legacy().helpInfo();
        }
    }

    /**
     * <p>getShortHelp.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getShortHelp() {
        StringBuilder helpString = new StringBuilder("{");

        for (Listing in : Listing.values())
            helpString.append(in.getType()).append(": ").append(in.getFlag()).append(", ");

        helpString.append("}");

        return helpString.toString();
    }
    //endregion

    //endregion
}

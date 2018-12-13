package main.frontEnd.MessagingSystem.routing;

import main.frontEnd.MessagingSystem.routing.inputStructures.InputStructure;
import main.frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;

/**
 * The enum containing all of the different messaging types available for the user.
 *
 * @author franceme
 * @since V01.00.00
 */
public enum Listing {
    //region Different Values
    LegacyOutput("Legacy", "L"),
    ScarfXMLOutput("ScarfXML", "SX");
    //endregion
    //region Attributes
    private String type;
    private String flag;
    //endregion

    //region Constructor

    /**
     * The inherint constructor of all the enum value types listed here
     *
     * @param Type - the string value of the type of
     * @param Flag - the flag used to identify the specific messaging type
     */
    Listing(String Type, String Flag) {
        this.type = Type;
        this.flag = Flag;
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

    /**
     * A method to dynamically retrieve the type of messaging structure asked for by the flag type.
     * NOTE: if there is any kind of issue instantiating the class name, it will default to the Legacy Output
     *
     * @param flag - the type of Messaging Structure asked for
     * @return outputStructure - the type of messaging structure to be used to return information
     */
    public static OutputStructure getTypeOfMessagingOutput(String flag) {

        if (flag == null)
            return new main.frontEnd.MessagingSystem.routing.outputStructures.Legacy();

        try {
            String className;
            switch (flag) {
                case "SX":
                    className = ScarfXMLOutput.type;
                    break;
                default:
                    className = LegacyOutput.type;
                    break;
            }

            //Return a dynamically loaded instantiation of the class
            return (OutputStructure) Class.forName("main.frontEnd.MessagingSystem.routing.outputStructures." + className).newInstance();
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
     * @param flag - the type of Messaging Structure asked for
     * @return outputStructure - the type of messaging structure to be used to return information
     */
    public static InputStructure getTypeOfMessagingInput(String flag) {

        if (flag == null)
            return new main.frontEnd.MessagingSystem.routing.inputStructures.Legacy();

        try {
            String className;
            switch (flag) {
                case "SX":
                    className = ScarfXMLOutput.type;
                    break;
                default:
                    className = LegacyOutput.type;
                    break;
            }

            //Return a dynamically loaded instantiation of the class
            return (InputStructure) Class.forName("main.frontEnd.MessagingSystem.routing.inputStructures." + className).newInstance();
        }
        //In Case of any error, default to the Legacy Output
        catch (Exception e) {
            return new main.frontEnd.MessagingSystem.routing.inputStructures.Legacy();
        }
    }

    /**
     * The method retrieving all of the input help for each of the messaging system types
     *
     * @return {@link java.lang.String} - The full multi-line string describing help for each type
     */
    public static String getInputHelp() {
        StringBuilder help = new StringBuilder();
        help.append("===========================================================\n");
        help.append("key: {}=required ()=optional \n");
        help.append("General Useage : java -jar {thisJar} {.apk/.jar file or sourcecode dir} {dir of dependencies, \"\" if there are none} (outputType) ({required depending on the output Type}) \n");
        help.append("===========================================================\n\n");

        for (Listing type : Listing.values()) {
            help.append("===========================================================\n");
            help.append("===============").append(type.type).append("===============\n");
            help.append("Flag : ").append(type.flag).append("\n");
            help.append(Listing.getTypeOfMessagingInput(type.flag).helpInfo()).append("\n");
            help.append("===========================================================\n\n");
        }

        return help.toString();
    }
}

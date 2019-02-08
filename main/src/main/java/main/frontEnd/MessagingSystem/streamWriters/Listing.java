package main.frontEnd.MessagingSystem.streamWriters;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * <p>Listing class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @since 03.02.00
 *
 * <p>The list of the different stream writing classes.</p>
 * @version $Id: $Id
 */
public enum Listing {
    //region Different Values
    Legacy(main.frontEnd.MessagingSystem.streamWriters.structures.LegacyStream.class, main.frontEnd.MessagingSystem.routing.Listing.Legacy),
    ScarfXML(main.frontEnd.MessagingSystem.streamWriters.structures.ScarfXMLStream.class, main.frontEnd.MessagingSystem.routing.Listing.ScarfXML);
    //endregion
    //region Attributes
    private Class type;
    private main.frontEnd.MessagingSystem.routing.Listing engineType;
    //endregion
    //region Constructor

    Listing(Class classType, main.frontEnd.MessagingSystem.routing.Listing type) {
        this.type = classType;
        this.engineType = type;
    }
    //endregion

    //region Getter


    /**
     * <p>retrieveWriterByType.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @return a {@link main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter} object.
     */
    public static baseStreamWriter retrieveWriterByType(EnvironmentInformation info) {
        for (Listing writer : Listing.values())
            if (writer.getEngineType() == info.getMessagingType())
                return writer.getType(info);
        return Listing.Legacy.getType(info);
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @return a {@link main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter} object.
     */
    public baseStreamWriter getType(EnvironmentInformation info) {

        try {
            return (baseStreamWriter) this.type.getDeclaredConstructor(EnvironmentInformation.class).newInstance(info);
        } catch (Exception e) {
            return new main.frontEnd.MessagingSystem.streamWriters.structures.LegacyStream(info);
        }
    }

    /**
     * <p>Getter for the field <code>engineType</code>.</p>
     *
     * @return a {@link main.frontEnd.MessagingSystem.routing.Listing} object.
     */
    public main.frontEnd.MessagingSystem.routing.Listing getEngineType() {
        return engineType;
    }
    //endregion
}

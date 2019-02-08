package main.frontEnd.MessagingSystem.streamWriters;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @since {VersionHere}
 *
 * <p>{Description Here}</p>
 */
public enum Listing {
    //region Different Values
    Legacy(main.frontEnd.MessagingSystem.streamWriters.structures.LegacyXMLStream.class, main.frontEnd.MessagingSystem.routing.Listing.Legacy),
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


    public static baseStreamWriter retrieveWriterByType(EnvironmentInformation info) {
        for (Listing writer : Listing.values())
            if (writer.getEngineType() == info.getMessagingType())
                return writer.getType(info);
        return Listing.Legacy.getType(info);
    }

    public baseStreamWriter getType(EnvironmentInformation info) {

        try {
            return (baseStreamWriter) this.type.getDeclaredConstructor(EnvironmentInformation.class).newInstance(info);
        } catch (Exception e) {
            return new main.frontEnd.MessagingSystem.streamWriters.structures.LegacyXMLStream(info);
        }
    }

    public main.frontEnd.MessagingSystem.routing.Listing getEngineType() {
        return engineType;
    }
    //endregion
}

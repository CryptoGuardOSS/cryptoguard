package main;

import main.frontEnd.MessagingSystem.routing.inputStructures.ScarfXMLId;
import main.frontEnd.argsIdentifier;

public class TestUtilities {

    public static String makeArg(argsIdentifier id, String value) {
        return makeArg(id.getId(), value);
    }

    public static String makeArg(ScarfXMLId id, String value) {
        return makeArg(id.getId(), value);
    }

    public static String makeArg(String id, String value) {
        return "-" + id + " " + value + " ";
    }

}

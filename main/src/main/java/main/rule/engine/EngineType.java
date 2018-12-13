package main.rule.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * The different types of "sources" accepted to examine.
 * <ul>
 * <li>JAR - A Jar file with or without external dependencies</li>
 * <li>APK - A Apk file with or without external dependencies</li>
 * <li>SOURCE - The source code of a Maven or a Gradle Project</li>
 * </ul>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public enum EngineType {
    //region Values
    JAR("JAR File", "jar", ".jar"),
    APK("APK File", "apk", ".apk"),
    SOURCE("Source Code", "source", "dir");
    //endregion

    //region Attributes
    private String name;
    private String flag;
    private String inputExtension;
    //endregion

    //region Constructor

    /**
     * The base constructor for the EngineType
     *
     * @param name - the human readable name of the engine type
     * @param flag - the flag used to identify the engine type
     */
    EngineType(String name, String flag, String extension) {
        this.name = name;
        this.flag = flag;
        this.inputExtension = extension;
    }
    //endregion

    //region Getters

    /**
     * The getter for the flag
     *
     * @return string - the flag of the engine type
     */
    public String getFlag() {
        return this.flag;
    }

    /**
     * The getter for the human readable name of the engine type
     *
     * @return string - the name of the engine type
     */
    public String getName() {
        return this.name;
    }


    /**
     * The getter for the extension
     *
     * @return {@link java.lang.String} - The extension for the engine Type
     */
    public String getInputExtension() {
        return this.inputExtension;
    }

    /**
     * The method to retrieve the engine type from the flag
     *
     * @param flag - the flag used to look for the specified engine type
     * @return - either null if no flag matched or the engine type
     */
    public static EngineType getFromExt(String flag) {
        for (EngineType type : EngineType.values())
            if (type.inputExtension.equals(flag)) {
                return type;
            }

        return null;
    }

    /**
     * The method to automatically retrieve a list of the available flag types
     *
     * @return list string - the different available engine type flags
     */
    public static List<String> flagTypes() {
        List<String> flags = new ArrayList<>();
        for (EngineType type : EngineType.values())
            flags.add(type.flag);
        return flags;
    }
    //endregion
}

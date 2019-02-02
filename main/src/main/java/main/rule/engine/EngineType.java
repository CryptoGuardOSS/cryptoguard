package main.rule.engine;

import main.frontEnd.Interface.ExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    JAR("JAR File", "jar", ".jar", "To signal a Jar File to be scanned.", "-s {(absolute path)/file.jar} -d {relative path to dependencies}"),
    APK("APK File", "apk", ".apk", "To signal a APK File to be scanned.", "-s {(absolute path)/sourcefile.apk}"),
    DIR("Directory of Source Code", "source", "dir", "To signal the source directory of a Maven/Gradle Project.", "-s {absolute path to dir}"),
    JAVAFILES("Java File(s)", "java", ".java", "To signal a Java File(s) to be scanned.", "-s {(absolute path)/file.java (s)}"),
    CLASSFILES("Class File(s)", "class", ".class", "To signal a Class File(s) to be scanned.", "-s {(absolute path)/file.class (s)} -d {relative path to dependencies}");
    //endregion

    //region Attributes
    private String name;
    private String flag;
    private String inputExtension;

    private String helpInfo;
    private String validInfo;
    //endregion

    //region Constructor

    /**
     * The base constructor for the EngineType
     *
     * @param name - the human readable name of the engine type
     * @param flag - the flag used to identify the engine type
     */
    EngineType(String name, String flag, String extension, String helpInfo, String validInfo) {
        this.name = name;
        this.flag = flag;
        this.inputExtension = extension;
        this.helpInfo = helpInfo;
        this.validInfo = validInfo;
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
     * The getter for helpInfo
     *
     * <p>getHelpInfo()</p>
     *
     * @return {@link String} - Returns the HelpInfo field
     */
    public String getHelpInfo() {
        return helpInfo;
    }

    /**
     * The method to retrieve the engine type from the flag
     *
     * @param flag - the flag used to look for the specified engine type
     * @return - either null if no flag matched or the engine type
     */
    public static EngineType getFromFlag(String flag) {
        for (EngineType type : EngineType.values())
            if (type.flag.equalsIgnoreCase(flag)) {
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

    /**
     * The method to automatically retrieve all of the help info for all of the different use cases.
     *
     * @return {@link String} - The full help info for console use
     */
    public static String getHelp() {
        StringBuilder out = new StringBuilder();

        for (EngineType type : EngineType.values())
            out.append(type.getFlag()).append(" : ").append(type.getHelpInfo()).append("\n");

        return out.toString();
    }

    public static String getValidHelp() {
        StringBuilder help = new StringBuilder();

        help.append("===========================================================\n")
                .append("key: {}=required ()=optional \n");
        for (EngineType type : EngineType.values()) {
            help.append("\t===========================================================\n");

            help.append("\tType : ").append(type.name).append("\n");
            help
                    .append("\tjava7 -jar rigorityj.jar ")
                    .append(type.flag)
                    .append(" ")
                    .append(type.validInfo)
                    .append("(Output Type flag) ({required depending on the output Type})")
                    .append("\n");

            help.append("\t===========================================================\n");
        }
        help.append("===========================================================\n");
        return help.toString();
    }

    public List<String> retrieveDirs(List<String> arguments) throws ExceptionHandler {
        List<String> dirs = new ArrayList<>();
        for (String dir : arguments) {
            File dirChecking = new File(dir);
            if (!dirChecking.exists() || !dirChecking.isDirectory())
                throw new ExceptionHandler(dirChecking.getName() + " is not a valid directory.");

            try {
                dirs.add(dirChecking.getCanonicalPath());
            } catch (Exception e) {
                throw new ExceptionHandler("Error retrieving the path of the directory.");
            }
        }
        return dirs;
    }

    public List<String> retrieveFilesByType(List<String> arguments) throws ExceptionHandler {
        if (this == EngineType.DIR)
            if (arguments.size() != 1)
                throw new ExceptionHandler("Please enter one argument for this use case.");
            else
                return retrieveDirs(arguments);

        List<String> filePaths = new ArrayList<>();
        for (String in : arguments) {
            if (!in.endsWith(inputExtension))
                throw new ExceptionHandler("File " + in + "doesn't have the right file type ");

            File tempFile = new File(in);
            if (!tempFile.exists() || !tempFile.isFile())
                throw new ExceptionHandler(tempFile.getName() + " is not a valid file.");

            try {
                filePaths.add(tempFile.getCanonicalPath());
            } catch (Exception e) {
                throw new ExceptionHandler("Error retrieving the path of the file " + tempFile.getName() + ".");
            }
        }
        return filePaths;
    }

    public Map<String, List<String>> retrieveInputsFromInput(List<String> args, Integer messageType) throws ExceptionHandler {
        Map<String, List<String>> source_dependencies = new HashMap<>();

        if (!args.get(0).equals("-s"))
            throw new ExceptionHandler("You need to pass the -s for the source files/dir.");

        Integer dependencyLoc = args.indexOf("-d");
        Integer msgLoc = messageType;

        Integer sourceEnding = dependencyLoc != -1 ? dependencyLoc : (msgLoc != -1 ? msgLoc - 1 : args.size());

        if (sourceEnding == 0)
            throw new ExceptionHandler("You need to pass a source files/dir.");

        source_dependencies.put("source",
                retrieveFilesByType(args.subList(1, sourceEnding)));

        if (dependencyLoc != -1) {
            Integer dependencyEnding = msgLoc != -1 ? msgLoc : args.size();

            if (this != EngineType.JAR && this != EngineType.CLASSFILES && this != EngineType.DIR)
                throw new ExceptionHandler("You can only pass dependencies with the Jar or Java Class Path.");

            //region TODO - check these relative pathes
            /*
            source_dependencies.put("dependencies",
                    retrieveDirs(args.subList(sourceEnding + 1,dependencyEnding)));
            */
            //endregion
            //TODO - Depdencency pulling/checking
            source_dependencies.put("dependencies",
                    args.subList(sourceEnding + 1, dependencyEnding - 1));

        }

        String pkg = null;

        List<String> basePath = new ArrayList<String>();
        File sourceFile;
        switch (this) {
            case APK:
            case JAR:
                sourceFile = new File(source_dependencies.get("source").get(0));
                basePath.add(sourceFile.getName());
                pkg = sourceFile.getName();
                break;
            case DIR:
                sourceFile = new File(source_dependencies.get("source").get(0));
                try {
                    basePath.add(sourceFile.getCanonicalPath() + ":dir");
                } catch (IOException e) {
                }
                pkg = sourceFile.getName();
                break;
            case JAVAFILES:
            case CLASSFILES:
                for (String file : source_dependencies.get("source")) {
                    try {
                        sourceFile = new File(file);
                        basePath.add(sourceFile.getCanonicalPath());

                        if (pkg == null) {
                            pkg = sourceFile.getCanonicalPath();//.replace(sourceFile.getName(), "")
                        }

                    } catch (IOException e) {
                    }
                }
                break;
        }

        source_dependencies.put("sourcePaths", basePath);
        source_dependencies.put("sourcePkg", Arrays.asList(pkg));

        return source_dependencies;
    }
    //endregion
}

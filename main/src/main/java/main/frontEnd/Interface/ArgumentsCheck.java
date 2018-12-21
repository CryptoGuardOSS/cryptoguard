package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;

import java.util.ArrayList;
import java.util.Arrays;

import static main.util.Utils.retrieveFullyQualifiedName;
import static main.util.Utils.trimFilePath;

/**
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @since 01.01.02
 *
 * <p>The main check for the Arguments</p>
 */
public class ArgumentsCheck {

    //TODO - Need To Unit Test These Paths
    /**
     * The fail fast parameter Check method
     * <p>This method will attempt to create the Environment Information and provide help if the usage doesn't match</p>
     *
     * @param args {@link java.lang.String[]} - the raw arguments passed into the console
     * @return {@link EnvironmentInformation} - when not null, the general Information is created for usage within any output structure.
     */
    public static EnvironmentInformation paramaterCheck(String[] args) {

        EngineType flow;
        EnvironmentInformation info = null;

        //Needs a minimum of at least two arguments, flag type and source directory
        if (args.length >= 2 && (flow = EngineType.getFromFlag(args[0])) != null) {

            ArrayList<String> sourceFiles = new ArrayList<>();
            String dependency = null;
            Integer argumentSplit = -1;
            String filePath;

            switch (flow) {
                //region APK
                case APK:
                    filePath = args[1];

                    if (!filePath.endsWith(".apk") || args[2].equals("-d")) {
                        failFast();
                        return null;
                    }

                    argumentSplit = 2;
                    sourceFiles.add(filePath);


                    break;
                //endregion
                //region Jar
                case JAR:
                    filePath = args[1];

                    if (!filePath.endsWith(".jar")) {
                        failFast();
                        return null;
                    }

                    if (args[2].equals("-d")) {
                        dependency = args[3];
                        argumentSplit = 4;
                    } else
                        argumentSplit = 2;

                    sourceFiles.add(filePath);

                    break;
                //endregion
                //region Dir
                case DIR:

                    filePath = args[1];

                    if (args[2].equals("-d")) {
                        failFast();
                        return null;
                    }

                    argumentSplit = 2;
                    sourceFiles.add(filePath);

                    break;
                //endregion
                //region JavaFiles
                case JAVAFILES:

                    argumentSplit = 1;
                    while (argumentSplit < args.length && !args[argumentSplit].equals("-d") && Listing.retrieveListingType(args[argumentSplit]) == null) {
                        if (!args[argumentSplit].endsWith(".java")) {
                            failFast();
                            return null;
                        } else
                            sourceFiles.add(retrieveFullyQualifiedName(args[argumentSplit]));

                        argumentSplit++;
                    }

                    break;
                //endregion
                //region ClassFiles
                case CLASSFILES:

                    argumentSplit = 1;
                    while (argumentSplit < args.length && !args[argumentSplit].equals("-d") && Listing.retrieveListingType(args[argumentSplit]) == null) {
                        if (!args[argumentSplit].endsWith(".class")) {
                            failFast();
                            return null;
                        } else
                            sourceFiles.add(trimFilePath(args[argumentSplit]));

                        argumentSplit++;
                    }

                    break;
                //endregion
            }

            String messagingType = args.length >= argumentSplit + 1 ? null : args[argumentSplit];
            info = new EnvironmentInformation(sourceFiles.toArray(new String[0]), flow, dependency, messagingType);
            String[] newArgs = Arrays.copyOfRange(args, argumentSplit, args.length);

            if (!info.getMessagingType().getTypeOfMessagingInput().inputValidation(info, newArgs)) {
                failFast();
                return null;
            }

            return info;

        } else {
            failFast();
            return null;
        }

    }

    /**
     * A universal method to return failure/help message
     */
    private static void failFast() {
        System.out.println(Listing.getInputHelp());
        return;
    }

}

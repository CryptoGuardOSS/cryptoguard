package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;

import java.util.List;
import java.util.Map;
/**
 * @author RigorityJTeam
 * Created on 12/13/18.
 * @since 01.01.02
 *
 * <p>The main check for the Arguments</p>
 */
public class ArgumentsCheck {

    /**
     * The fail fast parameter Check method
     * <p>This method will attempt to create the Environment Information and provide help if the usage doesn't match</p>
     *
     * @param args {@link java.lang.String} - the raw arguments passed into the console
     * @return {@link EnvironmentInformation} - when not null, the general Information is created for usage within any output structure.
     */
    public static EnvironmentInformation paramaterCheck(List<String> args) throws ExceptionHandler {

        EngineType flow;
        EnvironmentInformation info = null;


        //Needs a minimum of at least two arguments, flag type and source directory
        if (args.size() >= 3 && (flow = EngineType.getFromFlag(args.get(0))) != null) {

            Integer messageLoc = args.indexOf("-m");

            Map<String, List<String>> source_dependencies =
                    flow.retrieveInputsFromInput(
                            args.subList(1, args.size()), messageLoc
                    );

            Listing messageType = Listing.retrieveListingType(
                    messageLoc != -1 && args.size() == messageLoc + 2
                            ? args.get(messageLoc + 1) : null);

            info = new EnvironmentInformation(
                    source_dependencies.get("source"),
                    flow,
                    messageType,
                    source_dependencies.get("dependencies")
            );

            List<String> messagingArgs = args.subList(
                    messageLoc != -1 ? messageLoc : args.size() - 1, args.size() - 1
            );

            if (!info.getMessagingType().getTypeOfMessagingInput().inputValidation(info, messagingArgs.toArray(new String[0]))) {
                failFast();
                return null;
            }

            //region OldMethod
        /*

            ArrayList<String> sourceFiles = new ArrayList<>();
            String dependency = null;
            Integer argumentSplit = -1;
            String filePath;

            //region Old Split
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
                            sourceFiles.addAll(retrieveFullyQualifiedName(Arrays.asList(args[argumentSplit])));

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
            //endregion

            String messagingType = args.length >= argumentSplit + 1 ? null : args[argumentSplit];
            info = new EnvironmentInformation(sourceFiles.toArray(new String[0]), flow, messagingType, dependency);
            String[] newArgs = Arrays.copyOfRange(args, argumentSplit, args.length);

            if (!info.getMessagingType().getTypeOfMessagingInput().inputValidation(info, newArgs)) {
                failFast();
                return null;
            }
            */
            //endregion
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

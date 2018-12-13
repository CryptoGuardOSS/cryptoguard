package main.frontEnd.Interface;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

/**
 * @author RigorityJTeam
 * Created on 12/5/18.
 * @since 01.00.06
 *
 * <p>The main entry point of the program when this program
 * is used via command-line and not as a library</p>
 */
public class EntryPoint {

    public static void main(String[] args) {

        //Fail Fast on the input validation
        EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(args);
        if (generalInfo == null)
            System.exit(0);

        //TODO - need to ensure the reset of the System.out to the console output by the end
        //System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}

package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.commons.lang3.StringUtils;
import rule.engine.*;

import java.util.ArrayList;

/**
 * <p>EntryPoint class.</p>
 *
 * @author RigorityJTeam
 * Created on 12/5/18.
 * @version $Id: $Id
 * @since 01.00.06
 *
 * <p>The main entry point of the program when this program
 * is used via command-line and not as a library</p>
 */
public class EntryPoint {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {

        ArrayList<String> strippedArgs = new ArrayList<>();
        for (String arg : args)
            if (StringUtils.isNotEmpty(arg))
                strippedArgs.add(arg);

        try {
            //Fail Fast on the input validation
            EnvironmentInformation generalInfo = ArgumentsCheck.paramaterCheck(strippedArgs);

            EntryHandler handler = null;
            switch (generalInfo.getSourceType()) {
                case APK:
                    handler = new ApkEntry();
                    break;
                case JAR:
                    handler = new JarEntry();
                    break;
                case DIR:
                    handler = new SourceEntry();
                    break;
                case JAVAFILES:
                    handler = new JavaFileEntry();
                    break;
                case CLASSFILES:
                    handler = new JavaClassFileEntry();
                    break;
            }
            generalInfo.startScanning();

            handler.Scan(generalInfo);

            generalInfo.stopScanning();

        } catch (ExceptionHandler e) {

            if (e.getErrorCode().getId().equals(0))
                System.out.print(e.getLongDesciption());
            else
                System.err.print(e.toString());

            System.exit(e.getErrorCode().getId());
        }

    }
}
package main.frontEnd.MessagingSystem.streamWriters;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.EngineType;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @since 03.02.00
 *
 * <p>{Description Here}</p>
 */
public abstract class baseStreamWriter {

    private FileOutputStream streamOut;
    private EngineType type;
    private final Charset chars = Charset.forName("UTF-8");

    public baseStreamWriter(EnvironmentInformation info) {

        this.type = info.getSourceType();
        try {
            this.streamOut = new FileOutputStream(new File(info.getFileOut()));
        } catch (Exception e) {
            //TODO - handle this
            System.out.println("ERROR CREATING THE PRINTWRITER.");
            e.printStackTrace();
            System.exit(0);
        }
        this.writeHeader(info);
    }

    public void writeln(String output, Boolean pretty) {
        try {
            if (pretty)
                output += "\n";
            this.streamOut.write(output.getBytes(chars));
        } catch (Exception e) {
            //TODO - handle this
            System.out.println("ERROR writing THE PRINTWRITER.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void newln() {
        try {
            this.streamOut.write("\n".getBytes(chars));
        } catch (Exception e) {
            //TODO - handle this
            System.out.println("ERROR writing THE PRINTWRITER.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void write(String output) {
        try {
            this.streamOut.write(output.getBytes(chars));
        } catch (Exception e) {
            //TODO - handle this
            System.out.println("ERROR writing THE PRINTWRITER.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void close(EnvironmentInformation info) {
        info.openConsoleStream();
        this.writeFooter(info);
        try {
            this.streamOut.close();
        } catch (Exception e) {
            //TODO - handle this
            System.out.println("ERROR closing THE PRINTWRITER.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public abstract void writeHeader(EnvironmentInformation info);

    public abstract void streamIntoBody(AnalysisIssue issue);

    public abstract void writeFooter(EnvironmentInformation info);

    public EngineType getType() {
        return type;
    }
}

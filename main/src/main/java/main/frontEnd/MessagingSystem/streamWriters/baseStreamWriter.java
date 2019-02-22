package main.frontEnd.MessagingSystem.streamWriters;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.rule.engine.EngineType;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * <p>Abstract baseStreamWriter class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @since 03.02.00
 *
 * <p>The "interface" for the stream writing.</p>
 * @version $Id: $Id
 */
public abstract class baseStreamWriter {

    private FileOutputStream streamOut;
    private EngineType type;
    private final Charset chars = Charset.forName("UTF-8");

    /**
     * <p>Constructor for baseStreamWriter.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
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

    /**
     * <p>writeln.</p>
     *
     * @param output a {@link java.lang.String} object.
     * @param pretty a {@link java.lang.Boolean} object.
     */
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

    /**
     * <p>newln.</p>
     */
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

    /**
     * <p>write.</p>
     *
     * @param output a {@link java.lang.String} object.
     */
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

    /**
     * <p>close.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public void close(EnvironmentInformation info) {
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

    /**
     * <p>writeHeader.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public abstract void writeHeader(EnvironmentInformation info);

    /**
     * <p>streamIntoBody.</p>
     *
     * @param issue a {@link main.frontEnd.MessagingSystem.AnalysisIssue} object.
     */
    public abstract void streamIntoBody(AnalysisIssue issue);

    /**
     * <p>writeFooter.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public abstract void writeFooter(EnvironmentInformation info);

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link main.rule.engine.EngineType} object.
     */
    public EngineType getType() {
        return type;
    }
}

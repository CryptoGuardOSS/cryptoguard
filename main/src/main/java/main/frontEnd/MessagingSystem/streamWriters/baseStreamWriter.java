package main.frontEnd.MessagingSystem.streamWriters;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.Interface.ExceptionId;
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
 * @version $Id: $Id
 * @since 03.02.00
 *
 * <p>The "interface" for the stream writing.</p>
 */
public abstract class baseStreamWriter {

    private FileOutputStream streamOut;
    private EngineType type;
    private final Charset chars = Charset.forName("UTF-8");

    /**
     * <p>Constructor for baseStreamWriter.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public baseStreamWriter(EnvironmentInformation info) throws ExceptionHandler {

        this.type = info.getSourceType();
        try {
            this.streamOut = new FileOutputStream(new File(info.getFileOut()));
        } catch (Exception e) {
            throw new ExceptionHandler("Error creating the output stream with " + info.getFileOutName(), ExceptionId.FILE_IO);
        }
        this.writeHeader(info);
    }

    /**
     * <p>writeln.</p>
     *
     * @param output a {@link java.lang.String} object.
     * @param pretty a {@link java.lang.Boolean} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void writeln(String output, Boolean pretty) throws ExceptionHandler {
        try {
            if (pretty)
                output += "\n";
            this.streamOut.write(output.getBytes(chars));
        } catch (Exception e) {
            throw new ExceptionHandler("Error writing the output " + output, ExceptionId.FILE_IO);
        }
    }

    /**
     * <p>newln.</p>
     *
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void newln() throws ExceptionHandler {
        try {
            this.streamOut.write("\n".getBytes(chars));
        } catch (Exception e) {
            throw new ExceptionHandler("Error writing the nextline.", ExceptionId.FILE_IO);
        }
    }

    /**
     * <p>write.</p>
     *
     * @param output a {@link java.lang.String} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void write(String output) throws ExceptionHandler {
        try {
            this.streamOut.write(output.getBytes(chars));
        } catch (Exception e) {
            throw new ExceptionHandler("Error writing the output " + output, ExceptionId.FILE_IO);
        }
    }

    /**
     * <p>close.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void close(EnvironmentInformation info) throws ExceptionHandler {
        this.writeFooter(info);
        try {
            this.streamOut.close();
        } catch (Exception e) {
            throw new ExceptionHandler("Error closing the stream.", ExceptionId.FILE_IO);
        }
    }

    /**
     * <p>writeHeader.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public abstract void writeHeader(EnvironmentInformation info) throws ExceptionHandler;

    /**
     * <p>streamIntoBody.</p>
     *
     * @param issue a {@link main.frontEnd.MessagingSystem.AnalysisIssue} object.
     */
    public abstract void streamIntoBody(AnalysisIssue issue) throws ExceptionHandler;

    /**
     * <p>writeFooter.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public abstract void writeFooter(EnvironmentInformation info) throws ExceptionHandler;

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link main.rule.engine.EngineType} object.
     */
    public EngineType getType() {
        return type;
    }
}

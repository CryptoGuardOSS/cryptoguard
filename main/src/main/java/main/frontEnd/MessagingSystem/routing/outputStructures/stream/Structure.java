package main.frontEnd.MessagingSystem.routing.outputStructures.stream;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.Interface.ExceptionId;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;

/**
 * <p>Abstract Structure class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @version $Id: $Id
 * @since 03.02.00
 *
 * <p>The "interface" for the stream writing.</p>
 */
public abstract class Structure extends OutputStructure {

    //region Attributes
    private FileOutputStream streamOut;
    //endregion

    //region Constructors

    /**
     * <p>Constructor for Structure.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public Structure(EnvironmentInformation info) throws ExceptionHandler {
        super(info);

        try {
            this.streamOut = new FileOutputStream(super.getOutfile());
        } catch (Exception e) {
            throw new ExceptionHandler("Error creating the output stream with " + info.getFileOutName(), ExceptionId.FILE_CON);
        }
    }
    //endregion

    //region Overridden Methods
    //region From Super

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAnalyzing() throws ExceptionHandler {
        writeHeader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopAnalyzing() throws ExceptionHandler {
        writeFooter();
        close();
    }

    //endregion
    //region Self Overridden Methods

    /**
     * <p>writeHeader.</p>
     *
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public abstract void writeHeader() throws ExceptionHandler;

    /**
     * <p>writeFooter.</p>
     *
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public abstract void writeFooter() throws ExceptionHandler;
    //endregion
    //endregion

    //region Helper Methods

    /**
     * <p>writeln.</p>
     *
     * @param output a {@link java.lang.String} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void writeln(String output) throws ExceptionHandler {
        this.write(output + "\n");
    }

    /**
     * <p>write.</p>
     *
     * @param output a {@link java.lang.String} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void write(String output) throws ExceptionHandler {
        output = StringUtils.trimToNull(output);
        try {
            this.streamOut.write(output.getBytes(super.getChars()));
        } catch (Exception e) {
            throw new ExceptionHandler("Error writing the output " + output, ExceptionId.FILE_O);
        }
    }

    /**
     * <p>close.</p>
     *
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void close() throws ExceptionHandler {
        try {
            this.streamOut.close();
        } catch (Exception e) {
            throw new ExceptionHandler("Error closing the stream.", ExceptionId.FILE_CUT);
        }
    }
    //endregion

}

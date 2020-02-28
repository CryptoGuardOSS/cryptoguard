package frontEnd.MessagingSystem.routing.outputStructures.stream;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;

/**
 * <p>Abstract TypeSpecificArg class.</p>
 *
 * @author CryptoguardTeam
 * Created on 2/7/19.
 * @version 03.07.01
 * @since 03.02.00
 *
 * <p>The "interface" for the stream writing.</p>
 */
@Log4j2
public abstract class Structure extends OutputStructure {

    //region Attributes
    private FileOutputStream streamOut;
    //endregion

    //region Constructors

    /**
     * <p>Constructor for TypeSpecificArg.</p>
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public Structure(EnvironmentInformation info) throws ExceptionHandler {
        super(info);
        try {
            this.streamOut = new FileOutputStream(super.getOutfile());
        } catch (Exception e) {
            log.fatal("Error creating the output stream with " + info.getFileOutName());
            throw new ExceptionHandler("Error creating the output stream with " + info.getFileOutName(), ExceptionId.FILE_CON);
        }
    }

    /**
     * <p>Constructor for TypeSpecificArg.</p>
     */
    public Structure() {

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

    /** {@inheritDoc} */
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
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public abstract void writeHeader() throws ExceptionHandler;

    /**
     * <p>writeFooter.</p>
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public abstract void writeFooter() throws ExceptionHandler;
    //endregion
    //endregion

    //region Helper Methods

    /**
     * <p>writeln.</p>
     *
     * @param output a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void writeln(String output) throws ExceptionHandler {
        this.write(output + "\n");
    }

    /**
     * <p>write.</p>
     *
     * @param output a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void write(String output) throws ExceptionHandler {
        //output = StringUtils.trimToNull(output);
        if (output != null)
            try {
                this.streamOut.write(output.getBytes(super.getChars()));
            } catch (Exception e) {
                log.fatal("Error writing the output " + output);
                throw new ExceptionHandler("Error writing the output " + output, ExceptionId.FILE_O);
            }
    }

    /**
     * <p>close.</p>
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void close() throws ExceptionHandler {
        try {
            this.streamOut.close();
        } catch (Exception e) {
            log.fatal("Error closing the stream.");
            throw new ExceptionHandler("Error closing the stream.", ExceptionId.FILE_CUT);
        }
    }
    //endregion

}

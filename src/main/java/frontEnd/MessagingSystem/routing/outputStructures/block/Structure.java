package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;

/**
 * <p>Abstract Structure class.</p>
 *
 * @author CryptoguardTeam
 * Created on 3/2/19.
 * @version 03.07.01
 * @since 03.03.00
 *
 * <p>The overarching structure encompassing the block marshalling, extending from the output structure.</p>
 */
@Log4j2
public abstract class Structure extends OutputStructure {

    //region Attributes
    //endregion

    //region Constructor

    /**
     * <p>Constructor for Structure.</p>
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public Structure(EnvironmentInformation info) {
        super(info);
    }

    public Structure() {

    }
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAnalyzing() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
        super.addIssueToCollection(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopAnalyzing() throws ExceptionHandler {
        WriteIntoFile(StringUtils.stripToNull(this.handleOutput()));
    }

    //endregion
    //region Self OverriddenMethods

    /**
     * <p>handleOutput.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public abstract String handleOutput() throws ExceptionHandler;
    //endregion

    //region Helper Methods

    /**
     * <p>WriteIntoFile.</p>
     *
     * @param in a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void WriteIntoFile(String in) throws ExceptionHandler {
        try {
            Files.write(this.getOutfile().toPath(), in.getBytes(super.getChars()));
        } catch (IOException e) {
            log.debug("Error: " + e.getMessage());
            throw new ExceptionHandler("Error writing to file: " + this.getSource().getFileOutName(), ExceptionId.FILE_O);
        }
    }
    //endregion

}

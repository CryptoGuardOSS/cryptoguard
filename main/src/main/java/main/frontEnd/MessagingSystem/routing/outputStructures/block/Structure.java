package main.frontEnd.MessagingSystem.routing.outputStructures.block;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.Interface.ExceptionId;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;

/**
 * <p>Abstract Structure class.</p>
 *
 * @author RigorityJTeam
 * Created on 3/2/19.
 * @version $Id: $Id
 * @since 03.03.00
 *
 * <p>The overarching structure encompassing the block marshalling, extending from the output structure.</p>
 */
public abstract class Structure extends OutputStructure {

    //region Attributes
    //endregion

    //region Constructor

    /**
     * <p>Constructor for Structure.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public Structure(EnvironmentInformation info) {
        super(info);
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
    public void addIssue(AnalysisIssue issue) {
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
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public abstract String handleOutput() throws ExceptionHandler;
    //endregion

    //region Helper Methods

    /**
     * <p>WriteIntoFile.</p>
     *
     * @param in a {@link java.lang.String} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public void WriteIntoFile(String in) throws ExceptionHandler {
        try {
            Files.write(this.getOutfile().toPath(), in.getBytes(super.getChars()));
        } catch (IOException e) {
            throw new ExceptionHandler("Error writing to file: " + this.getSource().getFileOutName(), ExceptionId.FILE_O);
        }
    }
    //endregion

}

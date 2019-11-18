package frontEnd.MessagingSystem.routing.outputStructures.stream;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.*;

/**
 * <p>Legacy class.</p>
 *
 * @author CryptoguardTeam
 * Created on 2/7/19.
 * @version 03.07.01
 * @since 03.02.00
 *
 * <p>The Legacy stream writer.</p>
 */
public class Legacy extends Structure {

    //region Attributes
    //endregion

    //region Constructor

    /**
     * <p>Constructor for Legacy.</p>
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public Legacy(EnvironmentInformation info) throws ExceptionHandler {
        super(info);
    }
    //endregion

    //region Overridden Methods
    public OutputStructure deserialize(String filePath) throws ExceptionHandler {
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHeader() throws ExceptionHandler {
        this.writeln(marshallingHeader(super.getType(), super.getSource().getSource()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
        super.addIssue(issue);

        this.writeln("=======================================");
        this.writeln("***Violated Rule " + issue.getRuleId() + ": " + issue.getRule().getDesc());

        this.writeln(marshalling(issue, super.getType()));

        this.writeln("=======================================");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFooter() throws ExceptionHandler {
        this.writeln(marshallingSootErrors(super.getSource().getSootErrors()));

        //region Heuristics
        if (super.getSource().getDisplayHeuristics()) {
            this.writeln(frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.marshalling(super.getSource()));
        }
        //endregion

        if (super.getSource().isShowTimes()) {
            this.writeln(frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.marshalling(super.getSource().getAnalysisMilliSeconds()));
        }
    }
    //endregion

    //region HelperMethods
    //endregion

}

package frontEnd.MessagingSystem.routing.outputStructures.stream;

import frontEnd.Interface.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Legacy.*;

/**
 * <p>Legacy class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @version $Id: $Id
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
     * @param info a {@link EnvironmentInformation} object.
     * @throws ExceptionHandler if any.
     */
    public Legacy(EnvironmentInformation info) throws ExceptionHandler {
        super(info);
    }
    //endregion

    //region Overridden Methods

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

        if (super.getSource().isShowTimes()) {
            this.writeln(marshallingShowTimes(super.getSource().getAnalyisisTime()));
        }
    }
    //endregion

    //region HelperMethods
    //endregion

}

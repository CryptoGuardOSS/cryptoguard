package frontEnd.MessagingSystem.routing.outputStructures.block;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.outputStructures.common.CSVMapper;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import lombok.extern.log4j.Log4j2;

import java.io.File;

import static frontEnd.MessagingSystem.routing.outputStructures.common.Default.mapper;

/**
 * <p>Default class.</p>
 *
 * @author franceme
 * Created on 04/30/2019.
 * @version 03.07.01
 * @since 03.05.01
 *
 * <p>{Description Here}</p>
 */
@Log4j2
public class CSVDefault extends Structure {

    //region Attributes

    public final CSVMapper mapper;

    /**
     * {@inheritDoc}
     *
     * @param info a {@link EnvironmentInformation} object.
     */
    public CSVDefault(EnvironmentInformation info) {
        super(info);
        mapper = new CSVMapper(info);
    }

    /**
     * <p>Constructor for Default.</p>
     *
     * @param filePath a {@link String} object.
     * @throws ExceptionHandler if any.
     */
    public CSVDefault(String filePath) throws ExceptionHandler {
        Report struct = Report.deserialize(new File(filePath));

        EnvironmentInformation info = mapper(struct);
        mapper = new CSVMapper(info);
        super.setSource(info);
        super.setOutfile(new File(info.getFileOut()));
        super.setType(mapper(struct.getTarget().getType()));


        for (frontEnd.MessagingSystem.routing.structure.Default.Issue issue : struct.getIssues())
            super.addIssueToCollection(mapper(issue));
    }
    //endregion

    //region Constructor
    //endregion

    //region Overridden Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public String handleOutput() throws ExceptionHandler {

        StringBuilder contents = new StringBuilder();

        //region Setting the report for marshalling
        log.info("Marshalling the Report from the Env. Info.");
        contents.append(mapper.writeHeader()).append("\n");

        //region Creating Bug Instances
        log.trace("Adding all of the collected issues");
        for (AnalysisIssue in : super.getCollection()) {
            log.debug("Marshalling and adding the issue: " + in.getInfo());
            contents.append(mapper.writeIssue(in)).append("\n");
        }
        //endregion

        return contents.toString();

    }

    //endregion
}

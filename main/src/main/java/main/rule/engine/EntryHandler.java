package main.rule.engine;

import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;

import java.util.ArrayList;

/**
 * <p>EntryHandler interface.</p>
 *
 * @author RigorityJTeam
 * Created on 2019-01-20.
 * @version $Id: $Id
 * @since 02.02.00
 *
 * <p>The interface for how the the different use cases are called.</p>
 */
public interface EntryHandler {

    /**
     * The method to return all of the Analysis Issues in a block style method.
     *
     * @param generalInfo {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} - The basic information retrieved from the command line.
     * @return {@link main.frontEnd.MessagingSystem.AnalysisIssue} - Returns a list of captured issues.
     */
    public ArrayList<AnalysisIssue> NonStreamScan(EnvironmentInformation generalInfo);

    /**
     * The method to return all of the Analysis Issues in a stream style method.
     *
     * @param generalInfo {@link EnvironmentInformation} - The basic information retrieved from the command line.
     * @return {@link AnalysisIssue} - Returns a stream of captured issues.
     */
    /*public Stream<AnalysisIssue> StreamScan(EnvironmentInformation generalInfo);*/
}

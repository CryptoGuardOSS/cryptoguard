package main.rule.engine;

import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface of the RuleChecker class. Used to generate the structure of the various rules being checked.
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public interface RuleChecker {

    /**
     * The checkRule will verify the allocated rules for the given source.
     * Any failed rules will be printed to the commandline.
     *
     * @param type                  the type of engine to be used for the processing
     * @param projectJarPath        the path to the used jar(s)
     * @param projectDependencyPath the path to the dependencies of the project
     * @param printout              a {@link java.lang.Boolean} object.
     * @param sourcePaths           a {@link java.util.List} object.
     * @param streamWriter          a {@link main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter} object.
     * @return a {@link java.util.ArrayList} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    ArrayList<AnalysisIssue> checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, Boolean printout, List<String> sourcePaths, baseStreamWriter streamWriter) throws ExceptionHandler;
}

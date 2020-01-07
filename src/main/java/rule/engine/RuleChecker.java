package rule.engine;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;

import java.util.List;

/**
 * The interface of the RuleChecker class. Used to generate the structure of the various rules being checked.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public interface RuleChecker {

    /**
     * <p>checkRule.</p>
     *
     * @param type                  a {@link rule.engine.EngineType} object.
     * @param projectJarPath        a {@link java.util.List} object.
     * @param projectDependencyPath a {@link java.util.List} object.
     * @param sourcePaths           a {@link java.util.List} object.
     * @param output                a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
     * @param mainKlass             a {@link java.lang.String} object.
     * @param androidHome           a {@link java.lang.String} object.
     * @param javaHome              a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath, List<String> sourcePaths, OutputStructure output, String mainKlass, String androidHome, String javaHome) throws ExceptionHandler;
}

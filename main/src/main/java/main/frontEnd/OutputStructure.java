package main.frontEnd;

import main.rule.engine.EngineType;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * The general interface for the structure.
 * This will be implemented by the different type of messaging structures
 *
 * @author franceme
 * @since V01.00
 */
public interface OutputStructure {
	/**
	 * <p>getOutput.</p>
	 *
	 * @param source           a {@link main.frontEnd.EnvironmentInformation} object.
	 * @param type             a {@link main.rule.engine.EngineType} object.
	 * @param brokenRules      a {@link java.util.ArrayList} object.
	 * @param internalWarnings a {@link java.io.PrintStream} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getOutput(EnvironmentInformation source, EngineType type, ArrayList<AnalysisIssue> brokenRules, PrintStream internalWarnings);
}

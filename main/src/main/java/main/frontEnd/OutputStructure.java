package main.frontEnd;

import main.rule.engine.EngineType;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * The general interface for the structure.
 * This will be implemented by the different type of messaging structures
 */
public interface OutputStructure {
	String getOutput(EnvironmentInformation source, EngineType type, ArrayList<AnalysisIssue> brokenRules, PrintStream internalWarnings);
}

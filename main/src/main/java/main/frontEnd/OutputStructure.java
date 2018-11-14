package main.frontEnd;

import main.rule.engine.EngineType;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * The general interface for the structure.
 * This will be implemented by the different type of messaging structures
 */
public interface OutputStructure {
	Object getOutput(String source, EngineType type, ArrayList<AnalysisRule> brokenRules, PrintStream internalWarnings);
}

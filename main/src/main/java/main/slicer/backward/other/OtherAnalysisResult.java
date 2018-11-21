package main.slicer.backward.other;

import main.analyzer.backward.UnitContainer;
import soot.SootMethod;

import java.util.List;

/**
 * <p>OtherAnalysisResult class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00
 */
public class OtherAnalysisResult {

	private String instruction;
	private SootMethod method;
	private List<UnitContainer> analysis;

	/**
	 * <p>Constructor for OtherAnalysisResult.</p>
	 *
	 * @param instruction a {@link java.lang.String} object.
	 * @param method      a {@link soot.SootMethod} object.
	 * @param analysis    a {@link java.util.List} object.
	 */
	public OtherAnalysisResult(String instruction, SootMethod method, List<UnitContainer> analysis) {
		this.instruction = instruction;
		this.method = method;
		this.analysis = analysis;
	}

	/**
	 * <p>Getter for the field <code>instruction</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getInstruction() {
		return instruction;
	}

	/**
	 * <p>Getter for the field <code>method</code>.</p>
	 *
	 * @return a {@link soot.SootMethod} object.
	 */
	public SootMethod getMethod() {
		return method;
	}

	/**
	 * <p>Getter for the field <code>analysis</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<UnitContainer> getAnalysis() {
		return analysis;
	}
}

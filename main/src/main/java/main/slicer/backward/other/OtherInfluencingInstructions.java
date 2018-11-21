package main.slicer.backward.other;

import main.analyzer.backward.UnitContainer;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>OtherInfluencingInstructions class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00
 */
public class OtherInfluencingInstructions {

	private OtherAnalysisResult analysisResult;

	/**
	 * <p>Constructor for OtherInfluencingInstructions.</p>
	 *
	 * @param method          a {@link soot.SootMethod} object.
	 * @param slicingCriteria a {@link java.lang.String} object.
	 */
	public OtherInfluencingInstructions(SootMethod method, String slicingCriteria) {

		Body b = method.retrieveActiveBody();
		DirectedGraph methodToSlice = new ExceptionalUnitGraph(b);

		OtherInstructionSlicer analysis = new OtherInstructionSlicer(methodToSlice, slicingCriteria, method.toString());

		Iterator unitIt = methodToSlice.iterator();
		if (unitIt.hasNext()) {
			Unit s = (Unit) unitIt.next();

			FlowSet set = (FlowSet) analysis.getFlowBefore(s);
			List<UnitContainer> slicingResult = Collections.unmodifiableList(set.toList());

			analysisResult = new OtherAnalysisResult(slicingCriteria, method, slicingResult);
		}
	}

	/**
	 * <p>Getter for the field <code>analysisResult</code>.</p>
	 *
	 * @return a {@link main.slicer.backward.other.OtherAnalysisResult} object.
	 */
	public OtherAnalysisResult getAnalysisResult() {
		return analysisResult;
	}
}

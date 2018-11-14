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

public class OtherInfluencingInstructions {

	private OtherAnalysisResult analysisResult;

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

	public OtherAnalysisResult getAnalysisResult() {
		return analysisResult;
	}
}

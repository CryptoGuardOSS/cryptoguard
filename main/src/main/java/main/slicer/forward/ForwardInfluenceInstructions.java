package main.slicer.forward;

import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;

import java.util.Collections;
import java.util.List;

/**
 * Created by krishnokoli on 7/1/17.
 *
 * @author krishnokoli
 * @since V01.00.00
 */
public class ForwardInfluenceInstructions implements InfluenceInstructions {

	private SlicingResult slicingResult;

	/**
	 * <p>Constructor for ForwardInfluenceInstructions.</p>
	 *
	 * @param graph           a {@link soot.toolkits.graph.DirectedGraph} object.
	 * @param slicingCriteria a {@link main.slicer.forward.SlicingCriteria} object.
	 */
	public ForwardInfluenceInstructions(DirectedGraph graph,
										SlicingCriteria slicingCriteria) {
		ForwardProgramSlicing analysis = new ForwardProgramSlicing(graph, slicingCriteria);

		for (Object aGraph : graph) {
			Unit s = (Unit) aGraph;

			FlowSet set = (FlowSet) analysis.getFlowAfter(s);
			List<Unit> analysisResult = Collections.unmodifiableList(set.toList());

			this.slicingResult = new SlicingResult();

			this.slicingResult.setAnalysisResult(analysisResult);
			this.slicingResult.setCallSiteInfo(analysis.getMethodCallSiteInfo());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SlicingResult getSlicingResult() {
		return this.slicingResult;
	}

}

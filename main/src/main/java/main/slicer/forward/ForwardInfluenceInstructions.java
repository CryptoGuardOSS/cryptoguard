package main.slicer.forward;

import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;

import java.util.*;

/**
 * Created by krishnokoli on 7/1/17.
 */
public class ForwardInfluenceInstructions implements InfluenceInstructions {

    private SlicingResult slicingResult;

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

    @Override
    public SlicingResult getSlicingResult() {
        return this.slicingResult;
    }

}

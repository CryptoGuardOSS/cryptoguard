package main.slicer.backward.orthogonal;

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
 * Created by krishnokoli on 7/1/17.
 */
public class OrthogonalInfluenceInstructions {

    private OrthogonalSlicingResult orthogonalSlicingResult;

    public OrthogonalInfluenceInstructions(SootMethod method, String slicingCriteria, int depth) {

        Body b = method.retrieveActiveBody();
        DirectedGraph methodToSlice = new ExceptionalUnitGraph(b);

        OrthogonalInstructionSlicer analysis = new OrthogonalInstructionSlicer(methodToSlice, slicingCriteria, method.toString(), depth);

        Iterator unitIt = methodToSlice.iterator();
        if (unitIt.hasNext()) {
            Unit s = (Unit) unitIt.next();

            FlowSet set = (FlowSet) analysis.getFlowBefore(s);
            List<UnitContainer> analysisResult = Collections.unmodifiableList(set.toList());

            this.orthogonalSlicingResult = new OrthogonalSlicingResult();
            this.orthogonalSlicingResult.setPropertyUseMap(analysis.getPropertyUseMap());
            this.orthogonalSlicingResult.setAnalysisResult(analysisResult);
        }
    }

    public OrthogonalSlicingResult getOrthogonalSlicingResult() {
        return this.orthogonalSlicingResult;
    }
}

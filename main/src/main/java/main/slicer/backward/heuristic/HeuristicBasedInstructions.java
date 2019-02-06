package main.slicer.backward.heuristic;

import main.analyzer.backward.UnitContainer;
import main.slicer.backward.property.PropertyAnalysisResult;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HeuristicBasedInstructions {

    private HeuristicBasedAnalysisResult analysisResult;

    public HeuristicBasedInstructions(SootMethod method, String slicingCriteria) {

        Body b = method.retrieveActiveBody();
        DirectedGraph methodToSlice = new ExceptionalUnitGraph(b);

        HeuristicBasedInstructionSlicer analysis = new HeuristicBasedInstructionSlicer(methodToSlice, slicingCriteria, method.toString());

        Iterator unitIt = methodToSlice.iterator();
        if (unitIt.hasNext()) {
            Unit s = (Unit) unitIt.next();

            FlowSet set = (FlowSet) analysis.getFlowBefore(s);
            List<UnitContainer> slicingResult = Collections.unmodifiableList(set.toList());
            Map<String, List<PropertyAnalysisResult>> propertyUseMap = analysis.getPropertyUseMap();

            analysisResult = new HeuristicBasedAnalysisResult(slicingCriteria, method, slicingResult, propertyUseMap);
        }
    }

    public HeuristicBasedAnalysisResult getAnalysisResult() {
        return analysisResult;
    }
}

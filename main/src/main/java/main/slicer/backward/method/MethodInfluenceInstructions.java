package main.slicer.backward.method;

import main.analyzer.backward.UnitContainer;
import main.slicer.backward.MethodCallSiteInfo;
import main.slicer.backward.property.PropertyAnalysisResult;
import main.util.Utils;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by krishnokoli on 7/1/17.
 */
public class MethodInfluenceInstructions implements InfluenceInstructions {

    private MethodSlicingResult methodSlicingResult;

    public MethodInfluenceInstructions(DirectedGraph graph,
                                       MethodCallSiteInfo methodCallSiteInfo, List<Integer> slicingParams) {
        MethodInstructionSlicer analysis = new MethodInstructionSlicer(graph, methodCallSiteInfo, slicingParams);

        Iterator unitIt = graph.iterator();

        if (unitIt.hasNext()) {
            Unit s = (Unit) unitIt.next();

            FlowSet set = (FlowSet) analysis.getFlowBefore(s);
            List<UnitContainer> analysisResult = Collections.unmodifiableList(set.toList());

            this.methodSlicingResult = new MethodSlicingResult();
            this.methodSlicingResult.setPropertyUseMap(analysis.getPropertyUseMap());
            this.methodSlicingResult.setAnalysisResult(analysisResult);
            this.methodSlicingResult.setCallSiteInfo(analysis.getMethodCallSiteInfo());
            this.methodSlicingResult.setInfluencingParameters(Utils.findInfluencingParamters(analysisResult));
        }
    }

    public MethodSlicingResult getMethodSlicingResult() {
        return this.methodSlicingResult;
    }
}

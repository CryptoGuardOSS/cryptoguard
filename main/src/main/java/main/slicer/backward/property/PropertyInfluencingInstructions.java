package main.slicer.backward.property;

import main.analyzer.backward.MethodWrapper;
import main.analyzer.backward.UnitContainer;
import main.util.Utils;
import soot.Body;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PropertyInfluencingInstructions {

    private PropertyAnalysisResult slicingResult;

    public PropertyInfluencingInstructions(MethodWrapper initMethod, String slicingCriteria) {

        Body initBody = initMethod.getMethod().retrieveActiveBody();
        UnitGraph graph = new ExceptionalUnitGraph(initBody);
        PropertyInstructionSlicer analysis = new PropertyInstructionSlicer(graph, slicingCriteria, initMethod.toString());

        Iterator unitIt = graph.iterator();
        if (unitIt.hasNext()) {
            Unit s = (Unit) unitIt.next();

            FlowSet set = (FlowSet) analysis.getFlowBefore(s);

            List<UnitContainer> result = Collections.unmodifiableList(set.toList());

            slicingResult = new PropertyAnalysisResult();
            slicingResult.setMethodWrapper(initMethod);
            slicingResult.setSlicingResult(result);
            slicingResult.setInfluencingParams(Utils.findInfluencingParamters(result));
            slicingResult.setPropertyUseMap(analysis.getPropertyUseMap());
        }
    }

    public PropertyAnalysisResult getSlicingResult() {
        return this.slicingResult;
    }
}

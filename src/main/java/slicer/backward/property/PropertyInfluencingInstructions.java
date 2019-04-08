package slicer.backward.property;

import analyzer.backward.MethodWrapper;
import analyzer.backward.UnitContainer;
import soot.Body;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import util.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>PropertyInfluencingInstructions class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class PropertyInfluencingInstructions {

    private PropertyAnalysisResult slicingResult;

    /**
     * <p>Constructor for PropertyInfluencingInstructions.</p>
     *
     * @param initMethod      a {@link MethodWrapper} object.
     * @param slicingCriteria a {@link java.lang.String} object.
     */
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

    /**
     * <p>Getter for the field <code>slicingResult</code>.</p>
     *
     * @return a {@link PropertyAnalysisResult} object.
     */
    public PropertyAnalysisResult getSlicingResult() {
        return this.slicingResult;
    }
}

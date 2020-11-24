/* Licensed under GPL-3.0 */
package slicer.backward.property;

import analyzer.backward.MethodWrapper;
import analyzer.backward.UnitContainer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import util.Utils;

/**
 * PropertyInfluencingInstructions class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class PropertyInfluencingInstructions {

  private PropertyAnalysisResult slicingResult;

  /**
   * Constructor for PropertyInfluencingInstructions.
   *
   * @param initMethod a {@link analyzer.backward.MethodWrapper} object.
   * @param slicingCriteria a {@link java.lang.String} object.
   */
  public PropertyInfluencingInstructions(MethodWrapper initMethod, String slicingCriteria) {

    Body initBody = initMethod.getMethod().retrieveActiveBody();
    UnitGraph graph = new ExceptionalUnitGraph(initBody);
    PropertyInstructionSlicer analysis =
        new PropertyInstructionSlicer(graph, slicingCriteria, initMethod.toString());

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
   * Getter for the field <code>slicingResult</code>.
   *
   * @return a {@link slicer.backward.property.PropertyAnalysisResult} object.
   */
  public PropertyAnalysisResult getSlicingResult() {
    return this.slicingResult;
  }
}

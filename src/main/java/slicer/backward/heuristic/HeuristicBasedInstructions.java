/* Licensed under GPL-3.0 */
package slicer.backward.heuristic;

import analyzer.backward.UnitContainer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import slicer.backward.property.PropertyAnalysisResult;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;

/**
 * HeuristicBasedInstructions class.
 *
 * @author franceme
 * @version 03.07.01
 */
public class HeuristicBasedInstructions {

  private HeuristicBasedAnalysisResult analysisResult;

  /**
   * Constructor for HeuristicBasedInstructions.
   *
   * @param method a {@link soot.SootMethod} object.
   * @param slicingCriteria a {@link java.lang.String} object.
   */
  public HeuristicBasedInstructions(SootMethod method, String slicingCriteria) {

    Body b = method.retrieveActiveBody();
    DirectedGraph methodToSlice = new ExceptionalUnitGraph(b);

    HeuristicBasedInstructionSlicer analysis =
        new HeuristicBasedInstructionSlicer(methodToSlice, slicingCriteria, method.toString());

    Iterator unitIt = methodToSlice.iterator();
    if (unitIt.hasNext()) {
      Unit s = (Unit) unitIt.next();

      FlowSet set = (FlowSet) analysis.getFlowBefore(s);
      List<UnitContainer> slicingResult = Collections.unmodifiableList(set.toList());
      Map<String, List<PropertyAnalysisResult>> propertyUseMap = analysis.getPropertyUseMap();

      analysisResult =
          new HeuristicBasedAnalysisResult(slicingCriteria, method, slicingResult, propertyUseMap);
    }
  }

  /**
   * Getter for the field <code>analysisResult</code>.
   *
   * @return a {@link slicer.backward.heuristic.HeuristicBasedAnalysisResult} object.
   */
  public HeuristicBasedAnalysisResult getAnalysisResult() {
    return analysisResult;
  }
}

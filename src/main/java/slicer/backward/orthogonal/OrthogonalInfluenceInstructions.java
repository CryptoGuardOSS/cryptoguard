/* Licensed under GPL-3.0 */
package slicer.backward.orthogonal;

import analyzer.backward.UnitContainer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;

/**
 * Created by RigorityJTeam on 7/1/17.
 *
 * @author franceme
 * @version 03.07.01
 */
public class OrthogonalInfluenceInstructions {

  private OrthogonalSlicingResult orthogonalSlicingResult;

  /**
   * Constructor for OrthogonalInfluenceInstructions.
   *
   * @param method a {@link soot.SootMethod} object.
   * @param slicingCriteria a {@link java.lang.String} object.
   * @param depth a int.
   */
  public OrthogonalInfluenceInstructions(SootMethod method, String slicingCriteria, int depth) {

    Body b = method.retrieveActiveBody();
    DirectedGraph methodToSlice = new ExceptionalUnitGraph(b);

    OrthogonalInstructionSlicer analysis =
        new OrthogonalInstructionSlicer(methodToSlice, slicingCriteria, method.toString(), depth);

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

  /**
   * Getter for the field <code>orthogonalSlicingResult</code>.
   *
   * @return a {@link slicer.backward.orthogonal.OrthogonalSlicingResult} object.
   */
  public OrthogonalSlicingResult getOrthogonalSlicingResult() {
    return this.orthogonalSlicingResult;
  }
}

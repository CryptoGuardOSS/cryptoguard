/* Licensed under GPL-3.0 */
package slicer.backward.method;

import analyzer.backward.UnitContainer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import slicer.backward.MethodCallSiteInfo;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;
import util.Utils;

/**
 * Created by RigorityJTeam on 7/1/17.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class MethodInfluenceInstructions implements InfluenceInstructions {

  private MethodSlicingResult methodSlicingResult;

  /**
   * Constructor for MethodInfluenceInstructions.
   *
   * @param graph a {@link soot.toolkits.graph.DirectedGraph} object.
   * @param methodCallSiteInfo a {@link slicer.backward.MethodCallSiteInfo} object.
   * @param slicingParams a {@link java.util.List} object.
   */
  public MethodInfluenceInstructions(
      DirectedGraph graph, MethodCallSiteInfo methodCallSiteInfo, List<Integer> slicingParams) {
    MethodInstructionSlicer analysis =
        new MethodInstructionSlicer(graph, methodCallSiteInfo, slicingParams);

    Iterator unitIt = graph.iterator();

    if (unitIt.hasNext()) {
      Unit s = (Unit) unitIt.next();

      FlowSet set = (FlowSet) analysis.getFlowBefore(s);
      List<UnitContainer> analysisResult = Collections.unmodifiableList(set.toList());

      this.methodSlicingResult = new MethodSlicingResult();
      this.methodSlicingResult.setPropertyUseMap(analysis.getPropertyUseMap());
      this.methodSlicingResult.setAnalysisResult(analysisResult);
      this.methodSlicingResult.setCallSiteInfo(analysis.getMethodCallSiteInfo());
      this.methodSlicingResult.setInfluencingParameters(
          Utils.findInfluencingParamters(analysisResult));
    }
  }

  /**
   * Getter for the field <code>methodSlicingResult</code>.
   *
   * @return a {@link slicer.backward.method.MethodSlicingResult} object.
   */
  public MethodSlicingResult getMethodSlicingResult() {
    return this.methodSlicingResult;
  }
}

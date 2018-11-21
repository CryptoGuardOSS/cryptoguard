package main.slicer.backward.other;

import main.analyzer.backward.UnitContainer;
import main.slicer.ValueArraySparseSet;
import main.slicer.backward.property.PropertyAnalysisResult;
import main.util.FieldInitializationInstructionMap;
import soot.ArrayType;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.List;

/**
 * <p>OtherInstructionSlicer class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00
 */
public class OtherInstructionSlicer extends BackwardFlowAnalysis {

	private FlowSet emptySet;
	private String slicingCriteria;
	private String method;

	/**
	 * <p>Constructor for OtherInstructionSlicer.</p>
	 *
	 * @param g               a {@link soot.toolkits.graph.DirectedGraph} object.
	 * @param slicingCriteria a {@link java.lang.String} object.
	 * @param method          a {@link java.lang.String} object.
	 */
	public OtherInstructionSlicer(DirectedGraph g, String slicingCriteria, String method) {
		super(g);
		this.emptySet = new ValueArraySparseSet();
		this.slicingCriteria = slicingCriteria;
		this.method = method;
		doAnalysis();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flowThrough(Object in, Object node, Object out) {
		FlowSet inSet = (FlowSet) in,
				outSet = (FlowSet) out;
		Unit currInstruction = (Unit) node;

		if (currInstruction.toString().contains(slicingCriteria)) {
			addCurrInstInOutSet(outSet, currInstruction);
			return;

		}

		if (!inSet.isEmpty()) {
			for (Object anInSet : inSet.toList()) {

				UnitContainer insetInstruction = (UnitContainer) anInSet;
				List<ValueBox> useBoxes = insetInstruction.getUnit().getUseBoxes();

				outSet.union(inSet);

				for (ValueBox usebox : useBoxes) {

					if (isSpecialInvokeOn(currInstruction, usebox)) {
						addCurrInstInOutSet(outSet, currInstruction);
						return;
					}

					List<PropertyAnalysisResult> propertyAnalysisResults = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString());
					if (propertyAnalysisResults != null) {
						for (PropertyAnalysisResult propertyAnalysisResult : propertyAnalysisResults) {
							for (UnitContainer unit : propertyAnalysisResult.getSlicingResult()) {
								outSet.add(unit);
							}
						}
					}

					for (ValueBox defbox : currInstruction.getDefBoxes()) {
						if (defbox.getValue().equivTo(usebox.getValue())) {
							addCurrInstInOutSet(outSet, currInstruction);
							return;
						}
						else if (defbox.getValue().toString().contains(usebox.getValue().toString())) {
							if (usebox.getValue().getType() instanceof ArrayType) {
								addCurrInstInOutSet(outSet, currInstruction);
								return;
							}
						}
					}
				}
			}
		}
	}

	private void addCurrInstInOutSet(FlowSet outSet, Unit currInstruction) {
		UnitContainer currUnitContainer = new UnitContainer();
		currUnitContainer.setUnit(currInstruction);
		currUnitContainer.setMethod(method);

		outSet.add(currUnitContainer);
	}

	private boolean isSpecialInvokeOn(Unit currInstruction, ValueBox usebox) {
		return currInstruction.toString().contains("specialinvoke")
				&& currInstruction.toString().contains(usebox.getValue().toString() + ".<");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object newInitialFlow() {
		return emptySet.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object entryInitialFlow() {
		return emptySet.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void merge(Object in1, Object in2, Object out) {
		FlowSet inSet1 = (FlowSet) in1,
				inSet2 = (FlowSet) in2,
				outSet = (FlowSet) out;

		inSet1.union(inSet2, outSet);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copy(Object source, Object dest) {
		FlowSet srcSet = (FlowSet) source,
				destSet = (FlowSet) dest;
		srcSet.copy(destSet);
	}
}

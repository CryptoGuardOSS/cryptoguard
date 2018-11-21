package main.slicer.backward.property;

import main.analyzer.backward.PropertyFakeUnitContainer;
import main.analyzer.backward.UnitContainer;
import main.slicer.ValueArraySparseSet;
import main.util.FieldInitializationInstructionMap;
import soot.ArrayType;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.baf.internal.BafLocal;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>PropertyInstructionSlicer class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public class PropertyInstructionSlicer extends BackwardFlowAnalysis {

	private FlowSet emptySet;
	private String slicingCriteria;
	private String initMethod;
	private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

	/**
	 * <p>Constructor for PropertyInstructionSlicer.</p>
	 *
	 * @param g               a {@link soot.toolkits.graph.DirectedGraph} object.
	 * @param slicingCriteria a {@link java.lang.String} object.
	 * @param initMethod      a {@link java.lang.String} object.
	 */
	public PropertyInstructionSlicer(DirectedGraph g, String slicingCriteria, String initMethod) {
		super(g);
		this.emptySet = new ValueArraySparseSet();
		this.slicingCriteria = slicingCriteria;
		this.initMethod = initMethod;
		this.propertyUseMap = new HashMap<>();
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
			if (currInstruction.getDefBoxes().size() == 1) {
				ValueBox defBox = currInstruction.getDefBoxes().get(0);
				if (defBox.getValue().toString().contains(slicingCriteria)) {
					if (currInstruction.getUseBoxes().size() == 1) {
						Value localValue = new BafLocal("$fakeLocal_" + defBox.getValue().toString(), currInstruction.getUseBoxes().get(0).getValue().getType());
						AssignStmt assignStmt = Jimple.v().newAssignStmt(localValue, currInstruction.getUseBoxes().get(0).getValue());
						addFakeInstInOutSet(outSet, assignStmt, defBox.getValue().toString());
						return;
					}
					else if (currInstruction.getUseBoxes().size() > 1) {

						if (currInstruction.toString().contains("invoke ")) {
							for (ValueBox useBox : currInstruction.getUseBoxes()) {
								if (useBox.getValue().toString().contains("invoke ")) {
									Value localValue = new BafLocal("$fakeLocal_" + defBox.getValue().toString(), useBox.getValue().getType());
									AssignStmt assignStmt = Jimple.v().newAssignStmt(localValue, useBox.getValue());
									addFakeInstInOutSet(outSet, assignStmt, defBox.getValue().toString());
									return;
								}
							}
						}
						else if (currInstruction.getUseBoxes().size() == 2) {
							Value localValue = new BafLocal("$fakeLocal_" + defBox.getValue().toString(), currInstruction.getUseBoxes().get(1).getValue().getType());
							AssignStmt assignStmt = Jimple.v().newAssignStmt(localValue, currInstruction.getUseBoxes().get(1).getValue());
							addFakeInstInOutSet(outSet, assignStmt, defBox.getValue().toString());
							return;
						}
					}
				}
			}
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

					List<PropertyAnalysisResult> specialInitInsts;

					if (usebox.getValue().toString().startsWith("r0.")) {
						specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(3));
					}
					else if (usebox.getValue().toString().startsWith("this.")) {
						specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(5));
					}
					else {
						specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString());
					}

					if (specialInitInsts != null) {
						propertyUseMap.put(usebox.getValue().toString(), specialInitInsts);
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
		currUnitContainer.setMethod(initMethod);

		outSet.add(currUnitContainer);
	}


	private void addFakeInstInOutSet(FlowSet outSet, Unit fake, String original) {

		if (original.startsWith("r0.")) {
			original = original.substring(3);
		}
		else if (original.startsWith("this.")) {
			original = original.substring(5);
		}

		PropertyFakeUnitContainer currUnitContainer = new PropertyFakeUnitContainer();
		currUnitContainer.setUnit(fake);
		currUnitContainer.setMethod(initMethod);
		currUnitContainer.setOriginalProperty(original);

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

	/**
	 * <p>Getter for the field <code>propertyUseMap</code>.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, List<PropertyAnalysisResult>> getPropertyUseMap() {
		return propertyUseMap;
	}
}

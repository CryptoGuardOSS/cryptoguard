package main.slicer.backward.other;

import main.analyzer.backward.UnitContainer;
import main.slicer.ValueArraySparseSet;
import main.slicer.backward.property.PropertyAnalysisResult;
import main.util.FieldInitializationInstructionMap;
import soot.ArrayType;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherInstructionSlicer extends BackwardFlowAnalysis {

    private FlowSet emptySet;
    private String slicingCriteria;
    private String method;
    private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

    public OtherInstructionSlicer(DirectedGraph g, String slicingCriteria, String method) {
        super(g);
        this.emptySet = new ValueArraySparseSet();
        this.slicingCriteria = slicingCriteria;
        this.method = method;
        this.propertyUseMap = new HashMap<>();
        doAnalysis();
    }

    @Override
    protected void flowThrough(Object in, Object node, Object out) {
        FlowSet inSet = (FlowSet) in,
                outSet = (FlowSet) out;
        Unit currInstruction = (Unit) node;

        if (currInstruction.toString().startsWith(slicingCriteria)) {
            addCurrInstInOutSet(outSet, currInstruction);
            return;
        }

        if (!inSet.isEmpty()) {
            for (Object anInSet : inSet.toList()) {

                UnitContainer insetInstruction = (UnitContainer) anInSet;
                List<ValueBox> useBoxes = insetInstruction.getUnit().getUseBoxes();

                outSet.union(inSet);

                for (ValueBox usebox : useBoxes) {

                    if ((usebox.getValue().toString().equals("r0") && insetInstruction.getUnit().toString().contains("r0.")) ||
                            (usebox.getValue().toString().equals("this") && insetInstruction.getUnit().toString().contains("this."))) {
                        continue;
                    }

                    if (isInvokeOn(currInstruction, usebox)) {
                        addCurrInstInOutSet(outSet, currInstruction);
                        return;
                    }


                    for (ValueBox defbox : currInstruction.getDefBoxes()) {

                        if ((defbox.getValue().toString().equals("r0") && currInstruction.toString().startsWith("r0.")) ||
                                (defbox.getValue().toString().equals("this") && currInstruction.toString().startsWith("this."))) {
                            continue;
                        }

                        if (defbox.getValue().equivTo(usebox.getValue())) {
                            addCurrInstInOutSet(outSet, currInstruction);
                            return;
                        } else if (defbox.getValue().toString().contains(usebox.getValue().toString())) {
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

        for (ValueBox usebox : currInstruction.getUseBoxes()) {
            if (propertyUseMap.get(usebox.getValue().toString()) == null) {

                List<PropertyAnalysisResult> specialInitInsts;
                if (usebox.getValue().toString().matches("r[0-9]+\\.<[^\\>]+>")) {
                    specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(3));
                } else if (usebox.getValue().toString().startsWith("this.")) {
                    specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(5));
                } else {
                    specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString());
                }

                if (specialInitInsts != null) {
                    propertyUseMap.put(usebox.getValue().toString(), specialInitInsts);
                }
            }
        }

        UnitContainer currUnitContainer = new UnitContainer();
        currUnitContainer.setUnit(currInstruction);
        currUnitContainer.setMethod(method);

        outSet.add(currUnitContainer);
    }

    private boolean isInvokeOn(Unit currInstruction, ValueBox usebox) {
        return currInstruction instanceof JInvokeStmt
                && currInstruction.toString().contains(usebox.getValue().toString() + ".<");
    }

    @Override
    protected Object newInitialFlow() {
        return emptySet.clone();
    }

    @Override
    protected Object entryInitialFlow() {
        return emptySet.clone();
    }

    @Override
    protected void merge(Object in1, Object in2, Object out) {
        FlowSet inSet1 = (FlowSet) in1,
                inSet2 = (FlowSet) in2,
                outSet = (FlowSet) out;

        inSet1.union(inSet2, outSet);
    }

    @Override
    protected void copy(Object source, Object dest) {
        FlowSet srcSet = (FlowSet) source,
                destSet = (FlowSet) dest;
        srcSet.copy(destSet);
    }

    public Map<String, List<PropertyAnalysisResult>> getPropertyUseMap() {
        return propertyUseMap;
    }
}

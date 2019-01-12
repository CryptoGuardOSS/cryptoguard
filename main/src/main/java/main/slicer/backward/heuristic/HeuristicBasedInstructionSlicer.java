package main.slicer.backward.heuristic;

import main.analyzer.backward.UnitContainer;
import main.slicer.ValueArraySparseSet;
import main.slicer.backward.property.PropertyAnalysisResult;
import main.util.FieldInitializationInstructionMap;
import soot.ArrayType;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeuristicBasedInstructionSlicer extends BackwardFlowAnalysis {

    private FlowSet emptySet;
    private String slicingCriteria;
    private String method;
    private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

    public HeuristicBasedInstructionSlicer(DirectedGraph g, String slicingCriteria, String method) {
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

            outSet.union(inSet);

            if (currInstruction.toString().startsWith("if ")) {
                return;
            }



            for (Object anInSet : inSet.toList()) {

                UnitContainer insetInstruction = (UnitContainer) anInSet;
                List<ValueBox> useBoxes = insetInstruction.getUnit().getUseBoxes();

                for (ValueBox usebox : useBoxes) {

                    if ((usebox.getValue().toString().equals("r0") && insetInstruction.getUnit().toString().contains("r0.")) ||
                            (usebox.getValue().toString().equals("this") && insetInstruction.getUnit().toString().contains("this."))) {
                        continue;
                    }

                    if (isArgOfAssignInvoke(usebox, insetInstruction.getUnit())) {
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

    private static final List<String> WHITE_LISTED_METHODS = new ArrayList<>();
    private static final List<String> BLACK_LISTED_METHODS = new ArrayList<>();

    static {
        WHITE_LISTED_METHODS.add("<javax.xml.bind.DatatypeConverterInterface: byte[] parseBase64Binary(java.lang.String)>");
        WHITE_LISTED_METHODS.add("<javax.xml.bind.DatatypeConverterInterface: byte[] parseHexBinary(java.lang.String)>");

        BLACK_LISTED_METHODS.add("<javax.crypto.KeyGenerator: void <init>");
        BLACK_LISTED_METHODS.add("<javax.crypto.Cipher: void <init>");
    }

    private boolean isArgOfAssignInvoke(ValueBox useBox, Unit unit) {

        for (String blacklisted : BLACK_LISTED_METHODS) {
            if (unit instanceof JInvokeStmt && unit.toString().contains(blacklisted)) {
                return true;
            }
        }

        if (unit instanceof JAssignStmt && unit.toString().contains("invoke ")) {

            for (String whitelisted : WHITE_LISTED_METHODS) {
                if (unit.toString().contains(whitelisted)) {
                    return false;
                }
            }

            InvokeExpr invokeExpr = ((JAssignStmt) unit).getInvokeExpr();
            List<Value> args = invokeExpr.getArgs();
            for (Value arg : args) {
                if (arg.equivTo(useBox.getValue())) {
                    return true;
                }
            }

        }

        if (unit.toString().contains(" newarray ")) {
            for (ValueBox valueBox : unit.getUseBoxes()) {
                if (valueBox.getValue().equivTo(useBox.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void addCurrInstInOutSet(FlowSet outSet, Unit currInstruction) {

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

package main.slicer.backward.orthogonal;

import main.analyzer.backward.AssignInvokeUnitContainer;
import main.analyzer.backward.InvokeUnitContainer;
import main.analyzer.backward.ParamFakeUnitContainer;
import main.analyzer.backward.UnitContainer;
import main.slicer.ValueArraySparseSet;
import main.slicer.backward.MethodCallSiteInfo;
import main.slicer.backward.property.PropertyAnalysisResult;
import main.util.FieldInitializationInstructionMap;
import main.util.Utils;
import soot.ArrayType;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.baf.internal.BafLocal;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.*;

public class OrthogonalInstructionSlicer extends BackwardFlowAnalysis {

    private FlowSet emptySet;
    private String slicingCriteria;
    private String method;
    private List<String> usedFields;
    private Map<String, List<PropertyAnalysisResult>> propertyUseMap;
    private int depth;

    public OrthogonalInstructionSlicer(DirectedGraph g, String slicingCriteria, String method, int depth) {
        super(g);
        this.emptySet = new ValueArraySparseSet();
        this.slicingCriteria = slicingCriteria;
        this.method = method;
        this.depth = depth;
        usedFields = new ArrayList<>();
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

                    if (insetInstruction instanceof AssignInvokeUnitContainer) {

                        int arg = Utils.isArgOfAssignInvoke(usebox, insetInstruction.getUnit());

                        if (arg > -1) {
                            String args = ((AssignInvokeUnitContainer) insetInstruction).getArgs().toString();
                            if (!args.contains("" + arg)) {
                                continue;
                            }
                        }
                    }

                    if (insetInstruction instanceof InvokeUnitContainer) {

                        int arg = Utils.isArgOfInvoke(usebox, insetInstruction.getUnit());
                        if (arg > -1) {
                            String args = ((InvokeUnitContainer) insetInstruction).getArgs().toString();

                            if (!args.contains("" + arg)) {
                                continue;
                            }
                        }
                    }

                    if (Utils.isArgOfByteArrayCreation(usebox, insetInstruction.getUnit())) {
                        continue;
                    }

                    if (insetInstruction.getUnit().toString().contains("[" + usebox.getValue() + "]")) {
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

    private static final List<String> ASSIGN_WHITE_LISTED_METHODS = new ArrayList<>();
    private static final List<String> INVOKE_WHITE_LISTED_METHODS = new ArrayList<>();
    private static final List<String> BLACK_LISTED_METHODS = new ArrayList<>();

    static {
        ASSIGN_WHITE_LISTED_METHODS.add("<javax.xml.bind.DatatypeConverterInterface: byte[] parseBase64Binary(java.lang.String)>");
        ASSIGN_WHITE_LISTED_METHODS.add("<javax.xml.bind.DatatypeConverterInterface: byte[] parseHexBinary(java.lang.String)>");
        ASSIGN_WHITE_LISTED_METHODS.add("<java.util.Arrays: byte[] copyOf(byte[],int)>");


        INVOKE_WHITE_LISTED_METHODS.add("<java.lang.System: void arraycopy(java.lang.Object,int,java.lang.Object,int,int)>");
        INVOKE_WHITE_LISTED_METHODS.add("<java.lang.String: void <init>");

        BLACK_LISTED_METHODS.add("<javax.crypto.KeyGenerator: void <init>");
        BLACK_LISTED_METHODS.add("<javax.crypto.Cipher: void <init>");
    }

    private void addCurrInstInOutSet(FlowSet outSet, Unit currInstruction) {

        for (String blacklisted : BLACK_LISTED_METHODS) {
            if (currInstruction instanceof JInvokeStmt && currInstruction.toString().contains(blacklisted)) {
                return;
            }
        }

        UnitContainer currUnitContainer;

        for (ValueBox usebox : currInstruction.getUseBoxes()) {
            if (propertyUseMap.get(usebox.getValue().toString()) == null) {

                List<PropertyAnalysisResult> specialInitInsts = null;

                if (usebox.getValue().toString().matches("r[0-9]+\\.<[^\\>]+>")) {
                    specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(3));
                } else if (usebox.getValue().toString().startsWith("this.")) {
                    specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(5));
                } else if (usebox.getValue().toString().startsWith("<")) {
                    specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString());
                }

                if (specialInitInsts != null) {
                    propertyUseMap.put(usebox.getValue().toString(), specialInitInsts);
                }
            }
        }

        if (currInstruction instanceof JAssignStmt && currInstruction.toString().contains("invoke ")) {

            for (String whitelisted : ASSIGN_WHITE_LISTED_METHODS) {
                if (currInstruction.toString().contains(whitelisted)) {

                    currUnitContainer = new UnitContainer();
                    currUnitContainer.setUnit(currInstruction);
                    currUnitContainer.setMethod(method);
                    outSet.add(currUnitContainer);
                    return;
                }
            }

            currUnitContainer = Utils.createAssignInvokeUnitContainer(currInstruction, method, depth);
            if (currUnitContainer instanceof AssignInvokeUnitContainer) {
                Set<String> usedProperties = ((AssignInvokeUnitContainer) currUnitContainer).getProperties();
                usedFields.addAll(usedProperties);
            }
        } else if (currInstruction instanceof JInvokeStmt) {
            currUnitContainer = Utils.createInvokeUnitContainer(currInstruction, method, usedFields, depth);
        } else {
            currUnitContainer = new UnitContainer();
        }

        currUnitContainer.setUnit(currInstruction);
        currUnitContainer.setMethod(method);

        outSet.add(currUnitContainer);
    }

    private boolean isInvokeOn(Unit currInstruction, ValueBox usebox) {
        boolean isInvoke = currInstruction instanceof JInvokeStmt
                && currInstruction.toString().contains(usebox.getValue().toString() + ".<");

        for (String whitelisted : INVOKE_WHITE_LISTED_METHODS) {
            if (currInstruction.toString().contains(whitelisted) &&
                    currInstruction.toString().contains(", " + usebox.getValue().toString() + ",")) {
                isInvoke = true;
                break;
            }
        }

        return isInvoke;
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
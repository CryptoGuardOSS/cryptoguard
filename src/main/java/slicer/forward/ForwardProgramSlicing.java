package slicer.forward;

import slicer.ValueArraySparseSet;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.baf.internal.BafLocal;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.List;

/**
 * <p>ForwardProgramSlicing class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class ForwardProgramSlicing extends ForwardFlowAnalysis {

    private FlowSet emptySet;
    private MethodCallSiteInfo methodCallSiteInfo;

    /**
     * <p>Constructor for ForwardProgramSlicing.</p>
     *
     * @param g               a {@link soot.toolkits.graph.DirectedGraph} object.
     * @param slicingCriteria a {@link slicer.forward.SlicingCriteria} object.
     */
    public ForwardProgramSlicing(DirectedGraph g,
                                 SlicingCriteria slicingCriteria) {
        super(g);
        this.emptySet = new ValueArraySparseSet();
        this.methodCallSiteInfo = new MethodCallSiteInfo();
        this.methodCallSiteInfo.setSlicingCriteria(slicingCriteria);
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

        SlicingCriteria slicingCriteria = methodCallSiteInfo.getSlicingCriteria();

        if (currInstruction.toString().contains(slicingCriteria.getMethodName()) &&
                (currInstruction instanceof JAssignStmt || currInstruction instanceof JInvokeStmt)) {

            methodCallSiteInfo.setColumnNumber(currInstruction.getJavaSourceStartColumnNumber());
            methodCallSiteInfo.setLineNumber(currInstruction.getJavaSourceStartLineNumber());

            Value valueToAssign = currInstruction.getDefBoxes().get(0).getValue();
            Value localValue = new BafLocal("$fakeLocal", valueToAssign.getType());
            AssignStmt assignStmt = Jimple.v().newAssignStmt(valueToAssign, localValue);
            outSet.add(assignStmt);

            return;
        }

        if (!inSet.isEmpty()) {
            outSet.union(inSet);

            for (Object anInSet : inSet.toList()) {


                Unit insetInstruction = (Unit) anInSet;
                List<ValueBox> defBoxes = insetInstruction.getDefBoxes();
                for (ValueBox defbox : defBoxes) {
                    for (ValueBox usebox : currInstruction.getUseBoxes()) {
                        if (defbox.getValue().equivTo(usebox.getValue())) {
                            outSet.add(currInstruction);
                            return;
                        }
                    }
                }
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    protected Object newInitialFlow() {
        return emptySet.clone();
    }

    /** {@inheritDoc} */
    @Override
    protected Object entryInitialFlow() {
        return emptySet.clone();
    }

    /** {@inheritDoc} */
    @Override
    protected void merge(Object in1, Object in2, Object out) {
        FlowSet inSet1 = (FlowSet) in1,
                inSet2 = (FlowSet) in2,
                outSet = (FlowSet) out;

        inSet1.union(inSet2, outSet);
    }

    /** {@inheritDoc} */
    @Override
    protected void copy(Object source, Object dest) {
        FlowSet srcSet = (FlowSet) source,
                destSet = (FlowSet) dest;
        srcSet.copy(destSet);
    }

    /**
     * <p>Getter for the field <code>methodCallSiteInfo</code>.</p>
     *
     * @return a {@link slicer.forward.MethodCallSiteInfo} object.
     */
    public MethodCallSiteInfo getMethodCallSiteInfo() {
        return methodCallSiteInfo;
    }

}

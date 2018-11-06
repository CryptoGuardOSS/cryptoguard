package main.slicer.backward.method;

import main.analyzer.backward.ParamFakeUnitContainer;
import main.analyzer.backward.UnitContainer;
import main.slicer.ValueArraySparseSet;
import main.slicer.backward.MethodCallSiteInfo;
import main.slicer.backward.property.PropertyAnalysisResult;
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

public class MethodInstructionSlicer extends BackwardFlowAnalysis
{

	private FlowSet emptySet;
	private MethodCallSiteInfo methodCallSiteInfo;
	private List<Integer> slicingParams;
	private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

	public MethodInstructionSlicer(DirectedGraph g,
								   MethodCallSiteInfo methodCallSiteInfo, List<Integer> slicingParams)
	{
		super(g);
		this.emptySet = new ValueArraySparseSet();
		this.methodCallSiteInfo = methodCallSiteInfo;
		this.slicingParams = slicingParams;
		this.propertyUseMap = new HashMap<>();
		doAnalysis();
	}

	@Override
	protected void flowThrough(Object in, Object node, Object out)
	{
		FlowSet inSet = (FlowSet) in,
				outSet = (FlowSet) out;
		Unit currInstruction = (Unit) node;

		if (currInstruction.toString().contains(methodCallSiteInfo.getCallee().toString()) &&
				currInstruction.getJavaSourceStartLineNumber() == methodCallSiteInfo.getLineNumber())
		{

			for (Integer slicingParam : slicingParams)
			{

				/*
				 *  Replace the instruction with new instruction(s) to precisely track the provided parameters.
				 *  Watch-out for invoke statements.
				 */
				Value valueToAssign = null;

				for (ValueBox usebox : currInstruction.getUseBoxes())
				{

					if (usebox.getValue().toString().contains("invoke ") &&
							usebox.getValue().getUseBoxes().size() > slicingParam)
					{
						valueToAssign = usebox.getValue().getUseBoxes().get(slicingParam).getValue();
						break;
					}
				}

				if (valueToAssign != null)
				{

					Value localValue = new BafLocal("$fakeLocal_" + methodCallSiteInfo.getLineNumber() + slicingParams, valueToAssign.getType());
					AssignStmt assignStmt = Jimple.v().newAssignStmt(localValue, valueToAssign);

					ParamFakeUnitContainer container = new ParamFakeUnitContainer();

					container.setUnit(assignStmt);
					container.setParam(slicingParam);
					container.setCallee(methodCallSiteInfo.getCallee().toString());
					container.setMethod(methodCallSiteInfo.getCaller().toString());

					outSet.add(container);
				}
			}

			return;
		}

		if (!inSet.isEmpty())
		{
			for (Object anInSet : inSet.toList())
			{

				UnitContainer insetInstruction = (UnitContainer) anInSet;
				List<ValueBox> useBoxes = insetInstruction.getUnit().getUseBoxes();

				outSet.union(inSet);

				for (ValueBox usebox : useBoxes)
				{

					if (isSpecialInvokeOn(currInstruction, usebox))
					{
						addCurrInstInOutSet(outSet, currInstruction);
						return;
					}

					List<PropertyAnalysisResult> specialInitInsts;

					if (usebox.getValue().toString().startsWith("r0."))
					{
						specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(3));
					}
					else if (usebox.getValue().toString().startsWith("this."))
					{
						specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString().substring(5));
					}
					else
					{
						specialInitInsts = FieldInitializationInstructionMap.getInitInstructions(usebox.getValue().toString());
					}

					if (specialInitInsts != null)
					{
						propertyUseMap.put(usebox.getValue().toString(), specialInitInsts);
					}

					for (ValueBox defbox : currInstruction.getDefBoxes())
					{

						if (defbox.getValue().equivTo(usebox.getValue()))
						{
							addCurrInstInOutSet(outSet, currInstruction);
							return;
						}
						else if (defbox.getValue().toString().contains(usebox.getValue().toString()))
						{
							if (usebox.getValue().getType() instanceof ArrayType)
							{
								addCurrInstInOutSet(outSet, currInstruction);
								return;
							}
						}
					}
				}
			}
		}

	}

	private void addCurrInstInOutSet(FlowSet outSet, Unit currInstruction)
	{
		UnitContainer currUnitContainer = new UnitContainer();
		currUnitContainer.setUnit(currInstruction);
		currUnitContainer.setMethod(methodCallSiteInfo.getCaller().toString());

		outSet.add(currUnitContainer);
	}

	private boolean isSpecialInvokeOn(Unit currInstruction, ValueBox usebox)
	{
		return currInstruction.toString().contains("specialinvoke")
				&& currInstruction.toString().contains(usebox.getValue().toString() + ".<");
	}

	@Override
	protected Object newInitialFlow()
	{
		return emptySet.clone();
	}

	@Override
	protected Object entryInitialFlow()
	{
		return emptySet.clone();
	}

	@Override
	protected void merge(Object in1, Object in2, Object out)
	{
		FlowSet inSet1 = (FlowSet) in1,
				inSet2 = (FlowSet) in2,
				outSet = (FlowSet) out;

		inSet1.union(inSet2, outSet);
	}

	@Override
	protected void copy(Object source, Object dest)
	{
		FlowSet srcSet = (FlowSet) source,
				destSet = (FlowSet) dest;
		srcSet.copy(destSet);
	}

	public MethodCallSiteInfo getMethodCallSiteInfo()
	{
		return methodCallSiteInfo;
	}

	public Map<String, List<PropertyAnalysisResult>> getPropertyUseMap()
	{
		return propertyUseMap;
	}
}
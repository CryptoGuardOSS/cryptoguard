package main.slicer.backward.other;

import main.analyzer.backward.UnitContainer;
import soot.SootMethod;

import java.util.List;

public class OtherAnalysisResult
{

	private String instruction;
	private SootMethod method;
	private List<UnitContainer> analysis;

	public OtherAnalysisResult(String instruction, SootMethod method, List<UnitContainer> analysis)
	{
		this.instruction = instruction;
		this.method = method;
		this.analysis = analysis;
	}

	public String getInstruction()
	{
		return instruction;
	}

	public SootMethod getMethod()
	{
		return method;
	}

	public List<UnitContainer> getAnalysis()
	{
		return analysis;
	}
}

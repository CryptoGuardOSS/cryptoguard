package main.slicer.backward.heuristic;

import main.analyzer.backward.UnitContainer;
import main.slicer.backward.property.PropertyAnalysisResult;
import soot.SootMethod;

import java.util.List;
import java.util.Map;

public class HeuristicBasedAnalysisResult {

    private String instruction;
    private SootMethod method;
    private List<UnitContainer> analysis;
    private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

    public HeuristicBasedAnalysisResult(String instruction,
                                        SootMethod method,
                                        List<UnitContainer> analysis,
                                        Map<String, List<PropertyAnalysisResult>> propertyUseMap) {
        this.instruction = instruction;
        this.method = method;
        this.analysis = analysis;
        this.propertyUseMap = propertyUseMap;
    }

    public String getInstruction() {
        return instruction;
    }

    public SootMethod getMethod() {
        return method;
    }

    public List<UnitContainer> getAnalysis() {
        return analysis;
    }

    public Map<String, List<PropertyAnalysisResult>> getPropertyUseMap() {
        return propertyUseMap;
    }
}

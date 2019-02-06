package main.analyzer.backward;

import java.util.*;

public class InvokeUnitContainer extends UnitContainer {

    private List<Integer> args = new ArrayList<>();
    private Set<String> definedFields = new HashSet<>();
    private List<UnitContainer> analysisResult = new ArrayList<>();

    public List<Integer> getArgs() {
        return args;
    }

    public void setArgs(List<Integer> args) {
        this.args = args;
    }

    public Set<String> getDefinedFields() {
        return definedFields;
    }

    public void setDefinedFields(Set<String> definedFields) {
        this.definedFields = definedFields;
    }

    public List<UnitContainer> getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(List<UnitContainer> analysisResult) {
        this.analysisResult = analysisResult;
    }
}

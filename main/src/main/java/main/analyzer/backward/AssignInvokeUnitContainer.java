package main.analyzer.backward;

import java.util.*;

public class AssignInvokeUnitContainer extends UnitContainer {
    private List<Integer> args = new ArrayList<>();
    private Set<String> properties = new HashSet<>();
    private List<UnitContainer> analysisResult = new ArrayList<>();

    public List<Integer> getArgs() {
        return args;
    }

    public void setArgs(List<Integer> args) {
        this.args = args;
    }

    public Set<String> getProperties() {
        return properties;
    }

    public void setProperties(Set<String> properties) {
        this.properties = properties;
    }

    public List<UnitContainer> getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(List<UnitContainer> analysisResult) {
        this.analysisResult = analysisResult;
    }

}

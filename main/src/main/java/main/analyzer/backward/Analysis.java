package main.analyzer.backward;

import java.util.List;

public class Analysis {
    private String methodChain;
    private List<UnitContainer> analysisResult;

    public String getMethodChain() {
        return methodChain;
    }

    public void setMethodChain(String methodChain) {
        this.methodChain = methodChain;
    }

    public List<UnitContainer> getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(List<UnitContainer> analysisResult) {
        this.analysisResult = analysisResult;
    }
}

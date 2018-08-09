package main.slicer.forward;

import soot.Unit;

import java.util.List;

/**
 * Created by krishnokoli on 10/14/16.
 */
public class SlicingResult {

    private MethodCallSiteInfo callSiteInfo;
    private List<Unit> analysisResult;

    public MethodCallSiteInfo getCallSiteInfo() {
        return callSiteInfo;
    }

    public void setCallSiteInfo(MethodCallSiteInfo callSiteInfo) {
        this.callSiteInfo = callSiteInfo;
    }

    public List<Unit> getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(List<Unit> analysisResult) {
        this.analysisResult = analysisResult;
    }
}

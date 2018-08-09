package main.analyzer.forward;

import main.slicer.forward.MethodCallSiteInfo;
import main.slicer.forward.SlicingResult;
import soot.SootMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by krishnokoli on 12/27/16.
 */
public class MethodWrapper {

    private SootMethod method;
    private List<MethodWrapper> calleeList;
    private Map<MethodCallSiteInfo, SlicingResult> analysisListMap;

    public MethodWrapper(SootMethod method) {
        this.method = method;
        this.calleeList = new ArrayList<>();
        this.analysisListMap = new HashMap<>();

    }

    public SootMethod getMethod() {
        return method;
    }

    public List<MethodWrapper> getCalleeList() {
        return calleeList;
    }

    public void setCalleeList(List<MethodWrapper> calleeList) {
        this.calleeList = calleeList;
    }

    public void setMethod(SootMethod method) {
        this.method = method;
    }

    public Map<MethodCallSiteInfo, SlicingResult> getAnalysisListMap() {
        return analysisListMap;
    }

    public void setAnalysisListMap(Map<MethodCallSiteInfo, SlicingResult> analysisListMap) {
        this.analysisListMap = analysisListMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodWrapper methodWrapper = (MethodWrapper) o;

        return method.toString().equals(methodWrapper.method.toString());

    }

    @Override
    public int hashCode() {
        return method.toString().hashCode();
    }

    @Override
    public String toString() {
        return method.toString();
    }
}

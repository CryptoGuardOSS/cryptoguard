package main.analyzer.backward;

import main.slicer.backward.MethodCallSiteInfo;
import main.slicer.backward.method.MethodSlicingResult;
import soot.SootMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by krishnokoli on 12/27/16.
 */
public class MethodWrapper {

    private boolean isTopLevel = true;
    private SootMethod method;
    private List<MethodCallSiteInfo> calleeList;
    private List<MethodWrapper> callerList;

    public MethodWrapper(SootMethod method) {
        this.method = method;
        this.calleeList = new ArrayList<>();
        this.callerList = new ArrayList<>();
    }

    public SootMethod getMethod() {
        return method;
    }

    public List<MethodCallSiteInfo> getCalleeList() {
        return calleeList;
    }

    public void setMethod(SootMethod method) {
        this.method = method;
    }

    public boolean isTopLevel() {
        return isTopLevel;
    }

    public void setTopLevel(boolean topLevel) {
        isTopLevel = topLevel;
    }

    public List<MethodWrapper> getCallerList() {
        return callerList;
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

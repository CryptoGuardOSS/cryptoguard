package main.slicer.backward;

import java.util.List;

public class SlicingCriteria {

    private MethodCallSiteInfo methodCallSiteInfo;
    private List<Integer> parameters;

    public SlicingCriteria(MethodCallSiteInfo methodCallSiteInfo, List<Integer> parameters) {
        this.methodCallSiteInfo = methodCallSiteInfo;
        this.parameters = parameters;
    }

    public MethodCallSiteInfo getMethodCallSiteInfo() {
        return methodCallSiteInfo;
    }

    public void setMethodCallSiteInfo(MethodCallSiteInfo methodCallSiteInfo) {
        this.methodCallSiteInfo = methodCallSiteInfo;
    }

    public List<Integer> getParameters() {
        return parameters;
    }

    public void setParameters(List<Integer> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlicingCriteria that = (SlicingCriteria) o;

        if (!methodCallSiteInfo.equals(that.methodCallSiteInfo)) return false;
        return parameters.toString().equals(that.parameters.toString());

    }

    @Override
    public int hashCode() {
        int result = methodCallSiteInfo.hashCode();
        result = 31 * result + parameters.toString().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SlicingCriteria{" +
                "methodCallSiteInfo=" + methodCallSiteInfo +
                ", parameters=" + parameters +
                '}';
    }
}

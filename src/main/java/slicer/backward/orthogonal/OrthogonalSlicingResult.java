package slicer.backward.orthogonal;

import analyzer.backward.UnitContainer;
import slicer.backward.MethodCallSiteInfo;
import slicer.backward.property.PropertyAnalysisResult;

import java.util.List;
import java.util.Map;

/**
 * Created by RigorityJTeam on 10/14/16.
 */
public class OrthogonalSlicingResult {

    private MethodCallSiteInfo callSiteInfo;
    private List<UnitContainer> analysisResult;
    private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

    public MethodCallSiteInfo getCallSiteInfo() {
        return callSiteInfo;
    }

    public void setCallSiteInfo(MethodCallSiteInfo callSiteInfo) {
        this.callSiteInfo = callSiteInfo;
    }

    public List<UnitContainer> getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(List<UnitContainer> analysisResult) {
        this.analysisResult = analysisResult;
    }

    public Map<String, List<PropertyAnalysisResult>> getPropertyUseMap() {
        return propertyUseMap;
    }

    public void setPropertyUseMap(Map<String, List<PropertyAnalysisResult>> propertyUseMap) {
        this.propertyUseMap = propertyUseMap;
    }
}

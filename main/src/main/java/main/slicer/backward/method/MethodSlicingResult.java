package main.slicer.backward.method;

import main.analyzer.backward.UnitContainer;
import main.slicer.backward.MethodCallSiteInfo;
import main.slicer.backward.property.PropertyAnalysisResult;

import java.util.List;
import java.util.Map;

/**
 * Created by krishnokoli on 10/14/16.
 */
public class MethodSlicingResult {

    private MethodCallSiteInfo callSiteInfo;
    private List<Integer> influencingParameters;
    private List<UnitContainer> analysisResult;
    private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

    public MethodCallSiteInfo getCallSiteInfo() {
        return callSiteInfo;
    }

    public void setCallSiteInfo(MethodCallSiteInfo callSiteInfo) {
        this.callSiteInfo = callSiteInfo;
    }

    public List<Integer> getInfluencingParameters() {
        return influencingParameters;
    }

    public void setInfluencingParameters(List<Integer> influencingParameters) {
        this.influencingParameters = influencingParameters;
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

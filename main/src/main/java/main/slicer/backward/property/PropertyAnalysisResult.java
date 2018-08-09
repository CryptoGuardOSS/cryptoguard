package main.slicer.backward.property;

import main.analyzer.backward.MethodWrapper;
import main.analyzer.backward.UnitContainer;

import java.util.List;

public class PropertyAnalysisResult {

    private MethodWrapper methodWrapper;
    private List<Integer> influencingParams;
    private List<UnitContainer> slicingResult;

    public MethodWrapper getMethodWrapper() {
        return methodWrapper;
    }

    public void setMethodWrapper(MethodWrapper methodWrapper) {
        this.methodWrapper = methodWrapper;
    }

    public List<Integer> getInfluencingParams() {
        return influencingParams;
    }

    public void setInfluencingParams(List<Integer> influencingParams) {
        this.influencingParams = influencingParams;
    }

    public List<UnitContainer> getSlicingResult() {
        return slicingResult;
    }

    public void setSlicingResult(List<UnitContainer> slicingResult) {
        this.slicingResult = slicingResult;
    }
}

package main.slicer.backward.property;

import main.analyzer.backward.MethodWrapper;
import main.analyzer.backward.UnitContainer;

import java.util.List;
import java.util.Map;

/**
 * <p>PropertyAnalysisResult class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class PropertyAnalysisResult {

    private MethodWrapper methodWrapper;
    private List<Integer> influencingParams;
    private List<UnitContainer> slicingResult;
    private Map<String, List<PropertyAnalysisResult>> propertyUseMap;

    /**
     * <p>Getter for the field <code>methodWrapper</code>.</p>
     *
     * @return a {@link main.analyzer.backward.MethodWrapper} object.
     */
    public MethodWrapper getMethodWrapper() {
        return methodWrapper;
    }

    /**
     * <p>Setter for the field <code>methodWrapper</code>.</p>
     *
     * @param methodWrapper a {@link main.analyzer.backward.MethodWrapper} object.
     */
    public void setMethodWrapper(MethodWrapper methodWrapper) {
        this.methodWrapper = methodWrapper;
    }

    /**
     * <p>Getter for the field <code>influencingParams</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getInfluencingParams() {
        return influencingParams;
    }

    /**
     * <p>Setter for the field <code>influencingParams</code>.</p>
     *
     * @param influencingParams a {@link java.util.List} object.
     */
    public void setInfluencingParams(List<Integer> influencingParams) {
        this.influencingParams = influencingParams;
    }

    /**
     * <p>Getter for the field <code>slicingResult</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<UnitContainer> getSlicingResult() {
        return slicingResult;
    }

    /**
     * <p>Setter for the field <code>slicingResult</code>.</p>
     *
     * @param slicingResult a {@link java.util.List} object.
     */
    public void setSlicingResult(List<UnitContainer> slicingResult) {
        this.slicingResult = slicingResult;
    }

    /**
     * <p>Getter for the field <code>propertyUseMap</code>.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, List<PropertyAnalysisResult>> getPropertyUseMap() {
        return propertyUseMap;
    }

    /**
     * <p>Setter for the field <code>propertyUseMap</code>.</p>
     *
     * @param propertyUseMap a {@link java.util.Map} object.
     */
    public void setPropertyUseMap(Map<String, List<PropertyAnalysisResult>> propertyUseMap) {
        this.propertyUseMap = propertyUseMap;
    }
}

package slicer.forward;

import soot.Unit;

import java.util.List;

/**
 * Created by krishnokoli on 10/14/16.
 *
 * @author krishnokoli
 * @version $Id: $Id
 * @since V01.00.00
 */
public class SlicingResult {

    private MethodCallSiteInfo callSiteInfo;
    private List<Unit> analysisResult;

    /**
     * <p>Getter for the field <code>callSiteInfo</code>.</p>
     *
     * @return a {@link MethodCallSiteInfo} object.
     */
    public MethodCallSiteInfo getCallSiteInfo() {
        return callSiteInfo;
    }

    /**
     * <p>Setter for the field <code>callSiteInfo</code>.</p>
     *
     * @param callSiteInfo a {@link MethodCallSiteInfo} object.
     */
    public void setCallSiteInfo(MethodCallSiteInfo callSiteInfo) {
        this.callSiteInfo = callSiteInfo;
    }

    /**
     * <p>Getter for the field <code>analysisResult</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Unit> getAnalysisResult() {
        return analysisResult;
    }

    /**
     * <p>Setter for the field <code>analysisResult</code>.</p>
     *
     * @param analysisResult a {@link java.util.List} object.
     */
    public void setAnalysisResult(List<Unit> analysisResult) {
        this.analysisResult = analysisResult;
    }
}

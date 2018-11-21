package main.analyzer.backward;

import java.util.List;

/**
 * <p>Analysis class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00.00
 */
public class Analysis {
	private String methodChain;
	private List<UnitContainer> analysisResult;

	/**
	 * <p>Getter for the field <code>methodChain</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMethodChain() {
		return methodChain;
	}

	/**
	 * <p>Setter for the field <code>methodChain</code>.</p>
	 *
	 * @param methodChain a {@link java.lang.String} object.
	 */
	public void setMethodChain(String methodChain) {
		this.methodChain = methodChain;
	}

	/**
	 * <p>Getter for the field <code>analysisResult</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<UnitContainer> getAnalysisResult() {
		return analysisResult;
	}

	/**
	 * <p>Setter for the field <code>analysisResult</code>.</p>
	 *
	 * @param analysisResult a {@link java.util.List} object.
	 */
	public void setAnalysisResult(List<UnitContainer> analysisResult) {
		this.analysisResult = analysisResult;
	}
}

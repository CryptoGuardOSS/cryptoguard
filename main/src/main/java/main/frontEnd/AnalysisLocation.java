package main.frontEnd;

/**
 * <p>AnalysisLocation class.</p>
 *
 * @author franceme
 * @since V01.00.00
 */
public class AnalysisLocation {

	//region Attributes
	private Integer lineStart;
	private Integer lineEnd;
	private Integer methodNumber = -1;
	//endregion

	//region Constructor

	/**
	 * <p>Constructor for AnalysisLocation.</p>
	 *
	 * @param start a {@link java.lang.Integer} object.
	 * @param end   a {@link java.lang.Integer} object.
	 */
	public AnalysisLocation(Integer start, Integer end) {
		this.lineStart = start;
		this.lineEnd = end;
	}

	/**
	 * <p>Constructor for AnalysisLocation.</p>
	 *
	 * @param start        a {@link java.lang.Integer} object.
	 * @param end          a {@link java.lang.Integer} object.
	 * @param methodNumber a {@link java.lang.Integer} object.
	 */
	public AnalysisLocation(Integer start, Integer end, Integer methodNumber) {
		this.lineStart = start;
		this.lineEnd = end;
		this.methodNumber = methodNumber;
	}
	//endregion

	//region Overridden Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		output.append(this.lineStart);

		if (!this.lineEnd.equals(this.lineStart)) {
			output.append("-");
			output.append(this.lineEnd);
		}

		return output.toString();
	}
	//endregion

	//region Getters

	/**
	 * <p>Getter for the field <code>lineStart</code>.</p>
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getLineStart() {
		return lineStart;
	}

	/**
	 * <p>Getter for the field <code>lineEnd</code>.</p>
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getLineEnd() {
		return lineEnd;
	}

	/**
	 * <p>Getter for the field <code>methodNumber</code>.</p>
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getMethodNumber() {
		return methodNumber;
	}

	/**
	 * <p>Setter for the field <code>methodNumber</code>.</p>
	 *
	 * @param methodNumber a {@link java.lang.Integer} object.
	 */
	public void setMethodNumber(Integer methodNumber) {
		this.methodNumber = methodNumber;
	}
	//endregion

}

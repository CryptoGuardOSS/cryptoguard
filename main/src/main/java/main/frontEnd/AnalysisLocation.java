package main.frontEnd;

public class AnalysisLocation {

	//region Attributes
	private Integer lineStart;
	private Integer lineEnd;
	private Integer methodNumber = -1;
	//endregion

	//region Constructor
	public AnalysisLocation(Integer start, Integer end) {
		this.lineStart = start;
		this.lineEnd = end;
	}

	public AnalysisLocation(Integer start, Integer end, Integer methodNumber) {
		this.lineStart = start;
		this.lineEnd = end;
		this.methodNumber = methodNumber;
	}
	//endregion

	//region Overridden Methods
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
	public Integer getLineStart() {
		return lineStart;
	}

	public Integer getLineEnd() {
		return lineEnd;
	}

	public Integer getMethodNumber() {
		return methodNumber;
	}

	public void setMethodNumber(Integer methodNumber) {
		this.methodNumber = methodNumber;
	}
	//endregion

}

package main.slicer.forward;

/**
 * Created by krishnokoli on 7/3/17.
 */
public class MethodCallSiteInfo {

    private SlicingCriteria slicingCriteria;
    private int lineNumber;
    private int columnNumber;

    public MethodCallSiteInfo() {
    }

    public MethodCallSiteInfo(SlicingCriteria slicingCriteria, int lineNumber, int columnNumber) {
        this.slicingCriteria = slicingCriteria;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public SlicingCriteria getSlicingCriteria() {
        return slicingCriteria;
    }

    public void setSlicingCriteria(SlicingCriteria slicingCriteria) {
        this.slicingCriteria = slicingCriteria;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallSiteInfo that = (MethodCallSiteInfo) o;

        if (lineNumber != that.lineNumber) return false;
        if (columnNumber != that.columnNumber) return false;
        return slicingCriteria.equals(that.slicingCriteria);

    }

    @Override
    public int hashCode() {
        int result = slicingCriteria.hashCode();
        result = 31 * result + lineNumber;
        result = 31 * result + columnNumber;
        return result;
    }
}

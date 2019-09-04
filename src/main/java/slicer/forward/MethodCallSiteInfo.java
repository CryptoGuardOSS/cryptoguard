package slicer.forward;

/**
 * Created by RigorityJTeam on 7/3/17.
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class MethodCallSiteInfo {

    private SlicingCriteria slicingCriteria;
    private int lineNumber;
    private int columnNumber;

    /**
     * <p>Constructor for MethodCallSiteInfo.</p>
     */
    public MethodCallSiteInfo() {
    }

    /**
     * <p>Constructor for MethodCallSiteInfo.</p>
     *
     * @param slicingCriteria a {@link slicer.forward.SlicingCriteria} object.
     * @param lineNumber      a int.
     * @param columnNumber    a int.
     */
    public MethodCallSiteInfo(SlicingCriteria slicingCriteria, int lineNumber, int columnNumber) {
        this.slicingCriteria = slicingCriteria;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    /**
     * <p>Getter for the field <code>slicingCriteria</code>.</p>
     *
     * @return a {@link slicer.forward.SlicingCriteria} object.
     */
    public SlicingCriteria getSlicingCriteria() {
        return slicingCriteria;
    }

    /**
     * <p>Setter for the field <code>slicingCriteria</code>.</p>
     *
     * @param slicingCriteria a {@link slicer.forward.SlicingCriteria} object.
     */
    public void setSlicingCriteria(SlicingCriteria slicingCriteria) {
        this.slicingCriteria = slicingCriteria;
    }

    /**
     * <p>Getter for the field <code>lineNumber</code>.</p>
     *
     * @return a int.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * <p>Setter for the field <code>lineNumber</code>.</p>
     *
     * @param lineNumber a int.
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * <p>Getter for the field <code>columnNumber</code>.</p>
     *
     * @return a int.
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * <p>Setter for the field <code>columnNumber</code>.</p>
     *
     * @param columnNumber a int.
     */
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallSiteInfo that = (MethodCallSiteInfo) o;

        if (lineNumber != that.lineNumber) return false;
        if (columnNumber != that.columnNumber) return false;
        return slicingCriteria.equals(that.slicingCriteria);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = slicingCriteria.hashCode();
        result = 31 * result + lineNumber;
        result = 31 * result + columnNumber;
        return result;
    }
}

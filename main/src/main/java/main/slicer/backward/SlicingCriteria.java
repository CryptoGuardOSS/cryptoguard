package main.slicer.backward;

import java.util.List;

/**
 * <p>SlicingCriteria class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class SlicingCriteria {

    private MethodCallSiteInfo methodCallSiteInfo;
    private List<Integer> parameters;

    /**
     * <p>Constructor for SlicingCriteria.</p>
     *
     * @param methodCallSiteInfo a {@link main.slicer.backward.MethodCallSiteInfo} object.
     * @param parameters         a {@link java.util.List} object.
     */
    public SlicingCriteria(MethodCallSiteInfo methodCallSiteInfo, List<Integer> parameters) {
        this.methodCallSiteInfo = methodCallSiteInfo;
        this.parameters = parameters;
    }

    /**
     * <p>Getter for the field <code>methodCallSiteInfo</code>.</p>
     *
     * @return a {@link main.slicer.backward.MethodCallSiteInfo} object.
     */
    public MethodCallSiteInfo getMethodCallSiteInfo() {
        return methodCallSiteInfo;
    }

    /**
     * <p>Setter for the field <code>methodCallSiteInfo</code>.</p>
     *
     * @param methodCallSiteInfo a {@link main.slicer.backward.MethodCallSiteInfo} object.
     */
    public void setMethodCallSiteInfo(MethodCallSiteInfo methodCallSiteInfo) {
        this.methodCallSiteInfo = methodCallSiteInfo;
    }

    /**
     * <p>Getter for the field <code>parameters</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getParameters() {
        return parameters;
    }

    /**
     * <p>Setter for the field <code>parameters</code>.</p>
     *
     * @param parameters a {@link java.util.List} object.
     */
    public void setParameters(List<Integer> parameters) {
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlicingCriteria that = (SlicingCriteria) o;

        if (!methodCallSiteInfo.equals(that.methodCallSiteInfo)) return false;
        return parameters.toString().equals(that.parameters.toString());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = methodCallSiteInfo.hashCode();
        result = 31 * result + parameters.toString().hashCode();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "SlicingCriteria{" +
                "methodCallSiteInfo=" + methodCallSiteInfo +
                ", parameters=" + parameters +
                '}';
    }
}

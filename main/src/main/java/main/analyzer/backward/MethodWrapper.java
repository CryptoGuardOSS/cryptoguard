package main.analyzer.backward;

import main.slicer.backward.MethodCallSiteInfo;
import soot.SootMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krishnokoli on 12/27/16.
 *
 * @author krishnokoli
 * @version $Id: $Id
 * @since V01.00.00
 */
public class MethodWrapper {

    private boolean isTopLevel = true;
    private SootMethod method;
    private List<MethodCallSiteInfo> calleeList;
    private List<MethodWrapper> callerList;

    /**
     * <p>Constructor for MethodWrapper.</p>
     *
     * @param method a {@link soot.SootMethod} object.
     */
    public MethodWrapper(SootMethod method) {
        this.method = method;
        this.calleeList = new ArrayList<>();
        this.callerList = new ArrayList<>();
    }

    /**
     * <p>Getter for the field <code>method</code>.</p>
     *
     * @return a {@link soot.SootMethod} object.
     */
    public SootMethod getMethod() {
        return method;
    }

    /**
     * <p>Getter for the field <code>calleeList</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<MethodCallSiteInfo> getCalleeList() {
        return calleeList;
    }

    /**
     * <p>Setter for the field <code>method</code>.</p>
     *
     * @param method a {@link soot.SootMethod} object.
     */
    public void setMethod(SootMethod method) {
        this.method = method;
    }

    /**
     * <p>isTopLevel.</p>
     *
     * @return a boolean.
     */
    public boolean isTopLevel() {
        return isTopLevel;
    }

    /**
     * <p>setTopLevel.</p>
     *
     * @param topLevel a boolean.
     */
    public void setTopLevel(boolean topLevel) {
        isTopLevel = topLevel;
    }

    /**
     * <p>Getter for the field <code>callerList</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<MethodWrapper> getCallerList() {
        return callerList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodWrapper methodWrapper = (MethodWrapper) o;
        return method.toString().equals(methodWrapper.method.toString());

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return method.toString().hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return method.toString();
    }
}

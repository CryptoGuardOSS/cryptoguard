package main.slicer.backward;

import main.analyzer.backward.MethodWrapper;

/**
 * Created by krishnokoli on 7/3/17.
 */
public class MethodCallSiteInfo {

    private MethodWrapper callee;
    private MethodWrapper caller;
    private int lineNumber;
    private int columnNumber;

    public MethodCallSiteInfo(MethodWrapper caller, MethodWrapper callee, int lineNumber, int columnNumber) {
        this.caller = caller;
        this.callee = callee;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public MethodWrapper getCallee() {
        return callee;
    }

    public void setCallee(MethodWrapper callee) {
        this.callee = callee;
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

    public MethodWrapper getCaller() {
        return caller;
    }

    public void setCaller(MethodWrapper caller) {
        this.caller = caller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallSiteInfo that = (MethodCallSiteInfo) o;

        if (lineNumber != that.lineNumber) return false;
        if (columnNumber != that.columnNumber) return false;
        return callee.equals(that.callee) && caller.equals(that.caller);
    }

    @Override
    public int hashCode() {
        int result = callee.hashCode();
        result = 31 * result + caller.hashCode();
        result = 31 * result + lineNumber;
        result = 31 * result + columnNumber;
        return result;
    }

    @Override
    public String toString() {
        return "MethodCallSiteInfo{" +
                "callee=" + callee +
                ", caller=" + caller +
                ", lineNumber=" + lineNumber +
                ", columnNumber=" + columnNumber +
                '}';
    }
}

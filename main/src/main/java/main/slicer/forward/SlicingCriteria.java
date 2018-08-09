package main.slicer.forward;

import java.util.List;

/**
 * Created by krishnokoli on 7/3/17.
 */
public class SlicingCriteria {

    private String methodName;

    public SlicingCriteria(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlicingCriteria that = (SlicingCriteria) o;

        return methodName.equals(that.methodName);
    }

    @Override
    public int hashCode() {
        return methodName.hashCode();
    }
}

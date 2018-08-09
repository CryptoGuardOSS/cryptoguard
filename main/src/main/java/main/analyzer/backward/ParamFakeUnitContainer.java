package main.analyzer.backward;

public class ParamFakeUnitContainer extends UnitContainer {

    private int param;
    private String callee;

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public String getCallee() {
        return callee;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }
}

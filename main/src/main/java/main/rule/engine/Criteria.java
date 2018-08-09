package main.rule.engine;

public class Criteria {

    private String className;
    private String methodName;
    private int param;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }
}

package main.analyzer.backward;

import soot.Unit;

public class UnitContainer {

    private Unit unit;
    private String method;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitContainer that = (UnitContainer) o;

        return unit.toString().equals(that.unit.toString());
    }

    @Override
    public int hashCode() {
        return ("UnitContainer{" +
                "unit=" + unit +
                ", method='" + method + "\'" +
                "}").hashCode();
    }

    @Override
    public String toString() {
        return "UnitContainer{" +
                "unit=" + unit +
                ", method='" + method + '\'' +
                '}';
    }


}

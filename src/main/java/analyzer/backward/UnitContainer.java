package analyzer.backward;

import soot.Unit;

/**
 * <p>UnitContainer class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class UnitContainer {

    private Unit unit;
    private String method;

    /**
     * <p>Getter for the field <code>unit</code>.</p>
     *
     * @return a {@link soot.Unit} object.
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * <p>Setter for the field <code>unit</code>.</p>
     *
     * @param unit a {@link soot.Unit} object.
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * <p>Getter for the field <code>method</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMethod() {
        return method;
    }

    /**
     * <p>Setter for the field <code>method</code>.</p>
     *
     * @param method a {@link java.lang.String} object.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitContainer that = (UnitContainer) o;

        return unit.toString().equals(that.unit.toString());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return ("UnitContainer{" +
                "unit=" + unit +
                ", method='" + method + "\'" +
                "}").hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "UnitContainer{" +
                "unit=" + unit +
                ", method='" + method + '\'' +
                '}';
    }


}

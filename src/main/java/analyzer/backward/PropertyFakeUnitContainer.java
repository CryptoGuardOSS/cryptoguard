package analyzer.backward;

/**
 * <p>PropertyFakeUnitContainer class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class PropertyFakeUnitContainer extends UnitContainer {

    private String originalProperty;

    /**
     * <p>Getter for the field <code>originalProperty</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOriginalProperty() {
        return originalProperty;
    }

    /**
     * <p>Setter for the field <code>originalProperty</code>.</p>
     *
     * @param originalProperty a {@link java.lang.String} object.
     */
    public void setOriginalProperty(String originalProperty) {
        this.originalProperty = originalProperty;
    }
}

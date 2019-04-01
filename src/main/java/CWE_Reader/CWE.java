package CWE_Reader;

/**
 * <p>CWE class.</p>
 *
 * @author RigorityJTeam
 * Created on 2018-12-05.
 * @version $Id: $Id
 * @since 01.00.07
 *
 * <p>The Class containing the CWE Info</p>
 */
public class CWE {

    //region Attributes
    private Integer id;
    private String name;
    private String weaknessAbstraction;
    private String description;
    private String extendedDescription;
    //endregion


    /**
     * The cwe general info body
     * This class only carries a brief subset of the full information of a CWE
     *
     * @param id                  {@link java.lang.Integer} - The id of the CWE
     * @param name                {@link java.lang.String} - The name of the CWE
     * @param weaknessAbstraction {@link java.lang.String} - The weakness type of the CWE
     * @param description         {@link java.lang.String} - The short description of the CWE
     * @param extendedDescription {@link java.lang.String} - The extended description of the CWE
     */
    public CWE(Integer id, String name, String weaknessAbstraction, String description, String extendedDescription) {
        this.id = id;
        this.name = name;
        this.weaknessAbstraction = weaknessAbstraction;
        this.description = description;
        this.extendedDescription = extendedDescription;
    }

    //region Getters

    /**
     * Getter for id
     *
     * <p>getId()</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter for name
     *
     * <p>getName()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for weaknessAbstraction
     *
     * <p>getWeaknessAbstraction()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWeaknessAbstraction() {
        return weaknessAbstraction;
    }

    /**
     * Getter for description
     *
     * <p>getDescription()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for extendedDescription
     *
     * <p>getExtendedDescription()</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getExtendedDescription() {
        return extendedDescription;
    }
    //endregion
}

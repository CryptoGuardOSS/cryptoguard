package main.CWE_Reader;

/**
 * @author RigorityJTeam
 * Created on 2018-12-05.
 * @since 01.00.07
 *
 * <p>The Class containing the CWE Info</p>
 */
public class CWE_Class {

    //region Attributes
    private Integer id;
    private String name;
    private String weaknessAbstraction;
    private String description;
    private String extendedDescription;
    //endregion


    /**
     * TODO - SET THIS INFO
     *
     * @param id
     * @param name
     * @param weaknessAbstraction
     * @param description
     * @param extendedDescription
     */
    public CWE_Class(Integer id, String name, String weaknessAbstraction, String description, String extendedDescription) {
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
     * @return a {@link Integer} object.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter for name
     *
     * <p>getName()</p>
     *
     * @return a {@link String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for weaknessAbstraction
     *
     * <p>getWeaknessAbstraction()</p>
     *
     * @return a {@link String} object.
     */
    public String getWeaknessAbstraction() {
        return weaknessAbstraction;
    }

    /**
     * Getter for description
     *
     * <p>getDescription()</p>
     *
     * @return a {@link String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for extendedDescription
     *
     * <p>getExtendedDescription()</p>
     *
     * @return a {@link String} object.
     */
    public String getExtendedDescription() {
        return extendedDescription;
    }
    //endregion
}

package main.CWE_Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RigorityJTeam
 * Created on 2018-12-05.
 * @since 01.00.07
 *
 * <p>The Lazy CWE Loader that retrieves CWES for Java</p>
 */
public class CWEList {

    //region Attributes
    private final String cweCSV = "rsc/CWE.csv";
    private final File cwe = new File(cweCSV);
    private final Double cweVersion = 3.1;
    private Map<Integer, CWE> cweList;
    //endregion

    /**
     * The empty constructor for this class
     */
    public CWEList() {

    }
    //region Getters

    /**
     * The "Lazy Loading" Getter for cweList
     *
     * <p>getCweList()</p>
     *
     * @return a {@link Map} object.
     */
    public Map<Integer, CWE> getCweList() {

        if (this.cweList == null)
            CWE_Loader();

        return cweList;
    }

    /**
     * The "Lazy Loading" for CWE
     *
     * <p>CWE_Loader()</p>
     */
    private void CWE_Loader() {
        this.cweList = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(cwe))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                cweList.put(Integer.valueOf(split[0]), new CWE(Integer.valueOf(split[0]), split[1], split[2], split[4], split[5]));
            }
            this.cweList.put(-1, new CWE(-1, "CWE Not Available", "CWE Not Available", "CWE Not Available", "CWE Not Available"));
        } catch (IOException e) {
            this.cweList.put(-1, new CWE(-1, "CWE Not Loaded", "CWE Not Loaded", "CWE Not Loaded", "CWE Not Loaded"));

        }

    }

    /**
     * The lookup for the CWE
     *
     * <p>CWE_Lookup({@link java.lang.Integer})</p>
     *
     * @param cweId {@link java.lang.Integer}
     * @return a {@link CWE} object.
     */
    public CWE CWE_Lookup(Integer cweId) {
        if (this.getCweList().containsKey(cweId))
            return this.cweList.get(cweId);
        else
            return this.cweList.get(-1);
    }
    //endregion


}
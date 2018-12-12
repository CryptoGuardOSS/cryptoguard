package main.CWE_Reader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RigorityJTeam
 * Created on 2018-12-05.
 * @since 01.00.07
 *
 * <p>{Description}</p>
 */
public class CWE_List {

    //region Attributes
    private final String cweCSV = "CWE.csv";
    private final Integer cweVersion = 600;
    private Map<Integer, CWE_Class> cweList;
    //endregion

    /**
     *
     */
    public CWE_List() {
        try{
            //File file = new File(getClass().getClassLoader().getResource(cweCSV).getFile());
            FileInputStream test = new FileInputStream(new File(getClass().getClassLoader().getResource(cweCSV).getFile()));
            System.out.println("HERE");
        }
        catch(Exception e)
        {
            System.out.println("HERE");
        }

        this.cweList = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(cweCSV))) {
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split(",");
                cweList.put(Integer.valueOf(split[0]), new CWE_Class(Integer.valueOf(split[0]), split[1], split[2], split[4], split[5]));
                line = br.readLine();
            }
        } catch (IOException e) {
        }
    }
    //region Getters
    /**
     * Getter for cweList
     *
     * <p>getCweList()</p>
     *
     * @return a {@link Map< Integer, CWE_Class>} object.
     */
    public Map<Integer, CWE_Class> getCweList() {
        return cweList;
    }
    //endregion
}
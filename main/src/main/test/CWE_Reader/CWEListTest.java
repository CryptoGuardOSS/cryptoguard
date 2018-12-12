package CWE_Reader;

import main.CWE_Reader.CWE_Class;
import main.CWE_Reader.CWE_List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CWEListTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sampleTestOne()
    {
        CWE_List list = new CWE_List();
        for (CWE_Class cwe : list.getCweList().values())
            System.out.println(cwe.getId());

    }
}
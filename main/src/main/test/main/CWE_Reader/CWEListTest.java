package main.CWE_Reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class CWEListTest {

    //region Attributes
    CWEList list;
    //endregion

    //region Setting Up Environment
    @Before
    public void setUp() throws Exception {
        list = new CWEList();
    }

    @After
    public void tearDown() throws Exception {
        list = null;
    }
    //endregion

    //region Tests
    @Test
    public void sampleTestOne() {
        assertNotNull(list.getCweList());
        assertTrue(list.getCweList().keySet().size() > 1);

        for (Integer key : list.getCweList().keySet()) {
            CWE tempCWE = list.getCweList().get(key);

            assertNotNull(tempCWE);
            assertNotNull(tempCWE.getId());
            assertNotNull(tempCWE.getName());
            assertNotNull(tempCWE.getDescription());
            assertNotNull(tempCWE.getExtendedDescription());
            assertNotNull(tempCWE.getWeaknessAbstraction());
        }

    }
    //endregion
}
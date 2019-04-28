package CWE_Reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * <p>CWEListTest class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class CWEListTest {

    //region Attributes
    CWEList list;
    //endregion

    //region Setting Up Environment

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
        list = new CWEList();
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
        list = null;
    }
    //endregion

    //region Tests

    /**
     * <p>sampleTestOne.</p>
     */
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

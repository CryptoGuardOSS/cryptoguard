/* Licensed under GPL-3.0 */
package CWE_Reader;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * CWEListTest class.
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
   * setUp.
   *
   * @throws java.lang.Exception if any.
   */
  @Before
  public void setUp() throws Exception {
    list = new CWEList();
  }

  /**
   * tearDown.
   *
   * @throws java.lang.Exception if any.
   */
  @After
  public void tearDown() throws Exception {
    list = null;
  }
  //endregion

  //region Tests

  /** sampleTestOne. */
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

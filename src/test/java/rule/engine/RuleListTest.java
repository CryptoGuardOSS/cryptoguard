/* Licensed under GPL-3.0 */
package rule.engine;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import CWE_Reader.CWE;
import CWE_Reader.CWEList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * RuleListTest class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class RuleListTest {

  //region Attributes
  private final CWEList cwes = new CWEList();
  //endregion

  //region Test Environment Setup

  /**
   * setUp.
   *
   * @throws java.lang.Exception if any.
   */
  @Before
  public void setUp() throws Exception {}

  /**
   * tearDown.
   *
   * @throws java.lang.Exception if any.
   */
  @After
  public void tearDown() throws Exception {}
  //endregion

  //region Tests

  /** testGetRulesByNumber. */
  @Test
  public void testGetRulesByNumber() {
    for (RuleList rule : RuleList.values())
      assertEquals(rule, RuleList.getRuleByRuleNumber(rule.getRuleId()));
  }

  /** testCWEListing. */
  @Test
  public void testCWEListing() {
    for (RuleList rule : RuleList.values())
      if (rule.getRuleId() != -1)
        for (CWE cwe : rule.retrieveCWEInfo(cwes)) assertTrue(cwe.getId() != -1);
  }
  //endregion
}

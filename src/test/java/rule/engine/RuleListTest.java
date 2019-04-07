package rule.engine;

import CWE_Reader.CWE;
import CWE_Reader.CWEList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>RuleListTest class.</p>
 *
 * @author drmonster
 * @version $Id: $Id
 * @since V03.03.10
 */
public class RuleListTest {

    //region Attributes
    private final CWEList cwes = new CWEList();
    //endregion

    //region Test Environment Setup

    /**
     * <p>setUp.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown() throws Exception {
    }
    //endregion

    //region Tests

    /**
     * <p>testGetRulesByNumber.</p>
     */
    @Test
    public void testGetRulesByNumber() {
        for (RuleList rule : RuleList.values())
            assertEquals(rule, RuleList.getRuleByRuleNumber(rule.getRuleId()));
    }

    /**
     * <p>testCWEListing.</p>
     */
    @Test
    public void testCWEListing() {
        for (RuleList rule : RuleList.values())
            if (rule.getRuleId() != -1)
                for (CWE cwe : rule.retrieveCWEInfo(cwes))
                    assertTrue(cwe.getId() != -1);
    }
    //endregion
}

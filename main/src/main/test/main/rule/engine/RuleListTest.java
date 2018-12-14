package main.rule.engine;

import main.CWE_Reader.CWE;
import main.CWE_Reader.CWEList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RuleListTest {

    //region Attributes
    private final CWEList cwes = new CWEList();
    //endregion

    //region Test Environment Setup
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    //endregion

    //region Tests
    @Test
    public void testGetRulesByNumber() {
        for (RuleList rule : RuleList.values())
            assertEquals(rule, RuleList.getRuleByRuleNumber(rule.getRuleId()));
    }

    @Test
    public void testCWEListing() {
        for (RuleList rule : RuleList.values())
            if (rule.getRuleId() != -1)
                for (CWE cwe : rule.retrieveCWEInfo(cwes))
                    assertTrue(cwe.getId() != -1);
    }
    //endregion
}
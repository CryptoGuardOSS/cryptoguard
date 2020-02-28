package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * <p>AnalyzerReportTest class.</p>
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class AnalyzerReportTest {

    private Method method;

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
        method = null;
    }

    /**
     * <p>simpleTest_1.</p>
     */
    @Test
    public void simpleTest_1() {
        try {
            method = new Method(23, true, "hai");

            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(method);
            assertNotNull(xml);
            System.out.println(xml);
        } catch (JsonProcessingException e) {
            assertNull(e);
            e.printStackTrace();
        }
    }
}

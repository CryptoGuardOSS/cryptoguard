package frontEnd.MessagingSystem.routing.structure.Scarf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AnalyzerReportTest {

    private AnalyzerReport report;
    private Method method;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
        method = null;
    }

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
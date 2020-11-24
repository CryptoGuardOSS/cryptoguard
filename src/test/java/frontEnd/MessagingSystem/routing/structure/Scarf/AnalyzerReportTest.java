/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.structure.Scarf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * AnalyzerReportTest class.
 *
 * @author franceme
 * @version $Id: $Id
 * @since V03.03.10
 */
public class AnalyzerReportTest {

  private Method method;

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
  public void tearDown() throws Exception {
    method = null;
  }

  /** simpleTest_1. */
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

/* Licensed under GPL-3.0 */
package frontEnd.Interface.parameterChecks;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author franceme Created on 2020-11-17.
 * @since {VersionHere}
 *     <p>ContainerTest
 *     <p>{Description Here}
 */
public class ContainerTest {
  //region Attributes
  //endregion

  //region Test Environment Setup
  @Before
  public void setup() {}

  @After
  public void tearDown() {}

  //endregion

  //region Tests
  @Test
  public void baseTest() {
    String baseContainer = new Container().toString();
    String baseResult =
        "\"Container\": {\"javaHome\":\"None\",\"androidHome\":\"None\",\"debuggingLevel\":-1,\"listing\":{ \"type\": \"Default\", \"flag\": \"D\"},\"engineType\":\"CLASSFILES\",\"sourceValues\":[],\"dependencyValues\":[],\"overwriteOutFile\":false,\"outFile\":\"None\",\"mainFile\":\"None\",\"prettyPrint\":true,\"showTimes\":false,\"streaming\":true,\"displayHeuristics\":false,\"scanningDepth\":\"None\",\"packageName\":\"None\"}";

    assertEquals(baseResult, baseContainer);
  }

  @Test
  public void javaHomeTest() {
    String fakePath = "/fake/path/here";

    String baseContainer = new Container().withJavaHome(fakePath).toString();
    String baseResult =
        "\"Container\": {\"javaHome\":\""
            + fakePath
            + "\",\"androidHome\":\"None\",\"debuggingLevel\":-1,\"listing\":{ \"type\": \"Default\", \"flag\": \"D\"},\"engineType\":\"CLASSFILES\",\"sourceValues\":[],\"dependencyValues\":[],\"overwriteOutFile\":false,\"outFile\":\"None\",\"mainFile\":\"None\",\"prettyPrint\":true,\"showTimes\":false,\"streaming\":true,\"displayHeuristics\":false,\"scanningDepth\":\"None\",\"packageName\":\"None\"}";

    assertEquals(baseResult, baseContainer);
  }

  @Test
  public void javaHome_AndroidHome_Test() {
    String fakePath = "/fake/path/here";
    String fakePathTwo = "/fake/path/here/again";

    String baseContainer =
        new Container().withJavaHome(fakePath).withAndroidHome(fakePathTwo).toString();
    String baseResult =
        "\"Container\": {\"javaHome\":\""
            + fakePath
            + "\",\"androidHome\":\""
            + fakePathTwo
            + "\",\"debuggingLevel\":-1,\"listing\":{ \"type\": \"Default\", \"flag\": \"D\"},\"engineType\":\"CLASSFILES\",\"sourceValues\":[],\"dependencyValues\":[],\"overwriteOutFile\":false,\"outFile\":\"None\",\"mainFile\":\"None\",\"prettyPrint\":true,\"showTimes\":false,\"streaming\":true,\"displayHeuristics\":false,\"scanningDepth\":\"None\",\"packageName\":\"None\"}";

    assertEquals(baseResult, baseContainer);
  }

  @Test
  public void sourceFil_Test() {
    String fakePath = "/fake/path/here";

    String baseContainer = new Container().addSourceValue(fakePath).toString();
    String baseResult =
        "\"Container\": {\"javaHome\":\"None\",\"androidHome\":\"None\",\"debuggingLevel\":-1,\"listing\":{ \"type\": \"Default\", \"flag\": \"D\"},\"engineType\":\"CLASSFILES\",\"sourceValues\":[\""
            + fakePath
            + "\"],\"dependencyValues\":[],\"overwriteOutFile\":false,\"outFile\":\"None\",\"mainFile\":\"None\",\"prettyPrint\":true,\"showTimes\":false,\"streaming\":true,\"displayHeuristics\":false,\"scanningDepth\":\"None\",\"packageName\":\"None\"}";

    assertEquals(baseResult, baseContainer);
  }

  @Test
  public void sourceFiles_Test() {
    String fakePath = "/fake/path/here";
    String fakePathTwo = "/fake/path/here/again";

    String baseContainer =
        new Container().addSourceValue(fakePath).addSourceValue(fakePathTwo).toString();
    String baseResult =
        "\"Container\": {\"javaHome\":\"None\",\"androidHome\":\"None\",\"debuggingLevel\":-1,\"listing\":{ \"type\": \"Default\", \"flag\": \"D\"},\"engineType\":\"CLASSFILES\",\"sourceValues\":[\""
            + fakePath
            + "\",\""
            + fakePathTwo
            + "\"],\"dependencyValues\":[],\"overwriteOutFile\":false,\"outFile\":\"None\",\"mainFile\":\"None\",\"prettyPrint\":true,\"showTimes\":false,\"streaming\":true,\"displayHeuristics\":false,\"scanningDepth\":\"None\",\"packageName\":\"None\"}";

    assertEquals(baseResult, baseContainer);
  }

  @Test
  public void sourceFiles_AddAll_Test() {
    ArrayList<String> fakeArray = new ArrayList<>();
    String fakePath = "/fake/path/here";
    String fakePathTwo = "/fake/path/here/again";
    fakeArray.add(fakePath);
    fakeArray.add(fakePathTwo);

    String baseContainer = new Container().addSourceValues(fakeArray).toString();
    String baseResult =
        "\"Container\": {\"javaHome\":\"None\",\"androidHome\":\"None\",\"debuggingLevel\":-1,\"listing\":{ \"type\": \"Default\", \"flag\": \"D\"},\"engineType\":\"CLASSFILES\",\"sourceValues\":[\""
            + fakePath
            + "\",\""
            + fakePathTwo
            + "\"],\"dependencyValues\":[],\"overwriteOutFile\":false,\"outFile\":\"None\",\"mainFile\":\"None\",\"prettyPrint\":true,\"showTimes\":false,\"streaming\":true,\"displayHeuristics\":false,\"scanningDepth\":\"None\",\"packageName\":\"None\"}";

    assertEquals(baseResult, baseContainer);
  }
  //endregion
}

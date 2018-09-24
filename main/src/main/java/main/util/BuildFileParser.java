package main.util;

import java.util.List;
import java.util.Map;

public interface BuildFileParser {

   Map<String, List<String>> getDependencyList() throws Exception;

}

package main.rule.engine;

import java.io.IOException;
import java.util.List;

public interface RuleChecker {

    public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath) throws IOException;
}

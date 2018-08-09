package main.rule.engine;

import java.io.IOException;

public interface RuleChecker {

    public void checkRule(EngineType type, String projectJarPath, String projectDependencyPath) throws IOException;
}

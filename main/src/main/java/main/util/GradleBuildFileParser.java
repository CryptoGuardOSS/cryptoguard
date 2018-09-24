package main.util;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.Statement;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradleBuildFileParser implements BuildFileParser {


    Map<String, String> moduleVsPath = new HashMap<>();

    public GradleBuildFileParser(String fileName) throws Exception {

        final String content = new String(Files.readAllBytes(Paths.get(fileName)), "UTF-8");

        List<ASTNode> astNodes = new AstBuilder().buildFromString(content);

        String[] splits = fileName.split("/");
        String projectName = splits[splits.length - 2];
        final String projectRoot = fileName.substring(0, fileName.lastIndexOf('/'));

        GroovyCodeVisitor visitor = new CodeVisitorSupport() {

            @Override
            public void visitMethodCallExpression(MethodCallExpression call) {

                List<Expression> args = ((ArgumentListExpression) call.getArguments()).getExpressions();

                for (Expression arg : args) {

                    moduleVsPath.put(arg.getText(), projectRoot + "/" + arg.getText());
                }
            }
        };

        for (ASTNode astNode : astNodes) {
            astNode.visit(visitor);
        }

        if (moduleVsPath.isEmpty()) {
            moduleVsPath.put(projectName, projectRoot);
        }
    }

    @Override
    public Map<String, List<String>> getDependencyList() throws Exception {

        Map<String, List<String>> moduleVsDependencies = new HashMap<>();

        for (String module : moduleVsPath.keySet()) {

            final List<String> dependencies = new ArrayList<>();

            String buildFile = moduleVsPath.get(module) + "/build.gradle";

            String content = new String(Files.readAllBytes(Paths.get(buildFile)), "UTF-8");

            List<ASTNode> astNodes = new AstBuilder().buildFromString(content);

            GroovyCodeVisitor visitor = new CodeVisitorSupport() {

                @Override
                public void visitClosureExpression(ClosureExpression expression) {

                    Statement block = expression.getCode();
                    if (block instanceof BlockStatement) {
                        BlockStatement bs = (BlockStatement) block;
                        for (Statement statement : bs.getStatements()) {

                            String stmtStr = statement.getText();

                            if (stmtStr.contains("this.compile(this.project(:")) {
                               String dependency = stmtStr.substring(stmtStr.indexOf(':') + 1, stmtStr.indexOf(')'));
                                dependencies.add(dependency);
                            }
                        }
                    }
                }
            };

            for (ASTNode astNode : astNodes) {
                astNode.visit(visitor);
            }

            moduleVsDependencies.put(module, dependencies);
        }

        Map<String, List<String>> moduleVsDependencyPaths = new HashMap<>();

        for (String module : moduleVsDependencies.keySet()) {
            List<String> dependencyPaths = new ArrayList<>();
            calcAlldependenciesForModule(module, moduleVsDependencies, dependencyPaths);
            dependencyPaths.add(moduleVsPath.get(module) + "/src/main/java");
            moduleVsDependencyPaths.put(module, dependencyPaths);
        }

        return moduleVsDependencyPaths;
    }

    private void calcAlldependenciesForModule(String module, Map<String, List<String>> mVsds, List<String> dependencyPaths) {
        for (String dependency : mVsds.get(module)) {
            dependencyPaths.add(moduleVsPath.get(dependency) + "/src/main/java");
            calcAlldependenciesForModule(dependency, mVsds, dependencyPaths);
        }
    }

    public static void main(String[] args) throws Exception {

        GradleBuildFileParser buildFileParser = new GradleBuildFileParser("/home/krishnokoli/projects/gradle-sample/settings.gradle");
        System.out.println(buildFileParser.getDependencyList());
    }
}

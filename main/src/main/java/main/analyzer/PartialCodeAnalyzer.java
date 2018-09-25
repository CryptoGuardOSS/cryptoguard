package main.analyzer;

import main.rule.base.BaseRuleChecker;
import main.util.Utils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartialCodeAnalyzer {

    public static void analyzeSlices(String criteriaClass,
                                     String criteriaMethod,
                                     int criteriaParam,
                                     List<String> snippetPath,
                                     List<String> projectDependency,
                                     BaseRuleChecker checker) throws IOException {

        String javaHome = System.getenv("JAVA7_HOME");

        if (javaHome == null || javaHome.isEmpty()) {

            System.err.println("Please set JAVA7_HOME");
            System.exit(1);
        }

        Options.v().set_allow_phantom_refs(true);
        Options.v().set_keep_line_number(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        StringBuilder srcPaths = new StringBuilder();

        for (String srcDir : snippetPath) {
            srcPaths.append(srcDir)
                    .append(":");
        }


        Scene.v().setSootClassPath(javaHome + "/jre/lib/rt.jar:"
                + javaHome + "/jre/lib/jce.jar:" + srcPaths.toString() + Utils.buildSootClassPath(projectDependency));

        List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

        for (String clazz : BaseAnalyzer.CRITERIA_CLASSES) {
            Scene.v().loadClassAndSupport(clazz);
        }

        for (String clazz : classNames) {
            Scene.v().loadClassAndSupport(clazz);
        }

        Scene.v().loadNecessaryClasses();

        String endPoint = "<" + criteriaClass + ": " + criteriaMethod + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteriaParam);

        BaseAnalyzer.analyzeSliceInternal(criteriaClass, classNames, endPoint, slicingParameters, checker);
    }
}

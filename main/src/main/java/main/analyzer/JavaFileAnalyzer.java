package main.analyzer;

import main.rule.base.BaseRuleChecker;
import main.rule.engine.Criteria;
import main.util.Utils;
import org.apache.commons.lang3.StringUtils;
import soot.Scene;
import soot.options.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author RigorityJTeam
 * Created on 2019-01-18.
 * @since 01.01.12
 *
 * <p>The transformation of the Java Source File Criteria.</p>
 */
public class JavaFileAnalyzer {

    //TODO - Fix This

    /**
     * <p>analyzeSlices.</p>
     *
     * @param snippetPath a {@link java.util.List} object.
     * @param checker     a {@link main.rule.base.BaseRuleChecker} object.
     * @throws java.io.IOException if any.
     */
    public static void analyzeSlices(Criteria criteria,
                                     List<String> snippetPath,
                                     List<String> projectDependency,
                                     BaseRuleChecker checker) throws IOException {

        String java7Home = System.getenv("JAVA7_HOME");

        if (StringUtils.isBlank(java7Home)) {
            System.err.println("Please set JAVA7_HOME");
            System.exit(1);
        }

        Options.v().set_allow_phantom_refs(true);
        Options.v().set_keep_line_number(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_java);

        String split = System.getProperty("file.separator");
        StringBuilder rt = new StringBuilder(java7Home);
        for (String in : new String[]{"jre", "lib", "rt.jar"})
            rt.append(split).append(in);
        //String jce = String.join(split, javaHome, "jre", "lib", "jce.jar");
        StringBuilder jce = new StringBuilder(java7Home);
        for (String in : new String[]{"jre", "lib", "jce.jar"})
            rt.append(split).append(in);

        Scene.v().setSootClassPath(rt.toString() + ":" + jce.toString() + ":" + Utils.buildSootClassPath(projectDependency));


        List<String> classNames = new ArrayList<>();

        for (String snippit : snippetPath)
            classNames.addAll(Utils.retrieveFullyQualifiedName(Arrays.asList(snippit)));

        for (String clazz : BaseAnalyzer.CRITERIA_CLASSES) {
            Scene.v().loadClassAndSupport(clazz);
        }

        for (String clazz : classNames) {
            Scene.v().loadClassAndSupport(clazz);
        }

        Scene.v().loadNecessaryClasses();

        String endPoint = "<" + criteria.getClassName() + ": " + criteria.getMethodName() + ">";
        ArrayList<Integer> slicingParameters = new ArrayList<>();
        slicingParameters.add(criteria.getParam());


        BaseAnalyzer.analyzeSliceInternal(criteria.getClassName(), classNames, endPoint, slicingParameters, checker);

    }
}

package main.rule;

import main.rule.engine.EngineType;
import main.rule.engine.RuleChecker;
import main.slicer.forward.ForwardInfluenceInstructions;
import main.slicer.forward.SlicingCriteria;
import main.slicer.forward.SlicingResult;
import main.util.Utils;
import soot.*;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.io.IOException;
import java.util.*;

import static main.util.Utils.getClassNamesFromApkArchive;

public class DefaultExportGradeKeyFinder implements RuleChecker {

	private static final List<String> SLICING_CRITERIA = new ArrayList<>();

	static {
		SLICING_CRITERIA.add("<java.security.KeyPairGenerator: java.security.KeyPairGenerator getInstance(java.lang.String)>");
		SLICING_CRITERIA.add("<java.security.KeyPairGenerator: java.security.KeyPairGenerator getInstance(java.lang.String,java.lang.String)>");
		SLICING_CRITERIA.add("<java.security.KeyPairGenerator: java.security.KeyPairGenerator getInstance(java.lang.String,java.security.Provider)>");
	}

	private static final String METHOD_TO_FIND = "<java.security.KeyPairGenerator: void initialize(";

	private boolean defaultSecure;

	private ArrayList<String> methodsToLook;

	private ArrayList<String> initializeCallsites = new ArrayList<>();

	@Override
	public void checkRule(EngineType type, List<String> projectJarPath, List<String> projectDependencyPath) throws IOException {

		for (String slicing_criterion : SLICING_CRITERIA) {

			SlicingCriteria criteria = new SlicingCriteria(slicing_criterion);
			Map<String, List<Unit>> analysisLists;
			if (type == EngineType.JAR) {
				analysisLists = analyzeJar(projectJarPath.get(0), projectDependencyPath.get(0), criteria);
			}
			else if (type == EngineType.APK) {
				analysisLists = analyzeApk(projectJarPath.get(0), criteria);
			}
			else {
				analysisLists = analyzeSnippet(projectJarPath, projectDependencyPath, criteria);
			}

			for (String method : analysisLists.keySet()) {

				if (!methodsToLook.toString().contains(method)) {
					continue;
				}

				List<Unit> analysis = analysisLists.get(method);

				if (!analysis.isEmpty()) {

					boolean isDefault = true;

					for (Unit unit : analysis) {

						if (unit.toString().contains(METHOD_TO_FIND)) {

							String containingMethod = method.substring(0, method.indexOf('['));
							initializeCallsites.add(containingMethod + "[" + unit.getJavaSourceStartLineNumber() + "]");
							isDefault = false;
							break;
						}
					}

					if (isDefault && !defaultSecure) {
						System.out.println("=======================================");
						String output = "***Violated Rule 5: Used export grade public Key ";
						output += "\n***Cause: Used default key size in method: " + method;
						System.out.println(output);
						System.out.println("=======================================");
					}

				}
			}
		}

	}

	public void setMethodsToLook(ArrayList<String> methodsToLook) {
		this.methodsToLook = methodsToLook;
	}

	public ArrayList<String> getInitializationCallsites() {
		return this.initializeCallsites;
	}

	public void setDefaultSecure(boolean defaultSecure) {
		this.defaultSecure = defaultSecure;
	}

	private Map<String, List<Unit>> analyzeJar(String projectJarPath, String projectDependencyPath, SlicingCriteria slicingCriteria) throws IOException {
		String javaHome = System.getenv("JAVA_HOME");

		if (javaHome.isEmpty()) {

			System.err.println("Please set JAVA_HOME");
			System.exit(1);
		}

		String sootClassPath = Utils.buildSootClassPath(projectJarPath,
														javaHome + "/jre/lib/rt.jar",
														javaHome + "/jre/lib/jce.jar",
														projectDependencyPath);
		Options.v().set_keep_line_number(true);
		Options.v().set_allow_phantom_refs(true);

		Scene.v().setSootClassPath(sootClassPath);

		Scene.v().loadBasicClasses();

		List<String> classNames = Utils.getClassNamesFromJarArchive(projectJarPath);
		return getForwardSlice(classNames, slicingCriteria);
	}

	private Map<String, List<Unit>> analyzeApk(String projectJarPath, SlicingCriteria slicingCriteria) throws IOException {
		String javaHome = System.getenv("JAVA_HOME");
		String androidHome = System.getenv("ANDROID_SDK_HOME");

		if (javaHome == null) {

			System.err.println("Please set JAVA_HOME");
			System.exit(1);
		}

		if (androidHome == null) {

			System.err.println("Please set ANDROID_SDK_HOME");
			System.exit(1);
		}

		Options.v().set_keep_line_number(true);
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_android_jars(androidHome + "/platforms");
		Options.v().set_soot_classpath(javaHome + "/jre/lib/rt.jar:" + javaHome + "/jre/lib/jce.jar");

		Options.v().set_process_dir(Collections.singletonList(projectJarPath));
		Options.v().set_whole_program(true);
		Options.v().set_allow_phantom_refs(true);

		Scene.v().loadBasicClasses();

		List<String> classNames = getClassNamesFromApkArchive(projectJarPath);
		return getForwardSlice(classNames, slicingCriteria);
	}

	private Map<String, List<Unit>> analyzeSnippet(List<String> snippetPath, List<String> projectDependencyPath, SlicingCriteria slicingCriteria) throws IOException {

		String javaHome = System.getenv("JAVA7_HOME");

		if (javaHome.isEmpty()) {

			System.err.println("Please set JAVA7_HOME");
			System.exit(1);
		}

		List<String> classNames = Utils.getClassNamesFromSnippet(snippetPath);

		StringBuilder srcPaths = new StringBuilder();

		for (String srcDir : snippetPath) {
			srcPaths.append(srcDir)
					.append(":");
		}

		Options.v().set_soot_classpath(javaHome + "/jre/lib/rt.jar:"
											   + javaHome + "/jre/lib/jce.jar:" + srcPaths.toString() + Utils.buildSootClassPath(projectDependencyPath));

		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_src_prec(Options.src_prec_java);

		for (String className : classNames) {
			Options.v().classes().add(className);
		}

		Options.v().set_keep_line_number(true);
		Options.v().set_allow_phantom_refs(true);

		Scene.v().loadBasicClasses();

		return getForwardSlice(classNames, slicingCriteria);
	}

	private static Map<String, List<Unit>> getForwardSlice(List<String> classNames, SlicingCriteria slicingCriteria) {

		Map<String, List<Unit>> analysisListMap = new HashMap<>();

		for (String className : classNames) {

			SootClass sClass = Scene.v().loadClassAndSupport(className);

			sClass.setApplicationClass();

			for (SootMethod method : sClass.getMethods()) {
				SlicingResult slicingResult = getInfluencingInstructions(slicingCriteria, method);

				if (slicingResult != null) {
					analysisListMap.put(method.toString() + "[" + slicingResult.getCallSiteInfo().getLineNumber() + "]", slicingResult.getAnalysisResult());
				}
			}
		}

		return analysisListMap;
	}

	public static SlicingResult getInfluencingInstructions(SlicingCriteria slicingCriteria,
														   SootMethod m) {
		if (m.isConcrete()) {

			Body b = m.retrieveActiveBody();

			UnitGraph graph = new ExceptionalUnitGraph(b);
			ForwardInfluenceInstructions vbe = new ForwardInfluenceInstructions(graph, slicingCriteria);
			return vbe.getSlicingResult();
		}
		return null;
	}
}

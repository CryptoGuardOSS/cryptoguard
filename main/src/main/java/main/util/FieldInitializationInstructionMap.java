package main.util;

import main.analyzer.backward.MethodWrapper;
import main.slicer.backward.property.PropertyAnalysisResult;
import main.slicer.backward.property.PropertyInfluencingInstructions;
import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>FieldInitializationInstructionMap class.</p>
 *
 * @author RigorityJTeam
 * @since V01.00
 */
public class FieldInitializationInstructionMap {

	private static Map<String, List<PropertyAnalysisResult>> initializationInstructions = null;
	private static Map<String, List<MethodWrapper>> fieldVsMethodWrapper = null;

	/**
	 * <p>reset.</p>
	 */
	public static void reset() {
		initializationInstructions = null;
		fieldVsMethodWrapper = null;
	}

	/**
	 * <p>build.</p>
	 *
	 * @param classNames a {@link java.util.List} object.
	 */
	public static void build(List<String> classNames) {

		if (fieldVsMethodWrapper == null) {

			fieldVsMethodWrapper = new HashMap<>();

			for (String className : classNames) {

				SootClass sClass = Scene.v().loadClassAndSupport(className);

				Chain<SootField> sootFields = sClass.getFields();

				for (SootField field : sootFields) {

					List<MethodWrapper> initMethods = new ArrayList<>();

					List<SootMethod> methodList = sClass.getMethods();

					for (SootMethod method : methodList) {

						if (method.isConcrete()) {

							StringBuilder methodBody = new StringBuilder();

							try {
								Body initBody = method.retrieveActiveBody();
								UnitGraph graph = new ExceptionalUnitGraph(initBody);

								for (Object aGraph : graph) {
									methodBody.append(aGraph);
								}

								if (methodBody.toString().contains(field.toString() + " =")) {
									initMethods.add(NamedMethodMap.getMethod(method.toString()));
								}
							} catch (RuntimeException e) {
								System.err.println(e);
								continue;
							}
						}

					}

					fieldVsMethodWrapper.put(field.toString(), initMethods);
				}

			}
		}
	}

	/**
	 * <p>getInitInstructions.</p>
	 *
	 * @param fieldName a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public static List<PropertyAnalysisResult> getInitInstructions(String fieldName) {

		if (fieldVsMethodWrapper == null) {
			throw new RuntimeException("Execute build first ...");
		}

		if (initializationInstructions == null) {
			initializationInstructions = new HashMap<>();
		}

		if (!initializationInstructions.containsKey(fieldName)) {
			List<MethodWrapper> initMethodList = fieldVsMethodWrapper.get(fieldName);

			if (initMethodList == null || initMethodList.isEmpty()) {
				return null;
			}
			else {

				List<PropertyAnalysisResult> analysisResultList = new ArrayList<>();
				for (MethodWrapper method : initMethodList) {
					PropertyInfluencingInstructions simpleSlicerInstructions =
							new PropertyInfluencingInstructions(method, fieldName);

					PropertyAnalysisResult analysis = simpleSlicerInstructions.getSlicingResult();

					if (!analysis.getSlicingResult().isEmpty()) {
						analysisResultList.add(analysis);
					}
				}

				if (!analysisResultList.isEmpty()) {
					initializationInstructions.put(fieldName, analysisResultList);
				}
			}
		}

		return initializationInstructions.get(fieldName);
	}
}

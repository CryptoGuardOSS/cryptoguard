package main.rule;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.UnitContainer;
import main.rule.base.BaseRuleChecker;
import main.rule.base.MajorHeuristics;
import main.rule.engine.Criteria;
import soot.IntType;
import soot.ValueBox;
import soot.jimple.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by krishnokoli on 10/22/17.
 *
 * @author krishnokoli
 * @since V01.00
 */
public class PBEInterationCountFinder extends BaseRuleChecker {

	private static final List<Criteria> CRITERIA_LIST = new ArrayList<>();


	private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
	private Map<UnitContainer, List<String>> othersSourceMap = new HashMap<>();

	static {

		Criteria criteria2 = new Criteria();
		criteria2.setClassName("javax.crypto.spec.PBEParameterSpec");
		criteria2.setMethodName("void <init>(byte[],int)");
		criteria2.setParam(1);
		CRITERIA_LIST.add(criteria2);

		Criteria criteria3 = new Criteria();
		criteria3.setClassName("javax.crypto.spec.PBEParameterSpec");
		criteria3.setMethodName("void <init>(byte[],int,java.security.spec.AlgorithmParameterSpec)");
		criteria3.setParam(1);
		CRITERIA_LIST.add(criteria3);

		Criteria criteria4 = new Criteria();
		criteria4.setClassName("javax.crypto.spec.PBEKeySpec");
		criteria4.setMethodName("void <init>(char[],byte[],int,int)");
		criteria4.setParam(2);
		CRITERIA_LIST.add(criteria4);

		Criteria criteria5 = new Criteria();
		criteria5.setClassName("javax.crypto.spec.PBEKeySpec");
		criteria5.setMethodName("void <init>(char[],byte[],int)");
		criteria5.setParam(2);
		CRITERIA_LIST.add(criteria5);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Criteria> getCriteriaList() {
		return CRITERIA_LIST;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void analyzeSlice(Analysis analysis) {
		if (analysis.getAnalysisResult().isEmpty()) {
			return;
		}

		for (int index = 0; index < analysis.getAnalysisResult().size(); index++) {

			UnitContainer e = analysis.getAnalysisResult().get(index);
			for (ValueBox usebox : e.getUnit().getUseBoxes()) {
				if (usebox.getValue() instanceof Constant) {

					if (usebox.getValue().getType() instanceof IntType && Integer.valueOf(usebox.getValue().toString()) < 1000) {

						List<UnitContainer> outSet = new ArrayList<>();
						outSet.add(e);

						if (MajorHeuristics.isArgumentOfInvoke(analysis, index, outSet)) {
							putIntoMap(othersSourceMap, e, usebox.getValue().toString());
						}
						else if (MajorHeuristics.isArgumentOfByteArrayCreation(analysis, index, outSet)) {
							putIntoMap(othersSourceMap, e, usebox.getValue().toString());
						}
						else {
							putIntoMap(predictableSourcMap, e, usebox.getValue().toString());
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void printAnalysisOutput(Map<String, String> configFiles) {

		String rule = "8";
		String ruleDesc = RULE_VS_DESCRIPTION.get(rule);

		List<String> predictableSources = new ArrayList<>();
		List<UnitContainer> predictableSourceInst = new ArrayList<>();
		List<String> others = new ArrayList<>();

		for (List<String> values : predictableSourcMap.values()) {
			predictableSources.addAll(values);
		}
		predictableSourceInst.addAll(predictableSourcMap.keySet());

		for (List<String> values : othersSourceMap.values()) {
			others.addAll(values);
		}

		if (!predictableSources.isEmpty()) {
			System.out.println("=======================================");
			String output = getPrintableMsg(predictableSourcMap, rule, ruleDesc);
			System.out.println(output);
			System.out.println("=======================================");
		}

//        if (!others.isEmpty()) {
//            System.out.println("=======================================");
//            String output = getOthersToPrint(configFiles, others, rule, ruleDesc);
//            System.out.println(output);
//            System.out.println("=======================================");
//        }
	}

	private String getPrintableMsg(Map<UnitContainer, List<String>> predictableSourcMap, String rule, String ruleDesc) {
		String output = "***Violated Rule " +
				rule + ": " +
				ruleDesc;

		for (UnitContainer unit : predictableSourcMap.keySet()) {

			output += "\n***Found: " + predictableSourcMap.get(unit);
			if (unit.getUnit().getJavaSourceStartLineNumber() >= 0) {
				output += " in Line " + unit.getUnit().getJavaSourceStartLineNumber();
			}

			output += " in Method: " + unit.getMethod();
		}

		return output;
	}
}

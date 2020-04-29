/* Licensed under GPL-3.0 */
package analyzer;

import analyzer.backward.Analysis;
import analyzer.backward.MethodWrapper;
import analyzer.backward.UnitContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import rule.base.BaseRuleChecker;
import slicer.backward.MethodCallSiteInfo;
import slicer.backward.SlicingCriteria;
import slicer.backward.method.MethodInfluenceInstructions;
import slicer.backward.method.MethodSlicingResult;
import slicer.backward.property.PropertyAnalysisResult;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import util.FieldInitializationInstructionMap;
import util.NamedMethodMap;
import util.Utils;

/**
 * BaseAnalyzer class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public class BaseAnalyzer {

  /** Constant <code>CRITERIA_CLASSES</code> */
  public static final List<String> CRITERIA_CLASSES = new ArrayList<>();

  private static final Logger log =
      org.apache.logging.log4j.LogManager.getLogger(BaseAnalyzer.class);

  static {
    CRITERIA_CLASSES.add("javax.crypto.Cipher");
    CRITERIA_CLASSES.add("java.security.MessageDigest");
    CRITERIA_CLASSES.add("javax.crypto.spec.SecretKeySpec");
    CRITERIA_CLASSES.add("javax.crypto.spec.PBEKeySpec");
    CRITERIA_CLASSES.add("java.security.KeyPairGenerator");
    CRITERIA_CLASSES.add("java.net.URL");
    CRITERIA_CLASSES.add("okhttp3.Request$Builder");
    CRITERIA_CLASSES.add("retrofit2.Retrofit$Builder");
    CRITERIA_CLASSES.add("javax.crypto.spec.PBEParameterSpec");
    CRITERIA_CLASSES.add("javax.crypto.spec.IvParameterSpec");
    CRITERIA_CLASSES.add("java.security.KeyStore");
    CRITERIA_CLASSES.add("java.security.SecureRandom");
  }

  static void analyzeSliceInternal(
      String criteriaClass,
      List<String> classNames,
      String endPoint,
      ArrayList<Integer> slicingParameters,
      BaseRuleChecker checker) {

    SootClass criteriaClazz = Scene.v().getSootClass(criteriaClass);

    if (criteriaClazz.isPhantomClass()
        || !criteriaClazz.getMethods().toString().contains(endPoint)) {
      return;
    }

    NamedMethodMap.build(classNames);

    NamedMethodMap.addCriteriaClasses(CRITERIA_CLASSES);
    NamedMethodMap.buildCallerCalleeRelation(classNames);

    FieldInitializationInstructionMap.build(classNames);

    runBackwardSlicingAnalysis(
        NamedMethodMap.getMethod(endPoint), slicingParameters, null, null, checker);
  }

  private static void runBackwardSlicingAnalysis(
      MethodWrapper criteria,
      List<Integer> slicingParams,
      Map<MethodWrapper, List<Analysis>> methodVsAnalysisResult,
      Map<SlicingCriteria, Boolean> slicingCriteriaMap,
      BaseRuleChecker checker) {

    List<MethodWrapper> callers = criteria.getCallerList();
    if (callers.isEmpty() || slicingParams == null || slicingParams.isEmpty()) {
      return;
    }

    List<MethodCallSiteInfo> callSites = new ArrayList<>();

    for (MethodWrapper caller : callers) {
      for (MethodCallSiteInfo site : caller.getCalleeList()) {
        if (site.getCallee().toString().equals(criteria.toString())) {
          callSites.add(site);
        }
      }
    }

    for (MethodCallSiteInfo callSiteInfo : callSites) {
      SlicingCriteria slicingCriteria = new SlicingCriteria(callSiteInfo, slicingParams);

      if (methodVsAnalysisResult == null) {
        Map<MethodWrapper, List<Analysis>> newResult = new HashMap<>();
        runBackwardSlicingAnalysisInternal(slicingCriteria, newResult, slicingCriteriaMap, checker);

        for (MethodWrapper methodWrapper : newResult.keySet()) {
          List<Analysis> analysisList = newResult.get(methodWrapper);
          for (Analysis analysis : analysisList) {

            if (!analysis.getAnalysisResult().isEmpty()) {
              Utils.NUM_SLICES++;
              Utils.SLICE_LENGTH.add(analysis.getAnalysisResult().size());
            }

            checker.analyzeSlice(analysis);
          }
        }

        System.gc();
      } else {
        runBackwardSlicingAnalysisInternal(
            slicingCriteria, methodVsAnalysisResult, slicingCriteriaMap, checker);
      }
    }
  }

  private static void runBackwardSlicingAnalysisInternal(
      SlicingCriteria slicingCriteria,
      Map<MethodWrapper, List<Analysis>> methodVsAnalysisResult,
      Map<SlicingCriteria, Boolean> slicingCriteriaMap,
      BaseRuleChecker checker) {

    if (slicingCriteriaMap == null) {
      slicingCriteriaMap = new HashMap<>();
    }

    MethodCallSiteInfo callSiteInfo = slicingCriteria.getMethodCallSiteInfo();
    List<Integer> slicingParams = slicingCriteria.getParameters();

    if (slicingCriteriaMap.get(slicingCriteria) != null) {
      return;
    }

    slicingCriteriaMap.put(slicingCriteria, Boolean.TRUE);

    MethodSlicingResult methodSlicingResult =
        getInfluencingInstructions(
            callSiteInfo, slicingParams, callSiteInfo.getCaller().getMethod());

    if (methodSlicingResult.getPropertyUseMap() != null) {
      for (String property : methodSlicingResult.getPropertyUseMap().keySet()) {
        List<PropertyAnalysisResult> propertyAnalysisResults =
            methodSlicingResult.getPropertyUseMap().get(property);
        for (PropertyAnalysisResult propertyAnalysisResult : propertyAnalysisResults) {

          List<Analysis> calleeAnalysisList = methodVsAnalysisResult.get(callSiteInfo.getCallee());
          List<Analysis> callerAnalysisList =
              methodVsAnalysisResult.get(propertyAnalysisResult.getMethodWrapper());

          List<Analysis> newAnalysisList =
              buildNewPropertyAnalysisList(
                  callSiteInfo,
                  methodSlicingResult.getAnalysisResult(),
                  propertyAnalysisResult,
                  calleeAnalysisList);

          if (callerAnalysisList == null) {
            callerAnalysisList = new ArrayList<>();
            methodVsAnalysisResult.put(
                propertyAnalysisResult.getMethodWrapper(), callerAnalysisList);
          }

          callerAnalysisList.addAll(newAnalysisList);

          runBackwardSlicingAnalysis(
              propertyAnalysisResult.getMethodWrapper(),
              propertyAnalysisResult.getInfluencingParams(),
              methodVsAnalysisResult,
              slicingCriteriaMap,
              checker);
        }
      }
    }

    List<Analysis> calleeAnalysisList = methodVsAnalysisResult.get(callSiteInfo.getCallee());
    List<Analysis> callerAnalysisList = methodVsAnalysisResult.get(callSiteInfo.getCaller());

    List<Analysis> newAnalysisList =
        buildNewAnalysisList(
            callSiteInfo, methodSlicingResult.getAnalysisResult(), calleeAnalysisList);

    if (callerAnalysisList == null) {
      callerAnalysisList = new ArrayList<>();
      methodVsAnalysisResult.put(callSiteInfo.getCaller(), callerAnalysisList);
    }

    callerAnalysisList.addAll(newAnalysisList);

    runBackwardSlicingAnalysis(
        callSiteInfo.getCaller(),
        methodSlicingResult.getInfluencingParameters(),
        methodVsAnalysisResult,
        slicingCriteriaMap,
        checker);
  }

  private static List<Analysis> buildNewPropertyAnalysisList(
      MethodCallSiteInfo callSiteInfo,
      List<UnitContainer> methodSlicingResult,
      PropertyAnalysisResult slicingResult,
      List<Analysis> calleeAnalysisList) {

    List<Analysis> newAnalysisList = new ArrayList<>();

    if (calleeAnalysisList != null && !calleeAnalysisList.isEmpty()) {
      for (Analysis analysis : calleeAnalysisList) {
        Analysis newAnalysis = new Analysis();

        String newChain =
            callSiteInfo.getCaller()
                + "["
                + callSiteInfo.getLineNumber()
                + "]"
                + "--->"
                + callSiteInfo.getCallee()
                + "--->"
                + analysis.getMethodChain();
        newAnalysis.setMethodChain(newChain);
        newAnalysis.setAnalysisResult(analysis.getAnalysisResult());
        newAnalysis.getAnalysisResult().addAll(methodSlicingResult);
        newAnalysis.getAnalysisResult().addAll(slicingResult.getSlicingResult());

        for (String key : slicingResult.getPropertyUseMap().keySet()) {
          for (PropertyAnalysisResult res : slicingResult.getPropertyUseMap().get(key))
            newAnalysis.getAnalysisResult().addAll(res.getSlicingResult());
        }

        newAnalysisList.add(newAnalysis);
      }
    } else {
      Analysis newAnalysis = new Analysis();

      String newChain =
          callSiteInfo.getCaller()
              + "["
              + callSiteInfo.getLineNumber()
              + "]"
              + "--->"
              + callSiteInfo.getCallee();
      newAnalysis.setMethodChain(newChain);
      newAnalysis.setAnalysisResult(new ArrayList<>());
      newAnalysis.getAnalysisResult().addAll(methodSlicingResult);
      newAnalysis.getAnalysisResult().addAll(slicingResult.getSlicingResult());

      for (String key : slicingResult.getPropertyUseMap().keySet()) {
        for (PropertyAnalysisResult res : slicingResult.getPropertyUseMap().get(key))
          newAnalysis.getAnalysisResult().addAll(res.getSlicingResult());
      }

      newAnalysisList.add(newAnalysis);
    }

    return newAnalysisList;
  }

  private static List<Analysis> buildNewAnalysisList(
      MethodCallSiteInfo callSiteInfo,
      List<UnitContainer> slicingResult,
      List<Analysis> calleeAnalysisList) {
    List<Analysis> newAnalysisList = new ArrayList<>();

    if (calleeAnalysisList != null && !calleeAnalysisList.isEmpty()) {
      for (Analysis analysis : calleeAnalysisList) {
        Analysis newAnalysis = new Analysis();

        String newChain =
            callSiteInfo.getCaller()
                + "["
                + callSiteInfo.getLineNumber()
                + "]"
                + "--->"
                + analysis.getMethodChain();
        newAnalysis.setMethodChain(newChain);

        newAnalysis.setAnalysisResult(analysis.getAnalysisResult());
        newAnalysis.getAnalysisResult().addAll(slicingResult);
        newAnalysisList.add(newAnalysis);
      }
    } else {
      Analysis newAnalysis = new Analysis();

      String newChain =
          callSiteInfo.getCaller()
              + "["
              + callSiteInfo.getLineNumber()
              + "]"
              + "--->"
              + callSiteInfo.getCallee();
      newAnalysis.setMethodChain(newChain);
      newAnalysis.setAnalysisResult(new ArrayList<>());
      newAnalysis.getAnalysisResult().addAll(slicingResult);
      newAnalysisList.add(newAnalysis);
    }

    return newAnalysisList;
  }

  private static MethodSlicingResult getInfluencingInstructions(
      MethodCallSiteInfo methodCallSiteInfo, List<Integer> slicingParams, SootMethod m) {
    Body b = m.retrieveActiveBody();

    UnitGraph graph = new ExceptionalUnitGraph(b);
    MethodInfluenceInstructions vbe =
        new MethodInfluenceInstructions(graph, methodCallSiteInfo, slicingParams);

    return vbe.getMethodSlicingResult();
  }
}

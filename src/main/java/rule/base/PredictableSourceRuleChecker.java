/* Licensed under GPL-3.0 */
package rule.base;

import analyzer.backward.Analysis;
import analyzer.backward.AssignInvokeUnitContainer;
import analyzer.backward.InvokeUnitContainer;
import analyzer.backward.UnitContainer;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.RValueBox;
import util.Utils;

/**
 * Created by RigorityJTeam on 11/26/17.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public abstract class PredictableSourceRuleChecker extends BaseRuleChecker {

  /** Constant <code>PREDICTABLE_SOURCES</code> */
  public static final List<String> PREDICTABLE_SOURCES = new ArrayList<>();

  static {
    PREDICTABLE_SOURCES.add("<java.lang.System: long nanoTime()>");
    PREDICTABLE_SOURCES.add("<java.lang.System: long currentTimeMillis()>");
    PREDICTABLE_SOURCES.add("<java.util.Date: java.util.Date <init>");
  }

  // Todo: Add a field to keep track of all the predictable sources ...

  private final String rule = getRuleId();
  private final String ruleDesc = RULE_VS_DESCRIPTION.get(rule);
  private Map<UnitContainer, List<String>> predictableSourcMap = new HashMap<>();
  private Map<UnitContainer, List<String>> othersSourceMap = new HashMap<>();

  /** {@inheritDoc} */
  @Override
  public void analyzeSlice(Analysis analysis) {

    if (analysis.getAnalysisResult().isEmpty()) {
      return;
    }

    HashMap<String, List<String>> callerVsUsedConstants = new HashMap<>();

    for (int index = 0; index < analysis.getAnalysisResult().size(); index++) {

      UnitContainer e = analysis.getAnalysisResult().get(index);

      if (!(e instanceof AssignInvokeUnitContainer) && e.getUnit() instanceof JAssignStmt) {

        List<String> usedConstants = callerVsUsedConstants.get(e.getMethod());

        if (usedConstants == null) {
          usedConstants = new ArrayList<>();
          callerVsUsedConstants.put(e.getMethod(), usedConstants);
        }

        if (e.getUnit().toString().contains("interfaceinvoke ")) {
          for (ValueBox usebox : e.getUnit().getUseBoxes()) {
            if (usebox.getValue() instanceof Constant) {
              usedConstants.add(usebox.getValue().toString());
            }
          }
        }
      }

      Map<UnitContainer, String> outSet = new HashMap<>();

      if (e instanceof AssignInvokeUnitContainer) {
        List<UnitContainer> resFromInside = ((AssignInvokeUnitContainer) e).getAnalysisResult();
        checkPredictableSourceFromInside(resFromInside, e, outSet);

      } else if (e instanceof InvokeUnitContainer) {
        List<UnitContainer> resFromInside = ((InvokeUnitContainer) e).getAnalysisResult();
        checkPredictableSourceFromInside(resFromInside, e, outSet);
      } else {
        for (String predictableSource : PREDICTABLE_SOURCES) {
          if (e.getUnit().toString().contains(predictableSource)) {
            outSet.put(e, e.toString());
            break;
          }
        }
      }

      if (e instanceof AssignInvokeUnitContainer) {
        List<UnitContainer> result = ((AssignInvokeUnitContainer) e).getAnalysisResult();

        if (result != null) {
          for (UnitContainer unit : result) {
            checkHeuristics(unit, outSet);
          }
        }

      } else if (e instanceof InvokeUnitContainer) {
        List<UnitContainer> result = ((InvokeUnitContainer) e).getAnalysisResult();
        if (result != null) {
          for (UnitContainer unit : result) {
            checkHeuristics(unit, outSet);
          }
        }

      } else {
        checkHeuristics(e, outSet);
      }

      if (outSet.isEmpty()) {
        continue;
      }

      UnitContainer invokeResult = Utils.isArgumentOfInvoke(analysis, index, new ArrayList<>());

      if (invokeResult != null && invokeResult instanceof InvokeUnitContainer) {
        if ((((InvokeUnitContainer) invokeResult).getDefinedFields().isEmpty()
                || !((InvokeUnitContainer) invokeResult).getArgs().isEmpty())
            && invokeResult.getUnit().toString().contains("specialinvoke")) {

          for (UnitContainer unitContainer : outSet.keySet()) {
            putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
          }
        }
      } else if (invokeResult != null && invokeResult.getUnit() instanceof JInvokeStmt) {
        if (invokeResult.getUnit().toString().contains("specialinvoke")) {

          for (UnitContainer unitContainer : outSet.keySet()) {
            putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
          }
        } else {

          for (UnitContainer unitContainer : outSet.keySet()) {
            if (unitContainer.getUnit() instanceof JInvokeStmt
                && unitContainer.getUnit().toString().contains("interfaceinvoke")) {

              boolean found = false;

              for (String constant : callerVsUsedConstants.get(e.getMethod())) {
                if (((JInvokeStmt) unitContainer.getUnit())
                    .getInvokeExpr()
                    .getArg(0)
                    .toString()
                    .contains(constant)) {
                  putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
                  found = true;
                  break;
                }
              }

              if (!found) {
                putIntoMap(othersSourceMap, unitContainer, outSet.get(unitContainer));
              }

            } else {
              putIntoMap(othersSourceMap, unitContainer, outSet.get(unitContainer));
            }
          }
        }
      } else {

        for (UnitContainer unitContainer : outSet.keySet()) {

          putIntoMap(predictableSourcMap, unitContainer, outSet.get(unitContainer));
        }
      }
    }
  }

  private void checkPredictableSourceFromInside(
      List<UnitContainer> result, UnitContainer e, Map<UnitContainer, String> outSet) {
    for (UnitContainer key : result) {

      if (key instanceof AssignInvokeUnitContainer) {
        checkPredictableSourceFromInside(
            ((AssignInvokeUnitContainer) key).getAnalysisResult(), key, outSet);
        continue;
      }

      if (key instanceof InvokeUnitContainer) {
        checkPredictableSourceFromInside(
            ((InvokeUnitContainer) key).getAnalysisResult(), key, outSet);
        continue;
      }

      for (String predictableSource : PREDICTABLE_SOURCES) {
        if (key.getUnit().toString().contains(predictableSource)) {
          outSet.put(e, e.toString());
        }
      }
    }
  }

  private void checkHeuristics(UnitContainer e, Map<UnitContainer, String> outSet) {

    if (e instanceof AssignInvokeUnitContainer) {
      for (UnitContainer u : ((AssignInvokeUnitContainer) e).getAnalysisResult()) {
        checkHeuristics(u, outSet);
      }
      return;
    }

    if (e instanceof InvokeUnitContainer) {
      for (UnitContainer u : ((InvokeUnitContainer) e).getAnalysisResult()) {
        checkHeuristics(u, outSet);
      }
      return;
    }

    for (ValueBox usebox : e.getUnit().getUseBoxes()) {
      if (usebox.getValue() instanceof Constant) {

        if (usebox.getValue().toString().equals("null")
            || usebox.getValue().toString().equals("\"null\"")
            || usebox.getValue().toString().equals("\"\"")
            || usebox.getValue().toString().contains(" = class ")) {
          putIntoMap(othersSourceMap, e, usebox.getValue().toString());
          continue;
        }

        if (e.getUnit() instanceof JAssignStmt) {
          if (((AssignStmt) e.getUnit()).containsInvokeExpr()) {
            InvokeExpr invokeExpr = ((AssignStmt) e.getUnit()).getInvokeExpr();
            List<Value> args = invokeExpr.getArgs();
            for (Value arg : args) {
              if (arg.equivTo(usebox.getValue())) {
                putIntoMap(othersSourceMap, e, usebox.getValue().toString());
                break;
              }
            }
          } else if (usebox.getValue().getType() instanceof IntegerType) {

            List<ValueBox> defBoxes = e.getUnit().getDefBoxes();

            if (defBoxes != null && !defBoxes.isEmpty()) {
              if (usebox instanceof RValueBox
                  && (defBoxes.get(0).getValue().getType() instanceof ByteType
                      || defBoxes.get(0).getValue().getType() instanceof CharType)) {
                outSet.put(e, usebox.getValue().toString());
              } else {
                putIntoMap(othersSourceMap, e, usebox.getValue().toString());
              }
            }

          } else {
            if (usebox.getValue().getType() instanceof BooleanType
                || usebox.getValue().getType() instanceof FloatType
                || usebox.getValue().getType() instanceof DoubleType) {
              putIntoMap(othersSourceMap, e, usebox.getValue().toString());
            } else {
              outSet.put(e, usebox.getValue().toString());
            }
          }
        } else if (e.getUnit().toString().contains(" newarray ")) {
          putIntoMap(othersSourceMap, e, usebox.getValue().toString());
        } else {
          if (usebox.getValue().getType() instanceof LongType
              || usebox.getValue().toString().startsWith("\"")) {
            outSet.put(e, usebox.getValue().toString());
          }
        }
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void createAnalysisOutput(
      Map<String, String> xmlFileStr, List<String> sourcePaths, OutputStructure output)
      throws ExceptionHandler {
    Utils.createAnalysisOutput(xmlFileStr, sourcePaths, predictableSourcMap, rule, output);
  }

  /**
   * getRuleId.
   *
   * @return a {@link java.lang.String} object.
   */
  public abstract String getRuleId();
}

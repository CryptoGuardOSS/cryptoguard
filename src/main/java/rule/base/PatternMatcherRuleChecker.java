/* Licensed under GPL-3.0 */
package rule.base;

import analyzer.backward.Analysis;
import analyzer.backward.AssignInvokeUnitContainer;
import analyzer.backward.InvokeUnitContainer;
import analyzer.backward.UnitContainer;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import soot.ValueBox;
import soot.jimple.Constant;
import util.Utils;

/**
 * Abstract PatternMatcherRuleChecker class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since V01.00.00
 */
public abstract class PatternMatcherRuleChecker extends BaseRuleChecker {

  //Todo: Add a field to keep track of all the found patterns ...

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

    for (UnitContainer e : analysis.getAnalysisResult()) {

      if (e instanceof AssignInvokeUnitContainer) {
        List<UnitContainer> resFromInside = ((AssignInvokeUnitContainer) e).getAnalysisResult();

        for (UnitContainer unit : resFromInside) {
          checkForMatch(unit);
        }
      }

      if (e instanceof InvokeUnitContainer) {
        List<UnitContainer> resFromInside = ((InvokeUnitContainer) e).getAnalysisResult();

        for (UnitContainer unit : resFromInside) {
          checkForMatch(unit);
        }
      }

      checkForMatchInternal(e);
    }
  }

  private void checkForMatch(UnitContainer e) {

    if (e instanceof AssignInvokeUnitContainer) {
      List<UnitContainer> resFromInside = ((AssignInvokeUnitContainer) e).getAnalysisResult();

      for (UnitContainer unit : resFromInside) {
        checkForMatch(unit);
      }
    }

    if (e instanceof InvokeUnitContainer) {
      List<UnitContainer> resFromInside = ((InvokeUnitContainer) e).getAnalysisResult();

      for (UnitContainer unit : resFromInside) {
        checkForMatch(unit);
      }
    }

    checkForMatchInternal(e);
  }

  private void checkForMatchInternal(UnitContainer e) {
    for (ValueBox usebox : e.getUnit().getUseBoxes()) {
      if (usebox.getValue() instanceof Constant) {
        boolean found = false;

        for (String regex : getPatternsToMatch()) {
          if (Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
              .matcher(usebox.getValue().toString())
              .matches()) {
            putIntoMap(predictableSourcMap, e, usebox.getValue().toString());
            found = true;
            break;
          }
        }

        if (!found) {
          putIntoMap(othersSourceMap, e, usebox.getValue().toString());
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
   * getPatternsToMatch.
   *
   * @return a {@link java.util.List} object.
   */
  public abstract List<String> getPatternsToMatch();

  /**
   * getRuleId.
   *
   * @return a {@link java.lang.String} object.
   */
  public abstract String getRuleId();
}

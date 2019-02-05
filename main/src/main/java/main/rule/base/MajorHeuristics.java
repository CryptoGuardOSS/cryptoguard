package main.rule.base;

import main.analyzer.backward.Analysis;
import main.analyzer.backward.ParamFakeUnitContainer;
import main.analyzer.backward.PropertyFakeUnitContainer;
import main.analyzer.backward.UnitContainer;
import main.util.Utils;
import soot.ArrayType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>MajorHeuristics class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class MajorHeuristics {

    /**
     * <p>isArgumentOfInvoke.</p>
     *
     * @param analysis a {@link main.analyzer.backward.Analysis} object.
     * @param index    a int.
     * @param outSet   a {@link java.util.List} object.
     * @return a boolean.
     */
    public static boolean isArgumentOfInvoke(Analysis analysis, int index, List<UnitContainer> outSet) {
        for (int i = index; i >= 0; i--) {

            UnitContainer curUnit = analysis.getAnalysisResult().get(i);

            List<UnitContainer> inset = new ArrayList<>();
            inset.addAll(outSet);

            for (UnitContainer insetIns : inset) {
                if (insetIns instanceof PropertyFakeUnitContainer) {
                    String property = ((PropertyFakeUnitContainer) insetIns).getOriginalProperty();
                    if (curUnit.getUnit() instanceof JAssignStmt) {
                        for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                            if (useBox.getValue().toString().contains(property)) {
                                if (((JAssignStmt) curUnit.getUnit()).containsInvokeExpr()) {
                                    InvokeExpr invokeExpr = ((JAssignStmt) curUnit.getUnit()).getInvokeExpr();
                                    List<Value> args = invokeExpr.getArgs();
                                    for (Value arg : args) {
                                        if (arg.toString().contains(property)) {
                                            return true;
                                        }
                                    }
                                } else {
                                    if (!outSet.toString().contains(curUnit.toString())) {
                                        outSet.add(curUnit);
                                    }
                                }
                            }
                        }
                    } else if (Utils.isSpecialInvokeOn(curUnit.getUnit(), property)) {
                        if (!outSet.toString().contains(curUnit.toString())) {
                            outSet.add(curUnit);
                        }
                    }
                } else if (insetIns instanceof ParamFakeUnitContainer) {

                    int param = ((ParamFakeUnitContainer) insetIns).getParam();
                    String method = ((ParamFakeUnitContainer) insetIns).getCallee();

                    for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                        String useboxStr = useBox.getValue().toString();
                        if (useboxStr.contains("@parameter")) {
                            Integer parameter = Integer.valueOf(useboxStr.substring("@parameter".length(), useboxStr.indexOf(':')));
                            if (parameter.equals(param) && curUnit.getMethod().equals(method)) {
                                if (!outSet.toString().contains(curUnit.toString())) {
                                    outSet.add(curUnit);
                                }
                            }
                        }
                    }
                } else if (insetIns.getUnit() instanceof JInvokeStmt) {
                    if (curUnit.getUnit() instanceof JAssignStmt) {
                        for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                            if (((JAssignStmt) curUnit.getUnit()).containsInvokeExpr()) {
                                InvokeExpr invokeExpr = ((JAssignStmt) curUnit.getUnit()).getInvokeExpr();
                                List<Value> args = invokeExpr.getArgs();
                                for (Value arg : args) {
                                    if (Utils.isSpecialInvokeOn(insetIns.getUnit(), arg.toString())) {
                                        return true;
                                    }
                                }
                            } else if (Utils.isSpecialInvokeOn(insetIns.getUnit(), useBox.getValue().toString())) {
                                if (!outSet.toString().contains(curUnit.toString())) {
                                    outSet.add(curUnit);
                                }
                            }
                        }
                    } else {
                        for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                            if (Utils.isSpecialInvokeOn(insetIns.getUnit(), useBox.getValue().toString())) {
                                if (!outSet.toString().contains(curUnit.toString())) {
                                    outSet.add(curUnit);
                                }
                            }
                        }
                    }

                } else {
                    if (curUnit.getUnit() instanceof JAssignStmt) {
                        for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {
                            for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                                if (((JAssignStmt) curUnit.getUnit()).containsInvokeExpr()) {
                                    InvokeExpr invokeExpr = ((JAssignStmt) curUnit.getUnit()).getInvokeExpr();
                                    List<Value> args = invokeExpr.getArgs();
                                    for (Value arg : args) {
                                        if (arg.equivTo(defBox.getValue())
                                                || isArrayUseBox(curUnit, insetIns, defBox, arg)) {
                                            return true;
                                        }
                                    }
                                } else if (useBox.getValue().equivTo(defBox.getValue())
                                        || isArrayUseBox(curUnit, insetIns, defBox, useBox.getValue())) {
                                    if (!outSet.toString().contains(curUnit.toString())) {
                                        outSet.add(curUnit);
                                    }
                                }
                            }
                        }

                    } else {
                        for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {
                            for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                                if (defBox.getValue().equivTo(useBox.getValue())
                                        || isArrayUseBox(curUnit, insetIns, defBox, useBox.getValue())) {
                                    if (!outSet.toString().contains(curUnit.toString())) {
                                        outSet.add(curUnit);
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }

        return false;
    }

    private static boolean isArrayUseBox(UnitContainer curUnit, UnitContainer insetIns, ValueBox defBox, Value useBox) {
        return (defBox.getValue().toString().contains(useBox.toString())
                && curUnit.getMethod().equals(insetIns.getMethod())
                && useBox.getType() instanceof ArrayType);
    }

    /**
     * <p>isArgumentOfByteArrayCreation.</p>
     *
     * @param analysis a {@link main.analyzer.backward.Analysis} object.
     * @param index    a int.
     * @param outSet   a {@link java.util.List} object.
     * @return a boolean.
     */
    public static boolean isArgumentOfByteArrayCreation(Analysis analysis, int index, List<UnitContainer> outSet) {

        for (int i = index; i >= 0; i--) {
            UnitContainer curUnit = analysis.getAnalysisResult().get(i);

            List<UnitContainer> inset = new ArrayList<>();
            inset.addAll(outSet);

            for (UnitContainer insetIns : inset) {
                if (insetIns instanceof PropertyFakeUnitContainer) {
                    String property = ((PropertyFakeUnitContainer) insetIns).getOriginalProperty();

                    for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                        if (useBox.getValue().toString().contains(property)) {
                            if (!outSet.toString().contains(curUnit.toString())) {
                                outSet.add(curUnit);
                            }
                        }

                        if (useBox.getValue().getType() instanceof ArrayType) {
                            for (ValueBox arg : useBox.getValue().getUseBoxes()) {
                                if (arg.getValue().toString().contains(property)) {
                                    return true;
                                }
                            }
                        }
                    }

                } else if (insetIns instanceof ParamFakeUnitContainer) {

                    int param = ((ParamFakeUnitContainer) insetIns).getParam();
                    String method = ((ParamFakeUnitContainer) insetIns).getCallee();

                    for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                        String useboxStr = useBox.getValue().toString();
                        if (useboxStr.contains("@parameter")) {
                            Integer parameter = Integer.valueOf(useboxStr.substring("@parameter".length(), useboxStr.indexOf(':')));
                            if (parameter.equals(param) && curUnit.getMethod().equals(method)) {
                                if (!outSet.toString().contains(curUnit.toString())) {
                                    outSet.add(curUnit);
                                }
                            }
                        }
                    }
                } else {
                    for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {

                        if (curUnit.getUnit().toString().contains(" newarray ")) {
                            for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {

                                if (useBox.getValue().equivTo(defBox.getValue())) {
                                    return true;
                                }
                            }
                        }

                        for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                            if (defBox.getValue().equivTo(useBox.getValue())) {
                                if (!outSet.toString().contains(curUnit.toString())) {
                                    outSet.add(curUnit);
                                }

                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}

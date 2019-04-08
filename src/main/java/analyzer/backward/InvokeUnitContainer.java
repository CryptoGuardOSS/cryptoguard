package analyzer.backward;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>InvokeUnitContainer class.</p>
 *
 * @author drmonster
 * @version $Id: $Id
 */
public class InvokeUnitContainer extends UnitContainer {

    private List<Integer> args = new ArrayList<>();
    private Set<String> definedFields = new HashSet<>();
    private List<UnitContainer> analysisResult = new ArrayList<>();

    /**
     * <p>Getter for the field <code>args</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getArgs() {
        return args;
    }

    /**
     * <p>Setter for the field <code>args</code>.</p>
     *
     * @param args a {@link java.util.List} object.
     */
    public void setArgs(List<Integer> args) {
        this.args = args;
    }

    /**
     * <p>Getter for the field <code>definedFields</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getDefinedFields() {
        return definedFields;
    }

    /**
     * <p>Setter for the field <code>definedFields</code>.</p>
     *
     * @param definedFields a {@link java.util.Set} object.
     */
    public void setDefinedFields(Set<String> definedFields) {
        this.definedFields = definedFields;
    }

    /**
     * <p>Getter for the field <code>analysisResult</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<UnitContainer> getAnalysisResult() {
        return analysisResult;
    }

    /**
     * <p>Setter for the field <code>analysisResult</code>.</p>
     *
     * @param analysisResult a {@link java.util.List} object.
     */
    public void setAnalysisResult(List<UnitContainer> analysisResult) {
        this.analysisResult = analysisResult;
    }
}

package main.analyzer.backward;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>AssignInvokeUnitContainer class.</p>
 *
 * @author drmonster
 * @version $Id: $Id
 */
public class AssignInvokeUnitContainer extends UnitContainer {
    private List<Integer> args = new ArrayList<>();
    private Set<String> properties = new HashSet<>();
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
     * <p>Getter for the field <code>properties</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getProperties() {
        return properties;
    }

    /**
     * <p>Setter for the field <code>properties</code>.</p>
     *
     * @param properties a {@link java.util.Set} object.
     */
    public void setProperties(Set<String> properties) {
        this.properties = properties;
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

package frontEnd.MessagingSystem.routing.outputStructures.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

/**
 * <p>Heuristics class.</p>
 *
 * @author maister
 * @version 03.10.04
 */
@ToString
public class Heuristics {

    @Getter @Setter
    private int numberOfOrthogonal = 0;
    @Getter @Setter
    private int numberOfConstantsToCheck = 0;
    @Getter @Setter
    private int numberOfHeuristics = 0;
    @Getter @Setter
    private int numberOfSlices = 0;
    @Getter @Setter
    private double sliceAverage = 0;
    private ArrayList<String> depthCount;

    /**
     * <p>Getter for the field <code>depthCount</code>.</p>
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<String> getDepthCount() {
        if (this.depthCount == null)
            this.depthCount = new ArrayList<>();

        return this.depthCount;
    }

    /**
     * <p>Setter for the field <code>depthCount</code>.</p>
     *
     * @param depthCount a {@link java.util.ArrayList} object.
     */
    public void setDepthCount(ArrayList<String> depthCount) {
        this.depthCount = depthCount;
    }

    /**
     * <p>addDepthCount.</p>
     *
     * @param depth a {@link java.lang.String} object.
     */
    public void addDepthCount(String depth) {
        this.getDepthCount().add(depth);
    }

    /**
     * <p>getDefaultHeuristics.</p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
     */
    public frontEnd.MessagingSystem.routing.structure.Default.Heuristics getDefaultHeuristics() {
        frontEnd.MessagingSystem.routing.structure.Default.Heuristics output = new frontEnd.MessagingSystem.routing.structure.Default.Heuristics();

        output.setNumberOfOrthogonal(this.numberOfOrthogonal);
        output.setNumberOfConstantsToCheck(this.numberOfConstantsToCheck);
        output.setNumberOfSlices(this.numberOfSlices);
        output.setNumberOfHeuristics(this.numberOfHeuristics);
        output.setAverageSlice(this.sliceAverage);
        output.setDepthCount(this.getDepthCount());

        return output;
    }

    /**
     * <p>getScarfXMLHeuristics.</p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics} object.
     */
    public frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics getScarfXMLHeuristics() {
        frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics output = new frontEnd.MessagingSystem.routing.structure.Scarf.Heuristics();

        output.setNumberOfOrthogonal(this.numberOfOrthogonal);
        output.setNumberOfConstantsToCheck(this.numberOfConstantsToCheck);
        output.setNumberOfSlices(this.numberOfSlices);
        output.setNumberOfHeuristics(this.numberOfHeuristics);
        output.setAverageSlice(this.sliceAverage);
        output.setDepthCount(this.getDepthCount());

        return output;
    }
}

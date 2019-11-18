package frontEnd.MessagingSystem.routing.outputStructures.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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

    public ArrayList<String> getDepthCount() {
        if (this.depthCount == null)
            this.depthCount = new ArrayList<>();

        return this.depthCount;
    }

    public void setDepthCount(ArrayList<String> depthCount) {
        this.depthCount = depthCount;
    }

    public void addDepthCount(String depth) {
        this.getDepthCount().add(depth);
    }

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

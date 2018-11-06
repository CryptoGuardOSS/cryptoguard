package main.frontEnd;

import main.rule.engine.EngineType;

/**
 * The class containing the implementation of the legacy output.
 * <p>STATUS: IC</p>
 * @author RigorityJTeam
 * @since 1.0
 */
public class LegacyOutput extends MessageRepresentation {

    /**
     * The constructor of the current type of output.
     *
     *  @param type the type of engine to be used for the processing
     *  @param source the name of the source being examined
     * */
    public LegacyOutput(String source, EngineType type)
    {
        super(source,type);
    }

    /***
     *  The overridden method for the Legacy output. Currently mimics the output as best seen.
     *
     * @return Object nothing is returned in legacy as legacy only prints
     * information out to the console
     */
    @Override
    public Object getOutput()
    {
        System.out.println("Analyzing "+this.getType()+": "+this.getSource());
        for (AnalysisRule rule:this.getAnalysisIssues())
        {
            System.out.println("=======================================");
            System.out.println("***Violated Rule "+rule.getRuleNumber()+": "+rule.getRuleType());
            if (rule.getIssues().size() > 0 )
            {
                for (AnalysisIssue issue : rule.getIssues())
                {
                    StringBuilder message = new StringBuilder("***Found: ");

                    message.append("["+issue.getCapturedInformation()+"] ");
                    if (issue.getLineNumber() != null)
                        message.append("in line "+issue.getLineNumber()+" ");
                    message.append("in Method: "+issue.getMethod());

                    System.out.println(message.toString());
                }
            }
            else
                System.out.println("***"+rule.getMessage());
            System.out.println("=======================================");
        }
        return null;
    }
}

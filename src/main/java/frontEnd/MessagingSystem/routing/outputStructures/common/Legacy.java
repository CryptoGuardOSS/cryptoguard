package frontEnd.MessagingSystem.routing.outputStructures.common;

import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.AnalysisLocation;
import org.apache.commons.lang3.StringUtils;
import rule.engine.EngineType;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Legacy class.</p>
 *
 * @author RigorityJTeam
 * Created on 3/2/19.
 * @version $Id: $Id
 * @since 03.03.00
 *
 * <p>The common utilities class for Legacy marshalling.</p>
 */
public class Legacy {
    /**
     * <p>marshallingHeader.</p>
     *
     * @param sourceType a {@link rule.engine.EngineType} object.
     * @param sources    a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    public static String marshallingHeader(EngineType sourceType, List<String> sources) {
        StringBuilder out = new StringBuilder();

        out.append("Analyzing " + sourceType.getName() + ": ");
        for (int sourceKtr = 0; sourceKtr < sources.size(); sourceKtr++) {
            out.append(sources.get(sourceKtr));

            if (sourceKtr != sources.size() - 1)
                out.append(",");
        }
        out.append("\n");

        return out.toString();
    }

    /**
     * <p>marshallingSootErrors.</p>
     *
     * @param soot a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String marshallingSootErrors(String soot) {
        if (StringUtils.isBlank(soot))
            return "";

        StringBuilder sootOut = new StringBuilder();

        sootOut.append("=======================================\n");
        sootOut.append("Internal Warnings: \n" + soot + "\n");
        sootOut.append("=======================================\n");

        return sootOut.toString();
    }

    /**
     * <p>marshalling.</p>
     *
     * @param issue      a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
     * @param sourceType a {@link rule.engine.EngineType} object.
     * @return a {@link java.lang.String} object.
     */
    public static String marshalling(AnalysisIssue issue, EngineType sourceType) {
        StringBuilder out = new StringBuilder();

        if (StringUtils.isNotBlank(issue.getClassName())) {
            out.append("***");
            if (!issue.getInfo().equals("UNKNOWN"))
                out.append(issue.getInfo());
            else
                out.append(issue.getRule().getDesc());
        } else {
            out.append("***Found: ");
            out.append("[\"" + issue.getInfo() + "\"] ");
        }

        //region Location Setting
        String lines = null;
        if (issue.getLocations().size() > 0) {

            List<AnalysisLocation> issueLocations = new ArrayList<>();
            for (AnalysisLocation loc : issue.getLocations())
                if (loc.getMethodNumber() == issue.getMethods().size() - 1)
                    issueLocations.add(loc);

            if (!issueLocations.isEmpty() && !issueLocations.toString().contains("-1"))
                lines = ":" + issueLocations.toString().replace("[", "").replace("]", "");

        }

        out.append(" in ").append(issue.getClassName());

        if (sourceType.equals(EngineType.DIR) || sourceType.equals(EngineType.JAVAFILES))
            out.append(".java");
        else if (sourceType.equals(EngineType.CLASSFILES))
            out.append(".class");

        out.append("::").append(issue.getMethods().pop());

        if (lines != null)
            out.append(lines);

        out.append(".");
        //endregion

        //endregion
        out.append("\n");

        return out.toString();
    }

    /**
     * <p>marshallingShowTimes.</p>
     *
     * @param analysisTime a {@link java.lang.Long} object.
     * @return a {@link java.lang.String} object.
     */
    public static String marshallingShowTimes(Long analysisTime) {
        StringBuilder out = new StringBuilder();

        out.append("=======================================\n");
        out.append("Analysis Timing (ms): ").append(analysisTime).append(".\n");
        out.append("=======================================\n");

        return out.toString();
    }
}

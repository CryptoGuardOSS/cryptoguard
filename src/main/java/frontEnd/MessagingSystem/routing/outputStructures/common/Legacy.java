/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures.common;

import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.AnalysisLocation;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import rule.engine.EngineType;

/**
 * Legacy class.
 *
 * @author CryptoguardTeam Created on 3/2/19.
 * @version 03.07.01
 * @since 03.03.00
 *     <p>The common utilities class for Legacy marshalling.
 */
public class Legacy {
  /**
   * marshallingHeader.
   *
   * @param sourceType a {@link rule.engine.EngineType} object.
   * @param sources a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   */
  public static String marshallingHeader(EngineType sourceType, List<String> sources) {
    StringBuilder out = new StringBuilder();

    out.append("Analyzing ").append(sourceType.getName()).append(": ");
    for (int sourceKtr = 0; sourceKtr < sources.size(); sourceKtr++) {
      out.append(sources.get(sourceKtr));

      if (sourceKtr != sources.size() - 1) out.append(",");
    }
    out.append("\n");

    return out.toString();
  }

  /**
   * marshallingSootErrors.
   *
   * @param soot a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String marshallingSootErrors(String soot) {
    if (StringUtils.isBlank(soot)) return "";

    String sootOut =
        "=======================================\n"
            + "Internal Warnings: \n"
            + soot
            + "\n"
            + "=======================================\n";
    return sootOut;
  }

  /**
   * marshalling.
   *
   * @param issue a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
   * @param sourceType a {@link rule.engine.EngineType} object.
   * @return a {@link java.lang.String} object.
   */
  public static String marshalling(AnalysisIssue issue, EngineType sourceType) {
    StringBuilder out = new StringBuilder();

    if (StringUtils.isNotBlank(issue.getClassName())) {
      out.append("***");
      if (!issue.getInfo().equals("UNKNOWN")) out.append(issue.getInfo());
      else out.append(issue.getRule().getDesc());
    } else {
      out.append("***Found: ");
      out.append("[\"").append(issue.getInfo()).append("\"] ");
    }

    //region Location Setting
    String lines = null;
    if (issue.getLocations().size() > 0) {

      List<AnalysisLocation> issueLocations = new ArrayList<>();
      for (AnalysisLocation loc : issue.getLocations())
        if (loc.getMethodNumber() == issue.getMethods().size() - 1) issueLocations.add(loc);

      if (!issueLocations.isEmpty() && !issueLocations.toString().contains("-1"))
        lines = ":" + issueLocations.toString().replace("[", "").replace("]", "");
    }

    out.append(" in ").append(issue.getClassName());

    if (sourceType.equals(EngineType.DIR) || sourceType.equals(EngineType.JAVAFILES))
      out.append(".java");
    else if (sourceType.equals(EngineType.CLASSFILES)) out.append(".class");

    out.append("::").append(issue.getMethods().pop());

    if (lines != null) out.append(lines);

    out.append(".");
    //endregion

    //endregion
    out.append("\n");

    return out.toString();
  }

  /**
   * marshallingShowTimes.
   *
   * @param analysisTime a {@link java.lang.Long} object.
   * @return a {@link java.lang.String} object.
   */
  public static String marshalling(Long analysisTime) {

    String out =
        "=======================================\n"
            + "Analysis Timing (ms): "
            + analysisTime
            + ".\n"
            + "=======================================\n";
    return out;
  }

  /**
   * marshalling.
   *
   * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   * @return a {@link java.lang.String} object.
   */
  public static String marshalling(EnvironmentInformation info) {

    StringBuilder out = new StringBuilder();

    out.append("Total Heuristics: ")
        .append(info.getHeuristics().getNumberOfHeuristics())
        .append("\n");
    out.append("Total Orthogonal: ")
        .append(info.getHeuristics().getNumberOfOrthogonal())
        .append("\n");
    out.append("Total Constants: ")
        .append(info.getHeuristics().getNumberOfConstantsToCheck())
        .append("\n");
    out.append("Total Slices: ").append(info.getHeuristics().getNumberOfSlices()).append("\n");
    out.append("Average Length: ")
        .append(info.getSLICE_AVERAGE_3SigFig())
        .append("\n")
        .append("\n");

    for (String depth : info.getHeuristics().getDepthCount()) out.append(depth).append("\n");

    return out.toString();
  }
}

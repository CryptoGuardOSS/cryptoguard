package main.frontEnd.MessagingSystem.routing.outputStructures.common;

import com.example.response.*;
import main.CWE_Reader.CWE;
import main.CWE_Reader.CWEList;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.AnalysisLocation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>ScarfXML class.</p>
 *
 * @author RigorityJTeam
 * Created on 3/2/19.
 * @version $Id: $Id
 * @since 03.03.00
 *
 * <p>The common utilities class for ScarfXML marshalling.</p>
 */
public class ScarfXML {

    /**
     * <p>marshalling.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @return a {@link com.example.response.AnalyzerReport} object.
     */
    public static AnalyzerReport marshalling(EnvironmentInformation info) {
        AnalyzerReport report = new AnalyzerReport();

        //region Setting Attributes
        report.setAssessFw(info.getAssessmentFramework());
        report.setAssessFwVersion(info.getAssessmentFrameworkVersion());
        report.setAssessmentStartTs(info.getAssessmentStartTime());
        report.setBuildFw(info.getBuildFramework());
        report.setBuildFwVersion(info.getBuildFrameworkVersion());
        report.setPackageName(info.getPackageName());
        report.setPackageVersion(info.getPackageVersion());
        report.setPackageRootDir(info.getPackageRootDir());
        report.setParserFw(info.getParserName());
        report.setParserFwVersion(info.getParserVersion());
        report.setPlatformName(info.getPlatformName());
        report.setToolName(info.getToolFramework());
        report.setToolVersion(info.getToolFrameworkVersion());
        report.setUuid(info.getUUID());
        report.setBuildRootDir(info.getBuildRootDir());
        //endregion

        return report;
    }

    /**
     * <p>marshalling.</p>
     *
     * @param issue    a {@link main.frontEnd.MessagingSystem.AnalysisIssue} object.
     * @param cwes     a {@link main.CWE_Reader.CWEList} object.
     * @param fileName a {@link java.lang.String} object.
     * @param id       a {@link java.lang.Integer} object.
     * @param buildId  a {@link java.lang.Integer} object.
     * @param xPath    a {@link java.lang.String} object.
     * @return a {@link com.example.response.BugInstanceType} object.
     */
    public static BugInstanceType marshalling(AnalysisIssue issue, CWEList cwes, String fileName, Integer id, Integer buildId, String xPath) {
        BugInstanceType instance = new BugInstanceType();

        //region Setting the instance

        instance.setId(id);
        instance.setBugCode(issue.getRuleId().toString());
        instance.setBugGroup(issue.getRule().getDesc());
        instance.setBugMessage(issue.getRule().getDesc());

        if (buildId != null || xPath != null) {
            BugTraceType trace = new BugTraceType();

            if (buildId != null)
                trace.setBuildId(buildId);

            if (xPath != null)
                trace.setAssessmentReportFile(xPath);

            instance.setBugTrace(trace);
        }

        for (CWE cwe : issue.getRule().retrieveCWEInfo(cwes))
            instance.getCweId().add(String.valueOf(cwe.getId()));

        if (StringUtils.isNotBlank(issue.getClassName())) {
            instance.setClassName(issue.getClassName());
        }

        //region Setting Methods If there are any, currently the first is the primary one
        if (!issue.getMethods().isEmpty()) {
            MethodsType methods = new MethodsType();
            for (int methodKtr = 0; methodKtr < issue.getMethods().size(); methodKtr++) {
                MethodType newMethod = new MethodType();

                newMethod.setId(methodKtr);
                newMethod.setPrimary(methodKtr == 0);
                newMethod.setValue((String) issue.getMethods().get(methodKtr));

                methods.getMethod().add(newMethod);
            }
            instance.setMethods(methods);
        }
        //endregion

        //region Setting Bug Locations
        BugLocationsType bugLocations = new BugLocationsType();
        if (!issue.getLocations().isEmpty()) {
            for (int locationKtr = 0; locationKtr < issue.getLocations().size(); locationKtr++) {
                LocationType newLocation = new LocationType();
                AnalysisLocation createdLoc = issue.getLocations().get(locationKtr);

                newLocation.setId(locationKtr);
                newLocation.setPrimary(locationKtr == 0);

                if (createdLoc.getLineStart() != -1)
                    newLocation.setStartLine(createdLoc.getLineStart());

                newLocation.setSourceFile(issue.getFullPathName());

                if (createdLoc.getLineEnd() > 0 && !createdLoc.getLineEnd().equals(createdLoc.getLineStart())) {
                    newLocation.setEndLine(createdLoc.getLineEnd());
                }

                bugLocations.getLocation().add(newLocation);
            }
        } else {
            LocationType newLocation = new LocationType();
            newLocation.setSourceFile(issue.getFullPathName());
            newLocation.setPrimary(true);
            bugLocations.getLocation().add(newLocation);
        }
        instance.setBugLocations(bugLocations);
        //endregion

        //region Setting Bug Message
        StringBuilder outputMessage = new StringBuilder();
        String info = StringUtils.trimToNull(issue.getInfo());

        if (info != null)
            outputMessage.append(info).append(". ");

        outputMessage.append(issue.getRule().getDesc()).append(".");

        instance.setBugMessage(outputMessage.toString());
        //endregion

        //region Setting BugTrace
        BugTraceType trace = new BugTraceType();

        if (buildId != null)
            trace.setBuildId(buildId);

        trace.setAssessmentReportFile(fileName);

        instance.setBugTrace(trace);
        //endregion

        //endregion

        return instance;
    }

    /**
     * The method for this output to create error messages
     *
     * @param message -  the string of the message to be sent to the output
     * @return string - the xml format of the error message
     */
    private String creatingErrorMessage(String message) {
        StringBuilder output = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        output.append("<ERROR>");
        output.append(message);
        output.append("</ERROR>");
        return output.toString();
    }
}

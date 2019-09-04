package frontEnd.MessagingSystem.routing.outputStructures.common;

import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.AnalysisLocation;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.structure.Default.*;
import rule.engine.EngineType;
import util.Utils;

/**
 * <p>Default class.</p>
 *
 * @author franceme
 * Created on 4/23/19.
 * @since 03.04.08
 *
 * <p>{Description Here}</p>
 */
public class Default {

    //region Attributes
    public static final Integer Version = 3;
    //endregion

    //region UnMarshallers
    public static Report mapper(EnvironmentInformation info) {
        Report report = new Report();

        report.setDateTime(info.getAssessmentStartTime());
        report.setProjectName(Utils.projectName);
        report.setProjectVersion(Utils.projectVersion);
        report.setSchemaVersion(Version);
        report.setUUID(info.getUUID());

        return report;
    }

    public static Target mapper(EnvironmentInformation info, String computerOS, String jvmInfo) {
        Target target = new Target();

        target.setComputerOS(computerOS);
        target.setFullPath(info.getPackageRootDir());
        target.setJVMVersion(jvmInfo);
        target.setProjectName(info.getTargetProjectName() == null ? info.getPackageName() : info.getTargetProjectName());
        target.setType(mapper(info.getSourceType()));

        if (info.getSourceType().equals(EngineType.DIR))
            target.setProjectType(mapper(info.getIsGradle()));
        target.setProjectVersion(info.getTargetProjectVersion());

        target.setPropertiesFilePath(info.getPropertiesFile());
        target.setRawCommand(info.getRawCommand());
        target.setTargetSources(info.getSource());

        return target;
    }

    public static Target.Type mapper(EngineType type) {
        switch (type) {
            case APK:
                return Target.Type.APK;
            case DIR:
                return Target.Type.SOURCE;
            case JAR:
                return Target.Type.JAR;
            case JAVAFILES:
                return Target.Type.JAVA;
            case CLASSFILES:
                return Target.Type.CLASS;
        }
        return Target.Type.JAR;
    }

    public static Target.ProjectType mapper(Boolean isGradle) {
        if (isGradle)
            return Target.ProjectType.GRADLE;
        else
            return Target.ProjectType.MAVEN;
    }


    public static Issue mapper(AnalysisIssue oldIssue, Integer id) {
        Issue issue = new Issue();

        issue.setId(String.valueOf(id));
        issue.setFullPath(oldIssue.getFullPathName());

        Location loc = new Location();
        if (!oldIssue.getLocations().isEmpty())
            loc = mapper(oldIssue.getLocations().get(0));
        loc.setClassName(oldIssue.getClassName());
        loc.setMethodName(oldIssue.getMethods().peek().toString());

        issue.setMessage(oldIssue.getInfo());
        issue.setRuleNumber(oldIssue.getRuleId());
        issue.setRuleDesc(oldIssue.getRule().getDesc());
        issue.setCWEId(oldIssue.getRule().getCweId()[0]);
        issue.setDescription(oldIssue.getRule().getDesc());

        //TODO - Not implemented yet
        //issue.setSeverity("TBD");


        return issue;
    }

    public static Location mapper(AnalysisLocation oldLoc) {
        Location loc = new Location();

        if (oldLoc.getColStart() != null)
            loc.setColumnNumber(oldLoc.getColStart());

        loc.setLineNumber(oldLoc.getLineStart());

        return loc;
    }

    public static Heuristics mapper(EnvironmentInformation info, double sliceAverageRounded) {

        return new Heuristics(
                info.getNUM_ORTHOGONAL(),
                info.getNUM_CONSTS_TO_CHECK(),
                info.getNUM_SLICES(),
                info.getNUM_HEURISTIC(),
                sliceAverageRounded,
                info.getDEPTH_COUNT()
        );
    }


//endregion
}

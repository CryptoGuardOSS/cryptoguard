package frontEnd.MessagingSystem.routing.outputStructures.common;

import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.AnalysisLocation;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.structure.Default.Issue;
import frontEnd.MessagingSystem.routing.structure.Default.Location;
import frontEnd.MessagingSystem.routing.structure.Default.Report;
import frontEnd.MessagingSystem.routing.structure.Default.Target;
import rule.engine.EngineType;
import util.Utils;

/**
 * <p>Default class.</p>
 *
 * @author franceme
 * Created on 4/23/19.
 * @version 03.07.01
 * @since 03.04.08
 *
 * <p>{Description Here}</p>
 */
public class Default {

    //region Attributes
    /**
     * Constant <code>Version</code>
     */
    public static final Integer Version = 3;
    //endregion

    //region UnMarshallers

    /**
     * <p>mapper.</p>
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
     */
    public static Report mapper(EnvironmentInformation info) {
        Report report = new Report();

        report.setDateTime(info.getAssessmentStartTime());
        report.setProjectName(Utils.projectName);
        report.setProjectVersion(Utils.projectVersion);
        report.setSchemaVersion(Version);
        report.setUUID(info.getUUID());

        return report;
    }

    /**
     * <p>mapper.</p>
     *
     * @param report a {@link frontEnd.MessagingSystem.routing.structure.Default.Report} object.
     * @return a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public static EnvironmentInformation mapper(Report report) {
        EnvironmentInformation info = new EnvironmentInformation();

        info.setAssessmentStartTime(report.getDateTime());
        info.setTargetProjectName(report.getProjectName());
        info.setTargetProjectVersion(report.getProjectVersion());
        info.setUUID(report.getUUID());

        info.setPackageRootDir(report.getTarget().getFullPath());
        info.setPackageName(report.getTarget().getProjectName());
        info.setTargetProjectName(report.getTarget().getProjectName());
        info.setSourceType(mapper(report.getTarget().getType()));

        info.setIsGradle(mapper(report.getTarget()));
        info.setRawCommand(report.getTarget().getRawCommand());
        info.getSource().addAll(report.getTarget().getTargetSources());

        return info;
    }

    /**
     * <p>mapper.</p>
     *
     * @param info       a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @param computerOS a {@link java.lang.String} object.
     * @param jvmInfo    a {@link java.lang.String} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
     */
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

    /**
     * <p>mapper.</p>
     *
     * @param type a {@link rule.engine.EngineType} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.Type} object.
     */
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

    /**
     * <p>mapper.</p>
     *
     * @param type a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.Type} object.
     * @return a {@link rule.engine.EngineType} object.
     */
    public static EngineType mapper(Target.Type type) {
        switch (type) {
            case APK:
                return EngineType.APK;
            case JAR:
                return EngineType.JAR;
            case JAVA:
                return EngineType.JAVAFILES;
            case CLASS:
                return EngineType.CLASSFILES;
            case SOURCE:
                return EngineType.DIR;
        }
        return EngineType.JAR;
    }

    /**
     * <p>mapper.</p>
     *
     * @param isGradle a {@link java.lang.Boolean} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Target.ProjectType} object.
     */
    public static Target.ProjectType mapper(Boolean isGradle) {
        if (isGradle)
            return Target.ProjectType.GRADLE;
        else
            return Target.ProjectType.MAVEN;
    }

    /**
     * <p>mapper.</p>
     *
     * @param target a {@link frontEnd.MessagingSystem.routing.structure.Default.Target} object.
     * @return a {@link java.lang.Boolean} object.
     */
    public static Boolean mapper(Target target) {
        return target.getProjectType().equals(Target.ProjectType.GRADLE);
    }


    /**
     * <p>mapper.</p>
     *
     * @param oldIssue a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
     * @param id       a {@link java.lang.Integer} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     */
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

    /**
     * <p>mapper.</p>
     *
     * @param oldIssue a {@link frontEnd.MessagingSystem.routing.structure.Default.Issue} object.
     * @return a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
     */
    public static AnalysisIssue mapper(Issue oldIssue) {
        AnalysisIssue issue = new AnalysisIssue(oldIssue.getRuleNumber());

        issue.setFullPathName(oldIssue.getFullPath());
        issue.setClassName(oldIssue.getLocation().getClassName());
        issue.addMethod(oldIssue.getLocation().getMethodName(), mapper(oldIssue.getLocation()));

        issue.setInfo(oldIssue.getMessage());

        return issue;
    }

    /**
     * <p>mapper.</p>
     *
     * @param oldLoc a {@link frontEnd.MessagingSystem.AnalysisLocation} object.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     */
    public static Location mapper(AnalysisLocation oldLoc) {
        Location loc = new Location();

        if (oldLoc.getColStart() != null)
            loc.setColumnNumber(oldLoc.getColStart());

        loc.setLineNumber(oldLoc.getLineStart());

        return loc;
    }

    /**
     * <p>mapper.</p>
     *
     * @param oldLoc a {@link frontEnd.MessagingSystem.routing.structure.Default.Location} object.
     * @return a {@link frontEnd.MessagingSystem.AnalysisLocation} object.
     */
    public static AnalysisLocation mapper(Location oldLoc) {
        AnalysisLocation loc = new AnalysisLocation(oldLoc.getLineNumber());

        if (oldLoc.getColumnNumber() > 0)
            loc.setColStart(oldLoc.getColumnNumber());

        return loc;
    }

    /**
     * <p>mapper.</p>
     *
     * @param info                a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     * @param sliceAverageRounded a double.
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Default.Heuristics} object.
     */
    /*
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

    public static Heuristics mapHeuristics(Report report) {

        return new Heuristics(
                report.getHeuristics().getNumberOfOrthogonal(),
                report.getHeuristics().getNumberOfConstantsToCheck(),
                report.getHeuristics().getNumberOfSlices(),
                report.getHeuristics().getNumberOfHeuristics(),
                report.getHeuristics().getAverageSlice(),
                report.getHeuristics().getDepthCount()
        );
    }
    */


//endregion
}

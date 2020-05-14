package frontEnd.MessagingSystem.routing.outputStructures.common;

import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import org.apache.commons.lang3.StringEscapeUtils;

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
public class CSVMapper {

    //region Constructor
    public CSVMapper(EnvironmentInformation info) {
        this.setProjectName(info.getTargetProjectName());
        this.setProjectVersion(info.getTargetProjectVersion());
        this.setBasePath(info.getBuildRootDir());
        this.setComputerOS(info.getComputerOS());
        this.setJvmVersion(info.getJVM());
    }
    //endregion

    //region Attributes
    /**
     * Constant <code>Version</code>
     */
    public static final Integer Version = 0;
    public static Integer issueCounter = 0;
    public static String projectName;
    public static String projectVersion;
    public static String basePath;
    public static String computerOS;
    public static String jvmVersion;

    //endregion

    //region Getters/Setters
    public static Integer getVersion() {
        return Version;
    }

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        CSVMapper.projectName = projectName;
    }

    public static String getProjectVersion() {
        return projectVersion;
    }

    public static void setProjectVersion(String projectVersion) {
        CSVMapper.projectVersion = projectVersion;
    }

    public static String getBasePath() {
        return basePath;
    }

    public static void setBasePath(String basePath) {
        CSVMapper.basePath = basePath;
    }

    public static String getComputerOS() {
        return computerOS;
    }

    public static void setComputerOS(String computerOS) {
        CSVMapper.computerOS = computerOS;
    }

    public static String getJvmVersion() {
        return jvmVersion;
    }

    public static void setJvmVersion(String jvmVersion) {
        CSVMapper.jvmVersion = jvmVersion;
    }

    public static Integer getIssueCounter() {
        return issueCounter;
    }

    public static String addIssueCounter() {
        return String.valueOf(issueCounter++);
    }

    public static void setIssueCounter(Integer issueCounter) {
        CSVMapper.issueCounter = issueCounter;
    }
    //endregion

    //region UnMarshallers

    public String writeHeader() {
        CustomStringBuilder headerLine = new CustomStringBuilder();

        headerLine.add("ProjectName");
        headerLine.add("ProjectVersion");
        headerLine.add("FullPath");
        headerLine.add("ComputerOs");
        headerLine.add("JVMVersion");
        headerLine.add("IssueId");
        headerLine.add("IssueMessage");
        headerLine.add("IssueDescription");
        headerLine.add("IssueRuleNumber");
        headerLine.add("IssueRuleDescription");
        headerLine.add("CWEId");
        headerLine.add("IssueSeverity");
        headerLine.append("IssueFullPath");

        return headerLine.toString();
    }

    public String writeIssue(AnalysisIssue issue) {
        CustomStringBuilder issueLine = new CustomStringBuilder();

        issueLine.add(this.getProjectName());
        issueLine.add(this.getProjectVersion());
        issueLine.add(this.getBasePath());
        issueLine.add(this.getComputerOS());
        issueLine.add(this.getJvmVersion());
        issueLine.add(this.addIssueCounter());
        issueLine.add(issue.getInfo());
        issueLine.add(issue.getRule().getDesc());
        issueLine.add(issue.getRule().getRuleId());
        issueLine.add(issue.getRule().getDesc());
        issueLine.add(issue.getRule().getCweId()[0]);
        issueLine.add(1);
        issueLine.append(issue.getFullPathName());

        return issueLine.toString();
    }
    //endregion

    //region Utils

    /**
     * A custom class to handle all of the csv escaping
     */
    public class CustomStringBuilder {
        private StringBuilder writer = new StringBuilder();

        public void append(Object value) {
            writer.append(StringEscapeUtils.escapeCsv(String.valueOf(value)));
        }

        public void add(Object value) {
            writer.append(value).append(",");
        }

        @Override
        public String toString() {
            return writer.toString();
        }
    }
    //endregion
}

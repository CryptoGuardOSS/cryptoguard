package main.frontEnd.MessagingSystem.streamWriters.structures;

import com.example.response.*;
import main.CWE_Reader.CWE;
import main.CWE_Reader.CWEList;
import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.Interface.ExceptionId;
import main.frontEnd.MessagingSystem.AnalysisIssue;
import main.frontEnd.MessagingSystem.AnalysisLocation;
import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.streamWriters.baseStreamWriter;
import main.rule.engine.RuleList;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

/**
 * <p>ScarfXMLStream class.</p>
 *
 * @author RigorityJTeam
 * Created on 2/7/19.
 * @version $Id: $Id
 * @since 03.02.00
 *
 * <p>The ScarfXML stream writer.</p>
 */
public class ScarfXMLStream extends baseStreamWriter {

    private String footerCatch = "</AnalyzerReport>";
    private HashMap<Integer, Integer> countOfBugs = new HashMap<Integer, Integer>();
    private Integer issueID = 0;
    private Integer buildId;
    private String xPath;
    private CWEList cwes = new CWEList();
    private final Boolean prettyPrint;
    private final String fileOut;

    /**
     * <p>Getter for the field <code>prettyPrint</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean getPrettyPrint() {
        return prettyPrint == null ? false : prettyPrint;
    }

    private String xmlStringClean(String in) {
        return StringUtils.trimToNull(in.replace(" xmlns:ns2=\"https://www.swamp.com/com/scarf/struct\"", "").replaceAll("<ns2:", "<").replaceAll("</ns2:", "</"));
    }

    /**
     * <p>createBugSummary.</p>
     *
     * @param brokenRuleCount a {@link java.util.HashMap} object.
     * @return a {@link com.example.response.BugSummaryType} object.
     */
    private BugSummaryType createBugSummary(HashMap<Integer, Integer> brokenRuleCount) {
        BugSummaryType bugDict = new BugSummaryType();

        //region Creating A Bug Category with counts per the Broken Rules
        for (int ruleNumber : brokenRuleCount.keySet()) {
            BugCategoryType ruleType = new BugCategoryType();

            ruleType.setGroup(RuleList.getRuleByRuleNumber(ruleNumber).getDesc());
            ruleType.setCode(String.valueOf(ruleNumber));
            ruleType.setCount(brokenRuleCount.get(ruleNumber));

            bugDict.getBugCategory().add(ruleType);
        }
        //endregion

        return bugDict;
    }

    /**
     * <p>Constructor for ScarfXMLStream.</p>
     *
     * @param info a {@link main.frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
     */
    public ScarfXMLStream(EnvironmentInformation info) throws ExceptionHandler {
        super(info);
        this.fileOut = info.getFileOutName();
        this.buildId = info.getBuildId();
        this.xPath = info.getxPath();
        this.prettyPrint = info.getPrettyPrint();
    }

    private Integer getId() {
        return this.issueID++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHeader(EnvironmentInformation info) throws ExceptionHandler {

        try {
            AnalyzerReport report = new AnalyzerReport();

            //region Setting Attributes
          /*  report.setAssessFw(info.getAssessmentFramework());
            report.setAssessFwVersion(info.getAssessmentFrameworkVersion());*/
            report.setAssessmentStartTs(info.getStartTimeStamp());
           /* report.setBuildFw(info.getBuildFramework());
            report.setBuildFwVersion(info.getBuildFrameworkVersion());*/
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

            //Creating Marshaller for the Analyzer Report
            Marshaller marshaller = JAXBContext.newInstance(AnalyzerReport.class).createMarshaller();

            //Settings Properties of the Marshaller
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, getPrettyPrint());

            //Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            //From the marshaller output
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            //Creating the Stream for the marshalling and marshalling
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            marshaller.marshal(report, new PrintStream(xmlStream));

            String xml = StringUtils.trimToNull(xmlStream.toString().replace("/>", ">"));

            if (getPrettyPrint())
                this.writeln(xml, getPrettyPrint());
            else
                this.write(xml);

        } catch (JAXBException e) {
            throw new ExceptionHandler("Error marshalling xml.", ExceptionId.MAR_VAR);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void streamIntoBody(AnalysisIssue issue) throws ExceptionHandler {


        BugInstanceType instance = new BugInstanceType();

        //region Setting the instance
        if (!countOfBugs.containsKey(issue.getRuleId())) {
            countOfBugs.put(issue.getRuleId(), 1);
        } else {
            countOfBugs.put(issue.getRuleId(), countOfBugs.get(issue.getRuleId()) + 1);
        }

        instance.setId(getId());
        instance.setBugCode(issue.getRuleId().toString());
        instance.setBugGroup(issue.getRule().getDesc());
        instance.setBugMessage(issue.getRule().getDesc());

        if (buildId != null || xPath != null) {
            BugTraceType trace = new BugTraceType();
            trace.setBuildId(buildId);
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

        trace.setBuildId(this.buildId);
        trace.setAssessmentReportFile(this.fileOut);

        instance.setBugTrace(trace);
        //endregion

        //endregion

        //region Writing the Bug
        try {
            //Creating Marshaller for the Analyzer Report
            Marshaller marshaller = JAXBContext.newInstance(BugInstanceType.class).createMarshaller();

            // To format XML
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, getPrettyPrint());

            //If we DO NOT have JAXB annotated class
            JAXBElement<BugInstanceType> jaxbElement = new JAXBElement<BugInstanceType>(
                    new QName("", "BugInstance"),
                    BugInstanceType.class,
                    instance);

            //Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            //From the marshaller output
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            //Creating the Stream for the marshalling and marshalling
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            marshaller.marshal(jaxbElement, new PrintStream(xmlStream));

            String xml = xmlStringClean(xmlStream.toString());

            if (!xml.endsWith("/>"))
                if (getPrettyPrint())
                    this.writeln(xml, getPrettyPrint());
                else
                    this.write(xml);

        } catch (JAXBException e) {
            throw new ExceptionHandler("Error marshalling Xml.", ExceptionId.MAR_VAR);
        }
        //endregion
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFooter(EnvironmentInformation info) throws ExceptionHandler {

        //region Setting the BugSummary
        try {
            //Creating Marshaller for the Analyzer Report
            Marshaller marshaller = JAXBContext.newInstance(BugSummaryType.class).createMarshaller();

            // To format XML
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, getPrettyPrint());

            //If we DO NOT have JAXB annotated class
            JAXBElement<BugSummaryType> jaxbElement = new JAXBElement<BugSummaryType>(
                    new QName("", "BugSummary"),
                    BugSummaryType.class,
                    createBugSummary(countOfBugs));

            //Removing the <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            //From the marshaller output
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            //Creating the Stream for the marshalling and marshalling
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            marshaller.marshal(jaxbElement, new PrintStream(xmlStream));

            String xml = xmlStringClean(xmlStream.toString());

            if (!xml.endsWith("/>"))
                if (getPrettyPrint())
                    this.writeln(xml, getPrettyPrint());
                else
                    this.write(xml);

        } catch (JAXBException e) {
            throw new ExceptionHandler("Error marshalling XML.", ExceptionId.MAR_VAR);
        }
        //endregion

        this.write(footerCatch);

        //region Writing any extra footer comments

        String footer = "";
        String prettyTab = getPrettyPrint() ? "\t" : "";
        String prettyLine = getPrettyPrint() ? "\n" : " ";

        StringBuilder commentedFooter = new StringBuilder();

        if (info.getSootErrors() != null && info.getSootErrors().split("\n").length >= 1)
            commentedFooter.append(prettyTab).append(info.getSootErrors().replaceAll("\n", prettyLine)).append(prettyLine);

        if (info.isShowTimes())
            commentedFooter.append("Analysis Timing (ms): ").append(info.getAnalyisisTime()).append(".").append(prettyLine);

        if (StringUtils.isNotBlank(commentedFooter.toString()))
            footer = prettyLine + "<!--" + prettyLine + commentedFooter.toString() + "-->";

        if (footer != null)
            this.writeln(footer, getPrettyPrint());

        //endregion

    }
}

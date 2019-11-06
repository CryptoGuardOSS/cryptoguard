package frontEnd.MessagingSystem;

import analyzer.backward.UnitContainer;
import rule.engine.RuleList;
import util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The class containing the specific analysis issue information.
 * The "flat" structure to be transformed to various formats.
 *
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.01
 */
public class AnalysisIssue {

    //region Attributes
    private String fullPathName;
    private String className;
    private RuleList rule;
    private Stack methods;
    private ArrayList<AnalysisLocation> locations;
    private String info;
    //endregion

    //region Constructors

    /**
     * <p>Constructor for AnalysisIssue.</p>
     *
     * @param ruleNumber a {@link java.lang.Integer} object.
     */
    public AnalysisIssue(Integer ruleNumber) {
        this.rule = RuleList.getRuleByRuleNumber(ruleNumber);
    }


    /**
     * <p>Constructor for AnalysisIssue.</p>
     *
     * @param sootString  a {@link java.lang.String} object.
     * @param ruleNumber  a {@link java.lang.Integer} object.
     * @param Info        a {@link java.lang.String} object.
     * @param sourcePaths a {@link java.util.List} object.
     */
    public AnalysisIssue(String sootString, Integer ruleNumber, String Info, List<String> sourcePaths) {
        String className = Utils.retrieveClassNameFromSootString(sootString);
        String methodName = Utils.retrieveMethodFromSootString(sootString);
        Integer lineNum = Utils.retrieveLineNumFromSootString(sootString);
        String constant = null;


        if (sootString.contains("constant keys") || ruleNumber == 3)
            constant = Utils.retrieveFoundMatchFromSootString(sootString);


        if (lineNum >= 0)
            this.addMethod(methodName, new AnalysisLocation(lineNum));
        else
            this.addMethod(methodName);

        this.className = className;
        this.rule = RuleList.getRuleByRuleNumber(ruleNumber);

        if (constant != null)
            Info += " Found value \"" + constant + "\"";

        this.info = Info;

        this.fullPathName = getPathFromSource(sourcePaths, className);
    }

    /**
     * <p>Constructor for AnalysisIssue.</p>
     *
     * @param unit        a {@link analyzer.backward.UnitContainer} object.
     * @param ruleNumber  a {@link java.lang.Integer} object.
     * @param sootString  a {@link java.lang.String} object.
     * @param sourcePaths a {@link java.util.List} object.
     */
    public AnalysisIssue(UnitContainer unit, Integer ruleNumber, String sootString, List<String> sourcePaths) {
        Integer lineNum;
        String constant = null;


        if (sootString.contains("constant keys") || ruleNumber == 3)
            constant = Utils.retrieveFoundMatchFromSootString(sootString);


        String methodName = Utils.retrieveMethodFromSootString(unit.getMethod());

        if ((lineNum = unit.getUnit().getJavaSourceStartLineNumber()) >= 0) {
            AnalysisLocation tempLoc = new AnalysisLocation(lineNum);
            if (unit.getUnit().getJavaSourceStartColumnNumber() >= 0) {
                tempLoc.setColStart(unit.getUnit().getJavaSourceStartColumnNumber());
                tempLoc.setColEnd(unit.getUnit().getJavaSourceStartColumnNumber());
            }

            this.addMethod(methodName, tempLoc);
        }

        this.className = Utils.retrieveClassNameFromSootString(unit.getMethod());
        this.rule = RuleList.getRuleByRuleNumber(ruleNumber);


        this.info = Utils.retrieveFoundPatternFromSootString(sootString);

        if (this.info.equals("UNKNOWN") && constant != null)
            this.info = "Found: Constant \"" + constant + "\"";
        else if (this.info.equals("UNKNOWN") && constant == null)
            this.info = "Found: " + sootString;
        else if (constant != null)
            this.info += " Found value \"" + constant + "\"";
        else
            this.info = "Found: \"" + this.info + "\"";


        if (lineNum <= 0) {
            this.addMethod(methodName,
                    new AnalysisLocation(
                            Utils.retrieveLineNumFromSootString(sootString)));
        }


        if (this.getMethods().empty())
            this.addMethod(methodName);

        this.fullPathName = getPathFromSource(sourcePaths, className);
    }

    //endregion

    //region Helper Methods

    /**
     * <p>getPathFromSource.</p>
     *
     * @param sources   a {@link java.util.List} object.
     * @param className a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String getPathFromSource(List<String> sources, String className) {
        String relativePath = null;
        if (sources.size() == 1) {
            String fullSource = sources.get(0);
            String[] split = fullSource.split(Utils.fileSep);
            fullSource = split[split.length - 1];

            if (fullSource.endsWith(":dir"))
                relativePath = Utils.osPathJoin(fullSource.replace(":dir", ""), "src", "main", "java", className.replace(".", System.getProperty("file.separator")) + ".java");
            else
                relativePath = Utils.osPathJoin(fullSource, className.replace(".", System.getProperty("file.separator")) + ".class");
        } else {
            for (String in : sources)
                if (in.contains(className)) {
                    String[] split = in.split(Utils.fileSep);
                    relativePath = split[split.length - 1];
                }
        }
        if (relativePath == null)
            return "UNKNOWN";
        else
            return relativePath;
    }
    //endregion

    //region Getters/Setters

    /**
     * <p>Getter for the field <code>fullPathName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFullPathName() {
        return fullPathName;
    }

    /**
     * <p>Setter for the field <code>fullPathName</code>.</p>
     *
     * @param fullPathName a {@link java.lang.String} object.
     */
    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    /**
     * <p>Getter for the field <code>className</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getClassName() {
        return className;
    }

    /**
     * <p>Setter for the field <code>className</code>.</p>
     *
     * @param className a {@link java.lang.String} object.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * <p>Getter for the field <code>rule</code>.</p>
     *
     * @return a {@link rule.engine.RuleList} object.
     */
    public RuleList getRule() {
        return rule;
    }

    /**
     * <p>getRuleId.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getRuleId() {
        return rule.getRuleId();
    }

    /**
     * <p>Getter for the field <code>methods</code>.</p>
     *
     * @return a {@link java.util.Stack} object.
     */
    public Stack getMethods() {

        if (this.methods == null) {
            this.methods = new Stack();
        }

        return this.methods;
    }

    /**
     * <p>Getter for the field <code>locations</code>.</p>
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<AnalysisLocation> getLocations() {

        if (this.locations == null) {
            this.locations = new ArrayList<>();
        }

        return locations;
    }

    /**
     * <p>addLocation.</p>
     *
     * @param newLocation a {@link frontEnd.MessagingSystem.AnalysisLocation} object.
     */
    public void addLocation(AnalysisLocation newLocation) {
        this.getLocations().add(newLocation);
    }

    /**
     * <p>addMethod.</p>
     *
     * @param methodName a {@link java.lang.String} object.
     */
    public void addMethod(String methodName) {
        this.getMethods().push(String.valueOf(methodName));
    }

    /**
     * <p>addMethod.</p>
     *
     * @param methodName a {@link java.lang.String} object.
     * @param location   a {@link frontEnd.MessagingSystem.AnalysisLocation} object.
     */
    public void addMethod(String methodName, AnalysisLocation location) {
        location.setMethodNumber(this.getMethods().size());
        this.getMethods().push(String.valueOf(methodName));

        this.addLocation(location);
    }

    /**
     * <p>Getter for the field <code>info</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getInfo() {
        return info;
    }

    /**
     * <p>Setter for the field <code>info</code>.</p>
     *
     * @param info a {@link java.lang.String} object.
     */
    public void setInfo(String info) {
        this.info = info;
    }
    //endregion
}

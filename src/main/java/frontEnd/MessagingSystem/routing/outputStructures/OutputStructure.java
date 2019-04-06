package frontEnd.MessagingSystem.routing.outputStructures;

import CWE_Reader.CWEList;
import frontEnd.Interface.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugCategoryList;
import rule.engine.EngineType;
import rule.engine.RuleList;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>Abstract OutputStructure class.</p>
 *
 * @author RigorityJTeam
 * Created on 3/1/19.
 * @version $Id: $Id
 * @since 03.03.00
 *
 * <p>The general class encompassing the output structure (stream and blocked).</p>
 */
public abstract class OutputStructure {

    //region Attributes
    private final EnvironmentInformation source;
    private final ArrayList<AnalysisIssue> collection;
    private final File outfile;
    private final EngineType type;
    private final CWEList cwes = new CWEList();
    private final Charset chars = Charset.forName("UTF-8");
    private final HashMap<Integer, Integer> countOfBugs = new HashMap<>();
    //endregion

    //region Constructors

    /**
     * <p>Constructor for OutputStructure.</p>
     *
     * @param info a {@link EnvironmentInformation} object.
     */
    public OutputStructure(EnvironmentInformation info) {
        this.source = info;
        this.outfile = new File(info.getFileOut());
        this.type = info.getSourceType();
        this.collection = new ArrayList<>();
    }
    //endregion

    //region Methods to be overridden

    /**
     * <p>startAnalyzing.</p>
     *
     * @throws ExceptionHandler if any.
     */
    public abstract void startAnalyzing() throws ExceptionHandler;

    /**
     * <p>addIssue.</p>
     *
     * @param issue a {@link AnalysisIssue} object.
     * @throws ExceptionHandler if any.
     */
    public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
        //Keeping a rolling count of the different kinds of bugs occuring
        if (!countOfBugs.containsKey(issue.getRuleId())) {
            countOfBugs.put(issue.getRuleId(), 1);
        } else {
            countOfBugs.put(issue.getRuleId(), countOfBugs.get(issue.getRuleId()) + 1);
        }
    }

    /**
     * <p>stopAnalyzing.</p>
     *
     * @throws ExceptionHandler if any.
     */
    public abstract void stopAnalyzing() throws ExceptionHandler;
    //endregion

    //region Public helper methods

    /**
     * <p>createBugCategoryList.</p>
     *
     * @return a {@link com.example.response.BugSummaryType} object.
     */
    public BugCategoryList createBugCategoryList() {
        BugCategoryList bugDict = new BugCategoryList();

        //region Creating A Bug Category with counts per the Broken Rules
        for (int ruleNumber : countOfBugs.keySet()) {
            BugCategory ruleType = new BugCategory();

            ruleType.setGroup(RuleList.getRuleByRuleNumber(ruleNumber).getDesc());
            ruleType.setCode(String.valueOf(ruleNumber));
            ruleType.setCount(countOfBugs.get(ruleNumber));

            bugDict.addBugSummary(ruleType);
        }
        //endregion

        return bugDict;
    }

    /**
     * <p>Getter for the field <code>source</code>.</p>
     *
     * @return a {@link EnvironmentInformation} object.
     */
    public EnvironmentInformation getSource() {
        return source;
    }

    /**
     * <p>Getter for the field <code>collection</code>.</p>
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<AnalysisIssue> getCollection() {
        return collection;
    }

    /**
     * <p>addIssueToCollection.</p>
     *
     * @param issue a {@link AnalysisIssue} object.
     */
    public void addIssueToCollection(AnalysisIssue issue) {
        //Keeping a rolling count of the different kinds of bugs occuring
        if (!countOfBugs.containsKey(issue.getRuleId())) {
            countOfBugs.put(issue.getRuleId(), 1);
        } else {
            countOfBugs.put(issue.getRuleId(), countOfBugs.get(issue.getRuleId()) + 1);
        }

        this.collection.add(issue);
    }

    /**
     * <p>Getter for the field <code>outfile</code>.</p>
     *
     * @return a {@link java.io.File} object.
     */
    public File getOutfile() {
        return outfile;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link EngineType} object.
     */
    public EngineType getType() {
        return type;
    }

    /**
     * <p>Getter for the field <code>chars</code>.</p>
     *
     * @return a {@link java.nio.charset.Charset} object.
     */
    public Charset getChars() {
        return chars;
    }

    /**
     * <p>Getter for the field <code>countOfBugs</code>.</p>
     *
     * @return a {@link java.util.HashMap} object.
     */
    public HashMap<Integer, Integer> getCountOfBugs() {
        return countOfBugs;
    }

    /**
     * <p>Getter for the field <code>cwes</code>.</p>
     *
     * @return a {@link CWEList} object.
     */
    public CWEList getCwes() {
        return cwes;
    }
    //endregion
}
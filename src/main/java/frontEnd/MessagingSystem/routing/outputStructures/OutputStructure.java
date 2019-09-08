package frontEnd.MessagingSystem.routing.outputStructures;

import CWE_Reader.CWEList;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugSummary;
import lombok.extern.log4j.Log4j2;
import rule.engine.EngineType;
import rule.engine.RuleList;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>Abstract OutputStructure class.</p>
 *
 * @author CryptoguardTeam
 * Created on 3/1/19.
 * @version 03.07.01
 * @since 03.03.00
 *
 * <p>The general class encompassing the output structure (stream and blocked).</p>
 */
@Log4j2
public abstract class OutputStructure {

    //region Attributes
    private final EnvironmentInformation source;
    private final ArrayList<AnalysisIssue> collection;
    private final File outfile;
    private final EngineType type;
    private final CWEList cwes = new CWEList();
    private final Charset chars = StandardCharsets.UTF_8;
    private final HashMap<Integer, Integer> countOfBugs = new HashMap<>();
    //endregion

    //region Constructors

    /**
     * <p>Constructor for OutputStructure.</p>
     *
     * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
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
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public abstract void startAnalyzing() throws ExceptionHandler;

    /**
     * <p>addIssue.</p>
     *
     * @param issue a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
        log.debug("Adding Issue: " + issue.getInfo());
        //Keeping a rolling count of the different kinds of bugs occuring
        if (!countOfBugs.containsKey(issue.getRuleId())) {
            countOfBugs.put(issue.getRuleId(), 1);
        } else {
            countOfBugs.put(issue.getRuleId(), countOfBugs.get(issue.getRuleId()) + 1);
        }
    }

    /**
     * <p>addIssueToCollection.</p>
     *
     * @param issue a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public void addIssueToCollection(AnalysisIssue issue) throws ExceptionHandler {
        log.debug("Adding Issue: " + issue.getInfo());
        //Keeping a rolling count of the different kinds of bugs occuring
        if (!countOfBugs.containsKey(issue.getRuleId())) {
            countOfBugs.put(issue.getRuleId(), 1);
        } else {
            countOfBugs.put(issue.getRuleId(), countOfBugs.get(issue.getRuleId()) + 1);
        }

        this.collection.add(issue);
    }

    /**
     * <p>stopAnalyzing.</p>
     *
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public abstract void stopAnalyzing() throws ExceptionHandler;
    //endregion

    //region Public helper methods

    /**
     * <p>createBugCategoryList.</p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.structure.Scarf.BugSummary} object.
     */
    public BugSummary createBugCategoryList() {
        log.trace("Creating the Bug Summary");

        BugSummary bugDict = new BugSummary();
        //region Creating A Bug Category with counts per the Broken Rules
        for (int ruleNumber : countOfBugs.keySet()) {
            BugCategory ruleType = new BugCategory();

            ruleType.setGroup(RuleList.getRuleByRuleNumber(ruleNumber).getDesc());
            ruleType.setCode(String.valueOf(ruleNumber));
            ruleType.setCount(countOfBugs.get(ruleNumber));

            bugDict.addBugSummary(ruleType);
            log.debug("Added ruleType: " + ruleType.toString());
        }
        //endregion

        return bugDict;
    }

    /**
     * <p>Getter for the field <code>source</code>.</p>
     *
     * @return a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
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
     * @return a {@link rule.engine.EngineType} object.
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
     * @return a {@link CWE_Reader.CWEList} object.
     */
    public CWEList getCwes() {
        return cwes;
    }
    //endregion
}

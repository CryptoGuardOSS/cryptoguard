/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing.outputStructures;

import CWE_Reader.CWEList;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugCategory;
import frontEnd.MessagingSystem.routing.structure.Scarf.BugSummary;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;
import rule.engine.EngineType;
import rule.engine.RuleList;

/**
 * Abstract OutputStructure class.
 *
 * @author CryptoguardTeam Created on 3/1/19.
 * @version 03.07.01
 * @since 03.03.00
 *     <p>The general class encompassing the output structure (stream and blocked).
 */
public abstract class OutputStructure {

  private static final Logger log =
      org.apache.logging.log4j.LogManager.getLogger(OutputStructure.class);
  //region Attributes
  private EnvironmentInformation source;
  private final ArrayList<AnalysisIssue> collection;
  private File outfile;
  private EngineType type;
  private final CWEList cwes = new CWEList();
  private final Charset chars = StandardCharsets.UTF_8;
  private final HashMap<Integer, Integer> countOfBugs = new HashMap<>();
  private final Function<AnalysisIssue, String> errorAddition;
  private final Function<HashMap<Integer, Integer>, String> bugSummaryHandler;
  //endregion

  //region Constructors

  /**
   * Constructor for OutputStructure.
   *
   * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   */
  public OutputStructure(EnvironmentInformation info) {
    this.source = info;
    this.outfile = new File(info.getFileOut());
    this.type = info.getSourceType();
    this.collection = new ArrayList<>();
    this.errorAddition = info.getErrorAddition();
    this.bugSummaryHandler = info.getBugSummaryHandler();
  }

  /** Constructor for OutputStructure. */
  public OutputStructure() {
    this.collection = new ArrayList<>();
    this.errorAddition = null;
    this.bugSummaryHandler = null;
  }
  //endregion

  //region Methods to be overridden

  /**
   * startAnalyzing.
   *
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public abstract void startAnalyzing() throws ExceptionHandler;

  private void addIssueCore(AnalysisIssue issue) throws ExceptionHandler {
    if (this.errorAddition != null) log.info(this.errorAddition.apply(issue));

    log.debug("Adding Issue: " + issue.getInfo());
    //Keeping a rolling count of the different kinds of bugs occuring
    if (!countOfBugs.containsKey(issue.getRuleId())) {
      countOfBugs.put(issue.getRuleId(), 1);
    } else {
      countOfBugs.put(issue.getRuleId(), countOfBugs.get(issue.getRuleId()) + 1);
    }
  }

  /**
   * addIssue.
   *
   * @param issue a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void addIssue(AnalysisIssue issue) throws ExceptionHandler {
    this.addIssueCore(issue);
  }

  /**
   * addIssueToCollection.
   *
   * @param issue a {@link frontEnd.MessagingSystem.AnalysisIssue} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public void addIssueToCollection(AnalysisIssue issue) throws ExceptionHandler {
    this.addIssueCore(issue);
    this.collection.add(issue);
  }

  /**
   * stopAnalyzing.
   *
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public abstract void stopAnalyzing() throws ExceptionHandler;
  //endregion

  //region Public helper methods

  /**
   * createBugCategoryList.
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

    if (this.bugSummaryHandler != null) log.info(this.bugSummaryHandler.apply(countOfBugs));

    return bugDict;
  }

  /**
   * Getter for the field <code>source</code>.
   *
   * @return a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   */
  public EnvironmentInformation getSource() {
    return source;
  }

  /**
   * Getter for the field <code>collection</code>.
   *
   * @return a {@link java.util.ArrayList} object.
   */
  public ArrayList<AnalysisIssue> getCollection() {
    return collection;
  }

  /**
   * Getter for the field <code>outfile</code>.
   *
   * @return a {@link java.io.File} object.
   */
  public File getOutfile() {
    return outfile;
  }

  /**
   * Getter for the field <code>type</code>.
   *
   * @return a {@link rule.engine.EngineType} object.
   */
  public EngineType getType() {
    return type;
  }

  /**
   * Getter for the field <code>chars</code>.
   *
   * @return a {@link java.nio.charset.Charset} object.
   */
  public Charset getChars() {
    return chars;
  }

  /**
   * Getter for the field <code>countOfBugs</code>.
   *
   * @return a {@link java.util.HashMap} object.
   */
  public HashMap<Integer, Integer> getCountOfBugs() {
    return countOfBugs;
  }

  /**
   * Getter for the field <code>cwes</code>.
   *
   * @return a {@link CWE_Reader.CWEList} object.
   */
  public CWEList getCwes() {
    return cwes;
  }

  public void setSource(EnvironmentInformation source) {
    this.source = source;
  }

  public void setOutfile(File outfile) {
    this.outfile = outfile;
  }

  public void setType(EngineType type) {
    this.type = type;
  }
  //endregion
}

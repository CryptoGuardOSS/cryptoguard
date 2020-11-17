/* Licensed under GPL-3.0 */
package frontEnd.Interface.parameterChecks;

import static util.Utils.prep;

import frontEnd.Interface.SubRunner;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.MessagingSystem.routing.EnvironmentInformation;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.argsIdentifier;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import rule.engine.EngineType;
import util.Utils;

/**
 * @author franceme Created on 2020-11-16.
 * @since 04.05.02
 *     <p>Container
 *     <p>The "Builder Class" to interface with CryptoGuard This class allows developers to
 *     integrate more freely with the internals with a builder pattern type of approach.
 */
public class Container {
  //region Attributes
  private String javaHome = null;
  private String androidHome = null;
  private Integer debuggingLevel = -1;
  private Listing listing = Listing.Default;
  private EngineType engineType = EngineType.CLASSFILES;
  private List<String> sourceValues = new ArrayList<String>();
  private List<String> dependencyValues = new ArrayList<String>();
  private Boolean overwriteOutFile = false;
  private String outFile = null;
  private String mainFile = null;
  private Boolean prettyPrint = true;
  private Boolean showTimes = false;
  private Boolean streaming = true;
  private Boolean displayHeuristics = false;
  private Integer scanningDepth = null;
  private String packageName = null;
  //endregion

  //region Constructors
  public Container() {}

  //endregion

  //region Getters Setters
  public EngineType getEngineType() {
    return engineType;
  }

  public void setEngineType(EngineType engineType) {
    this.engineType = engineType;
  }

  public String getJavaHome() {
    return javaHome;
  }

  public void setJavaHome(String javaHome) {
    this.javaHome = javaHome;
  }

  public String getAndroidHome() {
    return androidHome;
  }

  public void setAndroidHome(String androidHome) {
    this.androidHome = androidHome;
  }

  public Integer getDebuggingLevel() {
    return debuggingLevel;
  }

  public void setDebuggingLevel(Integer debuggingLevel) {
    this.debuggingLevel = debuggingLevel;
  }

  public Listing getListing() {
    return listing;
  }

  public void setListing(Listing listing) {
    this.listing = listing;
  }

  public List<String> getSourceValues() {
    return sourceValues;
  }

  public void setSourceValues(List<String> sourceValues) {
    this.sourceValues = sourceValues;
  }

  public List<String> getDependencyValues() {
    return dependencyValues;
  }

  public void setDependencyValues(List<String> dependencyValues) {
    this.dependencyValues = dependencyValues;
  }

  public Boolean getOverwriteOutFile() {
    return overwriteOutFile;
  }

  public void setOverwriteOutFile(Boolean overwriteOutFile) {
    this.overwriteOutFile = overwriteOutFile;
  }

  public String getOutFile() {
    return outFile;
  }

  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public String getMainFile() {
    return mainFile;
  }

  public void setMainFile(String mainFile) {
    this.mainFile = mainFile;
  }

  public Boolean getPrettyPrint() {
    return prettyPrint;
  }

  public void setPrettyPrint(Boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
  }

  public Boolean getShowTimes() {
    return showTimes;
  }

  public void setShowTimes(Boolean showTimes) {
    this.showTimes = showTimes;
  }

  public Boolean getStreaming() {
    return streaming;
  }

  public void setStreaming(Boolean streaming) {
    this.streaming = streaming;
  }

  public Boolean getDisplayHeuristics() {
    return displayHeuristics;
  }

  public void setDisplayHeuristics(Boolean displayHeuristics) {
    this.displayHeuristics = displayHeuristics;
  }

  public Integer getScanningDepth() {
    return scanningDepth;
  }

  public void setScanningDepth(Integer scanningDepth) {
    this.scanningDepth = scanningDepth;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  //endregion

  //region Builder Methods
  public Container addSourceValue(String sourceValue) {
    this.sourceValues.add(sourceValue);
    return this;
  }

  public Container addSourceValues(List<String> sourceValues) {
    this.sourceValues.addAll(sourceValues);
    return this;
  }

  public Container addDependencyValue(String dependencyValue) {
    this.dependencyValues.add(dependencyValue);
    return this;
  }

  public Container addDependencyValue(List<String> dependencyValues) {
    this.dependencyValues.addAll(sourceValues);
    return this;
  }

  public Container withEngineType(EngineType engineType) {
    this.engineType = engineType;
    return this;
  }

  public Container withJavaHome(String javaHome) {
    this.javaHome = javaHome;
    return this;
  }

  public Container withAndroidHome(String androidHome) {
    this.androidHome = androidHome;
    return this;
  }

  public Container withDebuggingLevel(Integer debuggingLevel) {
    this.debuggingLevel = debuggingLevel;
    return this;
  }

  public Container withFormatLevel(Listing formatLevel) {
    this.listing = formatLevel;
    return this;
  }

  public Container withSourceValues(List<String> sourceValues) {
    this.sourceValues = sourceValues;
    return this;
  }

  public Container withDependencyValues(List<String> dependencyValues) {
    this.dependencyValues = dependencyValues;
    return this;
  }

  public Container withOverwriteOutFile(Boolean overwriteOutFile) {
    this.overwriteOutFile = overwriteOutFile;
    return this;
  }

  public Container withOutFile(String outFile) {
    this.outFile = outFile;
    return this;
  }

  public Container withMainFile(String mainFile) {
    this.mainFile = mainFile;
    return this;
  }

  public Container withPrettyPrint(Boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
    return this;
  }

  public Container withShowTimes(Boolean showTimes) {
    this.showTimes = showTimes;
    return this;
  }

  public Container withStreaming(Boolean streaming) {
    this.streaming = streaming;
    return this;
  }

  public Container withDisplayHeuristics(Boolean displayHeuristics) {
    this.displayHeuristics = displayHeuristics;
    return this;
  }

  public Container withScanningDepth(Integer scanningDepth) {
    this.scanningDepth = scanningDepth;
    return this;
  }

  public Container withPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }
  //endregion

  //region Methods
  public String build() throws ExceptionHandler {
    setDebuggingLevel(this.getDebuggingLevel());

    EnvironmentInformation info =
        Core.paramaterCheck(
            this.getSourceValues(),
            this.getDependencyValues(),
            this.getEngineType(),
            this.getListing(),
            this.getOutFile(),
            this.getOverwriteOutFile(),
            this.getMainFile(),
            this.getShowTimes(),
            this.getJavaHome(),
            this.getAndroidHome(),
            null);

    info.setPrettyPrint(this.getPrettyPrint());
    info.setShowTimes(this.getShowTimes());
    info.setStreaming(this.getStreaming());
    info.setDisplayHeuristics(this.getDisplayHeuristics());
    info.setPackageName(this.getPackageName());
    info.setKillJVM(false);
    Utils.initDepth(this.getScanningDepth());

    info.setRawCommand(
        Utils.makeArg(argsIdentifier.SOURCE, this.getSourceValues())
            + Utils.makeArg(argsIdentifier.DEPENDENCY, this.getDependencyValues())
            + Utils.makeArg(argsIdentifier.FORMAT, this.getEngineType())
            + Utils.makeArg(argsIdentifier.FORMATOUT, this.getListing())
            + Utils.makeArg(argsIdentifier.OUT, this.getOutFile())
            + Utils.makeArg(argsIdentifier.NEW, this.getOverwriteOutFile())
            + (StringUtils.isNotEmpty(this.getMainFile())
                ? Utils.makeArg(argsIdentifier.MAIN, this.getMainFile())
                : "")
            + Utils.makeArg(argsIdentifier.TIMESTAMP, this.getShowTimes())
            + Utils.makeArg(argsIdentifier.STREAM, this.getStreaming())
            + Utils.makeArg(argsIdentifier.HEURISTICS, this.getDisplayHeuristics())
            + Utils.makeArg(argsIdentifier.DEPTH, this.getScanningDepth())
            + Utils.makeArg(argsIdentifier.JAVA, this.getJavaHome())
            + Utils.makeArg(argsIdentifier.ANDROID, this.getAndroidHome())
            + Utils.makeArg(argsIdentifier.VERBOSE, this.getDebuggingLevel())
            + Utils.makeArg(argsIdentifier.PRETTY, this.getPrettyPrint())
            + Utils.makeArg(argsIdentifier.JAVA, this.getJavaHome()));

    return SubRunner.run(info);
  }

  @Override
  public String toString() {
    return "\"Container\": {"
        + "\"javaHome\":"
        + prep(this.getJavaHome())
        + ","
        + "\"androidHome\":"
        + prep(this.getAndroidHome())
        + ","
        + "\"debuggingLevel\":"
        + prep(this.getDebuggingLevel())
        + ","
        + "\"listing\":"
        + prep(this.getListing())
        + ","
        + "\"engineType\":"
        + prep(this.getEngineType())
        + ","
        + "\"sourceValues\":"
        + prep(this.getSourceValues())
        + ","
        + "\"dependencyValues\":"
        + prep(this.getDependencyValues())
        + ","
        + "\"overwriteOutFile\":"
        + prep(this.getOverwriteOutFile())
        + ","
        + "\"outFile\":"
        + prep(this.getOutFile())
        + ","
        + "\"mainFile\":"
        + prep(this.getMainFile())
        + ","
        + "\"prettyPrint\":"
        + prep(this.getPrettyPrint())
        + ","
        + "\"showTimes\":"
        + prep(this.getShowTimes())
        + ","
        + "\"streaming\":"
        + prep(this.getStreaming())
        + ","
        + "\"displayHeuristics\":"
        + prep(this.getDisplayHeuristics())
        + ","
        + "\"scanningDepth\":"
        + prep(this.getScanningDepth())
        + ","
        + "\"packageName\":"
        + prep(this.getPackageName())
        + "}";
  }
  //endregion
}

package main.frontEnd.MessagingSystem.routing.inputStructures;

import main.frontEnd.MessagingSystem.routing.EnvironmentInformation;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;

import java.util.Arrays;

/**
 * @author RigorityJTeam
 * Created on 2018-12-14.
 * @since 01.01.07
 *
 * <p>The Scarf Input check implementation.</p>
 */
public class ScarfXML implements InputStructure {

    private ScarfJsonInput inputReader = new ScarfJsonInput();

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * The overridden method for the ScarfXML output.
     */
    public EnvironmentInformation inputValidation(String[] args, String dependencies, EngineType type) {

        EnvironmentInformation info = new EnvironmentInformation(args[0], type, dependencies, Listing.ScarfXML.getFlag());

        inputReader.parseArguments(Arrays.copyOfRange(args, 0, args.length - 1));
        info.setAssessmentFramework(inputReader.getAssessmentFramework());
        info.setAssessmentFrameworkVersion(inputReader.getAssessmentFrameworkVersion());
        info.setBuildRootDir(inputReader.getBuildRootDir());
        info.setPackageRootDir(inputReader.getPackageRootDir());
        info.setParserName(inputReader.getParserName());
        info.setParserVersion(inputReader.getParserVersion());

        return info;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * The overridden method for the Legacy output.
     */
    public String helpInfo() {
        return inputReader.helpInfo();
    }


    //region Sub Class Reader

    /**
     * A sub class to read the information Scarf Input Reader
     * This is externalized to be able to read from multiple forms of input
     */
    private class ScarfJsonInput {
        //region Attributes
        private String assessmentFramework = "UNAVAILABLE";
        private String assessmentFrameworkVersion = "UNAVAILABLE";
        private String buildRootDir = "UNAVAILABLE";
        private String packageRootDir = "UNAVAILABLE";
        private String parserName = "UNAVAILABLE";
        private String parserVersion = "UNAVAILABLE";
        //endregion


        /**
         * An Empty Constructor
         */
        public ScarfJsonInput() {

        }

        /**
         * The method to parse the raw arguments from the console.
         *
         * @param args {@link String[]} - The raw command arguments passed in from the command line.
         */
        public void parseArguments(String[] args) {

            if (args.length >= 1)
                this.assessmentFramework = args[0];
            if (args.length >= 2)
                this.assessmentFramework = args[1];
            if (args.length >= 3)
                this.assessmentFramework = args[2];
            if (args.length >= 4)
                this.assessmentFramework = args[3];
            if (args.length >= 5)
                this.assessmentFramework = args[4];
            if (args.length >= 6)
                this.assessmentFramework = args[5];
        }

        public String helpInfo() {
            StringBuilder help = new StringBuilder();

            help.append("Usage: (AssessmentFramework) (AssessmentFrameworkVersion) (BuildRootDir) (PackageRootDir) (ParserName) (ParserVersion)\n")
                    .append("\tAssessmentFramework: Default => STUBBED").append("\n")
                    .append("\tAssessmentFrameworkVersion: Default => STUBBED").append("\n")
                    .append("\tBuildRootDir: Default => STUBBED").append("\n")
                    .append("\tPackageRootDir: Default => STUBBED").append("\n")
                    .append("\tParserName: Default => STUBBED").append("\n")
                    .append("\tParserVersion: Default => STUBBED");

            return help.toString();
        }

        //region Getters
        /**
         * Getter for assessmentFramework
         *
         * <p>getAssessmentFramework()</p>
         *
         * @return {@link String} - The assessmentFramework.
         */
        public String getAssessmentFramework() {
            return assessmentFramework;
        }

        /**
         * Getter for assessmentFrameworkVersion
         *
         * <p>getAssessmentFrameworkVersion()</p>
         *
         * @return {@link String} - The assessmentFrameworkVersion.
         */
        public String getAssessmentFrameworkVersion() {
            return assessmentFrameworkVersion;
        }

        /**
         * Getter for buildRootDir
         *
         * <p>getBuildRootDir()</p>
         *
         * @return {@link String} - The buildRootDir.
         */
        public String getBuildRootDir() {
            return buildRootDir;
        }

        /**
         * Getter for packageRootDir
         *
         * <p>getPackageRootDir()</p>
         *
         * @return {@link String} - The packageRootDir.
         */
        public String getPackageRootDir() {
            return packageRootDir;
        }

        /**
         * Getter for parserName
         *
         * <p>getParserName()</p>
         *
         * @return {@link String} - The parserName.
         */
        public String getParserName() {
            return parserName;
        }

        /**
         * Getter for parserVersion
         *
         * <p>getParserVersion()</p>
         *
         * @return {@link String} - The parserVersion.
         */
        public String getParserVersion() {
            return parserVersion;
        }
        //endregion
    }
    //endregion
}

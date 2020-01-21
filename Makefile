#region Variables
#Variables dynamically set when the program is being built from the source
ver=V03.12.01
name=cryptoguard

#General Variables used throughout the Makefile
dir=./
runner=$(dir)$(name).py
#endregion

default:: build

#region Commands
#Builds the project without running the setup
autobuild:
	@$(info Building the project.)
	@$(runner) build

#Will build the project if there wasn't a full build
build: env
	@$(info Building the project.)
	@$(runner) build

#Sets the current environment variable if it's not set
env:
	@$(info Checking the home variables.)
	@$(runner) checkEnv

#This runs the help method
help:
	@$(runner) help

#This runs command builder
buildCmd:
	@$(info Running the build command program.)
	@$(runner) buildCmd

#This creates the Usage file
usage:
	@$(info Running the usage program.)
	@$(runner) writeUsage

#This runs all of the crawled tests
tests:
	@$(info Running the Gradle Tests.)
	@$(runner) tests

#This runs all of the crawled tests
clean:
	@$(info Cleaning the project.)
	@$(runner) clean
#endregion
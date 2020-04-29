#region Variables
#Variables dynamically set when the program is being built from the source
ver=03.14.00
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

docker:
	@$(info Build the Docker file)
	@sudo docker build -t cryptoguard .

live:
	@$(info Running the Docker file)
	@sudo docker run -p 127.0.0.1:8888:8888 -ti cryptoguard /bin/bash

kill:
	@$(info Killing all the Dockerfiles)
	@-sudo docker kill $$(sudo docker ps -q)
	@-sudo docker rm $$(sudo docker ps -a -q)
	@-sudo docker rmi $$(sudo docker images -q)

run:
	@$(info Running the juypter notebook)
	@jupyter notebook --ip=0.0.0.0 --port=8888
#endregion

#region Variables
#Variables dynamically set when the program is being built from the source
ver=03.14.00
name=cryptoguard
py=cryptosouple.py

#General Variables used throughout the Makefile
dir=./
runner=$(dir)$(py)
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

website:
	@$(info Building the Jekyll website)
	@cd docs && bundle exec jekyll serve -w

docker:
	@$(info Build the Docker file)
	@sudo docker build -t cryptoguard .

live:
	@$(info Running the Docker file)
	#This exposes port 9000 from the host machine to port 8888 within the docker machine
	@sudo docker run -p 9000:8888 -ti cryptoguard /bin/bash

kill:
	@$(info Killing all the Dockerfiles)
	@-sudo docker kill $$(sudo docker ps -q)
	@-sudo docker rm $$(sudo docker ps -a -q)
	@-sudo docker rmi $$(sudo docker images -q)

run:
	@$(info Running the juypter notebook)
	@jupyter notebook --ip=127.0.0.1 --port=9000
#endregion

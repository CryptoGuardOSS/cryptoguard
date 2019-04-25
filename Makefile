#General Variables used throughout the Makefile
dir=./
testDir=$(dir)build/tmp/
javaHome=${JAVA_HOME}/bin/java
java7=${JAVA7_HOME}/bin/java

#Variables dynamically set when the program is being built from the source
ver=V03.04.04
name=cryptoguard

#The short hand paths to execute the compiled jar and the scarf xsd for valiation
scan=$(java7) -jar $(dir)build/libs/$(name)-$(ver).jar
scarfXSD=$(dir)src/main/resources/Scarf/scarf_v1.2.xsd

#The location of all of the test samples
jarLoc=$(dir)samples/testable-jar.jar
dirLoc=$(dir)samples/testable-jar
depLoc=$(dir)samples/testable-jar/build/dependencies
apkLoc=$(dir)samples/app-debug.apk

default:: fullBuild

#Checking if the Java7_Home variable is set 
checkJava7Home:
	@$(info Verifying the JAVA7_HOME envvironment variable is set.)
ifeq ($(strip ${JAVA7_HOME}),)
	@$(error Please set the JAVA7_HOME environment variable.)
else
	@$(info JAVA7_HOME is set.)
endif

#Checking if the Java_Home variable is set 
checkJavaHome:
	@$(info Verifying the JAVA_HOME envvironment variable is set.)
ifeq ($(strip ${JAVA_HOME}),)
	@$(error Please set the JAVA_HOME environment variable.)
else
	@$(info JAVA_HOME is set.)
endif

#Checking if the ANDROID_HOME variable is set 
checkAndroidSDKHome:
	@$(info Verifying the ANDROID_HOME envvironment variable is set.)
ifeq ($(strip ${ANDROID_HOME}),)
	@$(error Please set the ANDROID_SDK environment variable.)
else
	@$(info ANDROID_SDK is set.)
endif

#A method to check if all of the required environment variables are set
checkEnv: checkJava7Home checkJavaHome checkAndroidSDKHome
	@$(info Envrionment Variables are set.)

#The command for a default jar scan output
scanJar: checkJava7Home
	@$(info Scanning the sample jar ($(jarLoc)))
	@$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o $(testDir)results_newJar.txt

#The command for a SCARF jar scan output, that also verifies the output
scanJar_Scarf: checkJava7Home
	@$(info Scanning the sample jar ($(jarLoc)).)
	@$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o $(testDir)results_newJar_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($($(testDir)results_newJar_Scarf.xml)).)
	@xmllint --schema $(scarfXSD) $(testDir)results_newJar_Scarf.xml>$(testDir)lint_JAR.out 2>$(testDir)lint_JAR.err

#The command for a default directory scan output
scanDir: JAVA_HOME
	@$(info Scanning the sample directory ($(dirLoc)))
	@$(scan) -in source -s $(dirLoc) -d $(depLoc) -o $(testDir)results_newDir.txt

#The command for a SCARF directory scan output, that also verifies the output
scanDir_Scarf: JAVA_HOME
	@$(info Scanning the sample jar ($(dirLoc)).)
	@$(scan) -in source -s $(dirLoc) -d $(depLoc) -o $(testDir)results_newDir_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($($(testDir)results_newDir_Scarf.xml)).)
	@xmllint --schema $(scarfXSD) $(testDir)results_newDir_Scarf.xml>$(testDir)lint_DIR.out 2>$(testDir)lint_DIR.err

#The command for a default apk scan output
scanAPK: checkAndroidSDKHome
	@$(info Scanning the sample apk ($(apkLoc)))
	@$(scan) -in apk -s $(apkLoc) -o $(testDir)results_newApk.txt

#The command for a SCARF apk scan output, that also verifies the output
scanAPK_Scarf: checkAndroidSDKHome
	@$(info Scanning the sample apk ($(apkLoc)).)
	@$(scan) -in apk -s $(apkLoc) -o $(testDir)results_newApk_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($($(testDir)results_newApk_Scarf.xml)).)
	@xmllint --schema $(scarfXSD) $(testDir)results_newApk_Scarf.xml>$(testDir)lint_APK.out 2>$(testDir)lint_APK.err

#This build skips the tests and removes the namespace from the xml creation
buildNoTest: checkEnv
	@$(info Running a gradle clean build without tests.)
	@gradle -p $(dir) clean build -x test

#Test build is currently skipped as there is an issue with gradle/java version
fullBuild: buildNoTest
	@$(info Cutting (copying) the source built $(name) jar file.)
	@cp $(dir)build/libs/$(name)-$(ver).jar $(dir)$(name).jar

#This runs the help method
help:
	@$(scan) -h

#This build calls all of the scan operations within this Makefile
scans:\
buildNoTest\
cleanScans\
scanJar\
scanJar_Scarf\
scanDir\
scanDir_Scarf\
scanAPK\
scanAPK_Scarf

#This build clears removes all of the scan results
cleanScans:
	@$(info Removing all of the test results.)
	@-rm $(testDir)results*
	@-rm $(testDir)ERROR*
	@-rm $(testDir)lint*

#This build runs the full build and removes the results
build: fullBuild cleanScans

#This build cleans the scan results and runs a gradle clean
clean: cleanScans
	@$(info Running a gradle clean and removing cut the $(dir)cryptoguard.jar file.)
	@gradle -p $(dir) clean
	@-rm $(dir)$(name).jar

#General Variables used throughout the Makefile
dir=./
testDir=$(dir)build/Makefile-tests/
testSrc=$(dir)samples/
java7=${JAVA7_HOME}/bin/java

#Variables dynamically set when the program is being built from the source
ver=V03.04.08
name=cryptoguard

#The short hand paths to execute the compiled jar and the scarf xsd for valiation
scan=$(java7) -jar $(dir)$(name).jar
scarfXSD=$(dir)src/main/resources/Scarf/scarf_v1.2.xsd

#The location of all of the test samples
dirLoc=$(testSrc)testable-jar
jarLoc=$(dirLoc)/build/libs/testable-jar.jar
depLoc=$(dirLoc)/build/dependencies
apkLoc=$(testSrc)app-debug.apk

#Setting some of the source java/class file constants for ease of use
srcJavaFolder=$(dirLoc)/src/main/java/tester/
srcClassFolder=$(dirLoc)/build/classes/java/main/tester/
sampleFile1=PBEUsage
sampleFile2=Crypto

javaFile1=$(srcJavaFolder)$(sampleFile1).java
javaFile2=$(srcJavaFolder)$(sampleFile2).java

classFile1=$(srcClassFolder)$(sampleFile1).class
classFile2=$(srcClassFolder)$(sampleFile2).class

default:: build

#Will build the project if there wasn't a full build
build: setHome checkEnv
ifeq ("$(wildcard $(dir)$(name).jar)","")
	@$(info Building the project.)
	@gradle -p $(dir) clean build -x test
	@$(info Cutting (copying) the source built $(name) jar file, and creating the test directory.)
	@cp $(dir)build/libs/$(name)-$(ver).jar $(dir)$(name).jar
	@$(info Creating the test directory.)
	@$(info $(testDir))
	@mkdir -p $(testDir)
endif

#Sets the current environment variable if it's not set
#Needed for gradle
setHome:
	@$(info Resetting the JAVA_HOME env variable (for Gradle).)
	@export JAVA_HOME=${JAVA7_HOME}

#Checking if the Java7_Home variable is set 
checkJava7Home:
	@$(info Verifying the JAVA7_HOME environment variable is set.)
ifeq ($(strip ${JAVA7_HOME}),)
	@$(error Please set the JAVA7_HOME environment variable.)
else
	@$(info JAVA7_HOME is set.)
endif

#Checking if the ANDROID_HOME variable is set 
checkAndroidSDKHome:
	@$(info Verifying the ANDROID_HOME environment variable is set.)
ifeq ($(strip ${ANDROID_HOME}),)
	@$(error Please set the ANDROID_SDK environment variable.)
else
	@$(info ANDROID_SDK is set.)
endif

#A method to check if all of the required environment variables are set
checkEnv: checkJava7Home checkAndroidSDKHome
	@$(info Envrionment Variables are set.)

#The grouping of Jar Scanning
scanJar: scanJar_Default scanJar_Scarf

#The command for a default jar scan output
scanJar_Default: checkJava7Home build
	@$(info Scanning the sample jar ($(jarLoc)).)
	@$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o $(testDir)results_Jar.txt
	@$(info View the output at $(testDir)results_Jar.txt.)

#The command for a SCARF jar scan output, that also verifies the output
scanJar_Scarf: checkJava7Home build
	@$(info Scanning the sample jar ($(jarLoc)).)
	@$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o $(testDir)results_Jar_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($(testDir)results_Jar_Scarf.xml).)
	@xmllint --schema $(scarfXSD) $(testDir)results_Jar_Scarf.xml>$(testDir)lint_JAR.out 2>$(testDir)lint_JAR.err
	@$(info View the output at $(testDir)results_Jar_Scarf.xml.)

#The grouping of Apk Scanning
scanAPK: scanAPK_Default scanAPK_Scarf

#The command for a default apk scan output
scanAPK_Default: checkAndroidSDKHome build
	@$(info Scanning the sample apk ($(apkLoc)))
	@$(scan) -in apk -s $(apkLoc) -o $(testDir)results_Apk.txt
	@$(info View the output at $(testDir)results_Apk.txt.)

#The command for a SCARF apk scan output, that also verifies the output
scanAPK_Scarf: checkAndroidSDKHome build
	@$(info Scanning the sample apk ($(apkLoc)).)
	@$(scan) -in apk -s $(apkLoc) -o $(testDir)results_Apk_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($(testDir)results_Apk_Scarf.xml).)
	@xmllint --schema $(scarfXSD) $(testDir)results_Apk_Scarf.xml>$(testDir)lint_APK.out 2>$(testDir)lint_APK.err
	@$(info View the output at $(testDir)results_Apk_Scarf.xml.)

#The grouping of Project Scanning
scanDir: scanDir_Default scanDir_Scarf

#The command for a default directory scan output
scanDir_Default: checkJava7Home build
	@$(info Scanning the sample directory ($(dirLoc)))
	@$(scan) -in source -s $(dirLoc) -d $(depLoc) -o $(testDir)results_Dir.txt
	@$(info View the output at $(testDir)results_Dir.txt.)

#The command for a SCARF directory scan output, that also verifies the output
scanDir_Scarf: checkJava7Home build
	@$(info Scanning the sample jar ($(dirLoc)).)
	@$(scan) -in source -s $(dirLoc) -d $(depLoc) -o $(testDir)results_Dir_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($(testDir)results_Dir_Scarf.xml).)
	@xmllint --schema $(scarfXSD) $(testDir)results_Dir_Scarf.xml>$(testDir)lint_DIR.out 2>$(testDir)lint_DIR.err
	@$(info View the output at $(testDir)results_Dir_Scarf.xml.)

#The grouping of Java File Scanning
scanJavaFile: scanJavaFile_Default scanJavaFiles_Default scanJavaFile_Scarf scanJavaFiles_Scarf

#The command for a default java class file scan output
scanJavaFile_Default: checkJava7Home build
	@$(info Scanning the sample java file ($(javaFile1)).)
	@$(scan) -in java -s $(javaFile1) -o $(testDir)results_javaFile.txt
	@$(info View the output at $(testDir)results_javaFile.txt.)

#The command for a default java class file scan output
scanJavaFiles_Default: checkJava7Home build
	@$(info Scanning the sample java files ($(javaFile1), $(javaFile2)).)
	@$(scan) -in java -s $(javaFile1) $(javaFile2) -o $(testDir)results_javaFiles.txt
	@$(info View the output at $(testDir)results_javaFiles.txt.)

#The command for a SCARF directory scan output, that also verifies the output
scanJavaFile_Scarf: checkJava7Home build
	@$(info Scanning the sample java class file ($(javaFile1)).)
	@$(scan) -in java -s $(javaFile1) -o $(testDir)results_javaFile_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($(testDir)results_javaFile_Scarf.xml).)
	@xmllint --schema $(scarfXSD) $(testDir)results_javaFile_Scarf.xml>$(testDir)lint_JavaFile.out 2>$(testDir)lint_JavaFile.err
	@$(info View the output at $(testDir)results_javaFile_Scarf.xml.)

#The command for a SCARF directory scan output, that also verifies the output
scanJavaFiles_Scarf: checkJava7Home build
	@$(info Scanning the sample java class files ($(javaFile1), $(javaFile2)).)
	@$(scan) -in java -s $(javaFile1) $(javaFile2) -o $(testDir)results_javaFiles_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($(testDir)results_javaFiles_Scarf.xml).)
	@xmllint --schema $(scarfXSD) $(testDir)results_javaFiles_Scarf.xml>$(testDir)lint_JavaFiles.out 2>$(testDir)lint_JavaFiles.err
	@$(info View the output at $(testDir)results_javaFiles_Scarf.xml.)

#The grouping of Java Class Scanning
scanJavaClass: scanJavaClass_Default scanJavaClasses_Default scanJavaClass_Scarf scanJavaClasses_Scarf

#The command for a default java class file scan output
scanJavaClass_Default: checkJava7Home build
	@$(info Scanning the sample java class file ($(classFile1)).)
	@$(scan) -in class -s $(classFile1) -o $(testDir)results_javaClass.txt
	@$(info View the output at $(testDir)results_javaClass.txt.)

#The command for a default java class file scan output
scanJavaClasses_Default: checkJava7Home build
	@$(info Scanning the sample java class files ($(classFile1), $(classFile2)).)
	@$(scan) -in class -s $(classFile1) $(classFile2) -o $(testDir)results_javaClasses.txt
	@$(info View the output at $(testDir)results_javaClasses.txt.)

#The command for a SCARF directory scan output, that also verifies the output
scanJavaClass_Scarf: checkJava7Home build
	@$(info Scanning the sample java class file ($(classFile1)).)
	@$(scan) -in class -s $(classFile1) -o $(testDir)results_javaClass_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($(testDir)results_javaClass_Scarf.xml).)
	@xmllint --schema $(scarfXSD) $(testDir)results_javaClass_Scarf.xml>$(testDir)lint_JavaClass.out 2>$(testDir)lint_JavaClass.err
	@$(info View the output at $(testDir)results_javaClass_Scarf.xml.)

#The command for a SCARF directory scan output, that also verifies the output
scanJavaClasses_Scarf: checkJava7Home build
	@$(info Scanning the sample java class files ($(classFile1), $(classFile2)).)
	@$(scan) -in class -s $(classFile1) $(classFile2) -o $(testDir)results_javaClasses_Scarf.xml -m SX -n
	@$(info Verifying the Scarf Output ($(testDir)results_javaClasses_Scarf.xml).)
	@xmllint --schema $(scarfXSD) $(testDir)results_javaClasses_Scarf.xml>$(testDir)lint_JavaClasses.out 2>$(testDir)lint_JavaClass.err
	@$(info View the output at $(testDir)results_javaClasses_Scarf.xml.)

#This runs the help method
help: build
	@$(scan) -h

#This build calls all of the scan operations within this Makefile
scans:\
setHome\
build\
setHome\
scanJar\
scanAPK\
scanDir\
scanJavaClass\
scanJavaFile

#This build clears removes all of the scan results
cleanScans:
	@$(info Removing all of the test results.)
	@-rm -r $(testDir)

#This build cleans the scan results and runs a gradle clean
clean: setHome cleanScans
	@$(info Running a gradle clean and removing cut the $(dir)cryptoguard.jar file.)
	@gradle -p $(dir) clean
	@-rm $(dir)$(name).jar

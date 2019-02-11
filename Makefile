dir=./
java7=${JAVA7_HOME}/bin/java

ver=03.02.05
scan=$(java7) -jar $(dir)main/build/libs/main-$(ver).jar
marshal=$(dir)main/src/main/java/com/example/response/package-info.java

jarLoc=$(dir)testable-jar/build/libs/testable-jar.jar
depLoc=$(dir)testable-jar/build/dependencies
dirLoc=$(dir)testable-jar
apkLoc=$(dir)

default:: build

scanJar:
	$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o ./results_newJar.txt

scanDir:
	$(scan) -in source -s $(dirLoc) -d $(depLoc) -o ./results_newDir.txt

scanAPK:
	#$(scan) -in source -s $(apkLoc) -o ./results_newApk.txt

#Sets the namespace for the xml, needed for unit/integration tests
setNS:
	sed -i 's+(elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)+(namespace = "https://www.swamp.com/com/scarf/struct", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)+g' $(marshal)

#Removes the namespace from the xml, needed for ScarfXML format output
rmvNS:
	sed -i 's+(namespace = "https://www.swamp.com/com/scarf/struct", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)+(elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)+g' $(marshal)

#The full custom build
# 1/2 - sets Namespace and removes the settings for the testable-jar
#	the testable-jar isn't fully setup
# 3 - builds the testable-jar
# 4 - recreates the settings file, needed for scanning
# 5 - builds the main project
fullBuild:
	make setNS
	-rm $(dirLoc)/settings.gradle
	gradle -p $(dirLoc) clean build
	echo "rootProject.name = 'testable-jar'" >> $(dirLoc)/settings.gradle
	gradle -p $(dir)main clean build

#This build skips the tests and removes the namespace from the xml creation
buildNoTest:
	make rmvNS
	gradle -p $(dir)main build -x test

help:
	$(scan) -h

build:
	make fullBuild
	make buildNoTest
	make help

clean:
	gradle -p $(dir) clean
	-rm results_newJar.txt
	-rm results_newDir.txt
	-rm results_newApk.txt

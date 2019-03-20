dir=./
java7=${JAVA7_HOME}/bin/java

ver=03.03.03
scan=$(java7) -jar $(dir)main/build/libs/main-$(ver).jar
marshal=$(dir)main/src/main/java/com/example/response/package-info.java
scarfXSD=$(dir)main/src/main/schema/xsd/Scarf/scarf_v1.2.xsd

jarLoc=$(dir)testable-jar/build/libs/testable-jar.jar
depLoc=$(dir)testable-jar/build/dependencies
dirLoc=$(dir)testable-jar
apkLoc=$(dir)app-debug.apk

default:: build

scanJar:
	$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o ./results_newJar.txt

scanJar_Scarf:
	$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o ./results_newJar_Scarf.xml -m SX -n
	xmllint --schema $(scarfXSD) ./results_newJar_Scarf.xml>lint_JAR.out 2>lint_JAR.err

scanDir:
	$(scan) -in source -s $(dirLoc) -d $(depLoc) -o ./results_newDir.txt

scanDir_Scarf:
	$(scan) -in source -s $(dirLoc) -d $(depLoc) -o ./results_newDir_Scarf.xml -m SX -n
	xmllint --schema $(scarfXSD) ./results_newDir_Scarf.xml>lint_DIR.out 2>lint_DIR.err

scanAPK:
	$(scan) -in apk -s $(apkLoc) -o ./results_newApk.txt

scanAPK_Scarf:
	$(scan) -in apk -s $(apkLoc) -o ./results_newApk_Scarf.xml -m SX -n
	xmllint --schema $(scarfXSD) ./results_newApk_Scarf.xml>lint_APK.out 2>lint_APK.err

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

scans:
	-make scanJar
	-make scanJar_Scarf
	-make scanDir
	-make scanDir_Scarf
	-make scanAPK
	-make scanAPK_Scarf

cleanScans:
	-rm results*
	-rm ERROR*
	-rm lint*

build:
	make fullBuild
	make buildNoTest
	make help

clean:
	gradle -p $(dir) clean
	make cleanScans

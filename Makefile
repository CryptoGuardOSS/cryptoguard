dir=./
testDir=$(dir)build/tmp/
java7=${JAVA7_HOME}/bin/java

ver=V03.04.01
name=cryptoguard
scan=$(java7) -jar $(dir)build/libs/$(name)-$(ver).jar
scarfXSD=$(dir)src/main/resources/Scarf/scarf_v1.2.xsd

jarLoc=$(dir)samples/testable-jar.jar
dirLoc=$(dir)samples/testable-jar
depLoc=$(dir)samples/testable-jar/build/dependencies
apkLoc=$(dir)samples/app-debug.apk

default:: fullBuild

scanJar:
	$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o $(testDir)results_newJar.txt

scanJar_Scarf:
	$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o $(testDir)results_newJar_Scarf.xml -m SX -n
	xmllint --schema $(scarfXSD) $(testDir)results_newJar_Scarf.xml>$(testDir)lint_JAR.out 2>$(testDir)lint_JAR.err

scanDir:
	$(scan) -in source -s $(dirLoc) -d $(depLoc) -o $(testDir)results_newDir.txt

scanDir_Scarf:
	$(scan) -in source -s $(dirLoc) -d $(depLoc) -o $(testDir)results_newDir_Scarf.xml -m SX -n
	xmllint --schema $(scarfXSD) $(testDir)results_newDir_Scarf.xml>$(testDir)lint_DIR.out 2>$(testDir)lint_DIR.err

scanAPK:
	$(scan) -in apk -s $(apkLoc) -o $(testDir)results_newApk.txt

scanAPK_Scarf:
	$(scan) -in apk -s $(apkLoc) -o $(testDir)results_newApk_Scarf.xml -m SX -n
	xmllint --schema $(scarfXSD) $(testDir)results_newApk_Scarf.xml>$(testDir)lint_APK.out 2>$(testDir)lint_APK.err

#This build skips the tests and removes the namespace from the xml creation
buildNoTest:
	gradle -p $(dir) clean build -x test

#This build skips the tests and removes the namespace from the xml creation
buildTest:
	gradle -p $(dir) clean build

#Test build is currently skipped as there is an issue with gradle/java version
fullBuild:
	#make buildTest
	make buildNoTest
	cp $(dir)build/libs/$(name)-$(ver).jar $(dir)$(name).jar

help:
	$(scan) -h

scans:
	make buildNoTest
	make cleanScans
	-make scanJar
	-make scanJar_Scarf
	-make scanDir
	-make scanDir_Scarf
	-make scanAPK
	-make scanAPK_Scarf

cleanScans:
	-rm $(testDir)results*
	-rm $(testDir)ERROR*
	-rm $(testDir)lint*

build:
	make fullBuild
	make buildNoTest
	make cleanScans

clean:
	gradle -p $(dir) clean
	make cleanScans

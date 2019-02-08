dir=./
java7=$JAVA7_HOME

ver=03.02.02
scan=$(java7) -jar $(dir)/main/build/libs/main-$(ver).jar

jarLoc=$(dir)testable-jar/build/libs/testable-jar.jar
depLoc=$(dir)testable-jar/build/dependencies
dirLoc=$(dir)testable-jar
apkLoc=$(dir)

default:: build

scanJar:
	$(scan) -in jar -s $(jarLoc) -d $(depLoc) -o ./results_newJar.txt

scanDir:
	$(new) -in source -s $(dirLoc) -d $(depLoc) -o ./results_newDir.txt

scanAPK:
	#$(new) -in source -s $(apkLoc) -o ./results_newApk.txt

build:
	-rm $(dirLoc)/settings.gradle
	gradle -p $(dirLoc) clean build
	echo "rootProject.name = 'testable-jar'" >> $(dirLoc)/settings.gradle
	gradle -p $(dir)main clean build

help:
	$(scan) -h

clean:
	gradle -p $(dir) clean
	-rm results_newJar.txt
	-rm results_newDir.txt
	-rm results_newApk.txt

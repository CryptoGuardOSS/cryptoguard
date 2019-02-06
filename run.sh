#!/usr/bin/env bash

#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/mvn-sample" ""
#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/gradle-sample" ""
#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/gradle-nosub" ""
#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/simple-gradle-1-sub" ""

java -jar main/build/libs/main.jar "jar" "testable-jar/build/libs/testable-jar.jar" "testable-jar/build/dependencies"

#java -jar main/build/libs/main.jar "apk" "/home/path/to/apk/sample.apk"


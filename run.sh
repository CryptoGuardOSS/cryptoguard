#!/usr/bin/env bash

#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/mvn-sample" ""
#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/simple-gradle-2-sub" "build/dependencies"
#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/simple-gradle-no-sub" ""
#/usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/home/krishnokoli/projects/simple-gradle-1-sub" ""

#java -jar main/build/libs/main.jar "jar" "testable-jar/build/libs/testable-jar.jar" "testable-jar/build/dependencies"
java -jar main/build/libs/main.jar "jar" "/home/krishnokoli/projects/simple-gradle-2-sub/sample-main/build/libs/sample-main.jar" "/home/krishnokoli/projects/simple-gradle-2-sub/sample-main/build/dependencies"
#java -jar main/build/libs/main.jar "jar" "/home/krishnokoli/projects/sample-mvn/sample-cmdlineapp/target/command-line-app.jar" ""

#java -jar main/build/libs/main.jar "apk" "/path/to/apk/crypto_test.apk"


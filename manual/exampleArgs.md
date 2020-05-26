---
layout: page
title: Example Arguments for CryptoGuard
permalink: /args/
---

# Arguments for CryptoGuard

## Example General Project Version

`java -jarcryptoguard.jar -V`

The version argument (-V) returns the version of the project and exits.

## Example General Project No Logging

`java -jarcryptoguard.jar -vx`

The argument (-vx) only displays the fatal logs.

## Example General Project Verbose Logging

`java -jar cryptoguard.jar -v`

The argument (-v) displays debug logs.

## Example General Project Very Verbose Logging

`java -jar cryptoguard.jar -vv`

The argument (-vv) displays the all of the logs available.

## Example General Project Stream

`java -jar cryptoguard.jar -st`

The argument (-st) enables streaming the results to whatever output file is specified.

## Example General Project Heuristics

`java -jar cryptoguard.jar -H`

The argument (-H) writes the heuristics picked up in the output file.

## Example General Project Specifying the main file

`java -jar cryptoguard.jar -main`

The argument (-main) specifies the main class (containing public static void main) if there are multiple within the project.

## Example General Project Java Home

`java -jar cryptoguard.jar -java`

The argument (-java) sets the Java file path needed for an internal library. This is needed if the environment variable isn't set.
JDK 7 needed for either a Project or Java File Scanning.
JDK 8 needed for the other projects.

## Example General Project Android Home

`java -jar cryptoguard.jar -android`

The argument (-android) sets the Android file path.needed for an internal library. This is needed if the environment variable isn't set.
Needed if an Android project is being scanned.

## Example General Project PrettyPrint

`java -jar cryptoguard.jar -n`

The prettyprint argument (-n) writes the result in the "pretty" format.

## Example General Project Time Measurement

`java -jar cryptoguard.jar -t`

The time argument (-t) displays the time taken for the scanning.

## Example JAR Project Base

`java -jar cryptoguard.jar -in jar -s .../project.jar`

The format argument (-in) specifies the type of project (jar) and the source argument (-s) specifies the location of the project.

## Example JAR Project Dependency

`java -jar cryptoguard.jar -in jar -s .../project.jar -d .../lib/file(s).jar`

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

## Example JAR Project Fileout

`java -jar cryptoguard.jar -in jar -s .../project.jar -m D -o .../fileout.json`

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

## Example JAR Project Sample Test 

`java -jarc ryptoguard.jar -in jar -s cryptoguard/samples/testable-jar/build/libs/testable-jar.jar  -d cryptoguard/samples/testable-jar/build/dependencies  -m SX  -o cryptoguard/build/tmp/tempJarFile_Scarf_0.xml  -t  -H  -n -java .../jdk8`

The output format argument (-in) specifies the type of project (jar).
The source argument (-s) specifies the project to be scanned (.../testable-jar.jar).
The dependency argument (-d) specifies the directory of the dependencies (.../dependencies).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../tempJarFile_Scarf_0.xml).
The time argument (-t) displays time taken via the project.
The heuristic argument (-H) writes various heuristics taken to the output.
The prettyprint argument (-n) formats and writes the output into a "pretty" format.
The java argument (-java) specifies the java home, either java 7 or 8 for the internal library.

## Example Project Scanner Base

`java -jar cryptoguard.jar -in source -s .../project/`

The format argument (-in) specifies the type of project (source) and the source argument (-s) specifies the location of the project.
This must either be a gradle or maven based project.

## Example Project Scanner Dependency

`java -jar cryptoguard.jar -in source -s .../project/ -d .../lib/file(s).jar`

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

## Example Project Fileout

`java -jar cryptoguard.jar -in source -s .../project/ -m D -o .../fileout.json`

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

## Example Project Sample Test

`java -jar cryptoguard.jar -in source -s cryptoguard/samples/testable-jar  -d cryptoguard/samples/testable-jar/build/dependencies  -m L  -o cryptoguard/build/tmp/testable-jar.txt  -t  -H  -n `

The output format argument (-in) specifies the type of project (source).
The source argument (-s) specifies the project to be scanned (.../testable-jar).
The dependency argument (-d) specifies the directory of the dependencies (.../dependencies).
The output format argument (-m) specifies the type of result to write out (Legacy).
The output argument (-o) specifies the file to write out to (.../testable-jar.txt).
The time argument (-t) displays time taken via the project.
The heuristic argument (-H) writes various heuristics taken to the output.
The prettyprint argument (-n) formats and writes the output into a "pretty" format.

## Example Java File(s) Project Base Single File
**CURRENTLY UNSTABLE**

`java -jar cryptoguard.jar -in java -s .../test.java`

The format argument (-in) specifies the type of project (java) and the source argument (-s) specifies the file to be used.

## Example Java File(s) Project Multiple Files (Split via space)
**CURRENTLY UNSTABLE**

`java -jar cryptoguard.jar -in java -s .../test.java .../testTwo.java`

The argument (-s) specifies the file to be used, retrieving test.java and testTwo.java via the space between the arguments.

## Example Java File(s) Project Multiple Files (Split via classpath)
**CURRENTLY UNSTABLE**

`java -jar cryptoguard.jar -in java -s .../test.java:.../testTwo.java`

The argument (-s) specifies the file to be used, retrieving test.java and testTwo.java via the split by classpath (delimited by :).

## Example Java File(s) Project Multiple Files (Split via input.in file)
**CURRENTLY UNSTABLE**

`java -jar cryptoguard.jar -in java -s .../input.in`

The argument (-s) specifies the input.in file to be used. This file should contain a line delimited paths to the source file. This also works based on the 
ex. 
.../test.java
.../testTwo.java

## Example Java File Fileout
**CURRENTLY UNSTABLE**

`java -jar cryptoguard.jar -in java -s .../test.java -m D -o .../fileout.json`

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

## Example Java File Dependency
**CURRENTLY UNSTABLE**

`java -jar cryptoguard.jar -in java -s .../test.java -d .../lib/file(s).jar`

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

## Example Java File Test 
**CURRENTLY UNSTABLE**

`java -jar cryptoguard.jar -in java -s cryptoguard/samples/temp/tester/test.java  -m SX  -o cryptoguard/build/tmp/test_java.xml  -t  -vv  -n `

The output format argument (-in) specifies the type of project (java).
The source argument (-s) specifies the project to be scanned (.../test.java).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../test_java.xml).
The very verbose argument (-vv) displays all of the logs available.
The prettyprint argument (-n) formats and writes the output into a "pretty" format.

## Example Java Class File(s) Project Base

`java -jar cryptoguard.jar -in class -s .../test.class`

The format argument (-in) specifies the type of project (class) and the source argument (-s) specifies the location of the project.

## Example Java Class File(s) Project Multiple Files (Split via space)

`java -jar cryptoguard.jar -in class -s .../test.class .../testTwo.class`

The argument (-s) specifies the file to be used, retrieving test.class and testTwo.class via the space between the arguments.

## Example Java Class File(s) Project Multiple Files (Split via classpath)

`java -jar cryptoguard.jar -in class -s .../test.class:.../testTwo.class`

The argument (-s) specifies the file to be used, retrieving test.class and testTwo.class via the split by classpath (delimited by :).

## Example Java Class File(s) Project Multiple Files (Split via input.in file)

`java -jar cryptoguard.jar -in class -s .../input.in`

The argument (-s) specifies the input.in file to be used. This file should contain a line delimited paths to the source file. This also works based on the 
ex. 
.../test.class
.../testTwo.class

## Example Java Class File(s) Project Dependency

`java -jar cryptoguard.jar -in class -s .../test.class -d .../lib/file(s).jar`

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

## Example Java Class File Test 

`java -jar cryptoguard.jar -in class -s cryptoguard/samples/VerySimple/very.class -m SX  -o cryptoguard/build/tmp/verySimple_klass.xml  -n `

The output format argument (-in) specifies the type of project (java).
The source argument (-s) specifies the project to be scanned (.../test.java).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../verySimple_klass.xml).
The prettyprint argument (-n) formats and writes the output into a "pretty" format.

## Example APK Project Base

`java -jar cryptoguard.jar -in apk -s .../app-debug.apk`

The format argument (-in) specifies the type of project (apk) and the source argument (-s) specifies the location of the project.

## Example APK Project Dependency

`java -jar cryptoguard.jar -in apk -s  .../app-debug.apk -d .../lib/file(s).jar`

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

## Example APK Project Fileout

`java -jar cryptoguard.jar -in apk -s .../app-debug.apk -m D -o .../fileout.json`

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

## Example APK Project File Test 

`java -jar cryptoguard.jar -in apk -s cryptoguard/samples/app-debug.apk -m SX  -o cryptoguard/build/tmp/app-debug.xml  -n -android .../android_home`

The output format argument (-in) specifies the type of project (java).
The source argument (-s) specifies the project to be scanned (.../test.java).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../debug.xml).
The prettyprint argument (-n) formats and writes the output into a "pretty" format.
The android argument (-android) specifies the android home for the internal library.


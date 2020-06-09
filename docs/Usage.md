---
layout: page
title: General Usage for CryptoGuard
permalink: /usage/
---


# cryptoguard:$CVER 04.04.01$ General Usage
## Raw Arguments

| Id | Default | Description |
|-------------------|-----------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| -in | format | Required: The format of input you want to scan |
| -s | file/files/*.in/dir/xargs/ClassPathString | Required: The source to be scanned use the absolute path or send all of the source files via the file input.in ex. find -type f *.java >> input.in. |
| -d | dir | The dependency to be scanned use the relative path. |
| -o | file | The file to be created with the output default will be the project name. |
| -new | Flag | The file to be created with the output if existing will be overwritten. |
| -t | Flag | Output the time of the internal processes. |
| -m | formatType | The output format you want to produce |
| -n | Flag | Output the analysis information in a 'pretty' format. |
| -v | Flag | Output the version number. |
| -VX | Flag | Display logs only from the fatal logs |
| -V | Flag | Display logs from debug levels |
| -VV | Flag | Display logs from trace levels |
| -ts | Flag | Add a timestamp to the file output. |
| -depth | Flag | The depth of slicing to go into |
| -java | envVariable | Directory of Java to be used JDK 7 for JavaFiles/Project and JDK 8 for ClassFiles/Jar |
| -android | envVariable | Specify of Android SDK |
| -H | Flag | The flag determining whether or not to display heuristics. |
| -st | Flag | Stream the analysis to the output file. |
| -h | Flag | Print out the Help Information. |
| -main | className | Choose the main class if there are multiple main classes in the files given. |
| -Sconfig | file | Choose the Scarf property configuration file. |
| -Sassessfw | variable | The assessment framework |
| -Sassessfwversion | variable | The assessment framework version |
| -Sassessmentstartts | variable | The assessment start timestamp |
| -Sbuildfw | variable | The build framework |
| -Sbuildfwversion | variable | The build framework version |
| -Sbuildrootdir | dir | The build root directory |
| -Spackagename | variable | The package name |
| -Spackagerootdir | dir | The package root directory |
| -Spackageversion | variable | The package version |
| -Sparserfw | variable | The parser framework |
| -Sparserfwversion | variable | The parser framework version |
| -Suuid | uuid | The uuid of the current pipeline progress |


## Exceptions

| error code | error type |
|------------|-----------------------------------|
| 0 | Successful |
| 0 | Asking For Help |
| 0 | Asking For Version |
| 1 | General Argument Validation |
| 2 | Argument Value Validation |
| 7 | Format Specific Argument Validation |
| 15 | File Input Error |
| 16 | Reading File Error |
| 17 | File Not Available |
| 30 | File Output Error |
| 31 | Output File Creation Error |
| 32 | Error Closing The File |
| 45 | Environment Variable Not Set |
| 100 | Error Marshalling The Output |
| 120 | General Error Scanning The Program |
| 121 | Error Loading Class |
| 127 | Unknown |


## Format Types (used with the -in argument)

| Name | Flag | Description |
|------------------------|------|----------------------------------------------------------|
| JAR File | jar | To signal a Jar File to be scanned. |
| APK File | apk | To signal a APK File to be scanned. |
| Directory of Source Code | source | To signal the source directory of a Maven/Gradle Project. |
| Java File or Files | java | To signal a Java File(s to be scanned. |
| Class File or Files | class | To signal a Class File(s to be scanned. |

## Output Types (used with the -m argument, optional)

| Name | Flag |
|--------|--|
| Legacy | L |
| ScarfXML | SX |
| Default | D |



Listed Below are various examples of cmd line arguments with their explanations grouped by their project type.

## General
---
### General Project Version
> java -jar cryptoguard.jar -V

The version argument (-V) returns the version of the project and exits.

### General Project No Logging
> java -jar cryptoguard.jar -vx

The argument (-vx) only displays the fatal logs.

### General Project Verbose Logging
> java -jar cryptoguard.jar -v

The argument (-v) displays debug logs.

### General Project Very Verbose Logging
> java -jar cryptoguard.jar -vv

The argument (-vv) displays the all of the logs available.

### General Project Stream
> java -jar cryptoguard.jar -st

The argument (-st) enables streaming the results to whatever output file is specified.

### General Project Heuristics
> java -jar cryptoguard.jar -H

The argument (-H) writes the heuristics picked up in the output file.

### General Project Specifying the main file
> java -jar cryptoguard.jar -main

The argument (-main) specifies the main class (containing public static void main) if there are multiple within the project.

### General Project Java Home
> java -jar cryptoguard.jar -java

The argument (-java) sets the Java file path needed for an internal library. This is needed if the environment variable isn't set.
JDK 7 needed for either a Project or Java File Scanning.
JDK 8 needed for the other projects.

### General Project Android Home
> java -jar cryptoguard.jar -android

The argument (-android) sets the Android file path.needed for an internal library. This is needed if the environment variable isn't set.
Needed if an Android project is being scanned.

### General Project PrettyPrint
> java -jar cryptoguard.jar -n

The prettyprint argument (-n) writes the result in the "pretty" format.

### General Project Time Measurement
> java -jar cryptoguard.jar -t

The time argument (-t) displays the time taken for the scanning.

## APK
---
### APK Project Base
> java -jar cryptoguard.jar -in apk -s .../app-debug.apk

The format argument (-in) specifies the type of project (apk) and the source argument (-s) specifies the location of the project.

### APK Project Dependency
> java -jar cryptoguard.jar -in apk -s  .../app-debug.apk -d .../lib/file(s).jar

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

### APK Project Fileout
> java -jar cryptoguard.jar -in apk -s .../app-debug.apk -m D -o .../fileout.json

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

### APK Project File Test 
> java -jar cryptoguard.jar -in apk -s cryptoguard/samples/app-debug.apk -m SX  -o cryptoguard/build/tmp/app-debug.xml  -n -android .../android_home

The output format argument (-in) specifies the type of project (java).
The source argument (-s) specifies the project to be scanned (.../test.java).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../debug.xml).
The prettyprint argument (-n) formats and writes the output into a "pretty" format.
The android argument (-android) specifies the android home for the internal library.

## JAR
---
### JAR Project Base
> java -jar cryptoguard.jar -in jar -s .../project.jar

The format argument (-in) specifies the type of project (jar) and the source argument (-s) specifies the location of the project.

### JAR Project Dependency
> java -jar cryptoguard.jar -in jar -s .../project.jar -d .../lib/file(s).jar

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

### JAR Project Fileout
> java -jar cryptoguard.jar -in jar -s .../project.jar -m D -o .../fileout.json

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

### JAR Project Sample Test 
> java -jar cryptoguard.jar -in jar -s cryptoguard/samples/testable-jar/build/libs/testable-jar.jar  -d cryptoguard/samples/testable-jar/build/dependencies  -m SX  -o cryptoguard/build/tmp/tempJarFile_Scarf_0.xml  -t  -H  -n -java .../jdk8

The output format argument (-in) specifies the type of project (jar).
The source argument (-s) specifies the project to be scanned (.../testable-jar.jar).
The dependency argument (-d) specifies the directory of the dependencies (.../dependencies).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../tempJarFile_Scarf_0.xml).
The time argument (-t) displays time taken via the project.
The heuristic argument (-H) writes various heuristics taken to the output.
The prettyprint argument (-n) formats and writes the output into a "pretty" format.
The java argument (-java) specifies the java home, either java 7 or 8 for the internal library.



## Java
---
### Java File(s) Project Base Single File
> java -jar cryptoguard.jar -in java -s .../test.java

The format argument (-in) specifies the type of project (java) and the source argument (-s) specifies the file to be used.

### Java File(s) Project Multiple Files (Split via space)
> java -jar cryptoguard.jar -in java -s .../test.java .../testTwo.java

The argument (-s) specifies the file to be used, retrieving test.java and testTwo.java via the space between the arguments.

### Java File(s) Project Multiple Files (Split via classpath)
> java -jar cryptoguard.jar -in java -s .../test.java:.../testTwo.java

The argument (-s) specifies the file to be used, retrieving test.java and testTwo.java via the split by classpath (delimited by :).

### Java File(s) Project Multiple Files (Split via input.in file)
> java -jar cryptoguard.jar -in java -s .../input.in

The argument (-s) specifies the input.in file to be used. This file should contain a line delimited paths to the source file. This also works based on the 
ex. 
.../test.java
.../testTwo.java

### Java File Fileout
> java -jar cryptoguard.jar -in java -s .../test.java -m D -o .../fileout.json

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

### Java File Dependency
> java -jar cryptoguard.jar -in java -s .../test.java -d .../lib/file(s).jar

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

### Java File Test 
> java -jar cryptoguard.jar -in java -s cryptoguard/samples/temp/tester/test.java  -m SX  -o cryptoguard/build/tmp/test_java.xml  -t  -vv  -n 

The output format argument (-in) specifies the type of project (java).
The source argument (-s) specifies the project to be scanned (.../test.java).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../test_java.xml).
The very verbose argument (-vv) displays all of the logs available.
The prettyprint argument (-n) formats and writes the output into a "pretty" format.

## Project
---
### Project Scanner Base
> java -jar cryptoguard.jar -in source -s .../project/

The format argument (-in) specifies the type of project (source) and the source argument (-s) specifies the location of the project.
This must either be a gradle or maven based project.

### Project Scanner Dependency
> java -jar cryptoguard.jar -in source -s .../project/ -d .../lib/file(s).jar

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

### Project Fileout
> java -jar cryptoguard.jar -in source -s .../project/ -m D -o .../fileout.json

The output format argument (-m) specifies the type of output to write amd the output argument (-o) specifies the file to write the results to.

### Project Sample Test 
> java -jar cryptoguard.jar -in source -s cryptoguard/samples/testable-jar  -d cryptoguard/samples/testable-jar/build/dependencies  -m L  -o cryptoguard/build/tmp/testable-jar.txt  -t  -H  -n 

The output format argument (-in) specifies the type of project (source).
The source argument (-s) specifies the project to be scanned (.../testable-jar).
The dependency argument (-d) specifies the directory of the dependencies (.../dependencies).
The output format argument (-m) specifies the type of result to write out (Legacy).
The output argument (-o) specifies the file to write out to (.../testable-jar.txt).
The time argument (-t) displays time taken via the project.
The heuristic argument (-H) writes various heuristics taken to the output.
The prettyprint argument (-n) formats and writes the output into a "pretty" format.

## Class
---
### Java Class File(s) Project Base
> java -jar cryptoguard.jar -in class -s .../test.class

The format argument (-in) specifies the type of project (class) and the source argument (-s) specifies the location of the project.

### Java Class File(s) Project Multiple Files (Split via space)
> java -jar cryptoguard.jar -in class -s .../test.class .../testTwo.class

The argument (-s) specifies the file to be used, retrieving test.class and testTwo.class via the space between the arguments.

### Java Class File(s) Project Multiple Files (Split via classpath)
> java -jar cryptoguard.jar -in class -s .../test.class:.../testTwo.class

The argument (-s) specifies the file to be used, retrieving test.class and testTwo.class via the split by classpath (delimited by :).

### Java Class File(s) Project Multiple Files (Split via input.in file)
> java -jar cryptoguard.jar -in class -s .../input.in

The argument (-s) specifies the input.in file to be used. This file should contain a line delimited paths to the source file. This also works based on the 
ex. 
.../test.class
.../testTwo.class

### Java Class File(s) Project Dependency
> java -jar cryptoguard.jar -in class -s .../test.class -d .../lib/file(s).jar

The format argument (-d) specifies the directory of the dependencies to be used with the project and picks up the file.jar.

### Java Class File Test 
> java -jar cryptoguard.jar -in class -s cryptoguard/samples/VerySimple/very.class -m SX  -o cryptoguard/build/tmp/verySimple_klass.xml  -n 

The output format argument (-in) specifies the type of project (java).
The source argument (-s) specifies the project to be scanned (.../test.java).
The output format argument (-m) specifies the type of result to write out (Scarf).
The output argument (-o) specifies the file to write out to (.../verySimple_klass.xml).
The prettyprint argument (-n) formats and writes the output into a "pretty" format.
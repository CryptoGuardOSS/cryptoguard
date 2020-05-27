---
layout: page
title: Example Raw Arguments for CryptoGuard
permalink: /raw/
---

# CryptoGuard $CVER 04.04.00$ Raw Arguments

## Name: FORMAT
* Arg: -in
* Required: The format of input you want to scan
* Default: format
* Usage: java -jar cryptoguard -in format

### Different Options available for project types:
* jar: JAR File To signal a Jar File to be scanned.)
* apk: APK File To signal a APK File to be scanned.)
* source: Directory of Source Code To signal the source directory of a Maven/Gradle Project.)
* java: Java File or Files To signal a Java File(s) to be scanned.)
* class: Class File or Files To signal a Class File(s) to be scanned.);

## Name: SOURCE
* Arg: -s
* Required: The source to be scanned use the absolute path or send all of the source files via the file input.in; ex. find -type f *.java >> input.in.
* Default: file/files/*.in/dir/ClassPathString
* Usage: java -jar cryptoguard -s file/files/*.in/dir/ClassPathString

## Name: DEPENDENCY
Arg: -d
The dependency to be scanned use the relative path.
* Default: dir
* Usage: java -jar cryptoguard -d dir

## Name: OUT
Arg: -o
The file to be created with the output default will be the project name.
* Default: file
* Usage: java -jar cryptoguard -o file

## Name: NEW
Arg: -new
The file to be created with the output if existing will be overwritten.
* Usage: java -jar cryptoguard -new

## Name: TIMEMEASURE
Arg: -t
Output the time of the internal processes.
* Usage: java -jar cryptoguard -t

## Name: FORMATOUT
Arg: -m
The output format you want to produce
* Default: formatType
* Usage: java -jar cryptoguard -m formatType

Different Options available for output format:
* L: Legacy
* SX: ScarfXML
* D: Default

## Name: PRETTY
Arg: -n
Output the analysis information in a 'pretty' format.
* Usage: java -jar cryptoguard -n

## Name: EXPERIMENTRESULTS
Arg: -exp
View the experiment based results.
* Usage: java -jar cryptoguard -exp

## Name: VERSION
Arg: -V
Output the version number.
* Usage: java -jar cryptoguard -V

## Name: NOLOGS
Arg: -vx
Display logs only from the fatal logs
* Usage: java -jar cryptoguard -vx

## Name: VERBOSE
Arg: -v
Display logs from debug levels
* Usage: java -jar cryptoguard -v

## Name: VERYVERBOSE
Arg: -vv
Display logs from trace levels
* Usage: java -jar cryptoguard -vv

## Name: TIMESTAMP
Arg: -ts
Add a timestamp to the file output.
* Usage: java -jar cryptoguard -ts

## Name: DEPTH
Arg: -depth
The depth of slicing to go into
* Usage: java -jar cryptoguard -depth

## Name: LOG
Arg: -L
Enable logging to the console.
* Usage: java -jar cryptoguard -L

## Name: JAVA
Arg: -java
Directory of Java to be used JDK 7 for JavaFiles/Project and JDK 8 for ClassFiles/Jar
* Default: envVariable
* Usage: java -jar cryptoguard -java envVariable

## Name: ANDROID
Arg: -android
Specify of Android SDK
* Default: envVariable
* Usage: java -jar cryptoguard -android envVariable

## Name: HEURISTICS
Arg: -H
The flag determining whether or not to display heuristics.
* Usage: java -jar cryptoguard -H

## Name: STREAM
Arg: -st
Stream the analysis to the output file.
* Usage: java -jar cryptoguard -st

## Name: HELP
Arg: -h
Print out the Help Information.
* Usage: java -jar cryptoguard -h

## Name: MAIN
Arg: -main
Choose the main class if there are multiple main classes in the files given.
* Default: className
* Usage: java -jar cryptoguard -main className
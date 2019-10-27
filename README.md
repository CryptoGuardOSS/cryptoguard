# CryptoGuard V03.07.05

An program analysis tool to find cryptographic misuse in Java and Android.

## Building From Source
---
* Run `make`, this will build CryptoGuard and move the jar to the current directory
* Run `scans` to scan all of the tests included in the source
  * There is currently a sample project for each scan type within `src/test`
* Run `clean` to clean the entire project

## Prerequisites (Environment Variables)
---
1. JAVA8_HOME: Point to a valid Java 8 JDK Installation
   * Needed for all of the scans
#. JAVA7_HOME: Point to a valid Java 7 JDK Installation
   * Needed for all of the scans
#. ANDROID_SDK_HOME: Point to a valid Android JDK Installation
   * Needed for Android

### Note
---
* Run `make checkEnv` to verify all of these variables are set.
* This currently is stable on **Java 1.7**

## Different Scanning Options

### Source (Maven or Gradle Project Directory **only**) *(Unstable)*
---
* sample Makefile command `make scanDir`
* raw command (without dependencies) `java7 -jar cryptoguard.jar -in source -s /rootPath`
* raw command (with dependencies) `java7 -jar cryptoguard.jar -in source -s /rootPath -d /dependencies`

**Note**\
If the project have external dependencies then first gather the dependencies under a folder that is relative to the project root (e.g., “build/dependencies”).

If you have multiple subprojects with external dependencies, then you have to gather all the corresponding subproject dependencies under a path that is relative to each of the subprojects.

### JAR Files
---
* sample Makefile command `make scanJar`
* raw command `java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar`

### APK Files
---
* sample Makefile command `make scanAPK`
* raw command `java7 -jar cryptoguard.jar -in apk -s /path/to/apk/my-apk.apk`

### Java Files *(Experimental)*
---
* sample Makefile command `make scanJava`
* raw command `java7 -jar cryptoguard.jar -in java -s /path/to/java/file.java`
* raw command (for files) `java7 -jar cryptoguard.jar -in java -s /path/to/java/file1.java /path/to/java/file2.java`

**Note**
* Due to library constraints, Java File Scanning is currently limited to **Java 1.7 files and below**


### Java Class Files *(Experimental)*
---
* sample Makefile command `make scanClass`
* raw command `java7 -jar cryptoguard.jar -in class -s /path/to/java/file.class`
* raw command (for files) `java7 -jar cryptoguard.jar -in java -s /path/to/java/file1.class /path/to/java/file2.class`

## Special Argument Specifications

### Different Scanning Output Options
---

How To Choose
* using the argument `-m`, you can add the identifier of the other output formats

#### Default
---
* Argument `-m D`
  * example command `java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m D`
* This is a json file that represents a more simplified output
  * This will be iterated over time as more functionality is available

#### Legacy
---
* Argument `-m L`
  * example command `java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m L`
* This will output a txt file used within early CryptoGuard versions

#### Scarf XML
---
* Argument `-m SX`
  * example command `java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX`
  * This will ouput an xml file using the [scarf_v1.2.xsd](https://github.com/mirswamp/resultparser/blob/master/xsd/scarf_v1.2.xsd) used by [SWAMP](https://continuousassurance.org/open-source-software/).
* By using the argument `-Sconfig properties.file`, this will load the properties from within the file
  * example command `java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX -Sconfig properties.file`

### Input a single file "List of files"
---
* Argument `-s *.in`
* By using this argument (and you have to use the .in extension) input a single file containing all of the source input files
* example
  * command to generate a file like this `find -type f -name *.java >> input.in`
  * generated [input.in]() file
    ```bash
    ./samples/testable-jar/src/main/java/tester/UrlFrameWorks.java
    ./samples/testable-jar/src/main/java/tester/PasswordUtils.java
    ./samples/testable-jar/src/main/java/tester/Crypto.java
    ./samples/testable-jar/src/main/java/tester/PBEUsage.java
    ./samples/testable-jar/src/main/java/tester/NewTestCase2.java
    ./samples/testable-jar/src/main/java/tester/VeryBusyClass.java
    ./samples/testable-jar/src/main/java/tester/SymCrypto.java
    ./samples/testable-jar/src/main/java/tester/NewTestCase1.java
    ./samples/testable-jar/src/main/java/tester/LiveVarsClass.java
    ./samples/testable-jar/src/main/java/tester/PassEncryptor.java
    ```
  * command to be used with cryptoguard `java7 -jar -s input.in`
    * this is similar to `java7 -jar -s ./samples/testable-jar/src/main/java/tester/UrlFrameWorks.java ... ./samples/testable-jar/src/main/java/tester/PassEncryptor.java`
* **NOTE**: This can be used with any type of input/output marshalling

### Using the Class Path Argument
---
* Argument `-auxclasspath "path/to/jar.jar:path/to/class.class:"`
* This argument can be used to add dependencies via the class path string instead of directly via a commented string
* example
  * command to be used with cryptoguard `java7 -jar -s ./firstpackage/ -auxclasspath ./dep/libs/stringutils.jar:./dep/libs/test.jar`
  * similar to the regular command `java7 -jar -s ./firstpackage/ -d ./dep/libs/stringutils.jar ./dep/libs/test.jar`
* **NOTE**: This can be used with any type of input/output marshalling

## Help
---
* If you have any questions or suggestions, please email to [cryptoguardorg@gmail.com](mailto:cryptoguardorg@gmail.com).
* Please also run `make help` or `java7 -jar cryptoguard.jar -h` for argument help.

**FAQ**
* There may be **silent failures** if any of the environment variables below are not set.
  * This can be checked by running `make checkEnv`

## Disclaimer
---

CryptoGuard is a research prototype under GNU General Public License 3.0

 Copyright © 2019 CryptoGuard

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 3.0 for more details.
 
 You should have received a copy of the GNU General Public License 3.0 along with this program.  If not, see https://www.gnu.org/licenses/gpl-3.0.html.
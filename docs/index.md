---
layout: default
---

CryptoGuard $CVER 04.04.01$
=====================
<p align="center">
    <a href="https://mybinder.org/v2/gh/franceme/cryptoguard/master?filepath=Notebook%2F">
        <img src="https://mybinder.org/badge_logo.svg" alt="MyBinder Badge">
    </a>
</p>

A program analysis tool to find cryptographic misuse in Java and Android.
--------------------------------------------------------------------------

### CryptoSouple.py

This python file is the wrapper created to assist with various portions of the project. You should use it for various commands including:

-   setting the environment
-   clean the project
-   building the project
-   running tests
-   building a simple command

[MyBinder](https://mybinder.org/)
==========================================
This is a website hosting a Docker image that actively runs either Java or Python3 samples. The Java Notebook is only possible by utilitzing [IJava](https://github.com/SpencerPark/IJava). This is still under progress as the following are not active yet:
-   Android SDK tests
-   Sample Java Tests

Please run the following command to get more information on how to use it `./cryptosouple.py`.

### IMPORTANT NOTICE

-   Due to \'leaky tests\' from the environment and persistent
    variables, a python test runner was made
-   Please run `make tests`
    -   this ensures isolated environments via gradle calls

### Building From Source

-   Run `make`, this will build CryptoGuard and move the
    jar to the current directory
-   Run `scans` to scan all of the tests included in the source
    -   There is currently a sample project for each scan type within src/test
-   Run `clean` to clean the entire project

### Prerequisites (Environment Variables)

1.  JAVA\_HOME: Point to a valid Java 8 JDK Installation
    -   Needed for all of the scans

2.  JAVA7\_HOME: Point to a valid Java 7 JDK Installation
    -   Needed for project scans and java file scans

3.  ANDROID\_SDK\_HOME: Point to a valid Android JDK Installation
    -   Needed for Android

#### Note

-   Run `make env` to verify (and set) all of these variables by following the instructions.

### Different Scanning Options

#### Source (Maven or Gradle Project Directory **only**)

-   raw command (without dependencies)
    `java -jar cryptoguard.jar -in source -s /rootPath`
-   raw command (with dependencies)
    `java -jar cryptoguard.jar -in source -s /rootPath -d /dependencies`

##### Note

If the project have external dependencies then first gather the dependencies under a folder that is relative to the project root (e.g., \"build/dependencies\").

If you have multiple subprojects with external dependencies, then you have to gather all the corresponding subproject dependencies under a path that is relative to each of the subprojects.

#### JAR Files

-   raw command `java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar`

#### APK Files

-   raw command `java -jar cryptoguard.jar -in apk -s /path/to/apk/my-apk.apk`

#### Java Files (*Currently Unstable*, currently limited to Java JDK 8 by library constraint)

-   raw command `java -jar cryptoguard.jar -in java -s /path/to/java/file.java`
-   raw command (for files) `java -jar cryptoguard.jar -in java -s /path/to/java/file1.java /path/to/java/file2.java`

#### Java Class Files (Currently limited to Java JDK 8)

-   raw command `java -jar cryptoguard.jar -in class -s /path/to/java/file.class`
-   raw command (for files) `java -jar cryptoguard.jar -in java -s /path/to/java/file1.class /path/to/java/file2.class`

### Different Scanning Options

#### Source/Dependencies options

-   By default, both source and dependencies are expecting a list of files
    -   or for source the .in file
-   The source and dependencies can also handle a class path based string
    -   ex: `-s ~/tester/PBEUsage.class:~/tester/UrlFrameWorks.class:~/tester/NewTestCase1.class:~/tester/NewTestCase2.class`

#### Output options

-   using the argument `-m`, you can add the identifier of the other output formats

##### Default

-   Argument `-m D`
    -   example command `java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m D`

-   This is a json file that represents a more simplified output
    -   This will be iterated over time as more functionality is available

##### Legacy

-   Argument `-m L`
    -   example command `java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m L`
-   This will output a txt file used within early CryptoGuard versions

##### Scarf XML

-   Argument `-m SX`
    -   example command `java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX`

-   This will ouput an xml file using the [scarf\_v1.2.xsd](https://github.com/mirswamp/resultparser/blob/master/xsd/scarf_v1.2.xsd) used by [SWAMP](https://continuousassurance.org/open-source-software/).
-   By using the argument `-Sconfig properties.file`, this will load the properties from within the file
    -   example command `java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX -Sconfig properties.file`

#### Input a single file (list of files)

-   Argument `-s *.in`
-   By using this argument (and you have to use the .in extension) input a single file containing all of the source input files
-   example
    -   command to generate a file like this `find -type f -name \*.java \>\> input.in`
        -   generated input.in file

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
./samples/testable-jar/src/main/java/tester/PassEncryptor.java}
```

-   command to be used with cryptoguard `java -jar -s input.in`
    -   this is similar to `java -jar -s ./samples/testable-jar/src/main/java/tester/UrlFrameWorks.java ... ./samples/testable-jar/src/main/java/tester/PassEncryptor.java`

-   **NOTE**: This can be used with any type of input/output marshalling

### Help

-   If you have any questions or suggestions, please email to <sazzad114@gmail.com>.
-   Please also run `make help` or `java -jar cryptoguard.jar -h` for argument help.
-   You can also look at the auto-generated USAGE.md file.

#### FAQ

-   There may be **silent failures** if any of the environment variables below are not set.
    -   This can be checked by running `make env`

### Website

-   The generated website uses a slightly modified [Jekyll Hyde template](https://github.com/poole/hyde) under the [MIT License](https://github.com/poole/hyde/blob/master/LICENSE.md).
- Technology Reports
    - Gradle Junit Test Reports are generated from [Gradle](https://gradle.org/)
    - Cobertura Test Coverage Reports are generated from [Cobertura](http://cobertura.sourceforge.net/)
    - Java Documentation are generated from Java

### Disclaimer

#### CryptoGuard is a research prototype under GNU General Public License 3.0

Copyright © 2020 CryptoGuard

This program is free software: you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later
version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
Public License 3.0 for more details.

You should have received a copy of the GNU General Public License 3.0
along with this program. If not, see
<https://www.gnu.org/licenses/gpl-3.0.html>.

### Reference

If you find this project useful, please cite our [CCS\'19 CryptoGuard
paper](https://dl.acm.org/citation.cfm?doid=3319535.3345659) and the thesis supporting the latest enhancements 
from within this fork.

```latex
@phdthesis{frantz2020enhancing,
	title={Enhancing CryptoGuard's Deployability for Continuous Software Security Scanning},
	author={Frantz, Miles},
	year={2020},
	school={Virginia Tech}
}
```
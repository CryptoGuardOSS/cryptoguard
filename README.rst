=============================
CryptoGuard: Source Code
=============================

An program analysis tool to find cryptographic misuse in Java and Android.
Current Version: V03.03.09

Prerequisites to use CryptoGuard:
---------------------------------

1. Download and Install JDK 7.
#. Set `JAVA7_HOME` environment variable.
#. Download and Install Gradle.

Additional Prerequisite to use CryptoGuard on Apk:
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Download and Install Android SDK
#. Set `ANDROID_SDK_HOME`

How to run CryptoGuard:
-----------------------

1. Run `cd /path/to/cryptoguard`
#. Run `make build`

Source Directory
^^^^^^^^^^^^^^^^

You need to run CryptoGuard on JDK 7 to analyze source codes. If the project does not have any external dependencies then run,
     
`cd /path/to/cryptoguard &&  /usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main-{Version Here}.jar -in source -s /path/to/project/root`

If the project have external dependencies then first gather the dependencies under a folder that is relative to the project root (e.g., “build/dependencies”), then run

`cd /path/to/cryptoguard && /usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main-{Version Here}.jar -in source -s /path/to/project/root -d {fullPath}/build/dependencies`

Note that if you have multiple subprojects with external dependencies, then you have to gather all the corresponding subproject dependencies under a path that is relative to each of the subprojects.

JAR
^^^

`cd /path/to/cryptoguard && java -jar main/build/libs/main.jar -in jar -s /path/to/jar/my-jar-{Version Here}.jar`

APK
^^^

`cd /path/to/cryptoguard && java -jar main/build/libs/main-{Version Here}.jar -in apk -s /path/to/apk/my-apk.apk`

Java Files
^^^^^^^^^^

`cd /path/to/cryptoguard && java -jar main/build/libs/main-{Version Here}.jar -in java -s /path/to/java/files.java's`

Java Class Files (Experimental)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

`cd /path/to/cryptoguard && java -jar main/build/libs/main-{Version Here}.jar -in class -s /path/to/java/files.class's`

Help
----
* If you have any questions or suggestions, please email to cryptoguardorg@gmail.com.
* Please also run `make help` or `cd /path/to/grptoguard && java -jar main/build/libs/main-{Version Here}.jar -h` for argument help.

FAQ
^^^
* There may be ***silent failures*** if any of the environment variables below are not set.

+------------------------+--------------------+----------------------------------------+------------------------------------+
| Build Type             | Env. Variable Name | Path Resolution                        | Example                            |
+------------------------+--------------------+----------------------------------------+------------------------------------+
| JAR/Dir/APK/Java Class | JAVA_HOME          | Should resolve to the bin of Java 7    | export PATH=...:~/java7/bin:...    |
+------------------------+--------------------+----------------------------------------+------------------------------------+
| Source/Java Files      | JAVA7_HOME         | Should resolve to the bin of Java 7    | export PATH=...:~/java7/bin:...    |
+------------------------+--------------------+----------------------------------------+------------------------------------+
| APK                    | ANDROID_HOME       | Should resolve to the SDK Android path | export PATH=...:~/Android/Sdk/:... |
+------------------------+--------------------+----------------------------------------+------------------------------------+

* Also listed below is the current support (tldr) of the project across the various operating systems

+--------------------+-----------+---------+
| OS                 | Supported | Planned |
+--------------------+-----------+---------+
| Ubuntu             | Yes       | ~       |
+--------------------+-----------+---------+
| Linux (Non-ubuntu) | ?         | ?       |
+--------------------+-----------+---------+
| Mac                | No        | No      |
+--------------------+-----------+---------+
| Windows            | No        | No      |
+--------------------+-----------+---------+

Disclaimer
-----------

CryptoGuard is a research prototype under GNU General Public License 3.0
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

 Copyright © 2018 CryptoGuard

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 3.0 for more details.
 
 You should have received a copy of the GNU General Public License 3.0 along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.html>.



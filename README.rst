#################################
CryptoGuard V03.07.04
#################################


An program analysis tool to find cryptographic misuse in Java and Android.
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""


Building From Source
==================================================
* Run :code:`make`, this will build CryptoGuard and move the jar to the current directory
* Run :code:`scans` to scan all of the tests included in the source
    * There is currently a sample project for each scan type within `src/test`
* Run :code:`clean` to clean the entire project

Prerequisites (Environment Variables)
==================================================
1. JAVA8_HOME: Point to a valid Java 8 JDK Installation
    * Needed for all of the scans
#. JAVA7_HOME: Point to a valid Java 7 JDK Installation
    * Needed for all of the scans
#. ANDROID_SDK_HOME: Point to a valid Android JDK Installation
    * Needed for Android

Note
-----------
* Run :code:`make checkEnv` to verify all of these variables are set.
* This currently is stable on **Java 1.7**

Different Scanning Options
==================================================

Source (Maven or Gradle Project Directory **only**)
----------------------------------------------------------
* sample Makefile command :code:`make scanDir`
* raw command (without dependencies) :code:`java7 -jar cryptoguard.jar -in source -s /rootPath`
* raw command (with dependencies) :code:`java7 -jar cryptoguard.jar -in source -s /rootPath -d /dependencies`

Note
^^^^
If the project have external dependencies then first gather the dependencies under a folder that is relative to the project root (e.g., “build/dependencies”).

If you have multiple subprojects with external dependencies, then you have to gather all the corresponding subproject dependencies under a path that is relative to each of the subprojects.

JAR Files
----------------------------------------------------------
* sample Makefile command :code:`make scanJar`
* raw command :code:`java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar`

APK Files
----------------------------------------------------------
* sample Makefile command :code:`make scanAPK`
* raw command :code:`java7 -jar cryptoguard.jar -in apk -s /path/to/apk/my-apk.apk`

Java Files
----------------------------------------------------------
* sample Makefile command :code:`make scanJava`
* raw command :code:`java7 -jar cryptoguard.jar -in java -s /path/to/java/file.java`
* raw command (for files) :code:`java7 -jar cryptoguard.jar -in java -s /path/to/java/file1.java /path/to/java/file2.java`

Note
^^^^^
* Due to library constraints, Java File Scanning is currently limited to **Java 1.7 files and below**


Java Class Files (Experimental)
----------------------------------------------------------
* sample Makefile command :code:`make scanClass`
* raw command :code:`java7 -jar cryptoguard.jar -in class -s /path/to/java/file.class`
* raw command (for files) :code:`java7 -jar cryptoguard.jar -in java -s /path/to/java/file1.class /path/to/java/file2.class`

Note
^^^^
* This is still **experimental**


Different Scanning Output Options
==================================================

How To Choose
-----------------
* using the argument :code:`-m`, you can add the identifier of the other output formats

Default
-------------
* Argument :code:`-m D`
    * example command :code:`java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m D`
* This is a json file that represents a more simplified output
    * This will be iterated over time as more functionality is available

Legacy
-------------
* Argument :code:`-m L`
    * example command :code:`java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m L`
* This will output a txt file used within early CryptoGuard versions

Scarf XML
-------------
* Argument :code:`-m SX`
    * example command :code:`java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX`
* This will ouput an xml file using the `scarf_v1.2.xsd <https://github.com/mirswamp/resultparser/blob/master/xsd/scarf_v1.2.xsd>`_ used by `SWAMP <https://continuousassurance.org/open-source-software/>`_.
* By using the argument :code:`-Sconfig properties.file`, this will load the properties from within the file
    * example command :code:`java7 -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX -Sconfig properties.file`

Help
----
* If you have any questions or suggestions, please email to `cryptoguardorg@gmail.com <mailto:cryptoguardorg@gmail.com>`_.
* Please also run :code:`make help` or :code:`java7 -jar cryptoguard.jar -h` for argument help.

FAQ
^^^
* There may be **silent failures** if any of the environment variables below are not set.
    * This can be checked by running :code:`make checkEnv`

Disclaimer
-----------

CryptoGuard is a research prototype under GNU General Public License 3.0
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

 Copyright © 2019 CryptoGuard

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 3.0 for more details.
 
 You should have received a copy of the GNU General Public License 3.0 along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.html>.



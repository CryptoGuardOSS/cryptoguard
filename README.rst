#################################
CryptoGuard V03.12.02
#################################

.. image:: https://mybinder.org/badge_logo.svg
 :target: https://mybinder.org/v2/gh/franceme/cryptoguard/dev?filepath=Notebook%2F

An program analysis tool to find cryptographic misuse in Java and Android.
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

CryptoGuard.py
=====================
This python file is the wrapper created to assist with various portions of the project.
You should use it for various commands including:

* setting the environment
* clean the project
* building the project
* running tests
* building a simple command

`MyBinder <https://mybinder.org/>`_
=====================
This is a website hosting a Docker image that actively runs either Java or Python3 samples.
The Java Notebook is only possible by utilitzing `IJava <https://github.com/SpencerPark/IJava>`_.
This is still under progress as the following are not active yet:

* Android SDK tests
* Sample Java Tests


Please run the following command to get more information on how to use it :code:`./cryptoguard.py`

IMPORTANT NOTICE
========================
* Due to 'leaky tests' from the environment and persistent variables, a python test runner was made
* Please run :code:`make tests`
    * this ensures isolated environments via gradle calls

Building From Source
==================================================
* Run :code:`make`, this will build CryptoGuard and move the jar to the current directory
* Run :code:`scans` to scan all of the tests included in the source
    * There is currently a sample project for each scan type within `src/test`
* Run :code:`clean` to clean the entire project

Prerequisites (Environment Variables)
==================================================
1. JAVA_HOME: Point to a valid Java 8 JDK Installation
    * Needed for all of the scans
#. JAVA7_HOME: Point to a valid Java 7 JDK Installation
    * Needed for project scans and java file scans
#. ANDROID_SDK_HOME: Point to a valid Android JDK Installation
    * Needed for Android

Note
-----------
* Run :code:`make env` to verify (and set) all of these variables by following the instructions.

Different Scanning Options
==================================================

Source (Maven or Gradle Project Directory **only**)
--------------------------------------------------------------------------------------------------------------------
* raw command (without dependencies) :code:`java -jar cryptoguard.jar -in source -s /rootPath`
* raw command (with dependencies) :code:`java -jar cryptoguard.jar -in source -s /rootPath -d /dependencies`

Note
^^^^
If the project have external dependencies then first gather the dependencies under a folder that is relative to the project root (e.g., "build/dependencies").

If you have multiple subprojects with external dependencies, then you have to gather all the corresponding subproject dependencies under a path that is relative to each of the subprojects.

JAR Files
----------------------------------------------------------
* raw command :code:`java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar`

APK Files
----------------------------------------------------------
* raw command :code:`java -jar cryptoguard.jar -in apk -s /path/to/apk/my-apk.apk`

Java Files (*Currently Unstable*, currently limited to Java JDK 8 by library constraint)
--------------------------------------------------------------------------------------------------------------------
* raw command :code:`java -jar cryptoguard.jar -in java -s /path/to/java/file.java`
* raw command (for files) :code:`java -jar cryptoguard.jar -in java -s /path/to/java/file1.java /path/to/java/file2.java`


Java Class Files (Currently limited to Java JDK 8)
----------------------------------------------------------
* raw command :code:`java -jar cryptoguard.jar -in class -s /path/to/java/file.class`
* raw command (for files) :code:`java -jar cryptoguard.jar -in java -s /path/to/java/file1.class /path/to/java/file2.class`


Different Scanning Options
==================================================

Source/Dependencies options
----------------------------------
* By default, both source and dependencies are expecting a list of files
    * or for source the .in file

* The source and dependencies can also handle a class path based string
    * ex: :code:`-s ~/tester/PBEUsage.class:~/tester/UrlFrameWorks.class:~/tester/NewTestCase1.class:~/tester/NewTestCase2.class`

Ouput options
-----------------
* using the argument :code:`-m`, you can add the identifier of the other output formats

Default
^^^^^^^^^^^^^^^^^
* Argument :code:`-m D`
    * example command :code:`java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m D`
* This is a json file that represents a more simplified output
    * This will be iterated over time as more functionality is available

Legacy
^^^^^^^^^^^^^^^^^
* Argument :code:`-m L`
    * example command :code:`java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m L`
* This will output a txt file used within early CryptoGuard versions

Scarf XML
^^^^^^^^^^^^^^^^^
* Argument :code:`-m SX`
    * example command :code:`java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX`
* This will ouput an xml file using the `scarf_v1.2.xsd <https://github.com/mirswamp/resultparser/blob/master/xsd/scarf_v1.2.xsd>`_ used by `SWAMP <https://continuousassurance.org/open-source-software/>`_.
* By using the argument :code:`-Sconfig properties.file`, this will load the properties from within the file
    * example command :code:`java -jar cryptoguard.jar -in jar -s /path/to/jar/my-jar.jar -m SX -Sconfig properties.file`

Input a single file (list of files)
--------------------------------------------
* Argument :code:`-s *.in`
* By using this argument (and you have to use the .in extension) input a single file containing all of the source input files
* example
    * command to generate a file like this `find -type f -name *.java >> input.in`
    * generated [input.in]() file

.. code-block:: bash
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

  * command to be used with cryptoguard :code:`java -jar -s input.in`
    * this is similar to :code:`java -jar -s ./samples/testable-jar/src/main/java/tester/UrlFrameWorks.java ... ./samples/testable-jar/src/main/java/tester/PassEncryptor.java`

* **NOTE**: This can be used with any type of input/output marshalling

Help
======
* If you have any questions or suggestions, please email to `sazzad114@gmail.com <mailto:sazzad114@gmail.com>`_.
* Please also run :code:`make help` or :code:`java -jar cryptoguard.jar -h` for argument help.
* You can also look at the auto-generated USAGE.md file.

FAQ
-----
* There may be **silent failures** if any of the environment variables below are not set.
    * This can be checked by running :code:`make env`

Disclaimer
===============

CryptoGuard is a research prototype under GNU General Public License 3.0
--------------------------------------------------------------------------------

 Copyright © 2020 CryptoGuard

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 3.0 for more details.

 You should have received a copy of the GNU General Public License 3.0 along with this program.  If not, see https://www.gnu.org/licenses/gpl-3.0.html.

Reference
===============

If you find this project useful, please cite our CCS'19 `CryptoGuard paper <https://dl.acm.org/citation.cfm?doid=3319535.3345659>`__.

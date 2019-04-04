=======================
Makefile explanation
=======================

Global Variables
----------------

+---------------+--------------------------------------------+-----------------------------------------------------------------+
| variable name |               **explanation**              |                           **variable**                          |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| dir           |            Location of  project.           | ./                                                              |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| java7         |              Location of jre7.             | ${JAVA7_HOME}/bin/java                                          |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| ver           |         Current version of project.        | V03.03.08                                                       |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| scan          |            The command to scan.            | $(java7) -jar $(dir)main/build/libs/main-$(ver).jar             |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| marshal       |     The location of the xml marshaller.    | $(dir)main/src/main/java/com/example/response/package-info.java |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| jarLoc        | Location of testable-jar.jar.              | $(dir)testable-jar/build/libs/testable-jar.jar                  |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| depLoc        | Location of testable-jar.jar dependencies. | $(dir)testable-jar/build/dependencies                           |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| dirLoc        | Location of testable-jar directory.        | $(dir)testable-jar                                              |
+---------------+--------------------------------------------+-----------------------------------------------------------------+
| apkLoc        | Location of an apk.                        | $(dir)                                                          |
+---------------+--------------------------------------------+-----------------------------------------------------------------+

Sample Commands
------------------


+---------------------------------------+--------------+----------------------------------------------+
|              **cmdType**              |  **makeCmd** |                **sample cmd**                |
+---------------------------------------+--------------+----------------------------------------------+
| Scanning a jar                        | make scanJar | $(scan) -in jar -s $(jarLoc) -d $(depLoc)    |
+---------------------------------------+--------------+----------------------------------------------+
| Scanning a project dir (Gradle/Maven) | make scanDir | $(scan) -in source -s $(dirLoc) -d $(depLoc) |
+---------------------------------------+--------------+----------------------------------------------+
| Scanning a APK                        | make scanAPK | $(scan) -in apk -s $(apkLoc)                 |
+---------------------------------------+--------------+----------------------------------------------+
| Scanning Java File(s)                 |      N/A     | $(scan) -in java -s $(jarLoc)                |
+---------------------------------------+--------------+----------------------------------------------+
| Scanning Java Class File(s)           |      N/A     | $(scan) -in class -s $(jarLoc) -d $(depLoc)  |
+---------------------------------------+--------------+----------------------------------------------+

Extra Build Commands
---------------------

* :code: `setNS`

	* This command sets the namespace for the ScarfXML marshalling, (POJO -> XML).
	* This is needed for integration tests to validate the output matches the XML Schema.

* :code: `rmvNS`

	* This command removes the namespace from the ScarfXML marshalling, (POJO -> XML).
	* This is needed for ScarfXML valid output, as the examples seen don't have the namespace attribute set.
	* This may be unneeded however for strict interpretation of the xml this may break the reader.

* :code: `fullBuild`

	1. Sets the namespace if it isn't set, (used for integration tests).
	#. Removes the testable-jar/settings.gradle.
		* This file is malformed since this project is a child project.
		* This project can only be built (on its own) without the file.
	#. Builds testable-jar.
	#. Creates the testable-jar/settings.gradle file.
		* To determine the testable-jar project is a valid gradle project, this file is needed.
		* This is needed for integration tests.
	#. Fully builds the cryptoguard project.

* :code: `buildNoTest`

	1. Removes the namespace from the ScarfXML marshalling, (POJO -> XML).
		* This is needed for ScarfXML valid output, as the examples seen don't have the namespace attribute set.
		* This may be unneeded however for strict interpretation of the xml this may break the reader.
	#. Builds the cryptoguard project without any testing.
		* The integration tests will fail since the ScarfXML integration tests require the namespace for validating against the ScarfXML Schema.

* :code: `build`

	1. Runs a full build of the two projects, with tests active to ensure the project works.
	#. Builds the cryptoguard project without namespace or tests for a valid distributable.
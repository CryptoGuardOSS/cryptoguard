.. |cutS| raw:: html

    <strike>

.. |cutE| raw:: html

    </strike>

=======================================
Project TODO
=======================================

BUGS
------------------

* [~] CWE's (several issues defaulting to CWE: -1)
    * [~] It may only show up when running the tests via cmdline?

TODO
------------------

1. [~] Verify
	1. [X] the scarf-output is valid
	#. [X] old paths still work
	#. [!] verify the experimental rules 'a' are not showing up
	#. [X] new paths work (jar/apk/dir)
	#. [!] java/class files work

#. [~] Ask
    * [X] For the ScarfXML Format, take in a property file
    * [X] Use the 1 - ... - 127 as error codes
    * [ ] possibly populate the std out/err
        * maybe make it a flag?
#. [ ] Add argument to overwrite file
#. [ ] Allow property file be sent in with arguments
#. [ ] Verify if format/argument matches format/stream
    * ex1: Format X doesn't support streaming and streaming flag enabled -> break and show error
    * ex2: Format Y only supports streaming and streaming flag not enabled -> send out warning in output

#. [ ] path adaptions
    * [ ] build-dir: full path
    * [ ] package-dir: relative paths
        * [ ] other paths are relative as well

#. [ ] Github release (from master only)
    * [ ] at the next minor update?
#. [ ] Need to get the package version, default to 0.
	* [ ] How to easily pull this dynamically from files?
		* [ ] Maven -> pom.xml
		* [ ] Gradle -> settings.gradle
		* [ ] jar?
		* [ ] apk?
		* [ ] java classes -> "-1"
		* [ ] java files -> "-1"
#. [~] Add time checks
	1. [X] time check for analysis
	#. [ ] time check for message parsing :sub:`only applicable for non-streaming`
#. [ ] Upgrade to JDK ?
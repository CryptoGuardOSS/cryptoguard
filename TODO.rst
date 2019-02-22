.. |cutS| raw:: html

    <strike>

.. |cutE| raw:: html

    </strike>

=======================================
Project TODO
=======================================

BUGS
------------------

* [ ] CWE's (several issues defaulting to CWE: -1)
* [ ] ScarfXML specific arguments (valid id/values are error-prone/not registering)
* [X] Fully wrap the output with exceptions
    * [X] |cutS| APK |cutE| Soot scanning isn't wrapped
    * [~] set error messages (Exception)
        * [X] code - 0-27 (0:success 1-27 = self-define)
        * [X] message

TODO
------------------

1. [ ] Verify
	1. [X] the scarf-output is valid
	#. [X] old paths still work
	#. [!] verify the experimental rules 'a' are not showing up
	#. [X] new paths work (jar/apk/dir)
	#. [~] java/class files work

#. [ ] Allow property file be sent in with arguments
#. [ ] path adaptions
    * [ ] build-dir: full path
    * [ ] package-dir: relative paths
        * [ ] other paths are relative as well

#. [ ] Github release (from master only)
    * [ ] at the next minor update?
#. Ask
	#. Documentation
		* [~] Inline
		* [~] TODO
		* [ ] *
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
#. [ ] Upgrade to JDK 1.8.182
	1. [ ] Remove the annoying rt.jar/jce.jar dependency
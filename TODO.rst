=======================================
Project TODO
=======================================

BUGS
------------------

* [ ] CWE's (several issues defaulting to CWE: -1)
	1. [ ] Investigate
	#. [ ] Fix
* [ ] ScarfXML specific arguments (valid id/values are error-prone/not registering)
	1. [ ] Investigate
	#. [ ] Fix

TODO
------------------

1. [ ] Verify
	1. [X] the scarf-output is valid
	#. [~] old paths still work
	#. [ ] verify the experimental rules 'a' are not showing up
	#. [~] new paths work (jar/apk/dir)
	#. [~] java/class files work
#. Help
	* [ ] Trim down on the help output ~ make it smarter
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
#. [ ] Check against SWAMP online
#. [~] Add time checks
	1. [X] time check for analysis
	#. [ ] time check for message parsing :sub:`only applicable for non-streaming`
	#. [ ] time  check for overall (*Stop time right before writing to file*)
#. [ ] Add ability to change whether source file points to relative or full path
#. [ ] Upgrade to JDK 1.8.182
	1. [ ] Remove the annoying rt.jar/jce.jar dependency
#. [ ] Document Everything
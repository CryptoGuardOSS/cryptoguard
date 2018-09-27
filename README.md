# RigorityJ: Source Code

An program analysis tool to find cryptographic misuse in Java and Android.

###Prerequisites to use RigorityJ on Source code:

1. Download and Install JDK 7.
2. Set `JAVA7_HOME` environment variable.
3. Download and Install Gradle.

###Prerequisites to use RigorityJ on Jar:

1. Download and Install JDK (version >= 7).
2. Set `JAVA_HOME` environment variable.
3. Download and Install Gradle.

###Prerequisites to use RigorityJ on Apk:

1. Download and Install JDK (version >= 7).
2. Set `JAVA_HOME` environment variable.
3. Download and Install Android SDK
4. Set `ANDROID_SDK_HOME`
5. Download and Install Gradle.

###Build RigorityJ
1. Run `cd /path/to/rigorityj`
2. Run `gradle clean build`

###Run RigorityJ to analyze source code:

You need to run RigorityJ on JDK 7 to analyze source codes. If the project does not have any external dependencies then run,
     
`cd /path/to/rigorityj &&  /usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/path/to/project/root" ""`

If the project have external dependencies then first gather the dependencies under a folder that is relative to the project root (e.g., “build/dependencies”), then run

`cd /path/to/rigorityj && /usr/local/jdk1.7.0_80/bin/java -jar main/build/libs/main.jar "source" "/path/to/project/root" "build/dependencies"`

Note that if you have multiple subprojects with external dependencies, then you have to gather all the corresponding subproject dependencies under a path that is relative to each of the subprojects.

###Run RigorityJ to analyze Jar:

`cd /path/to/rigorityj && java -jar main/build/libs/main.jar "jar" "/path/to/jar/my-jar.jar" ""`

###Run RigorityJ to analyze Apk:

`cd /path/to/rigorityj && java -jar main/build/libs/main.jar "apk" "/path/to/apk/my-apk.apk" `

If you have any questions or suggestions, please email to rigorityj@gmail.com.

## Disclaimer

##### RigorityJ is a research prototype under GNU General Public License 3.0

 Copyright © 2018 RigorityJ

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 3.0 for more details.
 
 You should have received a copy of the GNU General Public License 3.0 along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.html>.



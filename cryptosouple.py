#!/usr/bin/env python3

import argparse
import glob
import shutil
import datetime
import hashlib
import json
import os
import shlex
import signal
import subprocess
import sys
import time
from collections import OrderedDict
import re
import nbformat as nb

# TODO - Add xml/json results verification

'''####################################
#A utility class that contains the rest of the main common files
'''  ####################################
curdir = os.path.abspath(os.curdir)
gitPath = os.path.join(curdir, '.git')
failFast, offline = False, not os.path.exists(gitPath)
android, java7, java = os.environ.get('ANDROID_HOME'), os.environ.get('JAVA7_HOME'), os.environ.get('JAVA_HOME')
generalArg, streamTests, generalCmd, generalFile = None, False, None, None
verify = False

# // @formatter:off
# region Offline information
archivedInformation = {'properties': {'projectName': 'cryptoguard', 'groupName': 'vt.edu', 'versionNumber': '04.03.00', 'buildFrameWork': 'Java', 'buildVersion': '1.8.232', 'org.gradle.daemon': 'false', 'gradle.version': '6.0'}, 'rawArgs': {}, 'engineType': {}, 'outputType': {'  Legacy': {'type': 'Legacy', 'flag': 'L', 'outputExtension': '.txt'}, '  ScarfXML': {'type': 'ScarfXML', 'flag': 'SX', 'outputExtension': '.xml'}, '  Default': {'type': 'Default', 'flag': 'D', 'outputExtension': '.json'}, '  YAMLGeneric': {'type': 'Default', 'flag': 'Y', 'outputExtension': '.yaml'}, '  XMLGeneric': {'type': 'Default', 'flag': 'X', 'outputExtension': '.xml'}, '  CSVDefault': {'type': 'CSVDefault', 'flag': 'CSV', 'outputExtension': '.csv'}}, 'exceptionType': {'  SUCCESS': {'id': '0', 'messageType': 'Successful'}, '  HELP': {'id': '0', 'messageType': 'Asking For Help'}, '  VERSION': {'id': '0', 'messageType': 'Asking For Version'}, '  GEN_VALID': {'id': '1', 'messageType': 'General Argument Validation'}, '  ARG_VALID': {'id': '2', 'messageType': 'Argument Value Validation'}, '  FORMAT_VALID': {'id': '7', 'messageType': 'Format Specific Argument Validation'}, '  FILE_I': {'id': '15', 'messageType': 'File Input Error'}, '  FILE_READ': {'id': '16', 'messageType': 'Reading File Error'}, '  FILE_AFK': {'id': '17', 'messageType': 'File Not Available'}, '  FILE_O': {'id': '30', 'messageType': 'File Output Error'}, '  FILE_CON': {'id': '31', 'messageType': 'Output File Creation Error'}, '  FILE_CUT': {'id': '32', 'messageType': 'Error Closing The File'}, '  ENV_VAR': {'id': '45', 'messageType': 'Environment Variable Not Set'}, '  MAR_VAR': {'id': '100', 'messageType': 'Error Marshalling The Output'}, '  SCAN_GEN': {'id': '120', 'messageType': 'General Error Scanning The Program'}, '  LOADING': {'id': '121', 'messageType': 'Error Loading Class'}, '  UNKWN': {'id': '127', 'messageType': 'Unknown'}}}
# endregion
# // @formatter:on
# region Loading
class Loading(object):
    # region Online Reading
    def retrieveProperties(file='gradle.properties'):
        dyct = {}
        with open(file, 'r') as props:
            for line in props:
                if '=' in line:
                    key, value = line.split('=')
                    dyct[str(key.strip())] = str(value.strip())
        return dyct

    def parseEngineType(file='src/main/java/rule/engine/EngineType.java'):
        properties, starter, stopper = {}, False, False
        try:
            with open(file, 'r') as java:
                line = java.readline()
                while line and not stopper:

                    if line.strip().startswith("//endregion"):
                        stopper = True
                    elif (starter and not line.strip().startswith("//") and not line.strip().startswith(";")):
                        name, rest = line.split("(\"")
                        broken = rest.split(",")
                        properties[name] = {
                            'name': broken[0].replace("\"", "").strip(),
                            'flag': broken[1].replace("\"", "").strip(),
                            'extension': broken[2].replace("\"", "").strip(),
                            'helpInfo': broken[3].replace("\"", "").strip()
                        }
                    elif line.strip().startswith("//region Values"):
                        starter = True

                    line = java.readline()
        except:
            properties = {}
        return properties

    def parseExceptionType(file='src/main/java/frontEnd/Interface/outputRouting/ExceptionId.java'):
        properties, starter, stopper = {}, False, False
        with open(file, 'r') as java:
            line = java.readline()
            while line and not stopper:

                if starter and line.strip().startswith(";"):
                    stopper = True
                elif (starter and line.strip() != '' and not line.strip().startswith(
                        "//") and not line.strip().startswith(";")):
                    name, rest = line.split("(")
                    broken = rest.split(",")

                    properties[name] = {
                        'id': broken[0].replace("\"", "").strip(),
                        'messageType': broken[1].replace("\"", "").replace(")", "").strip()
                    }
                elif line.strip().startswith("//region Values"):
                    starter = True

                line = java.readline()
        return properties

    def parseArgs(file='src/main/java/frontEnd/argsIdentifier.java'):
        properties, starter, stopper = {}, False, False
        try:
            with open(file, 'r') as java:
                line = java.readline()
                while line and not stopper:

                    if line.strip().startswith("//endregion"):
                        stopper = True
                    elif (starter and not line.strip().startswith("//") and not line.strip().startswith(";")):
                        name, rest = line.split("(\"")
                        broken = rest.split(",")
                        properties[name] = {
                            'id': broken[0].replace("\"", "").strip(),
                            'defaultArg': broken[1].replace("\"", "").strip(),
                            'desc': broken[2].replace("\"", "").strip(),
                            'Required': 'Required' in broken[2]
                        }
                    elif line.strip().startswith("//region Values"):
                        starter = True

                    line = java.readline()
        except:
                properties = {}
        return properties

    def parseOutputType(file='src/main/java/frontEnd/MessagingSystem/routing/Listing.java'):
        properties, starter, stopper = {}, False, False
        with open(file, 'r') as java:
            line = java.readline()
            while line and not stopper:

                if line.strip().startswith("//endregion"):
                    stopper = True
                elif (starter and not line.strip().startswith("//") and not line.strip().startswith(";")):
                    name, rest = line.split("(\"")
                    broken = rest.split(",")
                    ext = broken[2].replace("\"", "").strip()
                    if ext == 'null':
                        ext = broken[4].split('.')
                        ext = ext[-1]
                        ext = "." + ext[:int(ext.index(')'))].lower()

                    properties[name] = {
                        'type': broken[0].replace("\"", "").strip(),
                        'flag': broken[1].replace("\"", "").strip(),
                        'outputExtension': ext
                    }
                elif line.strip().startswith("//region Values"):
                    starter = True

                line = java.readline()
        return properties

    # endregion
    # region Online/Offline Reading

    def getProperties():
        if offline:
            return archivedInformation['properties']
        else:
            return Loading.retrieveProperties()

    def getRawArgs():
        if offline:
            return archivedInformation['rawArgs']
        else:
            return Loading.parseArgs()

    def getEngineType():
        if offline:
            return archivedInformation['engineType']
        else:
            return Loading.parseEngineType()

    def getDisplayOutputTypes():
        if offline:
            return archivedInformation['outputType']
        else:
            return Loading.parseOutputType()

    def getDisplayExceptionTypes():
        if offline:
            return archivedInformation['exceptionType']
        else:
            return Loading.parseExceptionType()
    # endregion


# endregion
# region Reading
class Reading(object):
    def prepareOffline():
        print('Writing offline information internally')
        data = {
            'properties': Loading.getProperties(),
            'rawArgs': Loading.getRawArgs(),
            'engineType': Loading.getEngineType(),
            'outputType': Loading.getDisplayOutputTypes(),
            'exceptionType': Loading.getDisplayExceptionTypes()
        }
        return data

    def overWriting(replaceWith=None):
        if replaceWith == None:
            replaceWith = Reading.prepareOffline()
        foil = 'cryptosouple.py'
        replace = "archivedInformation = "

        lines = []
        with open(foil) as reading:
            for line in reading.readlines():
                if line.startswith(replace):
                    line = replace + str(replaceWith) + '\n'
                lines += [line]

        with open(foil, 'w') as writing:
            for line in lines:
                writing.write(line)


# endregion
# region Utils
class Utils(object):

    def prettyTime(num):
        H, M, S = str(datetime.timedelta(seconds=num)).split(':')

        string = "S:" + str(S)
        if (int(M) > 0):
            string = "M:" + str(M) + " " + string
        if (int(H) > 0):
            string = "H:" + str(H) + " " + string

        return string

    def hash():
        Utils.build()
        print(Utils.halfRows())

        foil = Loading.getProperties()['projectName'] + '.jar'
        print("Determing the sha512 for " + foil, end=' | ')

        print(Utils.retrieveSha(foil))

    def retrieveSha(inFile):
        sha = None
        with open(inFile, 'rb') as new:
            contents = new.read()
            sha = hashlib.sha512(contents).hexdigest()
        return sha

    def stringMult(num, string='='):
        return ''.join([string for x in range(int(num))])

    def printSurveyURL():
        print(Loading.getProperties()['surveyURL'])

    def halfRows(kar='='):
        return Utils.stringMult(Utils.getWidthOfTerminal() / 2, kar)

    def splitRows(kar='='):
        return Utils.stringMult(Utils.getWidthOfTerminal(), kar)

    def getWidthOfTerminal():
        try:
            value = int(os.popen('stty size', 'r').read().split()[1])
            value = value * .75
        except:
            value = 50
        return value

    def lremove(string, find):
        return Utils.lreplace(string, find, '')

    def lreplace(string, find, replace):
        reverse = string[::-1]
        replace = reverse.replace(find[::-1],replace[::-1],1)
        return replace[::-1]

    def printVersion():
        props = Loading.getProperties()
        print(props['projectName'] + ': ' + props['versionNumber'])
        print('Gradle Version: ' + props['gradle.version'])
        print('Java Build Version: ' + props['buildVersion'])

    def percent(x, y):
        return Utils.outOf(x, x + y)

    def outOf(x, y):
        if (x == 0):
            return 0
        return round((x / y) * 100, 2)

    def clean():
        print("Cleaning the project")
        print(Utils.halfRows())

        cmd = str(os.path.join(os.path.abspath(os.curdir), 'gradlew')) + ' clean'
        print('Cleaning the project using | ./gradlew clean ', end='| ', flush=True)
        try:
            proc = subprocess.Popen(
                shlex.split(cmd),
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE
            )
            stdout, stderr = proc.communicate()
        except Exception as e:
            print('Unknown Error ' + str(e))
            sys.exit(0)

        stdout, stderr = stdout.decode('utf-8'), stderr.decode('utf-8')

        print('Removing the Generated files')
        for foil in glob.glob("*Generate*"):
            print('Removing the file' + str(foil))
            os.remove(foil)
        for foil in glob.glob("_CryptoGuard-V*_*_*"):
            print('Removing the file' + str(foil))
            os.remove(foil)

        print('Removing the build file')
        try:
            shutil.rmtree('build')
        except:
            pass

        if 'BUILD SUCCESSFUL' not in stdout:
            print('The build broke, exiting now')
            sys.exit(0)
        print('Successful')

    def custom(argz):
        print("Custom command " + str(argz))
        print(Utils.halfRows())
        print('Building the project using | ./gradlew ' + str(argz) + ' ' , end='| ', flush=True)
        start = time.time()
        cmd = str(os.path.join(os.path.abspath(os.curdir), 'gradlew')) + ' ' + str(argz)
        try:
            proc = subprocess.Popen(
                shlex.split(cmd),
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE
            )
            stdout, stderr = proc.communicate()
        except Exception as e:
            print('Unknown Error ' + str(e))
            sys.exit(0)

        stdout, stderr = stdout.decode('utf-8'), stderr.decode('utf-8')

        if 'BUILD SUCCESSFUL' not in stdout:
            print('The build broke, exiting now')
            sys.exit(0)

        print('Successful | ' + str(int(time.time() - start)) + ' (s)')

    def build():
        print("Building the project")
        print(Utils.halfRows())
        argz = ' clean build -x test '
        print('Building the project using | ./gradlew' + str(argz), end='| ', flush=True)
        start = time.time()
        cmd = str(os.path.join(os.path.abspath(os.curdir), 'gradlew')) + str(argz)
        try:
            proc = subprocess.Popen(
                shlex.split(cmd),
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE
            )
            stdout, stderr = proc.communicate()
        except Exception as e:
            print('Unknown Error ' + str(e))
            sys.exit(0)

        stdout, stderr = stdout.decode('utf-8'), stderr.decode('utf-8')

        if 'BUILD SUCCESSFUL' not in stdout:
            print('The build broke, exiting now')
            sys.exit(0)

        print('Successful | ' + str(int(time.time() - start)) + ' (s)')

        print('Copying the jar file to the current directory ', end='| ', flush=True)

        projectName = Loading.getProperties()['projectName']
        projectVersion = Loading.getProperties()['versionNumber']
        os.system('cp build/libs/' + projectName + '-' + projectVersion + '.jar ./' + projectName + '.jar')
        os.system('cp build/libs/' + projectName + '-' + projectVersion + '.jar ./Notebook/' + projectName + '.jar')

        if os.path.exists(projectName + '.jar'):
            print("Successful")
        else:
            print("Failure")

    def refresh():
        Utils.clean()
        Utils.custom('spotlessApply')
        Utils.build()

    # Setting the arguments to be handled by the parser
    def arguments(parser, curChoices):

        parser.add_argument("switch", choices=curChoices, nargs='?', default='help',
                            help='Use the q flag to show detailed help')
        parser.add_argument("extraArg", nargs='?', default=None,
                            help='Extra argument to be used to tune commands')
        parser.add_argument("extraArgFile", nargs='?', default=None,
                            help='Extra file to be used to tune commands')
        parser.add_argument("-v", action='store_true', help='Print the project version')

        return parser

    def help(exit=True):
        Utils.printVersion()
        print(Utils.halfRows())
        Utils.routingInfo('./cryptosouple.py')
        if exit:
            sys.exit(0)

    def routing(switch):
        offline = not os.path.exists(gitPath)
        func = routers.get(switch, Utils.routingInfo)
        if offline and not func['offline']:
            print('Cannot run ' + switch + ' in Offline Mode')
            sys.exit(0)
        return func

    def routingInfo(useage=''):
        global offline
        for val in routers:
            if (not offline or (offline and routers[val]['offline'])):
                print('\t' + str(useage) + ' ' + str(val) + ': ' + str(routers[val]['def']))
        return

    def start():
        print("Running CryptoSoule")
        global offline
        if offline:
            print('This is running in an offline mode')
        curChoices = list(routers.keys())
        args = Utils.arguments(argparse.ArgumentParser(), curChoices).parse_args()
        if (args.v):
            Utils.printVersion()
            sys.exit()

        print(Utils.splitRows() + '\n')

        global generalArg
        global generalFile
        if (args.extraArg):
            _temp = args.extraArg

            if ("-c" in _temp):
                global generalCmd
                generalCmd = _temp.replace('-c ','')
            elif ('-s' in _temp):
                global streamTests
                streamTests = True
                _temp = _temp.replace('-s', '')

            generalArg = _temp

        if (args.extraArgFile):
            generalFile = args.extraArgFile

        Utils.routing(args.switch)["func"]()


# endregion
# region envVars
class envVars(object):
    def checkVariables(outFile='_cryptoguard.source'):
        Utils.splitRows()
        fail = True

        global android
        global java7
        global java

        exports = []

        if (not android or not os.path.exists(android)):
            print("ANDROID_HOME is not set")
            yaynay = input('Do you want to set it (y/n-default)? ') == 'y'
            if yaynay:
                newHome = input('What is the new path to JAVA_HOME? ').strip()
                exports += ['export ANDROID_HOME=' + newHome]
        else:
            print("ANDROID_HOME is set to " + android)
            yaynay = input('Do you want to reset it (y/n-default)? ') == 'y'
            if yaynay:
                newHome = input('What is the new path to ANDROID_HOME? ').strip()
                exports += ['export ANDROID_HOME=' + newHome]

        if (not java7 or not os.path.exists(java7)):
            print("JAVA7_HOME is not set")
            yaynay = input('Do you want to set it (y/n-default)? ') == 'y'
            if yaynay:
                newHome = input('What is the new path to JAVA7_HOME? ').strip()
                exports += ['export JAVA7_HOME=' + newHome]
        else:
            print("JAVA7_HOME is set to " + java7)
            yaynay = input('Do you want to reset it (y/n-default)? ') == 'y'
            if yaynay:
                newHome = input('What is the new path to JAVA7_HOME? ').strip()
                exports += ['export JAVA7_HOME=' + newHome]

        if (not java or not os.path.exists(java)):
            print("JAVA_HOME is not set")
            yaynay = input('Do you want to set it (y/n-default)? ') == 'y'
            if yaynay:
                newHome = input('What is the new path to JAVA_HOME? ').strip()
                exports += ['export JAVA_HOME=' + newHome]
        else:
            print("JAVA_HOME is set to " + java)
            yaynay = input('Do you want to reset it (y/n-default)? ') == 'y'
            if yaynay:
                newHome = input('What is the new path to JAVA_HOME? ').strip()
                exports += ['export JAVA_HOME=' + newHome]

        if len(exports) > 0:
            with open(outFile, 'w') as out:
                for arg in exports:
                    out.write(arg + '\n')
            os.chmod(outFile, 0o777)
            print("Please run the following command: source " + outFile)

        return fail


# endregion
# region ArgUtils
class argsUtils(object):
    def fileStringLooper(strings):
        global verify
        output = []
        for string in strings.split(':'):
            string = string.strip()
            if verify:
                if os.path.isfile(string):
                    output += [string]
                else:
                    print("File doesn't exist: " + str(string))
                    raise TypeError('Empty')
            else:
                output += [string]

        return output

    def customFileDir(string):
        global verify
        output = []
        if isinstance(string, list):
            if len(string) == 0 and not verify:
                raise TypeError('Empty')

            for rawString in string:
                _temp = argsUtils.fileStringLooper(rawString)
                if _temp is None:
                    _temp = [argsUtils.customDir(rawString)]

                if _temp is not None:
                    output = output + _temp

        if isinstance(string, str):
            output = argsUtils.fileStringLooper(string)

        if len(output) == 0 and not verify:
            raise TypeError('Empty')

        return output

    def customDir(string):
        global verify
        output = []
        for string in strings.split(':'):
            string = string.strip()
            if verify:
                if os.path.isdir(string):
                    output += [string]
                else:
                    raise NotADirectoryError(string)
            else:
                output += [string]

        return output

    def createCryptoArgs():
        cryptoParse = argparse.ArgumentParser()

        cryptoParse.add_argument("-in", nargs=1, type=str, choices=['jar','apk','source','java','class'], dest="format", help="The format of input you want to scan.")
        cryptoParse.add_argument("-s", nargs='+', type=argsUtils.customFileDir, dest="source", help="The source to be scanned use the absolute path or send all of the source files via the file input.in; ex. find -type f *.java >> input.in.")
        cryptoParse.add_argument("-d", nargs='*', type=argsUtils.customFileDir, dest="dependency", help="The dependency to be scanned use the relative path.")
        cryptoParse.add_argument("-o", nargs='?', type=argparse.FileType('w'), dest="out", help="The file to be created with the output default will be the project name.")
        cryptoParse.add_argument("-new", nargs='?', dest="new", help="The file to be created with the output if existing will be overwritten.")
        cryptoParse.add_argument("-t", nargs='?', dest="timemeasure", help="Output the time of the internal processes.")
        cryptoParse.add_argument("-m", nargs='?', type=str,  choices=['L','SX','D'], default='D', dest="formatout", help="The output format you want to produce")
        cryptoParse.add_argument("-n", nargs='?', dest="pretty", help="Output the analysis information in a 'pretty' format.")
        cryptoParse.add_argument("-X", nargs='?', dest="noexit", help="Upon completion of scanning, don't kill the JVM")
        cryptoParse.add_argument("-v", nargs='?', dest="version", help="Output the version number.")
        cryptoParse.add_argument("-VX", nargs='?', dest="nologs", help="Display logs only from the fatal logs")
        cryptoParse.add_argument("-V", nargs='?', dest="verbose", help="Display logs from debug levels")
        cryptoParse.add_argument("-VV", nargs='?', dest="veryverbose", help="Display logs from trace levels")
        cryptoParse.add_argument("-ts", nargs='?', dest="timestamp", help="Add a timestamp to the file output.")
        cryptoParse.add_argument("-depth", nargs='?', type=int, dest="depth", help="The depth of slicing to go into")
        cryptoParse.add_argument("-java", nargs='?', type=argsUtils.customDir, dest="java", help="Directory of Java to be used JDK 7 for JavaFiles/Project and JDK 8 for ClassFiles/Jar")
        cryptoParse.add_argument("-android", nargs='?', type=argsUtils.customDir, dest="android", help="Specify of Android SDK")
        cryptoParse.add_argument("-H", nargs='?', dest="heuristics", help="The flag determining whether or not to display heuristics.")
        cryptoParse.add_argument("-st", nargs='?', dest="stream", help="Stream the analysis to the output file.")
        cryptoParse.add_argument("-main",  nargs='?', dest="main", help="Choose the main class if there are multiple main classes in the files given.")
        cryptoParse.add_argument("-Sconfig", nargs='?', type=argparse.FileType('r'), dest="Sconfig", help="Choose the Scarf property configuration file.")
        cryptoParse.add_argument("-Sassessfw", nargs='?', type=str, dest="Sassessfw", help="The assessment framework")
        cryptoParse.add_argument("-Sassessfwversion", nargs='?', type=str, dest="Sassessfwversion", help="The assessment framework version")
        cryptoParse.add_argument("-Sassessmentstartts", nargs='?', type=str, dest="Sassessmentstartts", help="The assessment start timestamp")
        cryptoParse.add_argument("-Sbuildfw", nargs='?', type=str, dest="Sbuildfw", help="The build framework")
        cryptoParse.add_argument("-Sbuildfwversion", nargs='?', type=str, dest="Sbuildfwversion", help="The build framework version")
        cryptoParse.add_argument("-Sbuildrootdir", nargs='?', type=str, dest="Sbuildrootdir", help="The build root directory")
        cryptoParse.add_argument("-Spackagename", nargs='?', type=str, dest="Spackagename", help="The package name")
        cryptoParse.add_argument("-Spackagerootdir", nargs='?', type=str, dest="Spackagerootdir", help="The package root directory")
        cryptoParse.add_argument("-Spackageversion", nargs='?', type=str, dest="Spackageversion", help="The package version")
        cryptoParse.add_argument("-Sparserfw", nargs='?', type=str, dest="Sparserfw", help="The parser framework")
        cryptoParse.add_argument("-Sparserfwversion", nargs='?', type=str, dest="Sparserfwversion", help="The parser framework version")
        cryptoParse.add_argument("-Suuid", nargs='?', type=str, dest="Suuid", help="The uuid of the current pipeline progress")

        return cryptoParse

    def generateTest():
        global generalCmd
        global generalFile

        if generalCmd is None:
            print('No commands passed in')
            sys.exit(0)

        if 'cryptoguard.jar' in generalCmd:
            generalCmd = generalCmd.split('cryptoguard.jar')[1]

        options = argsUtils.createCryptoArgs().parse_args(shlex.split(generalCmd))

        lineEnding = ".json"
        checker = """Report report = Report.deserialize(new File(outputFile));
                                        assertFalse(report.getIssues().isEmpty());
                                        assertTrue(report.getIssues().stream().anyMatch(bugInstance -> {
                                            try {
                                                return Utils.containsAny(bugInstance.getFullPath(), Utils.retrieveFullyQualifiedNameFileSep(tempSource));
                                            } catch (ExceptionHandler e) {
                                                assertNull(e);
                                                e.printStackTrace();
                                            }
                                            return false;
                                        }));
                                        """
        listing = "Default"

        if options.formatout == "L":
            lineEnding = ".txt"
            checker = """List<String> results = Files.readAllLines(Paths.get(outputFile), StandardCharsets.UTF_8);
                                        assertTrue(results.size() >= 10);

                                        List<String> filesFound = Utils.retrieveFilesPredicate(tempSource, s -> s.endsWith(".java"), file -> {
                                            try {
                                                return Utils.retrieveFullyQualifiedName(file.getAbsolutePath()) + ".java";
                                            } catch (ExceptionHandler exceptionHandler) {
                                                exceptionHandler.printStackTrace();
                                                return null;
                                            }
                                        });

                                        assertTrue(results.stream().anyMatch(str -> filesFound.stream().anyMatch(str::contains)));
                         """
            listing = "Legacy"
        elif options.formatout == "SX":
            lineEnding = ".xml"
            checker = """AnalyzerReport report = AnalyzerReport.deserialize(new File(outputFile));
                                        assertFalse(report.getBugInstance().isEmpty());
                                        assertTrue(report.getBugInstance().stream().anyMatch(bugInstance -> {
                                            try {
                                                return Utils.containsAny(bugInstance.getClassName(), Utils.retrieveFullyQualifiedName(tempSource));
                                            } catch (ExceptionHandler exceptionHandler) {
                                                exceptionHandler.printStackTrace();
                                                return false;
                                            }
                                        }));
                                        """
            listing = "ScarfXML"

        engineType = {
            'jar': 'JAR',
            'apk': 'APK',
            'source': 'DIR',
            'java': 'JAVAFILES',
            'class': 'CLASSFILES'
        }.get(options.format[0])

        if options.out is not None:
            fileOut = options.out.name
        else:
            fileOut = "_GeneratedTestFile" + str(lineEnding)

        sourcez = ""
        for string in options.source:
            sourcez = sourcez + " " + f"add(\"{string[0]}\");\n                            "

        depz = ""
        if options.dependency is not None:
            for _temp in options.dependency:
                for _itr in _temp:
                    for string in _itr.split(":"):
                        depz = depz + " " + f"add(\"{string}\");\n                            "

        dependency = ""
        if depz.strip():
            dependency = f"""String tempDependency = Utils.join(" ", new ArrayList<String>(){{{{
                                {depz}
                             }}}});
                             String dependency = Utils.join(" ", tempDependency);
                          """

        argz = f"""
                                String args = 
                                    makeArg(argsIdentifier.FORMAT, EngineType.{engineType}) + 
                                    makeArg(argsIdentifier.OUT, fileOut) +
                                    makeArg(argsIdentifier.FORMATOUT, Listing.{listing}) + """

        clean = argsUtils.createCryptoArgs()

        # Adding the out
        if depz.strip():
            argz = argz + f"""
                                    makeArg(argsIdentifier.DEPENDENCY, dependency) + """

        # Adding the flags
        for arg in [x.dest for x in clean.__dict__['_actions'] if
                    x.type is None and x.dest.strip() != 'help' and options.__dict__[x.dest] is not None]:
            argz = argz + f"""
                                    makeArg(argsIdentifier.{arg.upper()}) + """

        # Adding the strings
        for arg in [x.dest for x in clean.__dict__['_actions'] if
                    x.type is str and x.nargs is '?' and x.dest.strip().upper() != 'FORMATOUT' and options.__dict__[
                        x.dest] is not None]:
            argz = argz + f"""
                                    makeArg(argsIdentifier.{arg.upper()}, "{options.__dict__[arg]}") + """

        # Adding the new
        if options.new is not None:
            argz = argz + f"""
                                    makeArg(argsIdentifier.NEW, \"{options.new.name}\") + """

        # Adding the depth
        if options.depth is not None:
            argz = argz + f"""
                                    makeArg(argsIdentifier.DEPTH, {options.depth}) + """

        # Adding the java
        if options.java is not None:
            argz = argz + f"""
                                    makeArg(argsIdentifier.JAVA, \"{options.java[0]}\") + """

        # Adding the android
        if options.android is not None:
            argz = argz + f"""
                                    makeArg(argsIdentifier.ANDROID, \"{options.android[0]}\") + """

        # Adding the Sconfig
        if options.Sconfig is not None:
            argz = argz + f"""
                                    makeArg(argsIdentifier.SCONFIG, \"{options.Sconfig.name}\") + """

        argz = argz + """
                                    makeArg(argsIdentifier.SOURCE, source) +
                                    makeArg(argsIdentifier.PRETTY) +
                                    makeArg(argsIdentifier.NOEXIT);
                      """

        output = f"""
                    /**
                     * Generated Test
                     */
                    @Test
                    public void generatedTest() {{
                        String fileOut = "{fileOut}";
                        new File(fileOut).delete();

                        ArrayList<String> tempSource = new ArrayList<String>(){{{{
                            {sourcez}
                        }}}};
                        String source = Utils.join(" ", tempSource);

                        {dependency}

                        if (isLinux) {{

                                {argz}

                                try {{
                                    String outputFile = captureNewFileOutViaStdOut(args.split(" "));

                                    {checker}

                                }} catch (Exception e) {{
                                    e.printStackTrace();
                                    assertNull(e);
                                }}
                        }}
                    }}
                    """

        if (generalFile is not None):
            if os.path.exists(generalFile):
                os.remove(generalFile)

            with open(generalFile, 'w') as file:
                file.write(output)
        else:
            print(output)

        print('Completed')

    def basicBuildCommand():
        print("Building a basic command")
        print('Please Note this does not verify whether the directory/files exist')
        print("Common abreviations:")
        print("\tn = no")
        print("\ty = yes")
        print(Utils.halfRows())

        projectName = Loading.getProperties()['projectName']
        cmd = 'java -jar ' + projectName + '.jar '

        lookup = Loading.parseEngineType()
        for value in lookup.values():
            print(value['name'] + ' flag: ' + value['flag'])
        print()

        typeOfProject = input("What type of project by flag (jar/apk/source/java/class)? ")
        if typeOfProject not in [x['flag'] for x in lookup.values()]:
            print("Please enter a valid type of project");
            sys.exit()
        cmd += '-in ' + typeOfProject + ' '

        for key, value in lookup.items():
            if value['flag'] == typeOfProject:
                typeOfProject = value

        print()

        print(Utils.halfRows())

        source = input(
            "What file/project directory would you like to scan (java/class files please enter class path or single file)? ")
        cmd += '-s ' + source + ' '
        print()

        if (typeOfProject['flag'] != 'source' and not source.endswith(
                typeOfProject['extension']) and not source.endswith(
            ".in")):
            print("Please enter a valid file for Scanning");
            sys.exit()

        print(Utils.halfRows())

        global android
        global java7
        global java

        javaHome = sdkHome = None
        if (typeOfProject['flag'] == 'apk'):
            print("Current ANDROID_HOME is set to " + android)
            sdkHome = input("Would you like to specify the Android Home (n/sdk directory)? ")
            print()
            if sdkHome and sdkHome != 'n':
                cmd += '-android ' + sdkHome + ' '
        if (typeOfProject['flag'] == 'apk' or typeOfProject['flag'] == 'jar' or typeOfProject['flag'] == 'class'):
            print("Current JAVA_HOME is set to " + java)
            javaHome = input("Would you like to specify the Java 8 Home (n/jdk directory)? ")
        if (typeOfProject['flag'] == 'dir' or typeOfProject['flag'] == 'java'):
            print("Current JAVA7_HOME is set to " + java7)
            javaHome = input("Would you like to specify the Java 7 Home (n/jdk directory)? ")
        print()

        if javaHome and javaHome != 'n':
            cmd += '-java ' + javaHome + ' '

        dependency = input("Would you like to add a dependency folder (n/directory)? ")
        if (dependency != 'n'):
            cmd += '-in ' + dependency + ' '
        print()

        main = input("Would you like to specify the main class (n/file)? ")
        if (main != 'n'):
            cmd += '-main ' + main + ' '
        print()

        print(Utils.halfRows())
        print('Output formats')
        lookup = Loading.parseOutputType()
        for value in lookup.values():
            print(value['type'] + " flag: " + value['flag'] + ' extension: ' + value['outputExtension'])
        print()

        outType = input("Would you like to specify the output format by flag (n/SX/L/D)? ")
        if (outType != 'n'):
            if outType not in [x['flag'] for x in lookup.values()]:
                print('Please enter a valid output format');
                sys.exit(0)
            cmd += '-m ' + outType + ' '
            for key, value in lookup.items():
                if value['flag'] == outType:
                    lookup = value
        print()

        foil = input("Would you like to specify the output file (n/file)? ")
        if (foil != 'n'):
            if not foil.endswith(lookup['outputExtension']):
                print('Please enter a valid file for the output type.');
                sys.exit(0)
            cmd += '-o ' + foil + ' '
        print()

        if (outType == 'n' and foil != 'n' or outType != 'n' and foil == 'n'):
            print('Please enter both the output format and the output file.');
            sys.exit(0)

        if foil != 'n':
            overwrite = input("Would you like to overwrite the output file (y/n)? ") == 'y'
            if (overwrite):
                cmd += '-new '
            print()

        print(Utils.halfRows())
        logging = input("Would you like to set the logging high/medium/low/off/no (h/m/l/o/n)? ")
        if (logging == 'h'):
            cmd += '-vv '
        elif (logging == 'm'):
            cmd += '-v '
        elif (logging == 'o'):
            cmd += '-vx '
        print()

        print(Utils.halfRows())

        result = input("Would you like to stream the results (y/n)? ") == 'y'
        if (result):
            cmd += '-st '
        print()

        result = input("Would you like to the time measurement to the output (y/n)? ") == 'y'
        if (result):
            cmd += '-t '
        print()

        result = input("Would you like to format the results (y/n)? ") == 'y'
        if (result):
            cmd += '-n '
        print()

        result = input("Would you like to add heuristics to the results (y/n)? ") == 'y'
        if (result):
            cmd += '-H '
        print()

        result = input("Would you like to specify the depth of heuristics to the results (y/n)? ") == 'y'
        if (result):
            result = input("What number? ")
            try:
                result = int(result)
            except ValueError:
                print("Please enter a valid depth number.");
                sys.exit(0)
            cmd += '-depth ' + str(result) + ' '
        print()

        print(Utils.halfRows())
        print('The build up command is:' + cmd)

        print(Utils.halfRows())
        run = input('Would you like to run the command (y/n)?') == 'y'
        if (run):
            os.system(cmd)

    def displayProjectTypes():
        print("Can scan the following project types:")
        print(Utils.halfRows())
        for key, value in Loading.parseEngineType().items():
            print('\t' + value['name'] + " accepts a " + value['extension'])

    def displayOutputTypes():
        print("Can write the results as the following output types:")
        print(Utils.halfRows())
        for key, value in Loading.parseOutputType().items():
            print('\t' + value['type'] + " accepts a " + value['outputExtension'] + ' file output type.')

    def displayExceptionTypes():
        print("Uses the following error codes:")
        print(Utils.halfRows())
        for key, value in Loading.parseExceptionType().items():
            print('\t' + value['id'] + " is a " + value['messageType'] + ' Exception.')

    def writeUsage():
        argsUtils.helpfulArgs(writeOut=True)

    def helpfulArgs(filter=None, writeOut=False, writeOutFile='USAGE.md'):
        projectName = Loading.getProperties()['projectName']
        projectVersion = Loading.getProperties()['versionNumber']

        data = Loading.getExampleArgs()

        grouping = {
            'General': {},
            'APK': {},
            'JAR': {},
            'Java': {},
            'Project': {},
            'Class': {}
        }
        for key, value in data.items():
            if filter is None or key.contains(filter):
                grouping[value['type']][key] = value

                if not writeOut:
                    print('Example ' + key)

                    if ('Java File' in key):
                        print('CURRENTLY UNSTABLE')

                    print(Utils.halfRows())
                    print("java -jar " + projectName + ".jar " + value['arg'])
                    print(value['explanation'])
                    print()

        if writeOut:
            print("Writing the Usage file")
            print(Utils.halfRows())
            with open(writeOutFile, 'w') as out:
                out.write("# " + projectName + ":" + projectVersion + " General Usage\n")

                out.write("## Raw Arguments\n")
                out.write(
                    "**Please Note** the general arguments can be used in any order and in various combinations (except for the version argument)\n")
                lookup = Loading.parseArgs()
                longestOneCol = max(len(x['id']) for x in lookup.values())
                longestTwoCol = max(len(x['defaultArg']) for x in lookup.values())
                longestThreeCol = max(len(x['desc']) for x in lookup.values())
                out.write('| Id | Default | Description |\n')
                out.write('|-' + Utils.stringMult(longestOneCol, '-') + '|' + Utils.stringMult(longestTwoCol,
                                                                                               '-') + '|' + Utils.stringMult(
                    longestThreeCol, '-') + '|\n')
                for key, value in lookup.items():
                    if key.strip() != 'NOEXIT':
                        arg = value['defaultArg']
                        if arg == 'null':
                            arg = 'Flag'
                        out.write(
                            '| -' + value['id'] + ' | ' + arg + ' | ' + value['desc'].replace(")", "").replace(";",
                                                                                                               "") + ' |\n')

                out.write('---\n')

                out.write("## Exceptions\n")
                lookup = argsUtils.parseExceptionType()
                longestLength = max(len(x['messageType']) for x in lookup.values())
                out.write('| error code | error type |\n')
                out.write('|------------|' + Utils.stringMult(longestLength, '-') + '|\n')
                for key, value in lookup.items():
                    out.write('| ' + value['id'] + ' | ' + value['messageType'] + ' |\n')

                out.write('---\n')

                out.write('## Format Types (used with the -in argument)\n')
                lookup = argsUtils.parseEngineType()
                longestOneCol = max(len(x['name']) for x in lookup.values())
                longestTwoCol = max(len(x['flag']) for x in lookup.values())
                longestThreeCol = max(len(x['helpInfo']) for x in lookup.values())
                out.write('| Name | Flag | Description |\n')
                out.write('|' + Utils.stringMult(longestOneCol, '-') + '|' + Utils.stringMult(longestTwoCol,
                                                                                              '-') + '|' + Utils.stringMult(
                    longestThreeCol, '-') + '|\n')
                for key, value in lookup.items():
                    out.write('| ' + value['name'] + ' | ' + value['flag'] + ' | ' + value['helpInfo'].replace(")",
                                                                                                               "").replace(
                        ";", "") + ' |\n')

                out.write('---\n')

                out.write('## Output Types (used with the -m argument, optional)\n')
                lookup = argsUtils.parseOutputType()
                longestOneCol = max(len(x['type']) for x in lookup.values())
                longestTwoCol = max(len(x['flag']) for x in lookup.values())
                out.write('| Name | Flag |\n')
                out.write(
                    '|' + Utils.stringMult(longestOneCol, '-') + '|' + Utils.stringMult(longestTwoCol, '-') + '|\n')
                for key, value in lookup.items():
                    out.write('| ' + value['type'] + ' | ' + value['flag'] + ' |\n')

                out.write('---\n')

                out.write(
                    "\nListed Below are various examples of cmd line arguments with their explanations grouped by their project type.")
                out.write("\n\n")
                for key, value in grouping.items():
                    out.write("## " + key + "\n")
                    out.write("---\n")
                    for key, value in value.items():
                        out.write("### " + key + "\n")
                        out.write("> java -jar " + projectName + ".jar " + value['arg'] + "\n\n")
                        out.write(value['explanation'] + "\n\n")
                    out.write("\n\n")
            print('Written to ' + writeOutFile)

    def readRawArgs():
        projectName = Loading.getProperties()['projectName']
        rawArgs = Loading.parseArgs()

        for key, value in rawArgs.items():
            if key.strip() != 'NOEXIT':
                required = ''
                if (value['Required']):
                    required = '* '

                print("Name: " + key.strip())
                print(required + "Arg: -" + value['id'])
                print(value['desc'])

                if value['defaultArg'] != 'null':
                    print('Default: ' + value['defaultArg'])
                    print('Usage: java -jar ' + projectName + ' -' + value['id'] + ' ' + value['defaultArg'])
                else:
                    print('Usage: java -jar ' + projectName + ' -' + value['id'])

                if key.strip() == 'FORMAT':
                    print('\nDifferent Options available for project types:')
                    for key, value in Loading.parseEngineType().items():
                        print('\t' + value['flag'] + ": " + value['name'] + " " + value['helpInfo'])
                elif key.strip() == 'FORMATOUT':
                    print('\nDifferent Options available for output format:')
                    for key, value in Loading.parseOutputType().items():
                        print('\t' + value['flag'] + ": " + value['type'])

                print()

        print('Exceptions')
        for key, value in Loading.parseExceptionType().items():
            print(value['id'] + ': ' + value['messageType'])


# endregion
# region TestUtils
class TestUtils(object):
    def testToNotebook(jtest):
        cell = None

        jtest = jtest.replace('|','\n').replace('@Test','').strip()
        cell = nb.v4.new_code_cell(source=jtest)

        return cell

    def genNotebook(testUtils='src/test/java/test/TestUtilities.java'):

        testContents, read = '', False
        with open(testUtils,'r') as foil:
            for lin in foil.readlines():
                if (read):
                    testContents = str(testContents) + "\n" + str(lin)
                elif 'public class TestUtilities {' in lin:
                    read = True
        
        testContents = Utils.lremove(testContents, '}')

        fileContents, fileOut, notebook = generalArg, generalFile, None
        '''
        if not os.path.exists(fileIn) or (os.path.exists(fileOut) and not fileOut.endswith('.ipynb')):
            print("FileIn does not exist");sys.exit(0)
        else:
            fileIn = os.path.abspath(fileIn)
            fileOut = fileIn.replace('java','')
        '''
        testName = None
        if fileOut is None or not os.path.exists(fileOut):
            testName = re.search(r"public void (\w*)()",fileContents)[0].replace('public void ','')
            fileOut = testName + '.ipynb'
            notebook = nb.v4.new_notebook()
            notebook['cells'] += [nb.v4.new_markdown_cell(source="# Generated Test")]
            notebook['cells'] += [nb.v4.new_markdown_cell(source="## Test " + testName)]
            imports = '''
            //Custom Imports
            List<String> addedJars = %jars *.jar
            %maven junit:junit:4.12

            //CryptoGuard imports
            import frontEnd.Interface.EntryPoint;
            import static org.junit.Assert.assertFalse;
            import static org.junit.Assert.assertTrue;
            import static org.junit.Assert.assertEquals;
            import static org.junit.Assert.assertNull;
            import static org.junit.Assert.assertNotNull;
            import static util.Utils.makeArg;

            import frontEnd.Interface.outputRouting.ExceptionHandler;
            import frontEnd.Interface.ArgumentsCheck;
            import frontEnd.MessagingSystem.routing.Listing;
            import frontEnd.MessagingSystem.routing.EnvironmentInformation;
            import frontEnd.MessagingSystem.routing.structure.Default.Report;
            import frontEnd.MessagingSystem.routing.structure.Scarf.AnalyzerReport;
            import frontEnd.MessagingSystem.routing.structure.Scarf.BugInstance;
            import frontEnd.argsIdentifier;
            import java.io.File;
            import java.nio.charset.StandardCharsets;
            import java.nio.file.Files;
            import java.nio.file.Paths;
            import java.util.ArrayList;
            import java.util.Arrays;
            import java.util.List;
            import org.junit.After;
            import org.junit.Before;
            import org.junit.Test;
            import org.junit.runner.RunWith;
            import rule.engine.EngineType;
            import soot.G;
            import util.Utils;
            '''

            utils = '''
            //Utililties

            String java_home = "/home/runner/.sdkman/candidates/java/8.0.252-zulu";
            Boolean isLinux = true;

            public static String captureNewFileOutViaStdOut(String[] args, Boolean exceptionHandler) {
            //region Redirecting the std out to capture the file out
            //The new std out
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);

            //Old out
            PrintStream old = System.out;

            //Redirecting the std out to the capture
            System.setOut(ps);

            //region Critical Section
            EntryPoint.main(args);
            //endregion

            //Resetting the std out
            System.out.flush();
            System.setOut(old);
            //endregion

            //The output string
            String outputString = StringUtils.trimToNull(baos.toString());
            if (!exceptionHandler) assertTrue(StringUtils.isNotBlank(outputString));

            String[] lines = baos.toString().split(Utils.lineSep);
            outputString = StringUtils.trimToNull(lines[lines.length - 1]);
            if (!exceptionHandler) assertTrue(StringUtils.isNotBlank(outputString));

            return outputString;
            }

            public static String captureNewFileOutViaStdOut(String[] args) throws Exception {

                return captureNewFileOutViaStdOut(args, false);
            }
            public static String[] arr(ArrayList<String> in) {
                return in.toArray(new String[in.size()]);
            }

            public static ArrayList<String> arr(String[] in) {
                return new ArrayList<>(Arrays.asList(in));
            }
            '''
            notebook['cells'] += [nb.v4.new_code_cell(source=imports)]
            notebook['cells'] += [nb.v4.new_code_cell(source=testContents)]
            notebook['cells'] += [nb.v4.new_code_cell(source=utils)]
            notebook['cells'] += [TestUtils.testToNotebook(fileContents)]
        else:
            notebook = nb.read(fileOut, 4)
            notebook['cells'] += [TestUtils.testToNotebook(fileContents)]

        notebook['cells'] += [nb.v4.new_code_cell(source=str(testName)+"()")]
        nb.write(notebook,fileOut)

        print("Generated File " + str(fileOut))


    def pullTests(dir=os.path.join(os.path.abspath(os.curdir), "src", "test")):
        dyct = {}

        for root, dirnames, filenames in os.walk(dir):
            for filename in filenames:
                if (filename.endswith('.java')):
                    fulFoil, className = os.path.join(root, filename), filename.replace('.java', '')
                    with open(fulFoil, 'r') as foil:
                        line = foil.readline()
                        while line:
                            if line.__contains__('@Test'):
                                if (className not in dyct):
                                    dyct[className] = []

                                liveTest, method = not line.__contains__('//@Test'), foil.readline()
                                start, end = method.find("public void "), method.find("(")

                                if (start != -1 and end != -1):
                                    testName = method[(start + len("public void ")):end]
                                    dyct[className].append({'testName': testName, 'live': liveTest})

                            line = foil.readline()
        return dyct

    def test(dyct=pullTests(), passedtests=None, stream=False):
        global streamTests
        streamTests = stream

        tests = []
        for testType in dyct:
            for test in dyct[testType]:
                tests += [str(testType) + '.' + str(test['testName'])]

        tests = None

        if passedtests is not None:
            tests = passedtests

        global generalArg
        if tests is None:
            if (generalArg is None):
                tests = input("Please enter the Class.testName to be run: ")
            else:
                tests = generalArg

        for test in tests.split(','):
            if test not in tests:
                print('Test: ' + str(test) + ' is not currently available.')
                # print('Please use one of the following tests: ')
                # for test in tests:
                #    print(test)
                sys.exit(-1)

            print("Running the test: " + str(test))
            print(Utils.halfRows())
            if (TestUtils.runTest(test)):
                print("Passed")
            else:
                print("Failed")

    def runTest(test):
        cmd = str(os.path.join(os.path.abspath(os.curdir), 'gradlew')) + ' test --tests ' + str(
            test) + ' --info --rerun-tasks'
        try:
            proc = subprocess.Popen(
                shlex.split(cmd),
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                universal_newlines=True
            )

            global streamTests
            if streamTests:
                for line in proc.stdout:
                    sys.stdout.write(line)
            else:
                stdout, stderr = proc.communicate()

        except Exception as e:
            print('Unknown Error ' + str(e))
            sys.exit(0)

        if not proc.wait():
            return True
        else:
            return False

    def activeSkipTests(dyct=pullTests()):
        skippedTests = liveTests = 0
        for key, value in dyct.items():
            skippedTests += sum([not x['live'] for x in value])
            liveTests += sum([x['live'] for x in value])
        return liveTests, skippedTests

    def getHelpTests(dyct=pullTests()):
        liveTests, skippedTests = TestUtils.activeSkipTests(dyct)
        grouping = {
            'APK': {},
            'JAR': {},
            'JAVA': {},
            'SOURCE': {},
            'CLASS': {}
        }
        for key, value in dyct.items():
            if ('_' in key):
                testType = key.strip().strip().split('_')[1]
                grouping[testType]['Skipped'] = sum([not x['live'] for x in value])
                grouping[testType]['Active'] = sum([x['live'] for x in value])
        return liveTests, skippedTests, grouping

    def getDisplayTests(dyct=pullTests(), exit=True):
        print('Displaying available tests')
        grouping = [
            'APK',
            'JAR',
            'JAVA',
            'SOURCE',
            'CLASS',
            'OTHER',
            'ALL'
        ]

        option = input(
            "Please enter what kind of test you would like to have run from " + str(grouping) + " : ").upper()
        if (option not in grouping):
            print('Option is not valid')
            sys.exit()

        for key, value in dyct.items():
            testType = 'OTHER'
            if ('_' in key):
                testType = key.strip().strip().split('_')[1]
            if option == 'ALL' or option == testType:
                print('Test Type: ' + str(testType))

                print(Utils.halfRows())
                for test in value:
                    active = 'Live'
                    if not test['live']:
                        active = 'Skip'

                    print(
                        str(active) + ' | ' + str(key) + ' | ' + str(test['testName']) + ' | ' + str(key) + '.' + test[
                            'testName'])
                if exit:
                    sys.exit(0)

    def helptests(dyct=pullTests()):
        liveTests, skippedTests, grouping = TestUtils.getHelpTests(dyct)

        totalTests = liveTests + skippedTests
        print("General Information")
        print(Utils.halfRows())
        print("Total Tests: " + str(totalTests))
        print("Live Tests: " + str(liveTests) + ' : ' + str(Utils.outOf(liveTests, totalTests)))
        print("Deactivated Tests: " + str(skippedTests) + ' : ' + str(Utils.outOf(skippedTests, totalTests)))
        print()

        for key, value in grouping.items():
            print(key)
            print(Utils.halfRows())
            print("Live Tests: " + str(value['Active']) + ' : ' + str(Utils.outOf(value['Active'], totalTests)))
            print(
                "Deactivated Tests: " + str(value['Skipped']) + ' : ' + str(Utils.outOf(value['Skipped'], totalTests)))
            print()

    # status = ['Pass','Fail','Skip']
    def addTestResult(dyct, type, status, time, name, timeReRun):

        dyct[type][status] += [{
            'name': name,
            'timeTaken': time,
            'timesReRun': timeReRun
        }]

        return dyct

    def reRunTestResult(dyct, type, fail, testName, timeReRun, newTime):
        item = [item for item in dyct[type]['Fail'] if item['name'] == testName][0]

        if (not fail):
            item['timesReRun'] = timeReRun
            item['timeTaken'] = newTime
            dyct[type]['Pass'] += item
            dyct[type]['Fail'].remove(item)

        return item

    def testType():
        print("About to run a specific set of tests")
        dyct = TestUtils.pullTests()

        liveTests, skippedTests, grouping = TestUtils.getHelpTests(dyct)
        runningTests = 0
        for key, value in grouping.items():
            print(str(key) + " : Active Tests " + str(value['Active']))
            runningTests += value['Active']

        print('OTHER : Active Tests ' + str(liveTests - runningTests))

        options = list(grouping.keys())
        options += ['OTHER']

        ktr = 0
        for key in options:
            print(str(ktr) + ': ' + str(key))
            ktr = ktr + 1

        option = int(input("Please enter what kind of test you would like to have run from (default is OTHER) :"))

        if option >= len(options):
            option = 'OTHER'
        else:
            option = options[option]

        if option != 'OTHER':
            option = 'EntryPointTest_' + option

        testTracker = []

        for key, values in dyct.items():
            if (option == 'OTHER' and not key.startswith('EntryPointTest_')) or (key == option):
                for value in values:
                    if value['live']:
                        print(str(len(testTracker)) + ': ' + str(value['testName']))
                        testTracker += [str(key) + '.' + str(value['testName'])]
        print(str(len(testTracker)) + ': All of the above (default)')

        testValue = int(input("Please enter the test number you would like to run? : "))

        print(Utils.halfRows())

        if testValue < 0 or testValue >= len(testTracker):
            print('Running all of the tests')

            ktr = 0
            for test in testTracker:
                print(str(ktr) + '/' + str(len(testTracker)) + ' | ' + str(test) + ' | ', end='', flush=True)
                startTime = time.time()

                if TestUtils.runTest(test):
                    print('Pass | ', end='', flush=True)
                else:
                    print('Fail | ', end='', flush=True)

                testTime = int(time.time() - startTime)
                print(Utils.prettyTime(testTime))

                ktr = ktr + 1

        else:
            test = testTracker[testValue]
            print(str(test) + ' | ', end='', flush=True)
            startTime = time.time()

            if TestUtils.runTest(test):
                print('Pass | ', end='', flush=True)
            else:
                print('Fail | ', end='', flush=True)

            testTime = int(time.time() - startTime)
            print(Utils.prettyTime(testTime))

    def tests(dyct=pullTests(), filter=None, stream=False):
        Utils.refresh()
        if stream:
            global streamTests
            streamTests = True
        print("Running all of the available tests.")
        dyct = OrderedDict(sorted(dyct.items()))

        results = {
            'APK': {
                'Pass': [],
                'Fail': [],
                'Skip': []
            },
            'JAR': {
                'Pass': [],
                'Fail': [],
                'Skip': []
            },
            'JAVA': {
                'Pass': [],
                'Fail': [],
                'Skip': []
            },
            'SOURCE': {
                'Pass': [],
                'Fail': [],
                'Skip': []
            },
            'CLASS': {
                'Pass': [],
                'Fail': [],
                'Skip': []
            },
            'OTHER': {
                'Pass': [],
                'Fail': [],
                'Skip': []
            }
        }

        liveTests, skippedTests = TestUtils.activeSkipTests(dyct)
        numTests = liveTests + skippedTests

        passed, failed, skipped, testNum, rerun, rerunLim = 0, 0, 0, 1, 0, 1
        start, failedTests, verbose, skippedTests = time.time(), [], False, []

        global android
        global java7
        global java

        android_set, java7_set, java_set = android is not None, java7 is not None, java is not None

        java7_set = True

        if not java_set:
            print('No JAVA_HOME environment found, please set this')
            sys.exit(1)

        if (not android_set or not java7_set):
            print(Utils.halfRows())
            if not android_set:
                print('Skipping All Android Tests, no ANDROID_HOME env found')
            else:
                print('Skipping All Project and Java file Tests, no JAVA7_HOME env found')

        print(Utils.halfRows())
        if False:
            for key, value in dyct.items():
                subpassed, subfailed, subskipped = 0, 0, 0

                if ('_' in key):
                    testType = key.strip().strip().split('_')[1]
                else:
                    testType = 'OTHER'

                for test in value:
                    status, testTime = 'Skip', 0

                    envSkip = (not android_set and key.endswith('_APK')) or (
                            not java7_set and (key.endswith('SOURCE') or key.endswith('JAVA')))

                    testName, startTest = str(key) + '.' + str(test['testName']), time.time()
                    skipTestTime = False
                    strTest = str(testNum)
                    if testNum < 10:
                        strTest = '0' + str(strTest)
                    print(str(strTest) + '/' + str(numTests) + ' | ' + str(testName) + ' | ', end='', flush=True)
                    if not test['live'] or envSkip or (filter is not None and filter != testType):
                        skipped = skipped + 1
                        subskipped = subskipped + 1
                        skipTestTime = True
                        print('Skip | ', end='', flush=True)
                        skippedTests += [testName]
                    else:
                        testResult = TestUtils.runTest(testName)
                        if (testResult):
                            passed = passed + 1
                            subpassed = subpassed + 1
                            print('Pass | ', end='', flush=True)
                            status = 'Pass'
                        else:
                            failed = failed + 1
                            subfailed = subfailed + 1
                            print('Fail | ', end='', flush=True)
                            failedTests += [testName]
                            status = 'Fail'
                    testNum = testNum + 1
                    if (not skipTestTime):
                        testTime = int(time.time() - startTest)
                    else:
                        testTime = 0
                    print(Utils.prettyTime(testTime))

                    results = TestUtils.addTestResult(results, testType, status, testTime, testName, 0)
                if verbose:
                    print(
                        str(key) + ' Skipped/Passed/Fail/(% passed): ' + str(subskipped) + '/' + str(subpassed) + '/' + str(
                            subfailed) + '/% ' + str(Utils.percent(subpassed, subfailed)))
            while rerun < rerunLim:
                print('Rerunning the failed tests')
                for test in failedTests:
                    print(test, end=' | ', flush=True)
                    start = time.time()
                    result = TestUtils.runTest(test)
                    end = time.time()
                    testTime = int(end - start)
                    if result:
                        result = 'Pass'
                        failed = failed - 1
                        failedTests.remove(test)

                        if ('_' in key):
                            testType = key.strip().strip().split('_')[1]
                        else:
                            testType = 'OTHER'

                        results = TestUtils.reRunTestResult(results, testType, False, test, rerun, testTime)
                    else:
                        result = 'Fail'
                    print(str(result) + ' | ' + Utils.prettyTime(testTime))
                rerun = rerun + 1
        else:
            Utils.custom('test')
        print('==============================')
        print('Time Taken : ' + Utils.prettyTime(int(time.time() - start)))
        print('Skipped Tests: ' + str(skipped))
        print('Passed Tests: ' + str(passed))
        print('Failed Tests: ' + str(failed))
        print('Total Tests: ' + str(numTests))
        print('Total Tests Passed: %' + str(Utils.percent(passed, failed)))
        print('Total Tests Failed: %' + str(Utils.percent(failed, passed)))
        print('==============================')
        if failed > 0:
            print('Failed Tests')
            for test in failedTests:
                print(test)
            print('==============================')
        if len(skippedTests) > 0 and filter is None:
            print('Skipped Tests')
            for test in skippedTests:
                print(test)
            print('==============================')

        # if failed > 0:
        #    if failed > 255:
        #        failed = 255
        #    sys.exit(failed)
        # else:
        #    sys.exit(0)

        Utils.custom('jacocoTestTreport jacocoTestCoverageVerification cobertura coberturaReport')

        return results


# endregion
# region Routers
'''####################################
#The dictionary containing the working functions and their respective definitions
'''  ####################################
routers = {
    #'rawArgs': {
    #    "func": argsUtils.readRawArgs,
    #    "def": "Prints the raw arguments of the program.",
    #    'offline': True
    #},
    #'exampleArgs': {
    #    "func": argsUtils.helpfulArgs,
    #    "def": "Sample examples of running the program with arguments and explanations.",
    #    'offline': True
    #},
    #'writeUsage': {
    #    "func": argsUtils.writeUsage,
    #    "def": "Write the example args to a markdown file (USAGE.md).",
    #    'offline': True
    #},
    'checkEnv': {
        "func": envVars.checkVariables,
        "def": "Checks (suggestions to set them if missing) the environment variables.",
        'offline': True
    },
    'genNotebook': {
        "func": TestUtils.genNotebook,
        "def": "Write a Jupyter Notebook using a given Junit test String.",
        'offline': False
    },
    #'projectType': {
    #    "func": argsUtils.displayProjectTypes,
    #    "def": "Displays some information about the project types available to scan.",
    #    'offline': True
    #},
    #'outputType': {
    #    "func": argsUtils.displayOutputTypes,
    #    "def": "Displays some information about the various output types available to write out as.",
    #    'offline': True
    #},
    #'exceptionType': {
    #    "func": argsUtils.displayExceptionTypes,
    #    "def": "Displays information about the standardized exceptions.",
    #    'offline': True
    #},
    'clean': {
        "func": Utils.clean,
        "def": "Cleans the project.",
        'offline': False
    },
    'build': {
        "func": Utils.build,
        "def": "Builds the project.",
        'offline': False
    },
    'refresh': {
        "func": Utils.refresh,
        "def": "A shortcut to clean and build the project.",
        'offline': False
    },
    'hash': {
        "func": Utils.hash,
        "def": "Determines the hash of a freshly built project.",
        'offline': False
    },
    'buildCmd': {
        "func": argsUtils.basicBuildCommand,
        "def": "Build the command to run, NOTE experimental!",
        'offline': True
    },
    'test': {
        "func": TestUtils.test,
        "def": "Runs a specified test, can also supply the fullyqualified test names, (i.e. smapleTestOne or sampleTestOne,testCWEListing).",
        'offline': False
    },
    'tests': {
        "func": TestUtils.tests,
        "def": "Runs all of the tests crawled.",
        'offline': False
    },
    'testType': {
        "func": TestUtils.testType,
        "def": "Runs a specified set of tests.",
        'offline': False
    },
    'testsHelp': {
        "func": TestUtils.helptests,
        "def": "Shows helpful information about the tests crawled.",
        'offline': False
    },
    'genTest': {
        "func": argsUtils.generateTest,
        "def": 'Generate a test method from a string command. ex: ./cryptosouple.py genTest "-c CMD..." [file]',
        'offline': True
    },
    'displayTests': {
        "func": TestUtils.getDisplayTests,
        "def": "Displays Tests available.",
        'offline': False
    },
    # 'survey': {
    #    "func": Utils.printSurveyURL,
    #    "def": "Displays the url to the survey if it is active.",
    #    'offline': True
    # },
    'help': {
        "func": Utils.help,
        "def": "Displays helpful information to the user if it's the first time running.",
        'offline': True
    },
    'offline': {
        "func": Reading.overWriting,
        "def": "Write enough of the information internally for this script to run stand-alone.",
        'offline': False
    }
}


# endregion

def signal_handler(sig, frame):
    print('\nExiting...')
    sys.exit(0)


'''####################################
#The main runner of this file, intended to be ran from
'''  ####################################
if __name__ == '__main__':
    signal.signal(signal.SIGINT, signal_handler)

    Utils.start()

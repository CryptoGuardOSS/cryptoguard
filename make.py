#!/usr/bin/env python3

import argparse
# Imports
import os
import shlex
import signal
import subprocess
import sys
import time
from collections import OrderedDict

'''####################################
#A utility class that contains the rest of the main common files
'''  ####################################
numTests, failFast = 0, False

class Utils(object):
    # Setting the arguments to be handled by the parser
    def arguments(parser, curChoices):

        parser.add_argument("switch", choices=curChoices, nargs='?', default='tests',
                            help='Use the q flag to show detailed help')
        parser.add_argument("-q", action='store_true', help='Shows the information for the flags')

        return parser

    def routing(switch):
        return routers.get(switch, Utils.tests)

    def routingInfo():
        for val in routers:
            print('\t' + str(val) + ': ' + str(routers[val]['def']) + '\n')
        return

    def pullTests(dir=os.path.join(os.path.abspath(os.curdir), "src", "test")):
        dyct = {}

        for root, dirnames, filenames in os.walk(dir):
            for filename in filenames:
                if (filename.endswith('.java')):
                    fulFoil,className = os.path.join(root, filename), filename.replace('.java','')
                    with open(fulFoil, 'r') as foil:
                        line = foil.readline()
                        while line:
                            if line.__contains__('@Test'):
                                if (className not in dyct):
                                    dyct[className] = []

                                liveTest, method = not line.__contains__('//@Test'), foil.readline()
                                start, end = method.find("public void "), method.find("(")

                                if (start != -1 and end != -1):
                                    testName = method[(start+len("public void ")):end]
                                    dyct[className].append({'testName':testName,'live':liveTest})
                                    global numTests
                                    numTests = numTests + 1

                            line = foil.readline()
        return dyct

    def runTest(test):
        cmd = str(os.path.join(os.path.abspath(os.curdir), 'gradlew')) + ' test --tests ' + str(test) + ' --info --rerun-tasks'
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
        global failFast

        if 'BUILD SUCCESSFUL' in stdout:
            return True
        elif 'BUILD FAILED' in stderr:
            if failFast:
                print(stderr)
                print('Failing on '+str(test));sys.exit(0)
            return False
        else:
            print('Unknown Error: ')
            if failFast:
                print('Failing on '+str(test));sys.exit(0)
            return False

    def percent(x, y):
        if (x == 0):
            return 0
        return round((x/(x+y))*100,2)

    def tests(dyct):
        dyct = OrderedDict(sorted(dyct.items()))
        global numTests
        passed, failed, skipped, testNum, rerun,rerunLim = 0,0,0,1,0,1
        start, failedTests, verbose, skippedTests = time.time(), [], False, []
        
        android, java7, java = os.environ.get('ANDROID_HOME') is not None, os.environ.get('JAVA7_HOME') is not None, os.environ.get('JAVA_HOME') is not None


        if not java:
            print('No JAVA_HOME environment found, please set this')
            sys.exit(1)

        if (not android or not java7):
            print('==============================')
            if not android:
                print('Skipping All Android Tests, no ANDROID_HOME env found')
            else:
                print('Skipping All Project and Java file Tests, no JAVA7_HOME env found')

        print('==============================')
        for key, value in dyct.items():
            subpassed, subfailed, subskipped = 0,0,0
            for test in value:
                envSkip = (not android and key.endswith('_APK')) or (not java7 and (key.endswith('SOURCE') or key.endswith('JAVA')))

                testName, startTest = str(key) + '.' + str(test['testName']), time.time()
                strTest = str(testNum)
                if testNum < 10:
                    strTest = '0'+str(strTest)
                print(str(strTest)+'/'+str(numTests) + ' | ' + str(testName) + ' | ', end='', flush=True)
                if not test['live'] or envSkip:
                    skipped = skipped + 1
                    subskipped = subskipped + 1
                    print('Skip | ', end='', flush=True)
                    skippedTests += [testName]
                else:
                    testResult = Utils.runTest(testName)
                    if (testResult):
                        passed = passed + 1
                        subpassed = subpassed + 1
                        print('Pass | ', end='', flush=True)
                    else:
                        failed = failed + 1
                        subfailed = subfailed + 1
                        print('Fail | ', end='', flush=True)
                        failedTests += [testName]
                testNum = testNum + 1
                print(str(int(time.time()-startTest)) + ' (s)')
            if verbose:
                print(str(key) + ' Skipped/Passed/Fail/(% passed): ' + str(subskipped) + '/' + str(subpassed) + '/' +str(subfailed) + '/% ' + str(Utils.percent(subpassed,subfailed)))
        while rerun < rerunLim:
            print('Rerunning the failed tests')
            for test in failedTests:
                print(test, end=' | ', flush=True)
                start = time.time()
                result = Utils.runTest(test)
                end = time.time()
                if result:
                    result = 'Pass'
                    failed = failed - 1
                    failedTests.remove(test)
                else:
                    result = 'Fail'
                print(str(result) + ' | ' + str(int(end-start)) + ' (s)') 
            rerun = rerun + 1
        print('==============================')
        print('Time Taken : ' + str(int(time.time()-start)) + 's')
        print('Skipped Tests: ' + str(skipped))
        print('Passed Tests: ' + str(passed))
        print('Failed Tests: ' + str(failed))
        print('Total Tests: ' + str(skipped + passed + failed))
        print('Total Tests Passed: %' + str(Utils.percent(passed,failed)))
        print('==============================')
        if failed > 0:
            print('Failed Tests')
            for test in failedTests:
                print(test)
            print('==============================')
        if len(skippedTests) > 0:
            print('Skipped Tests')
            for test in skippedTests:
                print(test)
            print('==============================')

        if failed > 0:
            if failed > 255:
                failed = 255
            sys.exit(failed)
        else:
            sys.exit(0)

    def build():
        argz = ' clean build -x test '
        print('Building the project using | ./gradlew'+str(argz), end='| ', flush=True)
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

        print('Successful | ' + str(int(time.time()-start)) + ' (s)')


    def start():
        curChoices = list(routers.keys())
        args = Utils.arguments(argparse.ArgumentParser(), curChoices).parse_args()
        if (args.q):
            Utils.routingInfo()
            sys.exit()

        Utils.build()
        Utils.routing(args.switch)["func"](Utils.pullTests())


'''####################################
#The dictionary containing the working functions and their respective definitions
'''  ####################################
routers = {
    'tests': {
        "func": Utils.tests,
        "def": "Runs all of the tests crawled."
    }
}

def signal_handler(sig, frame):
        print('\nExiting...')
        sys.exit(0)

'''####################################
#The main runner of this file, intended to be ran from
'''  ####################################
if __name__ == '__main__':
    signal.signal(signal.SIGINT, signal_handler)
    Utils.start()

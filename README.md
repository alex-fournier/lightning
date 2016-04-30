# Overview

[![Join the chat at https://gitter.im/automatictester/lightning](https://badges.gitter.im/automatictester/lightning.svg)](https://gitter.im/automatictester/lightning?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://snap-ci.com/automatictester/lightning/branch/master/build_image)](https://snap-ci.com/automatictester/lightning/branch/master)
[![codecov](https://codecov.io/gh/automatictester/lightning/branch/master/graph/badge.svg)](https://codecov.io/gh/automatictester/lightning)
[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning)

### What's that?

Lightning integrates JMeter non-functional tests with Continuous Integration infrastructure. Lightning analyses results of your non-functional tests and provides your CI server with simple and meaningful information whether to pass or fail the build - with no human involvement needed. Check the [story behind Lightning](https://github.com/automatictester/lightning/wiki/Story-Behind-Lightning) for more information.

### Download

Lightning is a 100% Java tool. It is available as a standalone JAR for download from [Releases](https://github.com/automatictester/lightning/releases) tab.

### Changelog

All new features and bugfixes are included in [release notes](https://github.com/automatictester/lightning/releases).

### Philosophy

- Keep technology stack as close to JMeter as possible
- Be continuous integration server-independent and operating system-independent. Lightning should not be designed to run only in a particular environment, but can offer extra features for certain environments
- Using Lightning shouldn't require coding skills, as JMeter doesn't require that neither
- Release changes frequently
- Be well documented
- Be well tested
- Do not provide bugfixes and support for old versions
- Follow [SemVer](http://semver.org)

### Design assumptions

- JMeter and PerfMon CSV files produced in CI environment should be small enough to be processed by Lightning and stored in memory without hacks

### Project maturity

* Lightning as a standalone JAR has been used in day-to-day delivery for months and client-side tests can be considered production-ready.
* Server-side tests is a new functionality.

# How to start

Lightning is a standalone Java JAR executable from command line.

By default, continuous integration servers mark build as failed based on exit code of executed build step. This behaviour can be amended using CI server-specific features or plugins, which allow to fail build based on build console output.

There are two modes of Lightning execution: `verify` and `report`. Differences between those two are described in the below sections. To make things simple, Lightning running in `verify` mode exits with code 0 if none of the defined tests failed, and 1 if any of the tests failed. Lightning running in `report` mode exits with code 0 if there were no failed JMeter transactions, and 2 if any of JMeter transactions failed. If for some reason you prefer to parse Lightning output, you can do that as well. Lightning offers some extra features for [TeamCity](https://github.com/automatictester/lightning/wiki/Enhanced-TeamCity-Integration) and [Jenkins](https://github.com/automatictester/lightning/wiki/Enhanced-Jenkins-Integration) users.

### Preconditions

- Check your Java version with `java -version`. Lightning requires Java 7 or above.
- Download most recent `lightning-standalone-<version>.jar` from [releases](https://github.com/automatictester/lightning/releases).
- Configure your JMeter tests to [save relevant data](https://github.com/automatictester/lightning/wiki/Configure-JMeter-to-Save-Relevant-Data).

### Verify mode

This is the mode used is most scenarios. Miscellaneous tests described in [test types](https://github.com/automatictester/lightning/wiki/Test-Types) are executed agains JMeter output. Lightning returns 1 exit code on non-zero number of failed tests.

In `verify` mode, Lightning requires at least 2 sources of input data: XML config file and JMeter CSV output. XML file contains definition of tests, which will be executed to determine if execution should be marked as passed or failed, based on analysis of JMeter CSV output.

Lightning XML config file, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<testSet>
    <avgRespTimeTest>
        <testName>Test #1</testName>
        <description>Verify average login times</description>
        <transactionName>Login</transactionName>
        <maxAvgRespTime>4000</maxAvgRespTime>
    </avgRespTimeTest>
</testSet>
```

JMeter CSV output file, e.g.:

```
timeStamp,elapsed,label,responseCode,threadName,dataType,success,bytes,Latency
1434291247743,3514,Login,200,Thread Group 1-2,,true,444013,0
1434291247541,3780,Login,200,Thread Group 1-1,,true,444236,0
1434291247949,3474,Login,200,Thread Group 1-3,,true,444041,0
1434291248160,3448,Login,200,Thread Group 1-4,,true,444712,0
1434291248359,3700,Login,200,Thread Group 1-5,,true,444054,0
1434291251330,10769,Search,200,Thread Group 1-1,,true,1912363,0
1434291251624,10626,Search,200,Thread Group 1-4,,true,1912352,0
1434291251436,11086,Search,200,Thread Group 1-3,,true,1912321,0
1434291251272,11250,Search,200,Thread Group 1-2,,true,1912264,0
1434291252072,11221,Search,200,Thread Group 1-5,,true,1912175,0
```

To run Lightning:

`java -jar lightning-<version>.jar verify -xml=path/to/xml/file --jmeter-csv=path/to/csv/file`

Sample output:

```
Test name:        Test #1
Test description: Verify average login times
Transaction name: Login
Expected result:  Average response time <= 4000
Actual result:    Average response time = 3583.2
Test result:      Pass
```

[Here](https://github.com/automatictester/lightning/wiki/Using-Server-Side-Tests) you can read about using server-side tests.

# Misc

### Issues, questions and feature requests

Issues, questions and feature requests can be raised on [Gitter](https://gitter.im/automatictester/lightning) or using our [issue tracker](https://github.com/automatictester/lightning/issues).

### Contributors

All the information you may need (and even more) can be found in our [wiki](https://github.com/automatictester/lightning/wiki/Info-for-Contributors). Pull requests are welcome!

### License

Released under the MIT license.

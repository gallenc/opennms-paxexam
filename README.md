[<img src="../main/docs/images/opennmsLogo1.png">](https://github.com/OpenNMS/opennms)

# opennms-paxexam

![alt text](../main/docs/images/paxexamLogo.webp "Figure paxexamLogo.webp")
![alt text](../main/docs/images/Apache_Karaf-Logo.wine.png "Figure Apache_Karaf-Logo.wine.png")

Pax-Exam test runner for in-application testing of bundles, kar modules and services for applications based on OSGi including Karaf and OpenNMS. 
It can be used for generic  projects based on Karaf and for more specific development and testing with OpenNMS.

TL;DR To run some test examples see:

[Running Examples](../main/docs/RunningExamples.md)

For a brief video tutorial see:

[OpenNMS Pax-exam demonstration video](https://youtu.be/e6OQfn-xnxg)

## Overview
[Pax-Exam](https://ops4j1.jira.com/wiki/spaces/PAXEXAM4/overview) is a fantastic test framework which supports in-container testing.
 
In normal operation, Pax-Exam downloads and provisions a Karaf OSGi container before injecting a bundle of Junit 4 tests (called a test probe) to test the services running in the framework.
 
This is fine for development testing but what if you have a complex application which already embeds Karaf?
In this case we don't want to download a fresh karaf, but rather run the tests within the karaf already present in the application. 

This project extends the [Pax-Exam code](https://github.com/ops4j/org.ops4j.pax.exam2) to allow it to run Junit tests against an already running Karaf runtime.

## Motivation

The motivation for this project is to make it considerably easier to create and test both plugins and advanced configurations for OpenNMS without having to restart OpenNMS for each change in configuration. 

[OpenNMS](https://github.com/OpenNMS/opennms) is a complex open source network management application written in java and using the [Karaf OSGi implementation](https://karaf.apache.org/manual/latest/). 

Karaf is used as a run-time container within the core OpenNMS and also within distributed [Minion](https://docs.opennms.com/horizon/30/deployment/minion/introduction.html) and [Sentinel](https://docs.opennms.com/horizon/30/deployment/sentinel/introduction.html) modules. 

OpenNMS does makes some limited use of Pax-Exam within it's development test api and when testing some karaf modules. 
See for instance [KarafTestCase.java](https://github.com/OpenNMS/opennms/blob/develop/core/test-api/karaf/src/main/java/org/opennms/core/test/karaf/KarafTestCase.java).
However this is a complex internal use of Pax-exam which requires Pax-exam to run its Karaf test container in-test. 
It does not provide an easy to use example for external plugin development or testing.

The [OpenNMS Plugin API](https://github.com/OpenNMS/opennms-integration-api) leverages OSGi to allow plugins to be deployed and registered in a running OpenNMS application. Plugins are the preferred mechanism for external parties to create additional functionality for OpenNMS without having to modify the core OpenNMS code. 

Plugins are a great idea but until now, it has been difficult to independently develop and test OpenNMS plugins without building a test framework for the module within the code base of the core OpenNMS application. Setting up a development environment has been a complex and error prone activity for a new developer. 

To make plugin development easier, we wanted to create a simple to deploy test harness which can be used by plugin developers without having to dig into the core OpenNMS code. 

In addition, we wanted to make it easier to run tests against advanced configurations such as Drools rules or alarm/event configurations. 

By allowing us to inject tests and run them within a running pre-built OpenNMS it is possible for tests to directly access running OpenNMS services and data through the OpenNMS DAO's. 
This allows pre-test data-fill and event injection to be performed directly on a running system and the results to be monitored as part of a test. 
Ultimately this provides for more realistic and easier to configure integration testing than trying to create mock test frameworks or DAO's for specific modules as is done during development testing. 

In summary the design goals for this project are to create a test framework which :

1. Runs a full pre-built OpenNMS release instance with all dependencies within a docker-compose project on docker desktop - ideally supporting configurations based on the [opennms-forge stackplay](https://github.com/opennms-forge/stack-play) docker-compose examples.
2. Allows any local configurations to be applied through the opennms docker overlay mechanism
3. Provides simple to configure examples of how to set up OpenNMS (or karaf) to work with the external pax-exam project.
4. Injects karaf commands and Junit tests using RMI from an external test project.

## More Information
For more detailed information on how the system works see 

[Design Details](../main/docs/DesignDetails.md)

To run some test examples see

[Running Examples](../main/docs/RunningExamples.md)

## Credits and Licensing

This project is licensed under the Apache 2 licence.

Much of the extension work has built upon the excellent Pax-Exam project.
Apache licence (Copyright 2008 written by Toni Menzel and others).

In addition some test examples from the [Karaf-Cassandra](https://github.com/ANierbeck/Karaf-Cassandra) project have also been adapted. 
Apache licence (Copyright 2015 Achim Nierbeck)

### Note: OpenNMS licensing
OpenNMS is licensed under the AGPL v3 licence. 
Any code linking to or extending OpenNMS code should be similarly licensed. 

For this reason, the OpenNMS example in this project does NOT use any of the core OpenNMS api's but only generic Karaf commands.
 
A separate project will be provided to show example test cases which link directly to the OpenNMS api.


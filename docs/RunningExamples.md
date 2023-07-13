# Running Examples

## Pre-requisites

To build and run these examples you need to have installed

java 11 JDK

maven 3.x

docker

docker-compose

On a windows machine the docker and docker-compose requirements are most easily satisfied using docker-desktop.

## Building the test runner

The test runner and test container for opennms-paxexam needs to be built before you try to run any tests.
This will build and deploy all of the required dependencies in the local user's ~/.m2  repository.

The test runner code is in [opennms-paxexam-parent](../opennms-paxexam-parent) 

```
cd opennms-paxexam-parent
mvn clean install
```
Once the build is successful, you can proceed to running the tests.

## Running the tests

An example test project is provided in [test-example](../test-example)
This contains opennms-paxexam tests in

[opennms-paxexam-test-example](../test-example/opennms-paxexam-test-example)

These tests can be run against either one of two test environments using docker-compose.

[minimal-karaf](../test-example/minimal-karaf)
A simple project which runs a vanilla Karaf container in docker.

[minimal-horizon](../test-example/minimal-horizon)
A more complex project which runs a full OpenNMS horizon system and a couple of snmp agents for testing.

Note that onle one of these projects can be run at a time since they have overlapping public ports. 
In both cases, the RMI port 55555 is exposed for pax-exam to use to install test probes. 

The test environments need set up before you run any tests.
To do this there is a pom.xml file in each project which will download the required dependencies and place them in the correct folders to be picked up when the docker-compose projects are started.

For example in the minimal-karaf project:

```
cd minimal-karaf

mvn clean install

docker-compose up -d
```

Once the project is started you can then run the tests 

```
cd opennms-paxexam-test-example
mvn clean install
```
The tests should complete successfully. 

For more details on how to run the tests and what the tests do, please see the readme files in the individual projects.



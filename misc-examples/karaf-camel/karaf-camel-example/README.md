# Karaf Camel Example

This example shows how a simple [Apache Camel 3](https://camel.apache.org/) application can be packaged as a plugin and tested using paxexam in both a stand alone Karaf container and also within OpenNMS.

## Motivation

The motivation for this example is that Camel provides a lot of [components](https://camel.apache.org/components/3.20.x/index.html) which could be used to significantly speed up the delivery of OpenNMS integration plugins. 
These include number of out of the box connectors to significant OSS platforms including:

* [camel-kafka-connector](https://camel.apache.org/camel-kafka-connector/3.18.x/)
* [camel-service-now-component](https://camel.apache.org/components/3.20.x/servicenow-component.html)
* [camel-salesforce-component](https://camel.apache.org/components/3.20.x/salesforce-component.html)
* [camel-zendesk-component](https://camel.apache.org/components/3.20.x/zendesk-component.html)

Being able to quickly test camel routes and integrate them with the OpenNMS API opens up a lot of possibilities for OpenNMS plugin developers. 

OpenNMS uses camel 2.19.1 internally (as of Horizon 32). 
OSGi class path isolation allows later versions of camel  3.x to be installed for exclusive used of particular plugins. 
In this example, we can build and install a plugin feature based on Camel 3 and test it directly using paxexam tests.

## Test Application
The application under test has been chose to work in both a stand alone Karaf container and also in the embedded openNMS karaf. 
This test application does not use any OpenNMS specific api's or services but just illustrates the deployment of a a Camel 3 based application. 

The application under test is based on the [Karaf Camel Example](https://github.com/apache/karaf/tree/karaf-4.4.3/examples/karaf-camel-example) packaged with the Karaf release. 
This example provides both java and a camel blueprint examples of simple http ReST routes. 

The [original karaf camel example readme](../karaf-camel-example/originalReadme.md) contains the original developer documentation for the example.

These modules have been copied substantially unchanged from the original example 

| [karaf-camel-example-java](../karaf-camel-example/karaf-camel-example-java)| |
| [karaf-camel-example-blueprint](../karaf-camel-example/karaf-camel-example-blueprint)| |
| [karaf-camel-example-features](../karaf-camel-example/karaf-camel-example-features)| |

The [karaf-camel-example-features](../karaf-camel-example/karaf-camel-example-features) module creates a features file which calls in the features from Camel 3 and Apache CXF which are used in the example.

## packaging dependencies as kar archives

Several modules are used to download the dependency bundles and features definitions into the local .m2 repository and also create kar archives for each of the dependency projects.

The [karaf-maven-plugin](https://svn.apache.org/repos/asf/karaf/site/production/manual/latest/karaf-maven-plugin.html) is used to download all of the bundles used in each feature and package these in a kar file.

Unfortunately the karaf-mavel-plugin cannot read the standard camel features file, so this has been modified and re-packaged along with the standard cxf features file. 

The resulting camel features kar is very big (nearly 1G) and probably wouldn't be used in production but it is useful to test any of the camel features in this example.

| [karaf-camel-features-modified](../karaf-camel-example/karaf-camel-features-modified)| |
| [karaf-cxf-features](../karaf-camel-example/karaf-cxf-features)| |
| [karaf-camel-example-kar](../karaf-camel-example/karaf-camel-example-kar)| |


## Test Example

So far we have simply built and packaged original example project.
We now consider the new integration test project which  tests the running example using paxexam.

| [karaf-camel-paxexam-test-example](../karaf-camel-example/karaf-camel-paxexam-test-example)| |

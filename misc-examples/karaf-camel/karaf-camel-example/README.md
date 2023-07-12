# Karaf Camel Example

This example shows how a simple [Apache Camel 3](https://camel.apache.org/) application can be packaged as a plugin and tested using paxexam in both a stand alone Karaf container and also within OpenNMS.

## Motivation

The motivation for this example is that Camel 3 provides a lot of [components](https://camel.apache.org/components/3.20.x/index.html) which could be used to significantly speed up the delivery of OpenNMS integration plugins. 
These include number of out of the box connectors to significant OSS platforms including:

* [camel-kafka-connector](https://camel.apache.org/camel-kafka-connector/3.18.x/)
* [camel-service-now-component](https://camel.apache.org/components/3.20.x/servicenow-component.html)
* [camel-salesforce-component](https://camel.apache.org/components/3.20.x/salesforce-component.html)
* [camel-zendesk-component](https://camel.apache.org/components/3.20.x/zendesk-component.html)

Being able to quickly test camel routes and integrate them with the OpenNMS API opens up a lot of possibilities for OpenNMS plugin developers. 

OpenNMS uses camel 2.19.1 internally (as of Horizon 32). 
OSGi class path isolation allows later versions of camel  3.x to be installed for exclusive use by particular plugins. 
In this example, we can build and install a plugin feature based on Camel 3 and test it directly using paxexam tests.

The example also begins to investigate packaging and deploying complex plugins as Kar's - but more work will be required to make this a safe deployment mechanism.

## Test Application
The application under test has been chosen to work in both a stand alone Karaf container and also in the embedded OpenNMS Karaf container. 
This test application does not use any OpenNMS specific api's or services but just illustrates the deployment of a a Camel 3 based application in Karaf. 

The application under test is based on the [Karaf Camel Example](https://github.com/apache/karaf/tree/karaf-4.4.3/examples/karaf-camel-example) packaged with the Karaf release. 
This example provides both java and a camel blueprint examples of simple http ReST routes. 

The [original karaf camel example readme](../karaf-camel-example/originalReadme.md) contains the upstream developer documentation for the example.

These modules have been copied substantially unchanged from the original example 

| Project Module  | Description |
| ------------- | ------------- |
| [karaf-camel-example-java](../karaf-camel-example/karaf-camel-example-java) | Demonstrates a camel route configured using java. See [CamelComponent.java](../karaf-camel-example/karaf-camel-example-java/src/main/java/org/apache/karaf/examples/camel/java/CamelComponent.java). (A minor bug in the routing has been fixed). |
| [karaf-camel-example-blueprint](../karaf-camel-example/karaf-camel-example-blueprint) | Demonstrates a camel route configured in xml using the Camel Blueprint format. See [route.xml](../karaf-camel-example//karaf-camel-example-blueprint/src/main/resources/OSGI-INF/blueprint/route.xml) |
| [karaf-camel-example-features](../karaf-camel-example/karaf-camel-example-features) | Creates a features file which can load either the blueprint or java configured examples. (Note you can only activate one at a time). The module creates a features file which calls in the features from Camel 3 and Apache CXF which are used in the example. |

## Downloading maven dependencies and packaging as Kar archives

Once the project is built, there are three approaches to deploying it in Karaf.

The first approach is to publish (deploy) the module to a public or private maven repository server and have Karaf use that repo for downloading dependencies.
If a feature:install command is used, Karaf can download the feature definition and bundles from maven and then install the full application.

During development, it is unlikely that a separate maven repository will be used so the second approach is to share the users local ~/.m2/repository  with Karaf. 
Any locally built artifacts will be immediately available to the container. 
This is the approach recommended for use with  the opennms-paxexam test framework.

The third approach is to build a Kar archive of the project which contains the features descriptors and the bundle dependencies. 
The Kar can be placed in the Karaf deploy directory for immediate deployment.
This third approach for building a Kar is also the preferred mechanism for distributing a plugin as a complete stand alone feature which can be inserted into a running system.

Examples of using a Kar and of using a local .m2/repository are provided in this project.

### populating local ./m2/repository

The [karaf-maven-plugin](https://svn.apache.org/repos/asf/karaf/site/production/manual/latest/karaf-maven-plugin.html) is used to download all of the bundles used in each feature in order to have them available in the local .m2/repository.
The plugin is also used to package Kar files. 

Unfortunately the karaf-mavel-plugin cannot read the published camel features file because it cannot deal with version ranges in repository definitions. 

```
apache-camel-3.21.0-features.xml
<features xmlns="http://karaf.apache.org/xmlns/features/v1.5.0" name='camel-3.21.0'>
  <repository>mvn:org.apache.cxf.karaf/apache-cxf/3.6.1/xml/features</repository>
  <repository>mvn:org.ops4j.pax.cdi/pax-cdi-features/[1,2)/xml/features</repository>
  ...
```
Results in the error

```
cant resolve https://repo.maven.apache.org/maven2/org/ops4j/pax/cdi/pax-cdi-features/%5B1,2)/pax-cdi-features-[1,2)-features.xml
```
These issues have been raised with Karaf with no resolution date.

[KARAF-5999](https://issues.apache.org/jira/browse/KARAF-5999) - features-add-to-repository doesn't support feature inner repository with range/

[KARAF-7428](https://issues.apache.org/jira/browse/KARAF-7428) - karaf-maven-plugin and version ranges

So in order to use the karaf-maven-plugin to download our dependencies, we need first to modify and re-package the features file for apache-camel. 
This is done by the [karaf-camel-features-modified](../karaf-camel-example/karaf-camel-features-modified) module by changing the lines specifying a repository version range:

```
<features xmlns="http://karaf.apache.org/xmlns/features/v1.5.0" name='camel-3.21.0'>
  <repository>mvn:org.apache.cxf.karaf/apache-cxf/3.6.1/xml/features</repository>
<!--   <repository>mvn:org.ops4j.pax.cdi/pax-cdi-features/[1,2)/xml/features</repository> -->
  <repository>mvn:org.ops4j.pax.cdi/pax-cdi-features/1.1.4/xml/features</repository>
```

Having modified the camel features, the following plugin configuration can be used to ensure all the required dependencies in the local ./m2/repository. ( See [karaf-camel-example-kar/pom.xml](../karaf-camel-example/karaf-camel-example-kar/pom.xml) )

```
         <plugin>
            <groupId>org.apache.karaf.tooling</groupId>
            <artifactId>karaf-maven-plugin</artifactId>
            ...
               <execution>
                  <id>features-add-to-repo</id>
                  <phase>generate-resources</phase>
                  <goals>
                     <goal>features-add-to-repository</goal>
                  </goals>
                  <configuration>
                     <descriptors>
                        <repository>mvn:org.apache.karaf.features/standard/${karafVersion}/xml/features</repository>
                        <descriptor>mvn:org.opennms.karaf.examples/karaf-camel-example-features/${project.version}/xml</descriptor>
                     </descriptors>
                     <features>
                        <feature>karaf-camel-example-java</feature>
                        <feature>karaf-camel-example-blueprint</feature>
                     </features>
                     <repository>target/repository-local</repository>
                  </configuration>
               </execution>
```

### Kar building

Kar deployment of plugins is not as straight forward as one would hope. 

There is no problem if the required dependencies are shared from a common .m2 repo or downloaded into the Karaf/system repo. 
However problems can occur if the the required dependencies are placed in a Kar file.

Firstly, it seems that if a Kar accidentally packages Karaf system bundles which are already deployed in the system, they will be redeployed when the Kar is placed in the deploy directory. 
The effect of this on OpenNMS is that it can stop the core Karaf bundles working and everything is left in an unknown state.

Secondly, Karaf expects a full application with all of it's dependencies to be in a single Kar and not split across deployments. 
This can result in each feature plugin having a duplicated set of bundles because they need all of their dependencies provided in the same Kar.
There is no concept of Kars waiting for other Kars to be deployed, so if your Kar needs the contents of another Kar, you must deploy them in order.

There are probably answers or work-arounds for all of these issues but the bottom line is that packaging a plugin for deployment requires more thought and testing than is done in this example.

In this project we have just tried to use the standard tooling to at least illustrate a plugin being deployed as a Kar file.

Rather than build an uber-Kar, we have created Kars for all of camel, all of CXF and for the example project. 

Several modules are used to download the dependency bundles and features definitions into the local .m2 repository and also create Kar archives for each of the dependency projects.

| Project Module  | Description |
| ------------- | ------------- |
| [karaf-camel-features-modified](../karaf-camel-example/karaf-camel-features-modified) |This re-creates a features.xml file for Camel and creates a Kar. The resulting camel features Kar is very big (nearly 1G) and probably wouldn't be used in production but it is useful to test any of the camel features in this example. |
| [karaf-cxf-features](../karaf-camel-example/karaf-cxf-features) | This creates a Kar containing all of the Apache CXF features needed by Camel|
| [karaf-camel-example-kar](../karaf-camel-example/karaf-camel-example-kar) |This creates features file and a Kar of the current project |

A profile in the test example can be used to deploy the Kar files to a Karaf before testing rather than run the paxexam tests.


## Test Example

So far we have simply built and packaged original example project.
We now consider the new integration test project which  tests the running example using paxexam.

| Project Module  | Description |
| ------------- | ------------- |
| [karaf-camel-paxexam-test-example](../karaf-camel-example/karaf-camel-paxexam-test-example) |A project containing tests of the Camel module which can be run locally or using paxexam-opennms |

For more details on this module see [karaf-camel-paxexam-test-example](../karaf-camel-example/karaf-camel-paxexam-test-example/README.md)


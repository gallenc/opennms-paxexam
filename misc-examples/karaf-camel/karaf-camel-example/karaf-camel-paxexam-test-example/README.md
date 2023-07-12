# karaf-camel-paxexam-test-example

This is an example test project which will work with either a simple karaf installation or OpenNMS. 
It does not uses any OpenNMS specific API's.

The project illustrates testing a camel ReST project in three scenarios:

* Stand alone tests - Manually installing and testing using feature:install and IDE tests
* Paxexam-opennms driven tests - Automatic installation and testing using opennms-paxexam
* Automatic testing using maven-karaf-plugin without opennms-paxexam - Maven deploying and testing Kar files without paxexam

## Setting up the test environment

To run the tests, you first need to build and install the core classes. 
(In the future , these will probably be committed to the public maven repo but for now you need to build and install them in your local .m2 repository)

### build the opennms-paxexam project

```
cd opennms-paxexam-parent
mvn clean install
```
Next, run maven to install the dependencies in which ever project you want to test and then start the container with docker-compose.

To just run a simple karaf use:

```
cd minimal-karaf
mvn clean install
docker-compose up -d
```

To run a full OpenNMS horizon use:

```
cd minimal-horizon
mvn clean install
docker-compose up -d
```
Note that horizon will take a while to start - particularly if this is the first time you have run the container and the database needs built. 

## build the karaf-camel-example project

The whole karaf-camel-example project should be built before testing. 

```
cd karaf-camel-example
mvn clean install

```
Some of the dependencies will take a while to download. 

No tests will run during the build because they have been designed to run manually within an IDE when a test Karaf container is running.

You should import the projects into your favourite IDE (in my case [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/packages/release/2023-06/r/eclipse-ide-enterprise-java-and-web-developers)) so that you are able to select each test class and run them manually. 

### Stand alone tests

First start either the minimal-karaf or minimal-horizon projects.

open an SSH session to the container and manually install the required feature using the local maven repository. 
For minimal-karaf use

```
# start the karaf project
cd minimal-karaf
docker-compose up -d

# once the karaf has started log into the container and then ssh to the karaf
docker-compose exec karaf1 bash

ssh karaf@localhost -p 8101  -o StrictHostKeyChecking=no
# (password karaf)

```

if you are using minimal-horizon try

```
# start the opennms project
cd minimal-horizon
docker-compose up -d

# once horizon has started log into the container and then ssh to the karaf in horizon
docker-compose exec horizon bash

ssh admin@localhost -p 8101  -o StrictHostKeyChecking=no
# (password admin)

```

Once your test container is running you can install the camel feature


```

# add the features from this project 
feature:repo-add mvn:org.opennms.karaf.examples/karaf-camel-example-features/0.0.1-SNAPSHOT/xml
feature:install karaf-camel-example-blueprint

#(or feature:install karaf-camel-example-java)

# this command should show the feature is installed 
feature:list | grep karaf-camel-example-blueprint

# you can see the application installed and running using
log:tail
```

Now you can manually run the test in the IDE. 

The test class [HttpClientTests.java](../karaf-camel-example/karaf-camel-paxexam-test-example/src/test/java/org/opennms/karaf/httpclient/manual/HttpClientTests.java) contains ReST tests using Apache HTTPClient which exercise the camel routes. 

The tests replicate the curl calls documented in the [original karaf camel example readme](../../karaf-camel-example/originalReadme.md)

### Paxexam-opennms driven tests

The paxexam-opennms driven tests install the features and run the same HTTPClient tests directly using a paxExam probe injected into the container.
The tests manipulate the Karaf container directly to install and uninstall the features.
There is no need to install features directly as in the stand alone tests above.

* [CamelBlueprintTest.java](../karaf-camel-example/karaf-camel-paxexam-test-example/src/test/java/org/opennms/karaf/paxexamest/manual/CamelBlueprintTest.java) Installs and tests karaf-camel-example-blueprint and then uninstalls the module. 
* [CamelJavaTest.java](../karaf-camel-example/karaf-camel-paxexam-test-example/src/test/java/org/opennms/karaf/paxexamest/manual/CamelJavaTest.java) Installs and tests karaf-camel-example-java and then uninstalls the module. 


### Maven deploying and testing Kar files without paxexam.

It is also possible to test deploying the built Kar files directly in karaf or OpenNMS using the opennms-native profile within the [test project's pom.xml](../karaf-camel-example/karaf-camel-paxexam-test-example/pom.xml).

To do this, you can use a native OpenNMS installation or use an OpenNMS docker-compose project without paxexam from the OpenNMS-forge/stack-play repo. 
For example [OpenNMS-forge/stack-play/minimal-horizon](https://github.com/opennms-forge/stack-play/tree/main/minimal-horizon)

Start the project using docker-compose

```
cd stack-play/minimal-horizon
docker-compose up -d
```
Once OpenNMS is running, you can view the logs

```
# once horizon has started log into the container and then ssh to the karaf in horizon
docker-compose exec horizon bash

ssh admin@localhost -p 8101  -o StrictHostKeyChecking=no
# (password admin)
```
Now you can run the tests as part of the maven build.

Thie opennms-native profile deploys the camel application to an opennms without using paxexam or a shared maven .m2 repository.
It then automatically runs the tests in [HttpClientTests.java](../karaf-camel-example/karaf-camel-paxexam-test-example/src/test/java/org/opennms/karaf/httpclient/manual/HttpClientTests.java).

```
mvn clean install -P opennms-native

```
The project will use SFTP to deploy the 3 Kar files into the running horizon and then automatically run the tests in [HttpClientTests.java](../karaf-camel-example/karaf-camel-paxexam-test-example/src/test/java/org/opennms/karaf/httpclient/manual/HttpClientTests.java)





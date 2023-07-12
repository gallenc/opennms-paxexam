# karaf-camel-paxexam-test-example

This is an example test project which will work with either a simple karaf installation or OpenNMS. 
It does not uses any OpenNMS specific API's.

The whole karaf-camel-example project should be built before testing. 

```
cd karaf-camel-example
mvn clean install

```
Some of the dependencies will take a while to download. 

No tests will run during the build because they have been designed to run manually within an ide when a test karaf container is running.

You should import the projects into your favourite ide (in my case [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/packages/release/2023-06/r/eclipse-ide-enterprise-java-and-web-developers)) where you will be able to select each test class and run them manually. 


### Stand alone tests

The test class [](../karaf-camel-example/karaf-camel-paxexam-test-example/src/test/java/org/opennms/karaf/httpclient/manual/HttpClientTests.java)





### Paxexam-opennms driven tests


### deploying and testing Kar files without paxexam.





## Setting up the test environment

To run the tests, you first need to build and install the core classes. 
(In the future , these will probably be committed to the public maven repo but for now you need to build and install them in your local .m2 repository)

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





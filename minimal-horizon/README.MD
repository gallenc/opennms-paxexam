# Example opennms docker-compose project with paxexam

# Overview

## Key configuration elements


### OpenNMS etc configuration
Anyt files placed within opennms-overlay will be copied on startup as an overlay to the existing configuration by the OpenNMS container.

In the docker-compose file, we add 

```
  volumes:
      - ./container-fs/opt/opennms-overlay:/opt/opennms-overlay
```
The configuration files added are listed below
![alt text](../docs/images/injectedFilesOpenNMS.jpg "Figure injectedFilesOpenNMS.jpg")


### OpenNMS deploy
In the docker-compose file, we add 

```
  volumes:
      - ./container-fs/opt/opennms-overlay:/opt/opennms-overlay
```

### injecting the maven .m2 repository
By default, OpenNMS is configured not to download any maven dependencies directly from the internet.
OpenNMS expects any required dependencies to be in the {opennms-home}/system repository or in the 'localRepository'.
So it is necessary to inject the test dependencies from the developer's project directly into the localRepository .m2 repo.

The default karaf location used for the local m2 repo is /usr/share/opennms/.m2

Unfortunately, if we use the default location, the entire .m2 repo will be scanned for jars by the OpenNMS [Bootstrap.java](https://github.com/OpenNMS/opennms/blob/develop/opennms-bootstrap/src/main/java/org/opennms/bootstrap/Bootstrap.java)

This can have unintended consequences depending on which jars are in the users .m2 repo.
In particular, this causes the opennms install to fail because it pulls in the entire .m2 repo into its classpath

see [Installer.java](https://github.com/OpenNMS/opennms/blob/develop/opennms-install/src/main/java/org/opennms/install/Installer.java)

```
context.setClassLoader(Bootstrap.loadClasses(new File(m_opennms_home), true));
```
However Bootstrap.java will not search the {opennms-home}/share directory, so we inject the .m2 repository there

in the docker-compose file, we add 

```
  volumes:
      - ~/.m2/repository:/usr/share/opennms/share/.m2/repository
```
and a corresponding configuraton in
etc/org.ops4j.pax.url.mvn.cfg

```
# setting location of .m2 repository so that it isnt scanned by the Bootloader which doesn't scan {opennms-home}/share
org.ops4j.pax.url.mvn.localRepository=/usr/share/opennms/share/.m2/repository
```
This makes the user's .m2/repository available for use by OpenNMS to resolve both the pax-exam dependencies but also to pick up the built modules from the developer's plugin project.

## Injecting jars into {opennms-home}/lib
Because RMI has no knowledge of maven or OSGi, it is neccessary to make some of the pax-exam jars directly available on the OpenNMS classpath.
These are injected into to {opennms-home}/lib directory

## Additional test classes {opennms-home}/share/misc
For debugging purposes and in order to test directly that the RMI is working, a test class is provided in the opennms-paxexam-container-remote.jar. 
This test tries to access the local RMI registry and list any registered services. 

This test class and it's dependencies are placed in the /opt/opennms/share/misc folder.
in the docker-compose file, we add 

```
  volumes:
      - ./container-fs/misc:/opt/opennms/share/misc

```
The test can be run within the OpenNMS horizon docker container using the following command. 
(You can change the address and port to suit the local configuration. e.g. TestRMILookup horizon 55555). 
```
cd /usr/share/opennms/share/misc
java -cp opennms-paxexam-rmi-server-0.0.1-SNAPSHOT.jar:opennms-paxexam-container-remote-0.0.1-SNAPSHOT.jar:pax-exam-container-rbc-onms-4.13.5.jar:pax-exam-4.13.5.jar:org.osgi.core-6.0.0.jar org.opennms.paxexam.container.main.TestRMILookup localhost 55555

result:

using arguments address=localhost port=55555
TestServiceLookup
registry:RegistryImpl_Stub[UnicastRef [liveRef: [endpoint:[localhost:55555](remote),objID:[0:0:0, 0]]]]
simple test registry names:
number of registry names:1
name: PaxExam
   name: PaxExam
 obj class: com.sun.proxy.$Proxy0
 obj.toString():  Proxy[RemoteBundleContext,RemoteObjectInvocationHandler[UnicastRef2 [liveRef: [endpoint:[localhost:55555,org.opennms.integration.paxexam.rmitestserver.PaxexamRMIServerSocketFactory@4cdbe50f](remote),objID:[392b447d:188ec5621b2:-7ff5, 1607189668500639864]]]]]
```





You can then start and stop opennms in this project.
(You should have docker installed on your host).

```
docker-compose    up -d

## to see the logs use
docker-compose  logs -f horizon

## to shut down use
docker-compose  down

to see opennms

http://[::1]:8980/opennms/   username admin password admin
or
http://localhost:8980/opennms/



```

## to view OpenNMS logs as opennms starts up use

```
 docker-compose --profile roadfaultapi logs -f horizon
```

## to open a terminal in the running opennms container use

```
docker-compose --profile roadfaultapi exec horizon bash
```

## to ssh to karaf in opennms use
```
ssh admin@horizon -p 8101  -o StrictHostKeyChecking=no
```


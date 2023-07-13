# minimal-karaf docker-compose

This project enables tests to be injected against a standard Karaf docker container

Before starting this container, you should build the [opennms-paxexam-parent](../../opennms-paxexam-parent) project

```
cd opennms-paxexam-parent
mvn clean install
```

Then use maven to install the correct opennms-paxexam dependencies in the minimal-karaf project

```
cd minimal-karaf
mvn clean install
```

Start the container

```
cd minimal-karaf
docker-compose up -d
```

To view OpenNMS logs as karaf starts up use

```
 docker-compose logs -f karaf1 
 (ctrl-c to exit)
```

You can log into the container using
```
docker-compose exec karaf1 bash
```
Once inside you can follow the logs using
```
 tail -f /opt/apache-karaf/data/log/karaf.log
```

If you want to access the karaf terminal, you can use the local client while inside the container without a password use
```
/opt/apache-karaf/bin/client -a 8101
```

Or if you want to use ssh, you can install an ssh client in the container.
Note to install an ssh client this you may need to have logged into the container as user root using
```
docker-compose exec -u root karaf1 bash

apt-get install update
apt-get install openssh-client

 ssh karaf@karaf1 -p 8101  -o StrictHostKeyChecking=no
# (password karaf)
```

Once the container is running, you can run the tests in [opennms-paxexam-test-example](../opennms-paxexam-test-example) and view the output in the container log.

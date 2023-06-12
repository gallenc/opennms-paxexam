# example opennms with paxexam



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


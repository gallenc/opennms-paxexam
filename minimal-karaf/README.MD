# misc notes

 tail -f /opt/apache-karaf/data/log/karaf.log
 
 ssh karaf@karaf1 -p 8101  -o StrictHostKeyChecking=no
 (password karaf)
 
 bin/client -a 8101
 
/opt/apache-karaf/bin/client -a 8101
 
apt-get install update
  
apt-get install openssh-client


```

## to view OpenNMS logs as opennms starts up use

```
 docker-compose logs -f karaf1 
```

## to open a terminal in the running opennms container use

```
docker-compose - exec karaf1 bash
```

## to ssh to karaf from another container use
```
ssh karaf@karaf1 -p 8101  -o StrictHostKeyChecking=no
(password karaf)
```


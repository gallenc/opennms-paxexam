# FAQs and issues

## port 555555 is blocked on Windows 10 when starting docker-compose

On Windows 10, particularly following an update you may find port 555555 blocked when you try to start a project. 
This may be due to port reservation by docker-desktop.

```
docker-compose up
...
docker compose up error - Error response from daemon: Ports are not available: exposing port TCP 0.0.0.0:55555 -> 0.0.0.0:0: listen tcp 0.0.0.0:55555: bind: An attempt was made to access a socket in a way forbidden by its access permissions.

```
after a windows and / or docker-desktop update, the exclusion ranges included 555555

You can see the access permissions using
```
netsh interface ipv4 show excludedportrange protocol=tcp
Protocol tcp Port Exclusion Ranges

Start Port    End Port
----------    --------
...
55472       55571

```

This can be fixed using method described in https://stackoverflow.com/questions/58216537/what-is-administered-port-exclusions-in-windows-10

Stop docker-desktop (You may need to restart the PC)

run power shell commands as administrator (note net stop winnat will stop networking until restarted)
```
net stop winnat
netsh int ipv4 add excludedportrange protocol=tcp startport=55555 numberofports=10
net start winnat
netsh interface ipv4 show excludedportrange protocol=tcp

Protocol tcp Port Exclusion Ranges

Start Port    End Port
----------    --------
      5985        5985
     47001       47001
     49678       49777
     50000       50059     *
     55555       55564     *
 ...

* - Administered port exclusions.

```
After adding the exclusion start docker-desktop.
opennms-paxexam should now work. 

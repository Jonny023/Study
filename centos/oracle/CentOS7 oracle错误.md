# CentOS7 oracle错误

> 错误提示`Instance "orcl", status UNKNOWN, has 3 handler(s) for this service...`

```bash
[oracle@oracledb dbs]$ lsnrctl start

LSNRCTL for Linux: Version 11.2.0.1.0 - Production on 19-MAY-2019 17:53:28

Copyright (c) 1991, 2009, Oracle.  All rights reserved.

Starting /data/app/oracle/product/11.2.0//bin/tnslsnr: please wait...

TNSLSNR for Linux: Version 11.2.0.1.0 - Production
System parameter file is /data/app/oracle/product/11.2.0/network/admin/listener.ora
Log messages written to /data/app/oracle/diag/tnslsnr/oracledb/listener/alert/log.xml
Listening on: (DESCRIPTION=(ADDRESS=(PROTOCOL=ipc)(KEY=EXTPROC1521)))
Listening on: (DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=192.168.1.101)(PORT=1521)))

Connecting to (DESCRIPTION=(ADDRESS=(PROTOCOL=IPC)(KEY=EXTPROC1521)))
STATUS of the LISTENER
------------------------
Alias                     LISTENER
Version                   TNSLSNR for Linux: Version 11.2.0.1.0 - Production
Start Date                19-MAY-2019 17:53:28
Uptime                    0 days 0 hr. 0 min. 0 sec
Trace Level               off
Security                  ON: Local OS Authentication
SNMP                      OFF
Listener Parameter File   /data/app/oracle/product/11.2.0/network/admin/listener.ora
Listener Log File         /data/app/oracle/diag/tnslsnr/oracledb/listener/alert/log.xml
Listening Endpoints Summary...
  (DESCRIPTION=(ADDRESS=(PROTOCOL=ipc)(KEY=EXTPROC1521)))
  (DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=192.168.1.101)(PORT=1521)))
Services Summary...
Service "orcl" has 1 instance(s).
  Instance "orcl", status UNKNOWN, has 3 handler(s) for this service...
The command completed successfully
```

> 查看日志

```bash
[oracle@oracledb dbs]$ tail -30 /data/app/oracle/diag/tnslsnr/oracledb/listener/alert/log.xml
<msg time='2019-05-19T17:53:28.124+08:00' org_id='oracle' comp_id='tnslsnr'
 type='UNKNOWN' level='16' host_id='oracledb'
 host_addr='127.0.0.1'>
 <txt>19-MAY-2019 17:53:28 * (CONNECT_DATA=(CID=(PROGRAM=)(HOST=oracledb)(USER=oracle))(COMMAND=status)(ARGUMENTS=64)(SERVICE=LISTENER)(VERSION=186646784)) * status * 0
 </txt>
</msg>
<msg time='2019-05-19T17:53:28.152+08:00' org_id='oracle' comp_id='tnslsnr'
 type='UNKNOWN' level='16' host_id='oracledb'
 host_addr='127.0.0.1'>
 <txt>19-MAY-2019 17:53:28 * service_register * 12508
 </txt>
</msg>
<msg time='2019-05-19T17:53:28.152+08:00' org_id='oracle' comp_id='tnslsnr'
 type='UNKNOWN' level='16' host_id='oracledb'
 host_addr='127.0.0.1'>
 <txt>TNS-12508: TNS:listener could not resolve the COMMAND given
 </txt>
</msg>
<msg time='2019-05-19T17:53:28.152+08:00' org_id='oracle' comp_id='tnslsnr'
 type='UNKNOWN' level='16' host_id='oracledb'
 host_addr='127.0.0.1'>
 <txt>19-MAY-2019 17:53:28 * service_register * 12508
 </txt>
</msg>
<msg time='2019-05-19T17:53:28.152+08:00' org_id='oracle' comp_id='tnslsnr'
 type='UNKNOWN' level='16' host_id='oracledb'
 host_addr='127.0.0.1'>
 <txt>TNS-12508: TNS:listener could not resolve the COMMAND given
 </txt>
</msg>
```



> host设置

```bash
[root@oracledb sysconfig]# cat /etc/hosts
#127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
#::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
#127.0.0.1     oracledb

127.0.0.1     localhost localhost
192.168.1.101 oracledb
```

> 连接数据库

```bash
[oracle@oracledb dbs]$ sqlplus / as sysdba

SQL*Plus: Release 11.2.0.1.0 Production on Sun May 19 18:51:04 2019

Copyright (c) 1982, 2009, Oracle.  All rights reserved.


Connected to:
Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production
With the Partitioning, Oracle Label Security, OLAP, Data Mining,
Oracle Database Vault and Real Application Testing options

SQL> show parameter service

NAME				     TYPE
------------------------------------ ----------------------
VALUE
------------------------------
service_names			     string
orcl
SQL> show parameter service

NAME				     TYPE
------------------------------------ ----------------------
VALUE
------------------------------
service_names			     string
orcl
SQL> alter system set service_names='oracledb'; # 设置主机服务

System altered.

SQL> commit;

Commit complete.

SQL> shutdown
Database closed.
Database dismounted.
ORACLE instance shut down.
SQL> startup
ORACLE instance started.

Total System Global Area  409194496 bytes
Fixed Size		    2213856 bytes
Variable Size		  289409056 bytes
Database Buffers	  109051904 bytes
Redo Buffers		    8519680 bytes
Database mounted.
Database opened.
SQL> 
```

> 重启监听，提示`Instance "ORCL", status READY, has 1 handler(s) for this service...`

```bash
[oracle@oracledb ~]$ lsnrctl status

LSNRCTL for Linux: Version 11.2.0.1.0 - Production on 19-MAY-2019 18:53:16

Copyright (c) 1991, 2009, Oracle.  All rights reserved.

Connecting to (DESCRIPTION=(ADDRESS=(PROTOCOL=IPC)(KEY=EXTPROC1521)))
STATUS of the LISTENER
------------------------
Alias                     LISTENER
Version                   TNSLSNR for Linux: Version 11.2.0.1.0 - Production
Start Date                19-MAY-2019 18:50:07
Uptime                    0 days 0 hr. 3 min. 9 sec
Trace Level               off
Security                  ON: Local OS Authentication
SNMP                      OFF
Listener Parameter File   /data/app/oracle/product/11.2.0/network/admin/listener.ora
Listener Log File         /data/app/oracle/diag/tnslsnr/oracledb/listener/alert/log.xml
Listening Endpoints Summary...
  (DESCRIPTION=(ADDRESS=(PROTOCOL=ipc)(KEY=EXTPROC1521)))
  (DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=192.168.1.101)(PORT=1521)))
Services Summary...
Service "oracledb" has 1 instance(s).
  Instance "ORCL", status READY, has 1 handler(s) for this service...
Service "orcl" has 3 instance(s).
  Instance "ORCL", status READY, has 1 handler(s) for this service...
  Instance "orcl", status UNKNOWN, has 3 handler(s) for this service...
  Instance "orcl", status BLOCKED, has 1 handler(s) for this service...
Service "orclXDB" has 1 instance(s).
  Instance "ORCL", status READY, has 1 handler(s) for this service...
The command completed successfully
```


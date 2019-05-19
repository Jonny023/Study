# CentOS7静默安装Oracle数据库



## 安装准备



* [下载地址](https://pan.baidu.com/s/1gsyVwVs0C0KjvHE5xgN5YA) 密码: `v65p`

* 防火墙放行

  ```bash
  firewall-cmd --zone=public --add-port=1521/tcp --permanent  //放开1521端口
  
  firewall-cmd --reload  //在不改变状态的条件下重新加载防火墙配置文件
  ```

  

### 防火墙

* 查看防火墙的状态：`systemctl status firewalld`

* 启动防火墙服务：`systemctl start firewalld`

* 禁用防火墙：`systemctl disable firewalld`

* 重载配置文件：`firewall-cmd --reload`

* 查看已经开放的端口：`firewall-cmd --list-ports`



#### 默认是不提供swap分区的，这里需要自己动手加一下

```bash
dd if=/dev/zero of=/swap bs=1024 count=2048000
mkswap /swap
chmod 600 /swap
swapon /swap
echo "/swap swap swap default 0 0">>/etc/fstab  #设置成自动挂载
```



#### 配置hostname

```bash
hostnamectl set-hostname oracledb
echo "127.0.0.1     oracledb" >>/etc/hosts
```



#### 关闭selinux

```bash
sed -i "s/SELINUX=enforcing/SELINUX=disabled/" /etc/selinux/config  
setenforce 0
```



#### 安装依赖命令

```bash
yum -y install binutils compat-libcap1 compat-libstdc++-33 gcc gcc-c++ glibc glibc-devel ksh libaio libaio-devel libgcc libstdc++ libstdc++-devel libXi libXtst make sysstat unixODBC unixODBC-devel
```



#### 检查是否安装成功

```bash
rpm -q binutils compat-libcap1 compat-libstdc++-33 gcc gcc-c++ glibc glibc-devel ksh libaio libaio-devel libgcc libstdc++ libstdc++-devel libXi libXtst make sysstat unixODBC unixODBC-devel | grep "not installed"
```

> 如果没有指定yum源可自行安装。



#### 创建所需的操作系统组和用户

> 如果要安装Oracle数据库，则需要以下本地操作系统组和用户：

- `Oracle inventory`组(通常为 `oinstall`)
- `OSDBA`组 (通常为 `dba`)
- `OSOPER`组 (通常为 `oper`)
- `Oracle`软件所有者(通常为 `oracle`)

```bash
groupadd oinstall
groupadd dba
groupadd oper
useradd -g oinstall -G dba oracle
```

> 修改oracle用户密码

```bash
passwd oracle
```



#### 配置内核参数和资源限制

* 在`/etc/sysctl.conf`添加如下参数，如果系统中某个参数高于下面的参数的值 ，保留较大的值,下面的数值只是官方要求的最小值，可以根据系统调整数值，以优化系统性能

```bash
fs.aio-max-nr = 1048576
fs.file-max = 6815744
kernel.shmall = 2097152
kernel.shmmax = 536870912
kernel.shmmni = 4096
kernel.sem = 250 32000 100 128
net.ipv4.ip_local_port_range = 9000 65500
net.core.rmem_default = 262144
net.core.rmem_max = 4194304
net.core.wmem_default = 262144
net.core.wmem_max = 1048576
```

* 使内核参数生效

```bash
sysctl -p
```

* 在`/etc/security/limits.conf`中添加如下参数

```bash
oracle              soft    nproc   2047
oracle              hard    nproc   16384
oracle              soft    nofile  1024
oracle              hard    nofile  65536
```

* 在`/etc/pam.d/login`文件中，添加下面内容

```bash
session required /lib64/security/pam_limits.so
session required pam_limits.so
```

* `/etc/profile` 文件中添加如下内容

```bash
if [ $USER = "oracle" ]; then
   if [ $SHELL = "/bin/ksh" ]; then
       ulimit -p 16384
       ulimit -n 65536
    else
       ulimit -u 16384 -n 65536
   fi
fi
```

* 使用`/etc/profile`文件生效

```bash
source /etc/profile
```

* 禁用使用`Transparent HugePages`(启用`Transparent HugePages`,可能会导致造成内存在运行时的延迟分配，`Oracle`官方建议使用标准的`HugePages`)

* 查看是否启用 如果显示 `[always]`说明启用了

```bash
cat /sys/kernel/mm/transparent_hugepage/enabled
```

* 禁用`Transparent HugePages`,在`/etc/grub.conf`添加如下内容

```bash
echo never > /sys/kernel/mm/transparent_hugepage/enabled
```

>  重新启动系统以使更改成为永久更改



#### 配置oracle用户环境变量

* 在文件`/home/oracle/.bash_profile`里添加下面内容(具体值根据实际情况修改)

```bash
umask 022
export ORACLE_HOSTNAME=oracledb
export ORACLE_BASE=/data/app/oracle
export ORACLE_HOME=$ORACLE_BASE/product/11.2.0/
export ORACLE_SID=ORCL
export PATH=.:$ORACLE_HOME/bin:$ORACLE_HOME/OPatch:$ORACLE_HOME/jdk/bin:$PATH
export LC_ALL="en_US"
export LANG="en_US"
export NLS_LANG="AMERICAN_AMERICA.ZHS16GBK"
export NLS_DATE_FORMAT="YYYY-MM-DD HH24:MI:SS"
```



#### 重启系统

```bash
reboot
```



#### 上传`oracle`压缩包

```bash
[root@oracledb oracle]# pwd
/home/oracle
[root@oracledb oracle]# ll
总用量 2295592
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Desktop
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Documents
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Downloads
-rw-r--r--. 1 oracle oinstall 1239269270 5月  14 21:05 linux.x64_11gR2_database_1of2.zip
-rw-r--r--. 1 oracle oinstall 1111416131 5月  14 21:04 linux.x64_11gR2_database_2of2.zip
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Music
drwxr-x---. 3 oracle oinstall         18 5月  15 00:06 oradiag_oracle
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Pictures
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Public
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Templates
drwxr-xr-x. 2 oracle oinstall          6 5月  14 22:24 Videos
```



#### 解压下载好的两个Oracle数据库文件

```bash
unzip -q linux.x64_11gR2_database_1of2.zip -d /data
unzip -q linux.x64_11gR2_database_2of2.zip -d /data
mkdir -p /data/etc
cp /data/database/response/* /data/etc/
```

* 在`/data/etc/db_install.rsp`修改以下变量的值

```bash
oracle.install.option=INSTALL_DB_SWONLY
DECLINE_SECURITY_UPDATES=true
UNIX_GROUP_NAME=oinstall
INVENTORY_LOCATION=/data/app/oracle/inventory
SELECTED_LANGUAGES=en,zh_CN
ORACLE_HOSTNAME=oracledb
ORACLE_HOME=/data/app/oracle/product/11.2.0
ORACLE_BASE=/data/app/oracle
oracle.install.db.InstallEdition=EE
oracle.install.db.isCustomInstall=true
oracle.install.db.DBA_GROUP=dba
oracle.install.db.OPER_GROUP=dba
```



#### 开始安装

```bash
su - oracle
cd /data/database
./runInstaller -silent -responseFile /data/etc/db_install.rsp -ignorePrereq
```

* 安装报错

```bash
[oracle@oracledb database]$ ./runInstaller -silent -responseFile /data/etc/db_install.rsp -ignorePrereq
正在启动 Oracle Universal Installer...

检查临时空间: 必须大于 120 MB。   实际为 12859 MB    通过
检查交换空间: 必须大于 150 MB。   实际为 4033 MB    通过
准备从以下地址启动 Oracle Universal Installer /tmp/OraInstall2019-05-16_09-04-16PM. 请稍候...[oracle@oracledb database]$ [FATAL] [INS-32012] 无法创建目录。
   原因: 没有授予创建目录的正确权限, 或卷中没有剩余空间。
   操作: 请检查您对所选目录的权限或选择另一个目录。
[FATAL] [INS-32012] 无法创建目录。
   原因: 没有授予创建目录的正确权限, 或卷中没有剩余空间。
   操作: 请检查您对所选目录的权限或选择另一个目录。
此会话的日志当前已保存为: /tmp/OraInstall2019-05-16_09-04-16PM/installActions2019-05-16_09-04-16PM.log。如果要保留此日志, Oracle 建议将它从临时位置移动到更持久的位置。
```

> 推测：没有权限

* 切换到root用户执行

```bash
mkdir -p /data/app/oracle
chown -R oracle:oinstall /data/app/oracle
chmod -R 775 /data/app/oracle
```

> 再次执行：`./runInstaller -silent -responseFile /data/etc/db_install.rsp -ignorePrereq`

* 安装期间可以使用`tail`命令监看`oracle`的安装日志（执行命令后有提示日志文件路径）

```bash
#日志文件名称根据自己的实际执行时间变更
tail -f /data/app/oracle/inventory/logs/installActions2019-01-02_06-03-30PM.log
```

* 安装完成后有如下提示，如果有类似如下提示，说明安装完成

```bash
以下配置脚本需要以 "root" 用户的身份执行。
 #!/bin/sh 
 #要运行的 Root 脚本

/data/app/oracle/inventory/orainstRoot.sh
/data/app/oracle/product/11.2.0/root.sh
要执行配置脚本, 请执行以下操作:
	 1. 打开一个终端窗口
	 2. 以 "root" 身份登录
	 3. 运行脚本
	 4. 返回此窗口并按 "Enter" 键继续

信息: Shutting down OUISetupDriver.JobExecutorThread
信息: Cleaning up, please wait...
信息: Dispose the install area control object
信息: Update the state machine to STATE_CLEAN
Successfully Setup Software.
```



#### 使用`root`用户执行脚本

```bash
su - root
sh /data/app/oracle/inventory/orainstRoot.sh
sh /data/app/oracle/product/11.2.0/root.sh
```



#### 配置监听程序

```bash
[root@oracledb ~]# su - oracle
上一次登录：四 5月 16 21:04:01 CST 2019pts/0 上
[oracle@oracledb ~]$ netca /silent /responsefile /data/etc/netca.rsp

Parsing command line arguments:
    Parameter "silent" = true
    Parameter "responsefile" = /data/etc/netca.rsp
Done parsing command line arguments.
Oracle Net Services Configuration:
Profile configuration complete.
Oracle Net Listener Startup:
    Running Listener Control: 
      /data/app/oracle/product/11.2.0/bin/lsnrctl start LISTENER
    Listener Control complete.
    Listener started successfully.
Listener configuration complete.
Oracle Net Services configuration successful. The exit code is 0
```



#### 查看监听端口

```bash
netstat -tnpl | grep 1521

[oracle@oracledb ~]$ netstat -tnpl | grep 1521
(Not all processes could be identified, non-owned process info
 will not be shown, you would have to be root to see it all.)
tcp6       0      0 :::1521                 :::*                    LISTEN      12655/tnslsnr 
```



#### 静默创建数据库

> 防止配置出错，先备份下，切换至`root`用户执行

```bash
cp -p /data/etc/dbca.rsp /data/etc/dbca.rsp.bak
```

* 然后在编辑应答文件`/data/etc/dbca.rsp`

```bash
[GENERAL]
RESPONSEFILE_VERSION = "11.2.0"
OPERATION_TYPE = "createDatabase"
[CREATEDATABASE]
GDBNAME = "orcl"
SID = "orcl"
SYSPASSWORD = "oracle"
SYSTEMPASSWORD = "oracle"
SYSMANPASSWORD = "oracle"
DBSNMPPASSWORD = "oracle"
DATAFILEDESTINATION =/data/app/oracle/oradata
RECOVERYAREADESTINATION=/data/app/oracle/fast_recovery_area
CHARACTERSET = "AL32UTF8"
TOTALMEMORY = "1638"
```

* 完整配置，可直接替换原文件内容

```bash
[GENERAL]
RESPONSEFILE_VERSION = "11.2.0"
OPERATION_TYPE = "createDatabase"


[CREATEDATABASE]
GDBNAME = "orcl"
SID = "orcl"
TEMPLATENAME = "General_Purpose.dbc"
SYSPASSWORD = "oracle"
SYSTEMPASSWORD = "oracle"
SYSMANPASSWORD = "oracle"
DBSNMPPASSWORD = "oracle"
DATAFILEDESTINATION =/data/app/oracle/oradata
RECOVERYAREADESTINATION=/data/app/oracle/fast_recovery_area
CHARACTERSET = "AL32UTF8"
TOTALMEMORY = "1638"

[createTemplateFromDB]
SOURCEDB = "myhost:1521:orcl"
SYSDBAUSERNAME = "system"
TEMPLATENAME = "My Copy TEMPLATE"

[createCloneTemplate]
SOURCEDB = "orcl"
TEMPLATENAME = "My Clone TEMPLATE"

[DELETEDATABASE]
SOURCEDB = "orcl"

[generateScripts]

TEMPLATENAME = "New Database"
GDBNAME = "orcl"

[CONFIGUREDATABASE]

[ADDINSTANCE]
DB_UNIQUE_NAME = "orcl"
NODELIST=
SYSDBAUSERNAME = "sys"

[DELETEINSTANCE]
DB_UNIQUE_NAME = "orcl"
INSTANCENAME = "orcl11g"
SYSDBAUSERNAME = "sys"
```



* 执行静默建库

```bash
su - oracle
dbca -silent -responseFile /data/etc/dbca.rsp
```

* 安装过程如下

```bash
[oracle@oracledb ~]$ dbca -silent -responseFile /data/etc/dbca.rsp
Copying database files
1% complete
3% complete
11% complete
18% complete
26% complete
37% complete
Creating and starting Oracle instance
40% complete
45% complete
50% complete
55% complete
56% complete
60% complete
62% complete
Completing Database Creation
66% complete
70% complete
73% complete
85% complete
96% complete
100% complete
Look at the log file "/data/app/oracle/cfgtoollogs/dbca/orcl/orcl.log" for further details.
```



#### 查看监听

> 切换帐户一定要加 "-"会出现：   bash:lsnrctl:command not found.错误（未知命令）

```bash
切换帐户

$ su - oracle

启动监听

$ lsnrctl start

关闭监听

$ lsnrctl stop
```



#### 数据库安装按成，连接数据库

```bash
su - oracle
sqlplus / as sysdba
select status from v$instance;
```

* 如下

```bash
[oracle@oracledb ~]$ sqlplus / as sysdba

SQL*Plus: Release 11.2.0.1.0 Production on Thu May 16 22:17:14 2019

Copyright (c) 1982, 2009, Oracle.  All rights reserved.

Connected to an idle instance.

SQL> select status from v$instance;
select status from v$instance
*
ERROR at line 1:
ORA-01034: ORACLE not available
Process ID: 0
Session ID: 0 Serial number: 0
```

#### 解决方式

> 输入：`startup`

* 出现如下提示

```bash
SQL> startup
ORA-01078: failure in processing system parameters
LRM-00109: could not open parameter file '/data/app/oracle/product/11.2.0/dbs/initORCL.ora'
```

> 根据上面提示，复制
>
> `/data/app/oracle/admin/orcl/pfile/init.ora.416201922139` 到`/data/app/oracle/product/11.2.0/dbs/`
>
> 并命名为：`initORCL.ora`

```bash
[oracle@oracledb root]$ cp -p /data/app/oracle/admin/orcl/pfile/init.ora.416201922139 /data/app/oracle/product/11.2.0/dbs/initORCL.ora
[oracle@oracledb root]$
```

> 再次执行：`startup`

```bash
SQL> startup
ORACLE instance started.

Total System Global Area  409194496 bytes
Fixed Size		    2213856 bytes
Variable Size		  268437536 bytes
Database Buffers	  130023424 bytes
Redo Buffers		    8519680 bytes
ORA-01102: cannot mount database in EXCLUSIVE mode
```

* 查看数据库版本

```bash
SQL> select * from v$version;

BANNER
--------------------------------------------------------------------------------
Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production
PL/SQL Release 11.2.0.1.0 - Production
CORE	11.2.0.1.0	Production
TNS for Linux: Version 11.2.0.1.0 - Production
NLSRTL Version 11.2.0.1.0 - Production
```



#### 激活`scott`用户

```bash
# 创建用户scott
alter user scott account unlock; 
# 设置scott密码为tiger
alter user scott identified by tiger;
select username,account_status from dba_users;
```



#### 错误提示

> ORA-00205: error in identifying control file, check alert log for more info

* 查看日志位置，指向的是`/data/app/oracle/diag/rdbms/orcl/ORCL/trace`

```bash
SQL> show parameter dump

NAME				     TYPE
------------------------------------ ----------------------
VALUE
------------------------------
background_core_dump		     string
partial
background_dump_dest		     string
/data/app/oracle/diag/rdbms/or
cl/ORCL/trace
core_dump_dest			     string
/data/app/oracle/diag/rdbms/or
cl/ORCL/cdump
max_dump_file_size		     string

NAME				     TYPE
------------------------------------ ----------------------
VALUE
------------------------------
unlimited
shadow_core_dump		     string
partial
user_dump_dest			     string
/data/app/oracle/diag/rdbms/or
cl/ORCL/trace
```

* 查看`/data/app/oracle/diag/rdbms/orcl/ORCL/trace`下的文件

```bash
[oracle@oracledb dbs]$ ll /data/app/oracle/diag/rdbms/orcl/ORCL/trace
总用量 200
-rw-r----- 1 oracle oinstall 14714 5月  16 22:36 alert_ORCL.log
-rw-r----- 1 oracle oinstall  1870 5月  16 22:36 ORCL_ckpt_18044.trc
-rw-r----- 1 oracle oinstall   395 5月  16 22:36 ORCL_ckpt_18044.trm
-rw-r----- 1 oracle oinstall  3006 5月  16 22:36 ORCL_m000_18087.trc
-rw-r----- 1 oracle oinstall   185 5月  16 22:36 ORCL_m000_18087.trm
-rw-r----- 1 oracle oinstall   886 5月  16 22:26 ORCL_mman_17131.trc
```

* 查看`alert_ORCL.log`

```bash
cat /data/app/oracle/diag/rdbms/orcl/ORCL/trace/alert_ORCL.log
```

> 主要错误提示,此位置文件已经存在

```bash
ORA-00210: cannot open the specified control file
ORA-00202: control file: '/data/app/oracle/flash_recovery_area/orcl/control02.ctl'
ORA-27086: unable to lock file - already in use
Linux-x86_64 Error: 11: Resource temporarily unavailable
Additional information: 8
Additional information: 16182
ORA-00210: cannot open the specified control file
ORA-00202: control file: '/data/app/oracle/oradata/orcl/control01.ctl'
ORA-27086: unable to lock file - already in use
Linux-x86_64 Error: 11: Resource temporarily unavailable
Additional information: 8
Additional information: 16182
ORA-205 signalled during: ALTER DATABASE   MOUNT...
Thu May 16 22:36:54 2019
Checker run found 2 new persistent data failures
```

#### [解决方案一](https://blog.51cto.com/wuyelan/1557897)

#### [解决方案二](https://www.cnblogs.com/snake-hand/archive/2011/07/06/2452278.html)



## 设置Oracle开机启动

* 修改`/data/app/oracle/product/11.2.0/bin/dbstart`

```bash
ORACLE_HOME_LISTNER=$ORACLE_HOME
```

* 修改`/data/app/oracle/product/11.2.0/bin/dbshut`

```bash
ORACLE_HOME_LISTNER=$ORACLE_HOME
```

* 修改`vi /etc/oratab`

```bash
orcl:/data/app/oracle/product/11.2.0:Y
```

* 新建文件`/etc/rc.d/init.d/oracle`(管理员用户创建)

```bash
#! /bin/bash
# oracle: Start/Stop Oracle Database 11g R2
#
# chkconfig: 345 90 10
# description: The Oracle Database is an Object-Relational Database Management System.
#
# processname: oracle
. /etc/rc.d/init.d/functions
LOCKFILE=/var/lock/subsys/oracle
ORACLE_HOME=/data/app/oracle/product/11.2.0
ORACLE_USER=oracle
case "$1" in
'start')
   if [ -f $LOCKFILE ]; then
      echo $0 already running.
      exit 1
   fi
   echo -n $"Starting Oracle Database:"
   su - $ORACLE_USER -c "$ORACLE_HOME/bin/lsnrctl start"
   su - $ORACLE_USER -c "$ORACLE_HOME/bin/dbstart $ORACLE_HOME"
   su - $ORACLE_USER -c "$ORACLE_HOME/bin/emctl start dbconsole"
   touch $LOCKFILE
   ;;
'stop')
   if [ ! -f $LOCKFILE ]; then
      echo $0 already stopping.
      exit 1
   fi
   echo -n $"Stopping Oracle Database:"
   su - $ORACLE_USER -c "$ORACLE_HOME/bin/lsnrctl stop"
   su - $ORACLE_USER -c "$ORACLE_HOME/bin/dbshut"
   su - $ORACLE_USER -c "$ORACLE_HOME/bin/emctl stop dbconsole"
   rm -f $LOCKFILE
   ;;
'restart')
   $0 stop
   $0 start
   ;;
'status')
   if [ -f $LOCKFILE ]; then
      echo $0 started.
      else
      echo $0 stopped.
   fi
   ;;
*)
   echo "Usage: $0 [start|stop|status]"
   exit 1
esac
exit 0
```

* 给`/etc/init.d/oracle`添加执行权限

```bash
chmod +x /etc/init.d/oracle
```

* 开机启动`oracle`

```bash
systemctl enable oracle
或
chkconfig oracle on
```

* 给启动文件加权限

```bash
cd /data/app/oracle/product/11.2.0/bin/
chmod 6751 oracle
cd /var/tmp
chown -R oracle:oinstall .oracle
```



### [自启动](https://blog.csdn.net/qq_34618853/article/details/70990970)



# [错误参考](https://blog.csdn.net/lzwgood/article/details/26368323)

### 终于折腾完了




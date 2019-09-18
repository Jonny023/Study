# Windows docker

* [参考地址](<https://blog.csdn.net/Edison_shu/article/details/76982397>)

  ### [参考地址](<http://www.thxopen.com/linux/docker/2019/04/17/install-oracle11g-on-docker>)（重要步骤）



> 需要启动`Docker Quickstart Terminal`、`Docker Desktop`，下载`boot2docker.iso`镜像文件放到`C:\Users\Administrator\.docker\machine\cache`下

* 查找指定镜像文件

> `docker search oracle`

```bash
$ docker search oracle
NAME                                  DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
oraclelinux                           Official Docker builds of Oracle Linux.         605                 [OK]
jaspeen/oracle-11g                    Docker image for Oracle 11g database            122                                     [OK]
oracle/openjdk                        Docker images containing OpenJDK Oracle Linux   56                                      [OK]
oracleinanutshell/oracle-xe-11g                                                       54
absolutapps/oracle-12c-ee             Oracle 12c EE image with web management cons…   30
araczkowski/oracle-apex-ords          Oracle Express Edition 11g Release 2 on Ubun…   24                                      [OK]
oracle/nosql                          Oracle NoSQL on a Docker Image with Oracle L…   22                                      [OK]
bofm/oracle12c                        Docker image for Oracle Database                20                                      [OK]
relateiq/oracle-java8                                                                 13                                      [OK]
datagrip/oracle                       Oracle 11.2 & 12.1.0.2-se2 & 11.2.0.2-xe        12                                      [OK]
openweb/oracle-tomcat                 A fork off of Official tomcat image with Ora…   8                                       [OK]
oracle/weblogic-kubernetes-operator   Docker images containing the Oracle WebLogic…   8
softwareplant/oracle                  oracle db                                       2                                       [OK]
yannig/oracledb_exporter              Oracle DB exporter for Prometheus               2
18fgsa/oracle-client                  Hosted version of the Oracle Container Image…   1
publicisworldwide/oracle-core         This is the core image based on Oracle Linux…   1                                       [OK]
paulosalgado/oracle-java8-ubuntu-16   Oracle Java 8 on Ubuntu 16.04 LTS.              1                                       [OK]
iamseth/oracledb_exporter             A Prometheus exporter for Oracle modeled aft…   1
bitnami/oraclelinux-runtimes          Oracle Linux runtime-optimized images           0                                       [OK]
bitnami/oraclelinux-extras            Oracle Linux base images                        0                                       [OK]
toolsmiths/oraclelinux7.5-dev                                                         0
toolsmiths/oracle7-test                                                               0
pivotaldata/oracle7-test              Oracle Enterprise Linux (OEL) image for GPDB…   0
roboxes/oracle7                       A generic Oracle Linux 7 base image.            0
amd64/oraclelinux                     Official Docker builds of Oracle Linux.         0
```

* 登录用户,用户名登录`https://www.docker.com/`官网查看

```bash
 winpty docker login
```

* 下载需要安装的镜像，下载oracle，这个只是安装命令(安装脚本)，具体的安装包需要自己下载

```bash
docker pull jaspeen/oracle-11g
```

* 下载安装包[oracle11g](<https://www.oracle.com/database/technologies/112010-linx8664soft.html>), 分别下载 `linux.x64_11gR2_database_1of2.zip` 和 `linux.x64_11gR2_database_2of2.zip`两个压缩包，下载完成后解压到D盘 (如下目录结构)

```bash
D:.
└─oracleinstall
    └─database
        ├─doc
        ├─install
        ├─response
        ├─rpm
        ├─sshsetup
        ├─stage
        ├─runInstaller
        └─welcome.html
```

* #### 启动镜像（执行安装oracle）

* 解释
  * docker run 启动容器的命令
  * privileged 给这个容器特权，安装oracle可能需要操作需要root权限的文件或目录
  * name 给这个容器名一个名字
  * p 映射端口
  * v 挂在文件到容器指定目录 (`d:/oracleinstall/database` 对应容器 `/install/database`)
  * jaspeen/oracle-11g 代表启动指定的容器
* 执行安装, 时间很长，不要关闭窗口,

```bash
docker run --privileged --name oracle11g -p 1521:1521 -v d:/oracleinstall:/install jaspeen/oracle-11g
```

> 完成之后如下：

```bash
96% complete
100% complete
Look at the log file "/opt/oracle/app/cfgtoollogs/dbca/orcl/orcl.log" for further details.
Database created.
2019-09-18 08:38:17
Changind dpdump dir to /opt/oracle/dpdump
```

* 查看运行状态

```bash
docker ps -a
docker ps

C:\Users\Administrator>docker ps
CONTAINER ID        IMAGE                COMMAND                  CREATED             STATUS              PORTS                              NAMES
90468cd4890b        jaspeen/oracle-11g   "/assets/entrypoint.…"   45 minutes ago      Up 45 minutes       0.0.0.0:1521->1521/tcp, 8080/tcp   oracle11g
```

* ### 连接到容器

```bash
docker exec -it oracle11g /bin/bash
```

* ### 切换到oracle用户，然后连接到sql控制台

```bash
[root@90468cd4890b /]# su - oracle
Last login: Wed Sep 18 08:31:53 UTC 2019
[oracle@90468cd4890b ~]$ sqlplus / as sysdba

SQL*Plus: Release 11.2.0.1.0 Production on Wed Sep 18 09:01:02 2019

Copyright (c) 1982, 2009, Oracle.  All rights reserved.
```

* 激活`scott`用户，设置密码

```bash
SQL> alter user scott account unlock;

User altered.

SQL> commit;

Commit complete.

SQL> conn scott/tiger
ERROR:
ORA-28001: the password has expired


Changing password for scott
New password:
Retype new password:
Password changed
Connected.
SQL>
```

* 查看日志

```bash
docker logs instance
```

### 报错提示

* `failed to register layer: re-exec error: exit status 1: output: ProcessBaseLayer \`

> 解决方法：点击桌面右下角图条，右键`switch to Linux containers`

## Postgres-XL集群环境搭建



## 基础环境准备

| 名称 | 服务器          | 角色                               |
| ---- | --------------- | ---------------------------------- |
| gtm  | 192.168.137.101 | gtm                                |
| dn1  | 192.168.137.102 | coordinator & datanode & gtm_proxy |
| dn2  | 192.168.137.103 | coordinator & datanode & gtm_proxy |

所有主机添加`hosts`解析配置

```shell
# vi /etc/hosts
192.168.137.101 gtm
192.168.137.102 dn1
192.168.137.103 dn2
```

使`hosts`生效

```shell
/etc/init.d/network restart
```

以下操作，对每个服务器节点都适用。 关闭防火墙：

```shell
[root@localhost ~]# systemctl stop firewalld.service
[root@localhost ~]# systemctl disable firewalld.service
```

`selinux`设置:

```shell
[root@localhost ~]#vim /etc/selinux/config
```

设置`SELINUX=disabled`，保存退出。

```shell
# This file controls the state of SELinux on the system.
# SELINUX= can take one of these three values:
#     enforcing - SELinux security policy is enforced.
#     permissive - SELinux prints warnings instead of enforcing.
#     disabled - No SELinux policy is loaded.
SELINUX=disabled
# SELINUXTYPE= can take one of three two values:
#     targeted - Targeted processes are protected,
#     minimum - Modification of targeted policy. Only selected processes are protected.
#     mls - Multi Level Security protection.
```

重启

```bash
reboot
```

每个节点都建立用户`postgres`，并且建立`.ssh`目录，并配置相应的权限：

```bash
useradd postgres
passwd postgres

# 为了测试用户postgres的密码设置为：88888888
```

在`gtm`主机上创建`ssh`免密登陆

```shell
#在gtm节点创建key
su postgres
ssh-keygen -t rsa
chmod 700 .ssh
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys


# 600 代表只有当前用户具有读、写权限，这也是安全方面的考虑。
# 权限设置为600,否则无法登陆成功
chmod 600 authorized_keys

# 在node1上创建.ssh目录并赋值权限
su postgres
cd ~
chmod 600 .ssh

# 分别在dn1和dn2节点上创建.ssh目录
mkdir ~/.ssh
chmod 700 ~/.ssh

# 回到gtm节点，复制key到其它节点
scp ~/.ssh/authorized_keys postgres@dn1:~/.ssh/
scp ~/.ssh/authorized_keys postgres@dn2:~/.ssh/

# 在gtm节点分别测试ssh连接dn1和dn2，若不需要免密则证明配置成功
# 或者ssh postgres@dn1
[postgres@gtm ~]$ ssh dn1
Last login: Wed Mar 17 18:19:33 2021 from gtm
[postgres@dn1 ~]$ exit
登出
Connection to dn1 closed.
[postgres@gtm ~]$ ssh dn2
Last login: Wed Mar 17 18:19:34 2021 from gtm
[postgres@dn2 ~]$

```

每个节点都安装所需依赖包(`root权限才能安装`)：

```shell
yum install -y flex bison readline-devel zlib-devel openjade docbook-style-dsssl gcc bzip2
```

## 安装`postgres-xl`和`pgxc_ctl`（`每个节点都需要安装`）：

```shell
# 下载的压缩文件存放到/usr/local/tar.gz
cd /usr/local
mkdir tar.gz && cd tar.gz


#下载解压(也可以手动下载上传到/usr/local/tar.gz目录下)
wget https://www.postgres-xl.org/downloads/postgres-xl-10alpha2.tar.bz2

# 解压
tar -jxvf /usr/local/tar.gz/postgres-xl-10alpha2.tar.bz2 -C /usr/local
# 修改目录所属用户和组（授权）
chown postgres:postgres /usr/local/postgres-xl-10alpha2

# 若无法解压需安装解压工具
yum -y install bzip2

# 编译并安装
cd /usr/local/postgres-xl-10alpha2/
./configure --prefix=/home/postgres/pgxl/
make && make install
cd contrib/	
make && make install

# 如果是管理员安装的则需要修改权限为postgres
cd /home/postgres
chown -R postgres:postgres pgxl
```

编辑环境变量

```shell
# vim ~/.bashrc
export PGHOME=/home/postgres/pgxl
export PGUSER=postgres
export LD_LIBRARY_PATH=$PGHOME/lib:$LD_LIBRARY_PATH
export PATH=$PGHOME/bin:$PATH
```

使配置生效：

```shell
source ~/.bashrc
```

测试

```shell
# 运行
pgxc_ctl

# 打印变量
echo ${PGHOME}
echo $PGHOME
```



## 集群配置

架构图

![](C:\Users\e-lijing6\Desktop\share\Postgres-XL集群\img\jg.png)

在`gtm`节点，运行`pgxc_ctl`生成配置文件：

```shell
[postgres@gtm ~]$ pgxc_ctl
PGXC$  prepare config empty #生成一个空的配置文件/home/postgres/pgxc_ctl/pgxc_ctl.conf
PGXC$  exit
```

修改配置文件`$HOME/pgxc_ctl/pgxc_ctl.conf`

```shell
export dataDirRoot=$HOME/DATA/pgxl/nodes
```

```shell
# 编辑配置
# vim ~/pgxc_ctl/pgxc_ctl.conf
dataDirRoot=$HOME/DATA/pgxl/nodes
tmpDir=$HOME/DATA/tmp                                   
localTmpDir=$tmpDir                     # temporary dir used here locally

pgxcOwner=postgres
coordPgHbaEntries=(192.168.137.0/24)
datanodePgHbaEntries=(192.168.137.0/24)
```

停止多有节点

```shell
pgxc_ctl -c /home/postgres/pgxc_ctl/pgxc_ctl.conf stop all
```

**配置`gtm`节点**

```
pgxc_ctl
add gtm master gtm gtm 20001 $dataDirRoot/gtm
monitor all
```

**配置`coordinator`（协调员）节点**

```shell
add coordinator master coord1 dn1 30001 30011 $dataDirRoot/coord_master.1 none none

add coordinator master coord2 dn2 30002 30012 $dataDirRoot/coord_master.2 none none


# 交叉备份
add coordinator slave coord2 dn2 30001 30011 $dataDirRoot/coord_slave $dataDirRoot/pgxl/coord_archlog

add coordinator slave coord1 dn1 30002 30012 $dataDirRoot/coord_slave $dataDirRoot/pgxl/coord_archlog

monitor all
```

**配置`datanode`（子节点）**

```shell
add datanode master datanode1 dn1 40001 40011 $dataDirRoot/dn_master.1 none none none

add datanode master datanode2 dn2 40002 40012 $dataDirRoot/dn_master.2 none none none

add datanode slave datanode1 dn2 40001 40011 $dataDirRoot/pgxl/dn_slave none $dataDirRoot/pgxl/dn_archlog

add datanode slave datanode2 dn1 40002 40012 $dataDirRoot/pgxl/dn_slave none $dataDirRoot/pgxl/dn_archlog

monitor all
```

**配置`gtm_proxy`**

```shell
add gtm_proxy gtm_proxy1 dn1 6668 ~/pgxl/gtm_proxy
add gtm_proxy gtm_proxy2 dn2 6668 ~/pgxl/gtm_proxy
monitor all

PGXC monitor all
Running: gtm master
Running: gtm proxy gtm_proxy1
Running: gtm proxy gtm_proxy2
Running: coordinator master coord1
Running: coordinator master coord2
Running: datanode master dn1
Running: datanode master dn2

```

备份配置到子节点

```shell
scp /home/postgres/pgxc_ctl/pgxc_ctl.conf postgres@dn1:/home/postgres/pgxc_ctl/pgxc_ctl.conf
scp /home/postgres/pgxc_ctl/pgxc_ctl.conf postgres@dn2:/home/postgres/pgxc_ctl/pgxc_ctl.conf
```

初始化所有配置：

```shell
#初始化所有节点配置
[postgres@gtm ~]$ pgxc_ctl -c /home/postgres/pgxc_ctl/pgxc_ctl.conf init all
```



**启动、停止节点**

```shell
# 停止
stop gtm master
stop  coordinator master cr1
stop coordinator master cr2
stop datanode master dn1
stop datanode master dn2
 
# 启动
start gtm master
start  coordinator master cr1
start coordinator master cr2
start datanode master dn1
start datanode master dn2
```



**启动、关闭集群**

```shell
pgxc_ctl -c /home/postgres/pgxc_ctl/pgxc_ctl.conf start all

pgxc_ctl -c /home/postgres/pgxc_ctl/pgxc_ctl.conf stop all
```

查看运行状态： 

```shell
# 在dn1上连接coord1(coordinator)节点
[postgres@dn1 DATA]$ psql -p 30001
psql (PGXL 10alpha2, based on PG 10beta3 (Postgres-XL 10alpha2))
Type "help" for help.

postgres=# select * from pgxc_node;
 node_name | node_type | node_port | node_host | nodeis_primary | nodeis_preferred |   node_id
-----------+-----------+-----------+-----------+----------------+------------------+-------------
 coord2    | C         |     30002 | dn2       | f              | f                | -1197102633
 datanode1 | D         |     40001 | dn1       | f              | f                |   888802358
 datanode2 | D         |     40002 | dn2       | f              | f                |  -905831925
 coord1    | C         |     30001 | dn1       | f              | f                |  1885696643
(4 rows)

```

## 测试

```shell
# 在`dn1`节点上操作
# coordinator - 30001（读写） datanode - 40001（只读）
psql -p 30001

# 创建一个测试库
 create database test;
 
# 查看数据库
\l

# 连接datanode（40001）节点
psql -p 40001

[postgres@dn1 DATA]$ psql -p 40001
psql (PGXL 10alpha2, based on PG 10beta3 (Postgres-XL 10alpha2))
Type "help" for help.

# 查看数据库
postgres=# \l
                                  List of databases
   Name    |  Owner   | Encoding |   Collate   |    Ctype    |   Access privileges
-----------+----------+----------+-------------+-------------+-----------------------
 postgres  | postgres | UTF8     | zh_CN.UTF-8 | zh_CN.UTF-8 |
 template0 | postgres | UTF8     | zh_CN.UTF-8 | zh_CN.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
 template1 | postgres | UTF8     | zh_CN.UTF-8 | zh_CN.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
 test      | postgres | UTF8     | zh_CN.UTF-8 | zh_CN.UTF-8 |
(4 rows)

postgres=#


# 连接dn2节点，查看数据库同步情况
psql -p 30002

\l
```



也可以在`GTM`上进行操作

```shell
# psql -h host -p port dbname
# 连接到dn1节点的postgres库,默认为postgres库
psql -h dn1 -p 30001 postgres

# 查看数据库
\l或者\l+
select * from pg_database;

# 切换数据库
\c test;

# 查看表
\dt或者
select * from pg_tables where schemaname='public';

#查看表结构
\d table;
```

添加节点`dn3`

```bash
export dataDirRoot=$HOME/DATA/pgxl/nodes
add coordinator master coord3 dn3 50002 50012 $dataDirRoot/coord_master.3 none none

add datanode master datanode3 dn3 60002 60012 $dataDirRoot/dn_master.3 none none none

add gtm_proxy gtm_proxy3 dn3 6668 ~/pgxl/gtm_proxy
monitor all

pgxc_ctl -c /home/postgres/pgxc_ctl/pgxc_ctl.conf start all

pgxc_ctl -c /home/postgres/pgxc_ctl/pgxc_ctl.conf stop all
```



### kill

```shell
kill all
kill nodename ...
kill gtm [ master | slave | all ]
kill gtm_proxy [ all | nodename ... ]
kill coordinator nodename ...
kill coordinator [ master | slave ] [ all | nodename ... ]
kill datanode nodename ...
kill datanode [ master | slave ] [ all | nodename ... ]
```

### 删除节点

```shell
remove gtm master [ clean ]
remove gtm slave [ clean ]
remove gtm_proxy nodename [ clean ]
remove coordinator [ master| slave ] nodename [ clean ]
remove datanode [ master| slave ] nodename [ clean ]
```



# 问题总结

* 添加、删除节点必须在`pgxc_ctl`运行状态下进行
* `Some of the coordinator masters are not running. Cannot add one.`
  * 
## docker安装oracle

* [参考1](https://blog.csdn.net/chy555chy/article/details/124345973)

* [参考2](https://zhuanlan.zhihu.com/p/443324194)

```sh
docker pull registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g

#默认启动
docker run -d -it -p 1521:1521 --name oracle11g --restart=always registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g

#持久化启动
docker run -d -it -p 1521:1521 --name oracle11g -e TZ=Asia/Shanghai --restart=always --mount source=oracle_vol,target=/home/oracle/app/oracle/oradata registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g

#查看启动的线程
netstat -tulnp

#查看容器运行状态
docker ps

#进入容器
docker exec -it oracle11g bash

#切换到 root 用户 su root，密码为 helowin

#docker容器配置环境变量不是在 /etc/profile 中，容器启动不会走这个文件。
#可以将环境变量的配置设置在 /home/oracle/.bashrc 文件下，这样可以省略掉软连接的创建 ln -s $ORACLE_HOME/bin/sqlplus /usr/bin

# 编辑环境变量
vi /home/oracle/.bashrc

export ORACLE_HOME=/home/oracle/app/oracle/product/11.2.0/dbhome_2
export ORACLE_SID=helowin
export PATH=$ORACLE_HOME/bin:$PATH


#刷新配置使环境变量生效
source /home/oracle/.bashrc

#进入oracle命令行
sqlplus /nolog

#登录oracle
#如果直接使用默认的 root 用户登录，会报登录失败。这里必须使用 su - oracle 命令，将当前用户切换到 oracle，然后在执行登录命令
conn / as sysdba

#报错ORA-12514, TNS:listener does not currently know of service requested in connect descriptor
su - oracle
sqlplus /nolog
conn / as sysdba
select instance_name from v$instance;
show user;

#阿里的这个镜像，用户名：system,实例：helowin，所有的密码都是统一的helowin
#system用户具有DBA权限，但是没有SYSDBA权限。平常一般用该帐号管理数据库。
#而sys用户是Oracle数据库中权限最高的帐号，具有“SYSDBA”和“SYSOPER”权限，一般不允许从外部登录



#防火墙
# 打开防火墙
systemctl start firewalld
# 查询端口状态
firewall-cmd --query-port=1521/tcp
# 永久性开放端口
firewall-cmd --permanent --zone=public --add-port=1521/tcp
# 重启防火墙
firewall-cmd --reload
firewall-cmd --query-port=1521/tcp


# 连接无权限
#ORA-01017: invalid username/password; logon denied
# 用户名：system,实例：helowin，密码: helowin


##密码默认为7天过期，设置为不限制##
ALTER PROFILE DEFAULT LIMIT PASSWORD_LIFE_TIME UNLIMITED;
#修改后需要重启数据库
conn /as sysdba
#关闭数据库
shutdown immediate;
#启动数据库
startup;
#退出软链接
exit;
```



## 创建表空间（模式）

> 一般默认为orcl数据库，我们这里是`helowin`，授权后在模式里面可以看见

```sh
# 创建目录并授权
mkdir -p /home/oracle/app/oracle/oradata/test
chmod -R 777 /home/oracle/app/oracle/oradata/test

create tablespace test datafile '/home/oracle/app/oracle/oradata/test/test.dbf' size 200m autoextend on next 5m maxsize 300m;
# test 是tablespace的空间名字
# '/home/oracle/app/oracle/oradata/test/test.dbf' 是刚刚创建的文件夹下面需要生成一个dbf文件，必须是没存在的才能创建成功
# size 200m 初始化大小为200m
# autoextend on next 5m 空间不够了自动增长，每次增长5m
# maxsize 300m 最大增长到300m



#【指定表空间给已存在的用户】
grant connect,resource to system;
grant dba to system;

#赋予用户的表空间权限【经测试无效】
alter user username default tablespace username;
ALTER USER system quota unlimited on test; 
GRANT UNLIMITED TABLESPACE TO system ; 




create user test identified by test123 default tablespace test;
# test 为用户名 ，test123 为密码
# default tablespace test 指定用户test的默认表空间

grant connect,resource to test;
# 权限分为以下两种
# 系统权限：系统规定用户使用数据库的权限。(系统权限是对用户而言)。 
# 实体权限：某种权限用户对其它用户的表或视图的存取权限。(是针对表或视图而言的)。
# connect和resource是两个系统内置的角色，和dba是并列的关系，都是系统权限
# DBA：拥有全部特权，是系统最高权限，只有DBA才可以创建数据库结构。
# RESOURCE:拥有Resource权限的用户只可以创建实体，不可以创建数据库结构。
# CONNECT：拥有Connect权限的用户只可以登录Oracle，不可以创建实体，不可以创建数据库结构。

# 删除用户
drop user test cascade;

#切换到test用户
conn test

#查看当前实例所有表
select table_name from user_tables;


#创建一个用户，并指定表空间和临时表空间test
create user system identified by system default tablespace test temporary tablespace testtemp;

#连接数据库的权限
grant create session to system;

#创建表的权限
grant create table to system;

#使用表空间的权限
grant unlimited tablespace to system;

#查看表空间
select * from v$tablespace;
select tablespace_name from dba_tablespaces;

#给表空间重命名
#语法： alter tablespace 旧名称 rename to 新名称;
alter tablespace newspace rename to myspace;

#删除表空间[including contents cascade constraints]并把包含的数据和约束删除
#语法： drop tablespace 表空间名 [including contents cascade constraints];
drop tablespace myspace including contents cascade constraints;
drop tablespace test including contents and datafiles cascade constraints;



#撤销授权 test为用户名
revoke connect,resource from test;


#======角色=====
#添加角色
create role testRole;
#授权角色
grant select on class to 角色名;
#删除角色
drop role testRole;


#可能需要重启服务
#提交
commit;
#关闭数据库
shutdown immediate;
#启动数据库
startup;
#退出软链接
exit;
```



### 权限

```sh
grant connect,resource to system;  
grant create any sequence to system;  
grant create any table to system;  
grant delete any table to system;  
grant insert any table to system;  
grant select any table to system;  
grant unlimited tablespace to system;  
grant execute any procedure to system;  
grant update any table to system;  
grant create any view to system;  

# 赋予dba权限
grant dba to study;

#指定表空间给已存在的用户
ALTER USER study DEFAULT TABLESPACE study;

ALTER USER system DEFAULT TABLESPACE test;
grant create session,create table,unlimited tablespace to system;
grant create view,create trigger,create sequence,create procedure to system;
grant connect,resource,dba to system;
```



## 登录限制

```sql
#修改登录为不限制
alter profile default limit password_life_time unlimited;
alter profile default limit FAILED_LOGIN_ATTEMPTS unlimited;

# 需要再次设置密码无限制登录才会生效
alter user system identified by helowin;



#查看结果
select * from dba_profiles;
```



### 表空间

[参考](https://blog.51cto.com/gblfy/5658620)

```sql
# 查看表空间存放路径
select file_name from dba_data_files where tablespace_name='TEST';

#查看表空间状态
select status from dba_tablespaces where tablespace_name='TEST';

#修改为脱机状态
alter tablespace test offline;
alter tablespace test online;

#只读
alter tablespace test read only;

#可读写
alter tablespace test read write;

#删除表空间以及表空间下面的数据文件
drop tablespace 表空间名称 including contents;

#删除表空间为test以及表空间下面的数据文件
drop tablespace test including contents;
```

### 导入导出

```sh
imp usr_4/usr_4@localhost:1521/ORCL file=D:/1.dmp full=y [fromuser=usr_4 touser=usr_4]--导入
exp usr_4/usr_4@localhost:1521/ORCL file=D:/1.dmp [owner=usr_4] [rows=n表示不导出数据]--导出
```

```sh
#1、使用SQL*Plus导入导出数据：
#（1）导出数据：
exp username/password@database file=data.dmp

exp system/helowin@helowin file=data.dmp

#（2）导入数据：
imp username/password@database file=data.dmp


#使用Oracle Data Pump导入导出数据：
#（1）导出数据：
expdp username/password@database directory=dump_dir dumpfile=data.dmp

#（2）导入数据：
impdp username/password@database directory=dump_dir dumpfile=data.dmp
```

### 远程连接

```sql
sqlplus /nolog
conn user/passwd@ip:1521/instance_name as sysdba
```



### 查看数据库

```sql
#查看数据库名
SELECT NAME FROM V$DATABASE;


SELECT t.tablespace_name, round(SUM(bytes / (1024 * 1024)), 0) ts_size
  FROM dba_tablespaces t, dba_data_files d
 WHERE t.tablespace_name = d.tablespace_name
 GROUP BY t.tablespace_name;
```


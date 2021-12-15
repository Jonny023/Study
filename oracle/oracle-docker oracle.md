# docker oracle

[参考地址](https://segmentfault.com/a/1190000012053469)

## 1.搜索镜像

```
docker search oracle
```

## 2.拉取

```
docker pull registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g
```

## 3.运行

```
docker run --name oracle_11 -d -p 1522:1522 -v /opt/data/oracle:/home/oracle/app/oracle/oradata/ -e ORACLE_ALLOW_REMOTE=true registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g

docker run --name oracle_11 --privileged=true -itd -p 1522:1522 -v /opt/data/oracle:/home/oracle/app/oracle/oradata/ -e ORACLE_ALLOW_REMOTE=true registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g init
```

## 4.配置环境变量

> 修改配置，`vim ~/.bash_profile`，没有换行，修改完成后，使配置生效

```shell
source ~/.bash_profile

# 查看环境变量
env
```

## 5.切换oracle用户

> su - oracle 切换到oracle用户下执行数据库相关用户的修改与配置信息的修改, 密码为oracle

```shell
# 切换oracle用户
su - oracle

# 启动监听
lsnrctl start

# 连接oracle，密码oracle
sqlplus / as sysdba

# 启动实例
startup

# 查看版本
select * from v$version;

# 外部连接容器内部
# 宿主端口1522，容器内部1521 系统管理员密码oracle
sqlplus sys/oracle@192.168.1.2:1522/db as sysdba

# 关闭数据库
shutdown immediate;

startup nomount;

sqlplus /nolog
conn /as sysdba

# 修改system用户账号；
alter user system identified by system;

# 修改sys用户账号；
alter user sys identified by system;

# 创建内部管理员账号
create user yan_test identified by yan_test;

# 将dba权限授权给内部管理员账号
grant connect,resource,dba to yan_test;

# 修改密码规则策略为密码永不过期
ALTER PROFILE DEFAULT LIMIT PASSWORD_LIFE_TIME UNLIMITED;

# 修改数据库最大连接数据
alter system set processes=1000 scope=spfile;
```

## 6.报错

> ORA-01507: database not mounted

```shell
# 关闭数据库
shutdown

# 退出
exit

# 切换oracle用户
su - oracle

# 进入oracle根目录下的dbs下
oracle_home=`echo "$ORACLE_HOME/dbs"`
cd $oracle_home

# 检查lk开头的文件
fuser -u lkORCL

# kill掉
fuser -k lkORCL
```

## 7.重启数据库

```shell
sqlplus / as sysdba

startup

# 查看实例
select INSTANCE_NAME,STATUS from v$instance;
show parameter service_name;
```

## 8.用户授权

```sql
sqlplus system/oracle

-- 查看用户
select username,password from dba_users;

-- 创建用户
create user deque identified by 123;

-- 给新用户授权
grant connect,resource to deque; 
```



```shell
## 查看对应端口号状态
lsof -i:1521
## 进入容器 查看orcale数据库信息 [name]对应docker容器名称
docker exec -it [name] bash

## 进入sqlplus
sqlplus /nolog

## 使用sysdba角色登录sqlplus
conn sys/oracle as sysdba;
conn system/oracle as sysdba;

# 查看用户库
select instance_name from v$instance;

## 查询当前实例下的数据库
select name from v$database;

## 退出
SQL > exit;	# 退出sqlplus
root@20bd6a9226b3:/ exit; # 退出容器
```


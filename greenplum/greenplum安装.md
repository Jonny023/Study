# greenplum安装(pivotaldata/gpdb-devel)

[参考1](https://my.oschina.net/xiaozhublog/blog/2249525)

[参考2【重点】]([https://www.xswsym.online/pages/cf1834/#_9-%E9%87%8D%E5%90%AF%E5%AE%B9%E5%99%A8](https://www.xswsym.online/pages/cf1834/#_9-重启容器))

> 镜像大小上G

## 1.拉取镜像

```shell
docker pull pivotaldata/gpdb-devel
```

## 2.运行

```shell
mkdir -p /opt/greenplum/gpdb

# 临时运行一个容器
docker run -itd -p 15432:15432 --name gpdb1 \
-e TZ=Asia/Shanghai \
pivotaldata/gpdb-devel

# 进入容器
docker exec -it gpdb1 bash

# 切换用户
su gpadmin

# 关闭数据库
gpstop

# 关闭gpdb容器
docker stop gpdb1

# 拷贝配置
docker cp gpdb1:/workspace/gpdb/ /opt/greenplum/

# 删除镜像
docker rm -f gpdb1

# 修改目录权限
chmod 777 -R /opt/greenplum/*

# 持久化配置
docker run -itd -p 15432:15432 --name gpdb1 \
-e TZ=Asia/Shanghai \
--privileged \
-v /opt/greenplum:/workspace \
pivotaldata/gpdb-devel
```

## 3.进入容器

```shell
docker exec -it gpdb1 bash
```

## 4.切换用户

> 首次切换用户，自动启动gp数据库

```shell
su gpadmin
```

## 5.创建用户

> 创建一个数据库dba用户，密码设置为123456，输入两次密码

```
createuser -P dba
```

## 6.创建数据库

```shell
# 创建数据库: demo
createdb demo

# 连接数据库
psql demo

# 查看帮助
help
```

## 7.添加用户

```sql
CREATE USER test WITH PASSWORD '123456' NOSUPERUSER;
```

## 8.添加到用户组

```sql
-- 创建角色
CREATE ROLE test_role;

-- 用户绑定角色
GRANT test_role TO dba,test;

-- 查看用户角色信息
\du

-- 查看db版本
select * from version();

-- 取消超管
alter role dba superuser;
```

## 9.启用远程访问

> 进入容器内部，切换至用户：pgadmin

```shell
cd /workspace/gpdb/gpAux/gpdemo/datadirs/qddir/demoDataDir-1
vim pg_hba.conf
# 添加网段
host all all 0.0.0.0/0 trust
```

## 9.重启容器

```shell
# 容器内部重启
gpstop -r
gpstart

docker restart gpdb1
docker exec -it gpdb0 bash
su gpadmin
```

## 10.连接测试

```shell
psql -h localhost -U dba -d demo
```

## 11.修改密码

```sql
alter user postgres password '123456';
```

## 错误

> 通过gpstart启动gp数据库时报错，在容器内启用sshd服务：`service sshd restart`

### 1.gpstart错误

```shell
FAILED  host:'912720d52786' datadir:'/workspace/gpdb/gpAux/gpdemo/datadirs/dbfast1/demoDataDir0' with reason:'cmd had rc=255 completed=True halted=False
  stdout=''
  stderr='ssh: connect to host 912720d52786 port 22: Connection refused
''
```

### 2.找不到postmaster.pid

```shell
20220107:17:31:17:001066 gpstart:ee2880e03893:gpadmin-[CRITICAL]:-Error occurred: non-zero rc: 1
 Command was: 'env GPSESSID=0000000000 GPERA=None $GPHOME/bin/pg_ctl -D /workspace/gpdb/gpAux/gpdemo/datadirs/qddir/demoDataDir-1 -l /workspace/gpdb/gpAux/gpdemo/datadirs/qddir/demoDataDir-1/pg_log/startup.log -w -t 600 -o " -p 15432 -b 1 -z 0 --silent-mode=true -i -M master -C -1 -x 0 -c gp_role=utility " start'
rc=1, stdout='waiting for server to start...... stopped waiting
', stderr='pg_ctl: PID file "/workspace/gpdb/gpAux/gpdemo/datadirs/qddir/demoDataDir-1/postmaster.pid" does not exist
pg_ctl: could not start server
```


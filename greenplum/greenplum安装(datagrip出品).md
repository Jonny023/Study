# greenplum安装【datagrop】

> 版本：PostgreSQL 8.2.15 (Greenplum Database 4.3.7.1 build 1) on x86_64-unknown-linux-gnu, compiled by GCC gcc (GCC) 4.4.2 compiled on Jan 21 2016 15:51:02

## 1.拉取镜像

```shell
docker pull datagrip/greenplum
```

## 2.运行

```shell
mkdir -p /usr/local/greenplum/log

# 临时运行一个容器
docker run -itd -p 5432:5432 --name gpdb0 \
-e TZ=Asia/Shanghai \
datagrip/greenplum

# 查看日志，待启动完成后考到Database successfully started再执行下面的
docker logs -f gpdb0

# 拷贝配置
docker cp gpdb0:/gpdata /usr/local/greenplum

# 查看是否存在postmaster.pid，如果不存在则无法启动
ll /usr/local/greenplum/gpdata/master/gpseg-1

# 查看启动日志
cat /usr/local/greenplum/gpdata/master/gpseg-1/pg_log/startup.log

# 删除镜像
docker rm -f gpdb0

# 修改目录权限【权限必须是这些，修改就了就gg】
chown 500 -R /usr/local/greenplum/{gpdata,log}
chmod 0700 -R /usr/local/greenplum/{gpdata,log}

# 持久化配置
docker run -itd -p 5432:5432 --name gpdb0 \
-e TZ=Asia/Shanghai \
--privileged=true \
-v /usr/local/greenplum/gpdata:/gpdata \
-v /usr/local/greenplum/log:/home/gpadmin/gpAdminLogs \
datagrip/greenplum
```

## 3.进入容器

```shell
docker exec -it gpdb0 bash
```

## 4.切换用户

```shell
su gpadmin

# 想要执行命令，运行一次
source ~/.bash_profile
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

## 7.用户

> 默认用户名: `gpadmin` 密码: `pivotal`

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
alter role dba nosuperuser;

-- 设置超管
alter role dba nosuperuser;

-- 退出
\q
```

## 9.远程访问

> 进入容器内部，切换至用户：pgadmin，默认是启动远程访问的，若虚修改在容器内部路径：`/gpdata/master/gpseg-1`，物理机就是：`/usr/local/greenplum/gpdata/master/gpseg-1`下的`pg_hba.conf`

```shell
cd /gpdata/master/gpseg-1
vi pg_hba.conf

# 添加网段
host all all 0.0.0.0/0 md5
```

## 9.重启容器

```shell
# 容器内部重启
gpstop -r
gpstart
gpstart -a

docker restart gpdb0
docker exec -it gpdb0 bash
su gpadmin
```

## 10.连接测试

```shell
psql -h localhost -U dba -d demo
```

## 11.修改密码

```sql
alter user test password '1234561';
```

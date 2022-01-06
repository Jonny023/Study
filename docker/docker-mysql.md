## Docker 安装mysql5.7

## 1.搜索镜像

```shell
# 搜索
docker search mysql
```

## 2.拉取

```shell
# 下载
docker pull mysql:5.7
```

## 3.运行

```shell
# 运行docker
docker run -itd --restart=always --name mysql57 -p 3306:3306 \
-e TZ=Asia/Shanghai \
--privileged=true \
-v /data/docker/volumes/mysql/conf:/etc/mysql \
-v /data/docker/volumes/mysql/data:/var/lib/mysql \
-v /data/docker/volumes/mysql/log:/var/log \
-e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```

## 4.binlog

> 如果数据被误删除，想要恢复，则必须开启binlog

### 开启binlog

* 编辑配置，`vim /data/docker/volumes/mysql/conf/my.cnf`，这个配置文件没有则创建，容器中的my.cnf不能编辑，他是个空文件，这里手动编辑即可。

```properties
[mysqld]
# binlog settings
log-bin=/var/lib/mysql/mysql-bin
server-id=mysql57
```

* **设置保留时间**

```shell
echo -e "# set binlog save days\nexpire_logs_days=30" >> /data/docker/volumes/mysql/conf/my.cnf
```

* 查看binlog

```shell
# 查看是否启用binlog
show variables like '%log_bin%';

# docker查看
docker exec mysql57 mysql -u root -p123456 -se "show variables like '%log_bin%'"

# 查询binlog保留天数，0为永久
show variables like 'expire_logs_days';

# 查看binlog日志内容
show binlog events in 'mysql-bin.000001';

# 借助mysqlbinlog工具查看binlog原始数据内容
mysqlbinlog --base64-output=DECODE-ROWS -v mysql-bin.000001
```

* 备份还原binlog

```shell
# 查找指定时间范围记录输出到sql文件中
mysqlbinlog --no-defaults -d demo --start-datetime='2022-01-06 16:36:00' --stop-datetime='2022-01-06 16:37:00' /var/lib/mysql/mysql-bin.000001 > bak20220106.sql

# 导入
source bak20220106.sql
```

## 5.其他

```shell
docker exec -it mysql57 /bin/bash
mysqldump -uroot -p123456 database > /usr/local/sql/xxx.sql


# 导入数据库
mysql -u root -p --default-character-set=utf8

use bi_server;
source /usr/local/sql/xxx.sql;

# 进入容器
docker exec -it containerID /bin/bash
docker exec -it mysql57 bash
# 或者
mysql -h localhost -u root -p

# 在外部dump出docker容器里面的数据
docker exec -it mysql57 mysqldump -uroot -p123456 databaseName > /usr/local/sql/xxx.sql
docker exec -it mysql57 mysqldump -uroot -p123456 shiro > /usr/local/sql/db.sql

# 登录mysql
docker exec -it mysql57 mysql -uroot -p123456

# 使用数据库
use mydatabase;

# 导入数据
source /usr/local/db/xxx.sql;

# 拷贝sql
docker cp xxx.sql mysql:/xxx.sql

# 导入sql
mysql -u username -p -D db < xxx.sql
docker exec -i mysql57 mysql --default-character-set=utf8 -u root -p123456 -D shiro < db.sql

# 查看日志
docker logs -f -t --tail 200 mysql57
```

# 时区

```shell
# 进入容器
docker exec -it mysql57 bash

# 查看容器系统时间
date -R

# 连接mysql查看时区
mysql -u root -p123456

# 查看时区
show variables like '%time_zone';

# 查看时间
select now();



#==============时区相差8小时==========================
# 方式1 启动参数添加:-e TZ=Asia/Shanghai
-e TZ=Asia/Shanghai


# 方式2 永久生效
cp /usr/share/zoneinfo/PRC /etc/localtime
# 或者【推荐】,软连接
ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 重启容器生效
docker restart mysql5.7
```

[时区问题](cnblogs.com/shisanye/p/13926175.html)

# 磁盘空间不足

```shell
# 查看docker工作目录
docker info|grep Dir

# 查看docker工作路径占用情况
du -h --max-depth=1 /data/docker
```


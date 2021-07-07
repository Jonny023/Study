# CentOS7安装MySQL5.7安装

[官网下载](https://downloads.mysql.com/archives/community/)

上传解压

```shell
tar xzf mysql-5.7.23-linux-glibc2.12-x86_64.tar.gz -C /root/mysql/
mv mysql-5.7.23-linux-glibc2.12-x86_64 mysql-5.7.23
mv mysql-5.7.23 /usr/local/mysql
cd /usr/local/mysql/
groupadd mysql
useradd -r -g mysql mysql
cat /etc/passwd
mkdir -p /data/mysql # 创建目录
chown mysql:mysql -R /data/mysql # 赋予权限
vim /etc/my.cnf # 设置相关 退出命令再次重生 先摁ESC 然后:wq
```

vim /etc/my.cnf

```shell
[client]
# 设置mysql客户端默认字符集
default-character-set=utf8
[mysqld]
wait_timeout=86400
interactive_timeout=86400
bind-address=0.0.0.0
# 设置3306端口
port=3306
# 设置mysql的安装目录
basedir=/usr/local/mysql-5.7
# 设置 mysql数据库的数据的存放目录，MySQL 8+ 不需要以下配置，系统自己生成即可，否则有可能报错
datadir=/data/mysql
# 允许最大连接数
max_connections=20
# 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
socket=/tmp/mysql.sock
log-error=/data/mysql/mysql.err
pid-file=/data/mysql/mysql.pid
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0
explicit_defaults_for_timestamp=true
```

```
./mysqld --defaults-file=/etc/my.cnf --basedir=/usr/local/mysql-5.7/ --datadir=/data/mysql/ --user=mysql --initialize
```

* cat /data/mysql/mysql.err # 查看随机密码

* cp /usr/local/mysql-5.7/support-files/mysql.server /etc/init.d/mysql # 先将mysql.server放置到/etc/init.d/mysql中

- service mysql start
- service mysql restart # 重启命令 /etc/init.d/mysql
- ps -ef|grep mysql
- ./mysql -u root -p
- SET PASSWORD = PASSWORD('root'); # 重置密码你写 没了?嗯
- ALTER USER 'root'@'localhost' PASSWORD EXPIRE NEVER;
- FLUSH PRIVILEGES; # 这个是刷新修改.
- use mysql; # 访问mysql库
- update user set host = '%' where user = 'root'; # 使root能再任何host访问
- FLUSH PRIVILEGES;# 刷新





# 软连接

ln -s /usr/local/mysql-5.7/bin/mysql /usr/bin

```shell
ln -s /usr/local/mysql-5.7/bin/mysql /usr/bin/mysql
```



# windows导入指定编码（防止乱码）

```shell
mysql -u root -p --default-character-set=utf8

mysqldump -uroot -p --default-character-set=utf8 mo（dbname） > /usr/local/sql/bak.sql

mysql -h 10.113.74.113 -u root -p --default-character-set=utf8 # 远程连接
```

### 数据库导出和恢复

```shell
# 导出表结构和数据
mysqldump -h 127.0.0.1 -u root -p666 db > /usr/local/sql/bak.sql
mysqldump -d -A --add-drop-table -uroot -p > bak.sql

# 仅导出表结构
mysqldump --opt -d db -u root -p > /usr/local/sql/bak.sql

# 导出特定表结构
mysqldump -uroot -p -B db --table table > bak.sql

# 导出数据
mysqldump -t db -uroot -p > bak.sql

# 恢复
mysql -uroot -p < /usr/local/sql/bak.sql

# 或者
mysql -u root -p 
use db;
source /usr/local/sql/bak.sql
```



# 查询超时时间

> 默认wait_timeout=28800,也就是8小时

```
show global variables like 'wait_timeout';
```


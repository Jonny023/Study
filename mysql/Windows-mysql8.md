## Windows安装mysql8

[下载地址](https://downloads.mysql.com/archives/community/)

### 环境变量

```sh
path D:\devtools\mysql-8.0.28-winx64\bin
```

### 配置

> 在D:\devtools\mysql-8.0.28-winx64根目录下创建my.ini配置，写入配置，并在根目录下创建data目录用于存放数据

```properties
[mysqld]
basedir=D:\devtools\mysql-8.0.28-winx64
datadir=D:\devtools\mysql-8.0.28-winx64\data 
port=3306
# 允许最大连接数
max_connections=200
# 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
```

### 安装

> 以管理员身份进入mysql/bin目录

```sh
# 安装服务
mysqld -install

# mysqld --initialize初始化数据库
mysqld --initialize

# 若出错可以卸载重新安装
mysqld remove
```



### 密码

> 以管理员身份运行

* 提示：The MySQL server is running with the --skip-grant-tables option so it cannot execute this statement

  ```sh
  # 先执行，再重启数据库
  flush privileges;
  ```

  

```sh
# 无密码启动服务
mysqld --console --skip-grant-tables --shared-memory

# 以普通用户运行连接服务【直接回车】
mysql -u root -p

use mysql;
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
flush privileges;

# 重启服务
net start mysql
```



### 远程访问

```sh
mysql -u root -p

use mysql;

select host,user,plugin from user;

update user set host='%' where user='root';

flush privileges;
```


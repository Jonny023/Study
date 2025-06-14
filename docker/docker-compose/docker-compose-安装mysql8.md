## docker-compose安装mysql


### mysql配置

* 创建配置

```sh
mkdir -p /usr/local/docker/mysql/{conf,data,logs}

chmod -R 777 /usr/local/docker/mysql && chmod -R 644 /usr/local/docker/mysql/conf
```

* vim /usr/local/docker/mysql/conf/my.cnf

```bash
# 服务端参数配置
[mysqld]
user=mysql                     # MySQL启动用户
default-storage-engine=INNODB  # 创建新表时将使用的默认存储引擎
character-set-server=utf8mb4   # 设置mysql服务端默认字符集
collation-server = utf8mb4_general_ci # 数据库字符集对应一些排序等规则，注意要和character-set-server对应

pid-file        = /var/lib/mysql/mysqld.pid  # pid文件所在目录
socket          = /var/lib/mysql/mysqld.sock # 用于本地连接的socket套接字
datadir         = /var/lib/mysql             # 数据文件存放的目录
#bind-address   = 127.0.0.1                   # MySQL绑定IP

# 设置client连接mysql时的字符集,防止乱码
init_connect='SET NAMES utf8mb4'

# 是否对sql语句大小写敏感，1表示不敏感
lower_case_table_names = 1

# 执行sql的模式，规定了sql的安全等级, 暂时屏蔽，my.cnf文件中配置报错
#sql_mode = STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION

# 事务隔离级别，默认为可重复读，mysql默认可重复读级别（此级别下可能参数很多间隙锁，影响性能）
transaction_isolation = READ-COMMITTED

# TIMESTAMP如果没有显示声明NOT NULL，允许NULL值
explicit_defaults_for_timestamp = true

#它控制着mysqld进程能使用的最大文件描述(FD)符数量。
#需要注意的是这个变量的值并不一定是你设定的值，mysqld会在系统允许的情况下尽量获取更多的FD数量
open_files_limit    = 65535

# 允许最大连接数
max_connections=200

#最大错误连接数
max_connect_errors = 600


[client]
default-character-set=utf8mb4  # 设置mysql客户端默认字符集
```



* docker-compose-mysql.yml  [参考](https://blog.csdn.net/weixin_40461281/article/details/111246938)

```dockerfile
version: '3' # 指定了 Docker Compose 文件的格式版本，版本3是常用版本之一
services:
  mysql:
    image: mysql:8.3
    container_name: mysql8
    restart: unless-stopped #  指定了容器的重启策略，除了手动停止容器，其他情况都自动重启容器
    environment:
      - TZ=Asia/Shanghai # 指定了容器的环境变量，设置容器的时区为上海时区
      - LANG=en_US.UTF-8 # 指定了容器的环境变量，设置容器的编码为UTF-8
      - MYSQL_ROOT_HOST=%
      - MYSQL_ROOT_PASSWORD=123456
    ports:
      - 3306:3306
    logging:
        driver: "json-file"
        options:
            max-size: "10m"
            max-file: "3"
    volumes:
        - /usr/local/docker/mysql/conf:/etc/mysql/conf.d
        - /usr/local/docker/mysql/data:/var/lib/mysql
        - /usr/local/docker/mysql/logs:/var/log/mysql
```



* 安装

```bash
docker-compose -f docker-compose-mysql.yml -p mysql8 up -d
# 或者
docker-compose --compatibility -f docker-compose-mysql.yml -p mysql8 up -d
```

* 停止

```bash
docker-compose -f docker-compose-mysql.yml -p mysql8 stop
```

* 启动

```bash
docker-compose -f docker-compose-mysql.yml -p mysql8 start
```

* 重启

```bash
docker-compose -f docker-compose-mysql.yml -p mysql8 restart
```

* 卸载

```bash
docker-compose -f docker-compose-mysql.yml -p mysql8 down

# 删除数据
rm -rf /usr/local/docker/mysql/{data,logs}/*
```





## 问题

* 远程访问提示`Public Key Retrieval is not allowed`
  * url参数加上`allowPublicKeyRetrieval=true`

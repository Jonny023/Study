# docker安装mysql

### 下载mysql

```sh
# 下载
sudo docker pull mysql:5.7.38

# 查看镜像
sudo docker images

# 启动mysql
docker run -p 3306:3306 --name mysql \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7.38
```

### 修改配置

```properties
# 编辑配置
vi /mydata/mysql/conf/my.cnf

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
init_connect='SET collation_connection=utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
skip-name-resolve
```

### 重启mysql

```
docker restart mysql
```


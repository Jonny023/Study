# docker mysql8.0

* 拉取镜像

```shell
docker pull mysql:8
```

* 运行容器

```shell
docker run -itd -p 3309:3306 --restart=always \
--privileged=true \
--name mysql8 \
-v /data/docker/volumes/mysql8/conf:/etc/mysql \
-v /data/docker/volumes/mysql8/data:/var/lib/mysql \
-v /data/docker/volumes/mysql8/log/mysql:/var/log/mysql \
-v /data/docker/volumes/mysql8/mysql-files:/var/lib/mysql-files \
-v /etc/localtime:/etc/localtime \
-e MYSQL_ROOT_PASSWORD=123456 \
mysql:8
```

* 授权

```shell
# 进入容器
docker exec -it mysql8 bash

# 连接mysql,初始密码为空
mysql -uroot

# 查看用户权限
select user,host,authentication_string from mysql.user;

# 存在多个root用户，删除其中一个
DROP USER 'root'@'localhost';

# 选择库
use mysql

# 修改支持远程访问
update user set host='%' where user='root';

# Mysql5.6以上的版本修改了Password算法，这里需要更新密码算法
alter user 'root'@'%' IDENTIFIED by '123456' PASSWORD EXPIRE NEVER;

# 允许远程访问
grant all PRIVILEGES on *.* to root@'%' WITH GRANT OPTION;

alter user 'root'@'%' IDENTIFIED WITH mysql_native_password by '123456';

# 保存
flush privileges;
```


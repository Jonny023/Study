## Docker

```shell
docker exec -it mysql57 /bin/bash
mysqldump -uroot -p123456 database > /usr/local/sql/xxx.sql


# 导入数据库
mysql -u root -p --default-character-set=utf8

use bi_server;
source /usr/local/sql/xxx.sql;

# 搜索
docker search mysql

# 下载
docker pull mysql:5.7

mkdir -p /data/docker/volumes/mysql/conf
mkdir -p /data/docker/volumes/mysql/data

# 运行docker
docker run -itd --restart=always --name mysql57 -p 3306:3306 \
--mount type=bind,src=/data/docker/volumes/mysql/conf,dst=/etc/mysql \
--mount type=bind,src=/data/docker/volumes/mysql/data,dst=/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 mysql:5.7

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
```


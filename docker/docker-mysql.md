## Docker

```shell
docker exec -it mysql57 /bin/bash
mysqldump -uroot -p123456 database > /usr/local/sql/xxx.sql


# 导入数据库
mysql -u root -p --default-character-set=utf8

use bi_server;
source /usr/local/sql/xxx.sql;


docker run -itd --name mysql57 -p 3306:3306 \
--mount type=bind,src=/data/docker/volumes/mysql/conf,dst=/etc/mysql \
--mount type=bind,src=/data/docker/volumes/mysql/data,dst=/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 mysql:5.7

# 在外部dump出docker容器里面的数据
docker exec -it  mysql57 mysqldump -uroot -p123456 databaseName > /usr/local/sql/xxx.sql
```


# 安装postgres数据库

* 搜索

```shell
docker search postgres
```

* 拉取

```shell
docker pull postgres
```

* 运行

```shell
docker run -itd --name pg \
-p 5432:5432 \
-e TZ=Asia/Shanghai \
-e POSTGRES_PASSWORD=123456 \
-e PGDATA=/var/lib/postgresql/data/pgdata \
-v /usr/local/docker/postgresql/data:/var/lib/postgresql/data \
postgres
```

* 开启自启

```shell
docker update --restart=always

docker update --restart=no
```

* 操作

```shell
# 查看容器时区
docker exec pg date
docker exec pg date -R

# 查看日志
docker logs pg
# 控制台实时查看
docker logs -f pg
# 实时查看指定行数
docker logs -f -t --tail 20 pg

# 进入容器
docker exec -it pg bash

# 连接pg
psql -U postgres -W 123456

# 帮助
psql --help

# 查看数据库
\l

# 退出控制台
\q
```


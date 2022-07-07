## docker安装redis

```sh
# 拉取镜像
docker pull redis

mkdir -p /mydata/redis/conf
touch /mydata/redis/conf/redis.conf

# 运行
docker run -p 6379:6379 --name redis \
-v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf

# 连接
docker exec -it redis redis-cli
```

### 持久化配置

[完整配置文件](https://github.com/redis/redis/blob/6.2/redis.conf)

```sh
vi /mydata/redis/conf/redis.conf

appendonly yes
```

### 重启服务

```
docker redstart redis
```


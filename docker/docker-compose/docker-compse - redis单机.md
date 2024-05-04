## docker-compose部署单机版redis

* 创建数据配置目录

```sh
mkdir -p /home/docker/redis/{conf,data,logs}
```

* 创建配置

> vim /home/docker/redis/conf/redis.conf

```properties
# 关闭保护模式，允许远程连接
protected-mode no
# 开启AOF持久化
appendonly yes 
# 密码
requirepass 123456
```

* 编写compose配置

> vim redis-compose.yml

```yaml
version: '2'
services:
  redis:
    image: redis
    container_name: redis
    # command: redis-server --requirepass 123456
    ports:
      - "6379:6379"
    volumes:
      - /home/docker/redis/conf/redis.conf:/etc/redis/redis.conf
      - /home/docker/redis/data:/data
      - /home/docker/redis/logs:/logs
    restart: always
    privileged: true
    command: ["redis-server","/etc/redis/redis.conf"]
```

* 启动

```sh
docker-compose -f redis-compose.yml up -d 
```

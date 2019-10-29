### 备份数据

> 在redis安装目录中创建dump.rdb文件

* 立即备份
```
SAVE
```

* 后台运行备份
```
BGSAVE
```

### 恢复数据

> 如果需要恢复数据，只需将备份文件 (dump.rdb) 移动到 redis 安装目录并启动服务即可

### 查看redis安装目录

```
config get dir
```

### 安全

* 查看密码

```
CONFIG get requirepass
```

* 设置密码

```
config set requirepass "123456"
``` 

* 连接认证

```
127.0.0.1:6379> auth 123456
OK
```

### 获取所有配置

```
config get *
```

### 数据存储

> Redis支持五种数据类型：`string`（字符串），`hash`（哈希），`list`（列表），`set`（集合）及`zset`(`sorted set`：有序集合)。

#### string类型最大存储512MB

* string数据存取

```
redis 127.0.0.1:6379> SET runoob "菜鸟教程"
OK
redis 127.0.0.1:6379> GET runoob
"菜鸟教程"

DEL runoob
```

* hash数据存取

> 每个`hash`可以存储`2^32 - 1`键值对（`40`多亿）

```
redis 127.0.0.1:6379> HMSET runoob field1 "Hello" field2 "World"
"OK"
redis 127.0.0.1:6379> HGET runoob field1
"Hello"
redis 127.0.0.1:6379> HGET runoob field2
"World"

```

### List(列表)

> 列表最多可存储2^32 - 1元素 (4294967295, 每个列表可存储40多亿)

```
redis 127.0.0.1:6379> lpush runoob redis
(integer) 1
redis 127.0.0.1:6379> lpush runoob mongodb
(integer) 2
redis 127.0.0.1:6379> lpush runoob rabitmq
(integer) 3
redis 127.0.0.1:6379> lrange runoob 0 10
1) "rabitmq"
2) "mongodb"
3) "redis"
```

### Set（集合）

#### sadd 命令

> 添加一个 string 元素到 key 对应的 set 集合中，成功返回 1，如果元素已经在集合中返回 0。

```
redis 127.0.0.1:6379> sadd runoob redis
(integer) 1
redis 127.0.0.1:6379> sadd runoob mongodb
(integer) 1
redis 127.0.0.1:6379> sadd runoob rabitmq
(integer) 1
redis 127.0.0.1:6379> sadd runoob rabitmq
(integer) 0
redis 127.0.0.1:6379> smembers runoob

1) "redis"
2) "rabitmq"
3) "mongodb"
```

### zset(sorted set：有序集合)

#### zadd 命令

> 添加元素到集合，元素在集合中存在则更新对应score

```
redis 127.0.0.1:6379> zadd runoob 0 redis
(integer) 1
redis 127.0.0.1:6379> zadd runoob 0 mongodb
(integer) 1
redis 127.0.0.1:6379> zadd runoob 0 rabitmq
(integer) 1
redis 127.0.0.1:6379> zadd runoob 0 rabitmq
(integer) 0
redis 127.0.0.1:6379> > ZRANGEBYSCORE runoob 0 1000
1) "mongodb"
2) "rabitmq"
3) "redis"
```

### Redis 发布订阅

* 订阅：创建了订阅频道名为 redisChat:

```
127.0.0.1:6379> subscribe redisChat
```

* 客户端发布消息

```
127.0.0.1:6379> PUBLISH chat "hello"
```

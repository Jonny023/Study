# Redis运维常用

## 连接redis服务

```shell
# 连接redis
/usr/local/redis/bin/redis-cli -h 192.168.1.2 -p 6379

# 认证
auth 123456

# 选择库
select 10
```

## 操作

```shell
# 查看所有key
keys *

# 查看指定前缀key
keys "user_*"

# 删除指定key
del user_1

# 清空所有数据
flushall
```


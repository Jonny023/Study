## Redis而发性连接超时报错，多出现在远程连接服务器Redis的时候

> org.springframework.dao.QueryTimeoutException: Redis command timed out; 

* 配置

```properties
# Redis config
spring.redis.host=192.168.0.99
spring.redis.port=6379
spring.redis.password=123456
spring.redis.timeout=5000
spring.redis.database=0
spring.redis.jedis.pool.max-active=8
spring.redis.jedis.pool.max-wait=-1
spring.redis.jedis.pool.min-idle=0
spring.redis.jedis.pool.max-idle=30
```

# 解决方案

> 不要用lettuce连接池，用jedis连接池

* 添加依赖

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```


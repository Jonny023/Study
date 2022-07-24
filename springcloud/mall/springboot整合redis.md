## springboot整合redis

### RedisTemplate

#### 1.pom依赖

> springboot2+默认用lettuce作为客户端，底层基于netty实现网络通信，会有bug在io.netty.util.internal.PlatformDependentl类中有logger.debug("-Dio.netty.maxDirectMemory: {} bytes", maxDirectMemory);内存限制，所以可以用老版的jedis客户端，需要排除lettuce依赖，引入jedis客户端

```xml
		<dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-redis</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>io.lettuce</groupId>
                    <artifactId>lettuce-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
        </dependency>
```

#### 2.分布式锁

```java
package com.example.springbootredisdemo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class CacheController {

    private Logger log = LoggerFactory.getLogger(CacheController.class);

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/getData")
    public Object getData() {
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);
        if (lock) {
            Map<String, Object> map;
            try {
                log.info("获取锁成功");
                map = getDb();
            } finally {
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                // 删除锁
                Long lockMark = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            return map;
        } else {
            log.warn("获取锁失败，重新获取锁");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            return getData();
        }
    }

    /**
     * 模拟db查询
     *
     * @return
     */
    private Map<String, Object> getDb() {
        String cache = stringRedisTemplate.opsForValue().get("cache");
        if (StringUtils.hasText(cache)) {
            Map<String, Object> map = JSON.parseObject(cache, new TypeReference<Map<String, Object>>() {
            });
            return map;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("A", "张三");
        map.put("B", "李四");
        map.put("C", "王五");
        log.info("缓存数据");
        stringRedisTemplate.opsForValue().set("cache", JSON.toJSONString(map), 3, TimeUnit.MINUTES);
        return map;
    }

}
```

### Redission

#### 1.pom依赖

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.17.1</version>
</dependency>
```

#### 2.yml配置

```yaml
spring:
  redis:
    host: 192.168.56.111
    port: 6379
```

#### 3.示例

```java
@GetMapping("/getData")
public Object get() {
    RLock lock = redissonClient.getLock("my-lock");
    lock.lock(3000, TimeUnit.SECONDS);
    try {
        Thread.sleep(10000);
    } catch (InterruptedException e) {

    } finally {
        lock.unlock();
    }
    return "success";
}
```


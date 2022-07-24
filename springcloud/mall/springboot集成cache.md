## springboot集成cache

### 1.依赖

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.17.1</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```



### 2.yml

```yaml
spring:
  redis:
    host: 192.168.56.111
    port: 6379
  cache:
    type: redis
    redis:
      time-to-live: 360000
      key-prefix: CACHE_
      use-key-prefix: true
      cache-null-values: true # 缓存null值，防止缓存穿透
```



### 3.配置类

```java
package com.example.springbootredisdemo.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration {

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        //config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
```

### 4.使用

```java
package com.example.springbootredisdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {

    private final Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    /**
     *  最终缓存的key：test:data
     *  key可以用变量，如果是字符用单引号，如key = "'data'"
     *  key = "#root.method.name" 方法名
     *  sync = true 同步锁
     * @return
     */
    @Override
    @Cacheable(value = {"test"}, key = "#root.method.name", sync = true)
    public Map<Integer, String> get() {
        return getDb();
    }

    private Map<Integer, String> getDb() {
        log.info("query db.");
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "张三");
        map.put(2, "李四");
        map.put(3, "王五");
        return map;
    }

    /**
     *  更新缓存: @CachePut
     *  清空缓存: @CacheEvict
     *  清空多个缓存:
     *  @Caching(evict = {
     *     @CacheEvict(value = {"test"}, key = "'m1'"),
     *     @CacheEvict(value = {"test"}, key = "'m2'")
     *  })
     *
     *  删除缓存分区所有数据:
     *  @CacheEvict(value = {"test"}, allEntries = true)
     *
     *  双写模式： @CachePut(value = {"test"}, key = "'get'")
     *
     * @return
     *
     */
    @Override
    //@CachePut(value = {"test"}, key = "'get'")
    @CacheEvict(value = {"test"}, key = "'get'")
    public Map<Integer, String> update() {
        Map<Integer, String> map = getDb();
        map.put(1, "张三更新");
        return map;
    }

}
```


# SpringBoot Redis缓存

* `pom.xml`配置
  * 之所以排除`lettuce`是因为连接池用`lettuce`经常容易连不上，换成`jedis`连接池

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>lettuce-core</artifactId>
            <groupId>io.lettuce</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

* `yml`配置

```yaml
spring:
   redis:
    ## Redis数据库索引（默认为0）
    database: 0
    ## Redis服务器地址
    host: 127.0.0.1
    ## Redis服务器连接端口
    port: 6379
    ## Redis服务器连接密码（默认为空）
    password: root
    jedis:
      pool:
        ## 连接池最大连接数（使用负值表示没有限制）
        #spring.redis.pool.max-active=8
        max-active: 8
        ## 连接池最大阻塞等待时间（使用负值表示没有限制）
        #spring.redis.pool.max-wait=-1
        max-wait: -1
        ## 连接池中的最大空闲连接
        #spring.redis.pool.max-idle=8
        max-idle: 8
        ## 连接池中的最小空闲连接
        #spring.redis.pool.min-idle=0
        min-idle: 0
    ## 连接超时时间（毫秒）
    timeout: 1200
```

* `redis`配置

```java
package cn.com.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-02-04
 */
@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);//已过时换成activateDefaultTyping
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置序列化（解决乱码的问题）,过期时间600秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }

    /**
     * 当redis不可用时可以走db查询
     * 核心：继承CachingConfigurerSupport类重写errorHandler方法
     * @return
     */
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                redisNotAvailable(e, key);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                redisNotAvailable(e, key);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                redisNotAvailable(e, key);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                redisNotAvailable(e, null);
            }
        };
        return cacheErrorHandler;
    }

    protected void redisNotAvailable(Exception exception, Object key) {
        log.error("Redis not avalable. key: {} {}", key, exception);
    }
}
```

* 启用缓存

```java
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableCaching //启用缓存
public class DemoApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
 
}
```

* 在方法上加入缓存配置

```java
@Service
public class UserServiceImpl {
    
    // 缓存数据
    @Cacheable(value = "user", key = "#username", unless = "#result==null")
    public UserVO findByUsername(String username) {
        QUser userModel = QUser.user;
        User user = (User) super.jpaQueryFactory.select(userModel).from(userModel).where(userModel.username.eq(username)).fetchOne();
        return Convertor.convert(user, UserVO::new);
    }

    // 删除时触发删除缓存
	@CacheEvict(value = "user",key = "#userId")
    public int deleteUser(Long userId) {
 		userRepository.delete(userId);
    }
}
```


* 手动清除缓存

```java
cacheManager.getCache(cacheName).clear()
```

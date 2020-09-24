```xml
<!--redis-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
	<groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.6.5</version>
</dependency>
```

> yml配置

```yml
spring:
  redis:
    host: localhost
    port: 6379
    password: 123456
    database: 0
    timeout: 5000
    jedis: #lettuce: lettuce超时后不自动重连
      pool:
        max-active: 8
        max-wait: -1
        max-idle: 8
        min-idle: 0
```

> 配置类

```java
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 配置自定义redisTemplate
     * @return
     */
    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(MessageFormat.format("redis://{0}:{1}", redisProperties.getHost(), String.valueOf(redisProperties.getPort())))
                .setConnectionPoolSize(redisProperties.getJedis().getPool().getMaxActive())
                .setConnectionMinimumIdleSize(redisProperties.getJedis().getPool().getMaxActive());
        if(StringUtils.hasText(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);
    }

}
```

> 例子

```java
public void send() {
    String lockKey = "lock:user:" + userId;
    //启用分布式锁
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock(30, TimeUnit.SECONDS);
    try {
        //TODO 业务处理
    } finally {
        lock.unlock();
    }
}
```


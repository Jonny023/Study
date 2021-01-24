* 保证key值不乱码

```java
package com.example.redistest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /***
     * 自己实例化RedisTemplate对象
     * <bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate"></bean>
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate redisTemplate(){
        System.out.println("=====================redisTemplate");
        RedisTemplate redisTemplate = new RedisTemplate();
        //将系统实例化的redisConnectionFactory注入到自己实例化的redisTemplate对象中
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //创建StringRedisSerializer对象
        StringRedisSerializer key=new StringRedisSerializer();
        //创建GenericJackson2JsonRedisSerializer对
        GenericJackson2JsonRedisSerializer value=new GenericJackson2JsonRedisSerializer();

        //给自己实例化的redisTemplate对属性赋值
        redisTemplate.setKeySerializer(key);
        redisTemplate.setValueSerializer(value);
        redisTemplate.setHashKeySerializer(key);
        redisTemplate.setHashValueSerializer(value);

        return redisTemplate;
    }
}
```

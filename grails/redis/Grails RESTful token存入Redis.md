## Grails3 RESTful token存入Redis

* 依赖

```groovy
compile 'org.grails.plugins:spring-security-core:3.2.3'
compile "org.grails.plugins:spring-security-rest:2.0.0.RC1"
compile ("org.grails.plugins:spring-security-rest-redis:2.0.0.RC1") {
    exclude group: "org.grails.plugins", module: "grails-redis"
}
compile 'org.grails.plugins:redis:2.0.5'
```

* 配置文件

```yaml
grails:
    redis:
        poolConfig:
            maxIdle: 10
            doesnotexist: true
#        timeout: 2000   #default in milliseconds
#        password: 'Yfjcadmin123'    #defaults to no password
        useSSL: false   #or true to use SSL
        port: 6379
        host: localhost
        # 默认使用db5，初始db0-db15
        database: 5
        # 7 day
        token:
            timeout: 604800
```

> 如果不重写默认服务可以用默认配置

```groovy
springsecurity.rest.token.storage.useRedis = true
springsecurity.rest.token.storage.redis.expiration = 3600
```

* 重写服务类

```groovy
package com.jonny.token.storage

import com.middol.utils.RedisKeyService
import grails.plugin.springsecurity.rest.token.storage.RedisTokenStorageService
import grails.plugin.springsecurity.rest.token.storage.TokenNotFoundException
import grails.plugins.redis.RedisService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.core.serializer.support.SerializingConverter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import redis.clients.jedis.Jedis

/**
 * @description 重写实现token存储
 * @author Jonny
 * @date 2019/8/16
 */
@Slf4j
class CustomRedisTokenStorageService extends RedisTokenStorageService {

    @Autowired
    RedisService redisService
    @Autowired
    UserDetailsService userDetailsService
    @Autowired
    RedisKeyService redisKeyService

    /** Expiration in seconds */
    @Value('${grails.redis.token.timeout:3600}')
    Integer expiration = 3600

    private static final String PREFIX = "spring:security:token:"

    Converter<Object, byte[]> serializer = new SerializingConverter()

    @Override
    UserDetails loadUserByToken(String tokenValue) throws TokenNotFoundException {
        log.debug "Searching in Redis for UserDetails of token ${tokenValue}"

        byte[] userDetails
        redisService.withRedis { Jedis jedis ->
            String key = buildKey(tokenValue)
            userDetails = jedis.get(key.getBytes('UTF-8'))
            jedis.expire(key, expiration)
        }

        if (userDetails) {
            return deserialize(userDetails) as  UserDetails
        } else {
            throw new TokenNotFoundException("Token ${tokenValue} not found")
        }

    }

    @Override
    void storeToken(String tokenValue, UserDetails principal) {
        log.debug "Storing principal for token: ${tokenValue} with expiration of ${expiration} seconds"
        log.debug "Principal: ${principal}"

        redisService.withRedis { Jedis jedis ->
            String key = buildKey(tokenValue)
            jedis.set(key.getBytes('UTF-8'), serialize(principal))
            jedis.expire(key, expiration)

            // 保存用户token
            String userKey = PREFIX + principal.username + ":" + tokenValue
            jedis.set(userKey, tokenValue)
            jedis.expire(userKey, expiration)
        }
    }

    @Override
    void removeToken(String tokenValue) throws TokenNotFoundException {
        log.debug "Removing token: ${tokenValue}"
        redisService.withRedis { Jedis jedis ->
            jedis.del(buildKey(tokenValue))
        }
        // 根据token获取key
        List<String> list = redisKeyService.byToken(tokenValue)
        if(list) {
            redisService.withRedis { Jedis jedis ->
                // 移除token
                jedis.del(list[0])
            }
        }
    }

    private static String buildKey(String token){
        "$PREFIX$token"
    }

    private Object deserialize(byte[] bytes) {
        new ByteArrayInputStream(bytes).withObjectInputStream(getClass().classLoader) { is ->
            return is.readObject()
        }
    }

    private byte[] serialize(Object object) {
        if(object == null) {
            return new byte[0]
        } else {
            try {
                return serializer.convert(object)
            } catch (Exception e) {
                throw new Exception("Cannot serialize", e)
            }
        }
    }

}
```

* 注册

```groovy
// Place your Spring DSL code here
beans = {
    tokenStorageService(com.middol.token.storage.CustomRedisTokenStorageService)
}
```

* 模糊查询工具类

```groovy
package com.jonny.utils

import grails.plugins.redis.RedisService
import redis.clients.jedis.Jedis
import redis.clients.jedis.ScanParams

/**
 * @description Redis服务工具类
 * @author Jonny
 * @date 2019/8/16
 */
class RedisKeyService {

    RedisService redisService

    static String key = "spring:security:token:"

    /**
     *  通过用户模糊匹配找到对应的key值
     * @param username
     * @return
     */
    def get(String username) {
        redisService.withRedis { Jedis jedis ->
            ScanParams scanParams = new ScanParams()
            scanParams.match(key + username + ":" + "*")
            scanParams.count(5)
            List<String> scan = jedis.scan("0", scanParams).getResult()
            return scan
        }
    }

    /**
     *  通过用户token匹配找到对应的key值
     * @param username
     * @return
     */
    def byToken(String token) {
        redisService.withRedis { Jedis jedis ->
            ScanParams scanParams = new ScanParams()
            scanParams.match(key + "*" + ":" + token)
            scanParams.count(5)
            List<String> scan = jedis.scan("0", scanParams).getResult()
            return scan
        }
    }
}
```


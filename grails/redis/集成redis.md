* 插件地址：[http://plugins.grails.org/plugin/ctoestreich/redis](http://plugins.grails.org/plugin/ctoestreich/redis)
* 文档地址：[https://github.com/grails-plugins/grails-redis](https://github.com/grails-plugins/grails-redis)

## 核心代码

### `build.gradle`中引入依赖

```
compile 'org.grails.plugins:redis:2.0.5'
```

### `redis配置`（`grails-app/conf/application-redis.yml`）

```yaml
grails:
  redis:
      poolConfig:
          maxIdle: 10
          doesnotexist: true
      timeout: 5000
      password: 123456
      useSSL: false
      port: 6379
      host: 127.0.0.1
      database: 5
```

### 在`application.yml`中加入配置使其生效

```yaml
spring:
    profiles:
        include: redis
```

```Groovy
package com.atgenee

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.atgenee.common.SerializeUtil
import grails.plugins.redis.RedisService
import grails.transaction.Transactional
import redis.clients.jedis.Jedis
import redis.clients.jedis.Pipeline
import redis.clients.jedis.Transaction

class UserService {

    RedisService redisService


    /**
     *  存普通键值对
     * @return
     */
    @Transactional
    def save() {

        // 直接赋值
        // 缺点： 每个键值都单独用一个Jedis实例
        redisService.realName="张三"

        // 模板方法自动从连接池中获取一个Jedis对象
        // 优点： 多个键值对用同一个Jedis实例对象
        redisService.withRedis { Jedis redis ->
            redis.set("nickname","王大锤")
        }

        // 管道
        redisService.withPipeline { Pipeline pipeline ->
            pipeline.set("age", "20")
        }

        // 事物型
        redisService.withTransaction { Transaction transaction ->
            transaction.set("sex", "男")
        }
    }


    /**
     *  清空redis所有数据
     */
    @Transactional(readOnly = true)
    void clean() {
        redisService.flushDB()
    }


    /**
     *  删除redis中指定key
     */
    @Transactional(readOnly = true)
    void deleteOfKey(String key) {
        redisService.deleteKeysWithPattern(key)
    }

    /**
     *  将查询对象存入redis
     */
    @Transactional(readOnly = true)
    def list() {
        def userList = User.list([max:50000,offset:0]).collect {
            [
                id: it?.id,
                username: it.username,
                remark: it?.remark
            ]
        }
        redisService.withRedis { Jedis redis->
//            redis.set("userList", JSON.toJSONString(userList, SerializerFeature.DisableCircularReferenceDetect))
            redis.set("userList", JSON.toJSONString(userList))
        }
    }

    /**
     *  将学生信息序列化存入redis
     */
    def store() {
        Student.withNewTransaction {
            def student = new Student("李四", 20)
            student.save(flush: true)
            redisService.withRedis { Jedis redis ->
                redis.set("student:1".getBytes(), SerializeUtil.INSTANCE.serialize(student))
            }
        }
    }

    /**
     *  读取redis中的学生信息反序列化
     */
    @Transactional(readOnly = true)
    def get(Long id) {
        redisService.withRedis { Jedis redis->
            byte[] student = redis.get(("student:"+id).getBytes())
            return SerializeUtil.INSTANCE.unserialize(student)
        }
    }
}
```


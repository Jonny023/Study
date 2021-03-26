# SpringBoot Redis缓存

* `pom.xml`配置

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
    
    @Cacheable(value = "user", key = "#username", unless = "#result==null")
    public UserVO findByUsername(String username) {
        QUser userModel = QUser.user;
        User user = (User) super.jpaQueryFactory.select(userModel).from(userModel).where(userModel.username.eq(username)).fetchOne();
        return Convertor.convert(user, UserVO::new);
    }

	@CacheEvict(value = "user",key = "#userId")
    public int deleteUser(Long userId) {
 		userRepository.delete(userId);
    }
}
```


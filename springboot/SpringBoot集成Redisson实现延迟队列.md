# SpringBoot集成Redisson实现延迟队列

使用场景

1、下单成功，30分钟未支付。支付超时，自动取消订单



2、订单签收，签收后7天未进行评价。订单超时未评价，系统默认好评



3、下单成功，商家5分钟未接单，订单取消



4、配送超时，推送短信提醒



......



对于延时比较长的场景、实时性不高的场景，我们可以采用任务调度的方式定时轮询处理。如：xxl-job



今天我们采用一种比较简单、轻量级的方式，使用 Redis 的延迟队列来进行处理。当然有更好的解决方案，可根据公司的技术选型和业务体系选择最优方案。如：使用消息中间件Kafka、RabbitMQ 的延迟队列



先不讨论其实现原理，直接实战上代码先实现基于 Redis 的延迟队列



**1、引入 Redisson 依赖**

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.10.5</version>
</dependency>
```

**2、Nacos 配置 Redis 连接**

```yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    timeout: 3000
```

**3、创建 RedissonConfig 配置**

```java
/**
 * Created by LPB on 2020/04/20.
 */
@Configuration
public class RedissonConfig {
 
 @Value("${spring.redis.host}")
 private String host;
 @Value("${spring.redis.port}")
 private int port;
 @Value("${spring.redis.database}")
 private int database;
 @Value("${spring.redis.password}")
 private String password;
 
 @Bean
 public RedissonClient redissonClient() {
  Config config = new Config();
  config.useSingleServer()
   .setAddress("redis://" + host + ":" + port)
   .setDatabase(database)
   .setPassword(password);
  return Redisson.create(config);
 }
 
}
```

**4、封装 Redis 延迟队列工具类**

```java
/**
 * redis延迟队列工具
 * Created by LPB on 2021/04/20.
 */
@Slf4j
@Component
public class RedisDelayQueueUtil {
 
    @Autowired
 private RedissonClient redissonClient;
 
    /**
     * 添加延迟队列
     * @param value 队列值
     * @param delay 延迟时间
     * @param timeUnit 时间单位
     * @param queueCode 队列键
     * @param <T>
     */
    public <T> void addDelayQueue(T value, long delay, TimeUnit timeUnit, String queueCode){
        try {
            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueCode);
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            delayedQueue.offer(value, delay, timeUnit);
   log.info("(添加延时队列成功) 队列键：{}，队列值：{}，延迟时间：{}", queueCode, value, timeUnit.toSeconds(delay) + "秒");
        } catch (Exception e) {
            log.error("(添加延时队列失败) {}", e.getMessage());
            throw new RuntimeException("(添加延时队列失败)");
        }
    }
 
 /**
  * 获取延迟队列
  * @param queueCode
  * @param <T>
  * @return
  * @throws InterruptedException
  */
    public <T> T getDelayQueue(String queueCode) throws InterruptedException {
        RBlockingDeque<Map> blockingDeque = redissonClient.getBlockingDeque(queueCode);
        T value  = (T) blockingDeque.take();
        return value;
 }
}
```

**5、创建延迟队列业务枚举**

```java
/**
 * 延迟队列业务枚举
 * Created by LPB on 2021/04/20.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RedisDelayQueueEnum {
 
 ORDER_PAYMENT_TIMEOUT("ORDER_PAYMENT_TIMEOUT","订单支付超时，自动取消订单", "orderPaymentTimeout"),
 ORDER_TIMEOUT_NOT_EVALUATED("ORDER_TIMEOUT_NOT_EVALUATED", "订单超时未评价，系统默认好评", "orderTimeoutNotEvaluated");
 
 /**
  * 延迟队列 Redis Key
  */
 private String code;
 
 /**
  * 中文描述
  */
 private String name;
 
 /**
  * 延迟队列具体业务实现的 Bean
  * 可通过 Spring 的上下文获取
  */
 private String beanId;
 
}
```

**6、定义延迟队列执行器**

```java
/**
 * 延迟队列执行器
 * Created by LPB on 2021/04/20.
 */
public interface RedisDelayQueueHandle<T> {
 
 void execute(T t);
 
}
```

**7、创建枚举中定义的Bean，并实现延迟队列执行器**

- OrderPaymentTimeout：订单支付超时延迟队列处理类

```java
/**
 * 订单支付超时处理类
 * Created by LPB on 2021/04/20.
 */
@Component
@Slf4j
public class OrderPaymentTimeout implements RedisDelayQueueHandle<Map> {
 @Override
 public void execute(Map map) {
  log.info("(收到订单支付超时延迟消息) {}", map);
  // TODO 订单支付超时，自动取消订单处理业务...
 
 }
}
```

- OrderTimeoutNotEvaluated：订单超时未评价延迟队列处理类

```java
/**
 * 订单超时未评价处理类
 * Created by LPB on 2021/04/20.
 */
@Component
@Slf4j
public class OrderTimeoutNotEvaluated implements RedisDelayQueueHandle<Map> {
 @Override
 public void execute(Map map) {
  log.info("(收到订单超时未评价延迟消息) {}", map);
  // TODO 订单超时未评价，系统默认好评处理业务...
 
 }
}
```

**8、创建延迟队列消费线程，项目启动完成后开启**

```java
/**
 * 启动延迟队列
 * Created by LPB on 2021/04/20.
 */
@Slf4j
@Component
public class RedisDelayQueueRunner implements CommandLineRunner {
 
 @Autowired
 private RedisDelayQueueUtil redisDelayQueueUtil;
 
 @Override
 public void run(String... args) {
  new Thread(() -> {
   while (true){
    try {
     RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();
     for (RedisDelayQueueEnum queueEnum : queueEnums) {
      Object value = redisDelayQueueUtil.getDelayQueue(queueEnum.getCode());
      if (value != null) {
       RedisDelayQueueHandle redisDelayQueueHandle = SpringUtil.getBean(queueEnum.getBeanId());
       redisDelayQueueHandle.execute(value);
      }
     }
    } catch (InterruptedException e) {
     log.error("(Redis延迟队列异常中断) {}", e.getMessage());
    }
   }
  }).start();
  log.info("(Redis延迟队列启动成功)");
 }
}
```

以上步骤，Redis 延迟队列核心代码已经完成，下面我们写一个测试接口，用 PostMan 模拟测试一下



**9、创建一个测试接口，模拟添加延迟队列**

```java
/**
 * 延迟队列测试
 * Created by LPB on 2020/04/20.
 */
@RestController
public class RedisDelayQueueController {
 
 @Autowired
 private RedisDelayQueueUtil redisDelayQueueUtil;
 
 @PostMapping("/addQueue")
 public void addQueue() {
  Map<String, String> map1 = new HashMap<>();
  map1.put("orderId", "100");
  map1.put("remark", "订单支付超时，自动取消订单");
 
  Map<String, String> map2 = new HashMap<>();
  map2.put("orderId", "200");
  map2.put("remark", "订单超时未评价，系统默认好评");
 
  // 添加订单支付超时，自动取消订单延迟队列。为了测试效果，延迟10秒钟
  redisDelayQueueUtil.addDelayQueue(map1, 10, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());
 
  // 订单超时未评价，系统默认好评。为了测试效果，延迟20秒钟
  redisDelayQueueUtil.addDelayQueue(map2, 20, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_TIMEOUT_NOT_EVALUATED.getCode());
 }
 
}
```

**10、启动 SpringBoot 项目，用 PostMan 调用接口添加延迟队列**

- 通过 Redis 客户端可看到两个延迟队列已添加成功

![img](https://filescdn.proginn.com/fb1a36849d72277b7eeab29256c1719c/2678088739361b015a61ae8e826abcfb.webp)



- 查看 IDEA 控制台日志可看到延迟队列已消费成功



![img](https://filescdn.proginn.com/6378106e467836d9af83b6d74f52d57b/2e97cb4028b7ef26845fc49d5608db9d.webp)

 

————————————————

版权声明：本文为CSDN博主「KK·Liu先生」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。

原文链接：

https://blog.csdn.net/qq_40087415/article/details/115940092
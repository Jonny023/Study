## 批量测试存储时间

```java
package com.jonny;

import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jonny
 * @description
 * @date 2019年08月09日 22:22
 */
public class JedisDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(50);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            final String keyPrefix = "key_" + i + "_";
            new Thread(() -> {
                Jedis jedis = new Jedis("localhost", 6379);
                for (int j = 0; j < 10000; j++) {
                    jedis.set(keyPrefix + j, "value_" + j);
                }
                count.countDown();
            }).start();
        }
        count.await();
        System.out.println("执行完毕耗时：" + (System.currentTimeMillis() - start));
    }
}
```

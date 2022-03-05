# 多线程执行完一并返回

## 通过JUC实现线程同步

juc包下就有很这种场景的并发工具类。不要在自己写`while(true)`了。

- CountDownLatch
- CompletableFuture

2种方式的实现代码

```java
package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.*;

public class MainTest {

    private static Logger log = LoggerFactory.getLogger(MainTest.class);

    public static void main(String[] args) throws Exception {

        // CountDownLatch
        test1();

        // CompletableFuture
        test2();
    }

    public static void test2() {

        ExecutorService executor = Executors.newFixedThreadPool(5); // 2个线程

        LinkedList<String> queue = new LinkedList<String>(Arrays.asList("1", "2", "3", "4", "5"));

        CompletableFuture<?>[] completableFutureArray = new CompletableFuture<?>[queue.size()];

        for (int i = 0; i < queue.size(); i++) {

            String val = queue.get(i);

            // 使用线程池异步执行任务
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("val = {}", val);
            }, executor);

            completableFutureArray[i] = completableFuture;
        }

        CompletableFuture<Void> future = CompletableFuture.allOf(completableFutureArray);

        // 阻塞，直到执行完毕
        future.join();

        log.info("执行完毕");

        executor.shutdown();
    }

    public static void test1() throws InterruptedException {

        LinkedList<String> queue = new LinkedList<String>(Arrays.asList("1", "2", "3", "4", "5"));

        CountDownLatch countDownLatch = new CountDownLatch(queue.size());

        for (String val : queue) {
            new Thread(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("val = {}", val);
                countDownLatch.countDown();
            }).start();
        }

//        countDownLatch.await();
        countDownLatch.await(10, TimeUnit.SECONDS);
        log.info("执行完毕");
    }
}
```




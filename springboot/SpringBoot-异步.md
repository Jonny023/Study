## SpringBoot 异步

### 1.线程池ThreadPoolExecutor

#### 1.1 配置

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```



#### 1.2 yaml

```yaml
thread:
  pool:
    core-size: 8
    max-Size: 10
    keep-alive-time: 30
    thread-name: my-task
```



#### 1.3 配置类

```java
package com.example.springbootredisdemo.config;

import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@ImportAutoConfiguration(ThreadPoolConfiguration.ThreadPoolProperties.class)
public class ThreadPoolConfiguration {

    @Bean("myThreadPool")
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfiguration.ThreadPoolProperties properties) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(properties.getCoreSize(), properties.getMaxSize(), properties.getKeepAliveTime(), TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100), new DefaultThreadFactory(properties.getThreadName()), new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    @Data
    @Component
    @ConfigurationProperties("thread.pool")
    public static class ThreadPoolProperties {
        private Integer coreSize;
        private Integer maxSize;
        private Integer keepAliveTime;
        private String threadName = "my-thread";
    }
}
```



#### 1.4 使用

```java
@SpringBootTest
class SpringbootRedisDemoApplicationTests {

    @Resource(name = "myThreadPool")
    private ThreadPoolExecutor executor;

    @Test
    void contextLoads() {
        System.out.printf("coreSize: %s, max: %s, keepAliveTime: %s\n", executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getKeepAliveTime(TimeUnit.SECONDS));
        executor.execute(() -> System.out.println("execute thread pool: " + Thread.currentThread().getName()));
    }

}
```



### 2.异步CompletableFuture

> 通过CompletableFuture类实现

```java
package com.example.springbootredisdemo;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class Demo {

    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(100), new DefaultThreadFactory("task-pool"), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 任务执行完成后继续执行
     * 不加Async和上一步共用线程，相当于串行，有Async表示新开一个线程执行
     * runAsync() 异步无返回
     * thenAccept() 拿到上一步结果继续执行，无返回
     * thenRun() 上一步执行完后执行，不拿上一步执行结果，无返回
     * thenApplyAsync() 拿到上一步返回结果且有返回值
     */
    @Test
    public void run() throws ExecutionException, InterruptedException {

        /*
         * 方法执行完成后处理
         */
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            // 业务处理
            int x = 10 / 3;
            System.out.println("execute ing..." + Thread.currentThread().getName());
            return x;
        }, executor).whenComplete((result, exception) -> {
            // 捕获异常，但无法返回默认值
            System.out.println("结果：" + result);
            System.out.println("异常：" + exception);
        }).exceptionally(throwable -> {
            //捕获异常，并返回默认值
            return 0;
        });
        System.out.println("运行结束" + future.get());


        /*
         * 方法执行完成后处理【有异常直接处理异常】
         */
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 业务处理
            int x = 10 / 3;
            System.out.println("execute ing..." + Thread.currentThread().getName());
            return x;
        }, executor).handle((result, exception) -> {
            // 捕获异常，但无法返回默认值
            System.out.println("结果：" + result);
            System.out.println("异常：" + exception);
            if (result != null) {
                return result;
            }
            return 0;
        });
        System.out.println("运行结束" + completableFuture.get());
    }

    /**
     * 组合多个任务
     * 两个任务都完成后执行下一个任务
     */
    @Test
    public void run1() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1");
            return 10 / 2;
        }, executor);
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2");
            return 9 / 5;
        }, executor);

        /*
         * 无法获取前一步执行结果
         */
        //task1.runAfterBothAsync(task2, () -> {
        //    System.out.println("任务3");
        //}, executor);


        /*
         * 拿到前面执行结果，无返回
         */
        //task1.thenAcceptBothAsync(task2, (result1, result2) -> {
        //    System.out.println("任务3");
        //    System.out.println("任务1：" + result1 + "任务2：" + result2);
        //}, executor);


        /*
         * 拿到前面的结果，有返回
         */
        CompletableFuture<Integer> result = task1.thenCombineAsync(task2, (result1, result2) -> {
            return result1 + result2;
        }, executor);

        System.out.println("执行结束: " + result.get());
    }

    /**
     * 两个任务其中一个执行完成后执行下一个任务
     */
    @Test
    public void run3() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1");
            return 10 / 2;
        }, executor);
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("任务2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 9 / 5;
        }, executor);

        /*
         *  不需要前面执行结果，其中一个执行完成后执行后续
         *  无返回值
         */
        //task1.runAfterEitherAsync(task2, () -> {
        //    System.out.println("任务3");
        //}, executor);

        /*
         *  拿到前面执行结果，其中一个执行完成后执行后续
         *  无返回值
         */
        //task1.acceptEitherAsync(task2, (result) -> {
        //    System.out.println("任务3:" + result);
        //}, executor);

        CompletableFuture<String> result = task1.applyToEitherAsync(task2, (result1) -> {
            System.out.println("任务3:" + result1);
            return result1 + "1";
        }, executor);
        System.out.println("执行结果：" + result.get());
    }

    /**
     * 多任务组合
     */
    @Test
    public void run4() throws ExecutionException, InterruptedException {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询用户");
            return "张三";
        }, executor);
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("查询角色");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "管理员";
        }, executor);
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询部门");
            return "技术部";
        }, executor);


        //CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(task1, task2, task3);
        /*
         * 等待所有执行完成
         */
        //voidCompletableFuture.get();
        ////voidCompletableFuture.join();
        //System.out.printf("执行完成， 结果：%s, %s, %s\n", task1.get(), task2.get(), task3.get());


        /*
         *  任意一个任务完成
         */
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(task1, task2, task3);
        Object resultObj = objectCompletableFuture.get();
        System.out.printf("执行任意一个完成， 结果：%s\n", resultObj);

    }

}
```


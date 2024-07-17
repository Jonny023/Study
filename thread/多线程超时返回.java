package org.example.springbootsession.jdk17;

import org.example.springbootsession.domain.Role;
import org.example.springbootsession.domain.User;

import java.util.concurrent.*;

public class 多线程超时返回 {

    private static ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {

        CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> {
            User user = new User();
            user.setUsername("user1");
            return user;
        }, EXECUTOR);

        // 在第一个任务完成后，启动第二个任务，并设置超时
        CompletableFuture<User> roleFuture = userFuture.thenApplyAsync(user -> {
            setRole(user);
            return user;
        }, EXECUTOR).orTimeout(2, TimeUnit.SECONDS);

        // 等待结果，如果超时，则返回原始userFuture的结果
        User result = roleFuture.exceptionally(ex -> {
            if (ex instanceof TimeoutException) {
                // 忽略setRole的超时，返回原始userFuture的结果
                return userFuture.join();
            }
            // 其他异常情况，可以根据需要处理
            return null;
        }).join(); // 直接使用join来等待结果，不会抛出异常

        System.out.println(result);
        EXECUTOR.shutdown(); // 关闭ExecutorService

    }

    public static void setRole(User user) {
        try {
            Thread.sleep(5000);
            user.setRole(new Role("admin", "超管"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
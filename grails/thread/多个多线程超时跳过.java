package org.example.springbootsession.jdk17;

import org.example.springbootsession.domain.Role;
import org.example.springbootsession.domain.User;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class 多个多线程超时跳过 {

    private static ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        User user = new User();
        user.setUsername("user1");

        // 创建三个独立的CompletableFuture任务
        CompletableFuture<User> setAddressFuture = CompletableFuture.supplyAsync(() -> {
            setUserAddress(user);
            return user;
        }, EXECUTOR).orTimeout(2, TimeUnit.SECONDS);

        CompletableFuture<User> setExtendsInfoFuture = CompletableFuture.supplyAsync(() -> {
            setUserExtendsInfo(user);
            return user;
        }, EXECUTOR).orTimeout(2, TimeUnit.SECONDS);

        CompletableFuture<User> setRoleFuture = CompletableFuture.supplyAsync(() -> {
            setRole(user);
            return user;
        }, EXECUTOR).orTimeout(2, TimeUnit.SECONDS);

        // 等待所有任务完成，并处理超时
        User finalResult = waitForAllTasksWithTimeout(
                setAddressFuture,
                setExtendsInfoFuture,
                setRoleFuture,
                user
        );

        System.out.println(finalResult);
        EXECUTOR.shutdown(); // 关闭ExecutorService
    }

    private static User waitForAllTasksWithTimeout(
            CompletableFuture<User> setAddressFuture,
            CompletableFuture<User> setExtendsInfoFuture,
            CompletableFuture<User> setRoleFuture,
            User defaultUser) {
        AtomicReference<User> result = new AtomicReference<>(defaultUser);

        CompletableFuture.allOf(setAddressFuture, setExtendsInfoFuture, setRoleFuture)
                .exceptionally(ex -> null)
                .join();

        setAddressFuture.whenComplete((user, ex) -> {
            if (ex == null) {
                result.updateAndGet(currentUser -> {
                    currentUser.setAddress(user.getAddress());
                    return currentUser;
                });
            } else {
                System.out.println("setAddressFuture timed out or failed");
            }
        });

        setExtendsInfoFuture.whenComplete((user, ex) -> {
            if (ex == null) {
                result.updateAndGet(currentUser -> {
                    currentUser.setExtendsInfo(user.getExtendsInfo());
                    return currentUser;
                });
            } else {
                System.out.println("setExtendsInfoFuture timed out or failed");
            }
        });

        setRoleFuture.whenComplete((user, ex) -> {
            if (ex == null) {
                result.updateAndGet(currentUser -> {
                    currentUser.setRole(user.getRole());
                    return currentUser;
                });
            } else {
                System.out.println("setRoleFuture timed out or failed");
            }
        });

        return result.get();
    }

    public static void setUserAddress(User user) {
        // 模拟设置用户地址的逻辑
        user.setAddress("北京");
    }

    public static void setUserExtendsInfo(User user) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 模拟设置用户扩展信息的逻辑
        user.setExtendsInfo("用户扩展信息");
    }

    public static void setRole(User user) {
        // 模拟设置用户角色的逻辑
        user.setRole(new Role("admin", "超管"));
    }
}

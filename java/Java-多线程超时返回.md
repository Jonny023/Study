## 多线程超时返回

[参考](https://blog.csdn.net/wonking666/article/details/64907397)

> 主线程等待子线程返回结果，若指定时长仍未返回，则执行后续操作

* 方法1（get(超时时间,超时单位)）

> return futureTask.get(2, TimeUnit.SECONDS);

```java
	/**
     *  主线程等待子线程返回，若2秒仍未返回则直接返回主线程结果（超时返回）
     * @return string
     */
    public static String exe() {
        FutureTask<String> futureTask = new FutureTask<String>(() -> {
            Thread.sleep(10000);
            return "sub thread.";
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println("执行主线程...");
        try {
            return futureTask.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            futureTask.cancel(true);
        }
        return "main thread.";
    }
```

* 方法2

> thread.join(2000);
> thread.interrupt();

```java
	/**
     * 主线程等待子线程返回，若2秒仍未返回则直接返回主线程结果（超时返回）
     *
     * @return string
     */
    public static String exe() {
        FutureTask<String> futureTask = new FutureTask<String>(() -> {
            Thread.sleep(3000);
            return "sub thread.";
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            thread.join(2000);
            thread.interrupt();
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "main thread.";
    }
```

* 方法3

> ExecutorService.awaitTermination(long million, TimeUnit unit)
## 多线程超时返回

> 主线程等待子线程返回结果，若指定时长仍未返回，则执行后续操作

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
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return "main thread.";
    }
```


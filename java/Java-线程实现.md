# 多线程的3中实现方式

* 继承Thread类
* 实现Runnable接口
* 实现Callable接口

> 实例类

```java
import java.util.concurrent.FutureTask;

public class Main {

    public static void main(String[] args) {

        // 继承Thread
        ThreadDemo threadDemo = new ThreadDemo();
        threadDemo.start();

        // 实现Runnable接口
        RunnableDemo runnableDemo = new RunnableDemo();
        Thread thread = new Thread(runnableDemo);
        thread.start();

        // 实现Callable接口(带返回值)
        CallableDemo callableDemo = new CallableDemo();
        FutureTask futureTask = new FutureTask(callableDemo);
        Thread callThread = new Thread(futureTask);
        callThread.start();
        try {
            System.out.println(futureTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
```

> 继承Thread类

```java
/**
 * @Author Lee
 * @Description
 * @Date 2019年04月16日 20:09
 */
public class ThreadDemo extends Thread {

    @Override
    public void run() {
        System.out.println("继承Thread类实现.");
    }
}
```
> 实现Runnable接口

```java
/**
 * @Author Lee
 * @Description
 * @Date 2019年04月16日 20:11
 */
public class RunnableDemo implements Runnable {

    @Override
    public void run() {
        System.out.println("实现Runnable接口.");
    }
}

```
> 实现Callable接口

```java
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @Author Lee
 * @Description
 * @Date 2019年04月16日 20:20
 */
public class CallableDemo implements Callable {

    @Override
    public Object call() throws Exception {
        return "实现Callable接口.";
    }
}
```

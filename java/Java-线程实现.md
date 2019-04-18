# 多线程的3种实现方式

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

## 多线程实现

> `Thread`类是`Runnable`接口类的子类（实现类）

* 无论是继承`Thread`类，还是实现`Runnable`接口都必须重写`run`方法
* 真正的`cpu`调度是通过`Thread`类中的`start()`方法调用的



### 线程的状态

* 每一个线程对象都要经历五个步骤：
   1、 初始化：当创建了一个新的线程对象时
   2、 等待：调用了 start()方法
   3、 执行：调用 run()执行的操作的过程
   4、 停止：因为所有的线程都需要进行 CPU 资源的抢占，那么当一个线程执行完部分代码要交出资源，留给
  其他线程继续执行。
   5、 卸载：所有的线程的操作代码都执行完毕之后，就将线程对象卸载下来。



> 实现`Runnable`接口实现卖票

```java
/**
 * @Author Lee
 * @Description
 * @Date 2019年04月18日 21:10
 */
public class RunnableTest implements Runnable {
    // 余票
    private int count = 100;
    @Override
    public void run() {
        for (int i = 0;i< 100;i++) {
            if(this.count > 0) {
                System.out.println(Thread.currentThread().getName()+"卖票:"+count--);
            }
        }
    }

    public static void main(String[] args) {
        RunnableTest r = new RunnableTest();
        // 三个窗口同时卖100张票
        new Thread(r,"一号窗口").start();
        new Thread(r,"二号窗口").start();
        new Thread(r,"三号窗口").start();
    }
}
```

### 线程优先级

> 所有的线程启动之后并不是立刻运行的，都需要等待`CPU` 进行调度，但是调度的时候本身也是存在“优先”
> 级的，如果优先级高则有可能最先被执行。

* 如果要想设置优先级可以使用：`public final void setPriority(int newPriority) `
* 这个优先级需要接收一个整型的数字，这个数字只能设置三个内容：
  * 最高优先级：`public static final int MAX_PRIORITY`
  * 中等优先级：`public static final int NORM_PRIORITY`
  * 最低优先级：`public static final int MIN_PRIOR ITY`

* 示例

  * ```java
    RunnableTest r = new RunnableTest();
    Thread t1 = new Thread(r,"一号窗口");
    t1.setPriority(Thread.MIN_PRIORITY);
    t1.start();
    
    Thread t2 = new Thread(r,"二号窗口");
    t2.setPriority(Thread.MAX_PRIORITY);
    t2.start();
    
    Thread t3 = new Thread(r,"三号窗口");
    t3.setPriority(Thread.NORM_PRIORITY);
    t3.start();
    ```

    

### Object 类对线程的支持

* 在 `Object`类中提供了以下的方法可以实现对线程的等待及唤醒的处理：
  * 等待：`public final void wait() throws InterruptedException`
  * 唤醒：`public final void notify()`，唤醒第一个等待的线程
  * 唤醒：`public final void notifyAll()`，唤醒全部等待的线程

## 多个线程访问同一个资源的时候需要进行同步，但是过多的同步会产生死锁。


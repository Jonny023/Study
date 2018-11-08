> 调用某个方法后，我们需要程序过一段时间再去处理，就可以用`多线程阻塞`或`Timer定时器`来实现

# 方法一

> 多线程阻塞实现方式

#### MyTask

```java
package abc;

import java.util.concurrent.*;

public class MyTask implements Callable<Integer> {

    private int num;

    private int price;

    public MyTask(int num, int price) {
        this.num = num;
        this.price = price;
    }


    public int add(int m, int n) {
        System.out.println("执行运算..."+"\n");
        return m * n;
    }

    @Override
    public Integer call() throws Exception {
        //睡眠5秒
        Thread.sleep(5 * 1000L);
        return add(num,price);
    }
}
```

#### 测试类

```java
package abc;

import org.junit.Test;

import java.util.concurrent.FutureTask;

public class ThreadTest {

    @Test
    public void c() throws Exception {

        MyTask task = new MyTask(10,20);
        FutureTask<Integer> resultObject = new FutureTask<Integer>(task);
        new Thread(resultObject).start();
        //阻塞当前线程
        int result = resultObject.get();
        System.out.println(result);
    }
}
```



---

# 方法二

> 通过`Timer`定时器实现

#### TestTimerTask

```java
package abc;

import java.util.Date;
import java.util.TimerTask;

public class TestTimerTask extends TimerTask {

    private String username;

    private int age;

    public TestTimerTask(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @Override
    public void run() {
        // 处理业务逻辑
        System.out.println("开始处理.");
        System.out.println("大家好，我叫" + username + "，今年" + age + "岁");
        System.out.println("结束时间："+new Date().toLocaleString());
        System.gc();
        cancel();
    }
}
```

#### 测试类

```java
package abc;


import java.util.Date;
import java.util.Timer;

public class TimerTest {

    public static void main(String[] args) {
        System.out.println("开始时间："+new Date().toLocaleString());
        Timer timer = new Timer();
        System.out.println("一："+timer);
        timer.schedule(new TestTimerTask("张三",20),5000);
        System.out.println(timer);
    }
}
```

#### 输出结果

```bash
开始时间：2018-11-8 17:21:49
一：java.util.Timer@6acbcfc0
java.util.Timer@6acbcfc0
开始处理.
大家好，我叫张三，今年20岁
结束时间：2018-11-8 17:21:54

Process finished with exit code 0
```

#### 注意

* `Timer`测试不能用单元测试


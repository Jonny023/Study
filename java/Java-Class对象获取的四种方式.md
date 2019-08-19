# Class对象获取的四种方式

```java
package com.jonny;

/**
 * @description Class对象获取的四种方式
 * @author Jonny
 * @date 2019/8/19
 */
public class App {

    static {
        System.out.println("init class...");
    }
}

```

* 测试类

```java
package com.jonny;

import org.junit.Test;

/**
 * @author Jonny
 * @description
 * @date 2019年08月19日 21:09
 */
public class AppTest {

    @Test
    public void Hello() throws ClassNotFoundException {

        // 不会执行初始化
        Class clazz1 = App.class;

        // 创建实例对象会出发初始化
        Class clazz2 = new App().getClass().getClassLoader().loadClass("com.jonny.App");

        // 不会执行初始化
        Class clazz3 = App.class.getClassLoader().loadClass("com.jonny.App");

        // 完成初始化过程
        Class clazz4 = Class.forName("com.jonny.App");

    }
}
```

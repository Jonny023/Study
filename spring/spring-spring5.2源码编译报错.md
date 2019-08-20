* 环境说明
  * `idea 2018`
  * `jdk1.8_131`
  * `gradle-4.9`

* 错误提示

```
Error:(347, 51) java: 找不到符号
符号: 变量 CoroutinesUtils
位置: 类 org.springframework.core.ReactiveAdapterRegistry.CoroutinesRegistrar
```

* 解决方法，在自定义`module`中加入依赖

```groovy
compile project(":spring-instrument")
compile project(":spring-core-coroutines")
// 或者
compile files("../spring-instrument/build/libs/spring-instrument-5.2.0.BUILD-SNAPSHOT.jar")
compile files("../spring-core-coroutines/build/libs/spring-core-coroutines-5.2.0.BUILD-SNAPSHOT.jar")
```

### 配置

* 主配置类

```java
package com.jonny.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Lee
 * @Description
 * @Date 2019年07月14日 20:56
 */
@Configuration
@ComponentScan("com.jonny")
public class App {

}
```

* serviceimpl

```java
package com.jonny.service;

import org.springframework.stereotype.Service;

/**
 * @Author Lee
 * @Description
 * @Date 2019年07月14日 20:57
 */
@Service
public class UserServiceImpl {

	public UserServiceImpl() {
		System.out.println("实例化...");
	}
}
```

* 测试类

```java
import com.jonny.config.App;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author Lee
 * @Description
 * @Date 2019年07月14日 20:53
 */
public class Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(App.class);
		System.out.println(ioc);
	}
}
```

* 控制台输出

```bash
实例化...
org.springframework.context.annotation.AnnotationConfigApplicationContext@c038203, started on Sun Jul 14 22:06:17 CST 2019
```

*  [参考地址](https://github.com/spring-projects/spring-framework/issues/22859)

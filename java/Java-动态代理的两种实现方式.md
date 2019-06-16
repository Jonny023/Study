## 动态代理的两种实现方式

* JDK动态代理
* CGLIB动态代理

### 定义接口

```java
package com.example;

/**
 * @Author Lee
 * @Description
 * @Date 2019年06月16日 18:53
 */
public interface HelloService {

    String sayHello(String str);
}
```

### 实现

```java
package com.example;

/**
 * @Author Lee
 * @Description
 * @Date 2019年06月16日 18:53
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String str) {
        return "调用HelloImpl类:" + str;
    }
}
```

### JDK动态代理

```java
package com.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author Lee
 * @Description
 * @Date 2019年06月16日 18:51
 */
public class LogInvocationHandler implements InvocationHandler {

    private HelloService helloService;

    public LogInvocationHandler(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(helloService, args);
    }

    public static void main(String[] args) {
        // 2. 然后在需要使用Hello的时候，通过JDK动态代理获取Hello的代理对象。
        HelloService servie = (HelloService) Proxy.newProxyInstance(
                LogInvocationHandler.class.getClassLoader(), // 1. 类加载器
                new Class<?>[]{HelloService.class}, // 2. 代理需要实现的接口，可以有多个
                new LogInvocationHandler(new HelloServiceImpl()));// 3. 方法调用的实际处理者
        System.out.println(servie.sayHello("JDK动态代理"));
    }
}
```

## CGLIB动态代理

```java
package com.example;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author Lee
 * @Description
 * @Date 2019年06月16日 19:31
 */
public class CglibProxyInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invokeSuper(o, objects);
    }

    public static void main(String[] args) {
        CglibProxyInterceptor cglibProxy = new CglibProxyInterceptor();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloServiceImpl.class);
        enhancer.setCallback(cglibProxy);

        HelloService service = (HelloService) enhancer.create();
        System.out.println(service.sayHello("CGLIB动态代理"));
    }
}
```

> cglib动态代理依赖包：`asm`和`cglib`，当我在使用`asm-3+`和`cglib-3+`时报错，将`cglib`版本改为`cglib-2+`没问题了，我这里用的`cglib-2.2.2`

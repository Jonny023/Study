# Aop动态给对象赋值

* 注解类

```java
package com.example.springaop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-01-19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auto {
}

```

* aop类
```java
package com.example.springaop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-01-19
 */
@Aspect
@Component
public class UserAspect {

    /**
     * 定义切入点
     * 通过@Pointcut注解声明频繁使用的切点表达式
     */
    @Pointcut("@annotation(com.example.springaop.annotation.Auto)")
    public void cut() {}

    /**
     * @description 使用环绕通知
     */
    @Around("cut()")
    public Object doAroundGameData(ProceedingJoinPoint pjp) {
        try {

            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            String uid = requestAttributes.getRequest().getParameter("uid");

            Object target = pjp.proceed();
            //获取父类属性
            Field field = target.getClass().getSuperclass().getDeclaredField("userId");
            field.setAccessible(true);//如果是私有的 先要设置可访问

            //给指定的属性赋值
            field.set(target, uid);
            return target;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}

```

* 公共类

```java
package com.example.springaop.entity;

import lombok.Data;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-01-19
 */
@Data
public class Base {

    private String userId;
}
```

* 子类

```java
package com.example.springaop.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-01-19
 */
@Data
public class Order extends Base {

    private String orderId;
    private Date createTime;
}
```

* 控制器

```java
package com.example.springaop.controller;

import com.example.springaop.annotation.Auto;
import com.example.springaop.entity.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-01-19
 */
@RestController
public class MainController {

    @Auto
    @RequestMapping("fun1")
    public Order fun1(Order order) {
        return order;
    }

    @RequestMapping("fun2")
    public Order fun2(Order order) {
        return order;
    }
}
```

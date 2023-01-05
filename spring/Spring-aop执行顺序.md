## spring aop

> spring4和spring5执行顺序有些不一样

### 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### aop配置

```java
package com.example.springbootredis.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BizAspect {

    @Pointcut("execution (* com.example.springbootredis.service.*.*(..))")
    public void init() {

    }

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    @Before("init()")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info("@Before前置通知");
    }

    /**
     * 后置通知：方法正常执行后，有返回值，执行该后置通知：如果该方法执行出现异常，则不执行该后置通知
     *
     * @param joinPoint
     * @param returnVal
     */
    @AfterReturning(value = "init()", returning = "returnVal")
    public void afterReturningAdvice(JoinPoint joinPoint, Object returnVal) {
        log.info("@AfterReturning后置返回");
    }

    @After("init()")
    public void afterAdvice(JoinPoint joinPoint) {
        log.info("@After后置通知");
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("init()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("@Around环绕通知中的前置通知");
        Object returnVale = joinPoint.proceed();
        log.info("@Around环绕通知中的后置通知");
        return returnVale;
    }

    /**
     * 异常通知：方法出现异常时，执行该通知
     */
    @AfterThrowing(value = "init()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
        log.info("@AfterThrowing后置异常：" + ex.getMessage());
    }

}
```



### 正常执行顺序

#### spring4

```shell
2023-01-05 10:32:45.995 c.example.springbootredis.aop.BizAspect  : @Around环绕通知中的前置通知
2023-01-05 10:32:45.995 c.example.springbootredis.aop.BizAspect  : @Before前置通知
2023-01-05 10:32:45.997 c.e.springbootredis.service.UserService  : 业务：保存用户
2023-01-05 10:32:45.997 c.example.springbootredis.aop.BizAspect  : @Around环绕通知中的后置通知
2023-01-05 10:32:45.997 c.example.springbootredis.aop.BizAspect  : @After后置通知
2023-01-05 10:32:45.997 c.example.springbootredis.aop.BizAspect  : @AfterReturning后置返回
```

#### spring5

```shell
2023-01-05 10:16:41.821 c.example.springbootredis.aop.BizAspect  : @Around环绕通知中的前置通知
2023-01-05 10:16:41.821 c.example.springbootredis.aop.BizAspect  : @Before前置通知
2023-01-05 10:16:41.825 c.e.springbootredis.service.UserService  : 业务：保存用户
2023-01-05 10:16:41.825 c.example.springbootredis.aop.BizAspect  : @AfterReturning后置返回
2023-01-05 10:16:41.825 c.example.springbootredis.aop.BizAspect  : @After后置通知
2023-01-05 10:16:41.825 c.example.springbootredis.aop.BizAspect  : @Around环绕通知中的后置通知
```



### 异常执行顺序

#### spring4

```shell
2023-01-05 10:31:32.140 c.example.springbootredis.aop.BizAspect  : @Around环绕通知中的前置通知
2023-01-05 10:31:32.140 c.example.springbootredis.aop.BizAspect  : @Before前置通知
2023-01-05 10:31:32.141 c.e.springbootredis.service.UserService  : 业务：保存用户
2023-01-05 10:31:32.142 c.example.springbootredis.aop.BizAspect  : @After后置通知
2023-01-05 10:31:32.142 c.example.springbootredis.aop.BizAspect  : @AfterThrowing后置异常：/ by zero
```

#### spring5

```shell
2023-01-05 10:14:03.539 c.example.springbootredis.aop.BizAspect  : @Around环绕通知中的前置通知
2023-01-05 10:14:03.540 c.example.springbootredis.aop.BizAspect  : @Before前置通知
2023-01-05 10:14:03.543 c.e.springbootredis.service.UserService  : 业务：保存用户
2023-01-05 10:14:03.544 c.example.springbootredis.aop.BizAspect  : @AfterThrowing后置异常：/ by zero
2023-01-05 10:14:03.544 c.example.springbootredis.aop.BizAspect  : @After后置通知
```



### 总结

```sh
# 正常顺序
spring4:@Around==>@Before==>业务==>@Around==>@After==>@AfterReturning
spring5:@Around==>@Before==>业务==>@AfterReturning==>@After==>@Around

# 异常顺序
spring4:@Around==>@Before==>业务==>@After==>@AfterThrowing
spring5:@Around==>@Before==>业务==>@AfterThrowing==>@After
```


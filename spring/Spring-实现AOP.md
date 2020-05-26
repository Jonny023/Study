### 手动实现AOP

* 定义接口

```java
package com.example.redisson;

public interface IUserDao {

    String register(String username, String  password);

}
```

* 实现
```java
package com.example.redisson;

public class UserDao implements IUserDao {

    @Override
    public String register(String username, String password) {
        System.out.println("用户注册成功！");
        return "注册成功";
    }

}
```

* 切面

```java
package com.example.redisson;
import org.aspectj.lang.ProceedingJoinPoint;

public class AroundApp {

    public void beginTransaction() {
        System.out.println("[前置通知] 开启事务..");
    }
    public void commit() {
        System.out.println("[后置通知] 提交事务..");
    }
    public void afterReturing(){
        System.out.println("[返回后通知]");
    }
    public void afterThrowing(){
        System.out.println("[异常通知]");
    }
    public void arroud(ProceedingJoinPoint p) throws Throwable{
        System.out.println("[环绕前：]");
        p.proceed();             // 执行目标方法
        System.out.println("[环绕后：]");
    }
}
```

* 配置xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="userDao" class="com.example.redisson.UserDao"/>

    <bean id="transactionalApp" class="com.example.redisson.AroundApp"/>

    <aop:config>
        <!-- 切入点表达式定义 -->
        <aop:pointcut expression="execution(* com.example.redisson.*Dao.*(..))" id="transactionPointcut"/>
        <!-- 切面配置 -->
        <aop:aspect ref="transactionalApp">
            <!-- 【环绕通知】 -->
            <aop:around method="arroud" pointcut-ref="transactionPointcut"/>
            <!-- 【前置通知】 在目标方法之前执行 -->
            <aop:before method="beginTransaction" pointcut-ref="transactionPointcut" />
            <!-- 【后置通知】 -->
            <aop:after method="commit" pointcut-ref="transactionPointcut"/>
            <!-- 【返回后通知】 -->
            <aop:after-returning method="afterReturing" pointcut-ref="transactionPointcut"/>
            <!-- 异常通知 -->
            <aop:after-throwing method="afterThrowing" pointcut-ref="transactionPointcut"/>
        </aop:aspect>
    </aop:config>
</beans>
```

* 测试

```java
package com.example.redisson;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootTest(classes = {RedissionApplication.class})
class RedissionApplicationTests {


    @Test
    void exe2() {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring.xml");
        IUserDao userDao = (IUserDao) ioc.getBean("userDao");
        System.out.println(userDao.getClass());
        userDao.register("admin", "123456");
    }
}

```

* 输出结果

```bash
class com.sun.proxy.$Proxy82
[环绕前：]
[前置通知] 开启事务..
用户注册成功！
[环绕后：]
[后置通知] 提交事务..
[返回后通知]

```

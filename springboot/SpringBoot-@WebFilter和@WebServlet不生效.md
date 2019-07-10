# SpringBoot-@WebFilter和@WebServlet不生效

> 原因：包没被扫描到

### 解决方法，在主类上加上注解`@ServletComponentScen`
```java
package com.jonny.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
```

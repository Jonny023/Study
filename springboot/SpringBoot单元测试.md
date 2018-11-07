## 在测试类中读取某个`application-`开头的`properties`或`yaml`中的属性

# 命名规则

* 必须以`application-`开头
  + `application-dev.properties`
  + `application-test.properties`
  + `application-dev.yml`
  + `application-dev.yml`

> 通过`@ActiveProfiles`来指定使用哪个文件

## 例子

```java
package com.atgenee.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") //指定使用application-test.yml
public class TestApplicationTests {

    @Value("${user.first-name}")
    private String firstName;

    @Value("${user.weight}")
    private Integer weight;

    @Test
    public void hei() {
        System.out.println(firstName);
        System.out.println(weight);
    }

}
```

______

## `@TestPropertySource`

* 加载指定配置文件
* 可以是`properties`文件，也可以是`yaml`

## 例子

```java
package com.atgenee.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.config.location = classpath:test.properties" })
public class TestApplicationTests {

    @Value("${user.first-name}")
    private String firstName;

    @Value("${user.weight}")
    private Integer weight;

    @Test
    public void hei() {
        System.out.println(firstName);
        System.out.println(weight);
    }

}
```


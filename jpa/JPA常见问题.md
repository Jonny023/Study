### 常见报错

* 公共类里面注入同一个类，比如`baseRepository`不能用`@Resource`注解，要改成`@Autowired`

### 单元测试

* 报错

  > Not a managed type: class java.lang.Object

```java
package cn.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "cn.com.entity")
//意思是没找到Repository，添加这个注解
@EnableJpaRepositories(basePackages = {"cn.com.repository"}) 
@EnableTransactionManagement
public class StatTest {

    public static void main(String[] args) {
        SpringApplication.run(StatTest.class, args);
    }
}

```


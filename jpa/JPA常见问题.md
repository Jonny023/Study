### 常见报错

* 公共类里面注入同一个类，比如`baseRepository`不能用`@Resource`注解，要改成`@Autowired`

### 1、单元测试

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

### 2、JPA在`Respitory`中使用`@Query`问题

> @Query("select new User(xx,xx) from User where xxx=?1")这种返回数据封装到实体类，这个类不能是数据库`@Entity`标注的类，否则可能导致:`column 'id' not found`

* 返回自定类定义为单纯的VO或者Map
* 查询列不要用*,名具体列
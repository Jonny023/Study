# Springboot常见问题

* `springboot2`集成`druid`出现`(*) property for user to setup`

```java
package com.jonny.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
public class DruidDBConfig {

    // 这里必须为@Bean(destroyMethod = "close", initMethod = "init")，不然数据源为空
    @Bean(destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }
}
```

### 版本变化

* springboot 2.4之后，如果多个环境配置放在同一个文件中，可以通过这种方式区分

```yaml
spring:
  profiles:
    active: uat
---
server:
  port: 801
spring:
  config:
    activate:
      on-profile: dev

---
server:
  port: 802
spring:
  config:
    activate:
      on-profile: uat
```

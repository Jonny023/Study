## SpringBoot动态更新数据库url

> 项目在运行过程中，动态更新数据库url,username,password，实现可以通过druid连接池的restart()方法实现，配置数据源保存在服务器本地用户目录下

### 准备

```sql
CREATE DATABASE `test1`;
CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

INSERT INTO test2.`user` (id, username) VALUES(1, 'zhangsan');



CREATE DATABASE `test1`;
CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
INSERT INTO test2.`user` (id, username) VALUES(1, 'lisi');
```

### 1.依赖

```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
compileOnly 'org.projectlombok:lombok'
runtimeOnly 'mysql:mysql-connector-java'
annotationProcessor 'org.projectlombok:lombok'

implementation 'com.baomidou:mybatis-plus-boot-starter:3.5.2'
implementation 'com.alibaba:druid-spring-boot-starter:1.1.10'
```

### 2.yaml配置

> 主要是配置mybatis，url只是为了站位，不然要启动要报错

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://{IP}:{port}/{database}
    username: root
    password: root

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
```

### 3.数据源配置

```java
package app.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSourceBuilder().build();
    }

}
```

### 4.启动类

```java
package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringBootCustomDynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCustomDynamicApplication.class, args);
    }

}
```

### 5.工具类

* DataSourceUtils

```java
package app.utils;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

/**
 *  更新数据源
 */
@Slf4j
public class DataSourceUtils {

    /**
     *  将数据库配置文件存放到用户目录下
     */
    public static String PATH = System.getProperty("user.home") + "/db-default.properties";

    /**
     * 刷新数据源
     *
     * @return
     * @throws SQLException
     */
    public static String refresh(String path) throws SQLException, IOException {
        Properties prop = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(path))) {
            prop.load(is);
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            String url = prop.getProperty("url");
            String driverClassName = prop.getProperty("driverClassName");
            if (StringUtils.hasText(username) || StringUtils.hasText(url) || StringUtils.hasText(password) || StringUtils.hasText(driverClassName)) {
                DruidDataSource master = (DruidDataSource) SpringUtils.getBean("dataSource");
                if (StringUtils.hasText(username)) {
                    master.setUsername(username);
                }
                if (StringUtils.hasText(password)) {
                    master.setPassword(password);
                }
                if (StringUtils.hasText(url)) {
                    //com.alibaba.druid.pool.DruidAbstractDataSource.inited为true时报错，导致无法修改url
                    //这里先调用一次restart()方法将inited重置为false，再setUrl()
                    master.restart();
                    master.setUrl(url);
                }
                if (StringUtils.hasText(driverClassName)) {
                    master.setDriverClassName(driverClassName);
                }
                master.restart();
                log.info("mysql: {}", url);
            }
            return "success";
        }
    }
}

```

* SpringUtils

```java
package app.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringUtils.context = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * 重启加载之前保存的配置文件配置的数据库信息
     */
    @PostConstruct
    void init() {
        try {
            DataSourceUtils.refresh(DataSourceUtils.PATH);
        } catch (Exception e) {
            log.error("数据源加载失败", e);
        }
    }
}
```

### 5.实体类

* User

```java
package app.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
}
```

* DataSourceRequestVO

```java
package app.domain.vo;

import lombok.Data;

@Data
public class DataSourceRequestVO {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

}
```

### 7.Mapper

* UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="app.mapper.UserMapper">

    <!--查询所有用户信息-->
    <select id="list" resultType="app.domain.entity.User">
        select * from user
    </select>

</mapper>
```

* UserMapper.java

```java
package app.mapper;

import app.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> list();
}


```

### 8.数据源更新接口

```java
package app.controller;

import app.domain.vo.DataSourceRequestVO;
import app.utils.DataSourceUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@RestController
public class DataSourceController {

    /**
     * 刷新数据源
     * https://icode.best/i/00350443878337
     *
     * @return
     * @throws SQLException
     */
    @SneakyThrows
    @PostMapping("/db/save")
    public Object save(@RequestBody DataSourceRequestVO vo) {
        if (vo != null) {
            Properties prop = new Properties();
            if (StringUtils.hasText(vo.getUsername()) || StringUtils.hasText(vo.getUrl()) || StringUtils.hasText(vo.getPassword()) || StringUtils.hasText(vo.getDriverClassName())) {
                if (StringUtils.hasText(vo.getUsername())) {
                    prop.setProperty("username", vo.getUsername());
                }
                if (StringUtils.hasText(vo.getPassword())) {
                    prop.setProperty("password", vo.getPassword());
                }
                if (StringUtils.hasText(vo.getUrl())) {
                    prop.setProperty("url", vo.getUrl());
                }
                if (StringUtils.hasText(vo.getDriverClassName())) {
                    prop.setProperty("driverClassName", vo.getDriverClassName());
                }
                prop.store(Files.newOutputStream(Paths.get(DataSourceUtils.PATH)), "");
                DataSourceUtils.refresh(DataSourceUtils.PATH);
            }
        }
        return "url: " + Optional.ofNullable(vo).orElse(new DataSourceRequestVO()).getUrl();
    }

}
```

### 9.测试接口

```java
package app.controller;

import app.domain.entity.User;
import app.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/user/list")
    public Object list() {
        return userMapper.list();
    }

    /**
     *  正常提交
     * @return
     */
    @Transactional
    @PostMapping("/save")
    public String save() {
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        userMapper.insert(user);
        return "success";
    }

    /**
     * 异常回滚
     * @return
     */
    @Transactional
    @PostMapping("/saveRollback")
    public String saveRollback() {
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        userMapper.insert(user);
        throw new RuntimeException();
    }
}


```

### 10.测试

```JSON
// 切换到test1数据库
POST localhost:8080/db/save
{
    "username": "root",
    "password": "root",
    "url": "jdbc:mysql://localhost:3306/test1"
}

//查询用户
GEE localhost:8080/user/list
//正常提交测试
localhost:8080/save
//异常回滚测试
localhost:8080/saveRollback
            

//===============================================================
// 切换到test2数据库
POST localhost:8080/db/save
{
    "username": "root",
    "password": "root",
    "url": "jdbc:mysql://localhost:3306/test2"
}

//正常提交测试
localhost:8080/save
//异常回滚测试
localhost:8080/saveRollback
//查询用户
GEE localhost:8080/user/list
```

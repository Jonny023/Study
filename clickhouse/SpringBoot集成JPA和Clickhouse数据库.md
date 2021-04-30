# SpringBoot集成JPA和Clickhouse数据库

> 改造jpa默认的批量插入方法

### 1、文件依赖及配置

* `pom.xml`依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>ru.yandex.clickhouse</groupId>
	<artifactId>clickhouse-jdbc</artifactId>
	<version>0.3.0</version>
</dependency>
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<version>1.18.18</version>
</dependency>
<dependency>
	<groupId>com.google.guava</groupId>
	<artifactId>guava</artifactId>
	<version>20.0</version>
</dependency>
```

* `application.yml`配置，要用`jpa`的批量插入必须设置`batch_size`等参数，jpa没有提供clickhouse的方言，clickhouse支持mysql的语法，所以直接用mysql的方言，方言可以在yml配置文件里面配置，也可以在后面的`配置类`里面配置，必能开启jpa的自动建表策略，主键也不能指定，否则报错

```yaml
spring:
  jackson:
    time-zone: GMT+8
  datasource:
    ck:
      driver-class-name: ru.yandex.clickhouse.ClickHouseDriver
      jdbc-url: jdbc:clickhouse://10.113.74.246:8123/test
      username: default
      password: 123456
      connection-timeout: 20000
      maximum-pool-size: 5
  jpa:
    hibernate:
#      naming:
#        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
#        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true
    open-in-view: false
#    database-platform: org.hibernate.dialect.MySQL8Dialect
    properties:
      hibernate:
        jdbc:
          batch_size: 500
          batch_versioned_data: true
        order_inserts: true
        order_updates: true
```

### 2、重写批量插入实现

* interface接口

```java
package com.example.springbootclickhouse.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    <S extends T> Iterable<S> batchInsert(Iterable<S> var1);
    <S extends T> Iterable<S> batchUpdate(Iterable<S> var1);

    int delById(String tableName, Long id);
}
```

* 实现

```java
package com.example.springbootclickhouse.base;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Iterator;

public class BaseRepositoryImpl <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private static final int BATCH_SIZE = 500;
    private EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> Iterable<S> batchInsert(Iterable<S> var1) {
        Iterator<S> iterator = var1.iterator();
        int index = 0;
        while (iterator.hasNext()){
            entityManager.persist(iterator.next());
            index++;
            if (index % BATCH_SIZE == 0){
                entityManager.flush();
                entityManager.clear();
            }
        }
        if (index % BATCH_SIZE != 0){
            entityManager.flush();
            entityManager.clear();
        }
        return var1;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> Iterable<S> batchUpdate(Iterable<S> var1) {
        Iterator<S> iterator = var1.iterator();
        int index = 0;
        while (iterator.hasNext()){
            entityManager.merge(iterator.next());
            index++;
            if (index % BATCH_SIZE == 0){
                entityManager.flush();
                entityManager.clear();
            }
        }
        if (index % BATCH_SIZE != 0){
            entityManager.flush();
            entityManager.clear();
        }
        return var1;
    }

    @Override
    public int delById(String tableName, Long id) {
        Query nativeQuery = entityManager.createNativeQuery("alter table " + tableName + " delete where id=?");
        nativeQuery.setParameter(1, id);
        int count = nativeQuery.executeUpdate();
        return count;
    }
}
```

### 3、数据源核心配置类

> 由于clickhouse不支持事务，所以就不需要启动事务管理

```java
package com.example.springbootclickhouse.config;

import com.example.springbootclickhouse.base.BaseRepositoryImpl;
import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EntityScan(basePackages = "com.example.springbootclickhouse.entity")
@EnableJpaRepositories(
        basePackages = "com.example.springbootclickhouse.repository",
        entityManagerFactoryRef = "ckEntityManagerFactoryBean",
        transactionManagerRef = "ckTransactionManager",
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class JpaClickHouseConfig {

    /**
     *  数据源名称为：
     *
     * @return
     */
    @Bean(name = "ckDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ck")
    public DataSource ckDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Resource(name = "ckDataSource")
    private DataSource dataSource;

    // JPA扩展配置
    @Resource
    private JpaProperties jpaProperties;

    // 实体管理工厂
    @Resource
    private EntityManagerFactoryBuilder factoryBuilder;

    @Resource
    private HibernateProperties hibernateProperties;

    /**
     * 配置第二个实体管理工厂的bean
     *
     * @return
     */
    @Bean(name = "ckEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        return factoryBuilder.dataSource(dataSource)
                .properties(getVendorProperties())
                .packages("com.example.springbootclickhouse.entity")
                .persistenceUnit("ckPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        Map<String, String> properties = jpaProperties.getProperties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        return hibernateProperties.determineHibernateProperties(properties, new HibernateSettings());
    }

    @Bean(name = "ckEntityManager")
    public EntityManager entityManager() {
        return entityManagerFactoryBean().getObject().createEntityManager();
    }

    /**
     * jpa事务管理
     * @return
     */
    @Bean(name = "ckTransactionManager")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return jpaTransactionManager;
    }
}
```

### 4、测试

* 数据库建表

```sql
CREATE TABLE test.sys_user
(

    `id` Int64,

    `username` String,

    `addr` String,

    `create_time` DateTime
)
ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192
```

* 实体类

> 测试用jdk util包下得Date报错，换成了Timestamp，clickhouse不能指定主键策略

```
package com.example.springbootclickhouse.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  用户
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_user")
public class User {

    @Id
    @Column(name = "id")
    // 不能设置主键生成策略
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "addr")
    private String addr;

    @Column(name = "create_time")
    private Timestamp createTime;
}
```

* UserRepository类

> `clickhouse`的修改和新增语法和其他sql不一样需要自己实现，JPA没有提供支持
>
> 修改：`alter table tableName update x1=abc,x2=bcd where x3 = xxx`
>
> 删除：`alter table tableName delete where id = 1`

```java
package com.example.springbootclickhouse.repository;

import com.example.springbootclickhouse.base.BaseRepository;
import com.example.springbootclickhouse.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    @Query(value = "alter table sys_user delete where id=:id", nativeQuery = true)
    int delUser(@Param("id") Long id);
}
```

* Controller类

```java
package com.example.springbootclickhouse.controller;

import com.example.springbootclickhouse.base.BaseRepository;
import com.example.springbootclickhouse.entity.User;
import com.example.springbootclickhouse.repository.UserRepository;
import com.google.common.collect.Lists;
import java.sql.Timestamp;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Resource
  private JdbcTemplate jdbcTemplate;
  @Resource
  private UserRepository userRepository;
  @Resource
  private BaseRepository<User, Long> baseRepository;

  @GetMapping("/save")
  public String save() {
    User user = new User().setId(1L).setUsername("admin").setAddr("China1");
    userRepository.save(user);
    return "save success";
  }

  @GetMapping("/batch")
  public String batch() {
    long start = System.currentTimeMillis();
    List<User> users = new ArrayList<>();
    User user = null;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    for (long i = 1; i <= 100000; i++) {
      user = new User().setId(i).setUsername("admin_" + i).setAddr("China_" + i).setCreateTime(now);
      users.add(user);
    }
    //按每500一组分割
    List<List<User>> parts = Lists.partition(users, 500);
    parts.stream().forEach(list -> {
      userRepository.batchInsert(list);
    });
    double total = (System.currentTimeMillis() - start) / 1000;
    return "batch save success, time: " + total + "s";
  }

  @GetMapping("/list")
  public List<User> list() {
    return userRepository.findAll();
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id") Long id) {
    int i = baseRepository.delById("sys_user", id);
    System.out.printf("影响行数：%d\n", i);
    return "delete success";
  }
}
```


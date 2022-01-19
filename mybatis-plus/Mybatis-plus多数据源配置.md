# Mybatis-plus多数据源配置

## 1.yaml配置

```yaml
spring:
  datasource:
    master:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/master?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false&allowMultiQueries=true
      username: root
      password: 123456
    slave:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/slave?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false&allowMultiQueries=true
      username: root
      password: 123456

mybatis-plus:
  configuration:
    # 是否将sql打印到控制面板
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    # 是否开启自动驼峰命名规则（camel case）映射
    mapUnderscoreToCamelCase: true
```

## 2.配置类

### 2.1 主数据源

```java
package com.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = MasterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

    public static final String PACKAGE = "com.demo.master.mapper";
    public static final String MASTER = "masterDataSource";
    private static final String MAPPER_LOCATION = "classpath:master/*Mapper.xml";

    @Primary
    @Bean(name = MASTER, initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DruidDataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier(MASTER) DataSource masterDataSource)
            throws Exception {
        //使用 MybatisSqlSessionFactoryBean 而不是 SqlSessionFactoryBean
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MasterDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    @Primary
    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

### 2.2 从数据源

```java
package com.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = SlaveDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "slaveSqlSessionFactory")
public class SlaveDataSourceConfig {

    public static final String PACKAGE = "com.demo.slave.mapper";
    public static final String SLAVE = "slaveDataSource";
    private static final String MAPPER_LOCATION = "classpath*:slave/*.xml";

    @Bean(name = SLAVE, initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DruidDataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "slaveTransactionManager")
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(slaveDataSource());
    }

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier(SLAVE) DataSource slaveDataSource)
            throws Exception {
        //使用 MybatisSqlSessionFactoryBean 而不是 SqlSessionFactoryBean
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(slaveDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(SlaveDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
```

## 坑

* `com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration#sqlSessionFactory`类中的bean里面说明了创建SqlSessionFactory bean时，自定义多数据源使用 `MybatisSqlSessionFactoryBean` 而不是 `SqlSessionFactoryBean`，否则无法调用mybatis的api，会如下错误

  * ```java
    org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): com.demo.TestMapper.selectList
    ```

    


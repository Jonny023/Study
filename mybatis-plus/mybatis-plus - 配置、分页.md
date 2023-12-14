# mybatis-plus - 配置和分页

* yaml配置

```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    auto-mapping-behavior: full
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:mapper/**/*Mapper.xml
  global-config:
    # 逻辑删除配置
    db-config:
      # 删除前
      logic-not-delete-value: 0
      # 删除后
      logic-delete-value: 1
```

* xml

```xml
<properties>
	<mybatis-plus.version>3.2.0</mybatis-plus.version>
</properties>

<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus.version}</version>
</dependency>
```

* 使配置生效

> 注册`bean`对象`PaginationInterceptor`，别忘记添加`@Configuration`或`@Component`

```java
package com.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor().setDialectType("mysql");
    }
}
```

* 启动类scan

```java
@MapperScan("com.domain.mapper")
public class App {}
```

## 用法2

```java
PageInfo<XxxVO> pageInfo = PageMethod.startPage(param.getPageNum(), param.getPageSize()).doSelectPageInfo(() ->
    getBaseMapper().pageList()
);
```

## 多表联合查询导致返回数据记录数和数据条数不一直

> 分页数据不一致，多表left join导致分页数据不一致

```java
// 默认开启了sql优化，只会查主表的记录数，需要设置关闭count时的优化
page.setOptimizeCountSql(false);

// 分页查询如果不用默认的查询记录数，可以设置为关闭，这种关闭场景适合如app上滑加载更多
page.setSearchCount(false);
```

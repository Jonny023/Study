# mybatis-plus 踩坑

## 坑点1

> 查询超出的页码返回了第一页或者最后一页的数据

### 依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.6</version>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.1</version>
</dependency>
```

### 配置

```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath:mapper/*.xml
```

### 分页配置

> 下面为手动配置分页插件，如果是用的starter可以在配置文件中进行配置

```java
import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MybatisPlusConfiguration {

    @Bean
    public PageInterceptor mybatisPlusInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 4.0.0 以后版本可以不设置该参数 5.0 以前的版本使用
        // properties.setProperty("dialect", "mysql");

        properties.setProperty("reasonable", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
```

### 接口方法

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/list")
    public PageInfo<User> list(@RequestBody UserPageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize());
        List<User> users = userMapper.listPage();
        return new PageInfo<>(users);
    }
}
```

### 坑点说明

```java
// 分页参数合理化：就是假如传递的是不存在的页码，他会取默认的最小或者最大的页码
// 默认值为false，为true时【页码<=0】返回第一页数据，【页码>总页数】返回最后一页数据
properties.setProperty("reasonable", "true");

// 有时可能需要页码超过或者小于总页数时不返回则需要将reasonable设置为false
// 不修改全局配置也可以局部进行控制page.setReasonable(false); 当【pageSize<=0】返回所有
// 也可以这样设置PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize(), false);

// PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize(), true, false, null);
// 当【pageSize=0】返回所有，【pageSize<0】会返回空集合

// 下面这个方法会导致当【pageSize=0】时返回所有数据，【pageSize<0】返回空集合
PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize(), true, false, false);
```



## 坑点2

> mybatis-plu自带分页多表联查导致分页和总数据对应不上

### 依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.6</version>
</dependency>
```

### 配置类

```java
@Configuration
public class MybatisPlusConfiguration1 {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求
        // 默认false 为false时 会返回空数据
        paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setOptimizeJoin(true);

        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInterceptor);
        return mybatisPlusInterceptor;
    }
}
```

### 分页方法

> 需要设置关闭coun优化

```java
@RequestMapping("/userRoleList")
public IPage<Map<String, Object>> listUserRole(@RequestBody UserPageDTO pageDTO) {
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<Map<String, Object>> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageDTO.getPage(), pageDTO.getPageSize());
    // 关闭count优化
    page.setOptimizeCountSql(false);
    IPage<Map<String, Object>> pageResult = userMapper.listPageUserDefault(page);
    return pageResult;
}
```

* 关闭count查询：`page.setSearchCount(false)`
* 查询所有数据：`page.setSize(-1)`
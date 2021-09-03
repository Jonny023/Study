# Mybatis PageHelper分页

### 1.依赖

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.20</version>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.2.1</version>
</dependency>
```

2.配置文件

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/xx?serverTimezone=Asia/Shanghai&useAffectedRows=true&useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: root
    initial-size: 2
    min-idle: 5
    max-active: 10
    max-wait: 5000
    validation-query: SELECT 1
    test-on-borrow: false
    test-while-idle: true
    time-between-eviction-runs-millis: 18800
    
#mybatis的相关配置
mybatis:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  #mapper配置文件
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: xx.entity
```



### 3.配置类

> 用starter以来无需手动注册配置类

```java
package com.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DruidDataSourceConfig {

    private Logger LOG = LoggerFactory.getLogger(DruidDataSourceConfig.class);

    @Value("${mybatis.mapper-locations}")
    private String location;

    @Primary
    @Bean(value = "dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration configuration() {
        return new org.apache.ibatis.session.Configuration();
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager slaveTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    public PageInterceptor pageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, org.apache.ibatis.session.Configuration configuration) throws Exception {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(location));
        sessionFactoryBean.setConfiguration(configuration);

        //设置MyBatis分页插件
        PageInterceptor pageInterceptor = this.pageInterceptor();
        sessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});
        return sessionFactoryBean.getObject();
    }

}
```

### 3.分页数据封装

```java
package com.base.vo;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("分页返回数据")
public class PageVO<T> {

    @ApiModelProperty("当前页码")
    private Integer pageNo;

    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

    @ApiModelProperty("每页显示条数")
    private Long total;

    @ApiModelProperty("数据集合")
    private List<T> list;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public static <T> PageVO build(PageInfo page) {
        PageVO<T> vo = new PageVO<>();
        vo.setList(page.getList());
        vo.setPageNo(page.getPageNum());
        vo.setPageSize(page.getPageSize());
        vo.setTotal(page.getTotal());
        return vo;
    }
}
```

### 3.代码

```java
public PageVO list(TaskPageQueryForm form) {
    PageHelper.startPage(form.getPageNo(), form.getPageSize());
    PageHelper.orderBy(" create_time desc");
    List<TaskPageListVO> list = taskMapper.list(form);
    PageInfo pageInfo = new PageInfo(list);
    return PageVO.build(pageInfo);
}
```


# springboot多数据源启动报错

> 问题描述：循环依赖问题`Requested bean is currently in creation: Is there an unresolvable circular reference?`

```bash
Error creating bean with name 'ckDataSource' defined in class path resource [cn/com/geely/config/JpaClickHouseConfig.class]: Initialization of bean failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker': Invocation of init method failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'defaultDataSourceConfig': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'dataSource': Requested bean is currently in creation: Is there an unresolvable circular reference?
```

> 原因分析: DataSource还未被spring托管就执行了下面的@Resource导致

```java
/**
 *  数据源名称为：
 *
 * @return
 */
@Primary
@Bean(name = "dataSource")
@ConfigurationProperties(prefix = "spring.datasource")
public DataSource dataSource() {
    return DataSourceBuilder.create().build();
}

@Lazy
@Resource(name = "dataSource")
private DataSource dataSource;
```

> 解决方法：注入的属性上添加`@Lazy`延迟加载注解，问题解决
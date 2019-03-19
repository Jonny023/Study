# 报错

> 错误提示

```
nested exception is java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
```

> 解决方法

```java
@Configuration
public class ESConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
```

# 整合

> 依赖

```xml
<!--ElasticSearch-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
<!--需要引入transport-netty3-client，否则会启动报错-->
<dependency>
    <groupId>org.elasticsearch.plugin</groupId>
    <artifactId>transport-netty3-client</artifactId>
    <version>5.6.10</version>
</dependency>
```

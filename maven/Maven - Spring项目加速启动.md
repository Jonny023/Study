## Maven加速spring项目启动速度

> 如果是多module项目，则每个模块都需要引入这个依赖

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context-indexer</artifactId>
  <optional>true</optional>
</dependency>
```

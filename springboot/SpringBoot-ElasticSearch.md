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

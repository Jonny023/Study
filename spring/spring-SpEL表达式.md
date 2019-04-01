* 设置默认为空

```properties
${jdbc.url:}
```

* 类中

```java
@Value("${testWeb.host}") // 注入属性  
private String host;  
@Value("${testWeb.startpagenumber:1}") // 设置默认值  
private Integer startPageNumber;  
@Value("${testWeb.endpagenumber: #{T(java.lang.Integer).MAX_VALUE}}") // 使用SpEL设置默认值  
private Integer endPageNumber;   
```
